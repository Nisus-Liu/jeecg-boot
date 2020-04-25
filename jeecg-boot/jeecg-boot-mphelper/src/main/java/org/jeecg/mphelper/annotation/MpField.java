package org.jeecg.mphelper.annotation;


import org.jeecg.common.system.query.QueryRuleEnum;

import java.lang.annotation.*;

/**
 * 用于拼接 mybatis-plus sql 片段
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MpField {


    /**片段组名, 默认 'ew' 和 MP 和谐
     * @return
     */
    String[] group() default {"ew"};

    /**默认字段名的下划线认为是列名.
     * 区间字段, 如 ageGe, ageLe 就需要指定列名了.
     * 但 age_ge, age_le 就则不用
     * @return
     */
    String columnName() default "";

    /**关系类型 >= <= = ...
     * @return
     */
    QueryRuleEnum queryRule() default QueryRuleEnum.EQ;


    /**是否强制排除
     * @return
     */
    boolean exclude() default false;

}
