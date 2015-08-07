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

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import us.pserver.xprops.XBean;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/07/2015
 */
public class TestXml1 {

  
  public static class Wrapper {
    Integer integer;
    String string;
    Double dbl;
    Date date;
    byte[] bytes;
    List<Wrapper> wlist;
    public String toString() {
      return "\nWrapper {\n  integer="+ integer
          + "\n  string="+ string
          + "\n  dbl="+ dbl
          + "\n  date="+ date
          + "\n  bytes="+ Arrays.toString(bytes)
          + "\n  wlist{"+ (wlist != null && !wlist.isEmpty() ? "type="+ wlist.get(0).getClass().getSimpleName()+ ", size="+ wlist.size() : "")+ "}"
          + "\n}";
    }
  }
  
  
  public static Wrapper wrapper() {
    Wrapper wp = new Wrapper();
    wp.bytes = new byte[]{0,1,2,3,4,5,6,7,8,9};
    wp.dbl = Math.random()*100;
    wp.integer = wp.dbl.intValue();
    wp.date = new Date();
    wp.string = String.format("Hello Wrapper (%s)", System.currentTimeMillis());
    wp.wlist = new LinkedList();
    return wp;
  }
  
  
  public static void fillWrapperList(int size, Wrapper target) {
    for(int i = 0; i < size; i++) {
      Wrapper w = wrapper();
      //w.bytes = new byte[0];
      target.wlist.add(w);
    }
  }
  
  
  public static void main(String[] args) {
    Wrapper w = wrapper();
    System.out.println(w);
    XBean bean = new XBean(w);
    bean.setFieldsAsAttribute(true).bindAll().scanObject().setXmlIdentation("  ", 0);
    System.out.println(bean.toXml());
    bean = new XBean(bean, new Wrapper());
    bean.setFieldsAsAttribute(true).bindAll();
    System.out.println(bean.scanXml());
  }
  
}
