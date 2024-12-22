package org.example.avtodiller.configurations;
import org.example.avtodiller.interceptors.HandlerInterceptorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final HandlerInterceptorAdapter handlerInterceptorAdapter;

    public WebConfig(HandlerInterceptorAdapter handlerInterceptorAdapter) {
        this.handlerInterceptorAdapter = handlerInterceptorAdapter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptorAdapter).excludePathPatterns("/api/auth/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
