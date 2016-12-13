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

package tests.of.tests;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import oodb.tests.beans.FSize;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2016
 */
public class TestISize {

  
  public static void main(String[] args) throws IOException {
    //Path path = Paths.get("D:\\javadoc\\jdk-8-api\\api\\constant-values.html");
    Path path = Paths.get("/home/juno/nb/disecLib/dist/disecLib.jar");
    FSize size = FSize.of(path);
    System.out.println("bytes="+ size.bytes());
    System.out.println("unit="+ size.unit());
    System.out.println(size.unit().prev() + " < " + size.unit() + " > " + size.unit().next());
    System.out.println(size);
    
    System.out.println(" ----------------- ");

    //path = Paths.get("D:\\javadoc\\jdk-8-api\\api");
    path = Paths.get("/home/juno/nb/disecLib/dist");
    size = FSize.of(path);
    System.out.println("bytes="+ size.bytes());
    System.out.println("unit="+ size.unit());
    System.out.println(size.unit().prev() + " < " + size.unit() + " > " + size.unit().next());
    System.out.println(size);
  }
  
}
