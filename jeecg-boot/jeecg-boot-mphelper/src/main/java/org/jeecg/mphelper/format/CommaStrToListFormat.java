package org.jeecg.mphelper.format;

import java.util.Arrays;
import java.util.List;

/**
 * 'a,b,c' --> [a, b, c]
 * @author dafei
 * @version 0.1
 * @date 2020/4/26 0:02
 */
public class CommaStrToListFormat implements InOutFormat {
    @Override
    public Object apply(Object o) {
        List list = null;
        if (o==null) {
            return list;
        }
        String str = o.toString();
        String[] split = str.split("\\s*,\\s*");
        list = Arrays.asList(split);

        // TODO 适配元素类型

        return list;
    }
}
