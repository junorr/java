/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca � software livre; voc� pode redistribu�-la e/ou modific�-la sob os
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a vers�o 2.1 da Licen�a, ou qualquer
 * vers�o posterior.
 * 
 * Esta biblioteca � distribu�da na expectativa de que seja �til, por�m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE
 * OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto
 * com esta biblioteca; se n�o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.jom.def;

import java.util.Objects;
import us.pserver.jom.MappedValue;
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
  public StringValue map(Object t) {
    NotNull.of(t).failIfNull("Bad null object");
    return new StringValue(t.getClass().isArray() 
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
