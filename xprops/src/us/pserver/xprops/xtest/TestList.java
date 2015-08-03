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

import java.awt.Color;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import us.pserver.xprops.converter.ListXConverter;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/07/2015
 */
public class TestList {

  
  public static void print(String str, Object ... args) {
    System.out.println(String.format(str, args));
  }
  
  
  public static void main(String[] args) {
    List<Double> dls = new ArrayList<>();
    for(int i = 0; i < 10; i++) {
      dls.add(Math.random() * 100);
    }
    print("={list}=> %s", dls);
    
    ListXConverter<Double> dxl = new ListXConverter(dls);
    print("={xl.type}=> %s", dxl.getType());
    print("={xl}=\\ %n%s", dxl.toXml());
    
    dls.clear();
    ListXConverter<Double> ddxl = new ListXConverter(dls);
    ddxl.fromXml(dxl);
    print("={list}=> %s", ddxl.getList());
    
    print("-------------------------------");
    List<Date> datels = new ArrayList<>();
    Calendar cal = Calendar.getInstance();
    for(int i = 0; i < 10; i++) {
      cal.set(Calendar.DAY_OF_MONTH, i+1);
      datels.add(cal.getTime());
    }
    print("={list}=> %s", datels);
    
    ListXConverter<Date> txl = new ListXConverter(datels);
    print("={xl.type}=> %s", txl.getType());
    print("={xl}=\\ %n%s", txl.toXml());
    
    ListXConverter<Date> dtxl = new ListXConverter(new ArrayList<Date>());
    datels = dtxl.fromXml(txl);
    print("={list}=> %s", datels);
    
    print("-------------------------------");
    List<File> fls = new ArrayList<>();
    for(int i = 0; i < 10; i++) {
      fls.add(new File("/tmp/f"+ i+ ".tmp"));
    }
    print("={list}=> %s", fls);
    
    ListXConverter<File> fxl = new ListXConverter(fls);
    print("={xl.type}=> %s", fxl.getType());
    print("={xl}=\\ %n%s", fxl.toXml());
    
    ListXConverter<File> dfxl = new ListXConverter(new LinkedList<File>());
    fls = dfxl.fromXml(fxl);
    print("={list}=> %s", fls);
    
    print("-------------------------------");
    List<Color> cls = new ArrayList<>();
    cls.add(Color.BLACK);
    cls.add(Color.BLUE);
    cls.add(Color.CYAN);
    cls.add(Color.DARK_GRAY);
    cls.add(Color.GRAY);
    cls.add(Color.GREEN);
    cls.add(Color.YELLOW);
    cls.add(Color.ORANGE);
    cls.add(Color.RED);
    cls.add(Color.WHITE);
    print("={list}=> %s", cls);
    
    ListXConverter<Color> cxl = new ListXConverter(cls);
    print("={xl.type}=> %s", cxl.getType());
    print("={xl}=\\ %n%s", cxl.toXml());
    
    ListXConverter<Color> dcxl = new ListXConverter(new LinkedList<Color>());
    cls = dcxl.fromXml(cxl);
    print("={list}=> %s", cls);
    
    print("-------------------------------");
    List<SocketAddress> sls = new ArrayList<>();
    String base = "172.24.75.";
    for(int i = 80; i < 90; i++) {
      sls.add(new InetSocketAddress(base+ i, i));
    }
    print("={list}=> %s", sls);
    
    ListXConverter<SocketAddress> sxl = new ListXConverter(sls);
    print("={xl.type}=> %s", sxl.getType());
    print("={xl}=\\ %n%s", sxl.toXml());
    
    ListXConverter<SocketAddress> dsxl = new ListXConverter(new LinkedList<SocketAddress>());
    sls = dsxl.fromXml(sxl);
    print("={list}=> %s", sls);
    
    print("-------------------------------");
  }

  
}
