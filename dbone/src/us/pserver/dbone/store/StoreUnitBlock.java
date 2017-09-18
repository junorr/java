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

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;
import us.pserver.tools.NotNull;
import us.pserver.tools.UTF8String;
import us.pserver.tools.mapper.MappedValue;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/09/2017
 */
public class StoreUnitBlock extends DefaultBlock {
  
  public static final Region REGION_CLASS = Region.of(0, 255);
  
  public static final Region REGION_UID = Region.of(255, 40);
  
  public static final String CRLF = "\r\n";
  
  public static final String BOUNDARY = "::";
  

  public StoreUnitBlock(Region r, ByteBuffer b) {
    super(r, b);
  }
  
  public StoreUnit getStoreUnit() {
    this.buffer.position(REGION_CLASS.intOffset());
    String cname = UTF8String.from(getPart(REGION_CLASS)).toString();
    String uid = UTF8String.from(getPart(REGION_UID)).toString();
    Region remaining = Region.of(REGION_UID.offset() + REGION_UID.length(), this.buffer.limit() - 16);
    String sval = UTF8String.from(getPart(remaining)).toString();
    String[] lns = sval.split(CRLF);
    for(String ln : lns) {
      
    }
    return null;
  }
  
  private byte[] getPart(Region r) {
    byte[] bs = new byte[r.intLength()];
    this.buffer.position(r.intOffset());
    this.buffer.get(bs);
    return bs;
  }
  
  public StoreUnitBlock setStoreUnit(StoreUnit unit) throws StoreException {
    //NotNull.of(unit).failIfNull("Bad null StoreUnit");
    //this.buffer.position(REGION_CLASS.intOffset());
    //this.buffer.put(UTF8String.from(unit.getUID().getClassName()).getBytes());
    //this.buffer.position(REGION_UID.intOffset());
    //this.buffer.put(UTF8String.from(unit.getUID().getUID()).getBytes());
    //byte[] bs = 
//    
    //byte[] bs = UTF8String.from(unit.getValue().toString()).getBytes();
    //this.buffer.position(REGION_UID.intOffset() + REGION_UID.intLength());
    //if(bs.length > this.buffer.remaining()-16) {
      //throw new StoreException("StoreUnit length exceeds ByteBuffer capacity");
    //}
    //this.buffer.put(bs);
    return this;
  }
  
}
