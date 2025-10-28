package com.trabalho.crud.core.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.trabalho.crud.core.entity.TipoQuarto;

@Repository
@Profile("test") // Essencial: Só será usado quando o profile "test" estiver ativo
public class TipoQuartoRepositoryMemo implements TipoQuartoRepository {

    private final List<TipoQuarto> tiposQuarto = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1); // Para simular o auto-incremento

    /**
     * Limpa o repositório em memória.
     * Vamos usar isso no @BeforeEach dos nossos testes.
     */
    public void deleteAll() {
        tiposQuarto.clear();
        idCounter.set(1); // Reseta o contador de ID
    }

    @Override
    public List<TipoQuarto> findAll() {
        return new ArrayList<>(tiposQuarto); // Retorna uma cópia para evitar modificações externas
    }

    @Override
    public Optional<TipoQuarto> findById(Long id) {
        return tiposQuarto.stream()
                .filter(quarto -> quarto.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<TipoQuarto> findByNome(String nome) {
        return tiposQuarto.stream()
                .filter(quarto -> quarto.getNome().equals(nome))
                .findFirst();
    }

    @Override
    public TipoQuarto save(TipoQuarto tipoQuarto) {
        if (tipoQuarto.getId() == null) {
            // É um novo quarto (CREATE)
            tipoQuarto.setId(idCounter.getAndIncrement()); // Define um novo ID
            tiposQuarto.add(tipoQuarto);
        } else {
            // É um quarto existente (UPDATE)
            // Remove o antigo (se existir) e adiciona o novo
            tiposQuarto.removeIf(q -> q.getId().equals(tipoQuarto.getId()));
            tiposQuarto.add(tipoQuarto);
        }
        return tipoQuarto;
    }

    @Override
    public void deleteById(Long id) {
        // No nosso caso (deleção lógica), este método não será chamado pela service.
        // A service vai chamar findById() e depois save() com ativo=false.
        // Mas vamos implementar a remoção física para o caso de ser usado.
        tiposQuarto.removeIf(quarto -> quarto.getId().equals(id));
    }
}