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

package us.pserver.xprops.xtest;

import us.pserver.xprops.XBean;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class TestXBean {

  static class NetEndPoint {
    String host;
    int port;
    public String toString() { 
      return host+ ":"+ port; 
    }
  }
  
  public static void main(String[] args) {
    NetEndPoint ne = new NetEndPoint();
    ne.host = "localhost";
    ne.port = 5775;
    XBean bean = new XBean("net", ne);
    bean.bindField("host")
        .bindField("port")
        .bindMethod("toString")
        .aliasMethod("toString", "string")
        .scanObject();
    bean.setXmlIdentation("  ", 0);
    System.out.println(bean.toXml());
    
    ne = new NetEndPoint();
    System.out.println(bean.off(ne).bindAll().scanXml());
  }
  
}
