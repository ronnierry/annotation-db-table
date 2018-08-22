package com.ronnie.annotationdbtable.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: entity ID
 * @Author: rongyu
 * @CreateDate: 2018/8/22$ 16:16$
 * @Remark:
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

}
