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

package com.jpower.dsync;

import java.text.DecimalFormat;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 22/04/2013
 */
public class ProgressBar implements ProgressListener {

  private char barChar;
  
  private int barSize;
  
  private boolean showPercent;
  
  private double percent;
  
  private long total;
  
  private long progress;
  
  private String lastPercent;
  
  private DecimalFormat df;
  
  
  public ProgressBar() {
    percent = 0;
    progress = 0;
    total = 0;
    showPercent = true;
    barSize = 12;
    barChar = '=';
    df = new DecimalFormat("0.0 %");
  }
  
  
  public ProgressBar setPercent(double d) {
    if(d < 0) d = 0;
    else if(d > 1) d = 1;
    lastPercent = df.format(percent);
    percent = d;
    return this;
  }
  
  
  public String getPercent() {
    return df.format(percent);
  }


  public char getBarChar() {
    return barChar;
  }


  public ProgressBar setBarChar(char barChar) {
    this.barChar = barChar;
    return this;
  }


  public int getBarSize() {
    return barSize;
  }


  public ProgressBar setBarSize(int barSize) {
    this.barSize = barSize;
    return this;
  }


  public boolean isShowPercent() {
    return showPercent;
  }


  public ProgressBar setShowPercent(boolean showPercent) {
    this.showPercent = showPercent;
    return this;
  }
  
  
  public String getBar() {
    StringBuilder bar = new StringBuilder();
    bar.append("[");
    int size = (int) Math.round(barSize * percent);
    for(int i = 0; i < size; i++) {
      bar.append(barChar);
    }
    if(size > 0) bar.append(">");
    else size -= 1;
    for(int i = 0; i < (barSize - size); i++) {
      bar.append(" ");
    }
    bar.append("] ");
    if(showPercent) bar.append(getPercent());
    return bar.toString();
  }
  

  @Override
  public void setTotal(long total) {
    this.total = total;
  }
  
  
  public long getTotal() { return total; }
  
  public long getProgress() { return progress; }


  @Override
  public void setProgress(long progress) {
    this.progress = progress;
    if(total <= 0) return;
    this.setPercent((progress / (double) total));
    if(!lastPercent.equals(getPercent()))
      System.out.print(getBar()+ "\r");
  }
  
  
  @Override
  public void onError(Exception ex) {}
  
  @Override
  public void onComplete() {}
  
  
  public static void main(String[] args) throws InterruptedException {
    ProgressBar p = new ProgressBar();
    p.setTotal(100);
    for(int i = 0; i <= 100; i += 5) {
      p.setProgress(i);
      Thread.sleep(200);
    }
  }

}
