package org.jeecg.mphelper.util;

import cn.hutool.core.lang.Assert;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author dafei
 * @version 0.1
 * @date 2020/4/23 16:52
 */
public class FieldUtil {


    /**获取所有子类, 包括所有超类.
     * 超类的字段依次追加在后面.
     * @param object
     * @return
     */
    public static Field[] getAllFields(Class<?> clazz) {

        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    public static Field[] getAllFields(Object object) {
        Assert.notNull(object);
        return getAllFields(object.getClass());
    }

    /**
     * field name --> field
     * 自身和超类的字段名重复时, 优先使用自身的.
     */
    public static Map<String, Field> getAllFieldMap(Class<?> clz) {

        Field[] allFields = getAllFields(clz);
        // 倒序遍历, 让自己的替换超类的
        Map<String, Field> map = new LinkedHashMap<>();
        for (int i = allFields.length-1; i >-1 ; i--) {
            Field f = allFields[i];
            map.put(f.getName(), f);
        }

        return map;
    }
}
