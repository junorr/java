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

package br.com.bb.disec.micros.test;

import br.com.bb.disec.micro.ResourceLoader;
import br.com.bb.disec.micro.Server;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/09/2016
 */
public class TestResourceLoader {

  
  public static void main(String[] args) throws IOException {
    String resource = "/resources/serverconf.json";
    ResourceLoader local = new ResourceLoader(TestResourceLoader.class);
    ResourceLoader other = new ResourceLoader(Server.class);
    System.out.println("* Local serverconf.json:");
    System.out.println(local.loadContentString(resource));
    System.out.println();
    
    System.out.println("* Original serverconf.json:");
    System.out.println(other.loadContentString(resource));
  }
  
}
