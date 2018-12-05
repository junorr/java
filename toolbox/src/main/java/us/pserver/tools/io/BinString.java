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

import java.nio.ByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/12/2018
 */
public interface BinString extends BinaryForm {
  
  public ByteBuffer getContentBuffer();
  
  public byte[] getContentBytes();
  
  public BinString append(String str);
  
  public BinString append(BinString str);
  
  public int indexOf(String str, int start);
  
  public int indexOf(BinString str, int start);
  
  public boolean contains(BinString str);
  
  public boolean contains(String str);
  
  public int length();
  
  public BinString slice(int offset, int length);
  
  public BinString slice(int offset);
  
  
  
  
  public static BinString empty() {
    return new UTFBinString();
  }
  
  public static BinString of(String str) {
    return new UTFBinString(str);
  }
  
  public static BinString of(byte[] bs) {
    return new UTFBinString(bs);
  }
  
  public static BinString of(byte[] bs, int off, int len) {
    return new UTFBinString(bs, off, len);
  }
  
  public static BinString of(ByteBuffer buf) {
    return new UTFBinString(buf);
  }
  
  public static BinString of(DynamicByteBuffer buf) {
    return new UTFBinString(buf);
  }
  
  public static BinString ofContent(ByteBuffer buf) {
    return new UTFBinString(buf.remaining(), buf);
  }
  
}
