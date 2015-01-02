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

import us.pserver.sdb.query.Result;
import us.pserver.sdb.query.ResultOID;
import us.pserver.sdb.util.QueryUtils;
import java.util.ArrayList;
import us.pserver.sdb.util.ObjectUtils;
import us.pserver.sdb.engine.Index;
import us.pserver.sdb.engine.StorageEngine;
import java.util.Iterator;
import java.util.List;
import us.pserver.sdb.engine.CachedEngine;
import us.pserver.sdb.engine.DocHits;
import us.pserver.sdb.query.Query;
import us.pserver.sdb.query.QueryBuilder;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class ObjectDB implements DBEngine {

  private CachedEngine engine;
  
  private boolean rmcascade;
  
  
  protected ObjectDB() {
    engine = null;
    rmcascade = true;
  }
  
  
  public ObjectDB(StorageEngine eng) throws SDBException {
    if(eng == null)
      throw new IllegalArgumentException(
          "Invalid DocumentEngine: "+ eng+ " - [SimpleDB.init]");
    engine = new CachedEngine(eng);
    rmcascade = true;
  }
  
  
  @Override
  public StorageEngine getEngine() {
    return engine;
  }
  
  
  @Override
  public void setRemoveOnCascade(boolean bool) {
    rmcascade = bool;
  }
  
  
  @Override
  public boolean isRemoveOnCascade() {
    return rmcascade;
  }
  
  
  @Override
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
  
  
  @Override
  public OID put(Object obj) throws SDBException {
    OID id = new OID();
    if(obj == null) return id;
    if(obj instanceof OID) {
      return put((OID)obj);
    }
    Document doc = putDoc(
        ObjectUtils.toDocument(obj, true));
    return id.block(doc.block()).set(obj);
  }
  
  
  private Document putDoc(Document doc) throws SDBException {
    if(doc == null) return doc;
    doc = createLinks(doc);
    Document dc = findCached(
        QueryBuilder.builder().fromExample(doc));
    if(dc != null) {
      //System.out.println("* found in cache!!");
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
  
  
  @Override
  public OID remove(long blk) throws SDBException {
    OID id = new OID();
    Document doc = getDoc(blk);
    if(doc == null) return id;
    rmNested(doc);
    engine.remove(blk);
    return id.block(blk)
        .set(ObjectUtils.fromDocument(doc));
  }
  
  
  @Override
  public boolean remove(Object obj) throws SDBException {
    if(obj == null) return false;
    Document doc = findCached(
        QueryBuilder.builder().fromExample(
            ObjectUtils.toDocument(obj, true)));
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
  
  
  @Override
  public ResultOID removeAll(Query q) throws SDBException {
    ResultOID res = get(q);
    for(OID id : res)
      this.remove(id.block());
    return res;
  }
  
  
  @Override
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
  
  
  @Override
  public OID get(long blk) throws SDBException {
    OID id = new OID().block(blk);
    Document doc = getDoc(blk);
    if(doc == null) return id;
    return id.set(ObjectUtils.fromDocument(doc));
  }
  
  
  @Override
  public OID getOne(Object example) throws SDBException {
    OID id = new OID();
    if(example == null) return id;
    return getOne(
        QueryBuilder.builder().fromExample(
            ObjectUtils.forExample(example)));
  }
  
  
  public OID getOne(OID oid) throws SDBException {
    if(oid == null || (!oid.hasObject() && !oid.hasBlock()))
      return oid;
    if(oid.hasBlock())
      return getOne(oid.block());
    else
      return getOne(oid.get());
  }
  
  
  @Override
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
  
  
  @Override
  public ResultOID get(Object example) throws SDBException {
    ResultOID ro = new ResultOID(1);
    if(example == null) return ro;
    return get(
        QueryBuilder.builder().fromExample(
            ObjectUtils.forExample(example)));
  }
  
  
  @Override
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
  
  
  @Override
  public ResultOID get(Query q) throws SDBException {
    ResultOID objs = new ResultOID();
    if(q == null) return objs;
    //System.out.println("* get( "+ q+ " )");
    if(q.isEmpty() && q.label() != null)
      return get(q.label(), q.limit());
    
    Result rs = query(q);
    return QueryUtils.convert(rs, objs);
  }
  
  
  private Document findCached(Query q) throws SDBException {
    if(q == null) 
      return null;
    
    ArrayList<DocHits> cache = engine.getCache();
    if(cache == null || cache.isEmpty())
      return null;
    
    Result list = new Result();
    for(int i = 0; i < cache.size(); i++) {
      list.add(cache.get(i).document());
    }
    
    q.limit(1);
    Result rs = list.filter(q);
    if(rs == null || rs.isEmpty())
      return null;
    return rs.get(0);
  }

  
  private Result query(Query q) throws SDBException {
    Result docs = new Result();
    if(q == null || q.isEmpty()) 
      return docs;
    
    Index id = engine.getIndex();
    List<int[]> idx = id.getList(q.label());
    
    if(idx == null || idx.isEmpty())
      return docs;
    
    for(int[] is : idx) {
      if(is  == null || is[0] < 0) 
        continue;
      
      Document d = getDoc(is[0]);
      if(d == null) continue;
      
      if(q.exec(d))
        docs.add(d);
      
      if(q.limit() > 0 && docs.size() >= q.limit()) 
        break;
    }//for
    
    return docs;
  }
  
  
  @Override
  public ResultOID join(Query q, List list) {
    if(list == null) return null;
    ResultOID rs = new ResultOID();
    for(Object o : list) {
      if(o instanceof OID && !rs.contains(o))
        rs.add((OID) o);
      else
        rs.add(new OID().set(o));
    }
    if(q == null) return rs;
    ResultOID other = get(q);
    for(OID oid : other) {
      if(!rs.contains(oid))
        rs.add(oid);
    }
    return rs;
  }
  
}
