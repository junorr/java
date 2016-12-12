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

package oodb.tests.beans;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import oodb.tests.beans.ISize;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2016
 */
public class FileSize implements ISize {

  private final long bytes;
  
  
  public FileSize(long bytes) {
    this.bytes = bytes;
  }
  
  
  public FileSize(ISize size) {
    this.bytes = size.bytes();
  }
  
  
  @Override
  public long bytes() {
    return bytes;
  }


  @Override
  public double value() {
    Unit unit = Unit.from(bytes);
    return Long.valueOf(bytes).doubleValue() / unit.bytes();
  }


  @Override
  public Unit unit() {
    return Unit.from(bytes);
  }
  
  
  @Override
  public String toString() {
    return new DecimalFormat("0.0###").format(value())+ " "+ unit().name();
  }
  
  
  public static FileSize of(long bytes) {
    return new FileSize(bytes);
  }
  
  
  public static FileSize of(Path path) {
    FileSize size = of(Long.MIN_VALUE);
    try { size = of(Files.size(path)); } 
    catch(IOException e) {}
    return size;
  }
  
}
