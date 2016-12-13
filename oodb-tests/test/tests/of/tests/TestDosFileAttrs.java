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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public class TestDosFileAttrs {

  
  public static void main(String[] args) throws IOException {
    //Path path = Paths.get("/home/juno/nb/disecLib/dist/disecLib.jar");
    Path path = Paths.get("D:/videos/porn/anal.mp4");
    DosFileAttributes attrs = Files.readAttributes(path, DosFileAttributes.class);
    System.out.println("* path="+ path);
    System.out.println("* isDirectory? "+ attrs.isDirectory());
    System.out.println("* isReadOnly? "+ attrs.isReadOnly());
    System.out.println("* created: "+ attrs.creationTime());
    System.out.println("* modified: "+ attrs.lastModifiedTime());
    System.out.println("* accessed: "+ attrs.lastAccessTime());
  }
  
}
