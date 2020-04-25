package org.jeecg.mphelper.annotation;

import java.lang.annotation.*;

/**
 * 注解在实体类上, 表示开启 {@link MpField} 规则解析.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MpEntity {
}
