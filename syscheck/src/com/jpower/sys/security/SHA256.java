/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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


package com.jpower.sys.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @version 0.0 - 08/02/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class SHA256 {
  
  public transient static final String HASH_ALGORITHM = "SHA-256";
  
  
  public static String toHexString(byte[] array) {
    if(array == null || array.length == 0)
      return null;
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < array.length; i++) {
      int high = ((array[i] >> 4) & 0xf) << 4;
      int low = array[i] & 0xf;
      if(high == 0) sb.append("0");
      sb.append(Integer.toHexString(high | low));
    }
    return sb.toString();
  }
  
  
  public static byte[] fromHexString(String hex) {
    if(hex == null || hex.isEmpty()) return null;
    int len = hex.length();
    byte[] bytes = new byte[len / 2];
    for(int i = 0; i < len; i += 2) {
      bytes[i / 2] = (byte) (
          (Character.digit(hex.charAt(i), 16) << 4)
          + Character.digit(hex.charAt(i + 1), 16));
    }
    return bytes;
  }
  
  
  public static byte[] hash(String s) {
    if(s == null || s.isEmpty())
      return null;
    
    try {
      MessageDigest md = MessageDigest
          .getInstance(HASH_ALGORITHM);
      
      md.update(s.getBytes());
      return md.digest();
      
    } catch(NoSuchAlgorithmException ex) {
      ex.printStackTrace();
      return null;
    }
  }

}
