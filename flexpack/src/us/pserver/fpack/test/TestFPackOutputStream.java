/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca e um software livre; voce pode redistribui-la e/ou modifica-la sob os
 * termos da Licenca Publica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versao 2.1 da Licenca, ou qualquer
 * versao posterior.
 * 
 * Esta biblioteca eh distribuida na expectativa de que seja util, porem, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE
 * OU ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto
 * com esta biblioteca; se nao, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.fpack.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import us.pserver.fpack.FPackEntry;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class TestFPackOutputStream {

  
  public static void main(String[] args) throws IOException {
    FPackEntry e = new FPackEntry("log.xml");
    Path p = Paths.get("/storage/log.xml");
    e.setSize(Files.size(p));
    
  }
  
}
