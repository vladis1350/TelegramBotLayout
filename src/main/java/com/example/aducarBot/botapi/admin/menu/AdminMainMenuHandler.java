package com.example.aducarBot.botapi.admin.menu;

import com.example.aducarBot.bean.Bot;
import com.example.aducarBot.botapi.BotState;
import com.example.aducarBot.botapi.InputMessageHandler;
import com.example.aducarBot.cache.UserDataCache;
import com.example.aducarBot.service.AdminMainMenuService;
import com.example.aducarBot.service.ReplyMessagesService;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AdminMainMenuHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private AdminMainMenuService adminMainMenuService;
    private UserDataCache userDataCache;
    private Bot myBot;

    public AdminMainMenuHandler(ReplyMessagesService messagesService, AdminMainMenuService adminMainMenuService, UserDataCache userDataCache, @Lazy Bot myBot) {
        this.myBot = myBot;
        this.messagesService = messagesService;
        this.adminMainMenuService = adminMainMenuService;
        this.userDataCache = userDataCache;
    }

    @SneakyThrows
    @Override
    public void handle(Message message) {

        userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.SHOW_ADMIN_MAIN_MENU);
        myBot.execute(adminMainMenuService.getAdminMainMenuMessage(message.getChatId(),
                messagesService.getReplyText("reply.ShowAdminMainMenu")));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADMIN_MAIN_MENU;
    }


}
