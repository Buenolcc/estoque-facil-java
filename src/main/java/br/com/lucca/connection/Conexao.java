package br.com.lucca.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexao {

    private static final Properties propriedades = new Properties();

    static {
        try (InputStream arquivo = Conexao.class.getResourceAsStream("/db.properties")) {
            if (arquivo == null) {
                throw new IllegalStateException(
                    "Arquivo db.properties não encontrado. Crie uma cópia de db.properties.example."
                );
            }

            propriedades.load(arquivo);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível ler as configurações do banco.", e);
        }
    }

    private Conexao() {
    }

    public static Connection conectar() throws SQLException {
        String url = propriedades.getProperty("db.url");
        String usuario = propriedades.getProperty("db.user");
        String senha = propriedades.getProperty("db.password");

        return DriverManager.getConnection(url, usuario, senha);
    }
}
