package com.fastobject.diff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by colinsu
 *
 * @date 2019/9/9.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Difference implements Serializable {
    private static final long serialVersionUID = 2321642126795290L;

    private Object oldValue;
    private Object newValue;
}
