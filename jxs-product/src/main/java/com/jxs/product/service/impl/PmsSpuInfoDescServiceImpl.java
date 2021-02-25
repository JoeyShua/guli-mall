package com.jxs.product.service.impl;

import com.jxs.product.vo.SpuSaveVo;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.Query;

import com.jxs.product.dao.PmsSpuInfoDescDao;
import com.jxs.product.entity.PmsSpuInfoDescEntity;
import com.jxs.product.service.PmsSpuInfoDescService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsSpuInfoDescService")
@Transactional
public class PmsSpuInfoDescServiceImpl extends ServiceImpl<PmsSpuInfoDescDao, PmsSpuInfoDescEntity> implements PmsSpuInfoDescService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSpuInfoDescEntity> page = this.page(
                new Query<PmsSpuInfoDescEntity>().getPage(params),
                new QueryWrapper<PmsSpuInfoDescEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSpu(SpuSaveVo vo) {



    }

    /**
     * @param descEntity
     * 保存spu 描述信息
     */
    @Override
    public void saveSpuInfoDesc(PmsSpuInfoDescEntity descEntity) {
        this.baseMapper.insert(descEntity);
    }

}