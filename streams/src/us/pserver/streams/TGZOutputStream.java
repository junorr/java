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

package us.pserver.streams;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/10/2015
 */
public class TGZOutputStream extends FilterOutputStream {
  
  
  public static final int DEFAULT_BUFFER_SIZE = 8192;
  
  
  private Stream<Path> paths;
  
  private ByteBuffer buffer;
  
  private SeekableByteChannel currin;
  
  private TarArchiveEntry currentry;
  
  private TarArchiveOutputStream tar;
  
  private GZIPOutputStream gzo;


  public TGZOutputStream(OutputStream out, Stream<Path> paths) {
    super(Valid.off(out).getOrFail(OutputStream.class));
    this.paths = Valid.off(paths).forEmpty()
        .getOrFail(PathCollection.class);
  }
  
  
  public TGZOutputStream(OutputStream out, Path ... paths) {
    this(out, Arrays.asList(
        Valid.off(paths).forEmpty().getOrFail()
    ).stream());
  }
  
  
  private void init() throws IOException {
    
  }


}
