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

package com.jpower.pxy;

import com.jpower.net.ConnectionHandler;
import com.jpower.net.NetConfig;
import com.jpower.net.NioClient;
import com.jpower.net.http.RequestParser;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 15/07/2013
 */
public class TestHandler implements ConnectionHandler {
  
  private String request;
  
  private ByteBuffer buf;
  
  private static int step = 0;
  
  private static String req;
  
  private SocketChannel ch;
  
  
  public TestHandler() {
    if(step == 0) {
      request = "POST /distAuth/UI/Login HTTP/1.1\n" +
          "Host: login.intranet.bb.com.br\n" +
          "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0\n" +
          "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
          "Accept-Language: pt-BR,pt;q=0.8,en-US;q=0.5,en;q=0.3\n" +
          "Accept-Encoding: gzip, deflate\n" +
          "Referer: http://login.intranet.bb.com.br/distAuth/UI/Login?ForceAuth=true&goto=http://portal.intranet.bb.com.br/wps/myportal/intranet\n" +
          "Cookie: IBBID=49188f195f965e27553927dcdb8191f; JSESSIONID=000028cY7ehP4j8RxsokZFDmOK2:-1; amlbcookie=05; DistAuthLBCookieName=03; ssoacr=sso3.intranet.bb.com.br; BBSSOToken=AQIC5wM2LY4SfcyxolZ8jpvtqpCOzTF2OVffVqRUUz98zuA.*AAJTSQACMDMAAlMxAAIwNQ..*; cookieXContext=\"http://172.17.157.104:7001/portal/APPS\"; LtpaToken=dFNC3tvPbIPCsnAKIOBZuyPGYIckriRcXhgz/PGvdGdTw6zOxadZ2SpnGq1F4TbJ7V/dlXbIQcN89XtNF8VswZbHuRg3GuTi/YrXGkabwJ4h0J1dMM1UnjUr3FuIpPkvn/qGzXi21H+zXVB8AFQFIhWPiLYHZA2Trfby/rkdrNIk1v0sjMDyHaTgTTIONWhq5nzprJTO0TRYIqt+FEwRyriPSfGfQOQF030Q+SdaYfTmozCt/AzpGiv/D4vNA+Q/iEh6UGVBg5D7bA7L+fr2v/wy+nrYMc0nqtGSNxzJqfe4SClzuG7rlnaTygnYa95LYpryvH+ZMOENxfEx+4keb8pUA0qYdKbLN1YkPz6+bb2ZHeQEMstEflJRoFE9WQPD; parametrosUsuario=8234F7C20870A32AE54C08E961F61112.portal04-1; AMDistAuthCookie=https%3A%2F%2Flogin.intranet.bb.com.br%3A443%2FdistAuth%2FUI%2FLogin\n" +
          "Connection: keep-alive\n" +
          "Content-Length:	181\n" +
          "Content-Type:	application/x-www-form-urlencoded\n\n" +
          "IDToken0=&IDToken1=f6036477&IDToken2=98765498&IDButton=Submit&goto=aHR0cDovL3BvcnRhbC5pbnRyYW5ldC5iYi5jb20uYnIvd3BzL215cG9ydGFsL2ludHJhbmV0&gotoOnFail=&encoded=true&gx_charset=UTF-8\n\r\n\r";
    } else if(step == 1) {
      request = "GET /wps/myportal/intranet HTTP/1.1\n" +
          "Host: portal.intranet.bb.com.br\n" +
          "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0\n" +
          "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
          "Accept-Language: pt-BR,pt;q=0.8,en-US;q=0.5,en;q=0.3\n" +
          "Accept-Encoding: gzip, deflate\n" +
          "Cookie: IBBID=49188f195f965e27553927dcdb8191f; amlbcookie=05; DistAuthLBCookieName=03; ssoacr=sso5.intranet.bb.com.br; BBSSOToken=AQIC5wM2LY4Sfcxqb_O3x4ade7GTOMjdZ3Vkvm50UAtUvlE.*AAJTSQACMDMAAlMxAAIwNg..*; JSESSIONID=0000tkckKIq7u-6tZdHF9NU0MJK:16gckngjk; cookieXContext=\"http://172.17.157.104:7001/portal/APPS\"; LtpaToken=dFNC3tvPbIPCsnAKIOBZuyPGYIckriRcXhgz/PGvdGdTw6zOxadZ2SpnGq1F4TbJ7V/dlXbIQcN89XtNF8VswZbHuRg3GuTi/YrXGkabwJ4h0J1dMM1UnjUr3FuIpPkvhCxrhF9dH5Dc4oxx5HRd0hWPiLYHZA2Trfby/rkdrNIk1v0sjMDyHaTgTTIONWhq5nzprJTO0TRYIqt+FEwRyriPSfGfQOQF030Q+SdaYfTmozCt/AzpGiv/D4vNA+Q/iEh6UGVBg5D7bA7L+fr2v/wy+nrYMc0nqtGSNxzJqfe4SClzuG7rlnaTygnYa95LYpryvH+ZMOENxfEx+4keb8pUA0qYdKbLN1YkPz6+bb2ZHeQEMstEflJRoFE9WQPD; parametrosUsuario=8234F7C20870A32AE54C08E961F61112.portal04-1\n" +
          "Connection: keep-alive\n\n\r\n\r";
      buf.clear();
      buf.put(request.getBytes());
    } else if(step == 2) {
      int i1 = req.indexOf("Location: ");
      int i2 = req.indexOf("\n", i1);
      request = "GET "+ req.substring(i1, i2)+ " HTTP/1.1\n" +
          "Host: portal.intranet.bb.com.br\n" +
          "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0\n" +
          "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
          "Accept-Language: pt-BR,pt;q=0.8,en-US;q=0.5,en;q=0.3\n" +
          "Accept-Encoding: gzip, deflate\n" +
          "Cookie: IBBID=49188f195f965e27553927dcdb8191f; amlbcookie=05; DistAuthLBCookieName=03; ssoacr=sso5.intranet.bb.com.br; BBSSOToken=AQIC5wM2LY4Sfcxqb_O3x4ade7GTOMjdZ3Vkvm50UAtUvlE.*AAJTSQACMDMAAlMxAAIwNg..*; JSESSIONID=0000tkckKIq7u-6tZdHF9NU0MJK:16gckngjk; cookieXContext=\"http://172.17.157.104:7001/portal/APPS\"; LtpaToken=dFNC3tvPbIPCsnAKIOBZuyPGYIckriRcXhgz/PGvdGdTw6zOxadZ2SpnGq1F4TbJ7V/dlXbIQcN89XtNF8VswZbHuRg3GuTi/YrXGkabwJ4h0J1dMM1UnjUr3FuIpPkvhCxrhF9dH5Dc4oxx5HRd0hWPiLYHZA2Trfby/rkdrNIk1v0sjMDyHaTgTTIONWhq5nzprJTO0TRYIqt+FEwRyriPSfGfQOQF030Q+SdaYfTmozCt/AzpGiv/D4vNA+Q/iEh6UGVBg5D7bA7L+fr2v/wy+nrYMc0nqtGSNxzJqfe4SClzuG7rlnaTygnYa95LYpryvH+ZMOENxfEx+4keb8pUA0qYdKbLN1YkPz6+bb2ZHeQEMstEflJRoFE9WQPD; parametrosUsuario=8234F7C20870A32AE54C08E961F61112.portal04-1\n" +
          "Connection: keep-alive\n\n";
    }
  }


