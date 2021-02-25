package com.jxs.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.jxs.product.entity.PmsAttrAttrgroupRelationEntity;
import com.jxs.product.entity.PmsAttrEntity;
import com.jxs.product.service.PmsAttrAttrgroupRelationService;
import com.jxs.product.service.PmsAttrService;
import com.jxs.product.service.PmsCategoryService;
import com.jxs.product.vo.AttrGroupRelationVo;
import com.jxs.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jxs.product.entity.PmsAttrGroupEntity;
import com.jxs.product.service.PmsAttrGroupService;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.R;


/**
 * 属性分组
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:48:46
 */
@RestController
@RequestMapping("product/pmsattrgroup")
public class PmsAttrGroupController {
    @Autowired
    private PmsAttrGroupService pmsAttrGroupService;
    @Autowired
    private PmsAttrService pmsAttrService;
    @Autowired
    private PmsCategoryService pmsCategoryService;
    @Autowired
    private PmsAttrAttrgroupRelationService relationService;


    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){

        relationService.saveBatch(vos);
        return R.ok();
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<PmsAttrEntity> entities =  pmsAttrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody  AttrGroupRelationVo[] vos){
        pmsAttrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{categoryId}")
    // @RequiresPermissions("product:pmsattrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("categoryId") Long categoryId) {
        PageUtils page = pmsAttrGroupService.queryPage(params, categoryId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:pmsattrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        PmsAttrGroupEntity pmsAttrGroup = pmsAttrGroupService.getById(attrGroupId);

        Long[] catelogPath = pmsCategoryService.findCategoryPath(pmsAttrGroup.getCatelogId());

        pmsAttrGroup.setCatelogPath(catelogPath);

        return R.ok().put("pmsAttrGroup", pmsAttrGroup);
    }

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId")Long catelogId){

        //1、查出当前分类下的所有属性分组，
        //2、查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> vos =  pmsAttrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data",vos);
    }


    /**
     * 信息
     * http://localhost:8888/api/product/attrgroup/3/noattr/relation
     */
    @RequestMapping("/{attrGroupId}/noattr/relation")
    public R attrRelation(@RequestParam Map<String, Object> params, @PathVariable("attrGroupId") Long attrGroupId) {
        PageUtils page = pmsAttrGroupService.getAttrRelation(params, attrGroupId);
        return R.ok().put("page", page);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmsattrgroup:save")
    public R save(@RequestBody PmsAttrGroupEntity pmsAttrGroup) {
        pmsAttrGroupService.save(pmsAttrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:pmsattrgroup:update")
    public R update(@RequestBody PmsAttrGroupEntity pmsAttrGroup) {
        pmsAttrGroupService.updateById(pmsAttrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmsattrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        pmsAttrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
