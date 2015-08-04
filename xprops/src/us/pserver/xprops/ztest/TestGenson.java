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

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import us.pserver.tools.timer.Timer;
import us.pserver.xprops.XBean;
import us.pserver.xprops.ztest.TestXmlFile.Wrapper;
import static us.pserver.xprops.ztest.TestXmlFile.wrapper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/08/2015
 */
public class TestGenson {

  
  public static void main(String[] args) {
    Wrapper w = wrapper();
    TestXmlFile.fillWrapperList(10, w);
    Genson gs = new GensonBuilder().useIndentation(true).create();
    Timer t = new Timer.Nanos().start();
    String js = gs.serialize(w);
    t.stop();
    System.out.println("* json: "+ js);
    System.out.println("* time to json: "+ t);
    t.clear().start();
    w = gs.deserialize(js, Wrapper.class);
    t.stop();
    //System.out.println(w);
    System.out.println("* time from json: "+ t);
    
    XBean<Wrapper> bean = new XBean(w);
    t.clear().start();
    String xml = bean.bindAll().scanObject().toXml();
    t.stop();
    System.out.println("* total time to xml: "+ t);
    
    bean = new XBean(w);
    t.clear().start();
    bean.bindAll();
    t.stop();
    System.out.println("* bind all time: "+ t);
    
    bean = new XBean(w);
    t.clear().start();
    bean.scanObject();
    t.stop();
    System.out.println("* scan object time: "+ t);
    
    bean = new XBean(w);
    bean.bindAll().scanObject();
    t.clear().start();
    bean.bindAll();
    t.lap();
    bean.scanObject();
    t.lap();
    xml = bean.toXml();
    t.stop();
    System.out.println(xml);
    System.out.println("* time to xml: "+ t);
    
    bean = new XBean(bean, new Wrapper());
    t.clear().start();
    bean.bindAll();
    t.lap();
    w = bean.scanXml();
    t.stop();
    System.out.println("* time from xml: "+ t);
  }
  
}
