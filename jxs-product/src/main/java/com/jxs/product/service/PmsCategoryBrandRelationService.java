package com.jxs.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxs.common.utils.PageUtils;
import com.jxs.product.entity.PmsBrandEntity;
import com.jxs.product.entity.PmsCategoryBrandRelationEntity;
import com.jxs.product.entity.PmsCategoryEntity;
import com.jxs.product.info.BrandVo;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:48:46
 */
public interface PmsCategoryBrandRelationService extends IService<PmsCategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Object cateloglist(Long brandId);

    List<BrandVo> relationBrandsList(Long catId);

    List<PmsBrandEntity> getBrandsByCatId(Long catId);

    /**
     * @param pmsCategoryBrandRelation
     * 保存详情
     */
    void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation);

    /**
     * @param brandId
     * 更新品牌
     * @param name
     */
    void updateBrand(Long brandId, String name);

    /**
     * @param pmsCategory
     * 更新分类
     */
    void updateCategory(PmsCategoryEntity pmsCategory);
}

