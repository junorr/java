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
package com.jpower.nettyserver;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/06/2012
 */
public class ContentEncoder {
  
  private DEScrypto des;
  
  private ChannelBuffer buffer;
  
  
  public ContentEncoder(String cipherKey) {
    this.des = DEScrypto.newInstance(cipherKey);
    this.buffer = ChannelBuffers.dynamicBuffer();
  }
  
  
   public ChannelBuffer append(String s) {
    buffer.writeBytes(s.getBytes());
    return buffer;
  }
  
  
  public ChannelBuffer appendLine(String line) {
    buffer.writeBytes(line.concat("\n").getBytes());
    return buffer;
  }
  
  
  public byte[] encode(byte[] bs) {
    if(bs == null || bs.length == 0)
      return null;
    
    return des.encrypt(bs);
  }
  
  
  public ChannelBuffer encode(ChannelBuffer channel) {
    if(buffer == null || des == null)
      return null;
    
    buffer.writeBytes(des.encrypt(channel.array()));
    return buffer;
  }
  
  
  public ChannelBuffer getChannelBuffer() {
    return buffer;
  }
  
}
