package com.johann.demoparkapi.web.controller;

import com.johann.demoparkapi.entity.Vaga;
import com.johann.demoparkapi.service.VagaService;
import com.johann.demoparkapi.web.dto.VagaCreateDto;
import com.johann.demoparkapi.web.dto.VagaResponseDto;
import com.johann.demoparkapi.web.dto.mapper.VagaMapper;
import com.johann.demoparkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@Tag(name = "Vagas", description = "Contém todas as operações relativas ao recurso de uma vaga.")
@RequestMapping("/api/v1/vagas")
@RequiredArgsConstructor
@RestController
public class VagaController {

    private final VagaService vagaService;

    @Operation(
            summary = "Criar uma nova vaga", description = "Recurso para cirar uma nova vaga." + "Requisição exige o uso de um bearer token. Acesso Restrito para ADMIN.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Recurso criado com sucesso.",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado")),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Vaga já cadastrada.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Recurso não processado por falta de dados ou dados inválidos.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDto vagaCreateDto) {
        Vaga vaga = VagaMapper.toVaga(vagaCreateDto);
        vagaService.salvar(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();
        return ResponseEntity.created(location).build();
    }


    @Operation(
            summary = "Localizar uma vaga", description = "Recurso para localizar uma vaga." + "Requisição exige o uso de um bearer token. Acesso Restrito para ADMIN.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recurso localizado com sucesso.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = VagaResponseDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Vaga não encontrada.",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{codigo}")
    public ResponseEntity<VagaResponseDto> getByCodigo(@PathVariable String codigo) {
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }
}
