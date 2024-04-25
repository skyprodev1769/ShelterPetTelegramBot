package com.skypro.ShelterPetTelegramBot.service.impl.bot_service;

import com.skypro.ShelterPetTelegramBot.exception.RecipientNotFoundException;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.PotentialParent;
import com.skypro.ShelterPetTelegramBot.model.enums.MessageContent;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.PotentialParentRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.BasicMethods;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.SendingMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswerForParents.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ReportControllerDoc.EXAMPLE_SEND_MESSAGE;

/**
 * Класс {@link SendingMessageImpl}
 * является сервисным классом для отправки сообщения усыновителю в телеграм бот
 */
@Slf4j
@Service
public class SendingMessageImpl implements SendingMessage {

    private final BasicMethods methods;
    private final PotentialParentRepository potentialParentRepository;
    private final ParentRepository parentRepository;
    private final CheckService service;

    public SendingMessageImpl(BasicMethods methods,
                              PotentialParentRepository potentialParentRepository,
                              ParentRepository parentRepository,
                              CheckService service) {

        this.methods = methods;
        this.potentialParentRepository = potentialParentRepository;
        this.parentRepository = parentRepository;
        this.service = service;
    }

    @Override
    public String sendAnswer(MessageContent content,
                             String firstName,
                             String lastName,
                             String phoneNumber,
                             String petName) {

        service.checkName(firstName);
        service.checkName(lastName);
        service.checkName(petName);
        service.checkPhoneNumber(phoneNumber);

        Parent parent = parentRepository.getByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPetNameContainsIgnoreCase(firstName, lastName, petName)
                .orElseThrow(ParentNotFoundException::new);

        PotentialParent recipient = getRecipient(phoneNumber, parent);
        Long chatId = recipient.getChatId();

        sendMessage(chatId, content, firstName, lastName, phoneNumber, petName);
        return EXAMPLE_SEND_MESSAGE;
    }

    /**
     * Метод находит получателя сообщения по данным усыновителя и номеру телефона
     *
     * @param phoneNumber <i> является номером телефона получателя </i> <br>
     * @param parent      <i> является усыновителем, получающим сообщение </i>
     */
    private PotentialParent getRecipient(String phoneNumber, Parent parent) {

        List<PotentialParent> recipients = potentialParentRepository.findAll();

        for (PotentialParent recipient : recipients) {

            if (recipient.getFirstName().equalsIgnoreCase(parent.getFirstName())
                    & recipient.getLastName().equalsIgnoreCase(parent.getLastName())
                    & recipient.getPhoneNumber().equals(phoneNumber)) {

                log.info("ПОЛУЧАТЕЛЬ С ДАННЫМИ: {} {} {} НАЙДЕН", parent.getFirstName(), parent.getLastName(), phoneNumber);
                return recipient;
            }
        }
        log.info("ПОЛУЧАТЕЛЬ С ДАННЫМИ: {} {} {} НЕ НАЙДЕН", parent.getFirstName(), parent.getLastName(), phoneNumber);
        throw new RecipientNotFoundException();
    }

    /**
     * Метод отправляет сообщение по id чата в телеграм
     *
     * @param chatId      <i> является id чата (его идентификатор в телеграм) </i> <br>
     * @param content     <i> является определителем содержания сообщения </i> <br>
     * @param firstName   <i> является именем получателя </i> <br>
     * @param lastName    <i> является фамилией получателя </i> <br>
     * @param phoneNumber <i> является номером телефона получателя </i> <br>
     * @param petName     <i> является именем усыновленного животного получателя </i>
     */
    private void sendMessage(Long chatId,
                             MessageContent content,
                             String firstName,
                             String lastName,
                             String phoneNumber,
                             String petName) {

        SendMessage message;
        String answer;

        switch (content) {

            case EXTENSION_14 -> {
                answer = REACTION_TO_EXTENSION_14_DAYS(firstName, lastName, phoneNumber, petName);
                message = new SendMessage(chatId.toString(), answer);
                methods.sendText(message);
                log.info("ОТПРАВЛЕНО СООБЩЕНИЕ О ПРОДЛЕНИИ ИСПЫТАТЕЛЬНОГО СРОКА НА 14 ДНЕЙ УСЫНОВИТЕЛЮ {} {} {}", firstName, lastName, chatId);
            }

            case EXTENSION_30 -> {
                answer = REACTION_TO_EXTENSION_30_DAYS(firstName, lastName, phoneNumber, petName);
                message = new SendMessage(chatId.toString(), answer);
                methods.sendText(message);
                log.info("ОТПРАВЛЕНО СООБЩЕНИЕ О ПРОДЛЕНИИ ИСПЫТАТЕЛЬНОГО СРОКА НА 30 ДНЕЙ УСЫНОВИТЕЛЮ {} {} {}", firstName, lastName, chatId);
            }

            case CONGRATULATION -> {
                answer = REACTION_TO_CONGRATULATIONS(firstName, lastName, phoneNumber, petName);
                message = new SendMessage(chatId.toString(), answer);
                methods.sendText(message);
                log.info("ОТПРАВЛЕНО СООБЩЕНИЕ ОБ УСПЕШНОМ ПРОХОЖДЕНИИ ИСПЫТАТЕЛЬНОГО СРОКА УСЫНОВИТЕЛЮ {} {} {}", firstName, lastName, chatId);
            }

            case REFUSAL -> {
                answer = REACTION_TO_REFUSAL(firstName, lastName, phoneNumber, petName);
                message = new SendMessage(chatId.toString(), answer);
                methods.sendText(message);
                log.info("ОТПРАВЛЕНО СООБЩЕНИЕ О НЕУСПЕШНОМ ПРОХОЖДЕНИИ ИСПЫТАТЕЛЬНОГО СРОКА УСЫНОВИТЕЛЮ {} {} {}", firstName, lastName, chatId);
            }
        }
    }
}