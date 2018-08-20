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

import java.net.InetSocketAddress;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.SocketOptions;
import us.pserver.jpx.pool.impl.BufferPoolConfiguration;
import us.pserver.tools.StringPad;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/08/2018
 */
public class DefaultChannelConfiguration implements ChannelConfiguration {
  
  public static final int DEFAULT_IO_THREAD_POOL_SIZE = Math.max(8, Runtime.getRuntime().availableProcessors());
  
  public static final int DEFAULT_SYSTEM_THREAD_POOL_SIZE = DEFAULT_IO_THREAD_POOL_SIZE;
  
  
  private final BufferPoolConfiguration bufPoolConfig;
  
  private final SocketOptions options;
  
  private final int ioThreadPoolSize;
  
  private final int systemThreadPoolSize;
  
  private final InetSocketAddress address;
  
  private final boolean autoRead;
  
  private final boolean autoWrite;
  

  public DefaultChannelConfiguration() {
    this(new BufferPoolConfiguration(), 
        new DefaultSocketOptions(),
        DEFAULT_IO_THREAD_POOL_SIZE, 
        DEFAULT_SYSTEM_THREAD_POOL_SIZE, 
        null,
        true,
        true
    );
  }

  public DefaultChannelConfiguration(BufferPoolConfiguration bufPoolConfig, SocketOptions options, int ioThreadPoolSize, int systemThreadPoolSize, InetSocketAddress address, boolean autoRead, boolean autoWrite) {
    this.bufPoolConfig = bufPoolConfig;
    this.options = options;
    this.ioThreadPoolSize = ioThreadPoolSize;
    this.systemThreadPoolSize = systemThreadPoolSize;
    this.address = address;
    this.autoRead = autoRead;
    this.autoWrite = autoWrite;
  }
  
  
  @Override
  public int getIOThreadPoolSize() {
    return ioThreadPoolSize;
  }


  @Override
  public ChannelConfiguration withIOThreadPoolSize(int ioThreadPoolSize) {
    return new DefaultChannelConfiguration(bufPoolConfig, options, ioThreadPoolSize, systemThreadPoolSize, address, autoRead, autoWrite);
  }


  @Override
  public int getSystemThreadPoolSize() {
    return systemThreadPoolSize;
  }


  @Override
  public ChannelConfiguration withSystemThreadPoolSize(int systemThreadPoolSize) {
    return new DefaultChannelConfiguration(bufPoolConfig, options, ioThreadPoolSize, systemThreadPoolSize, address, autoRead, autoWrite);
  }


  @Override
  public BufferPoolConfiguration getBufferPoolConfiguration() {
    return bufPoolConfig;
  }


  @Override
  public ChannelConfiguration withBufferPoolConfiguration(BufferPoolConfiguration bufPoolConfig) {
    return new DefaultChannelConfiguration(bufPoolConfig, options, ioThreadPoolSize, systemThreadPoolSize, address, autoRead, autoWrite);
  }
  
  
  @Override
  public SocketOptions getSocketOptions() {
    return options;
  }


  @Override
  public ChannelConfiguration withSocketOptions(SocketOptions options) {
    return new DefaultChannelConfiguration(bufPoolConfig, options, ioThreadPoolSize, systemThreadPoolSize, address, autoRead, autoWrite);
  }
  
  
  @Override
  public InetSocketAddress getSocketAddress() {
    return address;
  }


  @Override
  public ChannelConfiguration withSocketAddress(InetSocketAddress address) {
    return new DefaultChannelConfiguration(bufPoolConfig, options, ioThreadPoolSize, systemThreadPoolSize, address, autoRead, autoWrite);
  }


  @Override
  public ChannelConfiguration withSocketAddress(String addr, int port) {
    return withSocketAddress(new InetSocketAddress(addr, port));
  }


  @Override
  public boolean isAutoReadEnabled() {
    return autoRead;
  }


  @Override
  public ChannelConfiguration withAutoReadEnabled(boolean autoRead) {
    return new DefaultChannelConfiguration(bufPoolConfig, options, ioThreadPoolSize, systemThreadPoolSize, address, autoRead, autoWrite);
  }


  @Override
  public boolean isAutoWriteEnabled() {
    return autoWrite;
  }


  @Override
  public ChannelConfiguration withAutoWriteEnabled(boolean autoWrite) {
    return new DefaultChannelConfiguration(bufPoolConfig, options, ioThreadPoolSize, systemThreadPoolSize, address, autoRead, autoWrite);
  }
  
  
  @Override
  public String toString(int ident) {
    String sident = StringPad.of("").lpad(" ", ident);
    return "DefaultChannelConfiguration{\n" 
        + sident + " - bufPoolConfig=" + bufPoolConfig.toString(ident + 2) + ",\n"
        + sident + " - options=" + options.toString(ident + 2) + ",\n"
        + sident + " - ioThreadPoolSize=" + ioThreadPoolSize + ",\n"
        + sident + " - systemThreadPoolSize=" + systemThreadPoolSize + ",\n"
        + sident + " - address=" + address + ",\n"
        + sident + " - autoRead=" + autoRead + ",\n"
        + sident + " - autoWrite=" + autoWrite + "\n"
        + sident + "}";
  }
  
  
  @Override
  public String toString() {
    return toString(0);
  }
  
}
