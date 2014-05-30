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

package com.jpower.stunnel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 27/07/2012
 */
public class ByteTest {

  
  public static void main(String[] args) {
    byte b = 104;
    System.out.println(b+" = '"+(char)b+"'");
    b = 116;
    System.out.println(b+" = '"+(char)b+"'");
    b = 109;
    System.out.println(b+" = '"+(char)b+"'");
    b = 108;
    System.out.println(b+" = '"+(char)b+"'");
    b = 62;
    System.out.println(b+" = '"+(char)b+"'");
    b = 32;
    System.out.println(b+" = '"+(char)b+"'");
    b = 10;
    System.out.println(b+" = '"+(char)b+"'");
    b = 0;
    System.out.println(b+" = '"+(char)b+"'");
    b = (byte) '\n';
    System.out.println("\\n = '"+b+"'");
    b = (byte) '\r';
    System.out.println("\\r = '"+b+"'");
    b = (byte) ' ';
    System.out.println("' ' = '"+b+"'");
    b = (byte) "".charAt(0);
    System.out.println("'' = '"+b+"'");
  }
  
  
}
