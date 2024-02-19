package com.skypro.ShelterPetTelegramBot.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CreatingKeyBoards {

    SendMessage createKeyBoardForUnregisteredUsers(Long chatId, String text);

    SendMessage createKeyBoardForRegisteredUsers(Long chatId, String text);

    SendMessage createKeyBoardForDetailedInfoAboutShelter(Long chatId, String text);
}