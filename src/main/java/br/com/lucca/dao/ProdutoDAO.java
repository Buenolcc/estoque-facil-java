package br.com.lucca.dao;

import br.com.lucca.connection.Conexao;
import br.com.lucca.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDAO {

    public void cadastrar(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, categoria, preco, quantidade) VALUES (?, ?, ?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, produto.getNome());
            comando.setString(2, produto.getCategoria());
            comando.setBigDecimal(3, produto.getPreco());
            comando.setInt(4, produto.getQuantidade());
            comando.executeUpdate();

            try (ResultSet ids = comando.getGeneratedKeys()) {
                if (ids.next()) {
                    produto.setId(ids.getInt(1));
                }
            }
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, categoria, preco, quantidade FROM produtos ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                produtos.add(criarProduto(resultado));
            }
        }

        return produtos;
    }

    public List<Produto> buscarPorNome(String nome) throws SQLException {
        String sql = """
            SELECT id, nome, categoria, preco, quantidade
            FROM produtos
            WHERE nome LIKE ?
            ORDER BY nome
            """;

        List<Produto> produtos = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, "%" + nome + "%");

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    produtos.add(criarProduto(resultado));
                }
            }
        }

        return produtos;
    }

    public Optional<Produto> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, categoria, preco, quantidade FROM produtos WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(criarProduto(resultado));
                }
            }
        }

        return Optional.empty();
    }

    public boolean atualizar(Produto produto) throws SQLException {
        String sql = """
            UPDATE produtos
            SET nome = ?, categoria = ?, preco = ?, quantidade = ?
            WHERE id = ?
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, produto.getNome());
            comando.setString(2, produto.getCategoria());
            comando.setBigDecimal(3, produto.getPreco());
            comando.setInt(4, produto.getQuantidade());
            comando.setInt(5, produto.getId());

            return comando.executeUpdate() > 0;
        }
    }

    public boolean atualizarQuantidade(int id, int quantidade) throws SQLException {
        String sql = "UPDATE produtos SET quantidade = ? WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, quantidade);
            comando.setInt(2, id);

            return comando.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);
            return comando.executeUpdate() > 0;
        }
    }

    private Produto criarProduto(ResultSet resultado) throws SQLException {
        return new Produto(
            resultado.getInt("id"),
            resultado.getString("nome"),
            resultado.getString("categoria"),
            resultado.getBigDecimal("preco"),
            resultado.getInt("quantidade")
        );
    }
}
