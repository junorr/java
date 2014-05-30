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

import com.jpower.sys.ClearMemory;
import com.jpower.sys.Memory;
import com.jpower.sys.MemoryTaker;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;

/**
 *
 * @version 0.0 - 23/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
@ManagedBean
@SessionScoped
public class MemoryCharts {
  
  public static final String FILE_CLEAR_MEM = "/clearMemoryCache.conf";
  
  private MemoryTaker mtk;
  
  private ClearMemory cmem;
  
  private LimitedStack<Memory> stack;
  
  private DonutChartModel totalModel;
  
  private CartesianChartModel detailedModel;
  
  private CartesianChartModel lineModel;
  
  @ManagedProperty(value="#{webConfig}")
  private WebConfig webc;
  
  
  public MemoryCharts() {
    stack = new LimitedStack<>(12);
    mtk = new MemoryTaker();
    this.update();
  }


  public WebConfig getWebc() {
    return webc;
  }


  public void setWebc(WebConfig webc) {
    this.webc = webc;
    if(webc != null) {
      File f = new File(webc.getSyscheckDir()+ FILE_CLEAR_MEM);
      cmem = new ClearMemory(f);
    }
  }
  
  
  public void update() {
    stack.push(mtk.take());
  }
  
  
  public Memory getMemory() {
    return stack.peek();
  }
  
  
  public void clearMemory() {
    System.out.println("** clearMemory!");
    cmem.clear();
    this.update();
  }
  
  
  public DonutChartModel getTotalModel() {
    totalModel = new DonutChartModel();
    
    if(stack.isEmpty()) return totalModel;
    
    Map<String, Number> vals = new LinkedHashMap<>();
    vals.put("Used", stack.peek().getUsed());
    vals.put("Free", stack.peek().getFree());
    
    totalModel.addCircle(vals);
    return totalModel;
  }
  
  
  public CartesianChartModel getDetailedModel() {
    detailedModel = new CartesianChartModel();
    
    if(stack.isEmpty()) return detailedModel;
    
    ChartSeries series = new ChartSeries();
    series.setLabel("Used");
    series.set("Mb", stack.peek().getUsed());
    detailedModel.addSeries(series);
    
    series = new ChartSeries();
    series.setLabel("Free");
    series.set("Mb", stack.peek().getFree());
    detailedModel.addSeries(series);
    
    series = new ChartSeries();
    series.setLabel("Cached");
    series.set("Mb", stack.peek().getCached());
    detailedModel.addSeries(series);
    
    series = new ChartSeries();
    series.setLabel("Total");
    series.set("Mb", stack.peek().getTotal());
    detailedModel.addSeries(series);
    
    return detailedModel;
  }
  
  
  public CartesianChartModel getLineModel() {
    lineModel = new CartesianChartModel();
    
    if(stack.isEmpty()) return lineModel;
    
    int last = stack.size() * 20;
    ChartSeries used = new ChartSeries("Used");
    ChartSeries cached = new ChartSeries("Cached");
    
    Iterator<Memory> it = stack.descIterator();
    while(it.hasNext()) {
      Memory mem = it.next();
      used.set(String.valueOf(last) + "s", mem.getUsed());
      cached.set(String.valueOf(last) + "s", mem.getCached());
      last -= 20;
    }
    
    lineModel.addSeries(used);
    lineModel.addSeries(cached);
    return lineModel;
  }
  
}
