package com.gnk2so.chatroom.user.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gnk2so.chatroom.user.model.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMock {

    public static User build() {
        return User.builder()
            .name("Mocked User")
            .email("mocked.user@email.com")
            .password("p@ssw0rd")
            .build();
    }

    public static User withIdFrom(User user) {
        return User.builder()
            .id(new Random().nextLong())
            .name(user.getName())
            .email(user.getEmail())
            .password(user.getPassword())
            .build();
    }

	public static User buildWithId() {
		return User.builder()
            .id(new Random().nextLong())
            .name("Mocked User")
            .email("mocked.user@email.com")
            .password("p@ssw0rd")
            .build();
	}

	public static User buildSecuredFrom(User user) {
        String password = user.getPassword();
        String encryptedPassword = encrypt(password);
        return User.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .password(encryptedPassword)
            .build();
	}

    private static String encrypt(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

	public static User buildSecured() {
		return buildSecuredFrom(build());
	}

    public static List<User> buildList(int size) {
        List<User> users = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            User user = User.builder()
                .id(Long.valueOf(index))
                .name("Mocked User " + index)
                .email("mocked.user@email.com")
                .password("p@ssw0rd")
                .build();
            users.add(user);
        }
        return users;
    }
}
