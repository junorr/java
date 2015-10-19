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

package us.pserver.cdr.crypt;

import us.pserver.cdr.crypt.iv.SecureRandomIV;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.crypto.CipherOutputStream;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.FileCoder;
import us.pserver.cdr.FileUtils;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64BufferCoder;
import us.pserver.tools.Valid;


/**
 * Codificador/Decodificador de criptografia para arquivos.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class CryptFileCoder implements FileCoder {

  /**
   * <code>DECODE_BUFFER_SIZE = 8192</code><br>
   * Tamanho do buffer para decodificação.
   */
  public static final int DECODE_BUFFER_SIZE = 8192;
  
  /**
   * <code>ENCODE_BUFFER_SIZE = 8190</code><br>
   * Tamanho do buffer para codificação.
   */
  public static final int ENCODE_BUFFER_SIZE = 8190;
  
  
  private CryptBufferCoder cryptCoder;
  
  private CryptKey key;
  
  
  /**
   * Construtor padrão que recebe a chave de criptografia.
   * @param key chave de criptografia <code>CryptKey</code>.
   */
  public CryptFileCoder(CryptKey key) {
    if(key == null || key.getKeySpec() == null)
      throw new IllegalArgumentException(
          "Invalid CryptKey: "+ key);
    this.key = key;
    cryptCoder = new CryptBufferCoder(key);
  }
  
  
  /**
   * Retorna a chave de criptografia <code>CryptKey</code>.
   * @return chave de criptografia <code>CryptKey</code>.
   */
  public CryptKey getKey() {
    return cryptCoder.getKey();
  }
  
  
  /**
   * Retorna o codificador de criptografia para ByteBuffer.
   * @return codificador de criptografia para ByteBuffer.
   */
  public CryptBufferCoder getCoder() {
    return cryptCoder;
  }
  
  
  /**
   * Cria um caminho <code>Path</code> a partir 
   * da String informada, criando um novo arquivo 
   * no caminho caso não exista.
   * @param strPath <code>String</code> a partir da qual
   * será criado o caminho <code>Path</code>.
   * @return Caminho de arquivo <code>Path</code>.
   */
  public Path path(String strPath) {
    Valid.off(strPath).forEmpty().fail("Invalid path: ");
    Path p = FileUtils.path(strPath);
    this.createIfNotExists(p, true);
    return p;
  }
  
  
  /**
   * Cria o arquivo/diretório representado pelo argumento 
   * <code>Path p</code>, caso não exista.
   * @param p Caminho do arquivo/diretório a ser criado.
   * @param isFile <code>true</code> se o caminho
   * representa um arquivo, <code>false</code> caso
   * represente um diretório.
   * @return <code>true</code> se o arquivo/diretório
   * foi criado com sucesso, <code>false</code>
   * caso contrário.
   */
  public boolean createIfNotExists(Path p, boolean isFile) {
    Valid.off(p).forNull().fail(Path.class);
    try {
      if(Files.exists(p)) return true;
      if(isFile)
        Files.createFile(p);
      else
        Files.createDirectory(p);
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  @Override
  public boolean apply(Path src, Path dst, boolean encode) {
    Valid.off(src).forNull().fail(Path.class);
    Valid.off(dst).forNull().fail(Path.class);

    InputStream input = null;
    OutputStream output = null;
    
    try {
      input = new FileInputStream(src.toFile());
      output = new FileOutputStream(dst.toFile());
      if(encode) {
        output = CryptUtils.createCipherOutputStream(output, key);
      } else {
        input = CryptUtils.createCipherInputStream(input, key);
      }
      
      byte[] buf = new byte[4096];
      int read = 0;
      while((read = input.read(buf)) != -1) {
        output.write(buf, 0, read);
      }
      output.flush();
      output.close();
      input.close();
      
      return true;
      
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  //@Override
  public boolean apply2(Path src, Path dst, boolean encode) {
    Valid.off(src).forNull().fail(Path.class);
    Valid.off(dst).forNull().fail(Path.class);

    try {
      if(!Files.exists(dst))
        Files.createFile(dst);
      
      FileChannel rc = FileChannel.open(
          src, StandardOpenOption.READ);
      FileChannel wc = FileChannel.open(
          dst, StandardOpenOption.WRITE);
      
      int size = (encode ? ENCODE_BUFFER_SIZE 
          : DECODE_BUFFER_SIZE);
      
      ByteBuffer buf = ByteBuffer
          .allocateDirect(size);
      
      while(rc.read(buf) > 0) {
        buf.flip();
        buf = cryptCoder.apply(buf, encode);
        wc.write(buf);
        buf = ByteBuffer
            .allocateDirect(size);
      }
      
      rc.close();
      wc.close();
      return true;
      
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  @Override
  public boolean applyTo(Path src, PrintStream ps, boolean encode) {
    Valid.off(src).forNull().fail(Path.class);
    Valid.off(ps).forNull().fail(PrintStream.class);
    
    try {
      FileChannel rc = FileChannel.open(
          src, StandardOpenOption.READ);
      
      ByteBuffer buf = ByteBuffer
          .allocateDirect(DECODE_BUFFER_SIZE);
      
      ByteBufferConverter bconv = new ByteBufferConverter();
      StringByteConverter sconv = new StringByteConverter();
      Base64BufferCoder bbc = new Base64BufferCoder();
        
      while(rc.read(buf) > 0) {
        buf.flip();
        if(encode) {
          buf = cryptCoder.encode(buf);
          buf = bbc.encode(buf);
        } else {
          buf = cryptCoder.decode(buf);
        }
        byte[] bs = bconv.convert(buf);
        ps.println(sconv.reverse(bs));
        buf = ByteBuffer
            .allocateDirect(DECODE_BUFFER_SIZE);
      }
      
      rc.close();
      return true;
      
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  @Override
  public boolean applyFrom(ByteBuffer buf, Path path, boolean encode) {
    Valid.off(buf).forNull().fail(ByteBuffer.class);
    Valid.off(buf.remaining()).forLesserThan(1)
        .fail("No remaining bytes to read");
    Valid.off(path).forNull().fail(Path.class);
    
    if(!this.createIfNotExists(path, true))
      return false;
    
    buf = cryptCoder.apply(buf, encode);
    try (FileChannel fc = FileChannel.open(
        path, StandardOpenOption.WRITE)) {
      fc.write(buf);
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  @Override
  public boolean encode(Path src, Path dst) {
    return this.apply(src, dst, true);
  }
  
  
  @Override
  public boolean decode(Path src, Path dst) {
    return this.apply(src, dst, false);
  }
  
  
  public static void main(String[] args) throws IOException {
    CryptKey KEY = CryptKey.createWithUnsecurePasswordIV(
        "4c036dad7048d8d7d9fa1c42964c54ba5c676a2f53ba9ee9e18d909a997849f1",
          CryptAlgorithm.AES_CBC_256_PKCS5
    );
    
    CryptFileCoder fc = new CryptFileCoder(KEY);
    
    /**/
    Path txtfile = Paths.get("/storage/java/incheck/scp/sample.txt");
    Path encfile = Paths.get("/storage/java/incheck/scp/sample.bce");
    Path decfile = Paths.get("/storage/java/incheck/scp/sample.dec.txt");
    /**//*
    Path txtfile = Paths.get("/storage/java/incheck/scp/se.txt");
    Path encfile = Paths.get("/storage/java/incheck/scp/se.bce");
    Path decfile = Paths.get("/storage/java/incheck/scp/se.dec.txt");
    /**/
    
    if(Files.exists(encfile)) Files.delete(encfile);
    if(Files.exists(decfile)) Files.delete(decfile);
    
    fc.encode(txtfile, encfile);
    fc.decode(encfile, decfile);
  }
  
}
