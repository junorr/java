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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import us.pserver.sdb.Document;
import us.pserver.sdb.SDBException;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class FileEngine implements StorageEngine {

  public static final byte BYTE_INDEX_START = 35;
  
  public static final byte BYTE_BLOCK_START = 36;
  
  public static final byte BYTE_END = 10;
  
  public static final String EMPTY_INDEX = "@empty";
  
  
  private FileHandler hand;
  
  private Index index;
  
  private SerialEngine serial;
  
  
  public FileEngine(String file) throws SDBException {
    this(new XmlSerialEngine(), file);
  }
  
  
  public FileEngine(SerialEngine serialEngine, String file) throws SDBException {
    if(file == null || file.isEmpty())
      throw new IllegalArgumentException(
          "Invalid file: "+ file+ " - [FileEngine.init]");
    if(serialEngine == null)
      throw new IllegalArgumentException(
          "Invalid SerialEngine: "+ serialEngine+ " - [FileEngine.init]");
    try {
      hand = new FileHandler(file);
      serial = serialEngine;
      index = new Index(serialEngine);
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
        index = (Index) serial.deserialize(hand.readLineBytes());
        index.serialEngine(serial);
        hand.seekBlock(blk);
        hand.internal().setLength(hand.position());
        break;
      }
      blk--;
    }
  }
  
  
  public FileHandler getFileHandler() {
    return hand;
  }
  
  
  @Override
  public Index getIndex() {
    return index;
  }
  
  
  @Override
  public void close() throws SDBException {
    try {
      hand.moveToEnd();
      if(!hand.isBlockBoundary())
        hand.nextBlock();
      hand.writeByte(BYTE_INDEX_START)
          .writeLine(serial.serialize(index));
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
  
  protected long blockSize() throws IOException {
    long ini = hand.position();
    byte b = 0;
    long count = 0;
    while (b != BYTE_END
        && b != BYTE_BLOCK_START 
        && b != BYTE_INDEX_START 
        && !hand.isEOF()) 
    {
      b = hand.readByte();
      count++;
    }
    hand.seek(ini);
    return count;
  }
  
  
  protected long newBlock() throws IOException {
    hand.moveToEnd();
    if(!hand.isBlockBoundary())
      hand.nextBlock();
    return hand.block();
  }
  
  
  protected int[] recycledBlock() throws IOException {
    if(!index.map().containsKey(EMPTY_INDEX))
      return null;
    List<int[]> ls = index.getList(EMPTY_INDEX);
    if(ls.isEmpty()) return null;
    return ls.remove(0);
  }
  
  
  protected void write(Document doc, int[] is) throws IOException {
    if(doc == null || is == null)
      return;
    doc.block(is[0]);
    hand.seekBlock(doc.block()).writeByte(BYTE_BLOCK_START);
    int sz = is[1] * FileHandler.BLOCK_SIZE;
    byte[] xml = serial.serialize(doc);
    if(xml.length < sz) {
      hand.writeLine(xml);
    }
    else {
      long pos = hand.position();
      long blk = newBlock();
      hand.seek(pos);
      String link = "@("+ String.valueOf(blk)+ ")@";
      int div = (int) sz - link.length() - 2;
      
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      bos.write(xml, 0, div);
      bos.write(link.getBytes("UTF-8"));
      hand.writeLine(bos.toByteArray());
      
      bos.reset();
      bos.write(xml, div, (xml.length - div));
      hand.seekBlock(blk)
          .writeByte(BYTE_BLOCK_START)
          .writeLine(bos.toByteArray());
    }
  }
  
  
  @Override
  public Document put(Document doc) throws SDBException {
    if(doc == null || doc.map().isEmpty())
      return doc;
    try {
      if(doc.block() >= 0 && index.contains(
          doc.label(), doc.block())) {
        int[] is = index.get(
            doc.label(), (int)doc.block());
        hand.seekBlock(is[0]).clearBlocks(is[1]);
        write(doc, is);
      }
      else {
        int[] is = recycledBlock();
        //System.out.println("* recycled block: "+ (is != null ? is[0] : -1));
        if(is != null) {
          write(doc, is);
        }
        else {
          doc.block(newBlock());
          hand.seekBlock(doc.block())
              .writeByte(BYTE_BLOCK_START)
              .writeLine(serial.serialize(doc));
        }
      }
      index.put(doc);
      return doc;
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
  
  protected Document rmContinueLink(byte[] bts) throws IOException {
    if(bts == null || bts.length < 1)
      return null;
    
    String cont = retrieveContinueLink(bts);
    if(cont != null) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      bos.write(bts, 0, bts.length - cont.length());
      cont = cont.replace("@", "").replace("(", "").replace(")", "");
      long cb = Long.parseLong(cont);
      bos.write(rmBlock(cb));
      bts = bos.toByteArray();
    }
      
    return (Document) serial.deserialize(bts);
  }
  
  
  public int getBlockSize(byte[] bts) {
    if(bts == null || bts.length < 1) 
      return 0;
    int len = bts.length;
    int bls = len / FileHandler.BLOCK_SIZE;
    if(len % FileHandler.BLOCK_SIZE > 0) {
      bls++;
    }
    return bls;
  }
  
  
  protected byte[] rmBlock(long blk) throws IOException {
    if(blk < 0) return null;
    hand.seekBlock(blk);
    if(hand.isEOF()) return null;
    
    byte[] bts = hand.readLineBytes();
    int sz = index.remove(blk);
    if(sz <= 0)
      sz = getBlockSize(bts);
    hand.seekBlock(blk).clearBlocks(sz);
    index.put(EMPTY_INDEX, (int)blk, sz);
    if(bts[0] == BYTE_BLOCK_START) {
      byte[] other = new byte[bts.length-1];
      System.arraycopy(bts, 1, other, 0, other.length);
      bts = other;
    }
    return bts;
  }
  
  
  @Override
  public Document remove(long blk) throws SDBException {
    if(blk <= 0) return null;
    try {
      byte[] bts = rmBlock(blk);
      Document doc = rmContinueLink(bts);
      if(doc == null) return doc;
      doc.block(blk);
      index.remove(doc);
      return doc;
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
  
  protected String retrieveContinueLink(byte[] bts) {
    if(bts == null || bts.length < 1)
      return null;
    StringBuffer sb = new StringBuffer();
    for(int i = bts.length-1; i >= 0; i--) {
      char c = (char) bts[i];
      if(i == bts.length-1 && c != '@')
        return null;
      sb.append(c);
      if(i < bts.length-1 && c == '@')
        break;
    }
    return sb.reverse().toString();
  }
  
  
  protected Document readDoc(byte[] bts) throws IOException {
    if(bts == null || bts.length < 1)
      return null;
    
    String cont = retrieveContinueLink(bts);
    if(cont != null) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      bos.write(bts, 0, bts.length - cont.length());
      cont = cont.replace("@", "").replace("(", "").replace(")", "");
      long cb = Long.parseLong(cont);
      hand.seekBlock(cb).readByte();
      bos.write(hand.readLineBytes());
      bts = bos.toByteArray();
    }
      
    return (Document) serial.deserialize(bts);
  }
  
  
  protected byte[] readXml(long blk) throws IOException {
    if(blk <= 0) return null;
    hand.seekBlock(blk);
    if(hand.isEOF()) return null;
    byte b = hand.readByte();
    if(b != BYTE_BLOCK_START)
      return null;
    return hand.readLineBytes();
  }
  
  
  @Override
  public Document get(long blk) throws SDBException {
    try {
      byte[] bts = readXml(blk);
      Document doc = readDoc(bts);
      if(doc == null) return null;
      return doc.block(blk);
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
}
