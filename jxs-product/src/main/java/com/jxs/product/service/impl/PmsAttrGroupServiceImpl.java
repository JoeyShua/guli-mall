package com.jxs.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxs.product.dao.PmsAttrAttrgroupRelationDao;
import com.jxs.product.dao.PmsAttrDao;
import com.jxs.product.entity.PmsAttrAttrgroupRelationEntity;
import com.jxs.product.entity.PmsAttrEntity;
import com.jxs.product.service.PmsAttrService;
import com.jxs.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.Query;

import com.jxs.product.dao.PmsAttrGroupDao;
import com.jxs.product.entity.PmsAttrGroupEntity;
import com.jxs.product.service.PmsAttrGroupService;


@Service("pmsAttrGroupService")
public class PmsAttrGroupServiceImpl extends ServiceImpl<PmsAttrGroupDao, PmsAttrGroupEntity> implements PmsAttrGroupService {

    @Autowired
    private PmsAttrAttrgroupRelationDao relationDao;
    @Autowired
    private PmsAttrDao attrAttrDao;
    @Autowired
    private PmsAttrService attrService;


    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {

        LambdaQueryWrapper<PmsAttrGroupEntity> lambda = new QueryWrapper<PmsAttrGroupEntity>().lambda();
        //如果传categoryId 则当作查询条件
        if (categoryId != null && categoryId > 0) {
            lambda.eq(PmsAttrGroupEntity::getCatelogId, categoryId);
        }
        String searchKey = (String) params.get("key");
        if (StringUtils.isNoneBlank(searchKey)) {
            lambda.and((obj) -> {
                obj.eq(PmsAttrGroupEntity::getAttrGroupId, searchKey)
                        .or()
                        .like(PmsAttrGroupEntity::getAttrGroupName, searchKey);
            });
        }
        IPage<PmsAttrGroupEntity> page = this.page(
                new Query<PmsAttrGroupEntity>().getPage(params),
                lambda);

        return new PageUtils(page);
    }

    /**
     *
     * @param params
     * @param attrGroupId 根据分组id查询关联的所有属性
     * @return
     */
    @Override
    public  PageUtils getAttrRelation(Map<String, Object> params, Long attrGroupId) {
        List<Long> collect = new ArrayList<>();
        //查询多个attrId
        List<PmsAttrAttrgroupRelationEntity> pmsAttrAttrgroupRelationEntities = relationDao.selectList(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().lambda()
                .eq(PmsAttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId));

        IPage<PmsAttrEntity> page = new Query<PmsAttrEntity>().getPage(params);
        if (pmsAttrAttrgroupRelationEntities != null && pmsAttrAttrgroupRelationEntities.size() > 0) {

            //将所有id 放到list中
            collect = pmsAttrAttrgroupRelationEntities.stream().map(PmsAttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
            //List<PmsAttrEntity> pmsAttrEntities =
            page = attrAttrDao.selectPage(page, new QueryWrapper<PmsAttrEntity>().lambda().in(PmsAttrEntity::getAttrId, collect));
        }
        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {

        //1、查询分组信息
        List<PmsAttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<PmsAttrGroupEntity>().lambda()
                .eq(PmsAttrGroupEntity::getCatelogId, catelogId));

        //2、查询所有属性
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(group -> {
            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group,attrsVo);
            List<PmsAttrEntity> attrs = attrService.getRelationAttr(attrsVo.getAttrGroupId());
            attrsVo.setAttrs(attrs);
            return attrsVo;
        }).collect(Collectors.toList());

        return collect;
    }

}