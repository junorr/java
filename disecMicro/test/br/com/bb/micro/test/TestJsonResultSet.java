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

package br.com.bb.micro.test;

import br.com.bb.disec.micro.db.ConnectionPool;
import br.com.bb.disec.micro.json.JsonResultSet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class TestJsonResultSet {
  
  public static void main(String[] args) throws IOException, SQLException {/*
    ConnectionPool pool = ConnectionPool.createPool("default");
    Connection cn = pool.getConnection();
    Statement st = cn.createStatement();
    ResultSet rs = st.executeQuery("select * from orc.DESPESA;");
    JsonResultSet jrs = new JsonResultSet(rs);
    System.out.println(jrs.getJsonObject().getPath().toAbsolutePath().toString());
    jrs.getJsonObject().close();
    pool.close(cn, st, rs);
    pool.closeDataSource();*/
  }
  
}
