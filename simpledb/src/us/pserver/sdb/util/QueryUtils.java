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

package us.pserver.sdb.util;

import us.pserver.sdb.Document;
import us.pserver.sdb.OID;
import us.pserver.sdb.query.Result;
import us.pserver.sdb.query.ResultOID;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 31/10/2014
 */
public class QueryUtils {

  public static ResultOID convert(Result rs, ResultOID ro) {
    if(rs == null || rs.isEmpty())
      return ro;
    if(ro == null)
      ro = new ResultOID();
    while(rs.hasNext()) {
      Document doc = rs.next();
      if(doc == null) continue;
      ro.addObj(ObjectUtils.fromDocument(doc), doc.block());
    }
    return ro;
  }
  
  
  public static Result convert(ResultOID rs, Result ro) {
    if(rs == null || rs.isEmpty())
      return ro;
    if(ro == null)
      ro = new Result();
    while(rs.hasNext()) {
      OID oid = rs.next();
      if(!oid.hasObject()) continue;
      Document d = ObjectUtils.toDocument(oid.get(), true);
      d.block(oid.block());
      ro.add(d);
    }
    return ro;
  }
  
}
