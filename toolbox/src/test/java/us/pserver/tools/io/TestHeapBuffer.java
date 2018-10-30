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

package us.pserver.tools.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.tools.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/10/2018
 */
public class TestHeapBuffer {
  
  /*                                     1...5...10...15...20...25...30...35...40...45...50...55...60...65...70...75...80...85...90...95..100..105..110..115..120..125..130..135..140..145..150..155..160..165.....173 */
  private static final byte[] content = "Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto \n com esta biblioteca; se nao, acesse \n http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html".getBytes(StandardCharsets.UTF_8);
  
  private static final Buffer buffer = Buffer.heapFactory().create(content);
  
  private static void resetTestBuffer() {
    buffer.clear();
    buffer.fillBuffer(content);
    buffer.readMark();
    buffer.writeMark();
  }

  @Test
  public void testFillBufferWithByteArray() {
    HeapBuffer buffer = new HeapBuffer(content.length);
    buffer.writeMark();
    buffer.fillBuffer(content);
    Logger.debug("%s", buffer);
    Assertions.assertEquals(content.length, buffer.readLength());
    Assertions.assertEquals(0, buffer.writeLength());
    Assertions.assertEquals(true, buffer.isReadable());
    Assertions.assertEquals(false, buffer.isWritable());
    buffer.writeReset();
    Assertions.assertEquals(content.length, buffer.writeLength());
    Assertions.assertEquals(true, buffer.isWritable());
    Assertions.assertEquals(false, buffer.isReadable());
  }
  
  @Test
  public void testFillBufferWithInputStream() throws IOException {
    HeapBuffer buffer = new HeapBuffer(content.length);
    buffer.writeMark();
    ByteArrayInputStream bis = new ByteArrayInputStream(content);
    buffer.fillBuffer(bis);
    Logger.debug("%s", buffer);
    Assertions.assertEquals(content.length, buffer.readLength());
    Assertions.assertEquals(0, buffer.writeLength());
    Assertions.assertEquals(true, buffer.isReadable());
    Assertions.assertEquals(false, buffer.isWritable());
    buffer.writeReset();
    Assertions.assertEquals(content.length, buffer.writeLength());
    Assertions.assertEquals(true, buffer.isWritable());
    Assertions.assertEquals(false, buffer.isReadable());
  }
  
  @Test
  public void testFillBufferWithByteBuffer() throws IOException {
    HeapBuffer buffer = new HeapBuffer(content.length);
    buffer.writeMark();
    buffer.fillBuffer(ByteBuffer.wrap(content));
    Logger.debug("%s", buffer);
    Assertions.assertEquals(content.length, buffer.readLength());
    Assertions.assertEquals(0, buffer.writeLength());
    Assertions.assertEquals(true, buffer.isReadable());
    Assertions.assertEquals(false, buffer.isWritable());
    buffer.writeReset();
    Assertions.assertEquals(content.length, buffer.writeLength());
    Assertions.assertEquals(true, buffer.isWritable());
    Assertions.assertEquals(false, buffer.isReadable());
  }
  
  @Test
  public void testFillBufferWithBuffer() throws IOException {
    HeapBuffer buffer = new HeapBuffer(content.length);
    buffer.writeMark();
    buffer.fillBuffer(TestHeapBuffer.buffer.clone());
    Logger.debug("%s", buffer);
    Assertions.assertEquals(content.length, buffer.readLength());
    Assertions.assertEquals(0, buffer.writeLength());
    Assertions.assertEquals(true, buffer.isReadable());
    Assertions.assertEquals(false, buffer.isWritable());
    buffer.writeReset();
    Assertions.assertEquals(content.length, buffer.writeLength());
    Assertions.assertEquals(true, buffer.isWritable());
    Assertions.assertEquals(false, buffer.isReadable());
  }
  
  @Test
  public void testWriteToByteArray() {
    Logger.debug("%s", buffer);
    byte[] buf = new byte[buffer.readLength()];
    buffer.writeTo(buf);
    Logger.debug("%s", buffer);
    Assertions.assertEquals(0, buffer.readLength());
    Assertions.assertEquals(0, buffer.writeLength());
    Assertions.assertEquals(false, buffer.isReadable());
    Assertions.assertEquals(false, buffer.isWritable());
    resetTestBuffer();
  }
  
  @Test
  public void testWriteToOutputStream() throws IOException {
    Logger.debug("%s", buffer);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    buffer.writeTo(bos);
    Logger.debug("%s", buffer);
    Assertions.assertEquals(0, buffer.readLength());
    Assertions.assertEquals(0, buffer.writeLength());
    Assertions.assertEquals(false, buffer.isReadable());
    Assertions.assertEquals(false, buffer.isWritable());
    Assertions.assertArrayEquals(content, bos.toByteArray());
    resetTestBuffer();
  }
  
