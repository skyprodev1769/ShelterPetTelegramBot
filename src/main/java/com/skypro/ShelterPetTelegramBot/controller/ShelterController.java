package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;

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
                            responseCode = "200",
                            description = "Новый приют добавлен",
                            content = @Content(
                                    schema = @Schema(implementation = Shelter.class),
                                    examples = @ExampleObject("""
                                            {
                                               "id": 1,
                                               "type": "DOG",
                                               "address": "ул. Планерная, д.1, стр.1"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка добавления. Возможно, ошибка в адресе или данный приют уже был добавлен ранее.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(INVALIDE_INPUT + "\n\n" + SHELTER_ALREADY_ADDED)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка добавления. Ошибка на стороне сервера.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )

    @PostMapping
    public Shelter add(@Parameter(description = "Тип животного", name = "Тип")
                       @RequestParam PetType type,

                       @Parameter(description = "Адрес приюта", name = "Адрес")
                       @RequestParam String address) {

        return service.add(type, address);
    }

    @Operation(
            summary = "Получение приюта для животных по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приют получен",
                            content = @Content(
                                    schema = @Schema(implementation = Shelter.class),
                                    examples = @ExampleObject("""
                                            {
                                               "id": 1,
                                               "type": "DOG",
                                               "address": "ул. Планерная, д.1, стр.1"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка получения. Возможно, передан некорректный id.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(INVALIDE_INPUT)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ошибка получения. Возможно, приют с таким id еще не был добавлен.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(SHELTER_NOT_FOUND)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка получения. Ошибка на стороне сервера.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )

    @GetMapping("{id}")
    public Shelter getById(@Parameter(description = "id приюта для животных", name = "id")
                           @PathVariable Long id) {

        return service.getById(id);
    }

    @Operation(
            summary = "Получение приютов для животных по адресу или типу животных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приюты получены",
                            content = @Content(
                                    schema = @Schema(implementation = Shelter.class),
                                    examples = @ExampleObject("""
                                            [
                                               {
                                                  "id": 1,
                                                  "type": "DOG",
                                                  "address": "ул. Планерная, д.1, стр.1"
                                               }
                                            ]
                                                                                        
                                            []""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка получения. Возможно, передан некорректный адрес.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(INVALIDE_INPUT)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка получения. Ошибка на стороне сервера.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )

    @GetMapping
    public Collection<Shelter> getAllByParameters(@Parameter(description = "Адрес приюта", name = "Адрес")
                                                  @RequestParam(required = false) String address,

                                                  @Parameter(description = "Тип животного", name = "Тип")
                                                  @RequestParam(required = false) PetType type) {

        return service.getAllByParameters(address, type);
    }

    @Operation(
            summary = "Получение всех приютов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приюты получены",
                            content = @Content(
                                    schema = @Schema(implementation = Shelter.class),
                                    examples = @ExampleObject("""
                                            [
                                               {
                                                  "id": 1,
                                                  "type": "DOG",
                                                  "address": "ул. Планерная, д.1, стр.1"
                                               }
                                            ]
                                                                                        
                                            []""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка получения. Ошибка на стороне сервера.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )

    @GetMapping("/all")
    public Collection<Shelter> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Изменение данных приюта для животных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные приюта изменены",
                            content = @Content(
                                    schema = @Schema(implementation = Shelter.class),
                                    examples = @ExampleObject("""
                                            {
                                               "id": 1,
                                               "type": "CAT",
                                               "address": "ул. Ленина, д.13, стр.14"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка изменения. Возможно, передан некорректный id, ошибка в адресе или данный приют уже был добавлен ранее.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(INVALIDE_INPUT + "\n\n" + SHELTER_ALREADY_ADDED)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ошибка изменения. Возможно, приют с таким id еще не был добавлен.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(SHELTER_NOT_FOUND)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка добавления. Ошибка на стороне сервера.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )

    @PutMapping("{id}")
    public Shelter edit(@Parameter(description = "id приюта для животных", name = "id")
                        @PathVariable Long id,

                        @Parameter(description = "Тип животного", name = "Тип")
                        @RequestParam(required = false) PetType type,

                        @Parameter(description = "Адрес приюта", name = "Адрес")
                        @RequestParam(required = false) String address) {

        return service.edit(id, type, address);
    }

    @Operation(
            summary = "Удаление приюта для животных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Приют удален",
                            content = @Content(
                                    schema = @Schema(implementation = Shelter.class),
                                    examples = @ExampleObject("""
                                            {
                                               "id": 1,
                                               "type": "DOG",
                                               "address": "ул. Планерная, д.1, стр.1"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка удаления. Возможно, передан некорректный id.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(INVALIDE_INPUT)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ошибка удаления. Возможно, приют с таким id еще не был добавлен.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(SHELTER_NOT_FOUND)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка удаления. Ошибка на стороне сервера.",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )

    @DeleteMapping("{id}")
    public Shelter delete(@Parameter(description = "id приюта для животных", name = "id")
                          @PathVariable Long id) {

        return service.delete(id);
    }
}