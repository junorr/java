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

package us.pserver.zerojs.io;

import java.io.IOException;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/04/2016
 */
public class ChannelWriter extends Writer {
  
  private final WritableByteChannel channel;
  
  private final Charset charset;
  
  
  public ChannelWriter(WritableByteChannel wch, Charset cst) {
    if(wch == null) {
      throw new IllegalArgumentException(
          "WritableByteChannel must be not null"
      );
    }
    if(cst == null) {
      throw new IllegalArgumentException(
          "Charset must be not null"
      );
    }
    this.channel = wch;
    this.charset = cst;
  }
  
  
  public ChannelWriter(WritableByteChannel wch) {
    this(wch, Charset.forName("UTF-8"));
  }
  

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    CharBuffer chars = CharBuffer.allocate(len - off);
    channel.write(charset.encode(chars));
  }


  @Override
  public void flush() throws IOException {}


  @Override
  public void close() throws IOException {
    channel.close();
  }

}
