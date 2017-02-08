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

package br.com.bb.disec.micros.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/02/2017
 */
public class TestGson {

  private final int a;
  
  private final String b;
  
  private final List<Double> c;
  
  
  public TestGson() {
    a = 0;
    b = null;
    c = new LinkedList<>();
  }
  
  
  public TestGson(int a, String b, List<Double> c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }


  public TestGson(int a, String b) {
    this.a = a;
    this.b = b;
    c = new LinkedList<>();
  }


  public int getA() {
    return a;
  }


  public String getB() {
    return b;
  }


  public List<Double> getC() {
    return c;
  }
  
  
  public TestGson add(double d) {
    c.add(d);
    return this;
  }


  @Override
  public String toString() {
    return "TestGson{" + "a=" + a + ", b=" + b + ", c=" + c + '}';
  }
  
  
  
  public static void main(String[] args) {
    TestGson tg = new TestGson(1, "2").add(3.0).add(4.0);
    System.out.println(tg);
    Gson g = new GsonBuilder().setPrettyPrinting().create();
    String js = g.toJson(tg);
    System.out.println(js);
    System.out.println(g.fromJson(js, TestGson.class));
  }
  
}
