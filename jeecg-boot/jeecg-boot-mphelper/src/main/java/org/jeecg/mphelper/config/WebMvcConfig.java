package org.jeecg.mphelper.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Nisus Liu
 * @version 0.0.1
 * @email liuhejun108@163.com
 * @date 2018/10/23 20:01
 */
@Slf4j
// @Configuration
// @ComponentScan
public class WebMvcConfig implements WebMvcConfigurer {
    // @Autowired
    // protected ModelVoArgumentResolver modelVoArgumentResolver;

    // @Autowired
    // protected RequestMappingHandlerAdapter adapter;
    //
    // //手动强制将自己的Resolver放到前面
    // @PostConstruct
    // public void injectSelfMethodArgumentResolver() {
    //     List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
    //     //先加我的
    //     argumentResolvers.add(modelVoArgumentResolver);
    //     log.trace("Add custom `ModelVoArgumentResolver` success.");
    //     //再加默认的
    //     argumentResolvers.addAll(adapter.getArgumentResolvers());
    //     log.trace("Add default ArgumentResolvers success.");
    //     adapter.setArgumentResolvers(argumentResolvers);
    // }


    /*Del: 自定义的Resolver只能排在后面
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        //其中this.argumentResolvers为LinkedList变量, 传入这里的resolves是0
        //List<HandlerMethodArgumentResolver> custResolvers = new ArrayList<>();


        resolvers.add(modelArgumentResolver);
        log.trace("Add ModelVoArgumentResolver success.");

        //resolvers.add(new ModelVoArgumentResolver());
    }*/


}
