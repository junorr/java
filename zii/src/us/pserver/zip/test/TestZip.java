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

package us.pserver.zip.test;

import us.pserver.zip.utils.DirScan;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import us.pserver.listener.SimpleListener;
import us.pserver.zip.Zip;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/08/2014
 */
public class TestZip {

  
  public static void main(String[] args) throws IOException {
    Path dir = Paths.get("d:/java/zipper");
    DirScan ds = new DirScan(dir);
    System.out.println("* ds.scan(false):");
    List<Path> ls = ds.scan(false);
    ls.forEach(System.out::println);
    
    System.out.println("------------------");
    System.out.println("* ds.scan(true):");
    ls = ds.scan(true);
    ls.forEach(System.out::println);
    /*
    System.out.println("------------------");
    new Zip().add("c:/.local/testzip")
        .output("c:/.local/test.zip")
        .addListener(new SimpleListener())
        .run();
    */
    
    System.out.println("------------------");
    new Zip().add(dir)
        .output("d:/zipper.zip")
        .addListener(new SimpleListener())
        .run();
    
  }
  
}
