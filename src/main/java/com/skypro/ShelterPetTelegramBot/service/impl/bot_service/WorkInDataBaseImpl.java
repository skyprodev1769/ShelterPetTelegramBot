package com.skypro.ShelterPetTelegramBot.service.impl.bot_service;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.Attachment;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.PotentialParent;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.User;
import com.skypro.ShelterPetTelegramBot.model.enums.FileType;
import com.skypro.ShelterPetTelegramBot.model.repository.AttachmentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.PotentialParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.ReportRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.UserRepository;
import com.skypro.ShelterPetTelegramBot.service.TelegramBot;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.BasicMethods;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.CreatingKeyBoards;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.RecordingContacts;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.WorkInDataBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;

import static com.skypro.ShelterPetTelegramBot.model.enums.FileType.PNG;
import static com.skypro.ShelterPetTelegramBot.model.enums.FileType.TXT;
import static com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus.NOT_VIEWED;
import static com.skypro.ShelterPetTelegramBot.utils.answers.contacts.AnswersForRecordContactsCommands.REACTION_TO_REPEAT_RECORD_CONTACT;
import static com.skypro.ShelterPetTelegramBot.utils.answers.contacts.AnswersForRecordContactsCommands.REACTION_TO_SUCCESSFUL_RECORD_CONTACT;
import static com.skypro.ShelterPetTelegramBot.utils.answers.contacts.AnswersForRemovedContactsCommand.REACTION_TO_REMOVED_CONTACTS_DETAILS;
import static com.skypro.ShelterPetTelegramBot.utils.answers.contacts.AnswersForRemovedContactsCommand.REACTION_TO_REMOVED_EMPTY_CONTACTS_DETAILS;

/**
 * Класс {@link WorkInDataBaseImpl}
 * является сервисным классом для работы с базой данных
 */
@Slf4j
@Service
public class WorkInDataBaseImpl implements WorkInDataBase {

    private final TelegramBot bot;
    private final BasicMethods methods;
    private final UserRepository userRepository;
    private final PotentialParentRepository parentRepository;
    private final ReportRepository reportRepository;
    private final AttachmentRepository attachmentRepository;
    private final RecordingContacts contacts;
    private final CreatingKeyBoards keyBoards;

    public WorkInDataBaseImpl(TelegramBot bot,
                              BasicMethods methods,
                              UserRepository userRepository,
                              PotentialParentRepository parentRepository,
                              ReportRepository reportRepository,
                              AttachmentRepository attachmentRepository,
                              RecordingContacts contacts,
                              CreatingKeyBoards keyBoards) {

        this.bot = bot;
        this.methods = methods;
        this.userRepository = userRepository;
        this.parentRepository = parentRepository;
        this.reportRepository = reportRepository;
        this.attachmentRepository = attachmentRepository;
        this.contacts = contacts;
        this.keyBoards = keyBoards;
    }

    @Override
    public void saveNewUserToDB(Long chatId, String userFirstName) {
        User user = new User();
        Timestamp registerAt = new Timestamp(System.currentTimeMillis());

        user.setChatId(chatId);
        user.setFirstName(userFirstName);
        user.setRegisterAt(registerAt);

        userRepository.save(user);

        log.info("ДОБАВЛЕН НОВЫЙ ПОЛЬЗОВАТЕЛЬ: {} {}", chatId, userFirstName);
    }

    @Override
    public void savePotentialParentToDB(Long chatId,
                                        String userFirstName,
                                        Matcher matcher) {
        String answer;

        PotentialParent parent = contacts.recordContact(chatId, matcher);
        Long id = Long.valueOf(parent.getPhoneNumber());

        if (parentRepository.findById(id).isEmpty()) { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ РАНЕЕ НЕ ОТПРАВЛЯЛ КОНТАКТНЫЕ ДАННЫЕ

            parentRepository.save(parent);
            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПИСАЛСЯ КАК ПОТЕНЦИАЛЬНЫЙ УСЫНОВИТЕЛЬ", chatId, userFirstName);
            answer = REACTION_TO_SUCCESSFUL_RECORD_CONTACT(userFirstName);
            SendMessage message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
            methods.sendText(message);

        } else { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ УЖЕ ОТПРАВЛЯЛ КОНТАКТНЫЕ ДАННЫЕ

            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОПЫТАЛСЯ ПОВТОРНО ОТПРАВИТЬ НОМЕР ТЕЛЕФОНА", chatId, userFirstName);
            answer = REACTION_TO_REPEAT_RECORD_CONTACT(userFirstName);
            methods.reaction(chatId, answer);
        }
    }

