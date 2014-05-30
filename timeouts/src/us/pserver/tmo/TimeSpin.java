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

package us.pserver.tmo;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/04/2014
 */
public class TimeSpin extends JSpinner implements ChangeListener {

  private int min, max;
  
  
  public TimeSpin(int value, int min, int max) {
    super();
    if(value < min || value > max || min > max)
      throw new IllegalArgumentException(
          "Invalid initial values: value="
          + value+ ", min="+ min+ ", max="+ max);
    
    this.min = min;
    this.max = max;
    this.setValue(value);
    this.addChangeListener(this);
  }
  
  
  public int value() {
    return (int) this.getValue();
  }


  @Override
  public void stateChanged(ChangeEvent e) {
    int value = value();
    if(value > max) {
      this.setValue(min);
    }
    else if(value < min) {
      this.setValue(max);
    }
  }
  
}
