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
public class DiffWapper implements Serializable {
    private static final long serialVersionUID = -3232326683473741L;
    private String path;
    private String logName;
    private String op = "";
    private Difference diffValue;
}