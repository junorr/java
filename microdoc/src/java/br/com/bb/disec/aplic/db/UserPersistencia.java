package br.com.bb.disec.aplic.db;

import br.com.bb.disec.aplic.resource.FileSqlSource;
import br.com.bb.disec.aplic.resource.SqlSource;
import br.com.bb.disec.aplic.resource.ResourceLoader;
import br.com.bb.sso.bean.User;
import br.com.bb.sso.bean.Usuario;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/04/2016
 */
public class UserPersistencia extends Persistencia {

  public static final String GROUP = "arh";
  
  public static final String SQL_SEL_USER = "selectUser";
  
  public static final String SQL_RESOURCE = "/resources/usersim-sql.ini";
  
  private User user;
  
  private Usuario usu;
  
  public UserPersistencia() {
    super("103");
    this.user = null;
    this.usu = null;
  }
  
  
  @Override
  public String getQuery(String group, String name) throws IOException {
    SqlSource src = new FileSqlSource(
        ResourceLoader.caller().loadPath(SQL_RESOURCE)
    );
    return src.getSql(group, name);
  }
  
  
  public User getUser(String chave) throws SQLException, IOException {
    if(user != null && chave.equalsIgnoreCase(user.getChave())) {
      return user;
    }
    String sql = getQuery(GROUP, SQL_SEL_USER);
    Connection cn = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      cn = this.getConnection();
      st = cn.prepareStatement(sql);
      st.setString(1, chave);
      rs = st.executeQuery();
      if(rs.next()) {
        this.user = this.getUser(rs);
        this.usu = this.getUsuario(rs);
      }
    }
    finally {
      if(rs != null) rs.close();
      if(st != null) st.close();
      if(cn != null) cn.close();
    }
    return user;
  }
  
  public Usuario getUsuario(String chave) throws SQLException, IOException {
    if(usu != null && chave.equalsIgnoreCase(usu.getChave())) {
      return usu;
    }
    String sql = getQuery(GROUP, SQL_SEL_USER);
    Connection cn = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      cn = this.getConnection();
      st = cn.prepareStatement(sql);
      st.setString(1, chave);
      rs = st.executeQuery();
      if(rs.next()) {
        this.user = this.getUser(rs);
        this.usu = this.getUsuario(rs);
      }
    }
    finally {
      if(rs != null) rs.close();
      if(st != null) st.close();
      if(cn != null) cn.close();
    }
    return usu;
  }
  
  private User getUser(ResultSet rs) throws SQLException {
    User user = null;
    if(rs != null) {
      user = new User()
          .setChave(rs.getString(1))
          .setNome(rs.getString(2))
          .setNomeGuerra(rs.getString(3))
          .setCelular(rs.getString(4))
          .setChaveNome(rs.getString(5))
          .setCodigoComissao(rs.getInt(6))
          .setCodigoIOR(rs.getInt(7))
          .setCodigoPilar(rs.getInt(8))
          .setComissao(rs.getString(9))
          .setCpf(rs.getString(10))
          .setEmail(rs.getString(11))
          .setMci(rs.getLong(12))
          .setPrefixoDepe(rs.getInt(13))
          .setPrefixoDiretoria(rs.getInt(14))
          .setUF(rs.getString(15))
          .setTelefone(rs.getString(16))
          .setTipoDepe(rs.getInt(17))
          .setUorDepe(rs.getInt(18))
          .setUorEquipe(rs.getInt(19));
    }
    return user;
  }
  
  private Usuario getUsuario(ResultSet rs) throws SQLException {
    Usuario user = null;
    if(rs != null) {
      user = new Usuario();
      user.setChave(rs.getString(1));
      user.setNome(rs.getString(2));
      user.setNomeGuerra(rs.getString(3));
      user.setTelefoneCelular(rs.getString(4));
      user.setCodigoComissao(String.valueOf(rs.getInt(6)));
      user.setCodigoPilar(String.valueOf(rs.getInt(8)));
      user.setNumeroCPF(rs.getString(10));
      user.setEmail(rs.getString(11));
      user.setCodigoCliente(String.valueOf(rs.getLong(12)));
      user.setPrefixoDependencia(String.valueOf(rs.getInt(13)));
      user.setPrefixoDiretoria(String.valueOf(rs.getInt(14)));
      user.setSiglaUF(rs.getString(15));
      user.setTelefoneComercial(rs.getString(16));
      user.setTipoDependencia(String.valueOf(rs.getInt(17)));
      user.setCodigoUorDependencia(String.valueOf(rs.getInt(18)));
      user.setCdUorEqp(String.valueOf(rs.getInt(19)));
    }
    return user;
  }
  
}
