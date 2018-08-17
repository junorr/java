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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/08/2018
 */
public interface SocketOptions {

  public boolean getTcpNoDelay();
  
  public ChannelConfiguration withTcpNoDelay(boolean tnd);
  
  
  public int getSoSndBuf();
  
  public ChannelConfiguration withSoSndBuf(int snb);
  
  
  public int getSoRcvBuf();
  
  public ChannelConfiguration withSoRcvBuf(int rcb);
  
  
  public boolean getSoKeepAlive();
  
  public ChannelConfiguration withSoKeepAlive(boolean kal);
  
  
  public boolean getSoReuseAddr();
  
  public ChannelConfiguration withSoReuseAddr(boolean rad);
  
  
  public int getSoLinger();
  
  public ChannelConfiguration withSoLinger(int lng);
  
}
