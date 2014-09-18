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

import com.jpower.sys.Memory;
import com.jpower.sys.Snapshot;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
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
public class CustomMemoryChart extends CustomChart {

  private boolean usedMem, 
      freeMem, cachedMem, 
      totalMem;
  
  
  public CustomMemoryChart() {
    super();
    usedMem = true;
    freeMem = false;
    cachedMem = false;
    totalMem = false;
  }


  public boolean isUsedMem() {
    return usedMem;
  }


  public void setUsedMem(boolean b) {
    this.usedMem = b;
  }


  public boolean isFreeMem() {
    return freeMem;
  }


  public void setFreeMem(boolean b) {
    this.freeMem = b;
  }


  public boolean isCachedMem() {
    return cachedMem;
  }


  public void setCachedMem(boolean b) {
    this.cachedMem = b;
  }


  public boolean isTotalMem() {
    return totalMem;
  }


  public void setTotalMem(boolean b) {
    this.totalMem = b;
  }


  @Override
  public CartesianChartModel getModel() {
    model = new CartesianChartModel();
    poll.setStartDate(startDate);
    poll.setEndDate(stopDate);
    snaps = poll.getInterval();
    
    if(snaps == null || snaps.isEmpty())
      return model;
    
    int used = 0,
        free = 0,
        cached = 0,
        total = 0;
    
    String usedLabel = "Used";
    String freeLabel = "Free";
    String cachedLabel = "Cached";
    String totalLabel = "Total";
    
    MultiSeries ms = new MultiSeries();
    if(usedMem) ms.series.add(new ChartSeries(usedLabel));
    if(freeMem) ms.series.add(new ChartSeries(freeLabel));
    if(cachedMem) ms.series.add(new ChartSeries(cachedLabel));
    if(totalMem) ms.series.add(new ChartSeries(totalLabel));
    
    if(ms.series.isEmpty()) return model;
    
    List<Snapshot> ls = new LinkedList<>();
    ls.addAll(snaps);
    Collections.sort(ls, new SnapComparator());
    snaps = ls;
    int count = 0;
    
    for(Snapshot s : snaps) {
      Memory mem = s.getMem();
      count++;
      int val = 0;
      
      if(usedMem) {
        val = mem.getUsed();
        ms.get(usedLabel)
            .set(formatTime(s.getTime()), val);
        used += val;
      }
      if(freeMem) {
        val = mem.getFree();
        ms.get(freeLabel)
            .set(formatTime(s.getTime()), val);
        free += val;
      }
      if(cachedMem) {
        val = mem.getCached();
        ms.get(cachedLabel)
            .set(formatTime(s.getTime()), val);
        cached += val;
      }
      if(totalMem) {
        val = mem.getTotal();
        ms.get(totalLabel)
            .set(formatTime(s.getTime()), val);
        total += val;
      }
    }
    
    averages.clear();
    if(usedMem) averages.add(
        new KVPair(usedLabel,
        new Integer(used / count)));
    
    if(freeMem) averages.add(
        new KVPair(freeLabel,
        new Integer(free / count)));
    
    if(cachedMem) averages.add(
        new KVPair(cachedLabel,
        new Integer(cached / count)));
    
    if(totalMem) averages.add(
        new KVPair(totalLabel,
        new Integer(total / count)));
    
    for(ChartSeries c : ms.series)
      model.addSeries(c);
    
    return model;
  }
  
}
