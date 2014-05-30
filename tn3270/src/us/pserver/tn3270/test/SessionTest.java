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

package us.pserver.tn3270.test;

import us.pserver.tn3270.Field;
import us.pserver.tn3270.Key;
import us.pserver.tn3270.PasswordField;
import us.pserver.tn3270.Path;
import us.pserver.tn3270.Session;
import us.pserver.tn3270.Task;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 31/07/2013
 */
public class SessionTest {

  
  public static void main(String[] args) throws InterruptedException {
    Session ses = new Session()
        .connect("3270.df.bb", 8023);
    
    Path pt = new Path()
        .add(new Task(new Field(20, 39, "SISBB"), Key.ENTER))
        .add(new Task(new Field(1, 3, "CIC"), Key.ENTER)
            .add(new Field(13, 21, "F6036477"))
            .add(new PasswordField().setPassword("NjU0NjU0Nzc=").setCursor(14, 21))
            .add(new Field(15, 21, "pessoal")))
        .add(new Task(new Field(1, 2, "BB68"), Key.ENTER)
            .add(new Field(21, 20, "31")))
        .add(new Task(new Field(5, 28, "IRPF"), Key.PF3))
        .add(new Task(new Field(5, 3, "Consultar"), Key.PF8))
        .add(new Task(new Field(5, 3, "Ponto"), Key.ENTER)
            .add(new Field(21, 20, "32")))
        .add(new Task().setControlField(new Field(13, 28, "Registro"))
            .add(new Field(16, 19, 5).setProtected(true)));
            
    ses.execute(pt);
    System.out.println(ses.getScreenln());
    System.out.println();
    
    System.out.println("Entrada: "+ pt.findTaskByControl(
        "Registro").findField(16, 19, 5).getContent());
    
    ses.close();
  }
  
}
