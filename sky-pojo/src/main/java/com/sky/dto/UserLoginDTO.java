package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data //Lombok 提供的注解，用于自动生成 getter、setter、toString()、equals()、hashCode() 等方法。
public class UserLoginDTO implements Serializable {

    private String code;

}
