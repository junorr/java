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

package us.pserver.sdb;

import us.pserver.sdb.util.ObjectUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 31/10/2014
 */
public class QueryUtils {

  
  public static void match(Document doc, Query q, Result docs, Result rm) {
    if(q == null || doc == null) return;
    
    while(q != null && (q.key() != null || q.other() != null || q.isDescend())) 
    {
      if(q.key() != null && !doc.map().containsKey(q.key()))
        break;
        
      q = queryOther(q, docs, rm);
      if(q == null 
          || (q.key() == null 
          &&  q.other() == null 
          && !q.isDescend()))
        break;
        
      Object val = doc.get(q.key());
      Document dv = null;
      while(q.isDescend()) {
        if(val != null && Document.class.isAssignableFrom(val.getClass())) {
          dv = (Document) val;
        }
        q = q.next();
        if(q.key() == null || dv == null) 
          break;
        val = dv.get(q.key());
      }
        
      boolean chk = q.exec(val).getResult();
      
      System.out.println("* exec: "+ q.field()+ ": ("+ val+ (q.isNot() ? ") !" : ") ")
          + q.method()+ " "+ q.value()+ ": "+ chk);
        
      if(chk) {
        if(!docs.contains(doc) 
            && !rm.contains(doc))
          docs.add(doc);
      } else if(q.prev() != null && q.prev().isAnd()) {
        if(docs.contains(doc)) {
          docs.remove(doc);
          rm.add(doc);
        }
      }
      q = q.next();
      if(q.isDescend() && dv != null
          && dv.map().containsKey(q.key())) {
        Result rdc = new Result();
        match(dv, q, rdc, rm);
        if(rdc.isEmpty() && docs.contains(doc))
          docs.remove(doc);
      }
    }//while
  }
  

  public static Query queryOther(Query q, Result docs, Result rm) {
    if(q == null || q.other() == null || docs == null)
      return q;
    
    while(q != null && q.other() != null) {
      System.out.println("* exec.other = "+ q.other());
      Result ors = docs.filter(q.other());
      if(ors.isEmpty()) continue;
      if(q.prev().isAnd()) {
        for(int i = 0; i < docs.size(); i++) {
          Document dd = docs.get(i);
          if(!ors.containsBlock(dd.block())
              && !rm.containsBlock(dd.block()))
            rm.add(dd);
        }
        for(int i = 0; i < rm.size(); i++) {
          Document dd = rm.get(i);
          if(docs.containsBlock(dd.block())) {
            System.out.println("  -> removing by other: "+ dd);
            docs.removeBlock(dd.block());
          }
        }
      }
      else {
        for(int i = 0; i < ors.size(); i++) {
          Document dd = ors.get(i);
          if(!docs.containsBlock(dd.block()))
            docs.add(dd);
        }
      }
      q = q.next();
    }
    return q;
  }
  
  
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
