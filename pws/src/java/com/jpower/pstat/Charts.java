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

package com.jpower.pstat;

import com.jpower.sys.Cpu;
import com.jpower.sys.Disk;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 25/09/2012
 */
@ManagedBean
@SessionScoped
public class Charts implements Serializable {
  
  private DonutChartModel memoryPerc;
  
  private CartesianChartModel memoryChart;
  
  private MeterGaugeChartModel cpuChart;
  
  private CartesianChartModel cpuDetail;
  
  private PieChartModel diskChart;
  
  private DiskCharts diskcharts;
  
  private String selected;
  
  
  public Charts() {
    diskcharts = new DiskCharts();
  }
  
  
  public String getSelected() {
    if(selected == null)
      selected = getDisks().get(0).getDevice();
    return selected;
  }
  
  
  public void setSelected(String disk) {
    selected = disk;
  }
  
  
  public List<Disk> getDisks() {
    if(diskcharts.getDisks() == null || diskcharts.getDisks().isEmpty())
      diskcharts.poll();
    return diskcharts.getDisks();
  }
  
  
  public Disk getSelectedDisk() {
    return getDisk(selected);
  }
  
  
  public Disk getDisk(String dev) {
    for(Disk d : getDisks()) {
      if(d.getDevice().equals(dev))
        return d;
    }
    return null;
  }
  
  
  public void createDiskChart() {
    diskChart = new PieChartModel();
    Disk d = getDisk(selected);
    if(d == null) return;
    double val = d.getUsedSpace() / 1000.0;
    diskChart.set("Used", val);
    val = d.getFreeSpace() / 1000.0;
    diskChart.set("Free", val);
  }
  
  
  public PieChartModel getDiskChart() {
    this.createDiskChart();
    return diskChart;
  }
  
}
