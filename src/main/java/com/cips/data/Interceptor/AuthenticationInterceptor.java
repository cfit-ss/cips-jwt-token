package com.cips.data.Interceptor;

import com.cips.data.Common.JwtTokenUtil;
import com.cips.data.Service.UserService;
import com.cips.data.Annotation.PassToken;
import com.cips.data.Annotation.UserLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        /*
        HandlerMethod handlerMethod_flag=(HandlerMethod)object;
        Method method_flag = handlerMetod.getMethod();
        if(method.isAnnotationPresent(PassToken.class)){
            PassToken passToken = method.getAnnotation(PassToken.class);
            if(passToken.required()){
                return true;
            }
        }//
        if(method.isAnnotationPresent(UserLoginToken.class)){
            //参数字段信息
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if(userLoginToken.required()){
                //认证
                if(token == null){
                    throw new  RuntimeException("无token，请重新登录");
                }
                if(JwtTokenUtil.verifyToken(token,s:"123456")){
                    return true;
                }else{
                   throw new RuntimeException("402");
                }
            }
            }
        }*/

        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 user id
                /*String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }
                User user = userService.getUserById(userId);
                if (user == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }*/
                // 验证 token
                if(JwtTokenUtil.verifyToken(token, "123456")){
                    return true;
                }else {
                    throw new RuntimeException("401");
                }
            }
        }
        return true;
    }

   // public void postHandle(HttpServletRequest httpServletRequest,)
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {


    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
