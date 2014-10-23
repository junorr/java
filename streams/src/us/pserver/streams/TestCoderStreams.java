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

package us.pserver.streams;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptUtils;
import us.pserver.cdr.lzma.LzmaStreamFactory;
import static us.pserver.streams.TestCoderStreams.os;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 01/08/2014
 */
public class TestCoderStreams {

  
  public static Path p(String p) {
    return Paths.get(p);
  }
  
  
  public static InputStream is(Path p) throws IOException {
    return Files.newInputStream(p, 
        StandardOpenOption.READ);
  }
  
  
  public static OutputStream os(Path p) throws IOException {
    return Files.newOutputStream(p, 
        StandardOpenOption.CREATE, 
        StandardOpenOption.WRITE);
  }
  
  
  public static OutputStream gz_cr_os(OutputStream os, CryptKey ky) throws IOException {
    //GZip >> Crypt >> out
    os = CryptUtils.createCipherOutputStream(os, ky);
    os = new BufferedOutputStream(os);
    os = new GZIPOutputStream(os);
    return os;
  }
  
  
  public static InputStream is_cr_gz(InputStream is, CryptKey ky) throws IOException {
    //in >> Crypt >> GZip
    is = CryptUtils.createCipherInputStream(is, ky);
    is = new BufferedInputStream(is);
    is = new GZIPInputStream(is);
    return is;
  }
  
  
  public static OutputStream cr_64_gz_os(OutputStream os, CryptKey ky) throws IOException {
    //Crypt >> B64 >> GZip >> out
    os = new GZIPOutputStream(os);
    os = new BufferedOutputStream(os);
    os = new Base64OutputStream(os);
    os = new BufferedOutputStream(os);
    os = CryptUtils.createCipherOutputStream(os, ky);
    return os;
  }
  
  
  public static InputStream is_gz_64_cr(InputStream is, CryptKey ky) throws IOException {
    //in >> GZip >> B64 >> Crypt
    is = new GZIPInputStream(is);
    is = new BufferedInputStream(is);
    is = new Base64InputStream(is);
    is = new BufferedInputStream(is);
    is = CryptUtils.createCipherInputStream(is, ky);
    return is;
  }
  
  
  public static OutputStream cr_64_lz_os(OutputStream os, CryptKey ky) throws IOException {
    //Crypt >> B64 >> LZ >> out
    os = LzmaStreamFactory.createLzmaOutput(os);
    os = new BufferedOutputStream(os);
    os = new Base64OutputStream(os);
    os = new BufferedOutputStream(os);
    os = CryptUtils.createCipherOutputStream(os, ky);
    return os;
  }
  
  
  public static InputStream is_lz_64_cr(InputStream is, CryptKey ky) throws IOException {
    //in >> LZ >> B64 >> Crypt
    is = LzmaStreamFactory.createLzmaInput(is);
    is = new BufferedInputStream(is);
    is = new Base64InputStream(is);
    is = new BufferedInputStream(is);
    is = CryptUtils.createCipherInputStream(is, ky);
    return is;
  }
  
  
  public static OutputStream gz_cr_64_os(OutputStream os, CryptKey ky) throws IOException {
    //GZip >> Crypt >> B64 >> out
    os = new Base64OutputStream(os);
    os = new BufferedOutputStream(os);
    os = CryptUtils.createCipherOutputStream(os, ky);
    os = new BufferedOutputStream(os);
    os = new GZIPOutputStream(os);
    return os;
  }
  
  
  public static InputStream is_64_cr_gz(InputStream is, CryptKey ky) throws IOException {
    //in >> B64 >> Crypt >> GZip
    is = new Base64InputStream(is);
    is = new BufferedInputStream(is);
    is = CryptUtils.createCipherInputStream(is, ky);
    is = new BufferedInputStream(is);
    is = new GZIPInputStream(is);
    return is;
  }
  
  
  public static void transfer(InputStream is, OutputStream os) throws IOException {
    int read = -1;
    byte[] buf = new byte[4096];
    while(true) {
      read = is.read(buf);
      if(read <= 0) break;
      os.write(buf, 0, read);
    }
    os.flush();
  }
  
  
  public static void main(String[] args) throws IOException {
    System.out.println("---- Encoding ----");
    
    Path pi = p("c:/.local/splash.png");
    Path po = p("c:/.local/splash.enc");
    
    InputStream is = is(pi);
    OutputStream os = os(po);
    CryptKey key = CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_PKCS5);
    
    // -- GZIP --
    //GZip >> Crypt >> out
    //os = gz_cr_os(os, key);
    //System.out.println("* ["+ pi+ "] >> GZip >> Crypt >> ["+ po+ "]");
    
    //Crypt >> B64 >> GZip >> out
    //os = cr_64_gz_os(os, key);
    //System.out.println("* ["+ pi+ "] >> Crypt >> B64 >> GZip >> ["+ po+ "]");
    
    //GZip >> Crypt >> B64 >> out
    //os = gz_cr_64_os(os, key);
    //System.out.println("* ["+ pi+ "] >> GZip >> Crypt >> B64 >> ["+ po+ "]");
    
    // -- LZMA --
    //Crypt >> B64 >> LZ >> out
    os = cr_64_lz_os(os, key);
    System.out.println("* ["+ pi+ "] >> Crypt >> B64 >> LZ >> ["+ po+ "]");
    
    transfer(is, os);
    os.close();
    is.close();
    System.out.println("* Done!");
    
    //------------------------------------
    System.out.println("---- Deconding ----");
    
    pi = p("c:/.local/splash.enc");
    po = p("c:/.local/splash-lz.png");
    
    is = is(pi);
    os = os(po);
    
    // -- GZIP --
    //in >> Crypt >> GZip
    //is = is_cr_gz(is, key);
    //System.out.println("* ["+ pi+ "] >> dCrypt >> dGZip >> ["+ po+ "]");
    
    //in >> GZip >> B64 >> Crypt
    //is = is_gz_64_cr(is, key);
    //System.out.println("* ["+ pi+ "] >> dGZip >> dB64 >> dCrypt >> ["+ po+ "]");
    
    //in >> B64 >> Crypt >> GZip
    //is = is_64_cr_gz(is, key);
    //System.out.println("* ["+ pi+ "] >> dB64 >> dCrypt >> dGZip >> ["+ po+ "]");
    
    // -- LZMA --
    //in >> LZ >> B64 >> Crypt
    is = is_lz_64_cr(is, key);
    System.out.println("* ["+ pi+ "] >> dLZ >> dB64 >> dCrypt >> ["+ po+ "]");
    
    transfer(is, os);
    os.close();
    is.close();
    System.out.println("* Done!");
  }
  
}
