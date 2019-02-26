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

package us.pserver.orb.impl;

import java.util.Objects;
import us.pserver.orb.bind.MethodBind;
import us.pserver.orb.ds.DataSource;
import us.pserver.orb.parse.OrbParser;
import us.pserver.orb.OrbSource;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/02/2019
 */
public class OrbSourceImpl<T> implements OrbSource<T> {

  private final OrbParser<T> parser;
  
  private final DataSource<T> ds;
  
  private final MethodBind bind;
  
  private final int priority;
  
  
  public OrbSourceImpl(DataSource<T> ds, OrbParser<T> ps, MethodBind mb, int priority) {
    this.ds = Objects.requireNonNull(ds);
    this.parser = Objects.requireNonNull(ps);
    this.bind = Objects.requireNonNull(mb);
    this.priority = priority;
  }
  
  public OrbSourceImpl(DataSource<T> ds, OrbParser<T> ps, MethodBind mb) {
    this(ds, ps, mb, 0);
  }


  @Override
  public DataSource<T> dataSource() {
    return ds;
  }
  
  
  @Override
  public OrbParser<T> parser() {
    return parser;
  }
  
  
  @Override
  public MethodBind methodBind() {
    return bind;
  }


  @Override
  public int priority() {
    return priority;
  }


  @Override
  public int compareTo(OrbSource o) {
    return Integer.compare(priority, o.priority());
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.ds);
    hash = 97 * hash + Objects.hashCode(this.parser);
    hash = 97 * hash + Objects.hashCode(this.bind);
    hash = 97 * hash + this.priority;
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(obj == null) {
      return false;
    }
    if(getClass() != obj.getClass()) {
      return false;
    }
    final OrbSource<?> other = (OrbSource<?>) obj;
    if(this.priority != other.priority()) {
      return false;
    }
    if(!Objects.equals(this.parser, other.parser())) {
      return false;
    }
    if(!Objects.equals(this.ds, other.dataSource())) {
      return false;
    }
    return Objects.equals(this.bind, other.methodBind());
  }


  @Override
  public String toString() {
    return "OrbConfigSource{" + "parser=" + parser + ", ds=" + ds + ", bind=" + bind + ", priority=" + priority + '}';
  }
  
}
