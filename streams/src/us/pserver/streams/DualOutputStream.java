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
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.nullarray;
import static us.pserver.chk.Checker.range;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/08/2014
 */
public class DualOutputStream extends OutputStream {

  private OutputStream flush;
  
  private ProtectedOutputStream out;
  
  
  public DualOutputStream() {
    out = null;
    flush = null;
  }
  
  
  public DualOutputStream(OutputStream os, ProtectedOutputStream pos) {
    nullarg(OutputStream.class, os);
    nullarg(ProtectedOutputStream.class, pos);
    flush = os;
    out = pos;
  }
  
  
  public DualOutputStream setFlushOutputStream(OutputStream os) {
    nullarg(OutputStream.class, os);
    flush = os;
    return this;
  }
  
  
  public DualOutputStream setPlainOutputStream(ProtectedOutputStream os) {
    nullarg(ProtectedOutputStream.class, os);
    out = os;
    return this;
  }
  
  
  public OutputStream getFlushOutputStream() {
    return flush;
  }
  
  
  public ProtectedOutputStream getPlainOutputStream() {
    return out;
  }
  
  
  public DualOutputStream forceClose() throws IOException {
    this.close();
    out.forceClose();
    return this;
  }
  
  
  @Override
  public void close() throws IOException {
    if(flush != null) {
      flush.flush();
      flush.close();
      flush = null;
    }
  }
  
  
  public DualOutputStream forceFlush() throws IOException {
    if(flush != null) {
      flush.flush();
      flush.close();
      flush = null;
    }
    out.flush();
    return this;
  }
  
  
  @Override
  public void flush() throws IOException {
    if(flush != null)
      flush.flush();
    out.flush();
  }


  @Override
  public void write(int b) throws IOException {
    if(flush != null) {
      flush.write(b);
    }
    else {
      writePlain(b);
    }
  }
  

  @Override
  public void write(byte[] bs) throws IOException {
    if(flush != null) {
      flush.write(bs);
    }
    else {
      writePlain(bs);
    }
  }
  

  @Override
  public void write(byte[] bs, int off, int len) throws IOException {
    range(off, 0, bs.length-1);
    range(len, 1, bs.length);
    if(flush != null) {
      flush.write(bs, off, len);
    }
    else {
      writePlain(bs, off, len);
    }
  }
  

  public DualOutputStream writePlain(int b) throws IOException {
    nullarg(OutputStream.class, out);
    out.write(b);
    return this;
  }
  

  public DualOutputStream writePlain(byte[] bs) throws IOException {
    nullarg(OutputStream.class, out);
    out.write(bs);
    return this;
  }
  

  public DualOutputStream writePlain(byte[] bs, int off, int len) throws IOException {
    range(off, 0, bs.length-1);
    range(len, 1, bs.length);
    nullarg(OutputStream.class, out);
    out.write(bs, off, len);
    return this;
  }
  
}
