# 🏠 Expense Control

Sistema de controle de gastos residenciais, desenvolvido para cadastrar pessoas, transações e calcular totais de despesas.

## 🔧 Funcionalidades
- **Cadastro de pessoas**: Adicionar novas pessoas ao sistema.
- **Listagem de pessoas**: Visualizar todas as pessoas cadastradas.
- **Exclusão de pessoas**: Remover pessoas cadastradas.
- **Cadastro de transações**: Registrar despesas de pessoas.
- **Listagem de transações**: Visualizar todas as transações registradas.
- **Cálculo de totais**: Visualizar transações com totais de despesas por pessoa.

## 🛠️ Tecnologias Utilizadas
- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **H2 Database**
- **SLF4J** (Logging)
- **Spring Validation**
- **JUnit 5** (Testes unitários)
- **Mockito** (Mocking)

## 🌐 Endpoints

### Pessoas
- ➕ **`POST /api/people`**: Cadastra uma nova pessoa.
- 📋 **`GET /api/people`**: Lista todas as pessoas cadastradas.
- 🗑️ **`DELETE /api/people/{id}`**: Deleta uma pessoa cadastrada pelo `id`.

### Transações
- ➕ **`POST /api/transactions`**: Cria uma nova transação.
- 📋 **`GET /api/transactions/list`**: Lista todas as transações cadastradas.
- 📊 **`GET /api/transactions/totals`**: Lista transações com totais de despesas por pessoa.

## 🧪 Testes

Testes implementados para as classes de serviço utilizando JUnit 5 e Mockito. Para executar os testes, use o seguinte comando dentro do diretório do projeto:

```bash
mvn test
```
## 🐳 Executando o Projeto com Docker
_É necessário ter o Docker instalado._<br>
1. Clone o repositório localmente:
```bash
git clone <repositorio_url>
cd <repositorio>
```
2. Construa a imagem Docker e execute o container:
```bash
docker build -t app-spring .
docker images
docker run -p 8080:8080 <id_da_imagem_gerada>
```
Agora a aplicação está pronta para ser testada.
