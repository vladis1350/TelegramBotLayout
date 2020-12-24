package com.example.aducarBot.service.broadcasting;

import com.example.aducarBot.service.MessageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class BroadcastService {

    @Autowired
    private MessageService messageService;

    private boolean isBroadcasting = false;

    @SneakyThrows
    private void broadcast() {
        while (true) {
            log.info("while true cycling");
            LocalTime now = LocalTime.now();
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            calendar.setTime(date);
            long nowHour = (23 - now.getHour()) * 3600000;
            long nowMinute = (59 - now.getMinute()) * 60000;
            int nowSecond = (60 - now.getSecond()) * 1000;
            long timeToUpdate = nowHour + nowMinute + nowSecond;
            int nowDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
            System.out.println(nowDayWeek);
            if (nowDayWeek != 5 && nowDayWeek != 7) {
                if (nowDayWeek == 1) {
                    messageService.newMondayNewPhotoUserReportWeigher();
                    log.info("newMondayNewPhotoUserReportWeigher() - WORKED");
                }
                Thread.sleep(timeToUpdate);
                messageService.updateDateInDB();
                log.info("updateDateInDB() - WORKED");
                messageService.newDayNewListUserGoals();
                log.info("newDayNewListUserGoals()  - WORKED");
                messageService.newDayNewPhotoUserReport();
                log.info("newDayNewPhotoUserReport() - WORKED");
                messageService.nexDayMarathon();
                log.info("nexDayMarathon() - WORKED");
            } else {
                Thread.sleep(timeToUpdate);
                messageService.nexDayMarathon();
                log.info("messageService.nexDayMarathon() - WORKED");
            }
        }
    }

    public void startBroadcasting() {
        if (!isBroadcasting) {
            isBroadcasting = true;
            log.info("start broadcasting");
            broadcast();
        }
    }


}