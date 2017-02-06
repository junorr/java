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

package us.pserver.ip;

import java.net.InetAddress;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/02/2017
 */
public interface INet {

  public String name();
  
  public int index();
  
  public boolean isLoopback();
  
  public InetAddress ipv4();
  
  public InetAddress ipv6();
  
  public String ipv4String();
  
  public String ipv6String();
  
  
  public static INet of(String name, int index, boolean loopback, InetAddress ipv4, InetAddress ipv6) {
    return new DefINet(name, index, loopback, ipv4, ipv6);
  }
  
  
  
  
  
  public static final class DefINet implements INet {
    
    private final String name;
    
    private final InetAddress ipv4;
    
    private final InetAddress ipv6;
    
    private final int index;
    
    private final boolean loopback;
    
    
    private DefINet() {
      name = null;
      ipv4 = null;
      ipv6 = null;
      index = 0;
      loopback = false;
    }
    
    
    public DefINet(String name, int index, boolean loopback, InetAddress ipv4, InetAddress ipv6) {
      if(name == null) {
        throw new IllegalArgumentException("Bad Null Name");
      }
      if(index <= 0) {
        throw new IllegalArgumentException("Bad INet Index ("+ index+ ")");
      }
      if(ipv4 == null) {
        throw new IllegalArgumentException("Bad Null IPv4 InetAddress");
      }
      this.name = name;
      this.loopback = loopback;
      this.ipv4 = ipv4;
      this.ipv6 = ipv6;
      this.index = index;
    }
    

    @Override
    public String name() {
      return name;
    }


    @Override
    public int index() {
      return index;
    }
    
    
    @Override
    public boolean isLoopback() {
      return loopback;
    }


    @Override
    public InetAddress ipv4() {
      return ipv4;
    }


    @Override
    public InetAddress ipv6() {
      return ipv6;
    }


    @Override
    public String ipv4String() {
      return ipv4.getHostAddress();
    }


    @Override
    public String ipv6String() {
      return ipv6 != null ? ipv6.getHostAddress() : null;
    }


    @Override
    public int hashCode() {
      int hash = 7;
      hash = 13 * hash + Objects.hashCode(this.ipv4);
      hash = 13 * hash + Objects.hashCode(this.ipv6);
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
      final DefINet other = (DefINet) obj;
      return Objects.equals(this.ipv6, other.ipv6) 
          && Objects.equals(this.ipv4, other.ipv4);
    }
    
    
    @Override
    public String toString() {
      return "INet("+ name+ ":"+ index+ "){IPv4="+ ipv4String()+ ", IPv6="+ ipv6String()+ "}";
    }
    
  }
  
}
