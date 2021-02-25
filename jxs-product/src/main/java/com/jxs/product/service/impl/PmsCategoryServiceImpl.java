package com.jxs.product.service.impl;

import com.jxs.product.service.PmsCategoryBrandRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.transaction.annotation.Transactional;


@Service("pmsCategoryService")
@Transactional
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryDao, PmsCategoryEntity> implements PmsCategoryService {

    @Autowired
    private PmsCategoryDao pmsCategoryDao;
    @Autowired
    private PmsCategoryBrandRelationService pmsCategoryBrandRelationService;

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

    /**
     * @param attrGroupId 查找分类路径
     * @return
     */
    @Override
    public Long[] findCategoryPath(Long attrGroupId) {

        List<Long> paths = new ArrayList<>();
        findParentPath(attrGroupId,paths);
        Collections.reverse(paths);
        return paths.toArray(new Long[paths.size()]);
    }

    /**
     * @param pmsCategory
     * 级联所有更新的字段更新
     */
    @Override
    public void updateCascade(PmsCategoryEntity pmsCategory) {


        //更新自己
        this.updateById(pmsCategory);

        if(StringUtils.isNotBlank(pmsCategory.getName())){
            //更新关联表
            pmsCategoryBrandRelationService.updateCategory(pmsCategory);
        }


    }

    /**
     * @param attrGroupId
     * 递归查找父分类
     * @param paths
     */
    private void findParentPath(Long attrGroupId, List<Long> paths) {

        //收集当前节点id
        paths.add(attrGroupId);
        PmsCategoryEntity pmsCategoryEntity = pmsCategoryDao.selectById(attrGroupId);

        if(pmsCategoryEntity.getParentCid() != null && pmsCategoryEntity.getParentCid() != 0){
            findParentPath(pmsCategoryEntity.getParentCid(),paths);
        }
        return;
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