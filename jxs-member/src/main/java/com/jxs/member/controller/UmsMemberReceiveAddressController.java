package com.jxs.member.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jxs.member.entity.UmsMemberReceiveAddressEntity;
import com.jxs.member.service.UmsMemberReceiveAddressService;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.R;



/**
 * 会员收货地址
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:40:25
 */
@RestController
@RequestMapping("member/memberreceiveaddress")
public class UmsMemberReceiveAddressController {
    @Autowired
    private UmsMemberReceiveAddressService umsMemberReceiveAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("member:umsmemberreceiveaddress:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = umsMemberReceiveAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:umsmemberreceiveaddress:info")
    public R info(@PathVariable("id") Long id){
		UmsMemberReceiveAddressEntity umsMemberReceiveAddress = umsMemberReceiveAddressService.getById(id);

        return R.ok().put("umsMemberReceiveAddress", umsMemberReceiveAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:umsmemberreceiveaddress:save")
    public R save(@RequestBody UmsMemberReceiveAddressEntity umsMemberReceiveAddress){
		umsMemberReceiveAddressService.save(umsMemberReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("member:umsmemberreceiveaddress:update")
    public R update(@RequestBody UmsMemberReceiveAddressEntity umsMemberReceiveAddress){
		umsMemberReceiveAddressService.updateById(umsMemberReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:umsmemberreceiveaddress:delete")
    public R delete(@RequestBody Long[] ids){
		umsMemberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
