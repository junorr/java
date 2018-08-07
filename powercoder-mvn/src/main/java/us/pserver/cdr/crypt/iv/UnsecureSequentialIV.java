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

package us.pserver.cdr.crypt.iv;

import java.util.Objects;
import us.pserver.cdr.crypt.CryptAlgorithm;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/10/2015
 */
public class UnsecureSequentialIV extends AlgorithmSizedIV {

  
  public UnsecureSequentialIV(int size) {
    this.init(size);
  }
  
  
  public UnsecureSequentialIV(int size, int value) {
    this.init(size, value);
  }
  
  
  public UnsecureSequentialIV(CryptAlgorithm algo) {
    this.init(algo);
  }
  
  
  public UnsecureSequentialIV(CryptAlgorithm algo, int value) {
    this.init(algo, value);
  }
  
  
  @Override
  public void init(CryptAlgorithm algo) {
    this.init(algo, 0);
  }
  
  
  @Override
  public void init(int size) {
    this.init(size, 0);
  }
  
  
  public void init(CryptAlgorithm algo, int value) {
    super.init(algo);
    this.setSequentialIV(value);
  }
  
  
  public void init(int size, int value) {
    this.init(size);
    this.setSequentialIV(size);
  }
  
  
  public void setSequentialIV(int start) {
    Objects.requireNonNull(this.getVector());
    byte[] vec = getVector();
    byte seq = (byte) start;
    for(int i = 0; i < vec.length; i++) {
      vec[i] = seq++;
    }
  }
  
}
