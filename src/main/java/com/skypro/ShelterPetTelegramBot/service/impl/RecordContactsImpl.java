package com.skypro.ShelterPetTelegramBot.service.impl;

import com.skypro.ShelterPetTelegramBot.model.entity.PotentialParent;
import com.skypro.ShelterPetTelegramBot.service.interfaces.RecordContacts;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

/**
 * Класс {@link RecordContactsImpl}
 * является сервисным классом для записи контактных данных потенциальных усыновителей {@link PotentialParent}
 */
@Component
public class RecordContactsImpl implements RecordContacts {

    /**
     * Метод <br> <br>
     * {@code recordContact(Matcher matcher)} <br> <br>
     * <p>
     * Записывает контактные данные потенциального усыновителя {@link PotentialParent}
     *
     * @param matcher <i> является объектом класса {@link Matcher}</i>
     */
    @Override
    public final PotentialParent recordContact(Long chatId, Matcher matcher) {
        String firstName = matcher.group(1);
        String lastName = matcher.group(3);
        String phoneNumber = matcher.group(5);

        PotentialParent parent = new PotentialParent();

        parent.setChatId(chatId);
        parent.setFirstName(firstName);
        parent.setLastName(lastName);
        parent.setPhoneNumber(phoneNumber);

        return parent;
    }
}