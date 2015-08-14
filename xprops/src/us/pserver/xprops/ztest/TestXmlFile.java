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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import us.pserver.tools.timer.Timer;
import us.pserver.xprops.XBean;
import us.pserver.xprops.XBeanBuilder;
import us.pserver.xprops.XFile;
import us.pserver.xprops.XTag;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/07/2015
 */
public class TestXmlFile {


  public static class NetEndPoint {
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
    NetEndPoint endpoint = new NetEndPoint();
    public String toString() {
      return "Host{"+ endpoint+ "}";
    }
  }
  
  public static class Hosts {
    NetEndPoint[] endpoints = new NetEndPoint[10];
    public String toString() {
      return "Hosts:"+Arrays.toString(endpoints);
    }
  }
  
  
  public static void save(String file) throws IOException {
    System.out.println("----------------------------");
    Hosts hosts = new Hosts();
    String baseAddr = "10.100.0.";
    int startAddr = 100;
    int basePort = 8080;
    for(int i = 0; i < 10; i++) {
      hosts.endpoints[i] = new NetEndPoint();
      hosts.endpoints[i].address = new InetSocketAddress(baseAddr.concat(
          String.valueOf((startAddr + i))), (basePort + i)
      );
    }
    System.out.println(hosts);
    
    XBean bean = XBeanBuilder.builder(hosts).create();
    System.out.println(bean.scanObject().setXmlIdentation("  ", 0).toXml());
    
    XFile xfile = new XFile(file, bean);
    System.out.printf("xfile.save()%n");
    xfile.save();
  }
  
  
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
  
  
  public static void main(String[] args) throws IOException {
    String file = "/home/juno/xfile.xml";
    
    //save(file);
    /*
    System.out.println("----------------------------");
    XFile xfile = new XFile(file);
    XTag tag = xfile.read();
    System.out.println(tag.toXml());
    
    XBean bean = new XBean(tag, new Hosts());
    System.out.println(bean.bindAll().scanXml());
    */
    System.out.println("----------------------------");
    Wrapper wp = wrapper();
    fillWrapperList(3, wp);
    System.out.println("* wp -->"+ wp);
    
    XBean bean = XBeanBuilder.builder(wp)
        .setAttributeByDefault(true)
        .create();
    System.out.println("* xml -->");
    Timer tm = new Timer.Nanos().start();
    String sxml = bean.scanObject().toXml();
    tm.stop();
    System.out.println("* xml length: "+ sxml.length());
    System.out.println("* time encoding to xml: "+ tm);
    
    String file2 = "/home/juno/xf.xml";
    XFile xf = new XFile(file2, bean.setXmlIdentation("  ", 0));
    System.out.println("* xf.save()");
    xf.save();
    
    xf = new XFile(file2);
    tm.clear().start();
    XTag tag = xf.read();
    tm.lapAndStop();
    System.out.println("* time parsing xml file: "+ tm);
    
    bean = XBeanBuilder.builder(new Wrapper())
        .fromTag(tag).create();
    tm.clear().start();
    Object o = bean.scanXml();
    tm.stop();
    System.out.println("* time XBean bind and scan xml: "+ tm);
    System.out.println("* wp -->"+ bean.scanXml());
  }
  
}
