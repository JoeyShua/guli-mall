package com.jxs.product.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.jxs.product.vo.AttrRespVo;
import com.jxs.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jxs.product.entity.PmsAttrEntity;
import com.jxs.product.service.PmsAttrService;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.R;



/**
 * 商品属性
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:48:46
 */
@RestController
@RequestMapping("product/pmsattr")
public class PmsAttrController {
    @Autowired
    private PmsAttrService pmsAttrService;

    /**
     * 列表
     */
    @RequestMapping("/{attrType}/list/{catelogId}")
    // @RequiresPermissions("product:pmsattr:list")
    public R queryBasePage(@RequestParam Map<String, Object> params,@PathVariable("catelogId")Long catelogId ,@PathVariable("attrType")String type){
        PageUtils page = pmsAttrService.queryBasePage(params,catelogId, type);

        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("product:pmsattr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsAttrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:pmsattr:info")
    public R info(@PathVariable("attrId") Long attrId){
		//PmsAttrEntity pmsAttr = pmsAttrService.getById(attrId);
        AttrRespVo pmsAttr = pmsAttrService.getAttrInfo(attrId);


        return R.ok().put("pmsAttr", pmsAttr);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmsattr:save")
    public R save(@RequestBody AttrVo pmsAttr){
        pmsAttrService.saveAttr(pmsAttr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:pmsattr:update")
    public R update(@RequestBody AttrVo vo){
		pmsAttrService.updateAttr(vo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmsattr:delete")
    public R delete(@RequestBody Long[] attrIds){
		pmsAttrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
