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

package us.pserver.tar;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/10/2015
 */
public class TarWriter {
  
  
  protected TarArchiveOutputStream tar;
  
  protected OutputStream out;
  
  private List<TarData> dataList;
  

  public TarWriter(OutputStream out) {
    this.out = Valid.off(out).getOrFail(OutputStream.class);
    dataList = new ArrayList<>();
  }
  
  
  public TarWriter put(TarData data) {
    if(data != null) {
      dataList.add(data);
    }
    return this;
  }
  
  
  public List<TarData> getDataList() {
    return dataList;
  }
  
  
  private void buildOutput() throws IOException {
    tar = new TarArchiveOutputStream(new BufferedOutputStream(out));
  }
  
  
  public TarWriter write() throws IOException {
    if(dataList.isEmpty()) return this;
    if(tar == null) buildOutput();
    Iterator<TarData> it = dataList.iterator();
    OutputConnector con = new OutputConnector(tar);
    while(it.hasNext()) {
      TarData data = it.next();
      write(con, data);
      while(Iterator.class.isAssignableFrom(data.getClass())
          && ((Iterator)data).hasNext()) {
        Object next = ((Iterator)data).next();
        if(TarData.class.isAssignableFrom(next.getClass())) {
          write(con, (TarData) next);
        }
      }
    }
    return this;
  }
  
  
  private void write(OutputConnector con, TarData data) throws IOException {
    if(data != null && data.getEntry() != null) {
      tar.putArchiveEntry(data.getEntry());
      if(data.getData() != null) {
        con.connect(data.getData());
        data.getData().close();
      }
      tar.closeArchiveEntry();
    }
  }
  
  
  public void close() throws IOException {
    tar.flush();
    tar.finish();
    out.flush();
    tar.close();
    dataList.clear();
    dataList = null;
  }
  
  
  public static void main(String[] args) throws IOException {
    String sout = "D:/test2.tar";
    TarWriter tw = new TarWriter(new FileOutputStream(sout));
    /*
    tw.put(new TarPathData("D:/pic.jpg"))
        .put(new TarPathData("D:/pic-2.jpg"));
    */
    /**/
    tw.put(new TarPathData("D:/test"));
    /**/
    tw.write();
    tw.close();
  }
  
}
