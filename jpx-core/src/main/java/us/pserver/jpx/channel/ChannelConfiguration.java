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

package us.pserver.jpx.channel;

import java.net.InetSocketAddress;
import us.pserver.jpx.pool.impl.BufferPoolConfiguration;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/08/2018
 */
public interface ChannelConfiguration {

  public int getIOThreadPoolSize();
  
  public ChannelConfiguration withIOThreadPoolSize(int size);
  
  
  public int getSystemThreadPoolSize();
  
  public ChannelConfiguration withSystemThreadPoolSize(int size);
  
  
  public BufferPoolConfiguration getBufferPoolConfiguration();
  
  public ChannelConfiguration withBufferPoolConfiguration(BufferPoolConfiguration cfg);
  
  
  public InetSocketAddress getSocketAddress();
  
  public ChannelConfiguration withSocketAddress(InetSocketAddress addr);
  
  public ChannelConfiguration withSocketAddress(String addr, int port);
  
  
  public SocketOptions getSocketOptions();
  
  public ChannelConfiguration withSocketOptions(SocketOptions options);
  
  
  public boolean isAutoReadEnabled();
  
  public ChannelConfiguration withAutoReadEnabled(boolean auto);
  
  
  public boolean isAutoWriteEnabled();
  
  public ChannelConfiguration withAutoWriteEnabled(boolean auto);
  
  public String toString(int ident);
  
}
