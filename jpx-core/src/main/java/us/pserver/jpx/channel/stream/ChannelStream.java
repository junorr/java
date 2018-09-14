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

import java.nio.ByteBuffer;
import java.util.Optional;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.event.EventListener;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/08/2018
 */
public interface ChannelStream extends Cloneable {
  
  public Channel getChannel();
  
  public ChannelStream addListener(EventListener<ChannelStream,ChannelStreamEvent> lst);
  
  public boolean removeListener(EventListener<ChannelStream,ChannelStreamEvent> lst);
  
  public <I,O> ChannelStream appendFunction(StreamFunction<I,O> fn);
  
  public <I,O> boolean removeFunction(StreamFunction<I,O> fn);
  
  public boolean isStreamFinished();
  
  public ChannelStream run(ByteBuffer buf);
  
  public ChannelStream runSync(ByteBuffer buf);
  
  public boolean isInIOContext();
  
  public <I> void switchToIOContext(Optional<I> opt);
  
  public boolean isInSytemContext();
  
  public <I> void switchToSystemContext(Optional<I> opt);
  
}
