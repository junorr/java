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

package us.pserver.redfs;

import com.jpower.conf.Config;
import java.io.IOException;
import us.pserver.rob.NetConnector;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/12/2013
 */
public class RedFSConfig {

  public static final String KEY_PORT = "PORT";
  
  public static final String CONFIG_FILE = "./redfs.conf";
  
  public static final String COMMENT = "RedFS";
  
  
  private int port;
  
  private Config conf;
  
  
  public RedFSConfig() {
    port = NetConnector.DEFAULT_PORT;
    conf = new Config(CONFIG_FILE);
    if(conf.contains(KEY_PORT))
      this.load();
    else
      this.save();
  }


  public int getPort() {
    return port;
  }


  public RedFSConfig setPort(int port) {
    this.port = port;
    this.save();
    return this;
  }


  public NetConnector getNetConnector() {
    this.load();
    return new NetConnector().setPort(port);
  }
  
  
  public void save() {
    conf.put(KEY_PORT, port);
    conf.setComment(COMMENT);
    conf.save();
  }
  
  
  public void load() {
    conf.load();
    if(conf.contains(KEY_PORT))
      port = conf.getInt(KEY_PORT);
  }
  
}
