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
package com.jpower.sisbb;

import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/04/2012
 */
public class FieldCondition implements Condition<Sisbb> {

  private List<Field> fields;
  
  private Action act;
  
  
  public FieldCondition() {
    fields = new LinkedList<Field>();
    act = null;
  }
  
  
  public void addField(Field f) {
    if(f != null)
      fields.add(f);
  }
  
  
  public List<Field> fields() {
    return fields;
  }
  
  
  public void setFields(List<Field> f) {
    if(f != null && !f.isEmpty())
      fields = f;
  }
  

  @Override
  public boolean attend(Sisbb e) {
    if(e == null || !e.isConnected())
      return false;
    
    int attends = 0;
    for(Field f : fields) {
      if(f != null && f.getContent() != null) {
        Field c = new Field(null, f.getPosition(), f.getLength());
        c.fillContent(e);
        if(f.equals(c)) attends++;
      }
    }
    
    return attends == fields.size();
  }


  @Override
  public Action getAction() {
    return act;
  }


  @Override
  public void setAction(Action a) {
    if(a != null)
      act = a;
  }
  
}
