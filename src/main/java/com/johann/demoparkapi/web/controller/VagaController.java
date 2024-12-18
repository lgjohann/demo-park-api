package com.johann.demoparkapi.web.controller;

import com.johann.demoparkapi.entity.Vaga;
import com.johann.demoparkapi.service.VagaService;
import com.johann.demoparkapi.web.dto.VagaCreateDto;
import com.johann.demoparkapi.web.dto.VagaResponseDto;
import com.johann.demoparkapi.web.dto.mapper.VagaMapper;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/api/v1/vagas")
@RequiredArgsConstructor
@RestController
public class VagaController {

    private final VagaService vagaService;

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{codigo}")
    public ResponseEntity<VagaResponseDto> getByCodigo(@PathVariable String codigo) {
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }
}
