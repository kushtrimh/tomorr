package com.kushtrimh.tomorr.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
public class ThymeleafConfiguration {

    @Bean
    public ResourceBundleMessageSource mailMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mail/messages");
        return messageSource;
    }

    @Bean
    public ITemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver ();
        templateResolver.setOrder(0);
        templateResolver.setPrefix("mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public ITemplateEngine mailTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(mailMessageSource());
        return templateEngine;
    }
}
