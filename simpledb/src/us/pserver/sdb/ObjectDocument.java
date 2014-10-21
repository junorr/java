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

package us.pserver.sdb;

import com.jpower.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/10/2014
 */
public class ObjectDocument {

  private Reflector ref;
  
  public ObjectDocument() {
    ref = new Reflector();
  }
  
  
  public Document toDocument(Object obj) {
    if(obj == null) return null;
    Document doc = new Document(obj.getClass().getName());
    return doc.put("obj", obj);
  }
  
  
  public Object fromDocument(Document doc) {
    if(doc == null || doc.map().containsKey("obj"))
      return null;
    return doc.get("obj");
  }
  
}
