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

package us.pserver.psf.func;

import java.awt.event.InputEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSUnsupportedException;
import murlen.util.fscript.FSVarExtension;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 06/02/2015
 */
public class Globals implements FSVarExtension {
  
  public static final String
      BTN1 = "BTN1",
      BTN2 = "BTN2",
      BTN3 = "BTN3",
      BUTTON1 = "BUTTON1",
      BUTTON2 = "BUTTON2",
      BUTTON3 = "BUTTON3",
      
      DIALOG_INFO = "DIALOG_INFO",
      DIALOG_INPUT = "DIALOG_INPUT",
      DIALOG_QUESTION = "DIALOG_QUESTION",
      DIALOG_WARNING = "DIALOG_WARNING",
      DIALOG_ERROR = "DIALOG_ERROR",
      
      DLINFO = "DLINFO",
      DLINPUT = "DLINPUT",
      DLQUESTION = "DLQUESTION",
      DLWARNING = "DLWARNING",
      DLERROR = "DLERROR",
      
      EOF = "EOF",
      
      KEY_LN = "LN",
      KEY_CSV = "CSV",
      KEY_SPACE = "SPACE",
      VAL_LN = (File.separatorChar == '/' ? "\n" : "\r\n"),
      VAL_CSV = ";",
      VAL_SPACE = " ",
      
      NOW = "NOW",
      
      UICENTER = "UICENTER",
      UILEFT = "UILEFT",
      UIRIGHT = "UIRIGHT",
      
      SCREEN_WIDTH = "SCREENW",
      SCREEN_HEIGHT = "SCREENH",
      
      YES = "YES",
      NO = "NO";
  
  
  public static final int 
      BTI1 = InputEvent.BUTTON1_DOWN_MASK,
      BTI2 = InputEvent.BUTTON2_DOWN_MASK,
      BTI3 = InputEvent.BUTTON3_DOWN_MASK;
  
  
  private Map<String, Object> vars;
  
  private KeyMap keys;
  
  
  public Globals() {
    keys = new KeyMap();
    vars = new HashMap<>();
    vars.put(EOF, EOF);
    vars.put(BTN1, BTI1);
    vars.put(BTN2, BTI2);
    vars.put(BTN3, BTI3);
    vars.put(BUTTON1, BTI1);
    vars.put(BUTTON2, BTI2);
    vars.put(BUTTON3, BTI3);
    vars.put(DIALOG_ERROR, DIALOG_ERROR);
    vars.put(DIALOG_INFO, DIALOG_INFO);
    vars.put(DIALOG_INPUT, DIALOG_INPUT);
    vars.put(DIALOG_QUESTION, DIALOG_QUESTION);
    vars.put(DIALOG_WARNING, DIALOG_WARNING);
    vars.put(DLERROR, DLERROR);
    vars.put(DLINFO, DLINFO);
    vars.put(DLINPUT, DLINPUT);
    vars.put(DLQUESTION, DLQUESTION);
    vars.put(DLWARNING, DLWARNING);
    vars.put(KEY_CSV, VAL_CSV);
    vars.put(KEY_LN, VAL_LN);
    vars.put(KEY_SPACE, VAL_SPACE);
    vars.put(NO, NO);
    vars.put(NOW, NOW);
    vars.put(SCREEN_HEIGHT, SCREEN_HEIGHT);
    vars.put(SCREEN_WIDTH, SCREEN_WIDTH);
    vars.put(UICENTER, UICENTER);
    vars.put(UILEFT, UILEFT);
    vars.put(UIRIGHT, UIRIGHT);
    vars.put(YES, YES);
  }


  @Override
  public Object getVar(String name) throws FSException {
    if(name == null || name.trim().isEmpty()
        || (!vars.containsKey(name) 
        && !keys.getMap().containsKey(name)))
      throw new FSUnsupportedException("No such var: "+ name);
    
    if(vars.containsKey(name))
      return vars.get(name);
    else
      return keys.getKeyCode(name);
  }


  @Override
  public void setVar(String name, Object value) throws FSException {
    throw new FSUnsupportedException("Read only variable: "+ name);
  }
  
  
  public void addTo(FSFastExtension fe) {
    if(fe == null) return;
    Iterator<String> it = vars.keySet().iterator();
    while(it.hasNext())
      fe.addVarExtension(it.next(), this);
    
    it = keys.getMap().keySet().iterator();
    while(it.hasNext())
      fe.addVarExtension(it.next(), this);
  }

}
