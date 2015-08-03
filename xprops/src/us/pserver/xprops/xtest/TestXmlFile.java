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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import us.pserver.xprops.XBean;
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
    
    XBean bean = new XBean(hosts);
    System.out.println(bean.bindAll().scanObject().setXmlIdentation("  ", 0).toXml());
    
    XFile xfile = new XFile(file, bean);
    System.out.printf("xfile.save(): %s%n", xfile.save());
  }
  
  
  public static class Wrapper {
    Integer _int;
    String _str;
    Double _dbl;
    Wrapper _wrp;
    byte[] _bts;
    Date[] _dts;
    public String toString() {
      return "\nWrapper {\n  _int="+ _int
          + "\n  _str="+ _str
          + "\n  _dbl="+ _dbl
          + "\n  _bts="+ Arrays.toString(_bts)
          + "\n  _dts="+ Arrays.toString(_dts)
          + "\n  _wrp="+ _wrp
          + "\n}";
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
    Wrapper wp = new Wrapper();
    wp._bts = new byte[]{0,1,2,3,4,5,6,7,8,9};
    wp._dbl = 11.987654321;
    wp._dts = new Date[]{new Date(),new Date()};
    wp._int = 5;
    wp._str = "Hello Wrapper";
    wp._wrp = new Wrapper();
    wp._wrp._bts = wp._bts;
    wp._wrp._dbl = wp._dbl;
    wp._wrp._dts = wp._dts;
    wp._wrp._int = wp._int;
    wp._wrp._str = wp._str;
    System.out.println("* wp -->"+ wp);
    
    XBean bean = new XBean(wp);
    System.out.println("* xml -->");
    System.out.println(bean.bindAll().scanObject().toXml());
    
    String file2 = "/home/juno/xf.xml";
    XFile xf = new XFile(file2, bean.setXmlIdentation("  ", 0));
    System.out.println("* xf.save(): "+ xf.save());
    
    xf = new XFile(file2);
    XTag tag = xf.read();
    System.out.println("* tag readed: "+ tag.toXml());
    
    bean = new XBean(tag, new Wrapper());
    System.out.println("* wp -->"+ bean.bindAll().scanXml());
  }
  
}
