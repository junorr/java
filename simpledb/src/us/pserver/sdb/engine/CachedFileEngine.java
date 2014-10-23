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

package us.pserver.sdb.engine;

import java.util.ArrayList;
import us.pserver.sdb.Document;
import us.pserver.sdb.SDBException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/10/2014
 */
public class CachedFileEngine implements StorageEngine {
  
  public static final int DEFAULT_MAX_CACHE_SIZE = 50;

  private FileEngine engine;
  
  private ArrayList<DocHits> cache;
  
  private int maxCacheSize;
  
  
  public CachedFileEngine(String file) throws SDBException {
    engine = new FileEngine(file);
    maxCacheSize = DEFAULT_MAX_CACHE_SIZE;
    cache = new ArrayList<>(maxCacheSize);
  }
  
  
  public int getCacheSise() {
    return cache.size();
  }
  
  
  public int getMaxCacheSize() {
    return maxCacheSize;
  }
  
  
  public CachedFileEngine setMaxCacheSize(int max) {
    if(max <= 0)
      max = DEFAULT_MAX_CACHE_SIZE;
    maxCacheSize = max;
    if(maxCacheSize < cache.size()) {
      int dif = cache.size() - maxCacheSize;
      for(int i = 0; i < dif; i++) {
        pop();
      }
    }
    else {
      cache.ensureCapacity(maxCacheSize);
    }
    return this;
  }
  
  
  protected int indexOf(long block) {
    if(cache.isEmpty()) return -1;
    for(int i = 0; i < cache.size(); i++) {
      DocHits d = cache.get(i);
      if(d.document().block() == block)
        return i;
    }
    return -1;
  }
  
  
  protected DocHits find(long block) {
    int idx = indexOf(block);
    if(idx < 0) return null;
    DocHits d = cache.get(idx);
    return d;
  }
  
  
  protected boolean contains(long block) {
    return indexOf(block) >= 0;
  }
  
  
  protected boolean pop() {
    if(cache.isEmpty()) return false;
    int min = Integer.MAX_VALUE;
    int idx = -1;
    for(int i = cache.size() -1; i >= 0; i--) {
      DocHits d = cache.get(i);
      if(d.hits() <= min) {
        min = d.hits();
        idx = i;
      }
    }
    if(idx < 0) return false;
    cache.remove(idx);
    return true;
  }
  
  
  protected void cache(Document doc) {
    if(doc == null || doc.block() < 0) 
      return;
    
    int idx = indexOf(doc.block());
    if(idx >= 0) {
      DocHits d = cache.get(idx);
      d.document(doc);
      cache.set(idx, d.incHits());
    }
    else {
      if(cache.size() >= maxCacheSize)
        pop();
      DocHits d = new DocHits(doc, 1);
      cache.add(d);
    }
  }
  
  
  @Override
  public Document put(Document doc) throws SDBException {
    if(doc == null 
        || doc.label() == null 
        || doc.map().isEmpty()) 
      return doc;
    
    doc = engine.put(doc);
    cache(doc);
    return doc;
  }


  @Override
  public Document get(long block) throws SDBException {
    DocHits d = find(block);
    if(d != null) {
      if(d.document() != null)
        System.out.println("* get cached: "+ d.document().toString().substring(0, 50));
      return d.incHits().document();
    }
    
    Document doc = engine.get(block);
    if(doc != null)
      System.out.println("* get from file: "+ doc.toString().substring(0, 50));
    cache(doc);
    return doc;
  }


  @Override
  public Document remove(long block) throws SDBException {
    int idx = indexOf(block);
    if(idx >= 0) cache.remove(idx);
    return engine.remove(block);
  }


  @Override
  public void close() throws SDBException {
    cache.clear();
    engine.close();
  }


  @Override
  public Index getIndex() {
    return engine.getIndex();
  }
  
}
