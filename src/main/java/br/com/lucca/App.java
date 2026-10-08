package br.com.lucca;

import br.com.lucca.dao.ProdutoDAO;
import br.com.lucca.model.Produto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ProdutoDAO produtoDAO = new ProdutoDAO();

    public static void main(String[] args) {
        int opcao;

        do {
            mostrarMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            System.out.println();

            try {
                executarOpcao(opcao);
            } catch (SQLException e) {
                System.out.println("Não foi possível acessar o banco de dados.");
                System.out.println("Detalhes: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }

            System.out.println();
        } while (opcao != 0);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("=================================");
        System.out.println("         CONTROLE DE ESTOQUE");
        System.out.println("=================================");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Buscar produto");
        System.out.println("4 - Atualizar produto");
        System.out.println("5 - Registrar entrada");
        System.out.println("6 - Registrar saída");
        System.out.println("7 - Excluir produto");
        System.out.println("0 - Sair");
        System.out.println();
    }

    private static void executarOpcao(int opcao) throws SQLException {
        switch (opcao) {
            case 1 -> cadastrarProduto();
            case 2 -> listarProdutos();
            case 3 -> buscarProduto();
            case 4 -> atualizarProduto();
            case 5 -> registrarEntrada();
            case 6 -> registrarSaida();
            case 7 -> excluirProduto();
            case 0 -> System.out.println("Programa encerrado.");
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void cadastrarProduto() throws SQLException {
        System.out.println("--- Novo produto ---");

        String nome = lerTexto("Nome: ");
        String categoria = lerTexto("Categoria: ");
        BigDecimal preco = lerDecimal("Preço: R$ ");
        int quantidade = lerInteiroNaoNegativo("Quantidade inicial: ");

        Produto produto = new Produto(nome, categoria, preco, quantidade);
        produtoDAO.cadastrar(produto);

        System.out.println("Produto cadastrado com o ID " + produto.getId() + ".");
    }

    private static void listarProdutos() throws SQLException {
        List<Produto> produtos = produtoDAO.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.println("--- Produtos em estoque ---");
        produtos.forEach(System.out::println);
    }

    private static void buscarProduto() throws SQLException {
        String nome = lerTexto("Digite parte do nome do produto: ");
        List<Produto> produtos = produtoDAO.buscarPorNome(nome);

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        produtos.forEach(System.out::println);
    }

    private static void atualizarProduto() throws SQLException {
        int id = lerInteiro("ID do produto: ");
        Optional<Produto> encontrado = produtoDAO.buscarPorId(id);

        if (encontrado.isEmpty()) {
            System.out.println("Produto não encontrado.");
            return;
        }

        Produto produto = encontrado.get();
        System.out.println("Atual: " + produto);

        produto.setNome(lerTexto("Novo nome: "));
        produto.setCategoria(lerTexto("Nova categoria: "));
        produto.setPreco(lerDecimal("Novo preço: R$ "));
        produto.setQuantidade(lerInteiroNaoNegativo("Nova quantidade: "));

        if (produtoDAO.atualizar(produto)) {
            System.out.println("Produto atualizado.");
        }
    }

    private static void registrarEntrada() throws SQLException {
        int id = lerInteiro("ID do produto: ");
        Optional<Produto> encontrado = produtoDAO.buscarPorId(id);

        if (encontrado.isEmpty()) {
            System.out.println("Produto não encontrado.");
            return;
        }

        int entrada = lerInteiroPositivo("Quantidade de entrada: ");
        Produto produto = encontrado.get();
        int novaQuantidade = produto.getQuantidade() + entrada;

        produtoDAO.atualizarQuantidade(id, novaQuantidade);
        System.out.println("Entrada registrada. Estoque atual: " + novaQuantidade);
    }

    private static void registrarSaida() throws SQLException {
        int id = lerInteiro("ID do produto: ");
        Optional<Produto> encontrado = produtoDAO.buscarPorId(id);

        if (encontrado.isEmpty()) {
            System.out.println("Produto não encontrado.");
            return;
        }

        Produto produto = encontrado.get();
        int saida = lerInteiroPositivo("Quantidade de saída: ");

        if (saida > produto.getQuantidade()) {
            System.out.println("Saída não realizada: quantidade maior que o estoque disponível.");
            return;
        }

        int novaQuantidade = produto.getQuantidade() - saida;
        produtoDAO.atualizarQuantidade(id, novaQuantidade);

        System.out.println("Saída registrada. Estoque atual: " + novaQuantidade);
    }

    private static void excluirProduto() throws SQLException {
        int id = lerInteiro("ID do produto que deseja excluir: ");
        Optional<Produto> encontrado = produtoDAO.buscarPorId(id);

        if (encontrado.isEmpty()) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println(encontrado.get());
        String confirmacao = lerTexto("Digite S para confirmar a exclusão: ");

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Exclusão cancelada.");
            return;
        }

        if (produtoDAO.excluir(id)) {
            System.out.println("Produto excluído.");
        }
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println("O campo não pode ficar vazio.");
        }
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();

            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static int lerInteiroNaoNegativo(String mensagem) {
        while (true) {
            int valor = lerInteiro(mensagem);

            if (valor >= 0) {
                return valor;
            }

            System.out.println("A quantidade não pode ser negativa.");
        }
    }

    private static int lerInteiroPositivo(String mensagem) {
        while (true) {
            int valor = lerInteiro(mensagem);

            if (valor > 0) {
                return valor;
            }

            System.out.println("Digite uma quantidade maior que zero.");
        }
    }

    private static BigDecimal lerDecimal(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim().replace(",", ".");

            try {
                BigDecimal numero = new BigDecimal(valor);

                if (numero.compareTo(BigDecimal.ZERO) >= 0) {
                    return numero;
                }

                System.out.println("O valor não pode ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor válido. Exemplo: 129,90");
            }
        }
    }
}
