package com.jxs.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxs.common.utils.PageUtils;
import com.jxs.common.utils.Query;

import com.jxs.product.dao.PmsCategoryDao;
import com.jxs.product.entity.PmsCategoryEntity;
import com.jxs.product.service.PmsCategoryService;


@Service("pmsCategoryService")
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryDao, PmsCategoryEntity> implements PmsCategoryService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryEntity> page = this.page(
                new Query<PmsCategoryEntity>().getPage(params),
                new QueryWrapper<PmsCategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @return
     */
    @Override
    public List<PmsCategoryEntity> listWithTree() {

        //1. 怕查询所有列表
        List<PmsCategoryEntity> entities = baseMapper.selectList(null);
        //2.1找到所有的以及分类

        List<PmsCategoryEntity> leve1Menus = entities.stream().filter(pmsCategoryEntity -> pmsCategoryEntity.getParentCid() == 0)
                //设置children
                .map((root) -> {
                    root.setChildren(getChildren(root, entities));
                    return root;
                })
                //排序
                .sorted((menu1, menu2) -> {
                    return (menu1.getSort() == null?0:menu1.getSort()) - (menu2.getSort() == null?0:menu2.getSort());
                })
                .collect(Collectors.toList());
        return leve1Menus;
    }

    /**
     * @param asList
     * 批量删除
     */
    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1.检查当前要删除的菜单， 是否被别的地方引用
        baseMapper.deleteBatchIds(asList);
    }

    private List<PmsCategoryEntity> getChildren(PmsCategoryEntity root, List<PmsCategoryEntity> entities) {

        return entities.stream().filter((item) -> {
            return item.getParentCid() == root.getCatId();
        }).map((category) -> {
            category.setChildren(getChildren(category, entities));
            return category;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null?0:menu1.getSort()) - (menu2.getSort() == null?0:menu2.getSort());
        }).collect(Collectors.toList());
    }

}