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
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/10/2015
 */
public class TBZWriter extends TarWriter {
  
  
  public static enum BZipBlockSize {
    
    B100K(1),
    B200K(2),
    B300K(3),
    B400K(4),
    B500K(5),
    B600K(6),
    B700K(7),
    B800K(8),
    B900K(9);
    
    private BZipBlockSize(int sz) {
      size = sz;
    }
    
    private final int size;
    
    public int getSize() {
      return size;
    }
    
  }
  
  
  public TBZWriter(OutputStream out) throws IOException {
    super(new BZip2CompressorOutputStream(new BufferedOutputStream(
        Valid.off(out).forNull().getOrFail(OutputStream.class)))
    );
  }
  
  
  public TBZWriter(OutputStream out, BZipBlockSize block) throws IOException {
    super(
        new BZip2CompressorOutputStream(new BufferedOutputStream(
            Valid.off(out).forNull().getOrFail(OutputStream.class)),
            Valid.off(block).forNull().getOrFail(BZipBlockSize.class).getSize()
        )
    );
  }
  
  
  public static void main(String[] args) throws IOException {
    String sout = "D:/test2.tar.bz";
    TBZWriter tw = new TBZWriter(new FileOutputStream(sout));
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
