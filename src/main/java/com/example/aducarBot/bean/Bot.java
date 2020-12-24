package com.example.aducarBot.bean;

import com.example.aducarBot.botapi.admin.telegramAdminFacade.TelegramAdminFacade;
import com.example.aducarBot.botapi.client.teleframUserFacade.TelegramUserFacade;
import com.example.aducarBot.botapi.verify.Verify;
import com.example.aducarBot.fitnessDB.bean.User;
import com.example.aducarBot.fitnessDB.blackList.BlackList;
import com.example.aducarBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.aducarBot.fitnessDB.repository.blackListRepo.BlackListRepository;
import com.example.aducarBot.service.broadcasting.BroadcastService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;


@Getter
@Setter
public class Bot extends TelegramLongPollingBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private MessagesMonday messagesMonday;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private BlackListRepository blackListRepository;

    @Autowired
    private BroadcastService broadcastService;

    private TelegramUserFacade telegramUserFacade;
    private TelegramAdminFacade telegramAdminFacade;

    public Bot(TelegramUserFacade telegramUserFacade, TelegramAdminFacade telegramAdminFacade) {
        this.telegramAdminFacade = telegramAdminFacade;
        this.telegramUserFacade = telegramUserFacade;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        int userId = 0;
        if (update.getEditedMessage() != null) {
            userId = update.getEditedMessage().getFrom().getId();
        } else if (update.getCallbackQuery() == null) {
            userId = update.getMessage().getFrom().getId();
        } else if (update.getMessage() == null) {
            userId = update.getCallbackQuery().getFrom().getId();
        }
        BlackList blackList = blackListRepository.findBlackListByChatId(userId);
        User user = userRepository.findUserByChatId(userId);
        if (Verify.isAdmin(user)) {
            telegramAdminFacade.handleUpdate(update);
        } else if (blackList != null) {
            execute(new SendMessage((long) userId, "Вы занесены в черный список!"));
        } else {
            telegramUserFacade.handleUpdate(update);
        }
    }

    @Override
    public String getBotUsername() {
        new Thread(messagesMonday::startReminder).start();
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @SneakyThrows
    public void sendPhoto(long chatId, String imageCaption, String imagePath) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(imagePath);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(imageCaption);
        execute(sendPhoto);
    }

    @SneakyThrows
    public void getReport(long chatId) {
        SendDocument sendDocument = new SendDocument();
        File file = ResourceUtils.getFile("root/fitnessBot/target/resources/reports/report.xls");
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(file);
        execute(sendDocument);
    }

    @SneakyThrows
    public void getFinalReport(long chatId) {
        SendDocument sendDocument = new SendDocument();
        File file = ResourceUtils.getFile("root/fitnessBot/target/resources/reports/finalReport.xls");
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(file);
        execute(sendDocument);
    }

    @SneakyThrows
    public void sendPhoto(long chatId, String photoId) {
        SendPhoto send = new SendPhoto();
        send.setChatId(chatId);
        send.setPhoto(photoId);
        execute(send);
    }

    @SneakyThrows
    public void sendPage(long chatId, String photoId) {
        SendPhoto send = new SendPhoto();
        send.setChatId(chatId);
        send.setPhoto(photoId);
        execute(send);
    }

    @SneakyThrows
    public void sendVideoNote(long chatId, Message message) {
        SendVideoNote send = new SendVideoNote();
        send.setChatId(chatId);
        send.setVideoNote(message.getVideoNote().getFileId());
        execute(send);
    }

    @SneakyThrows
    public void sendVoice(long chatId, Message message) {
        SendVoice send = new SendVoice();
        send.setChatId(chatId);
        send.setVoice(message.getVoice().getFileId());
        execute(send);
    }

    @SneakyThrows
    public void sendListMessages(List<SendMessage> sendMessageList) {
        for (SendMessage message : sendMessageList) {
            execute(message);
            Thread.sleep(5000);
        }
    }

}
