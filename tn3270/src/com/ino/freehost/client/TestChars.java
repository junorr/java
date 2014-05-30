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

package com.ino.freehost.client;

import us.pserver.tn3270.Cursor;
import us.pserver.tn3270.Field;
import us.pserver.tn3270.Session;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 07/02/2014
 */
public class TestChars {

  
  public static void main(String[] args) {
    
    Session sess = new Session();
    sess.connect("3270.df.bb", 8023);
    System.out.println("* sess.isConnected(): "+ sess.isConnected());
    sess.waitFor(new Cursor(20, 39), "SISBB");
    sess.enter();
    
    sess.waitFor(new Cursor(1, 3), "CIC");/*
    sess.set(new Cursor(13, 21), "f6036477");
    sess.set(new Cursor(14, 21), "32132155");
    sess.set(new Cursor(15, 21), "correio");
    sess.enter();
    
    sess.waitFor(new Cursor(1, 2), "COEP", 1000);
    sess.waitFor(new Cursor(22, 56), "Acesso", 1000);
    sess.set(new Cursor(21, 17), "01");
    sess.enter();
    
    sess.waitFor(new Cursor(1, 3), "COEM", 1000);
    sess.set(new Cursor(21, 18), "23");
    sess.enter();
    
    sess.waitFor(new Cursor(18, 3), "Informe", 1000);
    sess.set(new Cursor(18, 24), "01");
    sess.enter();
    
    sess.waitFor(new Cursor(8, 7), "2014/08267356", 1000);
    sess.set(new Cursor(21, 28), "01");
    sess.enter();
    
    sess.waitFor(new Cursor(5, 22), "2014/08267356", 1000);
    */
    System.out.println(sess.getScreen());
    System.out.println("----------------------");
    
    Field[] fls = sess.getFields();
    for(Field f : fls) {
      System.out.println(f);
      if(f == null) continue;
      System.out.println("  - bold: "+ f.isBold());
      System.out.println("  - hidden: "+ f.isHidden());
      System.out.println("  - protected: "+ f.isProtected());
    }
    
    RW3270Char[] chars = sess.internal().getDataBuffer();
    for(RW3270Char c : chars) {
      System.out.print(c.toString());
      if(c.getPosition() % 80 == 0)
        System.out.println();
    }
    
    sess.close();
  }
  
}
