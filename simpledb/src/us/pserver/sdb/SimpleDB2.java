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

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static us.pserver.sdb.FileHandler.BLOCK_SIZE;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class SimpleDB2 {

  public static final byte BYTE_INDEX_START = 35;
  
  public static final byte BYTE_BLOCK_START = 36;
  
  public static final byte BYTE_END = 10;
  
  public static final String EMPTY_INDEX = "@empty";
  
  
  private FileHandler hand;
  
  private IndexLabel index;
  
  
  public SimpleDB2(String file) throws SDBException {
    if(file == null || file.isEmpty())
      throw new IllegalArgumentException("Invalid file: "+ file);
    try {
      hand = new FileHandler(file);
      index = new IndexLabel();
      init();
    } catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
  
  private void init() throws IOException {
    hand.moveToEnd();
    long blk = hand.block();
    while(blk >= 0) {
      hand.seekBlock(blk);
      if(hand.isEOF()) continue;
      byte b = hand.readByte();
      if(b == BYTE_INDEX_START) {
        String str = hand.readLine();
        index = IndexLabel.fromXml(str);
        System.out.println("* indexes: "+ index.map().size());
        hand.seekBlock(blk);
        hand.internal().setLength(hand.position());
        break;
      }
      blk--;
    }
  }
  
  
  public FileHandler handler() {
    return hand;
  }
  
  
  private int blocksIndex() {
    int s = index.toXml().length();
    return (s / BLOCK_SIZE + (s % BLOCK_SIZE > 0 ? 1 : 0));
  }
  
  
  public void close() throws SDBException {
    try {
      hand.moveToEnd();
      if(!hand.isBlockBoundary())
        hand.nextBlock();
      hand.writeByte(BYTE_INDEX_START)
          .writeLine(index.toXml());
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
  
  protected Map<String, Document> findNested(Document doc) {
    Map<String, Document> map = Collections.EMPTY_MAP;
    if(doc == null) return map;
    Iterator<String> it = doc.map().keySet().iterator();
    map = new LinkedHashMap<>();
    while(it.hasNext()) {
      String key = it.next();
      Object o = doc.get(key);
      if(o != null && Document.class
          .isAssignableFrom(o.getClass())) {
        Document d = doc.getAs(key);
        doc.map().remove(key);
        map.put(key, d);
      }
    }
    return map;
  }
  
  
  protected Map<String, Long> findLinks(Document doc) {
    Map<String, Long> map = Collections.EMPTY_MAP;
    if(doc == null) return map;
    Iterator<String> it = doc.map().keySet().iterator();
    map = new LinkedHashMap<>();
    while(it.hasNext()) {
      String key = it.next();
      if(key.startsWith("@_")) {
        Object o = doc.get(key);
        if(o != null && Long.class
            .isAssignableFrom(o.getClass())) {
          Long l = doc.getAs(key);
          doc.map().remove(key);
          map.put(key.substring(2), l);
        }
      }
    }
    return map;
  }
  
  
  private long blockSize() throws IOException {
    long ini = hand.position();
    byte b = 0;
    long count = 0;
    while (b != BYTE_BLOCK_START 
        && b != BYTE_INDEX_START 
        && !hand.isEOF()) 
    {
      b = hand.readByte();
      count++;
    }
    hand.seek(ini);
    return count;
  }
  
  
  private long newBlock() throws IOException {
    hand.moveToEnd();
    if(!hand.isBlockBoundary())
      hand.nextBlock();
    return hand.block();
  }
  
  
  private long recycledBlock() throws IOException {
    Index id = null;
    if(!index.map().containsKey(EMPTY_INDEX))
      return -1;
    List<Long> ls = index.getList(EMPTY_INDEX);
    if(ls == null || ls.isEmpty())
      return -1;
    return ls.remove(0);
  }
  
  
  private void write(Document doc) throws IOException {
    if(doc == null || doc.block() < 0)
      return;
    hand.seekBlock(doc.block()).writeByte(BYTE_BLOCK_START);
    long sz = blockSize();
    String xml = doc.toXml();
    if(xml.length() < sz) {
      hand.writeLine(xml);
    }
    else {
      long pos = hand.position();
      long blk = newBlock();
      hand.seek(pos);
      String link = "@("+ String.valueOf(blk)+ ")@";
      int div = (int) sz - (link.length() + 1) - 1;
      hand.write(xml.substring(0, div));
      hand.writeLine(link);
      hand.seekBlock(blk)
          .writeByte(BYTE_BLOCK_START)
          .writeLine(xml.substring(div));
    }
  }
  
  
  public Document put(Document doc) throws SDBException {
    if(doc == null) return doc;
    try {
      Map<String, Document> nested = findNested(doc);
      if(!nested.isEmpty()) {
        Iterator<String> it = nested.keySet().iterator();
        while(it.hasNext()) {
          String key = it.next();
          Document d = put(nested.get(key));
          if(d == null || d.map().isEmpty())
            continue;
          doc.put("@_"+key, d.block());
        }
      }
      if(doc.block() >= 0) {
        write(doc);
      }
      else {
        long blk = recycledBlock();
        System.out.println("* recycled block: "+ blk);
        if(blk >= 0) {
          write(doc.block(blk));
        }
        else {
          doc.block(newBlock());
          hand.seekBlock(doc.block())
              .writeByte(BYTE_BLOCK_START)
              .writeLine(doc.toXml());
        }
      }
      index.put(doc);
      return doc;
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
  
  public boolean remove(Document doc) throws SDBException {
    if(doc == null || doc.label() == null
        || doc.map().isEmpty()) 
      return false;
    
    if(doc.block() < 0) {
      Document d = getOne(doc);
      doc.block(d.block());
    }
    if(doc.block() < 0) return false;
    
    try {
      hand.seekBlock(doc.block());
      hand.clearLine();
      for(Index i : ids) {
        if(i.remove(doc)) break;
      }
      Index id = getIndex(EMPTY_INDEX);
      if(id == null) {
        id = new Index(EMPTY_INDEX);
        ids.add(id);
      }
      id.put(new Document(EMPTY_INDEX)
          .put(EMPTY_INDEX, doc.block())
          .block(doc.block()));
      return true;
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
  
  public Document putOrUpdate(Document doc) throws SDBException {
    if(doc == null) return doc;
    if(doc.block() <= 0) {
      Document d = getOne(doc);
      if(d != null)
        doc.block(d.block());
    }
    return this.put(doc);
  }
  
  
  private String retrieveContinueLink(String xml) {
    if(xml == null 
        || xml.isEmpty()
        || !xml.endsWith(")@"))
      return null;
    StringBuffer sb = new StringBuffer();
    for(int i = xml.length()-1; i >= 0; i--) {
      char c = xml.charAt(i);
      sb.append(c);
      if(i < xml.length()-1 && c == '@')
        break;
    }
    return sb.reverse().toString();
  }
  
  
  public Document get(long blk) throws SDBException {
    if(blk <= 0) return null;
    System.out.println("* block > 0");
    try {
      hand.seekBlock(blk);
      if(hand.isEOF()) return null;
      System.out.println("* block is not eof");
      byte b = hand.readByte();
      System.out.println("* byte readed: "+ b);
      if(b != BYTE_BLOCK_START)
        return null;
      System.out.println("* reading doc");
      
      String xml = hand.readLine();
      String cont = retrieveContinueLink(xml);
      xml = xml.substring(0, xml.length()-cont.length());
      if(cont != null) {
        cont = cont.replace("@", "").replace("(", "").replace(")", "");
        long cb = Long.parseLong(cont);
        hand.seekBlock(cb).readByte();
        xml = xml + hand.readLine();
      }
      
      Document doc = Document.fromXml(hand.readLine());
      System.out.println("* doc: "+ doc);
      Map<String, Long> links = findLinks(doc);
      if(!links.isEmpty()) {
        Iterator<String> it = links.keySet().iterator();
        while(it.hasNext()) {
          String key = it.next();
          Document d = get(links.get(key));
          if(d == null || d.map().isEmpty())
            continue;
          doc.put(key, d);
        }
      }
      return doc.block(blk);
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
  
  public Document getOne(Document doc) throws SDBException {
    if(doc != null && doc.label() != null) {
      Index i = getIndex(doc.label());
      System.out.println("* found index: "+ (i != null ? i.label(): "null"));
      if(i == null) return null;
      long blk = i.findOne(doc);
      System.out.println("* found on: "+ blk);
      return get(blk);
    }
    return null;
  }
  
  
  public Document getOne(String label, String key, String value) throws SDBException {
    return getOne(new Document(label).put(key, value));
  }
  
  
  public List<Document> get(String label, String key, String value) throws SDBException {
    return get(new Document(label).put(key, value));
  }
  
  
  public List<Document> get(Document doc) throws SDBException {
    List<Document> list = Collections.EMPTY_LIST;
    if(doc == null || doc.label() == null
        || doc.label().isEmpty()) 
      return list;
    
    List<Long> blks = Collections.EMPTY_LIST;
    for(int i = 0; i < ids.size(); i++) {
      Index id = ids.get(i);
      if(id.label().equalsIgnoreCase(doc.label())) {
        blks = id.find(doc);
        break;
      }
    }
    if(blks.isEmpty()) return list;
    list = new LinkedList<>();
    for(int i = 0; i < blks.size(); i++) {
      Document d = get(blks.get(i));
      if(d != null)
        list.add(d);
    }
    return list;
  }
  
  
  public List<Document> get(String label) throws SDBException {
    List<Document> list = Collections.EMPTY_LIST;
    if(label == null || label.isEmpty()) 
      return list;
    
    List<Long> blks = Collections.EMPTY_LIST;
    for(int i = 0; i < ids.size(); i++) {
      Index id = ids.get(i);
      if(id.label().equalsIgnoreCase(label)) {
        blks = id.getAllBlocks();
        break;
      }
    }
    if(blks.isEmpty()) return list;
    list = new LinkedList<>();
    for(int i = 0; i < blks.size(); i++) {
      Document d = get(blks.get(i));
      if(d != null)
        list.add(d);
    }
    return list;
  }
  
  
  public Document getOne(String label, Query q) throws SDBException {
    if(label == null || label.isEmpty() 
        || q == null) return null;
    Index i = getIndex(label);
    if(i == null) return null;
    long blk = i.findOne(q);
    if(blk == -1) return null;
    return get(blk);
  }
  
  
  public List<Document> get(String label, Query q) throws SDBException {
    List<Document> list = Collections.EMPTY_LIST;
    if(label == null || label.isEmpty() 
        || q == null) return list;
    Index i = getIndex(label);
    if(i == null) return list;
    List<Long> bls = i.find(q);
    if(bls.isEmpty()) return list;
    list = new LinkedList<>();
    for(long blk : bls) {
      if(blk <= 0) continue;
      Document d = get(blk);
      if(d != null)
        list.add(d);
    }
    return list;
  }
  
}
