package com.trabalho.crud.core.repository;

import java.util.List;
import java.util.Optional;
import com.trabalho.crud.core.entity.TipoQuarto;

public interface TipoQuartoRepository {

    List<TipoQuarto> findAll();

    Optional<TipoQuarto> findById(Long id);

    Optional<TipoQuarto> findByNome(String nome);

    TipoQuarto save(TipoQuarto tipoQuarto);

    void deleteById(Long id);

}