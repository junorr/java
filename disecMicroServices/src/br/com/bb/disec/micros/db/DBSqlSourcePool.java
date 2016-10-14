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

package br.com.bb.disec.micros.db;

import br.com.bb.disec.micro.ResourceLoader;
import br.com.bb.disec.micro.db.DefaultFileSqlSource;
import br.com.bb.disec.micro.db.SqlSource;
import br.com.bb.disec.micro.db.SqlSourcePool;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/10/2016
 */
public class DBSqlSourcePool implements SqlSource {

  private static final DBSqlSourcePool instance = new DBSqlSourcePool();
  
  
  private DBSqlSourcePool() {
    if(instance != null) {
      throw new IllegalStateException(getClass().getName()+ " is Already Created");
    }
    SqlSourcePool.pool().source(new DefaultDBSqlSource(
        new DefaultFileSqlSource(ResourceLoader.caller()))
    );
  }
  
  
  public static DBSqlSourcePool pool() {
    return instance;
  }


  @Override
  public String getSql(String group, String name) throws IOException {
    return SqlSourcePool.pool().getSql(group, name);
  }


  @Override
  public boolean containsSql(String group, String name) throws IOException {
    return SqlSourcePool.pool().containsSql(group, name);
  }
  
}
