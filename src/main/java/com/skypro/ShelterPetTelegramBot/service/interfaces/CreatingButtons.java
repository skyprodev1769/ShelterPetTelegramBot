package com.skypro.ShelterPetTelegramBot.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CreatingButtons {

    SendMessage createButtonsForChoiceRegistration(Long chatId, String answer);

    SendMessage createButtonForRegistration(Long chatId, String answer);

    SendMessage createButtonsForChoiceShelter(Long chatId, String answer);

    SendMessage createButtonForGetInfoAboutShelter(Long chatId, String answer);

    SendMessage createButtonsForGetInfoAboutProcess(Long chatId, String answer);

    SendMessage createButtonForCallVolunteer(Long chatId, String answer);
}