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

package us.pserver.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


/**
 * Classe com métodos utilitários para manipulação 
 * de streams.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public abstract class StreamUtils {
  
  /**
   * Bytes de caracteres de quebra de linha 
   * (<code>LNR = {10, 13}</code>).
   */
  public static final byte[] LNR = {10, 13};
  
  /**
   * Bytes de caracteres 'EOF' de fim de transmissão 
   * (<code>BYTES_EOF = {69, 79, 70}</code>).
   */
  public static final byte[] BYTES_EOF = {69, 79, 70};
  
  /**
   * <code>String EOF = "EOF"</code>.
   */
  public static final String EOF = "EOF";
  
  /**
   * <code>UTF8 = "UTF-8"</code>.
   */
  public static final String UTF8 = "UTF-8";
  
  /**
   * Tamanho do buffer (<code>BUFFER_SIZE = 1024</code>).
   */
  public static int BUFFER_SIZE = 1024;
  
  
  /**
   * Transfere o conteúdo entre dois streams até final
   * ou até encontrar os bytes relativos ao fim de transmissão 
   * <code>EOF</code>.
   * @param in <code>InputStream</code>
   * @param out <code>OutputStream</code>
   * @return Número total de bytes transferidos.
   * @throws IOException caso ocorra erro na transferência.
   */
  public static long transfer(InputStream in, OutputStream out) throws IOException {
    long total = -1;
    if(in == null || out == null) return total;
    
    int read = 0;
    total = read;
    byte[] buf = new byte[BUFFER_SIZE];
    
    while((read = in.read(buf)) > 0) {
      total += read;
      out.write(buf, 0, read);
      int len = (read < 30 ? read : 30);
      String str = new String(buf, read -len, len);
      if(read < buf.length && str.contains(EOF))
        break;
    }
    return total;
  }
  
  
  /**
   * Transfere o conteúdo entre dois streams até que seja
   * encontrada a <code>String</code> no conteúdo da 
   * transmissão, ou até o final da transmissão.
   * @param in <code>InputStream</code>
   * @param out <code>OutputStream</code>
   * @param until <code>String</code> de condição de 
   * parada de transferência de conteúdo.
   * @return Número total de bytes transferidos.
   * @throws IOException caso ocorra erro na transferência.
   */
  public static long transferUntil(InputStream in, OutputStream out, String until) throws IOException {
    long total = -1;
    if(in == null || out == null 
        || until == null 
        || until.isEmpty()) return total;
    
    int read = 0;
    total = read;
    byte[] buf = new byte[BUFFER_SIZE];
    
    while((read = in.read(buf)) > 0) {
      total += read;
      int len = (read < until.length() ? read : until.length());
      String str = new String(buf, read -len, len);
      
      if(read < buf.length && str.contains(until)) {
        int ieof = str.indexOf(until);
        out.write(buf, 0, read - (len-ieof));
        break;
      } 
      else out.write(buf, 0, read);
    }
    return total;
  }
  
  
  /**
   * Escreve uma <code>String</code> codificada em <code>UTF-8</code>
   * no stream de saída.
   * @param str <code>String</code> a ser escrita.
   * @param out <code>OuputStream</code>.
   * @throws IOException caso ocorra erro na escrita.
   */
  public static void write(String str, OutputStream out) throws IOException {
    if(str == null || out == null)
      return;
    
    out.write(bytes(str));
    out.flush();
  }
  
  
  /**
   * Retorna os bytes relativos ao conteúdo
   * da <code>String</code> codificada em <code>UTF-8</code>.
   * @param str <code>String</code>.
   * @return <code>byte[]</code>;
   */
  public static byte[] bytes(String str) {
    if(str == null || str.isEmpty())
      return new byte[0];
    try {
      return str.getBytes(UTF8);
    } 
    catch(UnsupportedEncodingException e) {
      return new byte[0];
    }
  }
  
  
  /**
   * Lê e retorna o conteúdo do stream que esteja
   * delimitado entre duas <code>Strings</code>, 
   * <code>start</code> e <code>end</code>.
   * @param in <code>InputStream</code>
   * @param start <code>String</code> demarcando início do conteúdo.
   * @param end <code>String</code> demarcando o final do conteúdo.
   * @return O conteúdo <code>String</code> lido.
   * @throws IOException caso ocorra erro na leitura.
   */
  public static String readBetween(InputStream in, String start, String end) throws IOException {
    int read = 1;
    int ic = -1;
    int ie = -1;
    StringBuilder sb = new StringBuilder();
    while((read = in.read()) != -1) {
      sb.append((char) read);
      ic = sb.indexOf(start);
      ie = sb.indexOf(end, ic);
      if(ic > 0 && ie > ic) break;
    }
    if(ic < 0 || ie <= ic) return null;
    ic += start.length();
    return sb.substring(ic, ie);
  }


  /**
   * Lê e descarta o conteúdo do stream até que 
   * seja encontrada a <code>String</code> fornecida.
   * @param in <code>InputStream</code>
   * @param str <code>String</code> delimitadora.
   * @return <code>true</code> se a <code>String</code>
   * delimitadora for encontrada, <code>false</code>
   * caso contrário.
   * @throws IOException Caso ocorra erro na leitura do stream.
   */
  public static boolean readUntil(InputStream in, String str) throws IOException {
    int read = 1;
    int ic = -1;
    StringBuilder sb = new StringBuilder();
    while((read = in.read()) != -1) {
      sb.append((char) read);
      ic = sb.indexOf(str);
      if(ic > 0) return true;
    }
    return false;
  }


  /**
   * Lê e descarta o conteúdo do stream até que 
   * seja encontrada a primeira <code>String</code> 
   * fornecida (<code>str</code>: retornando <code>true</code> nesse caso),
   * ou até que seja encontrada a segunda <code>String</code> 
   * fornecida (<code>orFalse</code>: retornando <code>false</code>).
   * @param in <code>InputStream</code>
   * @param str primeira <code>String</code> delimitadora.
   * @param orFalse segunda <code>String</code> delimitadora.
   * @return <code>true</code> se a primeira <code>String</code>
   * delimitadora for encontrada, <code>false</code>
   * caso a segunda ou nenhuma seja encontrada.
   * @throws IOException Caso ocorra erro na leitura do stream.
   */
  public static boolean readUntil(InputStream in, String str, String orFalse) throws IOException {
    int read = 1;
    int ic = -1;
    StringBuilder sb = new StringBuilder();
    while((read = in.read()) != -1) {
      sb.append((char) read);
      ic = sb.indexOf(str);
      if(ic > 0) return true;
      ic = sb.indexOf(orFalse);
      if(ic > 0) return false;
    }
    return false;
  }

}
