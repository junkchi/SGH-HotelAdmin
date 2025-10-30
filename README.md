# Projeto API de Gestão Hoteleira - Backend (HotelAdmin)

## 📖 Descrição

Este projeto é o backend (API) para um sistema de gerenciamento de hotel. O objetivo principal é fornecer serviços RESTful para gerenciar as entidades do sistema, começando com um CRUD (Create, Read, Update, Delete) completo para **Tipos de Quarto**.

A aplicação segue uma arquitetura limpa (separada em `core`, `inbound` e `outbound`) e está documentada com Swagger.

## 🚀 Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.3.4**
* **Spring Data JPA:** Para persistência de dados.
* **H2 Database:** Banco de dados em memória, configurado para rodar com a aplicação.
* **MapStruct:** Para DTO/Entity mapping.
* **SpringDoc (Swagger):** Para documentação e teste de API.
* **Maven:** Gerenciador de dependências e build.
* **JUnit 5:** Para testes unitários e de integração.
* **JaCoCo:** Para análise de cobertura de testes.

## 🛠️ Pré-requisitos

* **JDK 17** (ou superior)
* **Maven 3.x**

## ⚡ Como Rodar a Aplicação (Backend + Frontend)

Para rodar a aplicação completa e testar a interface, siga os dois passos:

### 1. Rodar o Backend (A API Java)

1.  Clone o repositório:
    ```bash
    git clone https://github.com/junkchi/SGH-HotelAdmin
    ```

2.  Navegue até o diretório do projeto (a pasta que contém o `pom.xml`):
    ```bash
    cd SGH-HotelAdmin
    ```

3.  Compile e execute o projeto:
    ```bash
    mvn spring-boot:run
    ```

A API estará disponível em `http://localhost:8080`. **Deixe este terminal rodando.**

### 2. Abrir o Frontend (O `index.html`)

Um frontend simples (`index.html`) foi criado para consumir esta API.

1.  Com o backend rodando (Passo 1), abra o arquivo `index.html` (que você salvou na sua máquina) diretamente no seu navegador (Ex: clique duas vezes no arquivo).
2.  A interface irá carregar, se conectar automaticamente à API em `http://localhost:8080` e estará pronta para uso.

---

## 🔬 Ferramentas de Demonstração

Durante a execução do backend, duas ferramentas estão disponíveis para a apresentação:

### 1. Documentação da API (Swagger)

A documentação interativa da API (Swagger) é gerada automaticamente. Você pode usá-la para testar todos os endpoints.

* **URL:** `http://localhost:8080/swagger-ui/index.html`

### 2. Console do Banco de Dados (H2)

Você pode acessar o banco de dados em memória (H2) diretamente para provar que os dados estão sendo salvos, atualizados e inativados.

1.  **URL:** `http://localhost:8080/h2-console`
2.  **JDBC URL:** `jdbc:h2:mem:testdb` (use exatamente este valor)
3.  **User Name:** `sa`
4.  **Password:** (deixe em branco)
5.  Clique em **Conectar**.

Para ver os dados da tabela, rode o comando SQL: `SELECT * FROM TIPO_QUARTO;`

---

## 🧪 Testes e Cobertura (Coverage)

O projeto está configurado com testes de integração (`@SpringBootTest`) que validam todas as regras de negócio da `TipoQuartoService`, atingindo **100% de cobertura de código**.

1.  **Para rodar todos os testes:**
    ```bash
    mvn test
    ```

2.  **Para ver o Relatório de Cobertura (JaCoCo):**
    Após rodar `mvn test`, abra este arquivo no seu navegador:
    `target/site/jacoco/index.html`

---

## 🔌 Endpoints da API

Abaixo está a descrição dos endpoints da API de **Tipos de Quarto**:

### 1. `POST /tipos-quarto`

* **Descrição:** Cria um novo tipo de quarto. O `nome` não pode ser duplicado e todos os campos são obrigatórios.
* **Corpo da Requisição (JSON):**
    ```json
    {
      "nome": "Quarto Família",
      "descricao": "Quarto com duas camas de casal.",
      "capacidadeMaxima": 4,
      "tarifaPadrao": 300.0
    }
    ```
* **Resposta (201 Created):**
    ```json
    {
      "id": 1,
      "nome": "Quarto Família",
      "descricao": "Quarto com duas camas de casal.",
      "capacidadeMaxima": 4,
      "tarifaPadrao": 300.0
    }
    ```
* **Respostas de Erro:**
    * `400 Bad Request`: (Ex: tarifa < 0, ou campos nulos).
    * `409 Conflict`: (Ex: já existe um quarto com esse `nome`).

### 2. `GET /tipos-quarto`

* **Descrição:** Retorna uma lista de todos os tipos de quarto que estão **ativos**. Quartos inativados (deletados logicamente) não aparecem aqui.
* **Resposta (200 OK):**
    ```json
    [
      {
        "id": 1,
        "nome": "Quarto Família",
        "descricao": "Quarto com duas camas de casal.",
        "capacidadeMaxima": 4,
        "tarifaPadrao": 300.0
      }
    ]
    ```

### 3. `GET /tipos-quarto/{id}`

* **Descrição:** Retorna um tipo de quarto específico pelo seu ID.
* **Parâmetros de Caminho:**
    * `id` (Long): ID do tipo de quarto.
* **Resposta (200 OK):**
    ```json
    {
      "id": 1,
      "nome": "Quarto Família",
      "descricao": "Quarto com duas camas de casal.",
      "capacidadeMaxima": 4,
      "tarifaPadrao": 300.0
    }
    ```
* **Respostas de Erro:**
    * `404 Not Found`: Se o ID do quarto não for encontrado.

### 4. `PUT /tipos-quarto/{id}`

* **Descrição:** Atualiza um tipo de quarto existente.
* **Parâmetros de Caminho:**
    * `id` (Long): ID do tipo de quarto a ser atualizado.
* **Corpo da Requisição (JSON):**
    ```json
    {
      "nome": "Quarto Família (Reformado)",
      "descricao": "Agora com TV de 50 polegadas.",
      "capacidadeMaxima": 4,
      "tarifaPadrao": 320.0
    }
    ```
* **Resposta (200 OK):**
    ```json
    {
      "id": 1,
      "nome": "Quarto Família (Reformado)",
      "descricao": "Agora com TV de 50 polegadas.",
      "capacidadeMaxima": 4,
      "tarifaPadrao": 320.0
    }
    ```
* **Respostas de Erro:**
    * `404 Not Found`: Se o ID do quarto não for encontrado.

### 5. `DELETE /tipos-quarto/{id}`

* **Descrição:** **Inativa** (deleção lógica) um tipo de quarto pelo ID. O registro não é apagado do banco, apenas marcado como `ativo = false`.
* **Parâmetros de Caminho:**
    * `id` (Long): ID do tipo de quarto.
* **Resposta:**
    * `204 No Content`: (Sucesso, sem corpo de resposta).
* **Respostas de Erro:**
    * `404 Not Found`: Se o ID do quarto não for encontrado.
