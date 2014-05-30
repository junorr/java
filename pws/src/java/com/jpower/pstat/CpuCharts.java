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

import com.jpower.date.SimpleDate;
import com.jpower.sys.Cpu;
import com.jpower.sys.LastSnapshot;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Locale;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.MeterGaugeChartModel;

/**
 *
 * @version 0.0 - 23/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
@ManagedBean
@ApplicationScoped
public class CpuCharts {

  private LastSnapshotPoll poll;
  
  private LimitedStack<Cpu> stack;
  
  private MeterGaugeChartModel gaugeModel;
  
  private CartesianChartModel detailedModel;
  
  private CartesianChartModel lineModel;
  
  private SimpleDate start;
  
  private SimpleDate end;
  
  
  public CpuCharts() {
    poll = new LastSnapshotPoll();
    stack = new LimitedStack<>(12);
    this.update();
  }
  
  
  public void update() {
    LastSnapshot last = poll.poll();
    if(last != null)
      stack.push(last.getCpu());
  }
  
  
  public String getUsedCpu() {
    DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
    double used = 0.0;
    if(!stack.isEmpty())
      used = stack.peek().getUsedCpu();
    return df.format(used);
  }
  
  
  public Cpu getCpu() {
    return stack.peek();
  }
  
  
  public MeterGaugeChartModel getGaugeModel() {
    gaugeModel = new MeterGaugeChartModel();
    gaugeModel.addInterval(0);
    gaugeModel.addInterval(20);
    gaugeModel.addInterval(40);
    gaugeModel.addInterval(50);
    gaugeModel.addInterval(60);
    gaugeModel.addInterval(80);
    gaugeModel.addInterval(100);
    double value = 0;
    if(!stack.isEmpty())
      value = stack.peek().getUsedCpu();
    gaugeModel.setValue(value);
    return gaugeModel;
  }
  
  
  public CartesianChartModel getDetailedModel() {
    detailedModel = new CartesianChartModel();
    
    if(stack.isEmpty()) return detailedModel;
    Cpu cpu = stack.peek();
    
    ChartSeries series = new ChartSeries();
    series.setLabel("User");
    series.set("Usage %", cpu.getUser());
    detailedModel.addSeries(series);
    
    series = new ChartSeries();
    series.setLabel("Sys");
    series.set("Usage %", cpu.getSys());
    detailedModel.addSeries(series);
    
    series = new ChartSeries();
    series.setLabel("IO Wait");
    series.set("Usage %", cpu.getIowait());
    detailedModel.addSeries(series);
    
    series = new ChartSeries();
    series.setLabel("Hard Interrupt");
    series.set("Usage %", cpu.getHardInterruption());
    detailedModel.addSeries(series);
    
    series = new ChartSeries();
    series.setLabel("Soft Interrupt");
    series.set("Usage %", cpu.getSoftInterruption());
    detailedModel.addSeries(series);
    
    return detailedModel;
  }
  
  
  public CartesianChartModel getLineModel() {
    lineModel = new CartesianChartModel();
    
    if(stack.isEmpty()) return lineModel;
    
    int last = stack.size() * 20;
    ChartSeries total = new ChartSeries("Total");
    ChartSeries user = new ChartSeries("User");
    
    Iterator<Cpu> it = stack.descIterator();
    while(it.hasNext()) {
      Cpu cpu = it.next();
      total.set(String.valueOf(last) + "s", cpu.getUsedCpu());
      user.set(String.valueOf(last) + "s", cpu.getUser());
      last -= 20;
    }
    
    lineModel.addSeries(total);
    lineModel.addSeries(user);
    return lineModel;
  }
  
}
