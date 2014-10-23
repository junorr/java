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

import com.jpower.rfl.Reflector;
import us.pserver.sdb.engine.Index;
import us.pserver.sdb.engine.StorageEngine;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class ObjectDB {

  private StorageEngine engine;
  
  private boolean rmcascade;
  
  
  public ObjectDB(StorageEngine eng) throws SDBException {
    if(eng == null)
      throw new IllegalArgumentException(
          "Invalid DocumentEngine: "+ eng+ " - [SimpleDB.init]");
    engine = eng;
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
    OID id = new OID();
    if(obj == null) return id;
    Document doc = putDoc(
        ObjectUtils.toDocument(obj, true));
    return id.block(doc.block()).set(obj);
  }
  
  
  private Document putDoc(Document doc) throws SDBException {
    doc = createLinks(doc);
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
  
  
  public boolean remove(Object example) throws SDBException {
    return remove(new OID().set(example));
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
    if(oid.block() < 0) return false;
    
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
    Result docs = new Result();
    if(q == null) return objs;
    q = q.head();
    if(q.label() == null)
      return objs;
    if(q.key() == null 
        && q.method() == null 
        && q.value() == null)
      return get(q.label(), q.limit());
    
    Index id = engine.getIndex();
    List<int[]> idx = id.getList(q.label());
    
    if(idx == null || idx.isEmpty())
      return objs;
    
    Result rm = new Result();
    
    for(int[] is : idx) {
      if(is  == null || is[0] < 0) continue;
      
      Document d = getDoc(is[0]);
      if(d == null) continue;
      q = q.head();
      
      while(q != null && q.key() != null) 
      {
        if(!d.map().containsKey(q.key()))
          break;
        
        Object val = d.get(q.key());
        while(val != null && Document.class.isAssignableFrom(val.getClass())) {
          Document dv = (Document) val;
          q = q.next();
          val = dv.get(q.key());
        }
        
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
    idx = null;
    return convert(docs, objs);
  }
  
}
