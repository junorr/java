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
package com.jpower.nettyserver;

import biz.source_code.base64Coder.Base64Coder;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/06/2012
 */
public class DEScrypto {
  private Cipher cipher;
  private byte[] encryptKey;
  private KeySpec keySpec;
  private SecretKeyFactory secretKeyFactory;
  private SecretKey secretKey;
 
  /**
   * Method that create a new instance of class.
   * @return
   * @throws InvalidKeyException
   * @throws UnsupportedEncodingException
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeySpecException
   */
  public static DEScrypto newInstance( String key ) {
    try {
      return new DEScrypto(key);
    } catch(Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
 
  /**
   * Default Constructor.
   * @throws UnsupportedEncodingException
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   * @throws InvalidKeySpecException
   */
  private DEScrypto( String key ) throws UnsupportedEncodingException, NoSuchAlgorithmException,
    NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
    key = this.setKeySize(key);
    encryptKey = key.getBytes( "UTF-8" );
    cipher = Cipher.getInstance( "DESede" );
    keySpec = new DESedeKeySpec( encryptKey );
    secretKeyFactory = SecretKeyFactory.getInstance( "DESede" );
    secretKey = secretKeyFactory.generateSecret( keySpec );
  }
  
  
  private String setKeySize( String key ) {
    if(key == null || key.trim().isEmpty()) 
      return null;
    
    if(key.length() >= 24) return key;
    int n = 24 - key.length();
    String c = ".";
    for(int i = 0; i < n; i++) {
      key += c;
    }
    System.out.println(" * Key       : ["+ key+ "]");
    return key;
  }
  
 
  /**
   * Method that encrypts a value.
   * @param value
   * @return
   */
  public String encrypt( String value ) {
    try {
      cipher.init( Cipher.ENCRYPT_MODE, secretKey );
      byte[] cipherText = cipher.doFinal( value.getBytes( "UTF-8" ) );
      return new String(Base64Coder.encode(cipherText));
    } catch(Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  
  /**
   * Method that encrypts a value.
   * @param value
   * @return
   */
  public byte[] encrypt( byte[] bytes ) {
    try {
      cipher.init( Cipher.ENCRYPT_MODE, secretKey );
      return cipher.doFinal( bytes );
    } catch(Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  
  /**
   * Methot that decrypts a value.
   * @param value
   * @return
   */
  public String decrypt( String value ) {
    try {
      cipher.init( Cipher.DECRYPT_MODE, secretKey );
      byte[] decipherText = cipher.doFinal( Base64Coder.decode(value) );
      return new String( decipherText );
    } catch(Exception ex) {
      return null;
    }
  }
  
  
  /**
   * Methot that decrypts a value.
   * @param value
   * @return
   */
  public byte[] decrypt( byte[] bytes ) {
    try {
      cipher.init( Cipher.DECRYPT_MODE, secretKey );
      return cipher.doFinal( bytes );
    } catch(Exception ex) {
      return null;
    }
  }
  
  
  public static void main(String[] args) {
    DEScrypto des = DEScrypto.newInstance("inadonuj");
    String text = "abracadabra";
    System.out.println(" * Text      : "+ text);
    String cipherText = des.encrypt(text);
    System.out.println(" * Base 64   : "+ Base64Coder.encodeString(text));
    System.out.println(" * CipherText: "+ cipherText);
    text = des.decrypt(cipherText);
    System.out.println(" * Text      : "+ text);
  }
 
}