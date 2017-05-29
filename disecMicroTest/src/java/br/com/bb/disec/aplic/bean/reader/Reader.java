package br.com.bb.disec.aplic.bean.reader;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * Classe para criar beans
 * a partir de um <code>ResultSet</code>.
 * @author Juno Roesler - F6036477
 * @param <T> Tipo do bean gerado pelo Reader.
 */
public abstract class Reader<T> {
	
	protected final ResultSet rset;
	
	
	protected Reader(ResultSet rs) {
		if(rs == null) {
			throw new IllegalArgumentException(
					"ResultSet precisa ser NÃO NULO"
			);
		}
		this.rset = rs;
	}
	
	
	/**
	 * Retorna o ResultSet usado para gerar um bean.
	 * @return ResultSet
	 */
	public ResultSet getResultSet() {
		return rset;
	}
	
	
  /**
   * Identifies if the ResultSet contains a column with the given name.
   * @param col The column name to be verified.
   * @return [true] if the ResultSet contains the column, [false] otherwise.
   */
  protected boolean contains( String col ) {
    try {
      return rset.findColumn( col ) > 0;
    } catch( SQLException e ) {
      return false;
    }
  }
	

  /**
   * Identifies if the ResultSet contains a column with the given name.
   * @param en Enumeration of the verified field.
   * @return [true] if the ResultSet contains the column, [false] otherwise.
   */
  protected boolean contains(Enum<?> en) {
    try {
      return rset.findColumn( en.name() ) > 0;
    } catch( SQLException e ) {
      return false;
    }
  }
	

  /**
   * Create a bean with the ResultSet information.
   * @return The created DcrCtu bean.
	 * @throws SQLException In case of error reading the ResultSet.
   */
	public abstract T readBean() throws SQLException;
	
}
