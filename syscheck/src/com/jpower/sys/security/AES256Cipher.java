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

import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @version 0.0 - 06/02/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class AES256Cipher {

  public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
  
  static {
    try {
      Field field = Class.forName("javax.crypto.JceSecurity").
          getDeclaredField("isRestricted");
      field.setAccessible(true);
      field.set(null, java.lang.Boolean.FALSE);
    } catch (Exception ex) {
      ex.printStackTrace();
    }  
  }
  
  
  private Cipher encipher;
  
  private Cipher decipher;
  
  private Password pass;
  
  
  public AES256Cipher(Password p) {
    if(p == null || p.isEmpty())
      throw new IllegalArgumentException(
          "Invalid password: "+ p);
    pass = p;
    encipher = null;
    decipher = null;
  }
  
  
  private byte[] get16BytesLength(byte[] bs) {
    if(bs == null || bs.length <= 16)
      return bs;
    
    byte[] nb = new byte[16];
    for(int i = 0; i < nb.length; i++) {
      nb[i] = bs[i];
    }
    return nb;
  }
  
  
  private void initEncipher() {
    try {
      encipher = Cipher.getInstance(CIPHER_ALGORITHM);
      IvParameterSpec iv = new IvParameterSpec(
          get16BytesLength(pass.getBytes()));
      encipher.init(Cipher.ENCRYPT_MODE, pass.getAES256KeySpec(), iv);
    } catch(InvalidKeyException | NoSuchAlgorithmException
        | NoSuchPaddingException | InvalidAlgorithmParameterException ex) {
      ex.printStackTrace();
      throw new IllegalStateException("Error creating cipher", ex);
    }
  }
  
  
  private void initDecipher() {
    try {
      decipher = Cipher.getInstance(CIPHER_ALGORITHM);
      IvParameterSpec iv = new IvParameterSpec(
          get16BytesLength(pass.getBytes()));
      decipher.init(Cipher.DECRYPT_MODE, pass.getAES256KeySpec(), iv);
    } catch(InvalidKeyException | NoSuchAlgorithmException
        | NoSuchPaddingException | InvalidAlgorithmParameterException ex) {
      ex.printStackTrace();
      throw new IllegalStateException("Error creating cipher", ex);
    }
  }
  
  
  public byte[] encrypt(byte[] content) {
    if(content == null || content.length == 0)
      return null;
    
    if(encipher == null)
      this.initEncipher();
    
    try {
      return encipher.doFinal(content);
      
    } catch(IllegalBlockSizeException | BadPaddingException ex) {
      ex.printStackTrace();
      return null;
    }
  }
  
  
  public byte[] decrypt(byte[] content) {
    if(content == null || content.length == 0)
      return null;
    
    if(decipher == null)
      this.initDecipher();
    
    try {
      return decipher.doFinal(content);
      
    } catch(IllegalBlockSizeException | BadPaddingException ex) {
      ex.printStackTrace();
      return null;
    }
  }
  
  
  public static void main(String[] args) {
    String data = "Data to encrypt";
    String encData = null;
    byte[] encBytes = null;
    Password pass = Password.rawPassword(
        "673dc5cfe5376ca566034340c83ff5ffac8f0b7f54fc4e40b3836b90481803f1");
    AES256Cipher aes = new AES256Cipher(pass);
    
    encBytes = aes.encrypt(data.getBytes());
    System.out.println("* data    : "+ data);
    data = null;
    System.out.println("* encBytes: "+ new String(encBytes));
    System.out.println("* hexBytes: "+ SHA256.toHexString(encBytes));
    data = new String(aes.decrypt(encBytes));
    System.out.println("* decrypt : "+ data);
  }
  
}
