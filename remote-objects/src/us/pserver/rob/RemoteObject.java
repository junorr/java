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

package us.pserver.rob;

import us.pserver.rob.channel.Transport;
import us.pserver.rob.factory.DefaultFactoryProvider;
import us.pserver.rob.factory.ConnectorChannelFactory;
import us.pserver.rob.channel.Channel;
import java.io.IOException;
import java.io.InputStream;
import us.pserver.rob.server.FakeInputStreamRef;

/**
 * Representa um objeto remoto para invocação de
 * métodos.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/11/2013
 */
public class RemoteObject {
  
  private NetConnector net;
  
  private ConnectorChannelFactory factory;
  
  private Channel channel;
  
  
  /**
   * Construtor padrão sem argumentos,
   * com canal de comunicação de objetos na rede do
   * tipo padrão <code>XmlNetChannel</code>.
   */
  public RemoteObject() {
    net = new NetConnector();
    factory = DefaultFactoryProvider
        .getConnectorXmlChannelFactory();
    channel = null;
  }
  
  
  /**
   * Construtor que recebe as informações de 
   * conexão de rede.
   * @param con Conexão de rede.
   */
  public RemoteObject(NetConnector con) {
    this();
    if(con == null)
      throw new IllegalArgumentException(
          "Invalid NetConnector ["+ con+ "]");
    net = con;
    factory = DefaultFactoryProvider
        .getConnectorXmlChannelFactory();
  }
  
  
  /**
   * Construtor que recebe as informações de rede 
   * <code>NetConnector</code> e a fábrica do canal de 
   * transmissão de objetos na rede.
   * @param fact fábrica de canal transmissão de objetos na rede.
   */
  public RemoteObject(NetConnector con, ConnectorChannelFactory fact) {
    this(con);
    if(fact == null)
      throw new IllegalArgumentException(
          "Invalid ChannelFactory ["+ fact+ "]");
    factory = fact;
  }
  
  
  /**
   * Retorna informações de conexão de rede.
   * @return informações de conexão de rede.
   */
  public NetConnector getNetConnector() {
    return net;
  }


  /**
   * Define informações de conexão de rede.
   * @param net informações de conexão de rede.
   * @return Esta instância modificada de RemoteObject.
   */
  public RemoteObject setNetConnector(NetConnector net) {
    this.net = net;
    return this;
  }


  /**
   * Retorna a fábrica do canal de transmissão de objetos na rede.
   * @return fábrica do canal de transmissão de objetos na rede.
   */
  public ConnectorChannelFactory getChannelFactory() {
    return factory;
  }


