package com.example.aducarBot.appconfig;

import com.example.aducarBot.bean.Bot;
import com.example.aducarBot.botapi.admin.telegramAdminFacade.TelegramAdminFacade;
import com.example.aducarBot.botapi.client.teleframUserFacade.TelegramUserFacade;
import com.example.aducarBot.service.broadcasting.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;


@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String botUserName;
    private String botToken;

    @Bean
    public Bot myBot(TelegramUserFacade telegramUserFacade, TelegramAdminFacade telegramAdminFacade) {
        Bot myBot = new Bot(telegramUserFacade, telegramAdminFacade);
        myBot.setBotUserName(botUserName);
        myBot.setBotToken(botToken);
        return myBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