  @Override
  public void received(ByteBuffer buffer) {
    byte[] bs = new byte[buffer.limit()];
    buffer.get(bs);
    String req = new String(bs);
    RequestParser rp = new RequestParser();
    rp.parse(req);
    LogProvider.getLogger().info("Received from Browser:")
        .info(rp.getMethod())
        .info(rp.getAddress())
        .info(rp.getVersion())
        .info(rp.getHost())
        .info(rp.getUserAgent())
        .info("# # # # #\n-----------------------------------------\n" + req)
        .info("# # # # #\n-----------------------------------------");
  }


  @Override
  public void connected(SocketChannel channel) {
    LogProvider.getLogger().info("Connected");
    buf = ByteBuffer.allocate(request.length()*2);
    buf.put(request.getBytes());
    ch = channel;
  }


  @Override
  public void disconnected(SocketChannel channel) {
    LogProvider.getLogger().info("Disconnected");
  }


  @Override
  public void error(Throwable th) {
    LogProvider.getLogger().error(th);
  }


  @Override
  public ByteBuffer sending() {
    LogProvider.getLogger().info("Sending "+ buf.limit());
    request = null;
    return buf;
  }


  @Override
  public boolean isSending() {
    return request != null;
  }


  @Override
  public void sent(int bytes) {
    LogProvider.getLogger().info("sent "+ bytes);
    step++;
  }

  
  public static void main(String[] args) throws IOException {
    NioClient client;
    NetConfig conf = new NetConfig();
    conf.setAddress("172.17.78.118").setPort(80);
    conf.setLogger(LogProvider.getLogger());
    conf.setAutoFilterActivated(false);
    client = new NioClient(conf, new TestHandler());
    client.connect().run();
  }
  
}
