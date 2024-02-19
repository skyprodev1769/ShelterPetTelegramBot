package com.skypro.ShelterPetTelegramBot.service.interfaces;

import com.skypro.ShelterPetTelegramBot.model.entity.PotentialParent;

import java.util.regex.Matcher;

public interface RecordContacts {

    PotentialParent recordContact(Long chatId, Matcher matcher);
}