package com.trabalho.crud.inbound.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trabalho.crud.core.dto.TipoQuartoDto;
import com.trabalho.crud.core.service.TipoQuartoService;

// Imports do Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tipos-quarto") // Endpoint atualizado
@Tag(name = "Tipos de Quarto", description = "API para gerenciamento de tipos de quarto")
public class TipoQuartoController {

    private final TipoQuartoService tipoQuartoService;

    public TipoQuartoController(TipoQuartoService tipoQuartoService) {
        this.tipoQuartoService = tipoQuartoService;
    }

    @Operation(summary = "Listar todos os tipos de quarto", description = "Retorna uma lista de todos os tipos de quarto ATIVOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de quarto retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<TipoQuartoDto>> getAllTiposQuarto() {
        return ResponseEntity.ok(tipoQuartoService.findAll());
    }

    @Operation(summary = "Buscar tipo de quarto por ID", description = "Retorna um tipo de quarto específico pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de quarto encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo de quarto não encontrado (via RequestHandler)")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoQuartoDto> getTipoQuartoById(@PathVariable Long id) {
        return ResponseEntity.ok(tipoQuartoService.findById(id));
    }

    @Operation(summary = "Criar um novo tipo de quarto", description = "Cria um novo tipo de quarto. O nome não pode ser duplicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de quarto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (campos obrigatórios ou valores < 0)"),
            @ApiResponse(responseCode = "409", description = "Já existe um tipo de quarto com este nome (Conflict)")
    })
    @PostMapping
    public ResponseEntity<TipoQuartoDto> createTipoQuarto(@RequestBody TipoQuartoDto tipoQuartoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoQuartoService.save(tipoQuartoDto));
    }

    @Operation(summary = "Atualizar um tipo de quarto", description = "Atualiza os dados de um tipo de quarto existente pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de quarto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Tipo de quarto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Nome duplicado pertence a outro quarto (Conflict)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoQuartoDto> updateTipoQuarto(@PathVariable Long id, @RequestBody TipoQuartoDto tipoQuartoDto) {
        return ResponseEntity.ok(tipoQuartoService.update(id, tipoQuartoDto));
    }

    @Operation(summary = "Inativar um tipo de quarto (Deleção Lógica)", description = "Realiza a deleção lógica (inativação) de um tipo de quarto. O registro não é apagado do banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de quarto inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo de quarto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoQuarto(@PathVariable Long id) {
        tipoQuartoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}