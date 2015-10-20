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
 * @version 0.0 - 19/10/2015
 */
public class TarEmptyData implements TarData {
  
  
  private String name;
  
  private TarArchiveEntry entry;
  
  
  public TarEmptyData(String name) {
    this.name = Valid.off(name)
        .forEmpty().getOrFail("Invalid name: ");
  }
  
  
  @Override
  public InputStream getData() throws IOException {
    return null;
  }
  
  
  public TarEmptyData setEntry(TarArchiveEntry e) {
    this.entry = e;
    return this;
  }


  @Override
  public TarArchiveEntry getEntry() {
    if(entry == null) {
      entry = new TarArchiveEntry(name);
      entry.setModTime(System.currentTimeMillis());
    }
    return entry;
  }

}
