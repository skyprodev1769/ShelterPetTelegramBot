package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
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
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.VolunteerControllerDoc.ARG_SHELTER;

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
            summary = ADD_PET,
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
                                    examples = @ExampleObject(ADD_PET_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_SHELTER_CODE_404)
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
    public Pet add(
            @Parameter(description = TYPE_PET)
            @RequestParam(name = ARG_TYPE) PetType type,

            @Parameter(description = STATUS_PET)
            @RequestParam(name = ARG_STATUS) PetStatus status,

            @Parameter(
                    description = NAME_PET,
                    content = @Content(examples = @ExampleObject(EXAMPLE_NAME)))
            @RequestParam(name = ARG_NAME) String name,

            @Parameter(
                    description = ID_SHELTER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @RequestParam(name = ARG_SHELTER) Long shelterId) {

        return service.add(type, status, name, shelterId);
    }

    @Operation(
            summary = GET_PET,
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
                                    examples = @ExampleObject(GET_PET_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_PET_CODE_404)
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
    public Pet getById(
            @Parameter(
                    description = ID_PET,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id) {

        return service.getById(id);
    }

    @Operation(
            summary = GET_ALL_PETS_BY_PARAMETERS,
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
                                    examples = @ExampleObject(GET_ALL_PETS_BY_PARAMETERS_CODE_400)
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
    public Collection<Pet> getAllByParameters(
            @Parameter(description = TYPE_PET)
            @RequestParam(required = false, name = ARG_TYPE) PetType type,

            @Parameter(description = STATUS_PET)
            @RequestParam(required = false, name = ARG_STATUS) PetStatus status,

            @Parameter(
                    description = NAME_PET,
                    content = @Content(examples = @ExampleObject(EXAMPLE_NAME)))
            @RequestParam(required = false, name = ARG_NAME) String name,

            @Parameter(
                    description = ID_SHELTER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @RequestParam(required = false, name = ARG_SHELTER) Long shelterId) {

        return service.getAllByParameters(type, status, name, shelterId);
    }

    @Operation(
            summary = GET_ALL_PETS,
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
            summary = EDIT_PET,
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
                                    examples = @ExampleObject(EDIT_PET_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EDIT_PET_CODE_404)
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

    @PutMapping("{ID}")
    public Pet edit(
            @Parameter(
                    description = ID_PET,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id,

            @Parameter(description = TYPE_PET)
            @RequestParam(required = false, name = ARG_TYPE) PetType type,

            @Parameter(description = STATUS_PET)
            @RequestParam(required = false, name = ARG_STATUS) PetStatus status,

            @Parameter(
                    description = NAME_PET,
                    content = @Content(examples = @ExampleObject(EXAMPLE_NAME)))
            @RequestParam(required = false, name = ARG_NAME) String name,

            @Parameter(
                    description = ID_SHELTER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @RequestParam(required = false, name = ARG_SHELTER) Long shelterId) {

        return service.edit(id, type, status, name, shelterId);
    }

    @Operation(
            summary = DELETE_PET,
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
                                    examples = @ExampleObject(GET_PET_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_PET_CODE_404)
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

    @DeleteMapping("{ID}")
    public Pet delete(
            @Parameter(
                    description = ID_PET,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id) {
        return service.delete(id);
    }
}