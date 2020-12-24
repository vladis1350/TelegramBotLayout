package com.example.aducarBot.botapi;


import org.telegram.telegrambots.meta.api.objects.Message;

/**Обработчик сообщений
 */
public interface InputMessageHandler {
    void handle(Message message);

    BotState getHandlerName();
}
