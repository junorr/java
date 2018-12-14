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

package us.pserver.bitbox;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/12/2018
 */
public interface BitBox<T> extends Comparable<BitBox>, Serializable {
  
  public T get();
  
  public int boxID();
  
  public int boxSize();

  public String sha256sum();
  
  public ByteBuffer toByteBuffer();
  
  public byte[] toByteArray();
  
  public int writeTo(ByteBuffer buf);
  
  public int writeTo(WritableByteChannel ch) throws IOException;
  
  public int writeTo(DynamicByteBuffer buf);
  
  
  
  public static BitBoxFactory factory() {
    return BitBoxFactory.get();
  }
  
}
