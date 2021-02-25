package com.jxs.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxs.common.utils.PageUtils;
import com.jxs.product.entity.PmsAttrEntity;
import com.jxs.product.vo.AttrGroupRelationVo;
import com.jxs.product.vo.AttrRespVo;
import com.jxs.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:48:46
 */
public interface PmsAttrService extends IService<PmsAttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param pmsAttr
     * 保存属性
     */
    void saveAttr(AttrVo pmsAttr);

    PageUtils queryBasePage(Map<String, Object> params, Long catelogId,String type);

    /**
     * @param attrId
     * 查询属性信息
     * @return
     */
    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo vo);

    List<PmsAttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

}

