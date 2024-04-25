package com.skypro.ShelterPetTelegramBot.service.impl.bot_service;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.PotentialParent;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.PotentialParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.ReportRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.BasicMethods;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.SendingNotifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.answers.Reminders.*;

/**
 * Класс {@link SendingNotificationsImpl}
 * является сервисным классом для отправки уведомлений
 */
@Slf4j
@Service
public class SendingNotificationsImpl implements SendingNotifications {

    private final BasicMethods methods;
    private final PotentialParentRepository potentialParentRepository;
    private final ParentRepository parentRepository;
    private final ReportRepository reportRepository;

    public SendingNotificationsImpl(BasicMethods methods,
                                    PotentialParentRepository potentialParentRepository,
                                    ParentRepository parentRepository,
                                    ReportRepository reportRepository) {

        this.methods = methods;
        this.potentialParentRepository = potentialParentRepository;
        this.parentRepository = parentRepository;
        this.reportRepository = reportRepository;
    }

    @Override
    @Scheduled(cron = "0 0 18-21 * * *")
    public void sendReminder() {

        String answer;
        SendMessage message;

        List<Parent> parents = parentRepository.findAll();
        List<PotentialParent> recipients = potentialParentRepository.findAll();

        for (Parent parent : parents) {

            String firstName = parent.getFirstName();
            String lastName = parent.getLastName();
            String petName = parent.getPet().getName();

            Report report = reportRepository.getByParentFirstNameContainsIgnoreCaseAndParentLastNameContainsIgnoreCaseAndParentPetNameContainsIgnoreCaseAndDate(firstName, lastName, petName, LocalDate.now());

            for (PotentialParent recipient : recipients) {

                if (recipient.getFirstName().equalsIgnoreCase(firstName)
                        & recipient.getLastName().equalsIgnoreCase(lastName)) {

                    Long chatId = recipient.getChatId();
                    String phoneNumber = recipient.getPhoneNumber();

                    if (report == null) {
                        answer = REMINDER_NULL_REPORT(firstName, lastName, phoneNumber, petName);
                        message = new SendMessage(chatId.toString(), answer);
                        methods.sendText(message);
                        log.info("ОТПРАВЛЕНО НАПОМИНАНИЕ УСЫНОВИТЕЛЮ {} {} {}, ЧТО ОН НЕ ОТПРАВИЛ СЕГОДНЯ ОТЧЕТ", firstName, lastName, chatId);

                    } else if (report.getPhoto() == null | report.getDocument() == null) {
                        answer = REMINDER_NULL_ATTACHMENT(firstName, lastName, phoneNumber, petName);
                        message = new SendMessage(chatId.toString(), answer);
                        methods.sendText(message);
                        log.info("ОТПРАВЛЕНО НАПОМИНАНИЕ УСЫНОВИТЕЛЮ {} {} {}, ЧТО СЕГОДНЯШНИЙ ОТЧЕТ НЕ ПОЛОН", firstName, lastName, chatId);
                    }
                }
            }
        }
    }

    @Override
    @Scheduled(cron = "0 0 18-21 * * *")
    public void sendWarning() {

        String answer;
        SendMessage message;

        List<Parent> parents = parentRepository.findAll();
        List<PotentialParent> recipients = potentialParentRepository.findAll();

        for (Parent parent : parents) {

            String firstName = parent.getFirstName();
            String lastName = parent.getLastName();
            String petName = parent.getPet().getName();

            Collection<Report> reports = reportRepository.getByParentFirstNameContainsIgnoreCaseAndParentLastNameContainsIgnoreCaseAndParentPetNameContainsIgnoreCase(firstName, lastName, petName);
            Report report = null;

            for (Report el : reports) {
                report = el;
            }

            if (report != null) {

                for (PotentialParent recipient : recipients) {

                    if (recipient.getFirstName().equalsIgnoreCase(firstName)
                            & recipient.getLastName().equalsIgnoreCase(lastName)) {

                        Long chatId = recipient.getChatId();
                        String phoneNumber = recipient.getPhoneNumber();

                        if (report.getDate().isBefore(LocalDate.now().minusDays(1))) {
                            LocalDate date = report.getDate();
                            answer = WARNING(firstName, lastName, phoneNumber, petName, date);
                            message = new SendMessage(chatId.toString(), answer);
                            methods.sendText(message);
                            log.info("ОТПРАВЛЕНО ПРЕДУПРЕЖДЕНИЕ УСЫНОВИТЕЛЮ {} {} {}, ЧТО ОН НЕ СОБЛЮДАЕТ ТРЕБОВАНИЯ ОТПРАВКИ ОТЧЕТА", firstName, lastName, chatId);
                        }
                    }
                }
            }
        }
    }
}