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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/12/2018
 */
public interface BinaryArray extends BinaryForm {
  
  public int size();
  
  public BinaryForm get(int idx);
  
  public boolean isEmpty();
  
  public boolean contains(BinaryForm bf);
  
  public Iterator<BinaryForm> iterator();
  
  public Stream<BinaryForm> stream();
  
  public BinaryForm[] toArray();
  
  
  
  
  
  public static class BinaryArrayBuilder {
    
    private final List<BinaryForm> list;
    
    
    public BinaryArrayBuilder(Collection<BinaryForm> col) {
      this.list = new ArrayList<>(col);
    }
    
    
    public BinaryArrayBuilder(BinaryArray array) {
      this(Arrays.asList(array.toArray()));
    }
    
    
    public List<BinaryForm> list() {
      return list;
    }
    
    
    public BinaryArrayBuilder add(BinaryForm bf) {
      if(bf != null) {
        list.add(bf);
      }
      return this;
    }
    
    
    public BinaryArray build() {
      return null;
    }
    
  }
  
}
