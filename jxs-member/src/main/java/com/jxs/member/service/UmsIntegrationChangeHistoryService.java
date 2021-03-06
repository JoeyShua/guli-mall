package com.jxs.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxs.common.utils.PageUtils;
import com.jxs.member.entity.UmsIntegrationChangeHistoryEntity;

import java.util.Map;

/**
 * 积分变化历史记录
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:40:24
 */
public interface UmsIntegrationChangeHistoryService extends IService<UmsIntegrationChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

