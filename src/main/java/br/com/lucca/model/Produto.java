package br.com.lucca.model;

import java.math.BigDecimal;

public class Produto {

    private int id;
    private String nome;
    private String categoria;
    private BigDecimal preco;
    private int quantidade;

    public Produto() {
    }

    public Produto(String nome, String categoria, BigDecimal preco, int quantidade) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public Produto(int id, String nome, String categoria, BigDecimal preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return String.format(
            "ID: %d | %s | Categoria: %s | Preço: R$ %.2f | Estoque: %d",
            id, nome, categoria, preco, quantidade
        );
    }
}
