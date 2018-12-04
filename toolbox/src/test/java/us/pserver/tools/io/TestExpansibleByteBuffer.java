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

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/12/2018
 */
public class TestExpansibleByteBuffer {
  
  @Test
  public void testPutFlipGetString() {
    System.out.printf("====== testPutFlipGetString ======%n");
    try {
      //            1...5...10...15...20...25...30...35...40...45...50
      String str = "Hello World! 123 Hello World! 456 Hello World! 789";
      byte[] bs = str.getBytes(StandardCharsets.UTF_8);
      ExpansibleByteBuffer buffer = new ExpansibleByteBuffer(ExpansibleByteBuffer.AllocPolicy.HEAP_ALLOC_POLICY, 12);
      System.out.printf("* buffer.putInt( %d )...%n", bs.length);
      buffer.putInt(bs.length);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(4, buffer.position());
      Assertions.assertEquals(8, buffer.remaining());
      Assertions.assertEquals(12, buffer.limit());
      Assertions.assertEquals(12, buffer.capacity());
      buffer.put(bs, 0, bs.length);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(54, buffer.position());
      Assertions.assertEquals(18, buffer.remaining());
      Assertions.assertEquals(72, buffer.limit());
      Assertions.assertEquals(72, buffer.capacity());
      System.out.printf("* buffer.flip()...%n");
      buffer.flip();
      System.out.printf("%s%n", buffer);
      int i = buffer.getInt();
      System.out.printf("* buffer.getInt(): %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(50, i);
      Assertions.assertEquals(4, buffer.position());
      Assertions.assertEquals(50, buffer.remaining());
      Assertions.assertEquals(54, buffer.limit());
      Assertions.assertEquals(72, buffer.capacity());
      bs = new byte[i];
      buffer.get(bs, 0, i);
      str = new String(bs, StandardCharsets.UTF_8);
      System.out.printf("* buffer.get(bs, 0, %d): %s%n", i, str);
      Assertions.assertEquals(54, buffer.position());
      Assertions.assertEquals(0, buffer.remaining());
      Assertions.assertEquals(54, buffer.limit());
      Assertions.assertEquals(72, buffer.capacity());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Test
  public void testPutFlipGetInts() {
    System.out.printf("====== testPutFlipGetInts ======%n");
    try {
      ExpansibleByteBuffer buffer = new ExpansibleByteBuffer(ExpansibleByteBuffer.AllocPolicy.HEAP_ALLOC_POLICY, 10);
      System.out.printf("%s%n", buffer);
      int i = 1001;
      System.out.printf("* buffer.putInt( %d )...%n", i);
      buffer.putInt(i);
      i = 2002;
      System.out.printf("* buffer.putInt( %d )...%n", i);
      buffer.putInt(i);
      i = 3003;
      System.out.printf("* buffer.putInt( %d )...%n", i);
      buffer.putInt(i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(20, buffer.capacity());
      Assertions.assertEquals(12, buffer.position());
      Assertions.assertEquals(20, buffer.limit());
      Assertions.assertEquals(8, buffer.remaining());
      System.out.printf("* buffer.flip()...%n");
      buffer.flip();
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(20, buffer.capacity());
      Assertions.assertEquals(0, buffer.position());
      Assertions.assertEquals(12, buffer.limit());
      Assertions.assertEquals(12, buffer.remaining());
      Assertions.assertEquals(12, buffer.remaining());
      i = buffer.getInt();
      System.out.printf("* buffer.getInt() = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(1001, i);
      Assertions.assertEquals(4, buffer.position());
      i = buffer.getInt();
      System.out.printf("* buffer.getInt() = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(2002, i);
      Assertions.assertEquals(8, buffer.position());
      i = buffer.getInt();
      System.out.printf("* buffer.getInt() = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003, i);
      Assertions.assertEquals(12, buffer.position());
      i = buffer.getInt(8);
      System.out.printf("* buffer.getInt(8) = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003, i);
      Assertions.assertEquals(12, buffer.position());
      i = 2002;
      System.out.printf("* buffer.putInt( 4, %d )...%n", i);
      buffer.putInt(4, i);
      i = 3003;
      System.out.printf("* buffer.putInt( 8, %d )...%n", i);
      buffer.putInt(8, i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(20, buffer.capacity());
      Assertions.assertEquals(12, buffer.position());
      Assertions.assertEquals(12, buffer.limit());
      Assertions.assertEquals(0, buffer.remaining());
      i = buffer.getInt(4);
      System.out.printf("* buffer.getInt(4) = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(2002, i);
      Assertions.assertEquals(8, buffer.position());
      i = buffer.getInt(8);
      System.out.printf("* buffer.getInt(8) = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003, i);
      Assertions.assertEquals(12, buffer.position());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void testPutFlipGetLongs() {
    System.out.printf("====== testPutFlipGetLongs ======%n");
    try {
      ExpansibleByteBuffer buffer = new ExpansibleByteBuffer(ExpansibleByteBuffer.AllocPolicy.HEAP_ALLOC_POLICY, 20);
      System.out.printf("%s%n", buffer);
      long i = 1001;
      System.out.printf("* buffer.putLong( %d )...%n", i);
      buffer.putLong(i);
      i = 2002;
      System.out.printf("* buffer.putLong( %d )...%n", i);
      buffer.putLong(i);
      i = 3003;
      System.out.printf("* buffer.putLong( %d )...%n", i);
      buffer.putLong(i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(40, buffer.capacity());
      Assertions.assertEquals(24, buffer.position());
      Assertions.assertEquals(40, buffer.limit());
      Assertions.assertEquals(16, buffer.remaining());
      System.out.printf("* buffer.flip()...%n");
      buffer.flip();
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(40, buffer.capacity());
      Assertions.assertEquals(0, buffer.position());
      Assertions.assertEquals(24, buffer.limit());
      Assertions.assertEquals(24, buffer.remaining());
      i = buffer.getLong();
      System.out.printf("* buffer.getLong() = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(1001l, i);
      Assertions.assertEquals(8, buffer.position());
      i = buffer.getLong();
      System.out.printf("* buffer.getLong() = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(2002l, i);
      Assertions.assertEquals(16, buffer.position());
      i = buffer.getLong();
      System.out.printf("* buffer.getLong() = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003l, i);
      Assertions.assertEquals(24, buffer.position());
      i = buffer.getLong(16);
      System.out.printf("* buffer.getLong(16) = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003l, i);
      Assertions.assertEquals(24, buffer.position());
      i = 2002l;
      System.out.printf("* buffer.putLong( 8, %d )...%n", i);
      buffer.putLong(8, i);
      i = 3003l;
      System.out.printf("* buffer.putLong( 16, %d )...%n", i);
      buffer.putLong(16, i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(40, buffer.capacity());
      Assertions.assertEquals(24, buffer.position());
      Assertions.assertEquals(24, buffer.limit());
      Assertions.assertEquals(0, buffer.remaining());
      i = buffer.getLong(8);
      System.out.printf("* buffer.getLong(8) = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(2002l, i);
      Assertions.assertEquals(16, buffer.position());
      i = buffer.getLong(16);
      System.out.printf("* buffer.getLong(.16) = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003l, i);
      Assertions.assertEquals(24, buffer.position());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  
  @Test
  public void testPutFlipGetDoubles() {
    System.out.printf("====== testPutFlipGetDoubles ======%n");
    try {
      ExpansibleByteBuffer buffer = new ExpansibleByteBuffer(ExpansibleByteBuffer.AllocPolicy.HEAP_ALLOC_POLICY, 20);
      System.out.printf("%s%n", buffer);
      double i = 1001.1;
      System.out.printf("* buffer.putDouble( %f )...%n", i);
      buffer.putDouble(i);
      i = 2002.2;
      System.out.printf("* buffer.putDouble( %f )...%n", i);
      buffer.putDouble(i);
      i = 3003.3;
      System.out.printf("* buffer.putDouble( %f )...%n", i);
      buffer.putDouble(i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(40, buffer.capacity());
      Assertions.assertEquals(24, buffer.position());
      Assertions.assertEquals(40, buffer.limit());
      Assertions.assertEquals(16, buffer.remaining());
      System.out.printf("* buffer.flip()...%n");
      buffer.flip();
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(40, buffer.capacity());
      Assertions.assertEquals(0, buffer.position());
      Assertions.assertEquals(24, buffer.limit());
      Assertions.assertEquals(24, buffer.remaining());
      i = buffer.getDouble();
      System.out.printf("* buffer.getDouble() = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(1001.1, i);
      Assertions.assertEquals(8, buffer.position());
      i = buffer.getDouble();
      System.out.printf("* buffer.getDouble() = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(2002.2, i);
      Assertions.assertEquals(16, buffer.position());
      i = buffer.getDouble();
      System.out.printf("* buffer.getDouble() = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003.3, i);
      Assertions.assertEquals(24, buffer.position());
      i = buffer.getDouble(16);
      System.out.printf("* buffer.getDouble(16) = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003.3, i);
      Assertions.assertEquals(24, buffer.position());
      i = 2002.2;
      System.out.printf("* buffer.putDouble( 8, %f )...%n", i);
      buffer.putDouble(8, i);
      i = 3003.3;
      System.out.printf("* buffer.putDouble( 16, %f )...%n", i);
      buffer.putDouble(16, i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(40, buffer.capacity());
      Assertions.assertEquals(24, buffer.position());
      Assertions.assertEquals(24, buffer.limit());
      Assertions.assertEquals(0, buffer.remaining());
      i = buffer.getDouble(8);
      System.out.printf("* buffer.getDouble(8) = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(2002.2, i);
      Assertions.assertEquals(16, buffer.position());
      i = buffer.getDouble(16);
      System.out.printf("* buffer.getDouble(16) = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003.3, i);
      Assertions.assertEquals(24, buffer.position());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void testPutFlipGetFloats() {
    System.out.printf("====== testPutFlipGetFloats ======%n");
    try {
      ExpansibleByteBuffer buffer = new ExpansibleByteBuffer(ExpansibleByteBuffer.AllocPolicy.HEAP_ALLOC_POLICY, 10);
      System.out.printf("%s%n", buffer);
      float i = 1001.1f;
      System.out.printf("* buffer.putFloat( %f )...%n", i);
      buffer.putFloat(i);
      i = 2002.2f;
      System.out.printf("* buffer.putFloat( %f )...%n", i);
      buffer.putFloat(i);
      i = 3003.3f;
      System.out.printf("* buffer.putFloat( %f )...%n", i);
      buffer.putFloat(i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(20, buffer.capacity());
      Assertions.assertEquals(12, buffer.position());
      Assertions.assertEquals(20, buffer.limit());
      Assertions.assertEquals(8, buffer.remaining());
      System.out.printf("* buffer.flip()...%n");
      buffer.flip();
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(20, buffer.capacity());
      Assertions.assertEquals(0, buffer.position());
      Assertions.assertEquals(12, buffer.limit());
      Assertions.assertEquals(12, buffer.remaining());
      Assertions.assertEquals(12, buffer.remaining());
      i = buffer.getFloat();
      System.out.printf("* buffer.getFloat() = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(1001.1f, i);
      Assertions.assertEquals(4, buffer.position());
      i = buffer.getFloat();
      System.out.printf("* buffer.getFloat() = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(2002.2f, i);
      Assertions.assertEquals(8, buffer.position());
      i = buffer.getFloat();
      System.out.printf("* buffer.getFloat() = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003.3f, i);
      Assertions.assertEquals(12, buffer.position());
      i = buffer.getFloat(8);
      System.out.printf("* buffer.getFloat(8) = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003.3f, i);
      Assertions.assertEquals(12, buffer.position());
      i = 2002.2f;
      System.out.printf("* buffer.putFloat( 4, %f )...%n", i);
      buffer.putFloat(4, i);
      i = 3003.3f;
      System.out.printf("* buffer.putFloat( 8, %f )...%n", i);
      buffer.putFloat(8, i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(20, buffer.capacity());
      Assertions.assertEquals(12, buffer.position());
      Assertions.assertEquals(12, buffer.limit());
      Assertions.assertEquals(0, buffer.remaining());
      i = buffer.getFloat(4);
      System.out.printf("* buffer.getFloat(4) = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(2002.2f, i);
      Assertions.assertEquals(8, buffer.position());
      i = buffer.getFloat(8);
      System.out.printf("* buffer.getFloat(8) = %f%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(3003.3f, i);
      Assertions.assertEquals(12, buffer.position());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void testPutFlipGetShorts() {
    System.out.printf("====== testPutFlipGetShorts ======%n");
    try {
      ExpansibleByteBuffer buffer = new ExpansibleByteBuffer(ExpansibleByteBuffer.AllocPolicy.HEAP_ALLOC_POLICY, 5);
      System.out.printf("%s%n", buffer);
      short i = 101;
      System.out.printf("* buffer.putShort( %d )...%n", i);
      buffer.putShort(i);
      i = 202;
      System.out.printf("* buffer.putShort( %d )...%n", i);
      buffer.putShort(i);
      i = 303;
      System.out.printf("* buffer.putShort( %d )...%n", i);
      buffer.putShort(i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(10, buffer.capacity());
      Assertions.assertEquals(6, buffer.position());
      Assertions.assertEquals(10, buffer.limit());
      Assertions.assertEquals(4, buffer.remaining());
      System.out.printf("* buffer.flip()...%n");
      buffer.flip();
      System.out.printf("- %s%n", buffer);
      Assertions.assertEquals(10, buffer.capacity());
      Assertions.assertEquals(0, buffer.position());
      Assertions.assertEquals(6, buffer.limit());
      Assertions.assertEquals(6, buffer.remaining());
      i = buffer.getShort();
      System.out.printf("* buffer.getShort() = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(101, i);
      Assertions.assertEquals(2, buffer.position());
      i = buffer.getShort();
      System.out.printf("* buffer.getShort() = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(202, i);
      Assertions.assertEquals(4, buffer.position());
      i = buffer.getShort();
      System.out.printf("* buffer.getShort() = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(303, i);
      Assertions.assertEquals(6, buffer.position());
      i = buffer.getShort(4);
      System.out.printf("* buffer.getShort(8) = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(303, i);
      Assertions.assertEquals(6, buffer.position());
      i = 202;
      System.out.printf("* buffer.putShort( 2, %d )...%n", i);
      buffer.putShort(2, i);
      i = 303;
      System.out.printf("* buffer.putShort( 4, %d )...%n", i);
      buffer.putShort(4, i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(10, buffer.capacity());
      Assertions.assertEquals(6, buffer.position());
      Assertions.assertEquals(6, buffer.limit());
      Assertions.assertEquals(0, buffer.remaining());
      i = buffer.getShort(2);
      System.out.printf("* buffer.getShort(2) = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(202, i);
      Assertions.assertEquals(4, buffer.position());
      i = buffer.getShort(4);
      System.out.printf("* buffer.getShort(4) = %d%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(303, i);
      Assertions.assertEquals(6, buffer.position());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void testPutFlipGetChars() {
    System.out.printf("====== testPutFlipGetChars ======%n");
    try {
      ExpansibleByteBuffer buffer = new ExpansibleByteBuffer(ExpansibleByteBuffer.AllocPolicy.HEAP_ALLOC_POLICY, 5);
      System.out.printf("%s%n", buffer);
      char i = 'a';
      System.out.printf("* buffer.putChar( %s )...%n", i);
      buffer.putChar(i);
      i = 'b';
      System.out.printf("* buffer.putChar( %s )...%n", i);
      buffer.putChar(i);
      i = 'c';
      System.out.printf("* buffer.putChar( %s )...%n", i);
      buffer.putChar(i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(10, buffer.capacity());
      Assertions.assertEquals(6, buffer.position());
      Assertions.assertEquals(10, buffer.limit());
      Assertions.assertEquals(4, buffer.remaining());
      System.out.printf("* buffer.flip()...%n");
      buffer.flip();
      System.out.printf("- %s%n", buffer);
      Assertions.assertEquals(10, buffer.capacity());
      Assertions.assertEquals(0, buffer.position());
      Assertions.assertEquals(6, buffer.limit());
      Assertions.assertEquals(6, buffer.remaining());
      i = buffer.getChar();
      System.out.printf("* buffer.getChar() = %s%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals('a', i);
      Assertions.assertEquals(2, buffer.position());
      i = buffer.getChar();
      System.out.printf("* buffer.getChar() = %s%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals('b', i);
      Assertions.assertEquals(4, buffer.position());
      i = buffer.getChar();
      System.out.printf("* buffer.getChar() = %s%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals('c', i);
      Assertions.assertEquals(6, buffer.position());
      i = buffer.getChar(4);
      System.out.printf("* buffer.getChar(4) = %s%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals('c', i);
      Assertions.assertEquals(6, buffer.position());
      i = 'b';
      System.out.printf("* buffer.putChar( 2, %s )...%n", i);
      buffer.putChar(2, i);
      i = 'c';
      System.out.printf("* buffer.putChar( 4, %s )...%n", i);
      buffer.putChar(4, i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals(10, buffer.capacity());
      Assertions.assertEquals(6, buffer.position());
      Assertions.assertEquals(6, buffer.limit());
      Assertions.assertEquals(0, buffer.remaining());
      i = buffer.getChar(2);
      System.out.printf("* buffer.getChar(2) = %s%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals('b', i);
      Assertions.assertEquals(4, buffer.position());
      i = buffer.getChar(4);
      System.out.printf("* buffer.getChar(4) = %s%n", i);
      System.out.printf("%s%n", buffer);
      Assertions.assertEquals('c', i);
      Assertions.assertEquals(6, buffer.position());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
