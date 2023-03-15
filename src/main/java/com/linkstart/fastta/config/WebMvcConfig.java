package com.linkstart.fastta.config;

import com.linkstart.fastta.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 配置自定义资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/manage/**").addResourceLocations("classpath:/frontend/manage/");
        registry.addResourceHandler("/fastta/client/**").addResourceLocations("classpath:/frontend/client/");
    }

    /**
     * 拓展spring mvc框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("加载自定义配置消息转换器……");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Json将对象转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面配置的消息转换器对象追加到spring mvc框架的转换器集合中，index配置为0设定最高优先级
        converters.add(0, messageConverter);
    }
}
