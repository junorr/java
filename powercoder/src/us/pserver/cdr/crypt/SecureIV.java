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

package us.pserver.cdr.crypt;

import javax.crypto.spec.IvParameterSpec;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/08/2015
 */
public class SecureIV {

  private final byte[] iv;
  
  private boolean init;
  
  
  public SecureIV(CryptAlgorithm algo) {
    Valid.off(algo).forNull().fail(CryptAlgorithm.class);
    int size = 8;
    if(algo.getStringAlgorithm().contains("AES")) {
      size = 16;
    }
    iv = new byte[size];
    init = false;
  }
  
  
  public SecureIV(byte[] source, CryptAlgorithm algo) {
    this(algo);
    setBytes(source);
  }
  
  
  private void checkInit() {
    if(init == false) {
      throw new IllegalStateException(
          "Secure IV is not initialized yet. "
              + "Call SecureIV.initRandom() "
              + "or SecureIV.setBytes() first."
      );
    }
  }
  
  
  public byte[] getBytes() {
    checkInit();
    return iv;
  }
  
  
  public void setBytes(byte[] array) {
    Valid.off(array).forEmpty().fail();
    byte[] src = array;
    if(array.length != iv.length) {
      src = CryptUtils.truncate(array, iv.length);
    }
    System.arraycopy(src, 0, iv, 0, iv.length);
    init = true;
  }
  
  
  public IvParameterSpec getIVSpec() {
    checkInit();
    return new IvParameterSpec(iv);
  }
  
  
  public boolean isInitialized() {
    return init;
  }
  
  
  public void initRandom() {
    byte[] random = CryptUtils.randomBytes(iv.length);
    this.setBytes(Digester.toSHA256(random));
  }
  
  
  public static SecureIV createRandom(CryptAlgorithm algo) {
    SecureIV iv = new SecureIV(algo);
    iv.initRandom();
    return iv;
  }
  
}
