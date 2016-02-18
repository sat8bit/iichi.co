package co.iichi;

import co.iichi.web.filter.AuthenticationRequiredFilter;
import co.iichi.web.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    SessionHandler sessionHandler;

    @Autowired
    AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor);
    }

    @Bean
    public FilterRegistrationBean authenticationRequiredFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(getAuthenticationRequiredFilter());
        filterRegistrationBean.setOrder(1);

        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/accounts/*");
        filterRegistrationBean.setUrlPatterns(urlPatterns);
        return filterRegistrationBean;
    }

    protected AuthenticationRequiredFilter getAuthenticationRequiredFilter() {
        return new AuthenticationRequiredFilter(sessionHandler);
    }
}
