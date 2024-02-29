package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
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
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.*;

/**
 * Класс {@link ShelterController}
 * является контроллером для обработки запросов, связанных с приютом
 */

@RestController
@RequestMapping("/shelter")
public class ShelterController {

    @Autowired
    private ShelterService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %s. Error: %s", e.getStatusCode(), e.getStatusText());
    }

    @Operation(
            summary = "Добавление нового приюта для животных",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Shelter.class),
                                    examples = @ExampleObject(EXAMPLE_SHELTER)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_ADD_SHELTER_CODE_400)
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
    public Shelter add(@Parameter(description = "Тип животного")
                       @RequestParam(name = "Тип") PetType type,

                       @Parameter(description = "Адрес приюта для животных")
                       @RequestParam(name = "Адрес") String address) {

        return service.add(type, address);
    }

    @Operation(
            summary = "Получение приюта для животных по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Shelter.class),
                                    examples = @ExampleObject(EXAMPLE_SHELTER)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_SHELTER_CODE_400)
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

    @GetMapping("{id}")
    public Shelter getById(@Parameter(description = "id приюта для животных")
                           @PathVariable(name = "id") Long id) {

        return service.getById(id);
    }

    @Operation(
            summary = "Получение списка приютов для животных по типу или адресу",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Shelter.class)),
                                    examples = @ExampleObject(EXAMPLE_ARRAYS_SHELTERS)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_ALL_SHELTERS_BY_PARAMETERS_CODE_400)
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
    public Collection<Shelter> getAllByParameters(@Parameter(description = "Тип животного")
                                                  @RequestParam(required = false, name = "Тип") PetType type,

                                                  @Parameter(description = "Адрес приюта для животных")
                                                  @RequestParam(required = false, name = "Адрес") String address) {

        return service.getAllByParameters(type, address);
    }

    @Operation(
            summary = "Получение списка всех приютов для животных",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Shelter.class)),
                                    examples = @ExampleObject(EXAMPLE_ARRAYS_SHELTERS)
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
    public Collection<Shelter> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Изменение данных приюта для животных по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Shelter.class),
                                    examples = @ExampleObject(EXAMPLE_SHELTER)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_EDIT_SHELTER_CODE_400)
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

    @PutMapping("{id}")
    public Shelter edit(@Parameter(description = "id приюта для животных")
                        @PathVariable(name = "id") Long id,

                        @Parameter(description = "Тип животного")
                        @RequestParam(required = false, name = "Тип") PetType type,

                        @Parameter(description = "Адрес приюта для животных")
                        @RequestParam(required = false, name = "Адрес") String address) {

        return service.edit(id, type, address);
    }

    @Operation(
            summary = "Удаление приюта для животных по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Shelter.class),
                                    examples = @ExampleObject(EXAMPLE_SHELTER)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_SHELTER_CODE_400)
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

    @DeleteMapping("{id}")
    public Shelter delete(@Parameter(description = "id приюта для животных")
                          @PathVariable(name = "id") Long id) {

        return service.delete(id);
    }
}