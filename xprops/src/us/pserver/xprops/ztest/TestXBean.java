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

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Date;
import us.pserver.xprops.XBean;
import us.pserver.xprops.XBeanBuilder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class TestXBean {

  public static class NetEndpoint {
    SocketAddress address;
    Date creation = new Date();
    public String toString() { 
      return "NetEndPoint{address="+ address+ ", creation="+ creation+ "}"; 
    }
  }
  
  public static class WrongHosts {
    String[] hosts = new String[10];
    int[] ports = new int[10];
    public String toString() {
      StringBuilder sb = new StringBuilder().append("[");
      for(int i = 0; i < hosts.length; i++) {
        sb.append(hosts[i]);
        if(ports.length > i) 
          sb.append(":").append(ports[i]);
        if(i < hosts.length -1) sb.append(",");
      }
      return sb.append("]").toString();
    }
  }
  
  public static class Host {
    NetEndpoint endpoint = new NetEndpoint();
    public String toString() {
      return "Host{"+ endpoint+ "}";
    }
  }
  
  public static class Hosts {
    NetEndpoint[] endpoints = new NetEndpoint[10];
    public String toString() {
      return "Hosts:"+Arrays.toString(endpoints);
    }
  }
  
  public static void main(String[] args) {
    System.out.println("----------------------------");
    NetEndpoint ne = new NetEndpoint();
    ne.address = new InetSocketAddress("localhost", 5775);
    XBean bean = XBeanBuilder.builder(ne)
        .named("net")
        .setAttributeByDefault(true)
        .bindAll()
        .create();
    bean.scanObject();
    bean.setXmlIdentation("  ", 0);
    System.out.println(bean.toXml());
    bean = XBeanBuilder.builder(new NetEndpoint())
        .fromTag(bean)
        .bindAll()
        .create();
    System.out.println(bean.scanXml());
    
    
    System.out.println("----------------------------");
    String addr = "localhost";
    WrongHosts whosts = new WrongHosts();
    for(int i = 0; i < 10; i++) {
      whosts.hosts[i] = addr;
      whosts.ports[i] = i + 80;
    }
    System.out.println("WrongHosts: "+ whosts);
    XBean bean1 = XBeanBuilder.builder(whosts)
        .named("hosts")
        .setAttributeByDefault(true)
        .bindAll()
        .create();
    bean1.scanObject();
    System.out.println(bean1.toXml());
    XBean bean2 = XBeanBuilder.builder(new WrongHosts())
        .fromTag(bean1)
        .setAttributeByDefault(true)
        .bindAll()
        .create();
    System.out.println("bean.scanXml() = "+ bean2.scanXml());
    
    
    System.out.println("----------------------------");
    Host host = new Host();
    host.endpoint.address = new InetSocketAddress("10.100.0.102", 8080);
    System.out.println(host);
    
    XBean beanHost = XBeanBuilder.builder(host)
        .setAttributeByDefault(true)
        .bindAll()
        .create();
    System.out.println(beanHost.scanObject().toXml());
    beanHost = XBeanBuilder.builder(new Host())
        .fromTag(beanHost)
        .bindAll()
        .create();
    System.out.println(beanHost.scanXml());
    
    
    System.out.println("----------------------------");
    Hosts hosts = new Hosts();
    String baseAddr = "10.100.0.";
    int startAddr = 100;
    int basePort = 8080;
    for(int i = 0; i < 10; i++) {
      hosts.endpoints[i] = new NetEndpoint();
      hosts.endpoints[i].address = new InetSocketAddress(baseAddr.concat(
          String.valueOf((startAddr + i))), (basePort + i)
      );
    }
    System.out.println(hosts);
    
    bean = XBeanBuilder.builder(hosts)
        .setAttributeByDefault(true)
        .bindAll()
        .create();
    System.out.println(bean.scanObject().setXmlIdentation("  ", 0).toXml());
    
    bean = XBeanBuilder.builder(new Hosts())
        .fromTag(bean)
        .bindAll()
        .create();
    System.out.println(bean.scanXml());
  }
  
}
