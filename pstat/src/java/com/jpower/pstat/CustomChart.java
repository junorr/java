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


package com.jpower.pstat;

import us.pserver.date.SimpleDate;
import com.jpower.sys.Snapshot;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedProperty;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @version 0.0 - 24/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public abstract class CustomChart implements Serializable {

  @ManagedProperty(value="#{snapshotPoll}")
  protected SnapshotPoll poll;
  
  protected CartesianChartModel model;
  
  protected SimpleDate startDate;
  
  protected SimpleDate stopDate;
  
  protected boolean filled;
  
  protected static String[] subtitlePositions = 
      { "n", "e", "s", "w", "nw", "ne", "se", "sw" }; 
  
  protected String position;
  
  protected List<Snapshot> snaps;
  
  protected Snapshot snap;
  
  protected List<KVPair> averages;
  
  
  protected class MultiSeries {
    public List<ChartSeries> series;
    public MultiSeries() {
      series = new LinkedList<>();
    }
    public ChartSeries get(String label) {
      for(ChartSeries c : series) {
        if(c.getLabel().equals(label))
          return c;
      }
      return null;
    }
  }
  
  
  protected class SnapComparator implements Comparator<Snapshot> {
    @Override
    public int compare(Snapshot o1, Snapshot o2) {
      if(o1 == null && o2 == null) return 0;
      if(o1 == null || o2 == null) return -1;
      return o1.getTime().compareTo(o2.getTime());
    }
  }
  
  
  public CustomChart() {
    averages = new LinkedList<>();
    filled = false;
    position = "n";
    snaps = null;
    snap = null;
  }


  public SnapshotPoll getPoll() {
    return poll;
  }


  public void setPoll(SnapshotPoll poll) {
    this.poll = poll;
    if(poll != null) {
      startDate = poll.getOldest();
      stopDate = poll.getNewest();
    }
  }


  public Date getStartDate() {
    return startDate;
  }


  public void setStartDate(Date startDate) {
    this.startDate = SimpleDate.from(startDate);
  }


  public Date getStopDate() {
    return stopDate;
  }


  public void setStopDate(Date stopDate) {
    this.stopDate = SimpleDate.from(stopDate);
  }


  public boolean isFilled() {
    return filled;
  }


  public void setFilled(boolean filled) {
    this.filled = filled;
  }


  public String[] getSubtitlePositions() {
    return subtitlePositions;
  }


  public String getPosition() {
    return position;
  }


  public void setPosition(String position) {
    this.position = position;
  }
  
  
  public String getIntervalString() {
    return startDate.format(SimpleDate.DDMMYYYY_SLASH)
        + " - " + stopDate.format(SimpleDate.DDMMYYYY_SLASH);
  }
  
  
  public List<Snapshot> getSnaps() {
    return snaps;
  }
  
  
  public String getSnapDate() {
    return null;
  }
  
  
  public void setSnapDate(String s) {
    if(snaps == null || snaps.isEmpty())
      return;
    for(Snapshot sn : snaps)
      if(sn.getTime().toString().equals(s))
        snap = sn;
  }
  
  
  public Snapshot getSnap() {
    return snap;
  }


  public List<KVPair> getAverages() {
    return averages;
  }
  
  
  public abstract CartesianChartModel getModel();
  
  
  public String formatTime(SimpleDate d) {
    if(d == null) return "null";
    return d.hour() + ":" + d.minute();
  }
  
}
