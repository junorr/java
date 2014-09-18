/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package com.jpower.sockets;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/05/2013
 */
public class Main {

  
  public static void main(String[] args) {
    if(args.length < 2) {
      System.out.println("# Invalid args!");
      System.out.println("* Usage: sockets.jar <mode:{-server|-client}> [controllWord] [address:port] [proxyAddress:proxyPort]");
      System.exit(1);
    }
    String mode = args[0];
    String word = args[1];
    
    if(mode.equals("-server")) {
      String address = null;
      int port = 0;
      String cword = args[1];
      if(args.length > 2) {
        address = args[2].split(":")[0];
        port = Integer.parseInt(args[2].split(":")[1]);
      }
      Server s = new Server(address, port, cword);
      s.start();
    }
    else if(mode.equals("-client")) {
      String address = null;
      int port = -1;
      String padd = null;
      int pport = -1;
      if(args.length >= 2) {
        address = args[1].split(":")[0];
        port = Integer.parseInt(args[1].split(":")[1]);
      }
      if(args.length >= 3) {
        padd = args[2].split(":")[0];
        pport = Integer.parseInt(args[2].split(":")[1]);
      }
      if(args.length < 2) {
        System.out.println("# Invalid args!");
        System.out.println("* Usage: sockets.jar <mode:{-server|-client}> <controllWord> [address:port] [proxyAddress:proxyPort]");
        System.exit(1);
      }
      Client c = new Client(padd, pport, address, port);
      c.start();
    }
    else {
      System.out.println("# Invalid mode!");
      System.out.println("* Usage: sockets.jar <mode:{-server|-client}> <controllWord> [address:port] [proxyAddress:proxyPort]");
      System.exit(1);
    }
  }
  
}
