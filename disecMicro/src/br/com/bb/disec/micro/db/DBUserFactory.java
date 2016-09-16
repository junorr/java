package br.com.bb.disec.micro.db;

import br.com.bb.sso.bean.User;
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
public class DBUserFactory {

  public static final String SQL_GROUP = "auth";
  
  public static final String SQL_SEL_USER = "selectUser";

  
  public User createUser(String chave) throws SQLException, IOException {
    if(chave == null || chave.trim().isEmpty()) {
      return null;
    }
    String sql = SqlSourcePool.getDefaultSqlSource().getSql(SQL_GROUP, SQL_SEL_USER);
    Connection cn = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    User user = null;
    try {
      cn = PoolFactory.getPool(
          ConnectionPool.DEFAULT_DB_NAME
      ).getConnection();
      st = cn.prepareStatement(sql);
      st.setString(1, chave);
      rs = st.executeQuery();
      if(rs.next()) {
        user = this.getUser(rs);
      }
    }
    finally {
      ConnectionPool.close(cn, st, rs);
    }
    return user;
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
  
}