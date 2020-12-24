package com.example.aducarBot.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CurrentDate {

    public static String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

    public static LocalDate getCurrentLocalDate(){
        return LocalDate.now();
    }


    public static String formatLocalDate(LocalDate localDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return localDate.format(formatter);
    }
}
