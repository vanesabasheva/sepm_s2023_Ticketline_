package at.ac.tuwien.sepm.groupphase.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
            .addResourceLocations("classpath:/images/")
            .addResourceLocations("file:src/merchImages/")
            .addResourceLocations("file:src/newsImages/")
            .addResourceLocations("file:src/eventImages/");
    }
}
