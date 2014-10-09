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

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import us.pserver.sdb.Document;
import us.pserver.sdb.SDBException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/10/2014
 */
public class MemoryEngine implements StorageEngine {
  
  private File file;
  
  private Map<Long, Document> map;
  
  private long curblock;
  
  protected boolean disableSave;
  
  
  public MemoryEngine(String file) {
    if(file == null)
      throw new IllegalArgumentException(
          "Invalid journal file: "+ file+ " - [MemoryEngine.init]");
    this.file = new File(file);
    curblock = 0;
    disableSave = false;
    map = new HashMap<>();
    if(this.file.exists()) {
      load();
    }
  }
  
  
  public void load() throws SDBException {
    if(!file.exists()) 
      throw new SDBException(
          "Can not load file: "+ file+ " - [MemoryEngine.load]");
    System.out.println("* loading file: "+ file);
    try {
      XStream xst = new XStream();
      map = (Map<Long, Document>) xst.fromXML(file);
      Iterator<Long> it = map.keySet().iterator();
      while(it.hasNext()) {
        long bl = it.next();
        if(bl > curblock)
          curblock = bl;
      }
    } catch(Exception e) {
      throw new SDBException(e.getMessage() + " - [MemoryEngine.load]", e);
    }
  }
  
  
  public void save() throws SDBException {
    if(disableSave) {
      System.out.println("* save disabled");
      return;
    }
    
    System.out.println("* save Enabled");
    try {
      XStream xst = new XStream();
      FileOutputStream fos = new FileOutputStream(file);
      //printmap(map);
      xst.toXML(map, fos);
      fos.flush();
      fos.close();
    } catch(Exception e) {
      throw new SDBException(e.getMessage() + " - [MemoryEngine.save]", e);
    }
  }


  private void printdoc(Document doc) {
    if(doc == null)
      return;
    System.out.println("- Document: label="+ doc.label());
    Iterator<String> it = doc.map().keySet().iterator();
    while(it.hasNext()) {
      String k = it.next();
      System.out.println("  - "+ k+ "="+ doc.map().get(k));
    }
    System.out.println("-------------------------------");
  }
  
  
  public void printmap(Map<Long, Document> map) {
    if(map == null) return;
    System.out.println("* map.size: "+ map.size());
    Iterator<Long> it = map.keySet().iterator();
    while(it.hasNext()) {
      long l = it.next();
      System.out.println("- block="+ l+ ", doc:");
      printdoc(map.get(l));
    }
  }
  
  
  @Override
  public Document put(Document doc) throws SDBException {
    if(doc == null 
        || doc.label() == null
        || doc.map().isEmpty())
      return doc;
    
    if(doc.block() < 0) {
      doc.block(curblock++);
    }
    
    map.put(doc.block(), doc);
    save();
    return doc;
  }


  @Override
  public Document get(long block) throws SDBException {
    if(block < 0 || !map.containsKey(block)) 
      return null;
    return map.get(block).clone();
  }


  @Override
  public Document remove(long block) throws SDBException {
    if(block < 0 || !map.containsKey(block)) 
      return null;
    
    Document doc = map.get(block);
    map.remove(block);
    save();
    return doc;
  }


  @Override
  public void close() throws SDBException {
    disableSave = false;
    save();
    map.clear();
  }
  
  
  private Index createIndex() {
    Index idx = new Index();
    if(map.isEmpty()) return idx;
    Iterator<Long> it = map.keySet().iterator();
    while(it.hasNext()) {
      long l = it.next();
      idx.put(map.get(l).block(l));
    }
    return idx;
  }


  @Override
  public Index getIndex() {
    return createIndex();
  }

}
