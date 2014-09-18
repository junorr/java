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

import com.jpower.sys.Cpu;
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
public class CustomCpuChart extends CustomChart {

  private boolean totalCpu, 
      userCpu, sysCpu, 
      iowCpu, siCpu, hiCpu;
  
  
  public CustomCpuChart() {
    super();
    totalCpu = true;
    userCpu = false;
    sysCpu = false;
    iowCpu = false;
    siCpu = false;
    hiCpu = false;
  }


  public boolean isTotalCpu() {
    return totalCpu;
  }


  public void setTotalCpu(boolean totalCpu) {
    this.totalCpu = totalCpu;
  }


  public boolean isUserCpu() {
    return userCpu;
  }


  public void setUserCpu(boolean userCpu) {
    this.userCpu = userCpu;
  }


  public boolean isSysCpu() {
    return sysCpu;
  }


  public void setSysCpu(boolean sysCpu) {
    this.sysCpu = sysCpu;
  }


  public boolean isIowCpu() {
    return iowCpu;
  }


  public void setIowCpu(boolean iowCpu) {
    this.iowCpu = iowCpu;
  }


  public boolean isSiCpu() {
    return siCpu;
  }


  public void setSiCpu(boolean siCpu) {
    this.siCpu = siCpu;
  }


  public boolean isHiCpu() {
    return hiCpu;
  }


  public void setHiCpu(boolean hiCpu) {
    this.hiCpu = hiCpu;
  }
  
  
  @Override
  public CartesianChartModel getModel() {
    model = new CartesianChartModel();
    poll.setStartDate(startDate);
    poll.setEndDate(stopDate);
    snaps = poll.getInterval();
    
    if(snaps == null || snaps.isEmpty())
      return model;
    
    double total = 0;
    double user = 0;
    double sys = 0;
    double iow = 0;
    double si = 0;
    double hi = 0;
    
    String totalLabel = "Total";
    String userLabel = "User";
    String sysLabel = "System";
    String iowLabel = "IO Wait";
    String siLabel = "Soft Interrupt";
    String hiLabel = "Hard Interrupt";
    
    MultiSeries ms = new MultiSeries();
    if(totalCpu) ms.series.add(new ChartSeries(totalLabel));
    if(userCpu) ms.series.add(new ChartSeries(userLabel));
    if(sysCpu) ms.series.add(new ChartSeries(sysLabel));
    if(iowCpu) ms.series.add(new ChartSeries(iowLabel));
    if(siCpu) ms.series.add(new ChartSeries(siLabel));
    if(hiCpu) ms.series.add(new ChartSeries(hiLabel));
    
    if(ms.series.isEmpty()) return model;
    
    List<Snapshot> ls = new LinkedList<>();
    ls.addAll(snaps);
    Collections.sort(ls, new SnapComparator());
    snaps = ls;
    double count = 0;
    
    for(Snapshot s : snaps) {
      Cpu c = s.getCpu();
      double val = 0;
      count++;
      
      if(totalCpu) {
        val = c.getUsedCpu();
        ms.get(totalLabel)
            .set(formatTime(s.getTime()), val);
        total += val;
      }
      if(userCpu) {
        val = c.getUser();
        ms.get(userLabel)
            .set(formatTime(s.getTime()), val);
        user += val;
      }
      if(sysCpu) {
        val = c.getSys();
        ms.get(sysLabel)
            .set(formatTime(s.getTime()), val);
        sys += val;
      }
      if(iowCpu) {
        val = c.getIowait();
        ms.get(iowLabel)
            .set(formatTime(s.getTime()), val);
        iow += val;
      }
      if(siCpu) {
        val = c.getSoftInterruption();
        ms.get(siLabel)
            .set(formatTime(s.getTime()), val);
        si += val;
      }
      if(hiCpu) {
        val = c.getHardInterruption();
        ms.get(hiLabel)
            .set(formatTime(s.getTime()), val);
        hi += val;
      }
    }
    
    averages.clear();
    if(totalCpu) averages.add(
        new KVPair(totalLabel, 
        new Double(total / count)));
    
    if(userCpu) averages.add(
        new KVPair(userLabel, 
        new Double(user / count)));
    
    if(sysCpu) averages.add(
        new KVPair(sysLabel, 
        new Double(sys / count)));
    
    if(iowCpu) averages.add(
        new KVPair(iowLabel, 
        new Double(iow / count)));
    
    if(siCpu) averages.add(
        new KVPair(siLabel, 
        new Double(si / count)));
    
    if(hiCpu) averages.add(
        new KVPair(hiLabel, 
        new Double(hi / count)));
    
    for(ChartSeries c : ms.series)
      model.addSeries(c);
    
    return model;
  }
  
}
