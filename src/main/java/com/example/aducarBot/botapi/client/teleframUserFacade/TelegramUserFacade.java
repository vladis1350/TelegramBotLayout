package com.example.aducarBot.botapi.client.teleframUserFacade;

import com.example.aducarBot.bean.Bot;
import com.example.aducarBot.botapi.BotState;
import com.example.aducarBot.botapi.BotStateContext;
import com.example.aducarBot.cache.UserDataCache;
import com.example.aducarBot.service.ReplyMessagesService;
import com.example.aducarBot.service.UserMainMenuService;
import com.example.aducarBot.service.localeMessageServices.LocaleMessageService;
import com.example.aducarBot.utils.Emojis;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@Getter
@Setter
@Slf4j
public class TelegramUserFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private UserMainMenuService userMainMenuService;
    private Bot myBot;
    private ReplyMessagesService messagesService;

    public TelegramUserFacade(BotStateContext botStateContext, UserDataCache userDataCache, UserMainMenuService userMainMenuService,
                              @Lazy Bot myBot, ReplyMessagesService messagesService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.userMainMenuService = userMainMenuService;
        this.myBot = myBot;
        this.messagesService = messagesService;
    }

    @SneakyThrows
    public void handleUpdate(Update update) {
        SendMessage sendMessage = new SendMessage();
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: " + update.getCallbackQuery().getFrom().getUserName() +
                    ", userId: " + callbackQuery.getFrom().getId() +
                    ", with data: " + update.getCallbackQuery().getData());
            myBot.execute(processCallbackQuery(callbackQuery));
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

    private void handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotState botState;
        SendMessage replyMessage = new SendMessage(message.getChatId(), " ");

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


    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        LocaleMessageService localeMessageService;
        BotApiMethod<?> callBackAnswer = userMainMenuService.getUserMainMenuMessage(chatId);

        if (buttonQuery.getData().equals("buttonInputPersonalInfo")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askName"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);

        }
        return callBackAnswer;
    }

}