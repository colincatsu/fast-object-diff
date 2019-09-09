package com.fastobject.diff;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by colinsu
 *
 * @date 2019/9/6.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DiffLogKey {
    String name() default "id";
}
