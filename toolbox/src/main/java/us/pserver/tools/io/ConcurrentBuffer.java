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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.IntFunction;
import us.pserver.tools.fn.ThrowableSupplier;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/10/2018
 */
public class ConcurrentBuffer implements Buffer {
  
  private final ReentrantReadWriteLock lock;
  
  private final Buffer buffer;
  
  
  public ConcurrentBuffer(Buffer buffer) {
    this.buffer = Objects.requireNonNull(buffer, "Bad null Buffer");
    this.lock = new ReentrantReadWriteLock();
  }
  
  
  private <T> T readLock(ThrowableSupplier<T> sup) {
    if(sup == null) return null;
    lock.readLock().lock();
    try {
      return sup.supply();
    }
    catch(Exception e) {
      Throwable th = e;
      while(e.getCause() != null) {
        th = e.getCause();
      }
      throw new RuntimeException(th.toString(), th);
    }
    finally {
      lock.readLock().unlock();
    }
  }
  
  
  private <T> T writeLock(ThrowableSupplier<T> sup) {
    if(sup == null) return null;
    lock.writeLock().lock();
    try {
      return sup.supply();
    }
    catch(Exception e) {
      Throwable th = e;
      while(e.getCause() != null) {
        th = e.getCause();
      }
      throw new RuntimeException(th.toString(), th);
    }
    finally {
      lock.writeLock().unlock();
    }
  }
  
  
  @Override
  public int capacity() {
    return readLock(buffer::capacity);
  }
  
  
  @Override
  public int readLength() {
    return readLock(buffer::readLength);
  }
  
  
  @Override
  public int writeLength() {
    return readLock(buffer::writeLength);
  }
  
  
  @Override
  public boolean isReadable() {
    return readLock(buffer::isReadable);
  }
  
  
  @Override
  public boolean isWritable() {
    return readLock(buffer::isWritable);
  }
  
  
  @Override
  public Buffer clear() {
    writeLock(buffer::clear);
    return this;
  }
  
  
  @Override
  public Buffer readMark() {
    readLock(buffer::readMark);
    return this;
  }
  
  
  @Override
  public Buffer readReset() {
    readLock(buffer::readReset);
    return this;
  }
  
  
  @Override
  public Buffer writeMark() {
    writeLock(buffer::writeMark);
    return this;
  }
  
  
  @Override
  public Buffer writeReset() {
    writeLock(buffer::writeReset);
    return this;
  }
  
  
  @Override
  public Buffer clone() {
    return new ConcurrentBuffer(writeLock(buffer::clone));
  }
  
  
  @Override
  public Buffer cloneShared() {
    return new ConcurrentBuffer(writeLock(buffer::cloneShared));
  }
  
  
  @Override
  public int find(byte[] cont) {
    return readLock(() -> buffer.find(cont));
  }
  
  
  @Override
  public int find(byte[] cont, int ofs, int len) {
    return readLock(() -> buffer.find(cont, ofs, len));
  }
  
  
  @Override
  public int find(Buffer buf) {
    return readLock(() -> buffer.find(buf));
  }
  
  
  @Override
  public int fillBuffer(InputStream in) throws IOException {
    return writeLock(() -> buffer.fillBuffer(in));
  }
  
  
  @Override
  public int fillBuffer(InputStream in, int length) throws IOException {
    return writeLock(() -> buffer.fillBuffer(in, length));
  }
  
  
  @Override
  public int fillBuffer(ByteBuffer buf) {
    return writeLock(() -> buffer.fillBuffer(buf));
  }
  
  
  @Override
  public int fillBuffer(ByteBuffer buf, int length) {
    return writeLock(() -> buffer.fillBuffer(buf, length));
  }
  
  
  @Override
  public int fillBuffer(Buffer buf) {
    return writeLock(() -> buffer.fillBuffer(buf));
  }
  
  
  @Override
  public int fillBuffer(Buffer buf, int length) {
    return writeLock(() -> buffer.fillBuffer(buf, length));
  }
  
  
  @Override
  public int fillBuffer(byte[] src) {
    return writeLock(() -> buffer.fillBuffer(src));
  }
  
  
  @Override
  public int fillBuffer(byte[] src, int ofs, int len) {
    return writeLock(() -> buffer.fillBuffer(src, ofs, len));
  }
  
  
  @Override
  public int writeTo(OutputStream out) throws IOException {
    return readLock(() -> buffer.writeTo(out));
  }
  
  
  @Override
  public int writeTo(OutputStream out, int length) throws IOException {
    return readLock(() -> buffer.writeTo(out, length));
  }
  
  
  @Override
  public int writeTo(ByteBuffer out) {
    return readLock(() -> buffer.writeTo(out));
  }
  
  
  @Override
  public int writeTo(ByteBuffer out, int length) {
    return readLock(() -> buffer.writeTo(out, length));
  }
  
  
  @Override
  public int writeTo(Buffer out) {
    return readLock(() -> buffer.writeTo(out));
  }
  
  
  @Override
  public int writeTo(Buffer out, int length) {
    return readLock(() -> buffer.writeTo(out, length));
  }
  
  
  @Override
  public int writeTo(byte[] out) {
    return readLock(() -> buffer.writeTo(out));
  }
  
  
  @Override
  public int writeTo(byte[] out, int ofs, int len) {
    return readLock(() -> buffer.writeTo(out, ofs, len));
  }
  
  
  @Override
  public Buffer put(byte b) {
    writeLock(() -> buffer.put(b));
    return this;
  }
  
  
  @Override
  public Buffer put(short number) {
    writeLock(() -> buffer.put(number));
    return this;
  }
  
  
  @Override
  public Buffer put(int number) {
    writeLock(() -> buffer.put(number));
    return this;
  }
  
  
  @Override
  public Buffer put(long number) {
    writeLock(() -> buffer.put(number));
    return this;
  }
  
  
  @Override
  public Buffer put(float number) {
    writeLock(() -> buffer.put(number));
    return this;
  }
  
  
  @Override
  public Buffer put(double number) {
    writeLock(() -> buffer.put(number));
    return this;
  }
  
  
  @Override
  public byte get() {
    return readLock(buffer::get);
  }
  
  
  @Override
  public int getInt() {
    return readLock(buffer::getInt);
  }
  
  
  @Override
  public short getShort() {
    return readLock(buffer::getShort);
  }
  
  
  @Override
  public long getLong() {
    return readLock(buffer::getLong);
  }
  
  
  @Override
  public float getFloat() {
    return readLock(buffer::getFloat);
  }
  
  
  @Override
  public double getDouble() {
    return readLock(buffer::getDouble);
  }
  
  
  @Override
  public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> allocPolicy) {
    return readLock(() -> buffer.toByteBuffer(allocPolicy));
  }
  
  
  @Override
  public ByteBuffer toByteBuffer() {
    return readLock(() -> buffer.toByteBuffer());
  }
  
  
  @Override
  public byte[] toByteArray() {
    return readLock(() -> buffer.toByteArray());
  }
  
  
  @Override
  public String getContentAsString(Charset cs) {
    return readLock(() -> buffer.getContentAsString(cs));
  }
  
  
  @Override
  public String toString() {
    String str = buffer.toString();
    return "ConcurrentBuffer" + str.substring(str.indexOf("["));
  }

}
