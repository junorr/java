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

package us.pserver.tar;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/10/2015
 */
public class TarStreamData implements TarData {

  
  private final InputStream stream;
  
  private final String name;
  
  private TarArchiveEntry entry;
  
  
  public TarStreamData(InputStream in, String name) {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(name).forEmpty().fail("Invalid name: ");
    stream = in;
    this.name = name;
  }


  @Override
  public InputStream getData() throws IOException {
    return stream;
  }
  
  
  public TarStreamData setEntry(TarArchiveEntry entry) {
    this.entry = entry;
    return this;
  }


  @Override
  public TarArchiveEntry getEntry() {
    if(entry != null) return entry;
    entry = new TarArchiveEntry(name);
    entry.setModTime(System.currentTimeMillis());
    try { 
      int avl = stream.available();
      if(avl > 1) entry.setSize(avl); 
    } catch(IOException e) {}
    return entry;
  }
  
  
}
