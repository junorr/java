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

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.FileCoder;
import us.pserver.cdr.FileUtils;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64BufferCoder;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.nullbuffer;
import static us.pserver.chk.Checker.nullstr;


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
  
  
  /**
   * Construtor padrão que recebe a chave de criptografia.
   * @param key chave de criptografia <code>CryptKey</code>.
   */
  public CryptFileCoder(CryptKey key) {
    if(key == null || key.getSpec() == null)
      throw new IllegalArgumentException(
          "Invalid CryptKey: "+ key);
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
    nullstr(strPath);
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
    nullarg(Path.class, p);
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
    nullarg(Path.class, src);
    nullarg(Path.class, dst);

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
    nullarg(Path.class, src);
    nullarg(PrintStream.class, ps);
    
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
    nullbuffer(buf);
    nullarg(Path.class, path);
    
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
  
  
  public static void main(String[] args) {
    CryptKey KEY = 
      new CryptKey("4c036dad7048d8d7d9fa1c42964c54ba5c676a2f53ba9ee9e18d909a997849f1",
      CryptAlgorithm.DESede_CBC_PKCS5);
    
    CryptFileCoder fc = new CryptFileCoder(KEY);
    /*
    fc.encode(
        fc.path("f:/java/incheck/scp/ss.txt"),
        fc.path("f:/java/incheck/scp/ss.bce"));
    fc.decode(
        fc.path("f:/java/incheck/scp/ss.bce"), 
        fc.path("f:/java/incheck/scp/ss2.txt"));
    */
    fc.encode(
        FileUtils.path("d:/picture_low.jpg"),
        FileUtils.path("d:/picture_low.des"));
    fc.decode(
        FileUtils.path("d:/picture_low.des"), 
        FileUtils.path("d:/picture_low2.jpg"));
  }
  
}
