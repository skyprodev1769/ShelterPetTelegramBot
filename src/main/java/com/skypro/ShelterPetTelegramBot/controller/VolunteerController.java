package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.VolunteerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.utils.documentation.Codes.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.EXAMPLE_GET_BY_ID_SHELTER_CODE_404;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.VolunteerControllerDoc.*;

/**
 * Класс {@link VolunteerController}
 * является контроллером для обработки запросов, связанных с волонтером
 */
@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    @Autowired
    private VolunteerService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %s. Error: %s", e.getStatusCode(), e.getStatusText());
    }

    @Operation(
            summary = "Добавление нового волонтера",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Volunteer.class),
                                    examples = @ExampleObject(EXAMPLE_VOLUNTEER)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_ADD_VOLUNTEER_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_SHELTER_CODE_404)
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

    @PostMapping
    public Volunteer add(@Parameter(description = "Имя волонтера")
                         @RequestParam(name = "Имя") String firstName,

                         @Parameter(description = "Фамилия волонтера")
                         @RequestParam(name = "Фамилия") String lastName,

                         @Parameter(description = "Номер телефона волонтера")
                         @RequestParam(name = "Номер") String phoneNumber,

                         @Parameter(description = "id приюта для животных")
                         @RequestParam(name = "id") Long shelterId) {

        return service.add(firstName, lastName, phoneNumber, shelterId);
    }

    @Operation(
            summary = "Получение волонтера по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Volunteer.class),
                                    examples = @ExampleObject(EXAMPLE_VOLUNTEER)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_VOLUNTEER_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_VOLUNTEER_CODE_404)
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

    @GetMapping("{id}")
    public Volunteer getById(@Parameter(description = "id волонтера")
                             @PathVariable(name = "id") Long id) {

        return service.getById(id);
    }

    @Operation(
            summary = "Получение списка волонтеров по имени, фамилии, номеру телефона или id приюта для животных",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Volunteer.class)),
                                    examples = @ExampleObject(EXAMPLE_ARRAYS_VOLUNTEERS)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_ALL_VOLUNTEERS_BY_PARAMETERS_CODE_400)
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
    public Collection<Volunteer> getAllByParameters(@Parameter(description = "Имя волонтера")
                                                    @RequestParam(required = false, name = "Имя") String firstName,

                                                    @Parameter(description = "Фамилия волонтера")
                                                    @RequestParam(required = false, name = "Фамилия") String lastName,

                                                    @Parameter(description = "Номер телефона волонтера")
                                                    @RequestParam(required = false, name = "Номер") String phoneNumber,

                                                    @Parameter(description = "id приюта для животных")
                                                    @RequestParam(required = false, name = "id") Long shelterId) {

        return service.getAllByParameters(firstName, lastName, phoneNumber, shelterId);
    }

    @Operation(
            summary = "Получение списка всех волонтеров",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Volunteer.class)),
                                    examples = @ExampleObject(EXAMPLE_ARRAYS_VOLUNTEERS)
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
    public Collection<Volunteer> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Изменение данных волонтера по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Volunteer.class),
                                    examples = @ExampleObject(EXAMPLE_VOLUNTEER)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_EDIT_VOLUNTEER_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_EDIT_VOLUNTEER_CODE_404)
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

    @PutMapping("{id}")
    public Volunteer edit(@Parameter(description = "id волонтера")
                          @PathVariable(name = "id") Long id,

                          @Parameter(description = "Имя волонтера")
                          @RequestParam(required = false, name = "Имя") String firstName,

                          @Parameter(description = "Фамилия волонтера")
                          @RequestParam(required = false, name = "Фамилия") String lastName,

                          @Parameter(description = "Номер телефона волонтера")
                          @RequestParam(required = false, name = "Номер") String phoneNumber,

                          @Parameter(description = "id приюта для животных")
                          @RequestParam(required = false, name = "id") Long shelterId) {

        return service.edit(id, firstName, lastName, phoneNumber, shelterId);
    }

    @Operation(
            summary = "Удаление волонтера по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Volunteer.class),
                                    examples = @ExampleObject(EXAMPLE_VOLUNTEER)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_VOLUNTEER_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_VOLUNTEER_CODE_404)
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

    @DeleteMapping("{id}")
    public Volunteer delete(@Parameter(description = "id волонтера")
                            @PathVariable(name = "id") Long id) {

        return service.delete(id);
    }
}