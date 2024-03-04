package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.PetService;
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
import static com.skypro.ShelterPetTelegramBot.utils.documentation.PetControllerDoc.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.EXAMPLE_GET_BY_ID_SHELTER_CODE_404;

/**
 * Класс {@link PetController}
 * является контроллером для обработки запросов, связанных с животным
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %s. Error: %s", e.getStatusCode(), e.getStatusText());
    }

    @Operation(
            summary = "Добавление нового животного",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Pet.class),
                                    examples = @ExampleObject(EXAMPLE_PET)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_ADD_PET_CODE_400)
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
    public Pet add(@Parameter(description = "Тип животного")
                   @RequestParam(name = "Тип") PetType type,

                   @Parameter(description = "Статус животного")
                   @RequestParam(name = "Статус") PetStatus status,

                   @Parameter(description = "Имя животного")
                   @RequestParam(name = "Имя") String name,

                   @Parameter(description = "id приюта для животных")
                   @RequestParam(name = "Приют") Long shelterId) {

        return service.add(type, status, name, shelterId);
    }

    @Operation(
            summary = "Получение животного по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Pet.class),
                                    examples = @ExampleObject(EXAMPLE_PET)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_PET_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_PET_CODE_404)
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
    public Pet getById(@Parameter(description = "id животного")
                       @PathVariable(name = "id") Long id) {

        return service.getById(id);
    }

    @Operation(
            summary = "Получение списка животных по имени, статусу, типу или id приюта для животных",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class)),
                                    examples = @ExampleObject(EXAMPLE_ARRAYS_PETS)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_ALL_PETS_BY_PARAMETERS_CODE_400)
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
    public Collection<Pet> getAllByParameters(@Parameter(description = "Тип животного")
                                              @RequestParam(required = false, name = "Тип") PetType type,

                                              @Parameter(description = "Статус животного")
                                              @RequestParam(required = false, name = "Статус") PetStatus status,

                                              @Parameter(description = "Имя животного")
                                              @RequestParam(required = false, name = "Имя") String name,

                                              @Parameter(description = "id приюта для животных")
                                              @RequestParam(required = false, name = "Приют") Long shelterId) {

        return service.getAllByParameters(type, status, name, shelterId);
    }

    @Operation(
            summary = "Получение списка всех животных",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class)),
                                    examples = @ExampleObject(EXAMPLE_ARRAYS_PETS)
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
    public Collection<Pet> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Изменение данных животного по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Pet.class),
                                    examples = @ExampleObject(EXAMPLE_PET)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_EDIT_PET_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_EDIT_PET_CODE_404)
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
    public Pet edit(@Parameter(description = "id животного")
                    @PathVariable(name = "id") Long id,

                    @Parameter(description = "Тип животного")
                    @RequestParam(required = false, name = "Тип") PetType type,

                    @Parameter(description = "Статус животного")
                    @RequestParam(required = false, name = "Статус") PetStatus status,

                    @Parameter(description = "Имя животного")
                    @RequestParam(required = false, name = "Имя") String name,

                    @Parameter(description = "id приюта для животных")
                    @RequestParam(required = false, name = "Приют") Long shelterId) {

        return service.edit(id, type, status, name, shelterId);
    }

    @Operation(
            summary = "Удаление животного по id",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = SUCCESSFUL,
                            content = @Content(
                                    schema = @Schema(implementation = Pet.class),
                                    examples = @ExampleObject(EXAMPLE_PET)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_PET_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EXAMPLE_GET_BY_ID_PET_CODE_404)
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
    public Pet delete(@Parameter(description = "id животного")
                      @PathVariable(name = "id") Long id) {
        return service.delete(id);
    }
}