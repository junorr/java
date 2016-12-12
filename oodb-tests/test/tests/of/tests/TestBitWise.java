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

package tests.of.tests;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public class TestBitWise {

  
  public static void main(String[] args) {
    NumberFormat nf = new DecimalFormat("#,000");
    
    int i = 1;
    String b = Integer.toBinaryString(i);
    String x = Integer.toHexString(i);
    long l = Long.parseLong(b);
    System.out.println("* i = "+ i);
    System.out.println("* b = "+ b);
    System.out.println("* x = "+ x);
    System.out.println("* l = "+ nf.format(l));
    System.out.println();
    
    i = 2;
    b = Integer.toBinaryString(i);
    x = Integer.toHexString(i);
    l = Long.parseLong(b);
    System.out.println("* i = "+ i);
    System.out.println("* b = "+ b);
    System.out.println("* x = "+ x);
    System.out.println("* l = "+ nf.format(l));
    System.out.println();
    
    i = 4;
    b = Integer.toBinaryString(i);
    x = Integer.toHexString(i);
    l = Long.parseLong(b);
    System.out.println("* i = "+ i);
    System.out.println("* b = "+ b);
    System.out.println("* x = "+ x);
    System.out.println("* l = "+ nf.format(l));
    System.out.println();
    
    i = 0b110000000;
    b = Integer.toBinaryString(i);
    x = Integer.toHexString(i);
    l = Long.parseLong(b);
    System.out.println("--- 600 ---");
    System.out.println("* i = "+ i);
    System.out.println("* b = "+ b);
    System.out.println("* x = "+ x);
    System.out.println("* l = "+ nf.format(l));
    System.out.println();
    
    i = 0b111101101;
    b = Integer.toBinaryString(i);
    x = Integer.toHexString(i);
    l = Long.parseLong(b);
    System.out.println("--- 755 ---");
    System.out.println("* i = "+ i);
    System.out.println("* b = "+ b);
    System.out.println("* x = "+ x);
    System.out.println("* l = "+ nf.format(l));
    System.out.println();
    
    i = 0b111101001;
    b = Integer.toBinaryString(i);
    x = Integer.toHexString(i);
    l = Long.parseLong(b);
    System.out.println("--- 751 ---");
    System.out.println("* i = "+ i);
    System.out.println("* b = "+ b);
    System.out.println("* x = "+ x);
    System.out.println("* l = "+ nf.format(l));
    System.out.println();
    
    i = 0b1000000;
    b = Integer.toBinaryString(i);
    x = Integer.toHexString(i);
    l = Long.parseLong(b);
    System.out.println("--- 100 ---");
    System.out.println("* i = "+ i);
    System.out.println("* b = "+ b);
    System.out.println("* x = "+ x);
    System.out.println("* l = "+ nf.format(l));
    System.out.println();
    
    i = 0b110110110;
    b = Integer.toBinaryString(i);
    x = Integer.toHexString(i);
    l = Long.parseLong(b);
    System.out.println("--- 666 ---");
    System.out.println("* i = "+ i);
    System.out.println("* b = "+ b);
    System.out.println("* x = "+ x);
    System.out.println("* l = "+ nf.format(l));
    System.out.println();
    
    
    int o = i >> 6;
    int g = (i >> 3) & 0b111;
    int a = i & 0b111;
    System.out.println("* owner: "+ o);
    System.out.println("* group: "+ g);
    System.out.println("* other: "+ a);
  }
  
}
