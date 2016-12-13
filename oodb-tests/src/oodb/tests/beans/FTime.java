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

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public class FTime implements IFTime {
  
  private final Instant create;
  
  private final Instant modified;
  
  private final Instant access;
  
  
  public FTime(FileTime cre, FileTime mod, FileTime acc) {
    Objects.requireNonNull(cre, "Bad Null Create FileTime");
    Objects.requireNonNull(mod, "Bad Null Modified FileTime");
    Objects.requireNonNull(acc, "Bad Null Access FileTime");
    this.create = cre.toInstant();
    this.modified = mod.toInstant();
    this.access = acc.toInstant();
  }
  

  public FTime(Instant cre, Instant mod, Instant acc) {
    Objects.requireNonNull(cre, "Bad Null Create FileTime");
    Objects.requireNonNull(mod, "Bad Null Modified FileTime");
    Objects.requireNonNull(acc, "Bad Null Access FileTime");
    this.create = cre;
    this.modified = mod;
    this.access = acc;
  }
  

  @Override
  public Instant getCreationTime() {
    return this.create;
  }


  @Override
  public Instant getLastModifiedTime() {
    return this.modified;
  }


  @Override
  public Instant getLastAccessTime() {
    return this.access;
  }

}
