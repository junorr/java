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
import com.jpower.sys.LastSnapshot;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @version 0.0 - 29/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
@ManagedBean
@ApplicationScoped
public class IFNetCharts implements Serializable {
  
  private LastSnapshot last;
  
  private CartesianChartModel loadModel;
  
  private CartesianChartModel lineModel;
  
  private LimitedStack<List<IFNetwork>> stack;
  
  private LastSnapshotPoll poll;
  
  private String ifname;
  
  
  public IFNetCharts() {
    poll = new LastSnapshotPoll();
    stack = new LimitedStack<>(12);
    this.update();
    ifname = last.getInterfaces().get(0).getName();
  }
  
  
  public void update() {
    last = poll.poll();
    if(last != null)
      stack.push(last.getInterfaces());
  }


  public LastSnapshot getLast() {
    return last;
  }


  public void setLast(LastSnapshot last) {
    this.last = last;
  }


  public String getIfname() {
    return ifname;
  }


  public void setIfname(String ifname) {
    this.ifname = ifname;
  }
  
  
  public IFNetwork getIFNetwork() {
    if(ifname == null || last == null
        || last.getInterfaces() == null
        || last.getInterfaces().isEmpty()) 
      return null;
    
    return getIFNetwork(last.getInterfaces(), ifname);
  }
  
  
  public IFNetwork getIFNetwork(List<IFNetwork> is, String ifname) {
    if(ifname == null || is == null
        || is.isEmpty()) 
      return null;
    
    for(IFNetwork i : is) {
      if(i.getName().equals(ifname))
        return i;
    }
    return null;
  }
  
  
  public CartesianChartModel getLoadModel() {
    //this.update();
    loadModel = new CartesianChartModel();
    
    IFNetwork net = getIFNetwork();
    if(net == null) return loadModel;
    
    ChartSeries ins = new ChartSeries("Input");
    ChartSeries outs = new ChartSeries("Output");
    ChartSeries total = new ChartSeries("Total");
    
    ins.set(ifname, net.getLoad().getInputAverage());
    outs.set(ifname, net.getLoad().getOutputAverage());
    total.set(ifname, net.getLoad().getTotalAverage());
    
    loadModel.addSeries(ins);
    loadModel.addSeries(outs);
    loadModel.addSeries(total);
    return loadModel;
  }
  
  
  public CartesianChartModel getLineModel() {
    lineModel = new CartesianChartModel();
    
    if(stack.isEmpty()) return lineModel;
    
    ChartSeries ts = new ChartSeries("Total");
    ChartSeries ins = new ChartSeries("Input");
    
    Iterator<List<IFNetwork>> it = stack.descIterator();
    int time = 20 * stack.size();
    while(it.hasNext()) {
      List<IFNetwork> is = it.next();
      IFNetwork in = this.getIFNetwork(is, ifname);
      ts.set(String.valueOf(time) + "s", in.getLoad().getTotalAverage());
      ins.set(String.valueOf(time) + "s", in.getLoad().getInputAverage());
      time -= 20;
    }
    
    lineModel.addSeries(ins);
    lineModel.addSeries(ts);
    return lineModel;
  }
  
}
