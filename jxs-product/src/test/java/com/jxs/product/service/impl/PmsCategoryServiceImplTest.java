package com.jxs.product.service.impl;

import com.jxs.product.dao.PmsCategoryDao;
import com.jxs.product.service.PmsCategoryService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PmsCategoryServiceImplTest extends TestCase {

    @Autowired
    private PmsCategoryService pmsCategoryService;


    @Test
    public void testFindCategoryPath() {
        System.out.println(pmsCategoryService.findCategoryPath(25l));
    }
}