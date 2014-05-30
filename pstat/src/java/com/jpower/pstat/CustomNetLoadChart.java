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

import com.jpower.sys.IFNetwork;
import com.jpower.sys.IPTraffic;
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
public class CustomNetLoadChart extends CustomChart {

  private boolean inputAvrg, 
      outputAvrg, totalAvrg, 
      maxOutput, maxInput;
  
  @ManagedProperty(value="#{iFNetCharts}")
  private IFNetCharts nchart;
  
  
  public CustomNetLoadChart() {
    super();
    inputAvrg = false;
    outputAvrg = false;
    totalAvrg = true;
    maxOutput = false;
    maxInput = false;
  }


  public boolean isInputAvrg() {
    return inputAvrg;
  }


  public void setInputAvrg(boolean b) {
    this.inputAvrg = b;
  }


  public boolean isOutputAvrg() {
    return outputAvrg;
  }


  public void setOutputAvrg(boolean b) {
    this.outputAvrg = b;
  }


  public boolean isTotalAvrg() {
    return totalAvrg;
  }


  public void setTotalAvrg(boolean b) {
    this.totalAvrg = b;
  }


  public boolean isMaxOutput() {
    return maxOutput;
  }


  public void setMaxOutput(boolean b) {
    this.maxOutput = b;
  }


  public boolean isMaxInput() {
    return maxInput;
  }


  public void setMaxInput(boolean b) {
    this.maxInput = b;
  }
  
  
  public IFNetCharts getNchart() {
    return nchart;
  }
  
  
  public void setNchart(IFNetCharts nc) {
    nchart = nc;
  }
  
  
  public List<IPTraffic> getSnapshotTraffic() {
    if(snap == null) return null;
    return nchart.getIFNetwork(
        snap.getInterfaces(), nchart.getIfname())
        .getTraffic();
  }


  @Override
  public CartesianChartModel getModel() {
    model = new CartesianChartModel();
    poll.setStartDate(startDate);
    poll.setEndDate(stopDate);
    snaps = poll.getInterval();
    
    if(snaps == null || snaps.isEmpty())
      return model;
    
    double input = 0,
        output = 0,
        total = 0,
        maxOut = 0,
        maxIn = 0;
    
    String inputLabel = "Input Avrg";
    String outputLabel = "Output Avrg";
    String totalLabel = "Total Avrg";
    String maxOutLabel = "Max Output";
    String maxInLabel = "Max Input";
    
    MultiSeries ms = new MultiSeries();
    if(inputAvrg) ms.series.add(new ChartSeries(inputLabel));
    if(outputAvrg) ms.series.add(new ChartSeries(outputLabel));
    if(totalAvrg) ms.series.add(new ChartSeries(totalLabel));
    if(maxInput) ms.series.add(new ChartSeries(maxInLabel));
    if(maxOutput) ms.series.add(new ChartSeries(maxOutLabel));
    
    if(ms.series.isEmpty()) return model;
    
    List<Snapshot> ls = new LinkedList<>();
    ls.addAll(snaps);
    Collections.sort(ls, new SnapComparator());
    snaps = ls;
    double count = 0;
    
    for(Snapshot s : snaps) {
      List<IFNetwork> nets = s.getInterfaces();
      IFNetwork net = nchart.getIFNetwork(nets, nchart.getIfname());
      count++;
      double val = 0;
      
      if(inputAvrg) {
        val = net.getLoad().getInputAverage();
        ms.get(inputLabel)
            .set(formatTime(s.getTime()), val);
        input += val;
      }
      if(outputAvrg) {
        val = net.getLoad().getOutputAverage();
        ms.get(outputLabel)
            .set(formatTime(s.getTime()), val);
        output += val;
      }
      if(totalAvrg) {
        val = net.getLoad().getTotalAverage();
        ms.get(totalLabel)
            .set(formatTime(s.getTime()), val);
        total += val;
      }
      if(maxInput) {
        val = net.getLoad().getMaxInput();
        ms.get(maxInLabel)
            .set(formatTime(s.getTime()), val);
        maxIn += val;
      }
      if(maxOutput) {
        val = net.getLoad().getMaxOutput();
        ms.get(maxOutLabel)
            .set(formatTime(s.getTime()), val);
        maxOut += val;
      }
    }
    
    averages.clear();
    if(inputAvrg) averages.add(
        new KVPair(inputLabel,
        new Double(input / count)));
    
    if(outputAvrg) averages.add(
        new KVPair(outputLabel,
        new Double(output / count)));
    
    if(totalAvrg) averages.add(
        new KVPair(totalLabel,
        new Double(total / count)));
    
    if(maxInput) averages.add(
        new KVPair(maxInLabel,
        new Double(maxIn / count)));
    
    if(maxOutput) averages.add(
        new KVPair(maxOutLabel,
        new Double(maxOut / count)));
    
    for(ChartSeries c : ms.series)
      model.addSeries(c);
    
    return model;
  }
  
}
