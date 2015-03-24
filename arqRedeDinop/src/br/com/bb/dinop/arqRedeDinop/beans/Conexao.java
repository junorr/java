package br.com.bb.dinop.arqRedeDinop.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Conexao {
	

    private String driver;
    private String url;
    private String usuario;
    private String senha;

    /**
     * Modelo de conexão para MySQL.
     * @param driver Driver do BD
     * @param url ip do servidor
     * @param usuario Usuário habilitado no servidor
     * @param senha Senha do usuário
     */
    public Conexao(String driver, String url, String usuario, String senha) {
        super();
        this.driver = driver;
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

    /**
     * Modelo de conexão para Data Source.
     * @param alias servidor DS
     */
    public Conexao(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    /**
     * Retorna uma conexão.
     * @return Connection
     * @throws Exception
     */
    public Connection getConexao() throws Exception {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(getUrl(), getUsuario(), getSenha());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Erro nos parametros de conexao!");
        } catch (ClassNotFoundException e1) {
            throw new Exception("O driver não foi encontrado!");
        }
    }

    /**
     * Retorna uma conexão do tipo DataSource (TomCat)
     * @return Connection
     * @throws Exception
     */
    public Connection getDS() throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envContext = (Context) ctx.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup(url);
            Connection conn = ds.getConnection();
            return conn;
        } catch (Exception e) {
            throw new Exception();
        }
    }

}
