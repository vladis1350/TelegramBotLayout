package com.example.aducarBot.bean;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

/**
 * Данные анкеты пользователя
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class UserProfileData {
    String name;
    double height;
    double weight;
    String age;
    String date;
    double neck;
    double arm;
    double chest;
    double waist;
    double belly;
    double thighs;
    double thigh;
    double shin;

    @Override
    public String toString() {
        return String.format("%s%nФИО:  %s%nВозраст:  %s%nВес:  %s%nРост:  %s%nШея:  %s%nРука:  %s%nГрудь:  " +
                        "%s%nТалия:  %s%nЖивот:  %s%nБёдра:  %s%nБедро:  %s%nГолень:  %s%nДата заполнения:  %s",
                "Ваши данные: ", getName(), getAge(), getWeight(), getHeight(), getNeck(), getArm(),
                getChest(), getWeight(), getBelly(), getThighs(), getThigh(), getShin(), getDate());
    }
}
