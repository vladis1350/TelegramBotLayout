package com.example.aducarBot.botapi.admin.telegramAdminFacade;

import com.example.aducarBot.bean.Bot;
import com.example.aducarBot.botapi.BotState;
import com.example.aducarBot.botapi.BotStateContext;
import com.example.aducarBot.cache.UserDataCache;
import com.example.aducarBot.service.AdminMainMenuService;
import com.example.aducarBot.service.ReplyMessagesService;
import com.example.aducarBot.service.UserMainMenuService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Getter
@Setter
@Slf4j
public class TelegramAdminFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private AdminMainMenuService adminMainMenuService;
    private UserMainMenuService userMainMenuService;
    private Bot myBot;
    private ReplyMessagesService messagesService;


    public TelegramAdminFacade(BotStateContext botStateContext, UserDataCache userDataCache, AdminMainMenuService adminMainMenuService,
                               @Lazy Bot myBot, ReplyMessagesService messagesService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.adminMainMenuService = adminMainMenuService;
        this.myBot = myBot;
        this.messagesService = messagesService;
    }

    @SneakyThrows
    public void handleUpdate(Update update) {

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: " + update.getCallbackQuery().getFrom().getUserName() +
                    ", userId: " + callbackQuery.getFrom().getId() +
                    ", with data: " + update.getCallbackQuery().getData());
            myBot.execute(processCallbackQuery(callbackQuery));
        }

        if (update.hasEditedMessage()) {
            Message message = update.getEditedMessage();
            log.info("Edited message from User: " + message.getFrom().getUserName() +
                    ", userId: " + message.getFrom().getId() +
                    ", chatId: " + message.getChatId() +
                    ", with text: " + message.getText());
            /**handle and send edit messages*/
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User: " + message.getFrom().getUserName() +
                    ", userId: " + message.getFrom().getId() +
                    ", chatId: " + message.getChatId() +
                    ",  with text: " + message.getText());
            handleInputMessage(message);
        }
    }

    @SneakyThrows
    private void handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;

        switch (inputMsg) {
            case "/start":
                botState = BotState.ASK_START;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        botStateContext.processInputMessage(botState, message);
    }

    @SneakyThrows
    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();

        BotApiMethod<?> callBackAnswer = adminMainMenuService.getAdminMainMenuMessage(chatId, "");

        if (buttonQuery.getData().equals("buttonAddGoal")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTimeStampForTask"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_ONE);

        }

        return callBackAnswer;
    }

}
