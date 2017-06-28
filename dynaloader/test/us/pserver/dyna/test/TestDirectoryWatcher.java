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

package us.pserver.dyna.test;

import java.nio.file.Paths;
import us.pserver.dyna.DirectoryWatcher;
import us.pserver.dyna.impl.DirectoryWatcherImpl;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/06/2017
 */
public class TestDirectoryWatcher {

  public static void main(String[] args) throws InterruptedException {
    Reflector ref = new Reflector(Object.class);
    DirectoryWatcher dw = new DirectoryWatcherImpl(Paths.get("/home/juno/watch/"));
    dw.addChangeListener(d->System.out.println("* Classpath updated!"));
    System.out.println("* starting DirectoryWatcher...");
    dw.start();
    System.out.println("  [OK]");
    String cls = "br.com.bb.disec.micro.Main";
    System.out.println("* Creating "+ cls);
    Class cl = dw.getDynaLoader().load(cls);
    ref = new Reflector(cl);
    Object uid = ref.selectField("serialVersionUID").get();
    System.out.println("* cls: "+ cls+ ", serialVersionUID: "+ uid);
    cl = null;
    System.out.println("* Sleeping for 15s...");
    Thread.sleep(15000);
    System.out.println("* Creating "+ cls);
    cl = dw.getDynaLoader().load(cls);
    ref = new Reflector(cl);
    uid = ref.selectField("serialVersionUID").get();
    System.out.println("* cls: "+ cls+ ", serialVersionUID: "+ uid);
    System.out.println("* stopping DirectoryWatcher...");
    dw.stop();
    System.out.println("  [OK]");
  }

}
