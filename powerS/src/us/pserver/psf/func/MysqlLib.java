/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.psf.func;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSFunctionExtension;
import murlen.util.fscript.FSObject;
import murlen.util.fscript.FSUnsupportedException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/01/2014
 */
public class MysqlLib implements FSFunctionExtension {
  
  public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
  
  public static final String URL_PRE = "jdbc:mysql://";
  
  public static final String URL_POS = ":3306/";
  

  public static final String
      MYSQL_CONNECT = "mysql_connect",
      MYSQL_CONNECT_URL = "mysql_connect_url",
      MYSQL_CLOSE = "mysql_close",
      MYSQL_QUERY = "mysql_query",
      MYSQL_UPDATE = "mysql_update",
      MYSQL_NUM_ROWS = "mysql_num_rows",
      MYSQL_NUM_COLS = "mysql_num_cols",
      MYSQL_COLNAME = "mysql_colname",
      MYSQL_NEXT = "mysql_next",
      MYSQL_COLUMN = "mysql_column",
      MYSQL_COL_STRING = "mysql_col_string",
      MYSQL_COL_INT = "mysql_col_int",
      MYSQL_COL_DOUBLE = "mysql_col_double";
  
  
  private static boolean driverLoaded = false;
  
  
  public MysqlLib() {
  }
  
  
  private void chkstr(String str, String msg) throws FSException {
    if(str == null || str.trim().isEmpty())
      throw new FSException(msg+ ": "+ str);
  }
  
  
  private void chkobj(Object obj, String msg) throws FSException {
    if(obj == null)
      throw new FSException(msg+ ": "+ obj);
  }
  
  
  private void chkobj(Object obj, Class cls, String msg) throws FSException {
    if(obj == null || !cls.isAssignableFrom(obj.getClass()))
      throw new FSException(msg+ ": "+ obj+ "("+ cls.getName()+ ")");
  }
  
  
  public Connection mysql_connect(String addr, String user, String pass) throws FSException {
    chkstr(addr, "Invalid Address");
    return mysql_connect_url(URL_PRE + addr + URL_POS, user, pass);
  }
  
  
  public Connection mysql_connect(String addr, String schema, String user, String pass) throws FSException {
    chkstr(addr, "Invalid Address");
    chkstr(schema, "Invalid Schema");
    return mysql_connect_url(URL_PRE + addr + URL_POS + schema, user, pass);
  }
  
  
  public Connection mysql_connect_url(String url, String user, String pass) throws FSException {
    chkstr(url, "Invalid URL");
    chkstr(user, "Invalid User");
    chkstr(pass, "Invalid Password");
    
    if(!driverLoaded) {
      try {
        Class.forName(MYSQL_DRIVER);
      } catch(ClassNotFoundException e) {
        throw new FSException(e.toString());
      }
      driverLoaded = true;
    }
    
    try {
      return DriverManager.getConnection(url, user, pass);
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void mysql_close(Object conn) throws FSException {
    if(conn == null) return;
    try {
      if(conn instanceof FSObject) {
        conn = ((FSObject)conn).getObject();
      }
      if(conn instanceof Connection) {
        ((Connection)conn).close();
      }
      else if(conn instanceof ResultSet) {
        ((ResultSet)conn).close();
      }
    } catch(Exception e) {}
  }
  
  
  public ResultSet mysql_query(Object conn, String sql) throws FSException {
    chkobj(conn, "Invalid Object Connection");
    chkstr(sql, "Invalid SQL Query");
    if(conn instanceof FSObject) {
      conn = ((FSObject)conn).getObject();
    }
    chkobj(conn, Connection.class, "Invalid Object Type");
    try {
      Statement stm = ((Connection)conn).createStatement();
      return stm.executeQuery(sql);
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int mysql_update(Object conn, String sql) throws FSException {
    chkobj(conn, "Invalid Object Connection");
    chkstr(sql, "Invalid SQL Query");
    if(conn instanceof FSObject) {
      conn = ((FSObject)conn).getObject();
    }
    chkobj(conn, Connection.class, "Invalid Object Type");
    try {
      Statement stm = ((Connection)conn).createStatement();
      return stm.executeUpdate(sql);
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int mysql_num_rows(Object result) throws FSException {
    chkobj(result, "Invalid ResultSet");
    if(result instanceof FSObject) {
      result = ((FSObject)result).getObject();
    }
    
    chkobj(result, ResultSet.class, "Invalid Object Type");
    ResultSet rs = (ResultSet) result;
    
    try {
      int row = rs.getRow();
      rs.last();
      int num = rs.getRow();
      if(row < 1) rs.beforeFirst();
      else rs.absolute(row);
      return num;
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int mysql_num_cols(Object result) throws FSException {
    chkobj(result, "Invalid ResultSet");
    if(result instanceof FSObject) {
      result = ((FSObject)result).getObject();
    }
    
    chkobj(result, ResultSet.class, "Invalid Object Type");
    ResultSet rs = (ResultSet) result;
    
    try {
      return rs.getMetaData().getColumnCount();
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public String mysql_colname(Object result, int column) throws FSException {
    chkobj(result, "Invalid ResultSet");
    if(result instanceof FSObject) {
      result = ((FSObject)result).getObject();
    }
    
    chkobj(result, ResultSet.class, "Invalid Object Type");
    ResultSet rs = (ResultSet) result;
    
    try {
      return rs.getMetaData().getColumnLabel(column);
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int mysql_next(Object result) throws FSException {
    chkobj(result, "Invalid ResultSet");
    if(result instanceof FSObject) {
      result = ((FSObject)result).getObject();
    }
    
    chkobj(result, ResultSet.class, "Invalid Object Type");
    ResultSet rs = (ResultSet) result;
    
    try {
      return (rs.next() ? 1 : 0);
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public Object mysql_column(Object result, Object column) throws FSException {
    chkobj(result, "Invalid ResultSet");
    chkobj(column, "Invalid Column");
    if(result instanceof FSObject) {
      result = ((FSObject)result).getObject();
    }
    if(column instanceof FSObject) {
      column = ((FSObject)column).getObject();
    }
    
    chkobj(result, ResultSet.class, "Invalid Object Type");
    ResultSet rs = (ResultSet) result;
    
    try {
      if(column instanceof Integer) {
        return rs.getObject((int) column);
      }
      else {
        return rs.getObject(column.toString());
      }
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public String mysql_col_string(Object result, Object column) throws FSException {
    chkobj(result, "Invalid ResultSet");
    chkobj(column, "Invalid Column");
    if(result instanceof FSObject) {
      result = ((FSObject)result).getObject();
    }
    if(column instanceof FSObject) {
      column = ((FSObject)column).getObject();
    }
    
    chkobj(result, ResultSet.class, "Invalid Object Type");
    ResultSet rs = (ResultSet) result;
    
    try {
      if(column instanceof Integer) {
        return rs.getString((int) column);
      }
      else {
        return rs.getString(column.toString());
      }
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int mysql_col_int(Object result, Object column) throws FSException {
    chkobj(result, "Invalid ResultSet");
    chkobj(column, "Invalid Column");
    if(result instanceof FSObject) {
      result = ((FSObject)result).getObject();
    }
    if(column instanceof FSObject) {
      column = ((FSObject)column).getObject();
    }
    
    chkobj(result, ResultSet.class, "Invalid Object Type");
    ResultSet rs = (ResultSet) result;
    
    try {
      if(column instanceof Integer) {
        return rs.getInt((int) column);
      }
      else {
        return rs.getInt(column.toString());
      }
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public double mysql_col_double(Object result, Object column) throws FSException {
    chkobj(result, "Invalid ResultSet");
    chkobj(column, "Invalid Column");
    if(result instanceof FSObject) {
      result = ((FSObject)result).getObject();
    }
    if(column instanceof FSObject) {
      column = ((FSObject)column).getObject();
    }
    
    chkobj(result, ResultSet.class, "Invalid Object Type");
    ResultSet rs = (ResultSet) result;
    
    try {
      if(column instanceof Integer) {
        return rs.getDouble((int) column);
      }
      else {
        return rs.getDouble(column.toString());
      }
    } catch(SQLException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    switch(string) {
      case MYSQL_CLOSE:
        FUtils.checkLen(al, 1);
        mysql_close(al.get(0));
        return null;
      case MYSQL_COLNAME:
        FUtils.checkLen(al, 2);
        return mysql_colname(al.get(0), FUtils._int(al, 1));
      case MYSQL_COLUMN:
        FUtils.checkLen(al, 2);
        return mysql_column(al.get(0), al.get(1));
      case MYSQL_COL_DOUBLE:
        FUtils.checkLen(al, 2);
        return mysql_col_double(al.get(0), al.get(1));
      case MYSQL_COL_INT:
        FUtils.checkLen(al, 2);
        return mysql_col_int(al.get(0), al.get(1));
      case MYSQL_COL_STRING:
        FUtils.checkLen(al, 2);
        return mysql_col_string(al.get(0), al.get(1));
      case MYSQL_CONNECT:
        FUtils.checkLen(al, 3);
        if(al.size() == 3)
          return mysql_connect(FUtils.str(al, 0), FUtils.str(al, 1), FUtils.str(al, 2));
        else if(al.size() > 3)
          return mysql_connect(FUtils.str(al, 0), FUtils.str(al, 1), FUtils.str(al, 2), FUtils.str(al, 3));
        return null;
      case MYSQL_CONNECT_URL:
        FUtils.checkLen(al, 3);
        return mysql_connect_url(FUtils.str(al, 0), FUtils.str(al, 1), FUtils.str(al, 2));
      case MYSQL_NEXT:
        FUtils.checkLen(al, 1);
        return mysql_next(al.get(0));
      case MYSQL_NUM_COLS:
        FUtils.checkLen(al, 1);
        return mysql_num_cols(al.get(0));
      case MYSQL_NUM_ROWS:
        FUtils.checkLen(al, 1);
        return mysql_num_rows(al.get(0));
      case MYSQL_QUERY:
        FUtils.checkLen(al, 2);
        return mysql_query(al.get(0), FUtils.str(al, 1));
      case MYSQL_UPDATE:
        FUtils.checkLen(al, 2);
        return mysql_update(al.get(0), FUtils.str(al, 1));
      default:
        throw new FSUnsupportedException();
    }
  }
  
  
  public void addTo(FSFastExtension fs) {
    if(fs == null) return;
    fs.addFunctionExtension(MYSQL_CLOSE, this);
    fs.addFunctionExtension(MYSQL_COLNAME, this);
    fs.addFunctionExtension(MYSQL_COLUMN, this);
    fs.addFunctionExtension(MYSQL_COL_DOUBLE, this);
    fs.addFunctionExtension(MYSQL_COL_INT, this);
    fs.addFunctionExtension(MYSQL_COL_STRING, this);
    fs.addFunctionExtension(MYSQL_CONNECT, this);
    fs.addFunctionExtension(MYSQL_CONNECT_URL, this);
    fs.addFunctionExtension(MYSQL_NEXT, this);
    fs.addFunctionExtension(MYSQL_NUM_COLS, this);
    fs.addFunctionExtension(MYSQL_NUM_ROWS, this);
    fs.addFunctionExtension(MYSQL_QUERY, this);
    fs.addFunctionExtension(MYSQL_UPDATE, this);
  }
  
  
  public static void main(String[] args) throws FSException {
    MysqlLib lib = new MysqlLib();
    Connection con = lib.mysql_connect("172.29.14.103", "F6036477", "6036577");
    ResultSet rs = lib.mysql_query(con, "SELECT * FROM arhNovo.tb_est");
    System.out.println("SELECT * FROM arhNovo.tb_est >> "+ lib.mysql_num_rows(rs)+ " rows");
    int cols = lib.mysql_num_cols(rs);
    int count = 0;
    int first = 1;
    while(lib.mysql_next(rs) == 1) {
      count = 0;
      if(first == 1) {
        while(++count <= cols) {
          System.out.print(lib.mysql_colname(rs, count)+ "\t");
        }
        System.out.println();
        count = 0;
        while(++count <= cols) {
          System.out.print("------\t");
        }
        System.out.println();
        first = 0;
        count = 0;
      }
      while(++count <= cols)
        System.out.print(lib.mysql_column(rs, count)+ "\t");
      System.out.println();
    }
    lib.mysql_close(rs);
    lib.mysql_close(con);
  }
  
}
