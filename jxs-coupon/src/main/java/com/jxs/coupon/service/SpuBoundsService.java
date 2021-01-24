package com.jxs.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxs.common.utils.PageUtils;
import com.jxs.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:28:13
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

