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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static us.pserver.chk.Checker.nullarg;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/08/2014
 */
public class ProtectedInputStream extends FilterInputStream {

  private InputStream input;
  
  
  public ProtectedInputStream(InputStream is) {
    super(is);
    setInputStream(is);
  }
  
  
  public ProtectedInputStream setInputStream(InputStream is) {
    nullarg(InputStream.class, is);
    input = is;
    return this;
  }
  
  
  public InputStream getInputStream() {
    return input;
  }
  
  
  @Override
  public void close() throws IOException {}
  
  
  public ProtectedInputStream forceClose() throws IOException {
    super.close();
    input.close();
    return this;
  }
  
}
