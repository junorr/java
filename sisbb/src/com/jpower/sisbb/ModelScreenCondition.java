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
package com.jpower.sisbb;

import com.jpower.log.SimpleLogFactory;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 03/02/2012
 */
public class ModelScreenCondition implements ScreenCondition {
  
  private Screen model;
  
  private Action action;
  
  
  public ModelScreenCondition() {
    model = null;
    action = null;
  }
  
  
  public ModelScreenCondition(Screen model, Action action) {
    this.setModel(model);
    this.setAction(action);
  }


  public Screen getModel() {
    return model;
  }


  public void setModel(Screen model) {
    this.model = model;
  }


  public boolean attend(Screen s) {
    if(s == null || model == null 
        || model.getId() == null)
      return false;
    
    if(model.getId().equals(s.getId())) {
      int attends = 0;
      for(Field mf : model.fields()) {
        for(Field f : s.fields()) {
          if(mf.getPosition().equals(f.getPosition())
            && mf.equals(f)) attends++;
        }
      }
      if(attends == model.fields().size()) {
        SimpleLogFactory.getInstance()
            .logDebug("ScreenCondition.attend( "+
            s+" ): [true]");
        return true;
      }
    }
    
    SimpleLogFactory.getInstance()
        .logDebug("ScreenCondition.attend( "+
        s+" ): [false]");
    return false;
  }


  public Action getAction() {
    return action;
  }


  public void setAction(Action a) {
    this.action = a;
  }

}
