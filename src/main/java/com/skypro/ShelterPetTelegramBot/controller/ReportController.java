package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.enums.FileType;
import com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDate;
import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.utils.documentation.Codes.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.PetControllerDoc.ARG_STATUS;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ReportControllerDoc.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.*;

/**
 * Класс {@link ReportController}
 * является контроллером для обработки запросов, связанных с отчетом о животном
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %s. Error: %s", e.getStatusCode(), e.getStatusText());
    }

    @Operation(
            summary = GET_REPORT,
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Report.class),
                                    examples = @ExampleObject(EXAMPLE_REPORT)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_REPORT_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_REPORT_CODE_404)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(ERROR_SERVER)
                            )
                    )
            }
    )

    @GetMapping("{ID}")
    public Report getById(
            @Parameter(
                    description = ID_REPORT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id) {

        return service.getById(id);
    }

    @Operation(
            summary = GET_ATTACHMENT,
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Report.class),
                                    examples = @ExampleObject(EXAMPLE_GET_ATTACHMENT)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_REPORT_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_REPORT_CODE_404)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(ERROR_SERVER)
                            )
                    )
            }
    )

    @GetMapping("/attachment/{ID}")
    public void getAttachmentById(
            @Parameter(
                    description = ID_REPORT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id,

            @Parameter(description = TYPE_FILE)
            @RequestParam(name = ARG_TYPE) FileType type,

            HttpServletResponse response) {

        service.getAttachmentById(id, type, response);
    }

    @Operation(
            summary = GET_ALL_REPORTS_BY_PARAMETERS,
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class)),
                                    examples = @ExampleObject(EXAMPLE_ARRAYS_REPORTS)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(ERROR_SERVER)
                            )
                    )
            }
    )

    @GetMapping
    public Collection<Report> getAllByParameters(
            @Parameter(
                    description = DATE,
                    content = @Content(examples = @ExampleObject(EXAMPLE_DATE)))
            @RequestParam(required = false, name = ARG_DATE) LocalDate date,

            @Parameter(description = STATUS_REPORT)
            @RequestParam(required = false, name = ARG_STATUS) ReportStatus status) {

        return service.getAllByParameters(date, status);
    }

    @Operation(
            summary = GET_ALL_REPORTS,
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class)),
                                    examples = @ExampleObject(EXAMPLE_ARRAYS_REPORTS)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(ERROR_SERVER)
                            )
                    )
            }
    )

    @GetMapping("/all")
    public Collection<Report> getAll() {
        return service.getAll();
    }
}