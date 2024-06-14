package cn.org.dialogue.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TokenConfig implements WebMvcConfigurer {
    /**
     * 添加jwt拦截器，并指定拦截路径
     * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor())
                .addPathPatterns(
                        "/user/info",
                        "/user/login/direct",
                        "/user/logout",
                        "/message",
                        "/comment/insert",
                        "/comment/delete",
                        "/article/insert",
                        "/article/update",
                        "/article/delete",
                        "/image/insert",
                        "/image/update",
                        "/image/delete"
                );
    }

    @Bean
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }

}
