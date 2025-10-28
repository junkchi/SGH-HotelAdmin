package com.trabalho.crud.core.service;

import static org.junit.jupiter.api.Assertions.*;

import com.trabalho.crud.core.dto.TipoQuartoDto;
import com.trabalho.crud.core.entity.BusinessException;
import com.trabalho.crud.core.repository.TipoQuartoRepository;
import com.trabalho.crud.core.repository.TipoQuartoRepositoryMemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test") // Ativa o profile "test", que vai carregar o TipoQuartoRepositoryMemo
@SpringBootTest         // Sobe o contexto do Spring (Service, Mapper, e o RepoMemo)
class TipoQuartoServiceTest {

    @Autowired
    private TipoQuartoService service; // A service real

    @Autowired
    private TipoQuartoRepository repository; // O repo injetado (será o TipoQuartoRepositoryMemo)

    @BeforeEach
    void setUp() {
        // Antes de CADA teste, limpamos o banco em memória
        // Precisamos dar um "cast" para acessar o método específico do nosso MemoRepo
        if (repository instanceof TipoQuartoRepositoryMemo) {
            ((TipoQuartoRepositoryMemo) repository).deleteAll();
        }
    }

    // Método helper para criar um DTO válido rapidamente
    private TipoQuartoDto criarDtoValido(String nome) {
        var dto = new TipoQuartoDto();
        dto.setNome(nome);
        dto.setDescricao("Descricao " + nome);
        dto.setCapacidadeMaxima(2);
        dto.setTarifaPadrao(100.0);
        return dto;
    }

    @Test
    @DisplayName("Deve salvar um novo tipo de quarto com sucesso")
    void testSave_Success() {
        // 1. Arrange (Arrumar)
        TipoQuartoDto dto = criarDtoValido("Suíte Luxo");

        // 2. Act (Agir)
        TipoQuartoDto resultado = service.save(dto);

        // 3. Assert (Verificar)
        assertNotNull(resultado);
        assertNotNull(resultado.getId()); // Deve ter ganhado um ID
        assertEquals("Suíte Luxo", resultado.getNome());

        // Verifica se foi salvo no repo-memo
        var lista = service.findAll();
        assertEquals(1, lista.size());
        assertEquals("Suíte Luxo", lista.get(0).getNome());
    }

    @Test
    @DisplayName("Deve buscar um TipoQuarto por ID com sucesso")
    void testFindById_Success() {
        // 1. Arrange
        service.save(criarDtoValido("Quarto Standard")); // Salva um item (ganha ID 1)

        // 2. Act
        TipoQuartoDto resultado = service.findById(1L);

        // 3. Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Quarto Standard", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar BusinessException (Not Found) ao buscar ID inexistente")
    void testFindById_NotFound() {
        // 1. Arrange (Não salvamos nada)

        // 2. Act & 3. Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.findById(99L);
        });

        assertEquals("Tipo de quarto não encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Deve listar apenas quartos ativos no findAll")
    void testFindAll_ApenasAtivos() {
        // 1. Arrange
        service.save(criarDtoValido("Ativo 1")); // ID 1
        var dtoInativo = service.save(criarDtoValido("Inativo 1")); // ID 2

        // Inativa o quarto 2 (vamos testar o delete lógico aqui)
        service.deleteById(dtoInativo.getId());

        // 2. Act
        var lista = service.findAll();

        // 3. Assert
        assertEquals(1, lista.size()); // Deve retornar apenas 1
        assertEquals("Ativo 1", lista.get(0).getNome());
    }

    //
    // AGORA VAMOS TESTAR AS REGRAS DE NEGÓCIO (TAREFA 5)
    //

