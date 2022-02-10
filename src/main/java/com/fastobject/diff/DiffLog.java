package com.fastobject.diff;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by colinsu
 * @author colinsu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DiffLog {
    /**
     * 汉字全称
     * @return name
     */
    String name();

    /**
     * Date 如何格式化，默认可以为空
     * @return dateFormat
     */
    String dateFormat() default "";

    /**
     * 是否忽略该值
     * @return ignore
     */
    boolean ignore() default false;


}
