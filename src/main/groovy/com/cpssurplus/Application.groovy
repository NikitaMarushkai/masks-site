package com.cpssurplus

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver

@SpringBootApplication
class Application {

    static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }

}
