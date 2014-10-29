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

import us.pserver.sdb.engine.Index;
import us.pserver.sdb.engine.StorageEngine;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class SimpleDB {

  private StorageEngine engine;
  
  private boolean rmcascade;
  
  
  public SimpleDB(StorageEngine eng) throws SDBException {
    if(eng == null)
      throw new IllegalArgumentException(
          "Invalid DocumentEngine: "+ eng+ " - [SimpleDB.init]");
    engine = eng;
    rmcascade = true;
  }
  
  
  public StorageEngine getEngine() {
    return engine;
  }
  
  
  public SimpleDB setRemoveOnCascade(boolean bool) {
    rmcascade = bool;
    return this;
  }
  
  
  public boolean isRemoveOnCascade() {
    return rmcascade;
  }
  
  
  public void close() throws SDBException {
    engine.close();
  }
  
  
  protected Document resolveLinks(Document doc) {
    if(doc == null) return doc;
    Iterator<String> it = doc.map().keySet().iterator();
    while(it.hasNext()) {
      String key = it.next();
      if(key.startsWith("@_")) {
        Object o = doc.get(key);
        if(o != null && Long.class
            .isAssignableFrom(o.getClass())) {
          Long l = doc.getAs(key);
          doc.map().remove(key);
          Document d = get(l);
          doc.put(key.substring(2), d);
        }
      }
    }
    return doc;
  }
  
  
  protected Document createLinks(Document doc) {
    if(doc == null) return doc;
    
    Iterator<String> it = doc.map().keySet().iterator();
    while(it.hasNext()) {
      String key = it.next();
      Object o = doc.get(key);
      if(o != null && Document.class
          .isAssignableFrom(o.getClass())) {
        Document d = doc.getAs(key);
        if(d.block() < 0)
          d = put(d);
        doc.map().remove(key);
        doc.put("@_"+ key, d.block());
      }
    }
    return doc;
  }
  
  
  private void printdoc(Document doc) {
    if(doc == null)
      return;
    System.out.println("* Document: label="+ doc.label());
    Iterator<String> it = doc.map().keySet().iterator();
    while(it.hasNext()) {
      String k = it.next();
      System.out.println("  - "+ k+ "="+ doc.map().get(k));
    }
    System.out.println("* -----------------------------");
  }
  
  
  protected void rmNested(Document doc) {
    if(doc == null) return;
    
    Iterator<String> it = doc.map().keySet().iterator();
    while(it.hasNext()) {
      String key = it.next();
      Object o = doc.get(key);
      if(o != null && Document.class
          .isAssignableFrom(o.getClass())) {
        Document d = doc.getAs(key);
        if(d.block() >= 0)
          remove(d.block());
      }
    }
  }
  
  
  public Document put(Document doc) throws SDBException {
    if(doc == null) return doc;
    doc = createLinks(doc);
    //printdoc(doc);
    return engine.put(doc);
  }
  
  
  public Document remove(long blk) throws SDBException {
    Document doc = get(blk);
    rmNested(doc);
    return engine.remove(blk);
  }
  
  
  public boolean remove(Document doc) throws SDBException {
    if(doc == null || doc.label() == null
        || doc.map().isEmpty()) 
      return false;
    
    if(doc.block() < 0) {
      Document d = getOne(doc);
      if(d == null) return false;
      doc.block(d.block());
    }
    if(doc.block() < 0) return false;
    
    return remove(doc.block()) != null;
  }
  
  
  public Result remove(Query q) throws SDBException {
    Result res = get(q);
    for(Document d : res)
      this.remove(d.block());
    return res;
  }
  
  
  public Document get(long blk) throws SDBException {
    Document doc = engine.get(blk);
    return resolveLinks(doc);
  }
  
  
  public Document getOne(Document doc) throws SDBException {
    return getOne(Query.fromExample(doc));
  }
  
  
  public Document getOne(Query q) throws SDBException {
    if(q == null) return null;
    Result docs = get(q.limit(1));
    if(docs.isEmpty()) return null;
    Document d = docs.get(0);
    docs.clear();
    docs = null;
    return d;
  }
  
  
  public Result get(Document doc) throws SDBException {
    return get(Query.fromExample(doc));
  }
  
  
  public Result get(String label, int limit) throws SDBException {
    Result docs = new Result(limit);
    if(label == null) return docs;
    
    List<int[]> idx = engine.getIndex().getList(label);
    if(idx == null || idx.isEmpty())
      return docs;
    
    for(int[] is : idx) {
      if(is == null || is[0] < 0) continue;
      Document d = get(is[0]);
      if(d == null) continue;
      docs.add(d);
    }
    return docs;
  }
  
  
  public Result get(Query q) throws SDBException {
    Result docs = new Result();
    if(q == null) return docs;
    q = q.head();
    if(q.label() == null)
      return docs;
    if(q.key() == null 
        && q.method() == null 
        && q.value() == null)
      return get(q.label(), q.limit());
    
    Index id = engine.getIndex();
    List<int[]> idx = id.getList(q.label());
    
    if(idx == null || idx.isEmpty())
      return docs;
    
    Result rm = new Result();
    
    for(int[] is : idx) {
      if(is  == null || is[0] < 0) continue;
      
      Document d = get(is[0]);
      if(d == null) continue;
      q = q.head();
      
      while(q != null && q.key() != null) 
      {
        if(!d.map().containsKey(q.key()))
          break;
        
        Object val = d.get(q.key());
        while(q.isDescend()) {
          Document dv = null;
          if(val != null && Document.class.isAssignableFrom(val.getClass())) {
            dv = (Document) val;
          }
          q = q.next();
          if(q.key() == null || dv == null) 
            break;
          val = dv.get(q.key());
        }
        if(q == null) continue;
        
        boolean chk = q.exec(val).getResult();
        
        System.out.println("* exec: "+ q.field()+ ": ("+ val+ (q.isNot() ? ") !" : ") ")
            + q.method()+ " "+ q.value()+ ": "+ chk);
        
        if(chk) {
          if(!docs.containsBlock(d.block()) 
              && !rm.containsBlock(d.block()))
            docs.add(d);
        } else if(q.prev() != null && q.prev().isAnd()) {
          if(docs.containsBlock(d.block())) {
            docs.removeBlock(d.block());
            rm.add(d);
          }
        } else if(q.prev() != null && !q.prev().isOr()) {
          rm.add(d);
        }
        q = q.next();
      }//while
      
      d = null;
      if(q.limit() > 0 && docs.size() >= q.limit()) 
        break;
    }//for
    
    rm.clear();
    rm = null;
    idx = null;
    return docs;
  }
  
  
  private Result query(Query q, List<Document> list) throws SDBException {
    Result docs = new Result();
    if(q == null || list == null 
        || list.isEmpty()) 
      return docs;
    q = q.head();
    if(q.label() == null)
      return docs;
    if(q.key() == null 
        && q.method() == null 
        && q.value() == null)
      return docs;
    
    Result rm = new Result();
    
    for(Document d : list) {
      if(d == null) continue;
      q = q.head();
      
      while(q != null && (q.key() != null || q.other() != null)) 
      {
        if(q.key() != null && !d.map().containsKey(q.key()))
          break;
        
        while(q != null && q.other() != null) {
          System.out.println("* exec.other = "+ q.other());
          Result ors = query(q.other(), docs);
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
        if(q == null 
            || q.key() == null 
            || q.method() == null) 
          continue;
        
        Object val = d.get(q.key());
        while(q.isDescend()) {
          Document dv = null;
          if(val != null && Document.class.isAssignableFrom(val.getClass())) {
            dv = (Document) val;
          }
          q = q.next();
          if(q.key() == null || dv == null) 
            break;
          val = dv.get(q.key());
        }
        //if(q == null) continue;
        
        boolean chk = q.exec(val).getResult();
        
        System.out.println("* exec: "+ q.field()+ ": ("+ val+ (q.isNot() ? ") !" : ") ")
            + q.method()+ " "+ q.value()+ ": "+ chk);
        
        if(chk) {
          if(!docs.containsBlock(d.block()) 
              && !rm.containsBlock(d.block()))
            docs.add(d);
        } else if(q.prev() != null && q.prev().isAnd()) {
          if(docs.containsBlock(d.block())) {
            docs.removeBlock(d.block());
            rm.add(d);
          }
        } else {
          rm.add(d);
        }
        q = q.next();
      }//while
      
      d = null;
      if(q.limit() > 0 && docs.size() >= q.limit()) 
        break;
    }//for
    
    rm.clear();
    rm = null;
    return docs;
  }
  

  public Result get2(Query q) throws SDBException {
    Result docs = new Result();
    if(q == null) return docs;
    q = q.head();
    if(q.label() == null)
      return docs;
    if(q.key() == null 
        && q.method() == null 
        && q.value() == null)
      return get(q.label(), q.limit());
    
    Index id = engine.getIndex();
    List<int[]> idx = id.getList(q.label());
    
    if(idx == null || idx.isEmpty())
      return docs;
    
    Result rm = new Result();
    
    for(int[] is : idx) {
      if(is  == null || is[0] < 0) continue;
      
      Document d = get(is[0]);
      if(d == null) continue;
      q = q.head();
      
      while(q != null && (q.key() != null || q.other() != null)) 
      {
        if(q.key() != null && !d.map().containsKey(q.key()))
          break;
        
        while(q != null && q.other() != null) {
          System.out.println("* exec.other = "+ q.other());
          Result ors = query(q.other(), docs);
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
        if(q == null 
            || q.key() == null 
            || q.method() == null) 
          continue;
        
        Object val = d.get(q.key());
        while(q.isDescend()) {
          Document dv = null;
          if(val != null && Document.class.isAssignableFrom(val.getClass())) {
            dv = (Document) val;
          }
          q = q.next();
          if(q.key() == null || dv == null) 
            break;
          val = dv.get(q.key());
        }
        //if(q == null) continue;
        
        boolean chk = q.exec(val).getResult();
        
        System.out.println("* exec: "+ q.field()+ ": ("+ val+ (q.isNot() ? ") !" : ") ")
            + q.method()+ " "+ q.value()+ ": "+ chk);
        
        if(chk) {
          if(!docs.containsBlock(d.block()) 
              && !rm.containsBlock(d.block()))
            docs.add(d);
        } else if(q.prev() != null && q.prev().isAnd()) {
          if(docs.containsBlock(d.block())) {
            docs.removeBlock(d.block());
            rm.add(d);
          }
        } else if(q.prev() != null && !q.prev().isOr()) {
          rm.add(d);
        }
        
        q = q.next();
      }//while
      
      d = null;
      if(q.limit() > 0 && docs.size() >= q.limit()) 
        break;
    }//for
    
    rm.clear();
    rm = null;
    idx = null;
    return docs;
  }
  
}
