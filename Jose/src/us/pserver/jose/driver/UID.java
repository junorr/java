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

package us.pserver.jose.driver;

import us.pserver.jose.util.GrowableBuffer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
import us.pserver.cdr.digest.Digester;
import us.pserver.cdr.hex.HexCoder;
import us.pserver.tools.UTF8String;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/03/2017
 */
public interface UID {
  
  public String uid();
  
  public byte[] getBytes();
  

  public static UID of(Object obj) {
    if(obj == null) {
      throw new IllegalArgumentException("Bad Null Object");
    }
    GrowableBuffer buf = new GrowableBuffer()
        .put(obj.getClass().getName());
    Reflector ref = Reflector.of(obj);
    Field[] fls = ref.fields();
    for(Field f : fls) {
      if(Modifier.isStatic(f.getModifiers())) continue;
      //System.out.println("* put( "+ f.getName()+ " )");
      buf.put(f.getName());
      //System.out.println("* put( "+ f.getType().getName()+ " )");
      buf.put(f.getType().getName());
      //System.out.println("* put( "+ Objects.toString(ref.selectField(f.getName()).get())+ " )");
      buf.put(Objects.toString(ref.selectField(f.getName()).get()));
    }
    return new UIDImpl(HexCoder.toHexString(
        Digester.toSHA1(buf.getBytes())
    ).toUpperCase());
  }
  
  
  
  
  
  static final class UIDImpl implements UID {
    
    private final String uid;
    
    
    private UIDImpl() {
      uid = null;
    }
    
    
    private UIDImpl(String uid) {
      if(uid == null || uid.isEmpty()) {
        throw new IllegalArgumentException("Bad Null UID String");
      }
      this.uid = uid;
    }
    
    
    @Override
    public String uid() {
      return uid;
    }
    
    
    @Override
    public byte[] getBytes() {
      return UTF8String.from(uid).getBytes();
    }


    @Override
    public int hashCode() {
      int hash = 7;
      hash = 89 * hash + Objects.hashCode(this.uid);
      return hash;
    }


    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final UIDImpl other = (UIDImpl) obj;
      return Objects.equals(this.uid, other.uid);
    }


    @Override
    public String toString() {
      return uid;
    }
    
  }
  
}
