package com.fastobject.diff;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by colinsu
 *
 * @date 2019/9/6.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DiffLog {
    /**
     * 汉字全称
     * @return
     */
    String name();

    /**
     * Date 如何格式化，默认可以为空
     * @return
     */
    String dateFormat() default "";

    /**
     * 是否忽略该值
     * @return
     */
    boolean ignore() default false;


}
