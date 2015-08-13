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

package us.pserver.log.test;

import us.pserver.log.LogLevel;
import us.pserver.log.conf.XOutput;
import us.pserver.log.output.StandardOutput;
import us.pserver.xprops.XBean;
import us.pserver.xprops.XBeanBuilder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 06/08/2015
 */
public class TestConfig {

  
  public static void main(String[] args) {
    XOutput out = new XOutput("hello", StandardOutput.class);
    out.getLevels().setLevelEnabled(LogLevel.INFO, true);
    XBeanBuilder.DEFAULT_ATTR_FIELDS = true;
    XBean bean = XBeanBuilder.builder(out).create();
    bean.setXmlIdentation("  ", 0);
    System.out.println(bean.scanObject().toXml());
    bean = XBeanBuilder.builder(
        new XOutput(), bean).create();
    //bean.setAttributeByDefault(true);
    System.out.println(bean.scanXml());
  }
  
}