    @Override
    public void removePotentialParentFromDB(Long chatId, String userFirstName) {
        String answer;

        List<PotentialParent> parent = parentRepository.getAllByChatId(chatId);

        if (parent.isEmpty()) { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ НЕ ОТПРАВЛЯЛ РАНЕЕ КОНТАКТНЫЕ ДАННЫЕ

            log.info("НЕ НАЙДЕНЫ КОНТАКТНЫЕ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ: {} {}", chatId, userFirstName);
            answer = REACTION_TO_REMOVED_EMPTY_CONTACTS_DETAILS(userFirstName);
            methods.reaction(chatId, answer);

        } else { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ ОТПРАВЛЯЛ РАНЕЕ КОНТАКТНЫЕ ДАННЫЕ

            parentRepository.deleteAll();
            log.info("УДАЛЕНЫ КОНТАКТНЫЕ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ: {} {}", chatId, userFirstName);
            answer = REACTION_TO_REMOVED_CONTACTS_DETAILS(userFirstName);
            SendMessage message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
            methods.sendText(message);
        }
    }

    @Override
    public void saveReport(Parent parent,
                           List<PhotoSize> photos,
                           Document document,
                           String photosDir) {

        String firstName = parent.getFirstName();
        String lastName = parent.getLastName();

        String firstPartFileName = firstName + "_" + lastName + "_" + LocalDate.now();
        String parentDir = photosDir + "/" + firstName + "_" + lastName + "_" + parent.getPet().getName();

        Report report = reportRepository.getByDate(LocalDate.now()).orElseGet(Report::new);

        if (photos != null) { // ЕСЛИ ФОТО ЖИВОТНОГО НЕ ПРИСЛАЛИ
            savePhoto(photos, firstPartFileName, parentDir, report);
        }

        if (document != null) { // ЕСЛИ ДОКУМЕНТ С ОПИСАТЕЛЬНОЙ ЧАСТЬЮ НЕ ПРИСЛАЛИ
            saveDocument(document, firstPartFileName, parentDir, report);
        }

        report.setDate(LocalDate.now());
        report.setStatus(NOT_VIEWED);
        report.setParent(parent);
        reportRepository.save(report);
        log.info("СОХРАНЕН ОТЧЕТ О ЖИВОТНОМ ОТ УСЫНОВИТЕЛЯ: {} {}", firstName, lastName);
    }

    /**
     * Метод сохраняет фото животного в отчет о животном
     *
     * @param photos            <i> являются фото, отправляемыми усыновителем </i> <br>
     * @param firstPartFileName <i> является первой частью названия фото </i> <br>
     * @param parentDir         <i> является директорией сохранения фото </i> <br>
     * @param report            <i> является отчетом о животном </i>
     */
    private void savePhoto(List<PhotoSize> photos, String firstPartFileName, String parentDir, Report report) {

        PhotoSize photo = photos.get(photos.size() - 1);
        GetFile getFile = new GetFile(photo.getFileId());

        FileType type = PNG;
        String name = firstPartFileName + "." + type;
        Path path = Path.of(parentDir, name);

        File file = new File(parentDir, name);

        try {
            bot.downloadFile(bot.execute(getFile), file);
        } catch (TelegramApiException e) {
            log.error("ОШИБКА СОХРАНЕНИЯ ИЗОБРАЖЕНИЯ: {}", e.getMessage());
        }

        Attachment attachment = attachmentRepository.getByPath(path.toString()).orElseGet(Attachment::new);

        attachment.setPath(path.toString());
        attachment.setSize(photo.getFileSize());
        attachment.setType(type);
        attachment.setData(photo.toString().getBytes());

        attachmentRepository.save(attachment);

        report.setPhoto(attachment.getPath());
        log.info("ДОБАВЛЕНО ФОТО {} В ОТЧЕТ О ЖИВОТНОМ {}", name, LocalDate.now());
    }

    /**
     * Метод сохраняет документ с описательной частью в отчет о животном
     *
     * @param document          <i> является документом, отправляемым усыновителем </i> <br>
     * @param firstPartFileName <i> является первой частью названия фото </i> <br>
     * @param parentDir         <i> является директорией сохранения фото </i> <br>
     * @param report            <i> является отчетом о животном </i>
     */
    private void saveDocument(Document document, String firstPartFileName, String parentDir, Report report) {

        GetFile getFile = new GetFile(document.getFileId());

        FileType type = TXT;
        String name = firstPartFileName + "." + type;
        Path path = Path.of(parentDir, name);

        File file = new File(parentDir, name);

        try {
            bot.downloadFile(bot.execute(getFile), file);
        } catch (TelegramApiException e) {
            log.error("ОШИБКА СОХРАНЕНИЯ ДОКУМЕНТА: {}", e.getMessage());
        }

        Attachment attachment = attachmentRepository.getByPath(path.toString()).orElseGet(Attachment::new);

        attachment.setPath(path.toString());
        attachment.setSize(document.getFileSize());
        attachment.setType(type);
        attachment.setData(document.toString().getBytes());

        attachmentRepository.save(attachment);

        report.setDocument(attachment.getPath());
        log.info("ДОБАВЛЕН ДОКУМЕНТ {} В ОТЧЕТ О ЖИВОТНОМ {}", name, LocalDate.now());
    }
}