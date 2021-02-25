package com.jxs.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jxs.product.constant.ProductConstant;
import com.jxs.product.dao.PmsAttrAttrgroupRelationDao;
import com.jxs.product.dao.PmsAttrGroupDao;
import com.jxs.product.dao.PmsCategoryDao;
import com.jxs.product.entity.PmsAttrAttrgroupRelationEntity;
import com.jxs.product.entity.PmsAttrGroupEntity;
import com.jxs.product.entity.PmsCategoryEntity;
import com.jxs.product.service.PmsCategoryService;
import com.jxs.product.vo.AttrGroupRelationVo;
import com.jxs.product.vo.AttrRespVo;
import com.jxs.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.Query;

import com.jxs.product.dao.PmsAttrDao;
import com.jxs.product.entity.PmsAttrEntity;
import com.jxs.product.service.PmsAttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsAttrService")
@Transactional
public class PmsAttrServiceImpl extends ServiceImpl<PmsAttrDao, PmsAttrEntity> implements PmsAttrService {


    @Autowired
    private PmsAttrAttrgroupRelationDao relationDao;
    @Autowired
    PmsAttrGroupDao attrGroupDao;

    @Autowired
    PmsCategoryDao categoryDao;

    @Autowired
    PmsCategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                new QueryWrapper<PmsAttrEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @param pmsAttr
     */
    @Override
    public void saveAttr(AttrVo pmsAttr) {
        //保存自己
        PmsAttrEntity attrEntity = new PmsAttrEntity();
        BeanUtils.copyProperties(pmsAttr, attrEntity);
        this.save(attrEntity);

        //保存关联关系
        if (ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() == attrEntity.getAttrType() && pmsAttr.getAttrGroupId() != null) {
            PmsAttrAttrgroupRelationEntity pmsAttrAttrgroupRelationEntity = new PmsAttrAttrgroupRelationEntity();
            pmsAttrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            pmsAttrAttrgroupRelationEntity.setAttrGroupId(pmsAttr.getAttrGroupId());
            relationDao.insert(pmsAttrAttrgroupRelationEntity);
        }

    }

    @Override
    public PageUtils queryBasePage(Map<String, Object> params, Long catelogId, String type) {
        LambdaQueryWrapper<PmsAttrEntity> lambda = new QueryWrapper<PmsAttrEntity>().lambda();
        lambda.eq(PmsAttrEntity::getAttrType, "base".equalsIgnoreCase(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if (catelogId != 0) {
            lambda.eq(PmsAttrEntity::getCatelogId, catelogId);
        }

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            lambda.and((wrapper) -> {
                wrapper.eq(PmsAttrEntity::getAttrId, key)
                        .or()
                        .like(PmsAttrEntity::getAttrName, key);
            });
        }

        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                lambda
        );
        List<PmsAttrEntity> records = page.getRecords();
        List<AttrRespVo> collect = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //1、设置分类和分组的名字
            if ("base".equalsIgnoreCase(type)) {
                PmsAttrAttrgroupRelationEntity attrId = relationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().lambda().eq(PmsAttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
                if (attrId != null && attrId.getAttrGroupId() != null) {
                    PmsAttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }

            }


            PmsCategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(collect);

        return pageUtils;
    }

    /**
     * @param attrId 查询属性信息
     * @return
     */
    @Override
    public AttrRespVo getAttrInfo(Long attrId) {

        //查询到详细信息
        PmsAttrEntity byId = this.getById(attrId);

        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(byId, attrRespVo);
        //查询分组信息

        //1. 根据关联关系查询分组信息
        PmsAttrAttrgroupRelationEntity pmsAttrAttrgroupRelationEntity = relationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>()
                .lambda().eq(PmsAttrAttrgroupRelationEntity::getAttrId, attrId));
        if (pmsAttrAttrgroupRelationEntity != null) {
            attrRespVo.setAttrGroupId(pmsAttrAttrgroupRelationEntity.getAttrGroupId());
            //2. 查询group
            PmsAttrGroupEntity pmsAttrGroupEntity = attrGroupDao.selectOne(new QueryWrapper<PmsAttrGroupEntity>()
                    .lambda().eq(PmsAttrGroupEntity::getAttrGroupId, pmsAttrAttrgroupRelationEntity.getAttrGroupId()));
            if (pmsAttrGroupEntity != null) {
                attrRespVo.setGroupName(pmsAttrGroupEntity.getAttrGroupName());
            }
        }

        //设置分类信息
        Long[] categoryPath = categoryService.findCategoryPath(byId.getCatelogId());
        attrRespVo.setCatelogPath(categoryPath);

        PmsCategoryEntity pmsCategoryEntity = categoryDao.selectById(byId.getCatelogId());
        attrRespVo.setCatelogName(pmsCategoryEntity.getName());
        return attrRespVo;
    }

    @Override
    public void updateAttr(AttrVo vo) {

        PmsAttrEntity pmsAttrEntity = new PmsAttrEntity();
        BeanUtils.copyProperties(vo, pmsAttrEntity);
        //更新本身
        this.updateById(pmsAttrEntity);

        //修改分组关联
        Integer integer = relationDao.selectCount(new UpdateWrapper<PmsAttrAttrgroupRelationEntity>()
                .lambda().eq(PmsAttrAttrgroupRelationEntity::getAttrId, vo.getAttrId()));
        PmsAttrAttrgroupRelationEntity pmsAttrAttrgroupRelationEntity = new PmsAttrAttrgroupRelationEntity();
        pmsAttrAttrgroupRelationEntity.setAttrGroupId(vo.getAttrGroupId());
        pmsAttrAttrgroupRelationEntity.setAttrId(vo.getAttrId());
        if (integer > 0) {
            //更新操作
            relationDao.update(pmsAttrAttrgroupRelationEntity, new UpdateWrapper<PmsAttrAttrgroupRelationEntity>()
                    .lambda().eq(PmsAttrAttrgroupRelationEntity::getAttrId, vo.getAttrId()));
        } else {
            relationDao.insert(pmsAttrAttrgroupRelationEntity);
        }

    }

    /**
     * @param attrgroupId 根据分组id查找关联的所有基本属性
     * @return
     */
    @Override
    public List<PmsAttrEntity> getRelationAttr(Long attrgroupId) {

        //根据分组id  查找 属性id
        List<PmsAttrAttrgroupRelationEntity> entities = relationDao.selectList(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().lambda()
                .eq(PmsAttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId));

        //将属性id放到list
        List<Long> collect = entities.stream().map(PmsAttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

        List<PmsAttrEntity> pmsAttrEntities = this.baseMapper.selectBatchIds(collect);

        return pmsAttrEntities;
    }

    /**
     * @param vos
     * 删除对应关系
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {

        List<PmsAttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            PmsAttrAttrgroupRelationEntity relationEntity = new PmsAttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        this.baseMapper.deleteRelation(entities);
    }

}