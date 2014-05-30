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
import com.jpower.sys.Snapshot;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @version 0.0 - 24/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
@ManagedBean
@SessionScoped
public class CustomDiskChart extends CustomChart {

  private boolean usedDisk, freeDisk;
  
  @ManagedProperty(value="#{diskCharts}")
  private DiskCharts dschart;
  
  
  public CustomDiskChart() {
    super();
    usedDisk = false;
    freeDisk = false;
  }


  public boolean isUsedDisk() {
    return usedDisk;
  }


  public void setUsedDisk(boolean b) {
    this.usedDisk = b;
  }


  public boolean isFreeDisk() {
    return freeDisk;
  }


  public void setFreeDisk(boolean b) {
    this.freeDisk = b;
  }


  public DiskCharts getDschart() {
    return dschart;
  }


  public void setDschart(DiskCharts dschart) {
    this.dschart = dschart;
  }


  @Override
  public CartesianChartModel getModel() {
    model = new CartesianChartModel();
    poll.setStartDate(startDate);
    poll.setEndDate(stopDate);
    snaps = poll.getInterval();
    
    if(snaps == null || snaps.isEmpty() 
        || dschart == null)
      return model;
    
    String usedLabel = "Used";
    String freeLabel = "Free";
    
    long usedAvr = 0;
    long freeAvr = 0;
    
    MultiSeries ms = new MultiSeries();
    if(usedDisk) ms.series.add(new ChartSeries(usedLabel));
    if(freeDisk) ms.series.add(new ChartSeries(freeLabel));
    
    if(ms.series.isEmpty()) return model;
    
    List<Snapshot> ls = new LinkedList<>();
    ls.addAll(snaps);
    Collections.sort(ls, new SnapComparator());
    snaps = ls;
    
    int count = 0;
    
    for(Snapshot s : snaps) {
      List<Disk> disks = s.getDisks();
      Disk d = dschart.getDisk(disks, dschart.getDevice());
      count++;
      
      if(usedDisk) {
        long val = d.getUsedSpace() / 1000;
        ms.get(usedLabel)
            .set(formatTime(s.getTime()), val);
        usedAvr += val;
      }
      
      if(freeDisk) {
        long val = d.getFreeSpace() / 1000;
        ms.get(freeLabel)
            .set(formatTime(s.getTime()), val);
        freeAvr += val;
      }
    }
    
    averages.clear();
    if(usedDisk)
      averages.add(
          new KVPair(usedLabel, 
          new Long(usedAvr / count)));
    
    if(freeDisk)
      averages.add(
          new KVPair(freeLabel, 
          new Long(freeAvr / count)));
    
    for(ChartSeries c : ms.series)
      model.addSeries(c);
    
    return model;
  }
  
}
