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

package us.pserver.tmo.main;

import javax.swing.JFrame;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteMethod;
import us.pserver.rob.RemoteObject;
import us.pserver.rob.container.ObjectContainer;
import us.pserver.rob.factory.DefaultFactoryProvider;
import us.pserver.rob.server.NetworkServer;
import us.pserver.tmo.MainGui;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 27/01/2015
 */
public class Main {
  
  public static final int PORT = 33233;
  
  private NetworkServer server;
  
  private MainGui gui;
  
  
  public Main() {
    ObjectContainer cont = new ObjectContainer();
    cont.put("Main", this);
    server = new NetworkServer(cont, 
        new NetConnector().setPort(PORT), 
        DefaultFactoryProvider
            .getSocketXmlChannelFactory());
    gui = new MainGui();
    gui.setLocationRelativeTo(null);
    gui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
  }
  
  
  public void start() {
    gui.start();
    server.start();
  }
  
  
  public void show() {
    java.awt.EventQueue.invokeLater(
        ()->gui.setVisible(true));
  }
  
  
  public static void main(String[] args) {
    RemoteObject rob = new RemoteObject(new NetConnector().setPort(PORT));
    try {
      rob.invokeVoid(new RemoteMethod("Main", "show"));
      return;
    } 
    catch(MethodInvocationException e) {}
    finally { rob.close(); }
    
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception ex) {}
    
    Main m = new Main();
    m.show();
    m.start();
  }
  
}
