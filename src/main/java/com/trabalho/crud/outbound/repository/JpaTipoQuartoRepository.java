package com.trabalho.crud.outbound.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trabalho.crud.core.entity.TipoQuarto;
import com.trabalho.crud.core.repository.TipoQuartoRepository;

import java.util.Optional;

@Repository
@Profile("!test")
public interface JpaTipoQuartoRepository extends TipoQuartoRepository, JpaRepository<TipoQuarto, Long> {
    @Override
    Optional<TipoQuarto> findByNome(String nome);
}