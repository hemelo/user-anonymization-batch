package com.hetech.batch.anonymization.useranonymization.seeder;

import com.github.javafaker.Faker;
import com.hetech.batch.anonymization.useranonymization.models.User;
import com.hetech.batch.anonymization.useranonymization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            User user = new User();
            user.setName(faker.name().fullName());
            user.setEmail(faker.internet().emailAddress());
            user.setPhone(faker.phoneNumber().phoneNumber());
            user.setAddress(faker.address().fullAddress());
            user.setAnonymized(false);
            users.add(user);
        }

        userRepository.saveAll(users);
    }
}
