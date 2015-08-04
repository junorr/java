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

import us.pserver.log.conf.OutputConf;
import us.pserver.log.output.StandardOutput;
import us.pserver.xprops.XBean;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/08/2015
 */
public class TestXml {

  
  public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
    OutputConf out = new OutputConf("stdout", StandardOutput.class);
    XBean bean = new XBean(out);
    System.out.println(bean.bindAll().scanObject().setXmlIdentation("  ", 0).toXml());
  }
  
}
