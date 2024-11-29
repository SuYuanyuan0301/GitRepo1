package com.fansu.service;

import com.fansu.pojo.Category;

import java.util.List;

public interface CategoryService {
    //新增分类
    void add(Category category);

    //查询当前用户创建的分类
    List<Category> list();

    //根据id查询分类信息
    Category findById(Integer id);

    void update(Category category);
}
