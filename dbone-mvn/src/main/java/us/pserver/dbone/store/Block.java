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

package us.pserver.dbone.store;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/05/2018
 */
public interface Block extends Byteable {
  
  public static enum Type implements Byteable {
    
    NODE, ROOT;
    
    @Override
    public ByteBuffer toByteBuffer() {
      return ByteBuffer.wrap(new byte[]{
        Integer.valueOf(this.ordinal()).byteValue()
      });
    }
    
    public static final int BYTES = 1;
    
  }
  

  public static final int META_BYTES = Region.BYTES + Integer.BYTES + Type.BYTES;
  
  public Region getNextRegion();
  
  public ByteBuffer getBuffer();
  
  public int length();
  
  public boolean isRoot();
  
  public boolean isNode();
  
  public int writeTo(WritableByteChannel ch) throws IOException;
  
}
