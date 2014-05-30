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

import com.jpower.sys.Disk;
import com.jpower.sys.LastSnapshot;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @version 0.0 - 23/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
@ManagedBean
@SessionScoped
public class DiskCharts {

  @ManagedProperty(value="#{lastSnapshotPoll}")
  private LastSnapshotPoll poll;
  
  private LimitedStack<List<Disk>> stack;
  
  private PieChartModel usageModel;
  
  private CartesianChartModel lineModel;
  
  private String device;
  
  
  public DiskCharts() {
    stack = new LimitedStack<>(12);
  }


  public LastSnapshotPoll getPoll() {
    return poll;
  }


  public void setPoll(LastSnapshotPoll poll) {
    this.poll = poll;
    if(poll != null) {
      this.update();
      device = this.getDisks().get(0).getDevice();
    }
  }
  
  
  public void update() {
    LastSnapshot last = poll.poll();
    if(last != null) {
      stack.push(last.getDisks());
    }
  }
  
  
  public List<Disk> getDisks() {
    return stack.peek();
  }
  
  
  public void setDevice(String dev) {
    device = dev;
  }
  
  
  public String getDevice() {
    return device;
  }
  
  
  public Disk getDisk(String dev) {
    List<Disk> disks = getDisks();
    if(disks == null || disks.isEmpty())
      return null;
    for(Disk d : disks)
      if(d.getDevice().equals(dev))
        return d;
    return null;
  }
  
  
  public Disk getDisk(List<Disk> disks, String dev) {
    if(disks == null || disks.isEmpty())
      return null;
    for(Disk d : disks)
      if(d.getDevice().equals(dev))
        return d;
    return null;
  }
  
  
  public Disk getSelectedDisk() {
    return getDisk(device);
  }
  
  
  public PieChartModel getUsageModel() {
    usageModel = new PieChartModel();
    
    if(stack.isEmpty()) return usageModel;
    
    Disk d = getDisk(device);
    if(d == null) return usageModel;
    
    double val = d.getUsedSpace() / 1000.0;
    usageModel.set("Used", val);
    val = d.getFreeSpace() / 1000.0;
    usageModel.set("Free", val);
    
    return usageModel;
  }
  
  
  public CartesianChartModel getLineModel() {
    lineModel = new CartesianChartModel();
    
    if(stack.isEmpty()) return lineModel;
    
    ChartSeries us = new ChartSeries("Used");
    ChartSeries fs = new ChartSeries("Free");
    
    Iterator<List<Disk>> it = stack.descIterator();
    int time = 20 * stack.size();
    while(it.hasNext()) {
      List<Disk> ds = it.next();
      Disk d = this.getDisk(ds, device);
      us.set(String.valueOf(time) + "s", d.getUsedSpace() / 1000);
      fs.set(String.valueOf(time) + "s", d.getFreeSpace() / 1000);
      time -= 20;
    }
    
    lineModel.addSeries(us);
    lineModel.addSeries(fs);
    return lineModel;
  }
  
}
