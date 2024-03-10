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
import static com.skypro.ShelterPetTelegramBot.utils.documentation.PetControllerDoc.EXAMPLE_NAME;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.PetControllerDoc.NAME_PET;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.ARG_ID;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.EXAMPLE_ID;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.VolunteerControllerDoc.*;

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
            summary = ADD_PARENT,
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
                                    examples = @ExampleObject(ADD_PARENT_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(ADD_PARENT_CODE_404)
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
    public Parent add(
            @Parameter(
                    description = FIRST_NAME_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_FIRST_NAME)))
            @RequestParam(name = ARG_FIRST_NAME) String firstName,

            @Parameter(
                    description = LAST_NAME_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_LAST_NAME)))
            @RequestParam(name = ARG_LAST_NAME) String lastName,

            @Parameter(
                    description = PHONE_NUMBER_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_PHONE_NUMBER)))
            @RequestParam(name = ARG_PHONE_NUMBER) String phoneNumber,

            @Parameter(
                    description = NAME_PET,
                    content = @Content(examples = @ExampleObject(EXAMPLE_NAME)))
            @RequestParam(name = ARG_PET) String petName) {

        return service.add(firstName, lastName, phoneNumber, petName);
    }

    @Operation(
            summary = GET_PARENT,
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
                                    examples = @ExampleObject(GET_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_CODE_404)
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
    public Parent getById(
            @Parameter(
                    description = ID_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id) {

        return service.getById(id);
    }

    @Operation(
            summary = GET_ALL_PARENTS_BY_PARAMETERS,
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
                                    examples = @ExampleObject(GET_ALL_PARENTS_BY_PARAMETERS_CODE_400)
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
    public Collection<Parent> getAllByParameters(
            @Parameter(
                    description = FIRST_NAME_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_FIRST_NAME)))
            @RequestParam(required = false, name = ARG_FIRST_NAME) String firstName,

            @Parameter(
                    description = LAST_NAME_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_LAST_NAME)))
            @RequestParam(required = false, name = ARG_LAST_NAME) String lastName,

            @Parameter(
                    description = PHONE_NUMBER_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_SEARCH_PHONE_NUMBER)))
            @RequestParam(required = false, name = ARG_PHONE_NUMBER) String phoneNumber) {

        return service.getAllByParameters(firstName, lastName, phoneNumber);
    }

    @Operation(
            summary = GET_ALL_PARENTS,
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
            summary = EDIT_PARENT,
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
                                    examples = @ExampleObject(EDIT_PARENT_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EDIT_PARENT_CODE_404)
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
    public Parent edit(
            @Parameter(
                    description = ID_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id,

            @Parameter(
                    description = FIRST_NAME_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_FIRST_NAME)))
            @RequestParam(required = false, name = ARG_FIRST_NAME) String firstName,

            @Parameter(
                    description = LAST_NAME_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_LAST_NAME)))
            @RequestParam(required = false, name = ARG_LAST_NAME) String lastName,

            @Parameter(
                    description = PHONE_NUMBER_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_PHONE_NUMBER)))
            @RequestParam(required = false, name = ARG_PHONE_NUMBER) String phoneNumber,

            @Parameter(
                    description = NAME_PET,
                    content = @Content(examples = @ExampleObject(EXAMPLE_NAME)))
            @RequestParam(required = false, name = ARG_PET) String petName) {

        return service.edit(id, firstName, lastName, phoneNumber, petName);
    }

    @Operation(
            summary = DELETE_PARENT,
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
                                    examples = @ExampleObject(GET_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_CODE_404)
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
    public Parent delete(
            @Parameter(
                    description = ID_PARENT,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id) {

        return service.delete(id);
    }
}