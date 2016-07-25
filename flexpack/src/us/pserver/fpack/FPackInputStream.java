/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca e um software livre; voce pode redistribui-la e/ou modifica-la sob os
 * termos da Licenca Publica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versao 2.1 da Licenca, ou qualquer
 * versao posterior.
 * 
 * Esta biblioteca eh distribuida na expectativa de que seja util, porem, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE
 * OU ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto
 * com esta biblioteca; se nao, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.fpack;

import com.cedarsoftware.util.io.JsonReader;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import us.pserver.streams.NullOutput;
import us.pserver.streams.PushbackInputStream;
import us.pserver.streams.SearchableInputStream;
import us.pserver.streams.StreamConnector;
import us.pserver.tools.UTF8String;
import us.pserver.valid.Valid;
import us.pserver.valid.ValidChecked;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class FPackInputStream extends FilterInputStream {

  private PushbackInputStream pin;
  
  private SearchableInputStream sin;
  
  private FPackFileHeader filehd;
  
  private boolean headers, nomore;
  
  private long readCount;
  
  private FPackEntry entry;
  
  
  public FPackInputStream(InputStream in) {
    super(in);
    pin = new PushbackInputStream(in);
    sin = null;
    readCount = 0;
    nomore = false;
    try {
      headers = pin.read() == 1;
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public long getReadCount() {
    return readCount;
  }
  
  
  private byte[] readNextEntry() throws IOException {
    SearchableInputStream sin = new SearchableInputStream(
            pin, FPackConstants.ENTRY_END.getBytes()
    );
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    long count = StreamConnector.builder()
        .from(sin)
        .to(bos)
        .get()
        .connect()
        .getCount();
    nomore = count < 1;
    if(!nomore) readCount += count;
    byte[] ba = bos.toByteArray();
    if(ba != null && ba.length > 0) {
      //System.out.println("* readNextEntry: '"+ new UTF8String(ba).toString()+ "'");
    }
    return ba;
  }
  
  
  public FPackFileHeader getFileHeader() throws IOException {
    if(entry == null && headers) {
      byte[] bes = readNextEntry();
      filehd = (FPackFileHeader) JsonReader.jsonToJava(
          new UTF8String(bes).toString()
      );
    }
    return filehd;
  }
  
  
  public List<FPackHeader> getHeaders() {
    try {
      return getFileHeader();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public FPackEntry selectEntry(FPackHeader fhd) throws IOException {
    Valid.off(fhd).forNull().fail(FPackHeader.class);
    pin.skip(Math.min(0, fhd.getPosition() - readCount));
    return getNextEntry();
  }
  
  
  
  public FPackEntry getNextEntry() throws IOException {
    byte[] bes = readNextEntry();
    if(nomore || bes == null || bes.length < 1) {
      return null;
    }
    if(entry == null && headers && filehd == null) {
      filehd = (FPackFileHeader) JsonReader.jsonToJava(
          new UTF8String(bes).toString()
      );
      bes = readNextEntry();
    }
    entry = (FPackEntry) JsonReader.jsonToJava(
        new UTF8String(bes).toString()
    );
    if(entry != null) {
      sin = new SearchableInputStream(
        pin, FPackConstants.ENTRY_END.getBytes()
      );
    }
    return entry;
  }
  
  
  public long consumeEntry() throws IOException {
    SearchableInputStream sin = new SearchableInputStream(
        pin, FPackConstants.ENTRY_END.getBytes()
    );
    long count = StreamConnector.builder()
        .from(sin)
        .to(NullOutput.out)
        .get().connect()
        .getCount();
    readCount += count;
    return count;
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    Valid.off(bs).forEmpty().fail();
    Valid.off(off).forLesserThan(0).fail();
    Valid.off(len).forNotBetween(
        1, bs.length-off
    ).fail();
    ValidChecked.<FPackEntry,IOException>off(
        entry, m->new IOException(m))
        .forNull().fail("No entry selected. "
            + "Call getNextEntry() first");
    return sin.read(bs, off, len);
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return read(Valid.off(bs)
        .forNull().getOrFail(), 0, bs.length
    );
  }
  
  
  @Override
  public int read() throws IOException {
    byte[] bs = new byte[1];
    int read = read(bs);
    if(read != -1) read = bs[0];
    return read;
  }
  
}
