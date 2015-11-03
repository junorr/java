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

package us.pserver.fpack.test;

import com.cedarsoftware.util.io.JsonReader;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/11/2015
 */
public class TestJson {

  
  public static void main(String[] args) throws IOException {
    String js = "{\"@type\":\"us.pserver.fpack.FPackFileEntry\",\"values\":{\"@id\":1,\"@type\":\"java.util.HashMap\",\"owner\":\"BUILTIN\\\\Administrators\",\"path\":\"D:\\\\docs\\\\auto-logon.reg\",\"lastModifiedTime\":1318949004000,\"creationTime\":1429978958954,\"hidden\":false,\"readonly\":false,\"directory\":false},\"name\":\"auto-logon.reg\",\"position\":\"0000000000000001302\",\"size\":\"0000000000000000440\"}";
    System.out.println("* js: '"+ js+ "'");
    Object ob = JsonReader.jsonToJava(js);
    System.out.println("* ob: "+ ob);
  }
  
}
