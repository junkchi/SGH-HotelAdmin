package com.trabalho.crud.outbound.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trabalho.crud.core.entity.TipoQuarto;
import com.trabalho.crud.core.repository.TipoQuartoRepository;

import java.util.Optional;

/**
 * Esta é a implementação real (Adaptador) da porta do repositório.
 * Ela usa Spring Data JPA para interagir com o banco de dados.
 *
 * Está ativa em todos os profiles, exceto "test".
 */
@Repository
@Profile("!test")
public interface JpaTipoQuartoRepository extends TipoQuartoRepository, JpaRepository<TipoQuarto, Long> {

    /**
     * O Spring Data JPA implementará este método automaticamente
     * com base no nome. Ele gerará a query:
     * "SELECT * FROM TIPO_QUARTO WHERE NOME = ?1"
     *
     * Este método já está definido na interface 'TipoQuartoRepository'
     * (nossa porta), mas o Spring Data JPA precisa que ele seja
     * declarado aqui (na interface que extende JpaRepository) para
     * que ele possa criar a implementação.
     */
    @Override
    Optional<TipoQuarto> findByNome(String nome);

    /**
     * NOTA: Os métodos findAll(), findById(), save() e deleteById()
     * são implementados "de graça" pelo JpaRepository.
     * Nós não precisamos declará-los aqui.
     */
}