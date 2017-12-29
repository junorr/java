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

package us.pserver.test.mapping;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/12/2017
 */
public class TestPathContains {
  
  private final Path base = Paths.get("/storage");

  @Test public void isNotParent() throws IOException {
    Path other = Paths.get("../home/juno");
    Path another = base.resolve(other);
    System.out.printf("- isNotParent: isParent( %s, %s ): %s%n", 
        base.toAbsolutePath().toRealPath(LinkOption.NOFOLLOW_LINKS), 
        another.toAbsolutePath().toRealPath(LinkOption.NOFOLLOW_LINKS), 
        isParent(base, another)
    );
    Assertions.assertFalse(isParent(base, another));
  }
  
  @Test public void pathContains() throws IOException {
    Path other = Paths.get("./apps/linux");
    Path another = base.resolve(other);
    System.out.printf("- isParent: isParent( %s, %s ): %s%n", 
        base.toAbsolutePath().toRealPath(LinkOption.NOFOLLOW_LINKS), 
        another.toAbsolutePath().toRealPath(LinkOption.NOFOLLOW_LINKS), 
        isParent(base, another)
    );
    Assertions.assertTrue(isParent(base, another));
  }
  
  @Test public void resolve() {
    Path root = Paths.get("/");
    Path other = Paths.get("./storage");
    Path another = Paths.get("/storage");
    System.out.printf("- resolve: [%s].resolve( %s ): %s%n", root, other, root.relativize(other));
    System.out.printf("- resolve: [%s].resolve( %s ): %s%n", other, root, other.relativize(root));
  }
  
  private boolean isParent(Path parent, Path other) throws IOException {
    return other.toRealPath(LinkOption.NOFOLLOW_LINKS).startsWith(
        parent.toRealPath(LinkOption.NOFOLLOW_LINKS)
    );
  }
  
}
