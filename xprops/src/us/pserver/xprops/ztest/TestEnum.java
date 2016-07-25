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

package us.pserver.xprops.ztest;

import us.pserver.xprops.XTag;
import us.pserver.xprops.converter.EnumXConverter;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/08/2015
 */
public class TestEnum {

  public static enum ABC {
    A("a"), B("b"), C("c");
    private String str;
    ABC(String str) {
      this.str = str;
    }
    public String getString() {
      return str;
    }
    public String toString() {
      return this.name()+ "("+ str+ ")";
    }
  }
  
  
  public static enum EFG {
    A, B, C;
  }
  
  
  public static void main(String[] args) {
    System.out.println(ABC.A);
    System.out.println(ABC.B);
    System.out.println(ABC.C);
    EnumXConverter ex = new EnumXConverter(ABC.class);
    XTag t = ex.toXml(ABC.A);
    System.out.println(t.toXml());
    ABC d = (ABC) ex.fromXml(t);
    System.out.println(d);
    
    ex = new EnumXConverter(EFG.class);
    t = ex.toXml(EFG.A);
    System.out.println(t.toXml());
    EFG h = (EFG) ex.fromXml(t);
    System.out.println(h);
  }
  
}
