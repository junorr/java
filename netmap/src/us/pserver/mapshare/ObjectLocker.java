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

package us.pserver.mapshare;

import com.thoughtworks.xstream.XStream;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptByteCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.hex.HexByteCoder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/01/2014
 */
public class ObjectLocker {

  private String object;
  
  private transient XStream xst;
  
  private transient HexByteCoder cdr;
  
  private transient CryptByteCoder crypt;
  
  private transient StringByteConverter conv;
  
  
  public ObjectLocker() {
    object = null;
  }
  
  
  public ObjectLocker(Object obj) {
    this.lock(obj);
  }
  
  
  private void init() {
    if(xst == null)
      xst = new XStream();
    if(cdr == null)
      cdr = new HexByteCoder();
    if(conv == null)
      conv = new StringByteConverter();
    if(crypt == null)
      crypt = new CryptByteCoder(
          new CryptKey(this.getClass().getCanonicalName(), 
          CryptAlgorithm.DES_ECB_PKCS5));
  }
  
  
  public void lock(Object obj) {
    if(obj == null) return;
    this.init();
    byte[] bs = conv.convert(xst.toXML(obj));
    bs = crypt.encode(bs);
    object = conv.reverse(cdr.encode(bs));
  }
  
  
  public <T> T unlock() {
    if(object == null) return null;
    this.init();
    byte[] bs = cdr.decode(conv.convert(object));
    bs = crypt.decode(bs);
    return (T) xst.fromXML(conv.reverse(bs));
  }
  
  
  @Override
  public String toString() {
    return "ObjectLocker{ object="+ object+ " }";
  }
  
  
  public static void main(String[] args) {
    ObjectLocker locker = new ObjectLocker();
    String str = "a secret string";
    System.out.println(locker.xst.toXML(str));
    locker.lock(str);
    str = null;
    System.out.println(locker);
    System.out.println(locker.xst.toXML(locker));
    str = locker.unlock();
    System.out.println("unlocked: "+ str);
  }
  
}
