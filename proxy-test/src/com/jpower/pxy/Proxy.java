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

package com.jpower.pxy;

import com.jpower.net.NetConfig;
import com.jpower.net.NioServer;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/07/2013
 */
public class Proxy {

  public static void main(String[] args) {
    NetConfig conf = new NetConfig();
    conf.setLogger(LogProvider.getLogger());
    conf.setAddress("*").setPort(6000);
    //conf.setAutoFilterActivated(true);
    conf.setBufferSize(4096);
    conf.setConfigureFileSaving(true);
    try {
      NioServer server = new NioServer(conf, new ServerHandler());
      server.startServer();
    } catch (IOException ex) {
      LogProvider.getLogger().fatal(ex);
    }
  }
  
}
