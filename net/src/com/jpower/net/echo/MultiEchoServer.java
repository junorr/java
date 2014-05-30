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

package com.jpower.net.echo;

import com.jpower.net.NetConfig;
import com.jpower.net.NioClient;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/05/2013
 */
public class MultiEchoServer {
  
  
  private static void printUsage() {
    System.out.println("* Usage: net <mode> [n]");
    System.out.println("   mode: {-server | -sclient | -mclient <n>}");
    System.out.println("   -mclient <n>: Number of instances created (n > 0).");
    System.exit(1);
  }
  
  
  public static void main(String[] args) {
    if(args.length < 2) {
      printUsage();
    }
    int n = 0;
    try {
      n = Integer.parseInt(args[1]);
    } catch(NumberFormatException e) {
      printUsage();
    }
    
    NetConfig conf = new NetConfig();
    conf.setLogger(EchoClientHandler.log);
    conf.setAddress("localhost");
    conf.setPort(2000);
    try {
      NioClient client = null;
      for(int i = 0; i < n; i++) {
        client = new NioClient(conf, new EchoClientHandler(i));
        client.connect().start();
      }
   } catch(IOException ex) {
      EchoClientHandler.log.fatal(ex);
      System.exit(1);
    }
  }
  
}
