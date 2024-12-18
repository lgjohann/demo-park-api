package com.johann.demoparkapi.web.controller;

import com.johann.demoparkapi.entity.Cliente;
import com.johann.demoparkapi.jwt.JwtUserDetails;
import com.johann.demoparkapi.repository.projection.ClienteProjection;
import com.johann.demoparkapi.service.ClienteService;
import com.johann.demoparkapi.service.UsuarioService;
import com.johann.demoparkapi.web.dto.ClienteCreateDto;
import com.johann.demoparkapi.web.dto.ClienteResponseDto;
import com.johann.demoparkapi.web.dto.PageableDto;
import com.johann.demoparkapi.web.dto.UsuarioResponseDto;
import com.johann.demoparkapi.web.dto.mapper.ClienteMapper;
import com.johann.demoparkapi.web.dto.mapper.PageableMapper;
import com.johann.demoparkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clientes", description = "Contém todas as operações relativas ao recurso de um cliente.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(
            summary = "Criar um novo cliente.",
            security = @SecurityRequirement(name = "security"),
            description = "Recurso para criar um novo cliente vinculado a um usuário já cadastrado." + " Requisição exige o uso de um bearer token para autenticação. Recurso restrito a usuários do tipo CLIENTE",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Cliente CPF já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido aos usuários de perfil ADMIN",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));

    }


    @Operation(
            summary = "Localizar um cliente pelo ID.",
            security = @SecurityRequirement(name = "security"),
            description = "Recurso para buscar um cliente através do id do cliente." + " Requisição exige o uso de um bearer token para autenticação. Recurso restrito a usuários do tipo ADMIN",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido aos usuários de perfil CLIENTE",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteResponseDto> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @Operation(
            summary = "Buscar todos os clientes.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a página retornada"
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                            description = "Representa o total de elementos por página"
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "sort",
                            hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,asc")),
                            description = "Representa a ordenação dos resultados. Mmultiplos critérios de ordenação são suportados."
                    )
            },
            description = "Recurso para buscar todos os clientes." + " Requisição exige o uso de um bearer token para autenticação. Recurso restrito a usuários do tipo ADMIN",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Recurso não permitido aos usuários de perfil CLIENTE",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {
        Page<ClienteProjection> clientes = clienteService.buscarTodos(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(clientes));
    }

    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> getDetalhes(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = clienteService.buscarPorUsuarioId(userDetails.getId());
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }
}