  /**
   * Define a fábrica do canal de transmissão de objetos na rede.
   * @param fact fábrica do canal de transmissão de objetos na rede.
   * @return Esta instância modificada de RemoteObject.
   */
  public RemoteObject setChannelFactory(ConnectorChannelFactory fact) {
    this.factory = fact;
    return this;
  }
  
  
  /**
   * Retorna o último canal de comunicação 
   * de objetos criado e utilizado por 
   * <code>RemoteObject</code>.
   * @return <code>Channel</code>
   */
  public Channel getChannel() {
    return channel;
  }
  
  
  /**
   * Cria um canal de comunicação de objetos na rede.
   * @return <code>Channel</code>.
   */
  private Channel channel() {
    if(channel != null && channel.isValid())
      return channel;
    
    if(net == null) throw new IllegalStateException(
        "Cannot create Channel. Invalid NetConnector ["+ net+ "]");
    if(factory == null) throw new IllegalStateException(
        "Invalid ChannelFactory ["+ factory+ "]");
    channel = factory.createChannel(net);
    return channel;
  }
  
  
  private Channel newChannel() {
    channel.close();
    channel = null;
    return channel();
  }
  
  
  public RemoteObject close() {
    if(channel != null)
      channel.close();
    return this;
  }
  
  
  /**
   * Invoca o método remoto informado.
   * @param rmt método remoto a ser invocado.
   * @return Retorno do método remoto ou <code>null</code>
   * se não houver.
   * @throws MethodInvocationException Caso ocorra erro na invocação do
   * método.
   */
  public Object invoke(RemoteMethod rmt) throws MethodInvocationException {
    if(rmt == null) throw new 
        IllegalArgumentException(
        "Invalid Null RemoteObject");
    
    OpResult res = this.invokeSafe(rmt);
    if(res != null && res.isSuccessOperation()) {
      return res.getReturn();
    }
    else if(res != null && res.hasError()) {
      throw res.getError();
    }
    else return null;
  }
  
  
  /**
   * Invoca o método remoto informado.
   * @param rmt método remoto a ser invocado.
   * @throws MethodInvocationException Caso ocorra erro na invocação do
   * método.
   */
  public void invokeVoid(RemoteMethod rmt) throws MethodInvocationException {
    this.invoke(rmt);
  }
  
  
  /**
   * Invoca o método remoto informado.
   * @param rmt método remoto a ser invocado.
   * @return Objeto OpResult com informações do 
   * resultado da invocação do método remoto.
   */
  public OpResult invokeSafe(RemoteMethod rmt) {
    OpResult res = new OpResult();
    try {
      Transport trp = new Transport();
      this.checkInputStreamRef(trp, rmt);
      trp.setObject(rmt);
      trp = this.sendTransport(trp).read();
      if(trp == null || trp.getObject() == null) {
        res.setSuccessOperation(false);
        res.setError(new IllegalStateException(
            "Cannot read object from channel"));
      }
      else {
        res = trp.castObject();
        if(trp.hasContentEmbedded())
          res.setReturn(trp.getInputStream());
      }
    } 
    catch(IOException ex) {
      res.setError(ex);
      res.setSuccessOperation(false);
    }
    
    if(channel != null && !channel.isValid())
        channel.close();
    
    return res;
  }
  
  
  /**
   * Invoca o a cadeia de métodos informada.
   * @param chain cadeia de métodos a ser invocada.
   * @return Retorno do método remoto ou <code>null</code>
   * se não houver.
   * @throws MethodInvocationException Caso ocorra erro na invocação do
   * método.
   */
  public Object invoke(MethodChain chain) throws MethodInvocationException {
    this.validateChain(chain);
    OpResult res = this.invokeSafe(chain);
    if(res != null && res.isSuccessOperation()) {
      return res.getReturn();
    }
    else if(res != null && res.hasError()) {
      throw res.getError();
    }
    else return null;
  }
  
  
  private void validateChain(MethodChain chain) throws IllegalArgumentException {
    if(chain == null || chain.methods().isEmpty()) 
      throw new IllegalArgumentException(
        "Invalid MethodChain ["+ chain+ "]");
  }
  
  
  /**
   * Invoca o a cadeia de métodos informada.
   * @param chain cadeia de métodos a ser invocada.
   * @throws MethodInvocationException Caso ocorra erro na invocação do
   * método.
   */
  public void invokeVoid(MethodChain chain) throws MethodInvocationException {
    this.invoke(chain);
  }
  
  
  /**
   * Invoca o a cadeia de métodos informada.
   * @param chain cadeia de métodos a ser invocada.
   * @return Objeto OpResult com informações do 
   * resultado da invocação do método remoto.
   */
  public OpResult invokeSafe(MethodChain chain) {
    this.validateChain(chain);
    OpResult res = new OpResult();
    try {
      Transport trp = new Transport();
      this.checkInputStreamRef(trp, chain.current());
      trp.setObject(chain.rewind());
      trp = this.sendTransport(trp).read();
      if(trp == null || trp.castObject() == null) {
        res.setSuccessOperation(false);
        res.setError(new IllegalStateException(
            "Cannot read object from channel"));
      }
      else {
        res = trp.castObject();
        if(trp.hasContentEmbedded())
          res.setReturn(trp.getInputStream());
      }
    } 
    catch(IOException ex) {
      res.setError(ex);
      res.setSuccessOperation(false);
    }
    
    if(channel != null && !channel.isValid())
        channel.close();
    
    return res;
  }
  
  
  private void checkInputStreamRef(Transport t, RemoteMethod r) {
    if(t == null || r == null) return;
    if(r.types().isEmpty()) r.extTypesParams();
    if(r.types().isEmpty()) return;
    for(int i = 0; i < r.types().size(); i++) {
      Class c = r.types().get(i);
      if(InputStream.class.isAssignableFrom(c)) {
        Object o = r.params().get(i);
        if(o != null && InputStream.class
            .isAssignableFrom(o.getClass())) {
          t.setInputStream((InputStream) o);
          r.params().set(i, new FakeInputStreamRef());
        }
      }
    }
  }
  
  
  /**
   * Envia um objeto <code>Transport</code> através
   * do canal de comunicação e retorna o canal utilizado.
   * @param trp <code>Transport</code> a ser enviado.
   * @return <code>Channel</code> de transmissão de objetos na rede.
   * @throws IOException Caso ocorra erro enviando
   */
  public Channel sendTransport(Transport trp) throws IOException {
    if(trp == null) throw new 
        IllegalArgumentException(
        "Invalid Null RemoteMethod");
    if(net == null) throw new 
        IllegalStateException(
        "Invalid Null NetConnector");
    
    try {
      this.channel();
    } catch(RuntimeException e) {
      throw new IOException(e.toString(), e);
    }
    
    try {
      channel.write(trp);
    } catch(IOException e) {
      newChannel().write(trp);
    }
    return channel;
  }
  
}
