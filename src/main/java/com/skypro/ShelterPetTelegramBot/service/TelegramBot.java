package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.WorkInBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.Commands.*;
import static com.skypro.ShelterPetTelegramBot.utils.Descriptions.*;

/**
 * Класс {@link TelegramBot}
 * является основным сервисным классом для телеграм бота
 */
@Slf4j
@Service
public final class TelegramBot extends TelegramLongPollingBot {

    private final BotConfiguration botConfiguration;
    private final WorkInBot work;

    public TelegramBot(BotConfiguration botConfiguration,
                       @Lazy WorkInBot work) {

        super(botConfiguration.getToken());
        this.botConfiguration = botConfiguration;
        this.work = work;
        createMainMenu();
    }

    @Override
    public String getBotUsername() {
        return botConfiguration.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        work.work(update);
    }

    /**
     * Метод создает меню с основными командами: <br>
     *
     * <b>/start</b> <br>
     * <b>/help</b> <br>
     * <b>/settings</b>
     */
    private void createMainMenu() {
        List<BotCommand> listOfCommands = new ArrayList<>();

        listOfCommands.add(new BotCommand(START, DESCRIPTION_START));
        listOfCommands.add(new BotCommand(HELP, DESCRIPTION_HELP));
        listOfCommands.add(new BotCommand(SETTINGS, DESCRIPTION_SETTINGS));

        SetMyCommands commands = new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null);

        try {
            this.execute(commands);
        } catch (TelegramApiException e) {
            log.error("ОШИБКА СОЗДАНИЯ ГЛАВНОГО МЕНЮ: {}", e.getMessage());
        }
    }
}