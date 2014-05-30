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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/12/2013
 */
public class LoginManager {

  public static final String DEFAULT_CONFIG_FILE = "./redfs.login";
  
  public static final String COMMENT = "RedFS Login";
  
  
  private Config conf;
  
  
  public LoginManager() {
    conf = new Config(DEFAULT_CONFIG_FILE);
    conf.setComment(COMMENT);
    conf.save();
  }
  
  
  public boolean createLogin(String user, String pass) {
    if(user == null || pass == null
        || user.isEmpty() || pass.isEmpty())
      return false;
    conf.put(user, pass.getBytes());
    return conf.save();
  }
  
  
  public boolean containsLogin(String user) {
    if(user == null || user.isEmpty())
      return false;
    conf.load();
    return conf.contains(user);
  }
  
  
  public boolean authenticate(String user, String pass) {
    if(user == null || pass == null
        || user.isEmpty() || pass.isEmpty()
        || !conf.contains(user))
      return false;
    conf.load();
    byte[] bs = conf.getBytes(user);
    String orig = new String(bs);
    return orig.equals(pass);
  }
  
  
  public static void main(String[] args) {
    LoginManager lm = new LoginManager();
    System.out.println("* lm.createLogin(\"juno\", \"inadonuj\"): "
        + lm.createLogin("juno", "inadonuj"));
    System.out.println("* lm.containsLogin(\"juno\"): "
        + lm.containsLogin("juno"));
    System.out.println("* lm.authenticate(\"juno\", \"iNadonuj\"): "
        +lm.authenticate("juno", "iNadonuj"));
    System.out.println("* lm.authenticate(\"juno\", \"inadonuj\"): "
        +lm.authenticate("juno", "inadonuj"));
  }
  
}
