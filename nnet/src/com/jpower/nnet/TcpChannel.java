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
package com.jpower.nnet;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;


/**
 * Canal de comunicação TCP/IP entre dois pontos.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-27
 */
public class TcpChannel {
  
  private Channel channel;
  
  private FrameControl fcontrol;
  
  
  /**
   * Construtor padrão, recebe um 
   * <code>org.jboss.netty.channel.Channel</code>
   * @param ch <code>org.jboss.netty.channel.Channel</code>
   * @see org.jboss.netty.channel.Channel
   */
  public TcpChannel(Channel ch) {
    if(ch == null)
      throw new IllegalArgumentException(
          "Invalid Channel: "+ ch);
    
    channel = ch;
    fcontrol = new FrameControl();
  }
  
  
  /**
   * Espera até que a operação futura do canal seja 
   * concluída, retornando um valor booleano 
   * indicando o sucesso da operação.
   * @param f Canal com operação futura pendente.
   * @return <code>true</code> se a operação
   * futura for concluída com sucesso, 
   * <code>false</code> caso contrário.
   */
  public boolean wait(ChannelFuture f) {
    if(f == null) return false;
    try {
      while(!f.isDone()) {
        Thread.sleep(20);
      }
      return f.isSuccess();
    } catch(InterruptedException ex) {
      ex.printStackTrace();
      return false;
    }
  }


