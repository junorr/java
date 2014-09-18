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

package us.pserver.j3270;

import us.pserver.tn3270.Cursor;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 19/02/2014
 */
public class TestJDriver {

  
  public static void main(String[] args) {
    J3270 js = new J3270();
    JDriver jd = js.driver();
    js.setVisible(true);
    
    jd.delay(500);
    jd.connect("3270.df.bb", 8023);
    
    jd.delay(1000);
    jd.enter();
    jd.waitFor(new Cursor(1, 3), "CIC");
    jd.setText(new Cursor(13, 21), "f6036477");
    jd.setPassword(new Cursor(14, 21), "MzIxMzIxNTU=");
    jd.setText(new Cursor(15, 21), "cop");
    jd.enter();
    
    jd.waitFor(new Cursor(2, 21), "Portal");
    jd.select(new Cursor(13, 11), 32, 4);
    jd.status("Controlado pelo driver");
    jd.blinkStatus(5);
  }
  
}
