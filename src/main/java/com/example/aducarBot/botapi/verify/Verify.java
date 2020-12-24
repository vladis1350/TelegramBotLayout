package com.example.aducarBot.botapi.verify;

import com.example.aducarBot.fitnessDB.bean.User;

public class Verify {
    public static boolean isAdmin(User user) {
        if (user != null) {
            if (user.getRole() != null) {
                return user.getRole().equals("admin");
            }
        }
        return false;
    }
}
