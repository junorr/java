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

package br.com.bb.disec.micros.jiterator;

import br.com.bb.disec.micros.db.SqlObjectType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/09/2016
 */
public class ResultSetJsonIterator extends AbstractJsonIterator {

  private final ResultSet rset;
  
  private final SqlObjectType stype;
  
  private ResultSetMetaData meta;
  
  private String[] columns;
  
  private boolean next;
  
  
  public ResultSetJsonIterator(ResultSet rs) {
    if(rs == null) {
      throw new IllegalArgumentException("Bad Null ResultSet");
    }
    this.rset = rs;
    this.stype = new SqlObjectType();
  }
  
  
  private void readMetaData() throws JsonIteratorReadException {
    if(columns == null || meta == null) try {
      meta = rset.getMetaData();
      int cols = meta.getColumnCount();
      columns = new String[cols];
      for(int i = 1; i <= cols; i++) {
        columns[i-1] = meta.getColumnLabel(i);
      }
    }
    catch(SQLException e) {
      throw new JsonIteratorReadException(e);
    }
  }
  
  
  @Override
  public Document next() throws JsonIteratorReadException {
    try {
      if(!next) {
        return null;
      }
      this.readMetaData();
      Document doc = new Document();
      for(int i = 0; i < columns.length; i++) {
        doc.append(columns[i], stype.getObject(rset, i+1));
      }
      total++;
      return doc;
    }
    catch(SQLException e) {
      throw new JsonIteratorReadException(e);
    }
  }
  
  
  @Override
  public Stream<Document> stream() {
    return StreamSupport.stream(this.spliterator(), false);
  }


  @Override
  public boolean hasNext() {
    next = false;
    try { next = rset.next(); } 
    catch(SQLException e) {}
    return next;
  }


  @Override
  public Iterator<Document> iterator() {
    return this;
  }
  
}
