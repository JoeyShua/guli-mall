package com.jxs.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxs.common.utils.PageUtils;
import com.jxs.coupon.entity.CouponSpuCategoryRelationEntity;

import java.util.Map;

/**
 * 优惠券分类关联
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:28:13
 */
public interface CouponSpuCategoryRelationService extends IService<CouponSpuCategoryRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

