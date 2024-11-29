package com.fansu.controller;

import com.fansu.pojo.Category;
import com.fansu.pojo.Result;
import com.fansu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    //添加分类
    @PostMapping
    public Result add(@RequestBody @Validated (Category.Add.class)Category category){
        categoryService.add(category);
        return Result.success();
    }

    //查询当前用户创建的分类
    @GetMapping ()
    public Result<List<Category>> list(){
        //视频中的变量是cs，我这里改为了categoryList
        List<Category> cs = categoryService.list();
        return Result.success(cs);
    }

    //获取分类详情
    @GetMapping("/detail")
    public Result<Category> detail(Integer id){
        Category c = categoryService.findById(id);
        return Result.success(c);
    }

    //修改分类
    @PutMapping
    public Result update(@RequestBody @Validated(Category.Update.class) Category category){
        categoryService.update(category);
        return Result.success();
    }


}
