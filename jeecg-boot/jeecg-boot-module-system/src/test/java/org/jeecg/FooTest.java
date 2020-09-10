package org.jeecg;

import lombok.Data;
import org.apache.commons.beanutils.PropertyUtils;
import org.jeecg.common.util.oConvertUtils;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author dafei
 * @version 0.1
 * @date 2020/4/10 20:27
 */
public class FooTest {

    @Test
    public void foo1() {
        User u = new User();
        u.setName("libai");

        Class<User> clz = User.class;
        Field[] declaredFields = clz.getDeclaredFields();
        for (Field f : declaredFields) {
            if (f.isAnnotationPresent(MyAnn.class)) {
                MyAnn myAnn = f.getAnnotation(MyAnn.class);
                System.out.println(myAnn);
            }
        }
        Method[] declaredMethods = clz.getDeclaredMethods();
        for (Field m : declaredFields) {
            if (m.isAnnotationPresent(MyAnn.class)) {

            }
        }

        PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(u);
        for (int i = 0; i < origDescriptors.length; i++) {
            PropertyDescriptor origDescriptor = origDescriptors[i];
            System.out.println(origDescriptor);
        }
    }

    @Test
    public void foo2() {

        VipUser u = new VipUser();
        u.setName("libai");
        u.setAge(88);

        Field[] declaredFields = VipUser.class.getDeclaredFields();
        // 拿不到超类的
        for (Field f : declaredFields) {
            System.out.println(f);
        }

        Field[] allFields = oConvertUtils.getAllFields(u);
        for (Field f : allFields) {
            System.out.println(f);
        }

    }


    @Data
    public static class User {
        @MyAnn("hahhah")
        private String name;
        private int age;
        protected boolean sex;
    }

    public static class VipUser extends User {
        private String name;
    }


}
