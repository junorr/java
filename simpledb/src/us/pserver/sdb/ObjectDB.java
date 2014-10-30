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

import java.util.ArrayList;
import us.pserver.sdb.util.ObjectUtils;
import us.pserver.sdb.engine.Index;
import us.pserver.sdb.engine.StorageEngine;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import us.pserver.sdb.engine.CachedEngine;
import us.pserver.sdb.engine.DocHits;
import static us.pserver.sdb.test.TestSDB.rm;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class ObjectDB {

  private CachedEngine engine;
  
  private boolean rmcascade;
  
  
  public ObjectDB(StorageEngine eng) throws SDBException {
    if(eng == null)
      throw new IllegalArgumentException(
          "Invalid DocumentEngine: "+ eng+ " - [SimpleDB.init]");
    engine = new CachedEngine(eng);
    rmcascade = true;
  }
  
  
  public StorageEngine getEngine() {
    return engine;
  }
  
  
  public ObjectDB setRemoveOnCascade(boolean bool) {
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
          Document d = getDoc(l);
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
          d = putDoc(d);
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
  
  
  public OID put(Object obj) throws SDBException {
    if(obj instanceof OID) 
      put((OID)obj);
    OID id = new OID();
    if(obj == null) return id;
    Document doc = putDoc(
        ObjectUtils.toDocument(obj, true));
    return id.block(doc.block()).set(obj);
  }
  
  
  private Document putDoc(Document doc) throws SDBException {
    if(doc == null) return doc;
    doc = createLinks(doc);
    Document dc = findCached(Query.fromExample(doc));
    if(dc != null) {
      doc.block(dc.block());
    }
    return engine.put(doc);
  }
  
  
  public OID put(OID oid) throws SDBException {
    if(oid == null || !oid.hasObject()) 
      return oid;
    Document doc = ObjectUtils.toDocument(oid.get(), true);
    doc.block(oid.block());
    doc = putDoc(doc);
    return oid.block(doc.block());
  }
  
  
  public OID remove(long blk) throws SDBException {
    OID id = new OID();
    Document doc = getDoc(blk);
    if(doc == null) return id;
    rmNested(doc);
    engine.remove(blk);
    return id.block(blk)
        .set(ObjectUtils.fromDocument(doc));
  }
  
  
  public boolean remove(Object obj) throws SDBException {
    if(obj == null) return false;
    Document doc = findCached(Query.fromExample(ObjectUtils.toDocument(obj, true)));
    if(doc != null && doc.block() >= 0) {
      return remove(doc.block()) != null;
    }
    else {
      return remove(new OID().set(obj));
    }
  }
  
  
  public boolean remove(OID oid) throws SDBException {
    if(oid == null || (!oid.hasObject() && !oid.hasBlock()))
      return false;
    
    if(oid.block() < 0) {
      OID d = getOne(oid.get());
      if(d == null || d.block() < 0) 
        return false;
      oid.block(d.block());
    }
    return remove(oid.block()) != null;
  }
  
  
  public ResultOID removeAll(Query q) throws SDBException {
    ResultOID res = get(q);
    for(OID id : res)
      this.remove(id.block());
    return res;
  }
  
  
  public OID removeOne(Query q) throws SDBException {
    OID res = getOne(q);
    if(res == null || !res.isSetted())
      return res;
    this.remove(res.block());
    return res;
  }
  
  
  private Document getDoc(long blk) throws SDBException {
    Document doc = engine.get(blk);
    return resolveLinks(doc);
  }
  
  
  public OID get(long blk) throws SDBException {
    OID id = new OID().block(blk);
    Document doc = getDoc(blk);
    if(doc == null) return id;
    return id.set(ObjectUtils.fromDocument(doc));
  }
  
  
  public OID getOne(Object example) throws SDBException {
    OID id = new OID();
    if(example == null) return id;
    return getOne(
        Query.fromExample(
            ObjectUtils.forExample(example)));
  }
  
  
  public OID getOne(OID oid) throws SDBException {
    if(oid == null || !oid.hasObject())
      return oid;
    if(oid.hasBlock())
      return getOne(oid.block());
    else
      return getOne(oid.get());
  }
  
  
  public OID getOne(Query q) throws SDBException {
    OID id = new OID();
    if(q == null) return id;
    ResultOID objs = get(q.limit(1));
    if(objs.isEmpty()) return id;
    id = objs.get(0);
    objs.clear();
    objs = null;
    return id;
  }
  
  
  public ResultOID get(Object example) throws SDBException {
    ResultOID ro = new ResultOID(1);
    if(example == null) return ro;
    return get(Query.fromExample(ObjectUtils.forExample(example)));
  }
  
  
  public ResultOID get(String className, int limit) throws SDBException {
    ResultOID docs = new ResultOID(limit);
    if(className == null) return docs;
    
    List<int[]> idx = engine.getIndex().getList(className);
    if(idx == null || idx.isEmpty())
      return docs;
    
    for(int[] is : idx) {
      if(is == null || is[0] < 0) continue;
      OID d = get(is[0]);
      if(d == null) continue;
      docs.add(d);
    }
    return docs;
  }
  
  
  private ResultOID convert(Result rs, ResultOID ro) {
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
  
  
  public ResultOID get(Query q) throws SDBException {
    ResultOID objs = new ResultOID();
    if(q == null) return objs;
    
    Index id = engine.getIndex();
    List<int[]> idx = id.getList(q.label());
    
    if(idx == null || idx.isEmpty())
      return objs;
    
    List<Document> ls = new LinkedList<>();
    for(int i = 0; i < idx.size(); i++) {
      int[] is = idx.get(i);
      if(is  == null || is[0] < 0) continue;
      Document d = getDoc(is[0]);
      if(d == null) continue;
      ls.add(d);
    }
    
    Result rs = query(q, ls);
    if(rs == null || rs.isEmpty())
      return objs;
    return convert(rs, objs);
  }
  
  
  private Document findCached(Query q) throws SDBException {
    if(q == null) 
      return null;
    
    ArrayList<DocHits> cache = engine.getCache();
    if(cache == null || cache.isEmpty())
      return null;
    
    List<Document> list = new LinkedList<>();
    for(int i = 0; i < cache.size(); i++) {
      list.add(cache.get(i).document());
    }
    
    q.limit(1);
    Result rs = query(q, list);
    if(rs == null || rs.isEmpty())
      return null;
    return rs.get(0);
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
  
}
