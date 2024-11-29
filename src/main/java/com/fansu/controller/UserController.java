package com.fansu.controller;

import com.fansu.pojo.User;
import com.fansu.service.UserService;
import com.fansu.pojo.Result;
import com.fansu.utils.JwtUtil;
import com.fansu.utils.Md5Util;
import com.fansu.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
/*
 * @RestController 是 Spring 框架中的一个注解，
 * 用于标记一个类为控制器，并且默认所有方法都返回 JSON 或 XML 格式的数据，而不是视图页面。
 * 这个注解是 @Controller 和 @ResponseBody 的组合
 * */

@RestController
@RequestMapping("/user")
@Validated//参数校验
public class UserController {
    @Autowired
    private UserService userService;

    /*
    注册
    */
    @PostMapping("/register")//注册接口是post
    public Result register(@Pattern(regexp = "^\\S{6,15}$") String username, @Pattern(regexp = "^\\S{6,15}$") String password) {
        //参数校验,目的是在注册时对注册账号密码起到一个规范作用（密码长度不少于6位且密码长度不大于15）

        //密码校验通过
        //查询用户
        User user = userService.findByUsername(username);
        if (user == null) {
            //查询用户名，如果数据库中没有这个用户名才能继续下一步注册，如果有则该用户名不可用则注册失败
            userService.register(username, password);
            return Result.success();
        } else {
            //返回错误信息
            return Result.error("用户名已被占用");
        }
    }
    /*
     登录
    * */
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{6,15}$") String username, @Pattern(regexp = "^\\S{6,15}$") String password) {
        //根据用户名查询用户，查询结果为空则用户不存在
        User loginUser = userService.findByUsername(username);
        if (loginUser == null) {
            return Result.error("用户名不存在");
        }
        //密码校验,上面的loginUser 就是从数据库中查询出来的用户对象，所以该对象的密码是加密的
        //所以我们要将用户登录所输入的密码（传入的参数）进行加密，再跟数据库中已加密的密码进行比较
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            //登陆成功
            //生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        }else {
            return Result.error("密码错误");
        }
    }
    /*
    获取用户信息
    */
    @RequestMapping(value = "/userInfo")
    //注释掉，因为拦截器已经拦截了，这里就不用再拦截了
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/){
        //根据用户名查询用户
        /*Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");*/

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String)map.get("username");
        User user = userService.findByUsername(username);
        return Result.success(user);
    }
    /*
    修改用户信息
    */
    @RequestMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    /*
    * 更新用户头像
    * */
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    /*
    * 更新密码
    * */
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params){
        //1.校验参数
        String oldPwd = params.get("old_pwd");//原密码
        String newPwd = params.get("new_pwd");//新密码
        String rePwd = params.get("re_pwd");//确认密码
        //密码不能为空，且符合规范
        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("密码不能为空");
        }
        //判断原密码是否正确——>调用userService根据用户名拿到原密码，再和传入的参数old_pwd进行比对
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String)map.get("username");
        User loginUser = userService.findByUsername(username);
        //用户传入的密码是明文，而在数据库中的密码是加密的，所以要进行加密再进行比对
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码填写错误");
        }
        if(!newPwd.equals(rePwd)){
            return Result.error("两次密码不一致");
        }

        //2.调用service完成密码更新
        userService.updatePwd(newPwd);
        return Result.success();
    }

}
