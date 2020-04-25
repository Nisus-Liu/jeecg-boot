package org.jeecg.mphelper.helper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * @author dafei
 * @version 0.1
 * @date 2020/4/26 0:12
 */
public class MpWrapperHolder {

    private static final ThreadLocal holder = new ThreadLocal();

    public void set(Wrapper val) {
        holder.set(val);
    }

    public Wrapper get() {
        return (Wrapper) holder.get();
    }
}
