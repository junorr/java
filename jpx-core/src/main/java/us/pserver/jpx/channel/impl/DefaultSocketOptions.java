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

package us.pserver.jpx.channel.impl;

import us.pserver.jpx.channel.SocketOptions;
import us.pserver.tools.StringPad;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/08/2018
 */
public class DefaultSocketOptions implements SocketOptions {
  
  private final int rcvbuf;
  
  private final int sndbuf;
  
  private final int solinger;
  
  private final boolean tcpNoDelay;
  
  private final boolean keepAlive;
  
  private final boolean reuseAddr;
  

  public DefaultSocketOptions() {
    this(-1, -1, -1, false, false, false);
  }

  public DefaultSocketOptions(int rcvbuf, int sndbuf, int solinger, boolean tcpNoDelay, boolean keepAlive, boolean reuseAddr) {
    this.tcpNoDelay = tcpNoDelay;
    this.rcvbuf = rcvbuf;
    this.sndbuf = sndbuf;
    this.solinger = solinger;
    this.keepAlive = keepAlive;
    this.reuseAddr = reuseAddr;
  }
  
  
  @Override
  public boolean getTcpNoDelay() {
    return tcpNoDelay;
  }


  @Override
  public SocketOptions withTcpNoDelay(boolean tcpNoDelay) {
    return new DefaultSocketOptions(rcvbuf, sndbuf, solinger, tcpNoDelay, keepAlive, reuseAddr);
  }


  @Override
  public int getSoSndBuf() {
    return sndbuf;
  }


  @Override
  public SocketOptions withSoSndBuf(int sndbuf) {
    return new DefaultSocketOptions(rcvbuf, sndbuf, solinger, tcpNoDelay, keepAlive, reuseAddr);
  }


  @Override
  public int getSoRcvBuf() {
    return rcvbuf;
  }


  @Override
  public SocketOptions withSoRcvBuf(int rcvbuf) {
    return new DefaultSocketOptions(rcvbuf, sndbuf, solinger, tcpNoDelay, keepAlive, reuseAddr);
  }


  @Override
  public boolean getSoKeepAlive() {
    return keepAlive;
  }


  @Override
  public SocketOptions withSoKeepAlive(boolean keepAlive) {
    return new DefaultSocketOptions(rcvbuf, sndbuf, solinger, tcpNoDelay, keepAlive, reuseAddr);
  }


  @Override
  public boolean getSoReuseAddr() {
    return reuseAddr;
  }


  @Override
  public SocketOptions withSoReuseAddr(boolean reuseAddr) {
    return new DefaultSocketOptions(rcvbuf, sndbuf, solinger, tcpNoDelay, keepAlive, reuseAddr);
  }


  @Override
  public int getSoLinger() {
    return solinger;
  }


  @Override
  public SocketOptions withSoLinger(int solinger) {
    return new DefaultSocketOptions(rcvbuf, sndbuf, solinger, tcpNoDelay, keepAlive, reuseAddr);
  }
  
  
  @Override
  public String toString(int ident) {
    String sident = StringPad.of("").lpad(" ", ident);
    return sident + "DefaultSocketOptions{\n" 
        + sident + " - rcvbuf=" + rcvbuf + ",\n"
        + sident + " - sndbuf=" + sndbuf + ",\n"
        + sident + " - solinger=" + solinger + ",\n"
        + sident + " - tcpNoDelay=" + tcpNoDelay + ",\n"
        + sident + " - keepAlive=" + keepAlive + ",\n"
        + sident + " - reuseAddr=" + reuseAddr + "\n"
        + sident + "}";
  }
  
  
  @Override
  public String toString() {
    return toString(0);
  }
  
}
