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

package us.pserver.tools.mapper;

import java.util.Objects;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/09/2017
 */
public class StringMapper extends AbstractMapper {

  public StringMapper() {
    super(String.class, CharSequence.class, Character.class, char.class, char[].class);
  }


  @Override
  public MappedString map(Object t) {
    NotNull.of(t).failIfNull("Bad null object");
    return new MappedString(t.getClass().isArray() 
        ? String.copyValueOf((char[])t) 
        : Objects.toString(t)
    );
  }


  @Override
  public Object unmap(Class cls, MappedValue value) {
    NotNull.of(cls).failIfNull("Bad null Class");
    NotNull.of(value).failIfNull("Bad null value");
    String s = Objects.toString(value);
    if(CharSequence.class.isAssignableFrom(cls)) {
      return s;
    }
    else if(cls.isArray()) {
      return s.toCharArray();
    }
    else return s.charAt(0);
  }

}
