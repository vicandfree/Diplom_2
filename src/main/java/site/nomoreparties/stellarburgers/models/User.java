package site.nomoreparties.stellarburgers.models;

import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;

    public static User withAllFields() {
        User user = new User();
        user.setEmail(createRandomEmail());
        user.setPassword(createRandomString());
        user.setName(createRandomString());
        return user;
    }

    public static User withoutMail() {
        User user = new User();
        user.setPassword(createRandomString());
        user.setName(createRandomString());
        return user;
    }

    public static User withoutPassword() {
        User user = new User();
        user.setEmail(createRandomEmail());
        user.setName(createRandomString());
        return user;
    }

    public static User withoutName() {
        User user = new User();
        user.setEmail(createRandomEmail());
        user.setPassword(createRandomString());
        return user;
    }

    public static User editData() {
        return new User(createRandomEmail(), "", createRandomString());
    }

    public static String createRandomEmail() {
        return RandomStringUtils.randomAlphanumeric(8) + "@yandex.ru";
    }

    public static String createRandomString() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}