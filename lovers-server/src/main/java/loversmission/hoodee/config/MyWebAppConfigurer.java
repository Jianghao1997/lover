package loversmission.hoodee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Package: com.wwm.onlinestore.config
 * Description：解析前端的图片路径并在“D://uploaded/”这个目录下去找相应的静态资源
 * Author: 蒋豪
 * Date:  2021.04.09 17:37
 * Modified By:
 */
@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {
    /**
     * 图片保存路径，自动从yml文件中获取数据
     */
    @Value("${my-config.file-path}")
    private String fileSavePath;

    @Autowired
    private UserTokenInterceptor userTokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(userTokenInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/upload/image")
                .excludePathPatterns("/upload/avatar/image")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 配置资源映射
         * 意思是：如果访问的资源路径是以“/images/”开头的，
         * 就给我映射到本机的“E:/images/”这个文件夹内，去找你要的资源
         * 注意：E:/images/ 后面的 “/”一定要带上
         */
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("file:" + fileSavePath);
    }
}
