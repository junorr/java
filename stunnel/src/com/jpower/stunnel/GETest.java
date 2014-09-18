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

package com.jpower.stunnel;

import com.jpower.nnet.ConnectionHandler;
import com.jpower.nnet.DynamicBuffer;
import com.jpower.nnet.TcpChannel;
import com.jpower.nnet.TcpClient;
import com.jpower.nnet.http.HttpRequest;
import java.net.InetSocketAddress;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 25/07/2012
 */
public class GETest {

  static class Handler implements ConnectionHandler {
    @Override
    public void connected(TcpChannel ch) {
      HttpRequest req = new HttpRequest()
          .setHost("www.google.com")
          .setProxyAuth("f6036477:65465411");
      System.out.println(" * Request:");
      System.out.println(req.build());
      DynamicBuffer buf = new DynamicBuffer();
      buf.writeString(req.build());
      ch.write(buf);
      ch.flush();
    }
    @Override
    public void disconnected(TcpChannel ch) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void closed(TcpChannel ch) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void error(Throwable th, TcpChannel ch) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void received(DynamicBuffer buffer, TcpChannel ch) {
      System.out.println(" * Message Received:");
      System.out.println(buffer.readString());
    }
  }
  
  public static void main(String[] args) {
    TcpClient cli = new TcpClient(
        new InetSocketAddress("172.24.74.38", 6060), 
        new Handler(), null);
    
    cli.connect();
  }

}
