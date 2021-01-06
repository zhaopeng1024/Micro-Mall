package com.pingan.life.micromall.product;

import com.pingan.life.micromall.product.dao.CategoryDao;
import com.pingan.life.micromall.product.entity.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MicroMallProductApplicationTests {

    @Autowired
    CategoryDao categoryDao;

    @Test
    void contextLoads() {

        List<CategoryEntity> categoryEntities = categoryDao.selectCategoriesWithTree();
        categoryEntities.forEach(System.out::println);

    }

}
