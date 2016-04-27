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

package us.pserver.zerojs.jen;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/04/2016
 */
public class ObjectGenerator implements Generator<String> {

  private final JsonTypeGenerator types;
  
  private final JsonStringGenerator names;
  
  private final int length;
  
  
  public ObjectGenerator(int minJsonLength) {
    types = new JsonTypeGenerator();
    names = new JsonStringGenerator(new JsonNameGenerator(7));
    this.length = minJsonLength;
  }
  
  
  private String genPair() {
    return new StringBuilder()
        .append(names.generate())
        .append(':')
        .append(types.generate().getGenerator().generate())
        .toString();
  }
  
  
  @Override
  public String generate() {
    StringBuilder bld = new StringBuilder()
        .append('{');
    do {
      char last = bld.charAt(bld.length()-1);
      if(last != '{' && last != '[' && last != ',') {
        bld.append(',');
      }
      bld.append(genPair());
      if(bld.length()+1 < length) {
        bld.append(',');
      }
    } while(bld.length() < length);
    return bld.append('}').toString();
  }
  
}
