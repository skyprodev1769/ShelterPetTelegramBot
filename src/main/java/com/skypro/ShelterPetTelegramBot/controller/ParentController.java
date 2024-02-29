package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ParentService;
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
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ParentControllerDoc.*;

/**
 * Класс {@link ParentController}
 * является контроллером для обработки запросов, связанных с усыновителем
 */
@RestController
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    private ParentService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %s. Error: %s", e.getStatusCode(), e.getStatusText());
    }

    @Operation(
            summary = "Добавление нового усыновителя",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Parent.class),
                                    examples = @ExampleObject(EXAMPLE_PARENT)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_ADD_PARENT_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_ADD_PARENT_CODE_404)
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
    public Parent add(@Parameter(description = "Имя усыновителя")
                      @RequestParam(name = "Имя") String firstName,

                      @Parameter(description = "Фамилия усыновителя")
                      @RequestParam(name = "Фамилия") String lastName,

                      @Parameter(description = "Номер телефона усыновителя")
                      @RequestParam(name = "Номер") String phoneNumber,

                      @Parameter(description = "Имя животного")
                      @RequestParam(name = "Животное") String petName) {

        return service.add(firstName, lastName, phoneNumber, petName);
    }

    @Operation(
            summary = "Получение усыновителя по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Parent.class),
                                    examples = @ExampleObject(EXAMPLE_PARENT)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_PARENT_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_PARENT_CODE_404)
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
    public Parent getById(@Parameter(description = "id усыновителя")
                          @PathVariable(name = "id") Long id) {

        return service.getById(id);
    }

    @Operation(
            summary = "Получение списка усыновителей по имени, фамилии или номеру телефона",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Parent.class)),
                                    examples = @ExampleObject(EXAMPLE_ARRAYS_PARENTS)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_ALL_PARENTS_BY_PARAMETERS_CODE_400)
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
    public Collection<Parent> getAllByParameters(@Parameter(description = "Имя усыновителя")
                                                 @RequestParam(required = false, name = "Имя") String firstName,

                                                 @Parameter(description = "Фамилия усыновителя")
                                                 @RequestParam(required = false, name = "Фамилия") String lastName,

                                                 @Parameter(description = "Номер телефона усыновителя")
                                                 @RequestParam(required = false, name = "Номер") String phoneNumber) {

        return service.getAllByParameters(firstName, lastName, phoneNumber);
    }

    @Operation(
            summary = "Получение списка всех усыновителей",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Parent.class)),
                                    examples = @ExampleObject(EXAMPLE_ARRAYS_PARENTS)
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
    public Collection<Parent> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Изменение данных усыновителя по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Parent.class),
                                    examples = @ExampleObject(EXAMPLE_PARENT)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_EDIT_PARENT_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_EDIT_PARENT_CODE_404)
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
    public Parent edit(@Parameter(description = "id усыновителя")
                       @PathVariable(name = "id") Long id,

                       @Parameter(description = "Имя усыновителя")
                       @RequestParam(required = false, name = "Имя") String firstName,

                       @Parameter(description = "Фамилия усыновителя")
                       @RequestParam(required = false, name = "Фамилия") String lastName,

                       @Parameter(description = "Номер телефона усыновителя")
                       @RequestParam(required = false, name = "Номер") String phoneNumber,

                       @Parameter(description = "Имя животного")
                       @RequestParam(required = false, name = "Животное") String petName) {

        return service.edit(id, firstName, lastName, phoneNumber, petName);
    }

    @Operation(
            summary = "Удаление усыновителя по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Parent.class),
                                    examples = @ExampleObject(EXAMPLE_PARENT)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_PARENT_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_PARENT_CODE_404)
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
    public Parent delete(@Parameter(description = "id усыновителя")
                         @PathVariable(name = "id") Long id) {

        return service.delete(id);
    }
}