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

package com.jpower.tn3270.test;

import com.jpower.tn3270.Field;
import com.jpower.tn3270.Key;
import com.jpower.tn3270.Path;
import com.jpower.tn3270.Session;
import com.jpower.tn3270.Task;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 31/07/2013
 */
public class SessionTest {

  
  public static void main(String[] args) throws InterruptedException {
    Session ses = new Session()
        .connect("localhost", 8023);
        //.connect("172.17.78.220", 8023);
    //System.out.println(
        //"* ses.waitFor(new Field(20, 39, \"SISBB\"), 5): "
        //+ ses.waitFor(new Field(20, 39, "SISBB"), 5));
    ses.cursor(20, 39).waitFor("SISBB", 5);
    //ses.execute(new Path().add(new Task(
    //    new Field(20, 39, "SISBB"), Key.ENTER)));
    System.out.println(ses.getScreen());
    ses.close();
  }
  
}
