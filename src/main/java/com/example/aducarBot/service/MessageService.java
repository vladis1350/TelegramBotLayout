package com.example.aducarBot.service;

import com.example.aducarBot.bean.Bot;
import com.example.aducarBot.fitnessDB.bean.*;
import com.example.aducarBot.fitnessDB.finish.FinishPhotos;
import com.example.aducarBot.fitnessDB.repository.*;
import com.example.aducarBot.fitnessDB.repository.finish.FinishPhotoRepo;
import com.example.aducarBot.fitnessDB.repository.marathonRepo.MarathonRepository;
import com.example.aducarBot.fitnessDB.service.ListGoalsService;
import com.example.aducarBot.fitnessDB.service.ListUserGoalsService;
import com.example.aducarBot.fitnessDB.service.UserPhotoService;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MessageService {

    private static final Logger log = Logger.getLogger(MessageService.class);

    @Autowired
    private ListUserGoalsRepository userGoalsRepository;

    @Autowired
    private ListUserGoalsService listUserGoalsService;

    @Autowired
    private UserProfileImpl userProfileRepo;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private UserPhotoWeigherRepo userPhotoWeigherRepo;

    @Autowired
    private UserPhotoService userPhotoService;

    @Autowired
    private ListGoalsRepository listGoalsRepo;

    @Autowired
    private FinishPhotoRepo finishPhotoRepo;

    @Autowired
    private ListGoalsService listGoalsService;

    @Autowired
    private MarathonRepository marathonRepository;

    private Bot myBot;

    public MessageService(@Lazy Bot myBot) {
//        this.replyMessagesService = replyMessagesService;
        this.myBot = myBot;
    }

    @SneakyThrows
    public void botStarted() {
        myBot.execute(new SendMessage((long) 1331718111, "STARTED!"));
    }

    @SneakyThrows
    public void updateDateInDB() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = formatter.format(date);
        List<ListGoals> goals = listGoalsRepo.findAll();
        ListGoals listGoals = listGoalsRepo.findListGoalsByTimeStamp(currentDate);
        if (goals.size() == 0 && listGoals == null) {
            return;
        }
        if (listGoals != null && goals.size() > 1) {
            listGoalsRepo.deleteAll();
            listGoalsRepo.save(listGoals);
        } else {
            listGoals = goals.get(0);
            Date date2 = formatter.parse(listGoals.getTimeStamp());
            if (date.compareTo(date2) > 0) {
                listGoals.setTimeStamp(currentDate);
                listGoalsRepo.save(listGoals);
            }
        }
    }

    public void newDayNewListUserGoals() {
        List<User> userList = userRepository.findAll();
        String currentDate = CurrentDate.getCurrentDate();
        ListUserGoals listUserGoals = null;
        for (User user : userList) {
            if (userGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate) == null) {
                listUserGoals = ListUserGoals.builder()
                        .user(user)
                        .timeStamp(currentDate)
                        .totalTasks(listGoalsService.countGoalsToday())
                        .build();
                userGoalsRepository.save(listUserGoals);
            }
        }
    }

    public void newDayNewPhotoUserReport() {
        List<User> userList = userRepository.findAll();

        String currentDate = CurrentDate.getCurrentDate();
        UserPhoto userPhoto = null;
        for (User user : userList) {
            if (userPhotoRepository.findUserPhotoByTimeStampAndUser(currentDate, user) == null) {
                userPhoto = UserPhoto.builder()
                        .user(user)
                        .imageCategory("eat")
                        .timeStamp(currentDate)
                        .build();
                userPhotoRepository.save(userPhoto);
            }
        }
    }

    public void newMondayNewPhotoUserReportWeigher() {
        List<User> userList = userRepository.findAll();

        String currentDate = CurrentDate.getCurrentDate();
        UserPhotoWeigher userPhotoWeigher = null;
        for (User user : userList) {
            if (userPhotoWeigherRepo.findUserPhotoWeigherByTimeStampAndUser(currentDate, user) == null) {
                userPhotoWeigher = UserPhotoWeigher.builder()
                        .user(user)
                        .timeStamp(currentDate)
                        .build();
                userPhotoWeigherRepo.save(userPhotoWeigher);
            }
        }
    }

    public void nexDayMarathon() {
        LocalTime now = LocalTime.now();
        List<UserProfile> userProfiles = userProfileRepo.findAll();
        MarathonPeriod marathonPeriod = marathonRepository
                .findMarathonPeriodByFinishMarathonDateIsAfter(CurrentDate.getCurrentLocalDate());
        if (marathonPeriod != null) {
            int day = marathonPeriod.getCurrentDayMarathon();
            marathonPeriod.setCurrentDayMarathon(++day);
            marathonRepository.save(marathonPeriod);
        }
        int day = 0;
        if (now.getHour() == 0 && now.getMinute() < 10) {
            for (UserProfile userProfile : userProfiles) {
                day = userProfile.getDaysOfTheMarathon();
                userProfile.setDaysOfTheMarathon(++day);
                userProfileRepo.save(userProfile);
            }
        }
    }

    @SneakyThrows
    public void remindSendPhotoInMonday() {
        List<User> users = userRepository.findAll();
        MarathonPeriod marathonPeriod = marathonRepository
                .findMarathonPeriodByFinishMarathonDate(CurrentDate.getCurrentLocalDate());
        MarathonPeriod curentMarathon = marathonRepository
                .findMarathonPeriodByFinishMarathonDateIsAfter(CurrentDate.getCurrentLocalDate());
        if (marathonPeriod == null && (curentMarathon.getCurrentDayMarathon() < 14 || curentMarathon.getCurrentDayMarathon() > 17)) {
            for (User user : users) {
                if (user.getChatId() != 748582406) {
//                if (user.getChatId() == 683992434) {
                    myBot.execute(new SendMessage(user.getChatId(), "НАПОМИНАНИЕ!!! \n\nСегодня нужно отправить фото весов на тощак!!!"));
                }
            }
        }
    }


    @SneakyThrows
    public void createFinishPhotosTable() {
        List<User> userList = userRepository.findAll();
        FinishPhotos finishPhotos = null;
        for (User user : userList) {
            if (finishPhotoRepo.findFinishPhotosByUser(user) == null) {
                finishPhotos = FinishPhotos.builder()
                        .user(user)
                        .build();
                finishPhotoRepo.save(finishPhotos);
            }
        }
        for (User user : userList) {
            if (user.getChatId() != 748582406) {
                myBot.execute(getFinishButtonsAndMessage(user.getChatId()));
            }
        }
    }

    public void remindToCompleteTasks() {
        List<User> users = userRepository.findAll();
        UserPhoto userPhoto;
        ListUserGoals listUserGoals;
        String textRemind = "";
        String startTextRemind = "Внимание!!!\n\nВы сегодня: \n\n";
        int quantityPhoto = 0;
        int quantityGoals = 0;
        for (User user : users) {
            if (user.getChatId() != 748582406) {
                userPhoto = userPhotoRepository.findUserPhotoByTimeStampAndUser(CurrentDate.getCurrentDate(), user);
                listUserGoals = userGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, CurrentDate.getCurrentDate());
                if (userPhoto != null) {
                    quantityPhoto = userPhotoService.getCountPhotos(userPhoto);
                    if (quantityPhoto < 3) {
                        textRemind = textRemind.concat("Отправили фото " + userPhotoService.getCountPhotos(userPhoto) + " из 3\n");
                    }
                } else {
                    textRemind = textRemind.concat("Отправили фото 0 из 3\n");
                }
                if (listUserGoals != null) {
                    quantityGoals = listUserGoalsService.countDoneTasks(listUserGoals);
                    if (quantityGoals < listGoalsService.countGoalsToday()) {
                        textRemind = textRemind.concat("Выполнили заданий " + listUserGoalsService.countDoneTasks(listUserGoals) +
                                " из " + listGoalsService.countGoalsToday());
                    }
                } else {
                    textRemind = textRemind.concat("Выполнили заданий 0 из " + listGoalsService.countGoalsToday());
                }
                if (quantityGoals < listGoalsService.countGoalsToday() || quantityPhoto < 3) {
                    try {
                        myBot.execute(new SendMessage(user.getChatId(), startTextRemind + textRemind));
                    } catch (TelegramApiException e) {
                        log.error(e);
                    }
                }
            }
            textRemind = "";
        }
    }


    public SendMessage getFinishButtonsAndMessage(long chatId) {
        String message = "Сегодня последний день марафона загрузите пожалуйста свои текущие данные";
        return new SendMessage(chatId, message).setReplyMarkup(getFinishButtons());
    }

    private InlineKeyboardMarkup getFinishButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonFinishBodyParam = new InlineKeyboardButton().setText("Заполнить параметры тела");
        InlineKeyboardButton buttonFinishPhotoBody = new InlineKeyboardButton().setText("Отправить фото тела");
        InlineKeyboardButton buttonFinishPhotoWeigher = new InlineKeyboardButton().setText("Отправить фото весов");


        //Every button must have callBackData, or else not work !
        buttonFinishBodyParam.setCallbackData("buttonFinishBodyParam");
        buttonFinishPhotoBody.setCallbackData("buttonFinishPhotoBody");
        buttonFinishPhotoWeigher.setCallbackData("buttonFinishPhotoWeigher");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();

        keyboardButtonsRow1.add(buttonFinishBodyParam);
        keyboardButtonsRow2.add(buttonFinishPhotoBody);
        keyboardButtonsRow3.add(buttonFinishPhotoWeigher);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