  @Test
  public void testWriteToByteBuffer() throws IOException {
    Logger.debug("%s", buffer);
    ByteBuffer buf = ByteBuffer.allocate(content.length);
    buffer.writeTo(buf);
    Logger.debug("%s", buffer);
    Assertions.assertEquals(0, buffer.readLength());
    Assertions.assertEquals(0, buffer.writeLength());
    Assertions.assertEquals(false, buffer.isReadable());
    Assertions.assertEquals(false, buffer.isWritable());
    buf.flip();
    Assertions.assertEquals(new String(content, StandardCharsets.UTF_8), StandardCharsets.UTF_8.decode(buf).toString());
    resetTestBuffer();
  }
  
  @Test
  public void testWriteToBuffer() throws IOException {
    Logger.debug("%s", buffer);
    Buffer buf = new HeapBuffer(content.length);
    buffer.writeTo(buf);
    Logger.debug("%s", buffer);
    Assertions.assertEquals(0, buffer.readLength());
    Assertions.assertEquals(0, buffer.writeLength());
    Assertions.assertEquals(false, buffer.isReadable());
    Assertions.assertEquals(false, buffer.isWritable());
    Logger.debug("%s", buf);
    Assertions.assertEquals(content.length, buf.readLength());
    Assertions.assertEquals(0, buf.writeLength());
    Assertions.assertEquals(true, buf.isReadable());
    Assertions.assertEquals(false, buf.isWritable());
    resetTestBuffer();
  }
  
  @Test
  public void testClear() {
    Logger.debug("%s", buffer);
    buffer.clear();
    Assertions.assertEquals(0, buffer.readLength());
    Assertions.assertEquals(content.length, buffer.writeLength());
    Assertions.assertEquals(false, buffer.isReadable());
    Assertions.assertEquals(true, buffer.isWritable());
    resetTestBuffer();
  }
  
  @Test
  public void testClone() {
    Buffer buf = buffer.clone();
    Logger.debug("%s", buffer);
    Assertions.assertEquals(content.length, buf.readLength());
    Assertions.assertEquals(0, buf.writeLength());
    Assertions.assertEquals(true, buf.isReadable());
    Assertions.assertEquals(false, buf.isWritable());
    resetTestBuffer();
  }
  
  @Test
  public void testCloneShared() {
    Buffer buf = buffer.cloneShared();
    Logger.debug("%s", buf);
    Assertions.assertEquals(content.length, buf.readLength());
    Assertions.assertEquals(0, buf.writeLength());
    Assertions.assertEquals(true, buf.isReadable());
    Assertions.assertEquals(false, buf.isWritable());
    
    buf.clear();
    byte[] bs = "Eu  devo  ter recebido um leitao da Licença Publica Geral Menor do GNU junto \n com esta biblioteca; se nao, acesse \n http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html".getBytes(StandardCharsets.UTF_8);
    buf.fillBuffer(bs);
    Logger.debug("%s", buffer.getContentAsString());
    Assertions.assertArrayEquals(bs, buffer.toByteArray());
    resetTestBuffer();
  }
  
  @Test
  public void testFindByteArray() {
    byte[] search = "Menor".getBytes(StandardCharsets.UTF_8);
    int idx = buffer.find(search);
    Logger.debug("%s", buffer);
    Logger.debug("buffer.find('Menor'): %d", idx);
    Assertions.assertEquals(59, idx);
    Assertions.assertEquals(true, buffer.isReadable());
    Assertions.assertEquals(false, buffer.isWritable());
    resetTestBuffer();
  }
  
  @Test
  public void testFindBuffer() {
    byte[] bs = "Menor".getBytes(StandardCharsets.UTF_8);
    Buffer search = new HeapBuffer(bs.length);
    search.fillBuffer(bs);
    int idx = buffer.find(search);
    Logger.debug("%s", buffer);
    Logger.debug("buffer.find('Menor'): %d", idx);
    Assertions.assertEquals(59, idx);
    Assertions.assertEquals(true, buffer.isReadable());
    Assertions.assertEquals(false, buffer.isWritable());
    resetTestBuffer();
  }
  
  @Test
  public void testPutGetByte() {
    HeapBuffer buf = new HeapBuffer(1);
    buf.writeMark();
    byte b = '\r';
    buf.put(b);
    Logger.debug("%s", buf);
    Assertions.assertEquals(1, buf.readLength());
    Assertions.assertEquals(0, buf.writeLength());
    Assertions.assertEquals(true, buf.isReadable());
    Assertions.assertEquals(false, buf.isWritable());
    
    Assertions.assertEquals(b, buf.get());
    Assertions.assertEquals(0, buf.readLength());
    Assertions.assertEquals(false, buf.isReadable());
    resetTestBuffer();
  }
  
