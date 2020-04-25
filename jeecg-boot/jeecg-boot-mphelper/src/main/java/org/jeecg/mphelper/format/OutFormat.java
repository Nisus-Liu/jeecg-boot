package org.jeecg.mphelper.format;

import java.util.function.Function;

/**
 * @author dafei
 * @version 0.1
 * @date 2020/4/25 23:43
 */
public interface OutFormat<T,R> extends Function<T,R> {

    /**
     * 标记类, 表示没有配置任何 format 函数
     */
    public abstract static class None implements OutFormat {
        private None(){}
    }
}
