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

package us.pserver.cdr.lzma;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;
import lzma.streams.LzmaOutputStream;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 * Classe utilitária para criação de streams
 * de dados no formato LZMA.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 26/06/2014
 */
public class LzmaStreamFactory {
  
  
  /**
   * Cria um objeto <code>LzmaInputStream</code> para
   * descompactação de dados a partir de um byte array.
   * @param buf byte array de onde os dados serão lidos.
   * @return <code>LzmaInputStream</code> para
   * descompactação de dados.
   */
  public static LzmaInputStream createLzmaInput(byte[] buf) {
    Sane.of(buf).check(Checkup.isNotEmptyArray());
    try {
      return new LzmaInputStream(new ByteArrayInputStream(buf), new Decoder());
    } catch(IOException e) {
      throw new IllegalStateException(e);
    }
  }
  
  
  /**
   * Cria um objeto <code>LzmaInputStream</code> para
   * descompactação de dados a partir de outro objeto
   * <code>InputStream</code>.
   * @param in <code>InputStream</code> de onde os 
   * dados serão lidos e descompactados.
   * @return <code>LzmaInputStream</code> para
   * descompactação de dados.
   */
  public static LzmaInputStream createLzmaInput(InputStream in) {
    Sane.of(in).check(Checkup.isNotNull());
    try {
      return new LzmaInputStream(in, new Decoder());
    } catch(IOException e) {
      throw new IllegalStateException(e);
    }
  }
  
  
  /**
   * Cria um objeto <code>LzmaOutputStream</code> para
   * compactação de dados para outro objeto
   * <code>OutputStream</code>.
   * @param out <code>OutputStream</code> para onde os 
   * dados compactados serão escritos.
   * @return <code>LzmaOutputStream</code> para
   * compactação de dados.
   */
  public static LzmaOutputStream createLzmaOutput(OutputStream out) {
    Sane.of(out).check(Checkup.isNotNull());
    try {
      return new LzmaOutputStream.Builder(out).build();
    } catch(IOException e) {
      throw new IllegalStateException(e);
    }
  }


}
