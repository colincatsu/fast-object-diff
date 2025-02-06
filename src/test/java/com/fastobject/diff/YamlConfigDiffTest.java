package com.fastobject.diff;

import com.fastobject.diff.model.BeanF;
import com.fastobject.diff.model.BeanG;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by colinsu
 *
 * @date 2025/2/6.
 */
public class YamlConfigDiffTest {

    public static void main(String[] args) throws Exception{
        DiffConfig diffConfig = ConfigReader.readConfig("diff-config.yaml");
        System.out.printf("di"+diffConfig.toString());

        BeanG a1b = new BeanG(1L,"北京",new Date());
        a1b.setConsumptionLimit(false);
        BeanG a1b3 = new BeanG(3L,"3",new Date());
        BeanG a1b2 = new BeanG(2L, "1", new Date(), new BigDecimal(100), 0.9);
        a1b2.setConsumptionLimit(true);

        ArrayList<BeanG> list = new ArrayList<>();
        list.add(a1b);
        list.add(a1b3);
        list.add(a1b2);

        BeanF a1 = new BeanF("1","1",list);
        a1.setStart(new Date());
        a1.setBit(new Byte("11"));
        a1.setUnit(new Short("66"));

        final BeanF a2 = new BeanF("2","2",null);
        a2.setPrice(new BigDecimal("50.852236"));
        a2.setBit(new Byte("22"));
        a2.setUnit(new Short("99"));

        List<DiffWapper> diffWappers = AbstractObjectDiff.generateDiff(diffConfig,a1, a2);
//        for (DiffWapper diffWapper : diffWappers) {
//            System.out.println(diffWapper);
//        }


        String s = ChineseObjectDiff.genChineseDiffStr(diffConfig,a1, a2);
        System.out.println(s);
        
    }

}