  /**
   * Retorna o endereço local ao qual o canal está está conectado.
   * @return Endereço local <code>InetSocketAddress</code>.
   */
  public InetSocketAddress getLocalAddress() {
    return (InetSocketAddress) channel.getLocalAddress();
  }
  
  
  /**
   * Retorna o endereço remoto ao qual o canal está está conectado.
   * @return Endereço remoto <code>InetSocketAddress</code>.
   */
  public InetSocketAddress getRemoteAddress() {
    return (InetSocketAddress) channel.getRemoteAddress();
  }
  
  
  /**
   * Fecha o canal de comunicação.
   * @return Esta instância modificada de TcpChannel
   */
  public TcpChannel close() {
    if(channel.isOpen()) {
      ChannelFuture f = channel.close();
      this.wait(f);
    }
    return this;
  }
  
  
  /**
   * Desconecta o canal do endereço remoto ao qual 
   * está conectado.
   * @return Esta instância modificada de TcpChannel.
   */
  public TcpChannel disconnect() {
    if(channel.isConnected()) {
      ChannelFuture f = channel.disconnect();
      this.wait(f);
    }
    return this;
  }
  
  
  /**
   * Desvincula o canal do endereço local ao qual está conectado.
   * @return Esta instância modificada de TcpChannel.
   */
  public TcpChannel unbind() {
    if(channel.isBound()) {
      ChannelFuture f = channel.unbind();
      this.wait(f);
    }
    return this;
  }
  
  
  /**
   * Verifica se o canal está aberto.
   * @return <code>true</code> se o canal de comunicação 
   * estiver aberto, <code>false</code> caso contrário.
   */
  public boolean isOpen() {
    return channel.isOpen();
  }
  
  
  /**
   * Verifica se o canal está conectado.
   * @return <code>true</code> se o canal está
   * conectado, <code>false</code> caso contrário.
   */
  public boolean isConnected() {
    return channel.isConnected();
  }
  
  
  /**
   * Verifica se o canal está aberto para escrita.
   * @return <code>true</code> se o canal está aberto
   * para escrita, <code>false</code> caso contrário.
   */
  public boolean isWritable() {
    return channel.isWritable();
  }
  
  
  /**
   * Verifica se o canal pode ou possui dados para leitura.
   * @return <code>true</code> se o canal pode ou possui dados
   * para leitura, <code>false</code> caso contrário.
   */
  public boolean isReadable() {
    return channel.isReadable();
  }
  
  
  /**
   * Verifica se o canal está vinculado à um endereço 
   * local ou não.
   * @return <code>true</code> se o canal está vinculado
   * á um endereço local, <code>false</code> caso
   * contrário.
   */
  public boolean isBound() {
    return channel.isBound();
  }
  
  
  /**
   * Define o estado de leitura do canal de comunicação.
   * @param readable <code>true</code> se o canal deve
   * estar habilitado para leitura, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>TcpChannel</code>.
   */
  public TcpChannel setReadable(boolean readable) {
    channel.setReadable(readable);
    return this;
  }
  
  
  /**
   * Vincula o canal ao endereço local informado.
   * @param local Endereço local a ser vinculado.
   * @return Esta instância modificada de TcpChannel.
   */
  public TcpChannel bind(SocketAddress local) {
    ChannelFuture f = channel.bind(local);
    this.wait(f);
    return this;
  }
  
  
  /**
   * Conecta o canal ao endereço remoto informado.
   * @param remote Endereço remoto ao qual será conectado.
   * @return Esta instância modificada de TcpChannel.
   */
  public TcpChannel connect(SocketAddress remote) {
    ChannelFuture f = channel.connect(remote);
    this.wait(f);
    return this;
  }
  
  
  /**
   * Habilita/Deabilita o controle de frames de comunicação.
   * O controle de frames de comunicação evita o recebimento 
   * incompleto de informações pela outra ponta do canal de comunicação,
   * porém só funciona se as duas pontas de do canal de 
   * comunicação utilizem a biblioteca <code>com.jpower.nnet</code>.
   * @return Esta instância modificada de <code>GeneralTcpHandler</code>.
   */
  public TcpChannel setFrameControl(FrameControl fc) {
    if(fc != null)
      fcontrol = fc;
    return this;
  }
  
  
  /**
   * Verifica se o controle de frames de comunicação está ativo.
   * O controle de frames de comunicação evita o recebimento 
   * incompleto de informações pela outra ponta do canal de comunicação,
   * porém só funciona se as duas pontas de do canal de 
   * comunicação utilizem a biblioteca <code>com.jpower.nnet</code>.
   * @return <code>true</code> se o controle de frames de comunicação
   * está ativado, <code>false</code> caso contrário.
   */
  public FrameControl getFrameControl() {
    return fcontrol;
  }
  
  
  /**
   * Escreve os dados do buffer informado neste canal de comunicação.
   * @param buf buffer com os dados a serem escritos.
   * @return a quantidade de bytes escrita no canal.
   */
  public int write(DynamicBuffer buf) {
    if(buf == null || buf.isEmpty()
        || !this.isConnected() 
        || !this.isOpen()
        || !this.isWritable()) return -1;
    
    int size = buf.size();
    
    if(fcontrol.isEnabledOnSend()) 
      buf.write(FrameControl.getBytes());
    
    ChannelBuffer cb = ChannelBuffers.buffer(buf.size());
    buf.read(cb);
    //System.out.println("|"+ buf.readString()+ "|");
    ChannelFuture f = channel.write(cb);
    this.wait(f);
    
    return size;
  }
  
  
  /**
   * Escreve os dados do array informado neste canal de comunicação.
   * @param bytes array de bytes com os dados a serem escritos.
   * @return a quantidade de bytes escrita no canal.
   */
  public int write(byte[] bytes) {
    if(bytes == null || bytes.length == 0
        || !this.isConnected() 
        || !this.isOpen()
        || !this.isWritable()) return -1;

    DynamicBuffer buf = new DynamicBuffer();
    buf.write(bytes);
    
    if(fcontrol.isEnabledOnSend()) 
      buf.write(FrameControl.getBytes());
    
    ChannelBuffer cb = ChannelBuffers.buffer(buf.size());
    buf.read(cb);
    //System.out.println("|"+ buf.readString()+ "|");
    ChannelFuture f = channel.write(cb);
    this.wait(f);
    
    return bytes.length;
  }
  
}
