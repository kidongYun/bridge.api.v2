package com.kidongyun.bridge.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.TypeNameProviderPlugin;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }

    @Component
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
    public static class CustomTypeNameProvider implements TypeNameProviderPlugin {
        @Override
        public String nameFor(Class<?> type) {
            String fullName = type.getName();
            return fullName.substring(fullName.lastIndexOf(".") + 1);
        }

        @Override
        public boolean supports(DocumentationType documentationType) {
            return true;
        }
    }
}
