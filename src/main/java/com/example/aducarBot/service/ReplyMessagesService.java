package com.example.aducarBot.service;

import com.example.aducarBot.service.localeMessageServices.LocaleMessageService;
import com.example.aducarBot.service.localeMessageServices.LocaleMondayMessageService;
import com.example.aducarBot.utils.Emojis;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Формирует готовые ответные сообщения в чат.
 */
@Service
public class ReplyMessagesService {

    private LocaleMessageService localeMessageService;

    private LocaleMondayMessageService localeMondayMessageService;

    public ReplyMessagesService(LocaleMessageService messageService, LocaleMondayMessageService localeMondayMessageService) {
        this.localeMessageService = messageService;
        this.localeMondayMessageService = localeMondayMessageService;
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage) {
        return new SendMessage(chatId, localeMessageService.getMessage(replyMessage));
    }

    public SendMessage getReplyMondayMessage(long chatId, String replyMessage) {
        return new SendMessage(chatId, localeMondayMessageService.getMessage(replyMessage));
    }

    public SendMessage getReplyMondayMessage(long chatId, String replyMessage, Object... args) {
        return new SendMessage(chatId, localeMondayMessageService.getMessage(replyMessage, args));
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage, Object... args) {
        return new SendMessage(chatId, localeMessageService.getMessage(replyMessage, args));
    }

    public String getReplyText(String replyText) {
        return localeMessageService.getMessage(replyText);
    }

    public String getReplyMondayText(String replyText) {
        return localeMondayMessageService.getMessage(replyText);
    }

    public String getReplyMessage(String replyText, Emojis emojis) {
        return localeMessageService.getMessage(replyText, Emojis.ARROW_DOWN);
    }
}
