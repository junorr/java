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

import us.pserver.xprops.XID;
import us.pserver.xprops.XTag;
import us.pserver.xprops.XValue;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/07/2015
 */
public class TestXml1 {

  
  public static void main(String[] args) {
    XTag root = new XTag("log");
    root.addNewChild("class")
        .addChild(new XValue("java.lang.Exception"));
    root.addNewChild("level")
        .addNewChild("info")
        .addNewValue("true");
    root.addNewAttr("attr", "value");
    System.out.println(root.setXmlIdentation("  ", 0).toXml());
    
    String id = "log.level";
    System.out.println("root.find(\""+ id+ "\"): "+ root.find(new XID(id), false));
    
    id = "log.attr";
    System.out.println("root.find(\""+ id+ "\"): "+ root.find(new XID(id), true));
    
    id = "level";
    System.out.println("root.find(\""+ id+ "\"): "+ root.find(new XID(id), true));
    
    id = "log.level.info";
    System.out.println("root.find(\""+ id+ "\"): "+ root.find(id, true));
    System.out.println(root.findOne(new XID(id), true).id());
  }
  
}
