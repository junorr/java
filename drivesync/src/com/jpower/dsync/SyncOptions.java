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

package com.jpower.dsync;

import com.jpower.date.SimpleDate;
import com.jpower.scron.TimeUnit;
import java.nio.file.Path;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 08/05/2013
 */
public class SyncOptions {
  
  private Path source;
  
  private Path target;
  
  private SyncEvent event;
  
  private Path modfile;
  
  private SimpleDate date;
  
  private int repeatInterval;
  
  private TimeUnit unit;

  private boolean updateTarget;
  
  private boolean autoSync;
  
  private boolean showGui;
  
  private boolean printConsole;
  
  
  public SyncOptions() {}


  public Path getSource() {
    return source;
  }


  public SyncOptions setSource(Path source) {
    if(source != null)
      this.source = source;
    return this;
  }


  public Path getTarget() {
    return target;
  }


  public SyncOptions setTarget(Path target) {
    if(target != null)
      this.target = target;
    return this;
  }


  public SyncEvent getEvent() {
    return event;
  }


  public SyncOptions setEvent(SyncEvent event) {
    if(event != null)
      this.event = event;
    return this;
  }


  public Path getModfile() {
    return modfile;
  }


  public SyncOptions setModfile(Path modfile) {
    if(modfile != null)
      this.modfile = modfile;
    return this;
  }


  public SimpleDate getDate() {
    return date;
  }


  public SyncOptions setDate(SimpleDate date) {
    if(date != null)
      this.date = date;
    return this;
  }


  public int getRepeatInterval() {
    return repeatInterval;
  }
  
  
  public String getRepeatAsString() {
    if(unit == null) return null;
    return String.valueOf(repeatInterval)
        + unit.name();
  }


  public SyncOptions setRepeatInterval(int interval, TimeUnit time) {
    if(interval > 0 && time != null) {
      this.repeatInterval = interval;
      unit = time;
    }
    return this;
  }
  
  
  public int parseRepeatInterval(String s) {
    if(s != null && !s.trim().isEmpty()) {
      try {
        return Integer.parseInt(s.split(" ")[0]);
      } catch(NumberFormatException e) {
        return -1;
      }
    }
    return -1;
  }


  public TimeUnit parseTimeUnit(String s) {
    if(s != null && !s.trim().isEmpty()) {
      try {
        return TimeUnit.parseUnit(s.split(" ")[1]);
      } catch(NumberFormatException e) {
        return null;
      }
    }
    return null;
  }


  public boolean isUpdateTarget() {
    return updateTarget;
  }


  public SyncOptions setUpdateTarget(boolean updateTarget) {
    this.updateTarget = updateTarget;
    return this;
  }


  public boolean isAutoSync() {
    return autoSync;
  }


  public SyncOptions setAutoSync(boolean autoSync) {
    this.autoSync = autoSync;
    return this;
  }


  public boolean isShowGui() {
    return showGui;
  }


  public SyncOptions setShowGui(boolean showGui) {
    this.showGui = showGui;
    return this;
  }


  public boolean isPrintConsole() {
    return printConsole;
  }


  public SyncOptions setPrintConsole(boolean printConsole) {
    this.printConsole = printConsole;
    return this;
  }
  
}
