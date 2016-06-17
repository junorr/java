package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate LogUpload
 * JavaBean from a given java.sql.ResultSet.
 */
public class LogUploadReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the LogUpload information.
   */
  public LogUploadReader( ResultSet rs ) {
    if(rs == null) {
      throw new IllegalArgumentException("ResultSet must be not null");
    }
    this.rset = rs;
  }

  /**
   * Get the ResultSet of this BeanReader.
   * @return java.sql.ResultSet.
   */
  public ResultSet getResultSet() {
    return this.rset;
  }

  /**
   * Identifies if the ResultSet contains a column with the given name.
   * @param col The column name to be verified.
   * @return [true] if the ResultSet contains the column, [false] otherwise.
   */
  private boolean contains( String col ) {
    try {
      return rset.findColumn( col ) > 0;
    } catch( SQLException e ) {
      return false;
    }
  }

  /**
   * Create a LogUpload bean with the ResultSet information.
   * @return The created LogUpload bean.
   */
  public ILogUpload readBean() throws SQLException {
    ILogUpload bean = new LogUpload();
    if(this.contains( ILogUpload.COLUMN_CD_LOG )) {
      bean.setCdLog( rset.getInt( ILogUpload.COLUMN_CD_LOG) );
    }
    if(this.contains( ILogUpload.COLUMN_DT_LOG )) {
      bean.setDtLog( rset.getTimestamp( ILogUpload.COLUMN_DT_LOG) );
    }
    if(this.contains( ILogUpload.COLUMN_CD_USU )) {
      bean.setCdUsu( rset.getString( ILogUpload.COLUMN_CD_USU) );
    }
    if(this.contains( ILogUpload.COLUMN_FILE_SIZE )) {
      bean.setFileSize( rset.getInt( ILogUpload.COLUMN_FILE_SIZE) );
    }
    if(this.contains( ILogUpload.COLUMN_AUTD )) {
      bean.setAutd( rset.getShort( ILogUpload.COLUMN_AUTD) );
    }
    if(this.contains( ILogUpload.COLUMN_NM_USU )) {
      bean.setNmUsu( rset.getString( ILogUpload.COLUMN_NM_USU) );
    }
    return bean;
  }

}