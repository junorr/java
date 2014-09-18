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
import com.jpower.tn3270.PasswordField;
import com.jpower.tn3270.Path;
import com.jpower.tn3270.Session;
import com.jpower.tn3270.Task;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 31/07/2013
 */
public class PathTest {

  
  public static void main(String[] args) {
    Session ses = new Session()
        .setDefaultTimeout(10);
    ses.connect("172.17.78.220", 8023);
    Path pth = new Path()
        .setDelayBetweenTasks(500);
    
    pth.add(new Task().setControlField(
        new Field(20, 39, "SISBB"))
        .setKey(Key.ENTER));
    
    pth.add(new Task().setControlField(
        new Field(1, 3, "CIC"))
        .add(new Field(13, 21, "F6036477"))
        .add(new PasswordField(14, 21, "OTg3NjU0OTg="))
        .add(new Field(15, 21, "PESSOAL"))
        .setKey(Key.ENTER));
    
    pth.add(new Task().setControlField(
        new Field(1, 2, "BB68"))
        .add(new Field(21, 20, "31"))
        .setKey(Key.ENTER));
    
    pth.add(new Task().setControlField(
        new Field(1, 3, "P1001"))
        .setKey(Key.PF8));
    
    pth.add(new Task().setControlField(
        new Field(6, 3, "32.   Consultar Registro"))
        .add(new Field(21, 20, "32"))
        .setKey(Key.ENTER));
    
    pth.add(new Task().setControlField(
        new Field(1, 3, "P6956"))
        .add(new Field(16, 19, 5)));
    
    ses.execute(pth);
    ses.close();
    
    System.out.println("* Entrada='"
        + pth.findField(16, 19, 5).getContent()+ "'");
  }
  
}
