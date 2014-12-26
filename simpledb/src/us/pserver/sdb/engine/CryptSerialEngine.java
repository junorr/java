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

package us.pserver.sdb.engine;

import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptByteCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.sdb.SDBException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 26/12/2014
 */
public class CryptSerialEngine implements SerialEngine {

  private SerialEngine serial;
  
  private CryptByteCoder coder;
  
  
  public CryptSerialEngine(SerialEngine inner, CryptKey key) {
    if(inner == null)
      throw new IllegalArgumentException("Invalid null SerialEngine - CryptSerialEngine.init()");
    if(key == null)
      throw new IllegalArgumentException("Invalid null CryptKey - CryptSerialEngine.init()");
    serial = inner;
    coder = new CryptByteCoder(key);
  }

  
  @Override
  public byte[] serialize(Object obj) throws SDBException {
    if(obj == null) 
      throw new IllegalArgumentException(
        "Can not serialize null object - CryptSerialEngine.serialize");
    byte[] bs = serial.serialize(obj);
    return coder.encode(bs);
  }


  @Override
  public Object deserialize(byte[] bts) throws SDBException {
    if(bts == null || bts.length < 1)
      throw new IllegalArgumentException(
          "Invalid "+ (bts == null ? "null" : "empty")
              + " byte array - CryptSerialEngine.deserialize");
    byte[] bs = coder.decode(bts);
    return serial.deserialize(bs);
  }

}
