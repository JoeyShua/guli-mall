package com.jxs.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jxs.product.dao.PmsBrandDao;
import com.jxs.product.dao.PmsCategoryDao;
import com.jxs.product.entity.PmsBrandEntity;
import com.jxs.product.entity.PmsCategoryEntity;
import com.jxs.product.info.BrandVo;
import com.jxs.product.service.PmsBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.Query;

import com.jxs.product.dao.PmsCategoryBrandRelationDao;
import com.jxs.product.entity.PmsCategoryBrandRelationEntity;
import com.jxs.product.service.PmsCategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsCategoryBrandRelationService")
@Transactional
public class PmsCategoryBrandRelationServiceImpl extends ServiceImpl<PmsCategoryBrandRelationDao, PmsCategoryBrandRelationEntity> implements PmsCategoryBrandRelationService {

    @Autowired
    private PmsCategoryBrandRelationService pmsCategoryBrandRelationService;
    @Autowired
    private PmsBrandService pmsBrandService;
    @Autowired
    private PmsCategoryBrandRelationDao pmsCategoryBrandRelationDao;
    @Autowired
    private PmsBrandDao pmsBrandDao;
    @Autowired
    private PmsCategoryDao pmsCategoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryBrandRelationEntity> page = this.page(
                new Query<PmsCategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<PmsCategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Object cateloglist(Long brandId) {

        List<PmsCategoryBrandRelationEntity> data = this.list(
                new QueryWrapper<PmsCategoryBrandRelationEntity>().lambda().eq(PmsCategoryBrandRelationEntity::getBrandId, brandId)
        );
        return data;
    }

    @Override
    public List<BrandVo> relationBrandsList(Long catId) {
        List<PmsBrandEntity> vos = pmsCategoryBrandRelationService.getBrandsByCatId(catId);

        List<BrandVo> collect = vos.stream().map(item -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());

            return brandVo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<PmsBrandEntity> getBrandsByCatId(Long catId) {
        List<PmsCategoryBrandRelationEntity> catelogId = pmsCategoryBrandRelationDao.selectList(new QueryWrapper<PmsCategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<PmsBrandEntity> collect = catelogId.stream().map(item -> {
            Long brandId = item.getBrandId();
            PmsBrandEntity byId = pmsBrandService.getById(brandId);
            return byId;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * @param pmsCategoryBrandRelation 保存详情
     */
    @Override
    public void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation) {
        Long brandId = pmsCategoryBrandRelation.getBrandId();
        Long catelogId = pmsCategoryBrandRelation.getCatelogId();
        //1、查询详细名字
        PmsBrandEntity brandEntity = pmsBrandDao.selectById(brandId);
        PmsCategoryEntity categoryEntity = pmsCategoryDao.selectById(catelogId);

        pmsCategoryBrandRelation.setBrandName(brandEntity.getName());
        pmsCategoryBrandRelation.setCatelogName(categoryEntity.getName());

        pmsCategoryBrandRelationDao.insert(pmsCategoryBrandRelation);


    }

    /**
     * @param brandId 更新品牌
     * @param name
     */
    @Override
    public void updateBrand(Long brandId, String name) {
        PmsCategoryBrandRelationEntity pmsCategoryBrandRelationEntity = new PmsCategoryBrandRelationEntity();
        pmsCategoryBrandRelationEntity.setBrandId(brandId);
        pmsCategoryBrandRelationEntity.setBrandName(name);

        UpdateWrapper<PmsCategoryBrandRelationEntity> pmsCategoryBrandRelationEntityUpdateWrapper = new UpdateWrapper<>();
        pmsCategoryBrandRelationEntityUpdateWrapper.lambda()
                .eq(PmsCategoryBrandRelationEntity::getBrandId,brandId);
        pmsCategoryBrandRelationDao.update(pmsCategoryBrandRelationEntity,pmsCategoryBrandRelationEntityUpdateWrapper);
    }

    /**
     * @param pmsCategory
     * 级联更新分类
     */
    @Override
    public void updateCategory(PmsCategoryEntity pmsCategory) {

        this.baseMapper.updateCategory(pmsCategory.getCatId(),pmsCategory.getName());

    }

}