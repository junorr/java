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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/10/2015
 */
public class TBZReader extends TarReader {


  public TBZReader(InputStream in) throws IOException {
    super(new BZip2CompressorInputStream(new BufferedInputStream(in)));
  }

  
  public static void main(String[] args) throws IOException {
    String src = "D:/test2.tar.bz";
    TarReader tr = new TBZReader(new FileInputStream(src));
    while(tr.hasNext()) {
      System.out.println("- "+ tr.next().getEntry().getName());
    }
    tr.close();
    System.out.println("--------------------");
    tr = new TarReader(new FileInputStream(src));
    tr.untar("D:/tbzReader").close();
  }
  
  
}
