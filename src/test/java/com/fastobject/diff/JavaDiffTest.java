package com.fastobject.diff;

import com.fastobject.diff.model.BeanA;
import com.fastobject.diff.model.BeanB;

import com.fastobject.diff.model.BeanC;
import com.fastobject.diff.model.BeanD;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        BeanB a1b = new BeanB(1L,"北京",new Date());
        BeanB a1b3 = new BeanB(3L,"3",new Date());
        BeanB a1b2 = new BeanB(2L,"1",new Date());

        BeanC b1c = new BeanC(12L,"beanC",new Date(),new BeanD("源beand"));
        BeanC b2c = new BeanC(13L,"beanC2",new Date(),new BeanD("现beand"));

        ArrayList<BeanB> list = new ArrayList<>();
        list.add(a1b);
        list.add(a1b3);
        list.add(a1b2);
        BeanA a1 = new BeanA("1","1",list);
        a1.setStart(new Date());
        a1.setBit(new Byte("11"));
        a1.setUnit(new Short("66"));
        a1.setBeanC(b1c);
        //        a1.setPrice(new BigDecimal("10.23"));


        BeanB a2b = new BeanB(1L,"上海",new Date());
        BeanB a2b2 = new BeanB(2L,"2",new Date());

        ArrayList<BeanB> list2 = new ArrayList<>();
        list2.add(a2b);
        list2.add(a2b2);
        final BeanA a2 = new BeanA("2","2",null);
        a2.setPrice(new BigDecimal("50.852236"));
        a2.setBit(new Byte("22"));
        a2.setUnit(new Short("99"));
        a2.setBeanC(b2c);
        List<DiffWapper> diffWappers = AbstractObjectDiff.generateDiff(a1, a2);


        String s = ChineseObjectDiff.genChineseDiffStr(a1, a2);
        System.out.println(s);
    }
}
