package org.jeecg.mphelper.annotation;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import org.jeecg.common.system.query.QueryRuleEnum;

import java.lang.annotation.*;

/**
 * 用于拼接 mybatis-plus sql 片段
 * @version
 * @author dafei
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


    /**含义参见 {@link FieldStrategy}
     *
     * 注意: 暂 不支持 {@link FieldStrategy#DEFAULT} , 因为不方便读取全局配置.
     * @see https://mp.baomidou.com/guide/faq.html#%E6%8F%92%E5%85%A5%E6%88%96%E6%9B%B4%E6%96%B0%E7%9A%84%E5%AD%97%E6%AE%B5%E6%9C%89-%E7%A9%BA%E5%AD%97%E7%AC%A6%E4%B8%B2-%E6%88%96%E8%80%85-null
     *
     * @return
     */
    FieldStrategy strategy() default FieldStrategy.NOT_NULL;

}
