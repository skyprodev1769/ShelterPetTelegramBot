package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
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
            summary = ADD_SHELTER,
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
                                    examples = @ExampleObject(ADD_SHELTER_CODE_400)
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
    public Shelter add(
            @Parameter(description = TYPE_SHELTER)
            @RequestParam(name = ARG_TYPE) PetType type,

            @Parameter(
                    description = ADDRESS,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ADDRESS)))
            @RequestParam(name = ARG_ADDRESS) String address) {

        return service.add(type, address);
    }

    @Operation(
            summary = GET_SHELTER,
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
                                    examples = @ExampleObject(GET_SHELTER_CODE_400)
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

    @GetMapping("{ID}")
    public Shelter getById(
            @Parameter(
                    description = ID_SHELTER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id) {

        return service.getById(id);
    }

    @Operation(
            summary = GET_ALL_SHELTERS_BY_PARAMETERS,
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
                                    examples = @ExampleObject(GET_ALL_SHELTERS_BY_PARAMETERS_CODE_400)
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
    public Collection<Shelter> getAllByParameters(
            @Parameter(description = TYPE_SHELTER)
            @RequestParam(required = false, name = ARG_TYPE) PetType type,

            @Parameter(
                    description = ADDRESS,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ADDRESS)))
            @RequestParam(required = false, name = ARG_ADDRESS) String address) {

        return service.getAllByParameters(type, address);
    }

    @Operation(
            summary = GET_ALL_SHELTERS,
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
            summary = EDIT_SHELTER,
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
                                    examples = @ExampleObject(EDIT_SHELTER_CODE_400)
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

    @PutMapping("{ID}")
    public Shelter edit(
            @Parameter(
                    description = ID_SHELTER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id,

            @Parameter(description = TYPE_SHELTER)
            @RequestParam(required = false, name = ARG_TYPE) PetType type,

            @Parameter(
                    description = ADDRESS,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ADDRESS)))
            @RequestParam(required = false, name = ARG_ADDRESS) String address) {

        return service.edit(id, type, address);
    }

    @Operation(
            summary = DELETE_SHELTER,
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
                                    examples = @ExampleObject(GET_SHELTER_CODE_400)
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

    @DeleteMapping("{ID}")
    public Shelter delete(
            @Parameter(
                    description = ID_SHELTER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id) {

        return service.delete(id);
    }
}