package com.fastobject.diff;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by colinsu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DiffLog {
    /**
     * 汉字全称
     */
    String name();

    /**
     * Date 如何格式化，默认可以为空
     */
    String dateFormat() default "";

    /**
     * 是否忽略该值
     */
    boolean ignore() default false;


}
