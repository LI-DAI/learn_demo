package com.learn.security.anon;

import java.lang.annotation.*;

/**
 * @author LD
 * @date 2021/4/23 16:41
 * <p>
 * 匿名访问注解
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnonymousAccess {


}