    @Test
    @DisplayName("Deve falhar ao salvar com nome duplicado (Regra 2)")
    void testSave_FalhaNomeDuplicado() {
        // 1. Arrange
        service.save(criarDtoValido("Nome Duplicado")); // Salva o primeiro

        // 2. Act & 3. Assert
        TipoQuartoDto dtoDuplicado = criarDtoValido("Nome Duplicado"); // Tenta salvar o segundo

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.save(dtoDuplicado);
        });

        assertEquals("Já existe um tipo de quarto com este nome.", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

        // Garante que só o primeiro foi salvo
        assertEquals(1, service.findAll().size());
    }

    @Test
    @DisplayName("Deve falhar ao salvar com tarifa negativa (Regra 4)")
    void testSave_FalhaTarifaNegativa() {
        // 1. Arrange
        TipoQuartoDto dto = criarDtoValido("Tarifa Inválida");
        dto.setTarifaPadrao(-50.0); // Valor inválido

        // 2. Act & 3. Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.save(dto);
        });

        assertEquals("Tarifa padrão deve ser maior que zero.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("Deve falhar ao salvar com capacidade zero (Regra 3)")
    void testSave_FalhaCapacidadeZero() {
        // 1. Arrange
        TipoQuartoDto dto = criarDtoValido("Capacidade Inválida");
        dto.setCapacidadeMaxima(0); // Valor inválido

        // 2. Act & 3. Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.save(dto);
        });

        assertEquals("Capacidade máxima deve ser maior que zero.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("Deve falhar ao salvar com nome nulo (Regra 1)")
    void testSave_FalhaNomeNulo() {
        // 1. Arrange
        TipoQuartoDto dto = criarDtoValido("Nome Nulo");
        dto.setNome(null); // Valor inválido

        // 2. Act & 3. Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.save(dto);
        });

        assertEquals("Todos os campos (nome, descrição, capacidade, tarifa) são obrigatórios.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }


    @Test
    @DisplayName("Deve atualizar um tipo de quarto com sucesso")
    void testUpdate_Success() {
        // 1. Arrange
        // Salva um quarto
        TipoQuartoDto dtoOriginal = service.save(criarDtoValido("Quarto Antigo"));
        Long id = dtoOriginal.getId();

        // Cria o DTO com as atualizações
        TipoQuartoDto dtoAtualizado = new TipoQuartoDto();
        dtoAtualizado.setId(id);
        dtoAtualizado.setNome("Quarto Novo Nome");
        dtoAtualizado.setDescricao("Nova Descricao");
        dtoAtualizado.setCapacidadeMaxima(4); // Mudou de 2 para 4
        dtoAtualizado.setTarifaPadrao(999.0); // Mudou de 100 para 999

        // 2. Act
        TipoQuartoDto resultado = service.update(id, dtoAtualizado);

        // 3. Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Quarto Novo Nome", resultado.getNome()); // Verifica o nome
        assertEquals(4, resultado.getCapacidadeMaxima());     // Verifica a capacidade
        assertEquals(999.0, resultado.getTarifaPadrao());    // Verifica a tarifa

        // Verifica no "banco"
        var quartoDoRepo = service.findById(id);
        assertEquals("Quarto Novo Nome", quartoDoRepo.getNome());
    }

    @Test
    @DisplayName("Deve falhar ao atualizar um ID que não existe")
    void testUpdate_NotFound() {
        // 1. Arrange
        TipoQuartoDto dto = criarDtoValido("Nome");

        // 2. Act & 3. Assert
        // Tenta atualizar um ID (99L) que não existe
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.update(99L, dto);
        });

        assertEquals("Tipo de quarto não encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Deve falhar ao atualizar para um nome duplicado (Regra 2 Update)")
    void testUpdate_FalhaNomeDuplicado() {
        // 1. Arrange
        service.save(criarDtoValido("Nome Existente")); // Salva ID 1
        var dtoParaAtualizar = service.save(criarDtoValido("Nome Original")); // Salva ID 2

        Long idParaAtualizar = dtoParaAtualizar.getId(); // Pega o ID 2

        // Tenta renomear o quarto 2 para "Nome Existente" (que é o nome do quarto 1)
        dtoParaAtualizar.setNome("Nome Existente");

        // 2. Act & 3. Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.update(idParaAtualizar, dtoParaAtualizar);
        });

        assertEquals("Já existe um tipo de quarto com este nome.", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

        // Garante que o nome do quarto 2 não mudou
        var quartoNaoMudou = service.findById(idParaAtualizar);
        assertEquals("Nome Original", quartoNaoMudou.getNome());
    }
}