package com.jxs.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.Query;

import com.jxs.product.dao.PmsSkuInfoDao;
import com.jxs.product.entity.PmsSkuInfoEntity;
import com.jxs.product.service.PmsSkuInfoService;


@Service("pmsSkuInfoService")
public class PmsSkuInfoServiceImpl extends ServiceImpl<PmsSkuInfoDao, PmsSkuInfoEntity> implements PmsSkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        LambdaQueryWrapper<PmsSkuInfoEntity> lambda = new QueryWrapper<PmsSkuInfoEntity>().lambda();

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            lambda.and((item) -> {
                item.eq(PmsSkuInfoEntity::getSkuId, key).or()
                        .like(PmsSkuInfoEntity::getSkuName, key);
            });
        }
        String brandId = params.get("brandId") == null ? null : (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId)  && !"0".equals(brandId)) {
            lambda.eq(PmsSkuInfoEntity::getBrandId, brandId);
        }

        String minPrice = params.get("min") == null ? null : (String) params.get("min");
        if (StringUtils.isNotBlank(minPrice) && !"0".equals(minPrice)) {
            lambda.gt(PmsSkuInfoEntity::getPrice, new BigDecimal(minPrice));
        }
        String maxPrice = params.get("max") == null ? null : (String) params.get("max");
        if (StringUtils.isNotBlank(maxPrice) && !"0".equals(maxPrice)) {
            lambda.lt(PmsSkuInfoEntity::getPrice, new BigDecimal(maxPrice));
        }

        String catelogId = params.get("catelogId") == null ?null :(String)params.get("catelogId");
        if (StringUtils.isNotBlank(catelogId) && !"0".equals(maxPrice)) {
            lambda.eq(PmsSkuInfoEntity::getCatalogId, catelogId);
        }

        IPage<PmsSkuInfoEntity> page = this.page(
                new Query<PmsSkuInfoEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

    /**
     * @param skuInfoEntity 保存sku 信息
     */
    @Override
    public void saveSkuInfo(PmsSkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

}