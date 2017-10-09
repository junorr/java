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

package us.pserver.dbone;

import java.nio.ByteBuffer;
import java.util.Objects;
import us.pserver.tools.Hash;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 09/10/2017
 */
public interface ObjectUID {
  
  public String getUID();
  
  public String getClassName();
  
  
  public static ObjectUID of(String uid, String className) {
    return new DefObjectUID(uid, className);
  }
  
  
  public static ObjectUID of(ByteBuffer buf, String className) {
    Hash h = Hash.sha1();
    h.put(className);
    if(buf.hasArray()) {
      h.put(buf.array());
    }
    else {
      int pos = buf.position();
      int lim = buf.limit();
      byte[] bs = new byte[4096];
      while(buf.hasRemaining()) {
        int read = Math.min(bs.length, buf.remaining());
        buf.get(bs, 0, read);
        h.put(bs);
      }
      buf.limit(lim);
      buf.position(pos);
    }
    return new DefObjectUID(h.get(), className);
  }


  
  
  
  
  public static class DefObjectUID implements ObjectUID {
    
    private final String uid;
    
    private final String cname;
    
    
    public DefObjectUID(String uid, String cname) {
      this.cname = NotNull.of(cname).getOrFail("Bad null class name");
      this.uid = NotNull.of(uid).getOrFail("Bad null UID String");
    }
    
    
    @Override
    public String getUID() {
      return uid;
    }
    
    
    @Override
    public String getClassName() {
      return cname;
    }


    @Override
    public int hashCode() {
      int hash = 5;
      hash = 23 * hash + Objects.hashCode(this.uid);
      hash = 23 * hash + Objects.hashCode(this.cname);
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
      final DefObjectUID other = (DefObjectUID) obj;
      if (!Objects.equals(this.uid, other.uid)) {
        return false;
      }
      return Objects.equals(this.cname, other.cname);
    }


    @Override
    public String toString() {
      return "ObjectUID{" + "uid=" + uid + ", class=" + cname + '}';
    }

  }
  
}
