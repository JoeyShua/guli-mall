package com.jxs.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxs.common.utils.PageUtils;
import com.jxs.product.entity.PmsCategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:48:46
 */
public interface PmsCategoryService extends IService<PmsCategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @return
     * 树状结构查询
     */
    List<PmsCategoryEntity> listWithTree();

    /**
     * @param asList
     * 批量删除
     */
    void removeMenuByIds(List<Long> asList);
}

