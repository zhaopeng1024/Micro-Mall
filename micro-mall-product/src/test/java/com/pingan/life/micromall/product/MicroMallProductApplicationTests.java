package com.pingan.life.micromall.product;

import com.pingan.life.micromall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class MicroMallProductApplicationTests {

    /*@Autowired
    CategoryDao categoryDao;*/

    @Autowired
    CategoryService categoryService;

    @Test
    void contextLoads() {
//        List<CategoryEntity> categoryEntities = categoryDao.selectCategoriesWithTree();
//        categoryEntities.forEach(System.out::println);
    }

    @Test
    void testSelectFullPath() {
        System.out.println(Arrays.toString(categoryService.selectFullPath(2L)));
        System.out.println(Arrays.toString(categoryService.selectFullPath(22L)));
        System.out.println(Arrays.toString(categoryService.selectFullPath(483L)));
    }
}
