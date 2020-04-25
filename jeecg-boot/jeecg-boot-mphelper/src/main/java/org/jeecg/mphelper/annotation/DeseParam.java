package org.jeecg.mphelper.annotation;

import java.lang.annotation.*;

/**
 * 用于controller参数前, 开启DIY序列化/反序列化处理
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface DeseParam {
}
