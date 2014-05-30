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

package us.pserver.redfs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteMethod;
import us.pserver.rob.RemoteObject;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/12/2013
 */
public class AsyncFileReader implements Runnable {

  private CountDownLatch cdown;
  
  private IOData data;
  
  private NetConnector con;
  
  
  public AsyncFileReader(NetConnector nc, IOData dd, CountDownLatch ctl) {
    if(nc == null || dd == null || ctl == null)
      throw new IllegalArgumentException("Invalid Null Argument");
    if(dd.getRemoteFile() == null || dd.getPath() == null)
      throw new IllegalArgumentException("Invalid IOData");
    con = nc;
    data = dd;
    cdown = ctl;
    System.out.println("# AsyncFileReader( start="+ data.getStartPos()+ " )");
  }
  
  
  private SocketChannel invokeRemote() {
    RemoteObject rob = new RemoteObject(con);
    RemoteMethod rm = new RemoteMethod()
        .setObjectName(Tokens.LocalFileSystem.name())
        .setMethodName(Tokens.readPart.name())
        .setArgTypes(RemoteFile.class, long.class, 
          long.class, SocketChannel.class)
        .setArgs(data.getRemoteFile(), 
          data.getStartPos(), 
          data.getLength(), null);
    
    try {
      return rob.invokeRaw(rm);
    } catch(IOException e) {
      con.close();
      return null;
    }
  }


  @Override
  public void run() {
    try {
      FileChannel fch = FileChannel.open(data.getPath(), 
          StandardOpenOption.WRITE);
      fch.position(data.getStartPos());
      SocketChannel sck = invokeRemote();
      if(sck == null) return;
      this.copyChannels(sck, fch);
      
      fch.force(false);
      fch.close();
      sck.close();
      cdown.countDown();
      
    } catch (IOException ex) {}
  }
  
  
  private void copyChannels(ByteChannel src, ByteChannel dst) throws IOException {
    ByteBuffer buf = ByteBuffer.allocateDirect(FSConstants.BUFFER_SIZE);
    int read = 1;
    while(read > 0) {
      read = src.read(buf);
      buf.flip();
      dst.write(buf);
      buf.clear();
      data.update(read);
    }
  }
  
}
