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

import com.jpower.log.SimpleLog;
import com.jpower.log.SimpleLogFactory;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/02/2012
 */
public class Path {
  
  private List<Screen> screens;
  
  private List<Condition> conds;
  
  private Condition stopCondition;
  
  private boolean stopConditionAttend;
  
  private long delay;
  
  private SimpleLog slog;
  
  
  public Path() {
    delay = 100;
    screens = new LinkedList<Screen>();
    conds = new LinkedList<Condition>();
    stopConditionAttend = false;
    slog = SimpleLogFactory.getInstance();
  }
  
  
  public void addScreen(Screen step) {
    if(step != null) screens.add(step);
  }
  
  
  public void addCondition(Condition sc) {
    if(sc != null) conds.add(sc);
  }


  public Condition getStopCondition() {
    return stopCondition;
  }


  public void setStopCondition(Condition stopCondition) {
    this.stopCondition = stopCondition;
  }


  public boolean isStopConditionAttended() {
    return stopConditionAttend;
  }


  public long getDelay() {
    return delay;
  }


  public Path setDelay(long delay) {
    this.delay = delay;
    return this;
  }


  public List<Screen> screens() {
    return screens;
  }


  public Path setScreens(LinkedList<Screen> screens) {
    this.screens = screens;
    return this;
  }


  public List<Condition> conditions() {
    return conds;
  }


  public Path setConditions(List<Condition> conds) {
    this.conds = conds;
    return this;
  }
  
  
  public boolean exec(Sisbb s) {
    if(s == null || !s.isConnected()) 
      return false;
    if(screens.isEmpty()) return false;
    
    s.clearError();
    
    Iterator<Screen> iscreens = screens.iterator();
    Screen screen = null;
    boolean stop = false;
    
    while(iscreens.hasNext() && !stop) {
      screen = iscreens.next();
      
      slog.logDebug(screen);
      
      if(screen == null || screen.getId() == null) {
        continue;
      }
      
      s.wait(screen);
      screen.execArgs(s);
      
      if(screen.getKey() != null)
        s.sendKey(screen.getKey());
      
      s.delay(delay);
      
      screen.execFields(s);
      
      if(stopCondition instanceof FieldCondition) {
        stopConditionAttend = stopCondition.attend(s);
      } else if(stopCondition instanceof ScreenCondition) {
        stopConditionAttend = stopCondition.attend(screen);
      }
    
      for(Condition sc : conds) {
        if(sc instanceof FieldCondition) {
          if(sc.attend(s) && sc.getAction() != null) {
            slog.logDebug("sc.attend("+s+"): "+true);
            if(sc.getAction() instanceof ActionStop) {
              stop = true;
            } else {
              slog.logDebug("Executing Action...");
              sc.getAction().exec(s);
            }
          }
        } else if(sc instanceof ScreenCondition) {
          if(sc.attend(screen) && sc.getAction() != null) {
            slog.logDebug("sc.attend("+screen.toString()+"): "+true);
            if(sc.getAction() instanceof ActionStop) {
              stop = true;
            } else {
              slog.logDebug("Executing Action...");
              sc.getAction().exec(s);
            }//else
          }//if
        }//else if
      }//for
    }//while
    
    return s.hasError();
  }
  
  
  public static void main(String[] args) {
    SimpleLog slog = SimpleLogFactory.getInstance();
    
    try {
      
      Session s = new Session("A");
      Sisbb sbb = new Sisbb(s);
      
      Path p = new Path();
      p.setDelay(2000);
      
      Screen sc = new Screen();
      sc.setId("B30");
      sc.addArg(new Field(new Point(15, 14), "Pessoal"));
      sc.addArg(new Field(new Point(16, 14), "25874102"));
      sc.setKey(Sisbb.ENTER_STR);
      p.addScreen(sc);
      
      sc = new Screen();
      sc.setId("B68");
      sc.addArg(new Field(new Point(21, 20), "31"));
      sc.setKey(Sisbb.ENTER_STR);
      p.addScreen(sc);
      
      sc = new Screen();
      sc.setId("P1001");
      sc.addArg(new Field(new Point(21, 20), "05"));
      sc.addField(new Field("Condition", new Point(21, 20), 2));
      sc.setKey(Sisbb.ENTER_STR);
      p.addScreen(sc);
      
      sc = new Screen();
      sc.setId("P6090");
      sc.addField(new Field("Saldo Férias", new Point(13, 39), 2));
      sc.addField(new Field("Abonos Ant.", new Point(11, 80), 1));
      sc.addField(new Field("Abonos Atu.", new Point(12, 80), 1));
      sc.addField(new Field("Folgas", new Point(15, 80), 1));
      sc.addField(new Field("Faltas", new Point(19, 78), 3));
      p.addScreen(sc);
      

      Path alt = new Path();
      sc = new Screen("P1001");
      sc.setKey(Sisbb.F3_STR);
      alt.addScreen(sc);
      
      sc = new Screen("B68");
      sc.setKey(Sisbb.F5_STR);
      alt.addScreen(sc);
      
      sc = new Screen("B30");
      sc.addArg(new Field(new Point(15, 14), "Pessoal"));
      sc.addArg(new Field(new Point(16, 14), "25874102"));
      sc.setKey(Sisbb.ENTER_STR);
      alt.addScreen(sc);
      
      sc = new Screen("B68");
      sc.addArg(new Field(new Point(21, 20), "31"));
      sc.setKey(Sisbb.ENTER_STR);
      alt.addScreen(sc);
      
      sc = new Screen("P1001");
      sc.addArg(new Field(new Point(21, 20), "05"));
      sc.setKey(Sisbb.ENTER_STR);
      alt.addScreen(sc);
      
      sc = new Screen("P1001");
      sc.addField(new Field(new Point(21, 20), "F5"));
      ModelScreenCondition cond = 
          new ModelScreenCondition(sc, new ActionPath(alt));
      
      
      p.addCondition(cond);
      p.exec(sbb);
      
      
      Storage st = new Storage() {
        public boolean store(Path p) {
          if(p == null) return false;
          for(Screen s : p.screens())
            this.store(s);
          return true;
        }
        public boolean store(Screen s) {
          if(s == null) return false;
          for(Field f : s.fields())
            this.store(f);
          return true;
        }
        public boolean store(Field f) {
          if(f == null) return false;
          System.out.println("["+f.getName()+"]: "+f.getContent());
          return true;
        }
        public boolean store(Object o) {
          if(o == null) return false;
          if(o instanceof Field) return this.store((Field) o);
          if(o instanceof Screen) return this.store((Screen) o);
          if(o instanceof Path) return this.store((Path) o);
          System.out.println("["+o.toString()+"]: "+o.hashCode());
          return true;
        }
      };
      
      st.store(p);
      
    } catch(Exception ex) {
      ex.printStackTrace();
      slog.logWarning(ex.getMessage());
      slog.logInfo((ex.getCause() != null ? ex.getCause().getMessage() : ""));
    }
  }
  
}
