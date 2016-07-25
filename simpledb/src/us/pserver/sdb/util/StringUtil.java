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

package us.pserver.sdb.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 26/12/2014
 */
public class StringUtil {

  
  public static byte[] writeUtf8(String str) {
    if(str == null)
      return null;
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try ( OutputStreamWriter os = new OutputStreamWriter(bos, "UTF-8") ) {
      os.write(str);
      os.flush();
    } catch(IOException e) {}
    return bos.toByteArray();
  }
  
  
  public static int writeUtf8(String str, OutputStream out) {
    if(str == null || out == null) return -1;
    
    try ( OutputStreamWriter os = new OutputStreamWriter(out, "UTF-8") ) {
      os.write(str);
      os.flush();
      return str.length();
    } catch(IOException e) {
      return -1;
    }
  }
  
  
  public static String readUtf8(byte[] bts) {
    if(bts == null)
      return null;
    
    String str = "";
    if(bts.length == 0) return str;
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      bos.write(bts);
      str = bos.toString("UTF-8");
    } catch(IOException e) {}
    return str;
  }

  
  public static String readUtf8(InputStream ist) {
    if(ist == null) return null;
    
    String str = "";
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      int read = -1;
      while((read = ist.read()) != -1) {
        bos.write(read);
      }
      str = bos.toString("UTF-8");
    } catch(IOException e) {}
    return str;
  }
  
}
