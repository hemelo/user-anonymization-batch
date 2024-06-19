package com.hetech.batch.anonymization.useranonymization.processors;

import com.hetech.batch.anonymization.useranonymization.models.User;
import com.hetech.batch.anonymization.useranonymization.utils.AESUtil;
import org.springframework.batch.item.ItemProcessor;
import javax.crypto.SecretKey;

public class UserItemProcessor implements ItemProcessor<User, User> {

    private SecretKey secretKey;

    public UserItemProcessor(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public User process(User user) throws Exception {
        user.setName(anonymize(user.getName()));
        user.setEmail(anonymizeEmail(user.getEmail()));
        user.setPhone(anonymize(user.getPhone()));
        user.setAddress(anonymize(user.getAddress()));
        return user;
    }

    private String anonymize(String data) {
        try {
            return AESUtil.encrypt(data, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    private String anonymizeEmail(String email) {
        String[] parts = email.split("@");
        return "anon@" + parts[1];
    }
}
