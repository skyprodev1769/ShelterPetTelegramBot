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
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.*;
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
            summary = ADD_VOLUNTEER,
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
                                    examples = @ExampleObject(ADD_VOLUNTEER_CODE_400)
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
    public Volunteer add(
            @Parameter(
                    description = FIRST_NAME_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_FIRST_NAME)))
            @RequestParam(name = ARG_FIRST_NAME) String firstName,

            @Parameter(
                    description = LAST_NAME_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_LAST_NAME)))
            @RequestParam(name = ARG_LAST_NAME) String lastName,

            @Parameter(
                    description = PHONE_NUMBER_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_PHONE_NUMBER)))
            @RequestParam(name = ARG_PHONE_NUMBER) String phoneNumber,

            @Parameter(
                    description = ID_SHELTER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @RequestParam(name = ARG_SHELTER) Long shelterId) {

        return service.add(firstName, lastName, phoneNumber, shelterId);
    }

    @Operation(
            summary = GET_VOLUNTEER,
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
                                    examples = @ExampleObject(GET_VOLUNTEER_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_VOLUNTEER_CODE_404)
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
    public Volunteer getById(
            @Parameter(
                    description = ID_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id) {

        return service.getById(id);
    }

    @Operation(
            summary = GET_ALL_VOLUNTEERS_BY_PARAMETERS,
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
                                    examples = @ExampleObject(GET_ALL_VOLUNTEERS_BY_PARAMETERS_CODE_400)
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
    public Collection<Volunteer> getAllByParameters(
            @Parameter(
                    description = FIRST_NAME_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_FIRST_NAME)))
            @RequestParam(required = false, name = ARG_FIRST_NAME) String firstName,

            @Parameter(
                    description = LAST_NAME_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_LAST_NAME)))
            @RequestParam(required = false, name = ARG_LAST_NAME) String lastName,

            @Parameter(
                    description = PHONE_NUMBER_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_SEARCH_PHONE_NUMBER)))
            @RequestParam(required = false, name = ARG_PHONE_NUMBER) String phoneNumber,

            @Parameter(
                    description = ID_SHELTER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @RequestParam(required = false, name = ARG_SHELTER) Long shelterId) {

        return service.getAllByParameters(firstName, lastName, phoneNumber, shelterId);
    }

    @Operation(
            summary = GET_ALL_VOLUNTEERS,
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
            summary = EDIT_VOLUNTEER,
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
                                    examples = @ExampleObject(EDIT_VOLUNTEER_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(EDIT_VOLUNTEER_CODE_404)
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
    public Volunteer edit(
            @Parameter(
                    description = ID_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id,

            @Parameter(
                    description = FIRST_NAME_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_FIRST_NAME)))
            @RequestParam(required = false, name = ARG_FIRST_NAME) String firstName,

            @Parameter(
                    description = LAST_NAME_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_LAST_NAME)))
            @RequestParam(required = false, name = ARG_LAST_NAME) String lastName,

            @Parameter(
                    description = PHONE_NUMBER_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_PHONE_NUMBER)))
            @RequestParam(required = false, name = ARG_PHONE_NUMBER) String phoneNumber,

            @Parameter(
                    description = ID_SHELTER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @RequestParam(required = false, name = ARG_SHELTER) Long shelterId) {

        return service.edit(id, firstName, lastName, phoneNumber, shelterId);
    }

    @Operation(
            summary = DELETE_VOLUNTEER,
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
                                    examples = @ExampleObject(GET_VOLUNTEER_CODE_400)
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = ERROR,
                            content = @Content(
                                    examples = @ExampleObject(GET_VOLUNTEER_CODE_404)
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
    public Volunteer delete(
            @Parameter(
                    description = ID_VOLUNTEER,
                    content = @Content(examples = @ExampleObject(EXAMPLE_ID)))
            @PathVariable(name = ARG_ID) Long id) {

        return service.delete(id);
    }
}