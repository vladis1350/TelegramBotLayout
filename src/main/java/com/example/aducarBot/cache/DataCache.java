package com.example.aducarBot.cache;

import com.example.aducarBot.bean.UserProfileData;
import com.example.aducarBot.botapi.BotState;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    UserProfileData getUserProfileData(int userId);

    void saveUserProfileData(int userId, UserProfileData userProfileData);
}
