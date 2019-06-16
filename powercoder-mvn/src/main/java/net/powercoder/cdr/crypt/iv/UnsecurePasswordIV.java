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

package net.powercoder.cdr.crypt.iv;

import java.util.Objects;
import net.powercoder.cdr.crypt.CryptAlgorithm;
import net.powercoder.cdr.crypt.CryptUtils;
import net.powercoder.cdr.digest.Digester;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/10/2015
 */
public class UnsecurePasswordIV extends AlgorithmSizedIV {
  
  
  public UnsecurePasswordIV() {}
  
  
  public UnsecurePasswordIV(int size, String str) {
    init(size, str);
  }

  
  public UnsecurePasswordIV(CryptAlgorithm algo, String str) {
    super(algo);
    init(this.getVector().length, str);
  }
  
  
  public void init(int size, String str) {
    if(size < 1) throw new IllegalArgumentException("Bad size: "+ size);
    Objects.requireNonNull(str);
    this.init(
        CryptUtils.truncate(Digester.toSHA256(str), size)
    );
  }
  
  
  public void init(CryptAlgorithm algo, String str) {
    Objects.requireNonNull(algo);
    Objects.requireNonNull(str);
    this.init(
        CryptUtils.truncate(
            Digester.toSHA256(str), 
            this.getVector().length
        )
    );
  }
  
  
}
