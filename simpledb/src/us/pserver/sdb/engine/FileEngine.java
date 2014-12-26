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
    this(new JsonSerialEngine(), file);
  }
  
  
  public FileEngine(SerialEngine serialEngine, String file) throws SDBException {
    if(file == null || file.isEmpty())
      throw new IllegalArgumentException(
          "Invalid file: "+ file+ " - FileEngine.init");
    if(serialEngine == null)
      throw new IllegalArgumentException(
          "Invalid SerialEngine: "+ serialEngine+ " - FileEngine.init");
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
        index = (Index) serial.deserialize(readBytes(blk));
        index.setSerialEngine(serial);
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
          .writeLine(serial.serialize(index))
          .writeByte(BYTE_INDEX_START);
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
    return ls.get(0);
  }
  
  
  protected void useRecycled(int[] rblock) throws IOException {
    if(rblock == null || rblock[0] < 0)
      return;
    index.remove(rblock[0]);
  }
  
  
  private void write(Document doc) throws IOException {
    if(doc == null) return;
    if(doc.block() >= 0 
        && index.contains(doc.label(), doc.block())) {
      rmBlock(doc.block());
    }
    
    int[] rblock = recycledBlock();
    byte[] bytes = serial.serialize(doc);
    
    if(rblock != null 
        && bytes.length <= 
        (rblock[1] * FileHandler.BLOCK_SIZE)) {
      useRecycled(rblock);
    }
    else {
      rblock = new int[] { (int)newBlock() };
    }
    
    doc.block(rblock[0]);
    hand.seekBlock(rblock[0]);
    hand.writeByte(BYTE_BLOCK_START)
        .writeLine(bytes)
        .writeByte(BYTE_BLOCK_START);
  }
  
  
  @Override
  public Document put(Document doc) throws SDBException {
    if(doc == null || doc.map().isEmpty())
      return doc;
    try {
      write(doc);
      index.put(doc);
      return doc;
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
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
      Document doc = (Document) serial.deserialize(bts);
      if(doc == null) return doc;
      doc.block(blk);
      index.remove(doc);
      return doc;
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
  
  protected byte[] readBytes2(long blk) throws IOException {
    if(blk <= 0) return null;
    hand.seekBlock(blk);
    if(hand.isEOF()) return null;
    byte b = hand.readByte();
    if(b != BYTE_BLOCK_START)
      return null;
    return hand.readLineBytes();
  }
  
  
  protected byte[] readBytes(long blk) throws IOException {
    if(blk <= 0) return null;
    hand.seekBlock(blk);
    if(hand.isEOF()) return null;
    byte a = 0;
    byte b = hand.readByte();
    if(b != BYTE_BLOCK_START)
      return null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while(!hand.isEOF()) {
      a = hand.readByte();
      b = hand.readByte();
      if(a == BYTE_END && 
          (b == BYTE_BLOCK_START
          || b == BYTE_INDEX_START))
        break;
      bos.write(a);
      bos.write(b);
    }
    return bos.toByteArray();
  }
  
  
  @Override
  public Document get(long blk) throws SDBException {
    try {
      byte[] bts = readBytes(blk);
      Document doc = (Document) serial.deserialize(bts);
      if(doc == null) return null;
      return doc.block(blk);
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
}
