package com.jxs.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxs.common.utils.PageUtils;
import com.jxs.product.entity.PmsSpuInfoDescEntity;
import com.jxs.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:48:46
 */
public interface PmsSpuInfoDescService extends IService<PmsSpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpu(SpuSaveVo vo);

    /**
     * @param descEntity
     * 保存spu描述信息
     */
    void saveSpuInfoDesc(PmsSpuInfoDescEntity descEntity);
}

