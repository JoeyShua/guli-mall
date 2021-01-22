package com.jxs.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jxs.coupon.entity.SmsCouponEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-21 19:34:26
 */
@Mapper
public interface SmsCouponDao extends BaseMapper<SmsCouponEntity> {
	
}
