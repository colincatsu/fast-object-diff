package com.fastobject.diff;

import com.fastobject.diff.model.BeanA;
import com.fastobject.diff.model.BeanB;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by colinsu
 *
 * @date 2019/9/9.
 */
public class JavaDiffTest {


    public static void main(String[] args) throws Exception{

        test();
    }

    private static void test() throws Exception{

        BeanB a1b = new BeanB(1L,"北京");
        BeanB a1b3 = new BeanB(3L,"3");
        BeanB a1b2 = new BeanB(2L,"1");

        ArrayList<BeanB> list = new ArrayList<>();
        list.add(a1b);
        list.add(a1b3);
        list.add(a1b2);
        BeanA a1 = new BeanA("1","1",list);
        a1.setStart(new Date());
        //        a1.setPrice(new BigDecimal("10.23"));


        BeanB a2b = new BeanB(1L,"上海");
        BeanB a2b2 = new BeanB(2L,"2");

        ArrayList<BeanB> list2 = new ArrayList<>();
        list2.add(a2b);
        list2.add(a2b2);
        final BeanA a2 = new BeanA("2","2",list2);
        a2.setPrice(new BigDecimal("50.852236"));

        String s = ChineseObjectDiff.genChineseDiffStr(a1, a2);
        System.out.println(s);
    }
}
