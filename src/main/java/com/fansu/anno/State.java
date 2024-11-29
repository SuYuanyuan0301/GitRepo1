package com.fansu.anno;

import com.fansu.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented//元注解
@Target({FIELD})// 注解所修饰的对象类型
@Retention(RUNTIME)//
@Constraint(validatedBy = {StateValidation.class})// 指定校验器
public @interface State {
    // 校验失败的提示信息
    String message() default "状态值不合法,State的参数值只能是'已发布'或者'草稿'";
    //指定分组
    Class<?> [] groups() default {};
    //负载，获取到State注解的附加信息
    Class<? extends Payload>[] payload() default {};

}
