package com.aoenu.file.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * This is Description
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/28
 */
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(new ByteArrayHttpMessageConverter());
    }
}
