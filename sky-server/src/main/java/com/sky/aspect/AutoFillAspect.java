package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充
 */
@Aspect //@Aspect 注解表明这是一个切面类，包含增强逻辑的通知（Advice）和定义切入点（Pointcut）。
@Component //切面类一般使用 @Component 或者通过 XML 方式配置成 Spring Bean，这样 Spring 才能将其织入到业务逻辑中
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    //@annotation(com.sky.annotation.AutoFill) 表示只对有 AutoFill 注解的方法进行增强
    public void autuFillPointcut(){}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autuFillPointcut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自动填充...");

        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获取方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的数据库操作类型注解
        OperationType operationType = autoFill.value();//获取数据库操作类型

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }

        Object enetity =args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT){
            // 为四个公共字段赋值
            try {
//                Method setCreateTime = enetity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
//                Method setCreateUser = enetity.getClass().getDeclaredMethod("setCreateUser", Long.class);
//                Method setUpdateTime = enetity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
//                Method setUpdateUser = enetity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                Method setCreateTime = enetity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = enetity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = enetity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = enetity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对应的对象属性赋值
                setCreateTime.invoke(enetity, now);
                setCreateUser.invoke(enetity, currentId);
                setUpdateTime.invoke(enetity, now);
                setUpdateUser.invoke(enetity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            // 为两个公共字段赋值
            try {
//                Method setUpdateTime = enetity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
//                Method setUpdateUser = enetity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                Method setUpdateTime = enetity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = enetity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对应的对象属性赋值
                setUpdateTime.invoke(enetity, now);
                setUpdateUser.invoke(enetity, currentId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
