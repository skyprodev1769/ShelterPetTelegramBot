package com.skypro.ShelterPetTelegramBot.service.impl;

import com.skypro.ShelterPetTelegramBot.model.entity.PotentialParent;
import com.skypro.ShelterPetTelegramBot.service.interfaces.RecordingContacts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

/**
 * Класс {@link RecordingContactsImpl}
 * является сервисным классом для записи контактных данных потенциальных усыновителей {@link PotentialParent}
 */
@Slf4j
@Component
public class RecordingContactsImpl implements RecordingContacts {

    @Override
    public final PotentialParent recordContact(Long chatId, Matcher matcher) {
        String firstName = matcher.group(1);
        String lastName = matcher.group(3);
        String phoneNumber = matcher.group(5);

        PotentialParent parent = new PotentialParent();

        parent.setPhoneNumber(phoneNumber);
        parent.setChatId(chatId);
        parent.setFirstName(firstName);
        parent.setLastName(lastName);

        log.info("ЗАПИСАНЫ КОНТАКТНЫЕ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ {}: {} {} {}", chatId, firstName, lastName, phoneNumber);
        return parent;
    }
}