package com.trabalho.crud.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.trabalho.crud.core.dto.TipoQuartoDto;
import com.trabalho.crud.core.entity.BusinessException;
import com.trabalho.crud.core.entity.TipoQuarto;
import com.trabalho.crud.core.mapper.TipoQuartoMapper;
import com.trabalho.crud.core.repository.TipoQuartoRepository;

@Service
public class TipoQuartoService {

    private final TipoQuartoRepository repository;
    private final TipoQuartoMapper mapper;

    public TipoQuartoService(TipoQuartoRepository repository, TipoQuartoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Retorna uma lista de todos os tipos de quarto ATIVOS.
     * Tipos de quarto inativados não são retornados aqui.
     */
    public List<TipoQuartoDto> findAll() {
        return repository.findAll().stream()
                .filter(TipoQuarto::isAtivo)
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Busca um tipo de quarto pelo ID.
     */
    public TipoQuartoDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> BusinessException.notFoundException("Tipo de quarto não encontrado"));
    }

    /**
     * Salva um novo tipo de quarto, aplicando as regras de negócio.
     */
    public TipoQuartoDto save(TipoQuartoDto tipoQuartoDto) {
        this.validateSave(tipoQuartoDto);

        var entity = mapper.toEntity(tipoQuartoDto);

        entity.setAtivo(true);

        var savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    /**
     * Atualiza um tipo de quarto existente.
     */
    public TipoQuartoDto update(Long id, TipoQuartoDto tipoQuartoDto) {
        TipoQuarto existingEntity = this.findEntityById(id);
        this.validateUpdate(id, tipoQuartoDto);

        var entityToUpdate = mapper.toEntity(tipoQuartoDto);

        entityToUpdate.setId(id);
        entityToUpdate.setAtivo(existingEntity.isAtivo());
        entityToUpdate.setDataCriacao(existingEntity.getDataCriacao());

        var updatedEntity = repository.save(entityToUpdate);
        return mapper.toDto(updatedEntity);
    }

    /**
     * Inativa (Deleção Lógica) um tipo de quarto.
     */
    public void deleteById(Long id) {
        TipoQuarto entity = this.findEntityById(id);

        // TODO: Implementar verificação de reservas ativas

        entity.setAtivo(false);
        repository.save(entity);
    }

    // --- MÉTODOS PRIVADOS DE VALIDAÇÃO ---

    private void validateCommonRules(TipoQuartoDto dto) {
        if (dto.getNome() == null || dto.getNome().isBlank() ||
                dto.getDescricao() == null || dto.getDescricao().isBlank() ||
                dto.getCapacidadeMaxima() == null || dto.getTarifaPadrao() == null) {

            throw new BusinessException("Todos os campos (nome, descrição, capacidade, tarifa) são obrigatórios.", HttpStatus.BAD_REQUEST);
        }

        if (dto.getCapacidadeMaxima() <= 0) {
            throw new BusinessException("Capacidade máxima deve ser maior que zero.", HttpStatus.BAD_REQUEST);
        }
        if (dto.getTarifaPadrao() <= 0) {
            throw new BusinessException("Tarifa padrão deve ser maior que zero.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateSave(TipoQuartoDto dto) {
        this.validateCommonRules(dto);

        repository.findByNome(dto.getNome()).ifPresent(existing -> {
            throw new BusinessException("Já existe um tipo de quarto com este nome.", HttpStatus.CONFLICT);
        });
    }

    private void validateUpdate(Long id, TipoQuartoDto dto) {
        this.validateCommonRules(dto);

        Optional<TipoQuarto> existing = repository.findByNome(dto.getNome());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new BusinessException("Já existe um tipo de quarto com este nome.", HttpStatus.CONFLICT);
        }
    }

    private TipoQuarto findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> BusinessException.notFoundException("Tipo de quarto não encontrado"));
    }
}