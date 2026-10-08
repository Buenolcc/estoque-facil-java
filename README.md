# Estoque Fácil

Projeto em Java criado para praticar orientação a objetos, JDBC, SQL e operações CRUD.

A ideia é simular um controle de estoque simples para uma pequena loja, usando o terminal como interface.

## Funcionalidades

- Cadastrar produto
- Listar produtos
- Buscar produto por nome
- Atualizar produto
- Registrar entrada de estoque
- Registrar saída de estoque
- Excluir produto
- Impedir saída maior que a quantidade disponível

## Tecnologias

- Java 21
- Maven
- MySQL
- JDBC

## Estrutura

```text
src/main/java/br/com/lucca
├── App.java
├── connection
│   └── Conexao.java
├── dao
│   └── ProdutoDAO.java
└── model
    └── Produto.java
```

## Como executar

1. Execute o arquivo `database/estoque.sql` no MySQL Workbench.
2. Em `src/main/resources`, copie `db.properties.example` e renomeie a cópia para `db.properties`.
3. Preencha seu usuário e sua senha do MySQL no arquivo `db.properties`.
4. No terminal, dentro da pasta do projeto, execute:

```bash
mvn clean compile
mvn exec:java
```

O arquivo `db.properties` está no `.gitignore`, então sua senha não será enviada para o GitHub.

## Próximas melhorias

- Registrar histórico de entradas e saídas
- Criar testes automatizados
- Criar uma interface gráfica ou API
- Adicionar um front-end em React

## Autor

Lucca Machado Bueno
