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

package us.pserver.tools.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.tools.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/10/2018
 */
public class TestInputStreamView {

  private final String source = "Direitos Autorais Reservados (c) 2011 Juno Roesler." +
      " Esta biblioteca e software livre; voce pode redistribui-la e/ou modifica-la sob os" +
      " termos da Licença Publica Geral Menor do GNU conforme publicada pela Free" +
      " Software Foundation; tanto a versão 2.1 da Licença, ou qualquer" +
      " versao posterior." +
      " Esta biblioteca e distribuida na expectativa de que seja util, porem, SEM" +
      " NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE" +
      " OU ADEQUAÇAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica" +
      " Geral Menor do GNU para mais detalhes." +
      " Você deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto" +
      " com esta biblioteca; se nao, acesse" +
      " http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html," +
      " ou escreva para a Free Software Foundation, Inc., no" +
      " endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.";
  
  
  private InputStream newSourceInputStream() {
    return new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testSearch() throws IOException {
    InputStreamView view = new InputStreamView(newSourceInputStream());
    long readed = view.search(Buffer.heapFactory().create("livre;"));
    Assertions.assertEquals(79, readed);
  }
  
  @Test
  public void testSkip() throws IOException {
    InputStreamView view = new InputStreamView(newSourceInputStream());
    long readed = view.search(Buffer.heapFactory().create("livre;"));
    Assertions.assertEquals(79, readed);
    view.skip(6);
    readed = view.search(Buffer.heapFactory().create("-la"));
    Assertions.assertEquals(22, readed);
  }
  
  @Test
  public void testFlushUntil() throws IOException {
    InputStreamView view = new InputStreamView(newSourceInputStream());
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    long readed = view.search(Buffer.heapFactory().create("livre;"));
    Assertions.assertEquals(79, readed);
    view.skip(6);
    readed = view.flushUntil(bos, Buffer.heapFactory().create("-la"));
    Assertions.assertEquals(22, readed);
    Assertions.assertEquals(" voce pode redistribui", bos.toString(StandardCharsets.UTF_8.name()));
  }
  
  @Test
  public void testCacheUntil() throws IOException {
    InputStreamView view = new InputStreamView(newSourceInputStream());
    long readed = view.search(Buffer.heapFactory().create("livre;"));
    Assertions.assertEquals(79, readed);
    view.skip(6);
    Buffer b = view.cacheUntil(Buffer.heapFactory().create("-la"));
    Assertions.assertEquals(22, b.readLength());
    Assertions.assertEquals(" voce pode redistribui", b.getContentAsString());
  }
  
  @Test
  public void testExtract() throws IOException {
    InputStreamView view = new InputStreamView(newSourceInputStream());
    Buffer c1 = Buffer.heapFactory().create("SEM ");
    Buffer c2 = Buffer.heapFactory().create(" GARANTIA");
    Buffer extract = view.extract(c1, c2);
    Logger.debug("buffer: %s", extract);
    Logger.debug("buffer content: '%s'", extract.getContentAsString());
    Assertions.assertEquals(7, extract.readLength());
    Assertions.assertEquals("NENHUMA", extract.getContentAsString());
  }
  
}
