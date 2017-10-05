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

package us.pserver.ironbit;

import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/10/2017
 */
public abstract class SerialCommons {

  public static void writeInt(int val, byte[] bar, int pos) {
    int from = Integer.BYTES + pos - 1;
    for(int i = from; i >= pos; i--) {
      bar[i] = (byte)(val & 0xFF);
      val >>= Integer.BYTES;
    }
  }
  
  public static int readInt(byte[] bar, int pos) {
    //System.out.print("* readInt( "+ bar.length+ ", "+ pos+" ): ");
    int result = 0;
    for(int i = pos; i < Integer.BYTES + pos; i++) {
      result <<= Integer.BYTES;
      result |= (bar[i] & 0xFF);
    }
    //System.out.println(result);
    return result;
  }
  

  public static void writeLong(long val, byte[] bar, int pos) {
    int from = Long.BYTES + pos - 1;
    for(int i = from; i >= pos; i--) {
      bar[i] = (byte)(val & 0xFF);
      val >>= Long.BYTES;
    }
  }
  
  public static long readLong(byte[] bar, int pos) {
    long result = 0;
    for(int i = pos; i < Long.BYTES + pos; i++) {
      result <<= Long.BYTES;
      result |= (bar[i] & 0xFF);
    }
    return result;
  }
  

  public static void writeShort(short val, byte[] bar, int pos) {
    int from = Short.BYTES + pos - 1;
    for(int i = from; i >= pos; i--) {
      bar[i] = (byte)(val & 0xFF);
      val >>= Short.BYTES;
    }
  }
  
  public static short readShort(byte[] bar, int pos) {
    short result = 0;
    for(int i = pos; i < Short.BYTES + pos; i++) {
      result <<= Short.BYTES;
      result |= (bar[i] & 0xFF);
    }
    return result;
  }
  

  public static void writeFloat(float val, byte[] bar, int pos) {
    writeInt(Float.floatToIntBits(val), bar, pos);
  }
  
  public static float readFloat(byte[] bar, int pos) {
    return Float.intBitsToFloat(readInt(bar, pos));
  }
  

  public static void writeDouble(double val, byte[] bar, int pos) {
    writeLong(Double.doubleToLongBits(val), bar, pos);
  }
  
  public static double readDouble(byte[] bar, int pos) {
    return Double.longBitsToDouble(readLong(bar, pos));
  }
  

  public static void writeString(String val, byte[] bar, int pos) {
    byte[] bs = UTF8String.from(val).getBytes();
    System.arraycopy(bs, 0, bar, pos, bs.length);
  }
  
  public static String readString(byte[] bar, int pos, int len) {
    return UTF8String.from(bar, pos, len).toString();
  }
  
  public static String readString(byte[] bar, int pos) {
    return UTF8String.from(bar, pos, bar.length - pos).toString();
  }
  

  public static void writeChar(char val, byte[] bar, int pos) {
    writeString(String.valueOf(val), bar, pos);
  }
  
  public static char readChar(byte[] bar, int pos) {
    return readString(bar, pos, 1).charAt(0);
  }
  
}
