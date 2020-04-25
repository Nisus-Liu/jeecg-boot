package org.jeecg.mphelper.config;

import org.jeecg.mphelper.web.MyMappingJackson2HttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @see https://www.jianshu.com/p/333ed5ee958d
@Configuration
public class HttpMessageConverterConfig {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        MyMappingJackson2HttpMessageConverter converter = new MyMappingJackson2HttpMessageConverter();
        // 源码可知, 除了不可替代的converter, diy的子类会替代内置的.
        // ClassUtils.isAssignableValue(defaultConverter.getClass(), candidate);
        return new HttpMessageConverters(converter);
    }
}