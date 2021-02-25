package com.jxs.product.dao;

import com.jxs.product.entity.PmsAttrAttrgroupRelationEntity;
import com.jxs.product.entity.PmsAttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:48:46
 */
@Mapper
public interface PmsAttrDao extends BaseMapper<PmsAttrEntity> {

    void deleteRelation(@Param("list") List<PmsAttrAttrgroupRelationEntity>  collect);
}
