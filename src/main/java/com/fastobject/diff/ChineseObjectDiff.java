package com.fastobject.diff;

import java.util.List;

/**
 * Created by colinsu
 *
 */
public class ChineseObjectDiff extends AbstractObjectDiff {
    @Override
    protected String genDiffStr(Object sourceObject, Object targetObject) throws Exception {
        List<DiffWapper> diffWappers = generateDiff(sourceObject, targetObject);
        return DiffUtils.genDiffStr(diffWappers);
    }

}
