package com.darzee.shankh.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        List<Locale> supportedLocales = Arrays.asList(
                new Locale("en"),
                new Locale("hi"),
                new Locale("pa"),
                new Locale("gu"),
                new Locale("mr"),
                new Locale("te"),
                new Locale("bn"),
                new Locale("kn"),
                new Locale("ml"),
                new Locale("or"),
                new Locale("as"),
                new Locale("ta"),
                new Locale("ur")
        );
        Locale.setDefault(supportedLocales.get(0));
        return new AcceptHeaderLocaleResolver(){
             @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String headerLang = request.getHeader("Accept-Language");
            return headerLang == null || headerLang.isEmpty()
                    ? Locale.getDefault()
                    : Locale.lookup(Locale.LanguageRange.parse(headerLang), supportedLocales);
        }
        };
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}