  @Test
  public void testPutGetShort() {
    HeapBuffer buf = new HeapBuffer(Short.BYTES);
    buf.writeMark();
    short s = 5;
    buf.put(s);
    Logger.debug("%s", buf);
    Assertions.assertEquals(Short.BYTES, buf.readLength());
    Assertions.assertEquals(0, buf.writeLength());
    Assertions.assertEquals(true, buf.isReadable());
    Assertions.assertEquals(false, buf.isWritable());
    
    short get = buf.getShort();
    Logger.debug("getShort=%d", get);
    Assertions.assertEquals(s, get);
    Assertions.assertEquals(0, buf.readLength());
    Assertions.assertEquals(false, buf.isReadable());
    resetTestBuffer();
  }
  
  @Test
  public void testPutGetInt() {
    HeapBuffer buf = new HeapBuffer(Integer.BYTES);
    buf.writeMark();
    int i = 5;
    buf.put(i);
    Logger.debug("%s", buf);
    Assertions.assertEquals(Integer.BYTES, buf.readLength());
    Assertions.assertEquals(0, buf.writeLength());
    Assertions.assertEquals(true, buf.isReadable());
    Assertions.assertEquals(false, buf.isWritable());
    
    int get = buf.getInt();
    Logger.debug("getInt=%d", get);
    Assertions.assertEquals(i, get);
    Assertions.assertEquals(0, buf.readLength());
    Assertions.assertEquals(false, buf.isReadable());
    resetTestBuffer();
  }
  
  @Test
  public void testPutGetFloat() {
    HeapBuffer buf = new HeapBuffer(Integer.BYTES);
    buf.writeMark();
    float f = 5.005f;
    buf.put(f);
    Logger.debug("%s", buf);
    Assertions.assertEquals(Integer.BYTES, buf.readLength());
    Assertions.assertEquals(0, buf.writeLength());
    Assertions.assertEquals(true, buf.isReadable());
    Assertions.assertEquals(false, buf.isWritable());
    
    float get = buf.getFloat();
    Logger.debug("getFloat=%f", get);
    Assertions.assertEquals(f, get);
    Assertions.assertEquals(0, buf.readLength());
    Assertions.assertEquals(false, buf.isReadable());
    resetTestBuffer();
  }
  
  @Test
  public void testPutGetLong() {
    HeapBuffer buf = new HeapBuffer(Long.BYTES);
    buf.writeMark();
    long l = 5L;
    buf.put(l);
    Logger.debug("%s", buf);
    Assertions.assertEquals(Long.BYTES, buf.readLength());
    Assertions.assertEquals(0, buf.writeLength());
    Assertions.assertEquals(true, buf.isReadable());
    Assertions.assertEquals(false, buf.isWritable());
    
    long get = buf.getLong();
    Logger.debug("getLong=%d", get);
    Assertions.assertEquals(l, get);
    Assertions.assertEquals(0, buf.readLength());
    Assertions.assertEquals(false, buf.isReadable());
    resetTestBuffer();
  }
  
  @Test
  public void testPutGetDouble() {
    HeapBuffer buf = new HeapBuffer(Long.BYTES);
    buf.writeMark();
    double d = 5.005;
    buf.put(d);
    Logger.debug("%s", buf);
    Assertions.assertEquals(Long.BYTES, buf.readLength());
    Assertions.assertEquals(0, buf.writeLength());
    Assertions.assertEquals(true, buf.isReadable());
    Assertions.assertEquals(false, buf.isWritable());
    
    double get = buf.getDouble();
    Logger.debug("getDouble=%f", get);
    Assertions.assertEquals(d, get);
    Assertions.assertEquals(0, buf.readLength());
    Assertions.assertEquals(false, buf.isReadable());
    resetTestBuffer();
  }
  
  @Test 
  public void testToDirectByteBuffer() {
    ByteBuffer direct = buffer.toByteBuffer(ByteBuffer::allocateDirect);
    Logger.debug("%s", direct);
    Assertions.assertEquals(true, direct.isDirect());
    Assertions.assertEquals(content.length, direct.remaining());
    Assertions.assertEquals(buffer.getContentAsString(), StandardCharsets.UTF_8.decode(direct).toString());
    resetTestBuffer();
  }
  
  @Test 
  public void testToByteBuffer() {
    ByteBuffer heap = buffer.toByteBuffer();
    Logger.debug("%s", heap);
    Assertions.assertEquals(false, heap.isDirect());
    Assertions.assertArrayEquals(content, heap.array());
    Assertions.assertEquals(content.length, heap.remaining());
    Assertions.assertEquals(buffer.getContentAsString(), StandardCharsets.UTF_8.decode(heap).toString());
    resetTestBuffer();
  }
  
  @Test 
  public void testToByteArray() {
    byte[] heap = buffer.toByteArray();
    Assertions.assertEquals(content.length, heap.length);
    Assertions.assertArrayEquals(content, heap);
    Assertions.assertEquals(buffer.getContentAsString(), new String(heap, StandardCharsets.UTF_8));
    resetTestBuffer();
  }
  
}
