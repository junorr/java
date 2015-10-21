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

import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/10/2015
 */
public class UnsecureIntegerIV extends AlgorithmSizedIV {

  
  public UnsecureIntegerIV(int size) {
    super(size);
  }
  
  
  public UnsecureIntegerIV(int size, int value) {
    super(size);
    this.setIntegerIV(value);
  }
  
  
  public UnsecureIntegerIV(CryptAlgorithm algo) {
    super(algo);
  }
  
  
  public UnsecureIntegerIV(CryptAlgorithm algo, int value) {
    super(algo);
    this.setIntegerIV(value);
  }
  
  
  public void init(CryptAlgorithm algo, int value) {
    this.init(algo);
    this.setIntegerIV(value);
  }
  
  
  public void init(int size, int value) {
    super.init(size);
    this.setIntegerIV(value);
  }
  
  
  public void setIntegerIV(int value) {
    Valid.off(this.getVector()).forEmpty().fail();
    for(int i = 0; i < this.getVector().length; i++) {
      this.getVector()[i] = (byte) value;
    }
  }
  
}
