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

package us.pserver.rob.http;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/04/2015
 */
public interface MessageConsts {

  public static final String
      
      START_CRYPT_KEY = "<ckey>",
      END_CRYPT_KEY = "</ckey>",
      
      START_XML = "<xml>",
      END_XML = "</xml>",
      
      START_CONTENT = "<cnt>",
      END_CONTENT = "</cnt>",
      
      START_ROB = "<rob>",
      END_ROB = "</rob>",
      
      START_STREAM = "<stream>",
      END_STREAM = "</stream>",
      
      UTF8 = "UTF-8";
  
  
  public static byte[] getUTF8(String str) {
    if(str == null || str.trim().isEmpty())
      return null;
    try {
      return str.getBytes(UTF8);
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }
  
}
