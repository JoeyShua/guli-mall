package com.jxs.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.Query;

import com.jxs.ware.dao.WmsWareOrderTaskDao;
import com.jxs.ware.entity.WmsWareOrderTaskEntity;
import com.jxs.ware.service.WmsWareOrderTaskService;


@Service("wmsWareOrderTaskService")
public class WmsWareOrderTaskServiceImpl extends ServiceImpl<WmsWareOrderTaskDao, WmsWareOrderTaskEntity> implements WmsWareOrderTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WmsWareOrderTaskEntity> page = this.page(
                new Query<WmsWareOrderTaskEntity>().getPage(params),
                new QueryWrapper<WmsWareOrderTaskEntity>()
        );

        return new PageUtils(page);
    }

}