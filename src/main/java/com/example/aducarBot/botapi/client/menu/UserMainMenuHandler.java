package com.example.aducarBot.botapi.client.menu;

import com.example.aducarBot.bean.Bot;
import com.example.aducarBot.botapi.BotState;
import com.example.aducarBot.botapi.InputMessageHandler;
import com.example.aducarBot.cache.UserDataCache;
import com.example.aducarBot.service.UserMainMenuService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class UserMainMenuHandler implements InputMessageHandler {
    private Bot myBot;
    private UserDataCache userDataCache;
    @Autowired
    private UserMainMenuService userMainMenuService;

    public UserMainMenuHandler(@Lazy Bot myBot, UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
        this.myBot = myBot;

    }

    @SneakyThrows
    @Override
    public void handle(Message message) {

        userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.MAIN_MENU);
        myBot.execute(userMainMenuService.getUserMainMenuMessage(message.getChatId()));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MAIN_MENU;
    }


}
