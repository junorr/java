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

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.function.IntFunction;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2018
 */
public interface BitBoxConfiguration {

  
  public static enum BufferAlloc {
    
    DIRECT_ALLOC(ByteBuffer::allocateDirect),
    
    HEAP_ALLOC(ByteBuffer::allocate);
    
    private BufferAlloc(IntFunction<ByteBuffer> alloc) {
      this.alloc = alloc;
    }
    
    public ByteBuffer alloc(int size) {
      return alloc.apply(size);
    }
    
    private final IntFunction<ByteBuffer> alloc;
    
  }
  
  
  public ByteBuffer alloc(int size);
  
  public BitBoxConfiguration addTypeSupport(TypeSupport support);
  
  public Optional<TypeSupport> getTypeSupport(Class cls);
  
  public Optional<TypeSupport> removeTypeSupport(Class cls);
  
  public BitBoxConfiguration setBufferAlloc(BufferAlloc alloc);
  
  public BufferAlloc getBufferAlloc();
  
  public BitBoxConfiguration setUseGetters(boolean use);
  
  public boolean isUseGetters();
  
}
