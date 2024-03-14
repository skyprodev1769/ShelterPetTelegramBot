package com.skypro.ShelterPetTelegramBot.service.impl.bot_service;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.repository.PetRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.ShelterRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.VolunteerRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.SendingLists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForGeneralCommands.REACTION_TO_CALL_VOLUNTEER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutAnyShelterCommands.REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutProcess.REACTION_TO_LIST_PETS;

/**
 * Класс {@link SendingListsImpl}
 * является сервисным классом для отправки запрашиваемых списков
 */
@Slf4j
@Service
public class SendingListsImpl implements SendingLists {

    private final ShelterRepository shelterRepository;
    private final VolunteerRepository volunteerRepository;
    private final PetRepository petRepository;

    public SendingListsImpl(ShelterRepository shelterRepository,
                            VolunteerRepository volunteerRepository,
                            PetRepository petRepository) {

        this.shelterRepository = shelterRepository;
        this.volunteerRepository = volunteerRepository;
        this.petRepository = petRepository;
    }

    @Override
    public StringBuilder sendListPets(Long chatId,
                                      String userFirstName,
                                      Boolean isDogShelter) {

        StringBuilder dogs = new StringBuilder(REACTION_TO_LIST_PETS(userFirstName) + "\n\n");
        StringBuilder cats = new StringBuilder(REACTION_TO_LIST_PETS(userFirstName) + "\n\n");

        for (Pet pet : petRepository.findAll()) {

            String data = String.format("%s\nПриют - %s\n", pet.getName(), pet.getShelter().getAddress());

            if (pet.getStatus() == PetStatus.FREE) {

                if (pet.getType() == DOG) {
                    dogs.append(data).append("\n");

                } else {
                    cats.append(data).append("\n");
                }
            }
        }

        if (isDogShelter) {
            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ СПИСОК СОБАК ДЛЯ УСЫНОВЛЕНИЯ", chatId, userFirstName);
            return dogs;

        } else {
            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ СПИСОК КОШЕК ДЛЯ УСЫНОВЛЕНИЯ", chatId, userFirstName);
            return cats;
        }
    }

    @Override
    public StringBuilder sendListVolunteers(Long chatId, String userFirstName) {

        StringBuilder list = new StringBuilder(REACTION_TO_CALL_VOLUNTEER(userFirstName)).append("\n\n");

        for (Volunteer volunteer : volunteerRepository.findAll()) {

            String data = String.format("%s %s\nПриют - %s\n", volunteer.getPhoneNumber(), volunteer.getFirstName(), volunteer.getShelter().getAddress());
            list.append(data).append("\n");
        }

        log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ КОНТАКТЫ ВОЛОНТЕРОВ", chatId, userFirstName);
        return list;
    }

    @Override
    public StringBuilder sendListShelters(Long chatId,
                                          String userFirstName,
                                          Boolean isDogShelter) {

        StringBuilder shelterDog = new StringBuilder(REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS(userFirstName) + "\n\n");
        StringBuilder shelterCat = new StringBuilder(REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS(userFirstName) + "\n\n");

        for (Shelter shelter : shelterRepository.findAll()) {

            if (shelter.getType() == DOG) {
                shelterDog.append(shelter.getAddress()).append("\n");

            } else {
                shelterCat.append(shelter.getAddress()).append("\n");
            }
        }

        if (isDogShelter) {
            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ АДРЕС И РЕЖИМ РАБОТЫ ПРИЮТА ДЛЯ СОБАК", chatId, userFirstName);
            return shelterDog;

        } else {
            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ АДРЕС И РЕЖИМ РАБОТЫ ПРИЮТА ДЛЯ КОШЕК", chatId, userFirstName);
            return shelterCat;
        }
    }
}