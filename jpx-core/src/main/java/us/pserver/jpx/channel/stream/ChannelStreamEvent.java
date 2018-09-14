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

package us.pserver.jpx.channel.stream;

import java.time.Instant;
import java.util.Map;
import us.pserver.jpx.event.AbstractEvent;
import us.pserver.jpx.event.Attribute;
import us.pserver.jpx.event.Event;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/08/2018
 */
public class ChannelStreamEvent extends AbstractEvent {

  public static enum Type implements Event.Type {
    SWITCH_TO_IO_CONTEXT,
    SWITCH_TO_SYSTEM_CONTEXT,
    STREAM_FUNCTION_EXECUTED,
    STREAM_FUNCTION_APPENDED,
    STREAM_FUNCTION_REMOVED,
    STREAM_FINISHED,
    EXCEPTION_THROWED;
  }


  public ChannelStreamEvent(Type type, Instant inst, Map<Attribute, Object> attrs) {
    super(type, inst, attrs);
  }
  
  public ChannelStreamEvent(Type type, Map<Attribute, Object> attrs) {
    super(type, attrs);
  }
  
}
