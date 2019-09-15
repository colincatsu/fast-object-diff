package com.fastobject.diff;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by colinsu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DiffLogKey {
    String name() default "id";
}
