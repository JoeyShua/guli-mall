package com.jxs.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxs.common.utils.PageUtils;
import com.jxs.ware.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author jxs
 * @email 7346979070@qq.com
 * @date 2021-01-23 21:51:36
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

