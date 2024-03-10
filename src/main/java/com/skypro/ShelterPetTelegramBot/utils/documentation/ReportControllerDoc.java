package com.skypro.ShelterPetTelegramBot.utils.documentation;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.INVALIDE_INPUT;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.REPORT_NOT_FOUND;

public class ReportControllerDoc {

    public static final String GET_REPORT = "Получение отчета о животном по ID";
    public static final String GET_ATTACHMENT = "Получение вложенного файла для отчета о животном по ID и типу файла";
    public static final String GET_ALL_REPORTS_BY_PARAMETERS = "Получение списка отчетов о животных по дате получения или статусу";
    public static final String GET_ALL_REPORTS = "Получение списка всех отчетов о животных";

    public static final String ID_REPORT = "ID отчета о животном";
    public static final String TYPE_FILE = "Тип вложенного файла";
    public static final String STATUS_REPORT = "Статус отчета";
    public static final String DATE = "Дата получения отчета";

    public static final String ARG_DATE = "Дата";

    public static final String EXAMPLE_DATE = "2024-02-28";

    public static final String EXAMPLE_REPORT =
            """
                    {
                      "id": 1,
                      "photo": "path_file.PNG",
                      "document": "path_file.TXT",
                      "status": "NOT_VIEWED"
                    }""";

    public static final String EXAMPLE_ARRAYS_REPORTS =
            """
                    [
                      {
                        "id": 1,
                        "photo": "path_file.PNG",
                        "document": "path_file.TXT",
                        "status": "NOT_VIEWED"
                      }
                    ]
                                        
                    []""";

    public static final String EXAMPLE_GET_ATTACHMENT = "file.png или file.txt";

    public static final String ID_REPORT_INCORRECT = "ПРИЧИНА: Передан некорректный ID отчета.\n";
    public static final String ID_REPORT_NOT_FOUND = "ПРИЧИНА: Отчет с данным ID еще не был добавлен.\n";

    public static final String GET_REPORT_CODE_400 =
            ID_REPORT_INCORRECT + INVALIDE_INPUT;

    public static final String GET_REPORT_CODE_404 =
            ID_REPORT_NOT_FOUND + REPORT_NOT_FOUND;
}