package com.jxs.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxs.common.utils.PageUtils;
import com.jxs.member.entity.UmsMemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:40:25
 */
public interface UmsMemberService extends IService<UmsMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

