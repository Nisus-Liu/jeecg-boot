package org.jeecg.mphelper.annotation;

import org.jeecg.mphelper.format.InFormat;
import org.jeecg.mphelper.format.OutFormat;

import java.lang.annotation.*;
import java.util.function.Function;

/**
 * @author dafei
 * @version 0.1
 * @date 2020/4/22 16:18
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DeseField {

    String inFormatEl() default "";

    Class<? extends Function> inFormatFun() default InFormat.None.class;

    String outFormatEl() default "";

    Class<? extends Function> outFormatFun() default OutFormat.None.class;

}
