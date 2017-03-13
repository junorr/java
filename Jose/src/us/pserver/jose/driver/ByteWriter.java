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

package us.pserver.jose.driver;

import java.nio.ByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/03/2017
 */
public interface ByteWriter<T> extends ByteReader<T> {
  
  public ByteWriter<T> seek(int pos);
  
  public ByteWriter<T> write(byte[] bs, int off, int len);

  public ByteWriter<T> write(T t);
  
  
  
  public static ByteWriter<byte[]> of(ByteBuffer buf) {
    return new ByteWriterImpl(buf);
  }
  
  
  
  
  
  public static class ByteWriterImpl extends ByteReaderImpl implements ByteWriter<byte[]> {
    
    
    public ByteWriterImpl(ByteBuffer buf) {
      super(buf);
    }


    @Override
    public ByteWriter<byte[]> seek(int pos) {
      getBuffer().position(pos);
      return this;
    }


    @Override
    public ByteWriter<byte[]> write(byte[] bs, int off, int len) {
      if(bs != null && off >= 0 && off+len < bs.length) {
        getBuffer().put(bs, off, len);
      }
      return this;
    }


    @Override
    public ByteWriter<byte[]> write(byte[] t) {
      return this.write(t, 0, (t != null ? t.length : 0));
    }

  }
  
}
