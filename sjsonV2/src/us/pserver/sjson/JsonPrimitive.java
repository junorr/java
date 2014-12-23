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

package us.pserver.sjson;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/12/2014
 */
public class JsonPrimitive {

  private Object primitive;
  
  
  public JsonPrimitive() {
    primitive = null;
  }
  
  
  public JsonPrimitive(Object obj) {
    if(!isPrimitive(obj))
      throw new IllegalArgumentException("Object is not a valid primitive: "+ obj);
    primitive = obj;
  }
  
  
  public JsonPrimitive set(Object obj) {
    if(!isPrimitive(obj))
      throw new IllegalArgumentException("Object is not a valid primitive: "+ obj);
    primitive = obj;
    return this;
  }
  
  
  public Object get() {
    return primitive;
  }
  
  
  public static boolean isPrimitive(Object o) {
    return String.class.isAssignableFrom(o.getClass())
        || Character.class.isAssignableFrom(o.getClass())
        || Number.class.isAssignableFrom(o.getClass())
        || Boolean.class.isAssignableFrom(o.getClass());
  }
  
  
  public static boolean isQuotaNeeded(Object o) {
    return String.class.isAssignableFrom(o.getClass())
        || Character.class.isAssignableFrom(o.getClass());
  }
  
  
  @Override
  public String toString() {
    return (isQuotaNeeded(primitive) ? "\"" : "") 
        + primitive 
        + (isQuotaNeeded(primitive) ? "\"" : "");
  }
  
  
  public static void main(String[] args) {
    System.out.println("* isPrimitive( "+ String.valueOf(5)+ " ): "+ isPrimitive(5));
  }
  
}
