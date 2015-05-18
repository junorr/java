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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/04/2015
 */
public class MixedBufferOutputStream extends OutputStream {

  private MixedWriteBuffer buffer;
  
  
  public MixedBufferOutputStream(MixedWriteBuffer buf) {
    if(buf == null)
      throw new IllegalArgumentException(
          "[MixedBufferOutputStream( MixedWriteBuffer )] Invalid MixedWriteBuffer: '"+ buf+ "'");
    buffer = buf;
  }
  
  
  @Override public void close() {}
  
  @Override public void flush() {}
  
  
  @Override
  public void write(int b) throws IOException {
    buffer.write(b);
  }
  
  
  @Override
  public void write(byte[] bs, int off, int len) throws IOException {
    buffer.write(bs, off, len);
  }
  
  
  @Override
  public void write(byte[] bs) throws IOException {
    buffer.write(bs);
  }
  
}
