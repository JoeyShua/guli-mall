package com.jxs.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxs.product.entity.PmsAttrGroupEntity;
import com.jxs.product.service.PmsCategoryBrandRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.Query;

import com.jxs.product.dao.PmsBrandDao;
import com.jxs.product.entity.PmsBrandEntity;
import com.jxs.product.service.PmsBrandService;


@Service("pmsBrandService")
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandDao, PmsBrandEntity> implements PmsBrandService {

    @Autowired
    PmsBrandDao pmsBrandDao;
    @Autowired
    PmsCategoryBrandRelationService pmsCategoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        LambdaQueryWrapper<PmsBrandEntity> lambda = new QueryWrapper<PmsBrandEntity>().lambda();

        String searchKey = (String) params.get("key");
        if (StringUtils.isNotBlank(searchKey)) {
            lambda.and((obj) -> {
                obj.eq(PmsBrandEntity::getBrandId, searchKey)
                        .or()
                        .like(PmsBrandEntity::getName, searchKey);
            });
        }

        IPage<PmsBrandEntity> page = this.page(
                new Query<PmsBrandEntity>().getPage(params),
                lambda
        );

        return new PageUtils(page);
    }

    @Override
    public void upload() {


    }

    @Override
    public void updateDetail(PmsBrandEntity pmsBrand) {
        //保证冗余字段的数据一致
        pmsBrandDao.updateById(pmsBrand);
        if(!StringUtils.isEmpty(pmsBrand.getName())){
            //同步更新其他关联表中的数据
            pmsCategoryBrandRelationService.updateBrand(pmsBrand.getBrandId(),pmsBrand.getName());
        }
    }

}