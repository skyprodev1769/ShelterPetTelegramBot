package com.skypro.ShelterPetTelegramBot.tests.service;

import com.skypro.ShelterPetTelegramBot.configuration.AppConfiguration;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.PotentialParent;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.RecordingContactsImpl;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.RecordingContacts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordingContactsImplTest {

    private final RecordingContacts contacts = new RecordingContactsImpl();
    private final AppConfiguration configuration = new AppConfiguration();
    private final Pattern PATTERN = configuration.getPattern();

    @Test
    void recordContact_success() {
        String text = String.format("%s %s %s", FIRST_NAME, LAST_NAME, PHONE_NUMBER);

        PotentialParent expected = new PotentialParent();
        expected.setChatId(ID);
        expected.setFirstName(FIRST_NAME);
        expected.setLastName(LAST_NAME);
        expected.setPhoneNumber(PHONE_NUMBER);

        Matcher matcher = PATTERN.matcher(text);
        matcher.matches();

        PotentialParent actual = contacts.recordContact(ID, matcher);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForRecordContactTest")
    void checkPhoneNumber_fail(String text) {
        Matcher matcher = PATTERN.matcher(text);
        matcher.matches();

        assertThrows(IllegalStateException.class,
                () -> contacts.recordContact(ID, matcher));
    }

    private static Stream<Arguments> provideParamsForRecordContactTest() {
        return Stream.of(
                Arguments.of(" "),
                Arguments.of(String.format("%s%s%s", FIRST_NAME, LAST_NAME, PHONE_NUMBER)),
                Arguments.of(String.format("%s %s%s", FIRST_NAME, LAST_NAME, PHONE_NUMBER)),
                Arguments.of(String.format("%s%s %s", FIRST_NAME, LAST_NAME, PHONE_NUMBER)),
                Arguments.of(String.format("%s %s %s", FIRST_NAME, LAST_NAME, INCORRECT_STRING)),
                Arguments.of(String.format("%s %s %s", FIRST_NAME, LAST_NAME, SHORT_PHONE_NUMBER)),
                Arguments.of(String.format("%s %s %s", FIRST_NAME, LAST_NAME, LONG_PHONE_NUMBER))
        );
    }
}