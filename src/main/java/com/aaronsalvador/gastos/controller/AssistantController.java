package com.aaronsalvador.gastos.controller;

import com.aaronsalvador.gastos.dto.AssistantRequest;
import com.aaronsalvador.gastos.dto.AssistantResponse;
import com.aaronsalvador.gastos.model.User;
import com.aaronsalvador.gastos.service.AssistantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
@Tag(name = "Asistente")
public class AssistantController {

    private final AssistantService service;

    public AssistantController(AssistantService service) {
        this.service = service;
    }

    @Operation(summary = "Pregunta al asistente de IA (usa un resumen de tus gastos como contexto)")
    @PostMapping("/chat")
    public AssistantResponse chat(@AuthenticationPrincipal User user,
                                  @Valid @RequestBody AssistantRequest request) {
        return new AssistantResponse(service.reply(user.getEmail(), request));
    }
}
