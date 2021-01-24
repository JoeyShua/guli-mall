package com.jxs.coupon.dao;

import com.jxs.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:28:12
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
