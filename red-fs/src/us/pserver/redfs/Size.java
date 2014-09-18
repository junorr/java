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

package us.pserver.redfs;

import java.text.DecimalFormat;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 19/11/2013
 */
public class Size {
  
  public static final double UNIT = 1024;

  private long size;
  
  
  public Size() {
    size = 0;
  }
  
  
  public Size(long size) {
    this.size = size;
  }


  public long size() {
    return size;
  }


  public void size(long size) {
    this.size = size;
  }
  
  
  public long getKB() {
    return Math.round(size / UNIT);
  }
  
  
  public long getMB() {
    return Math.round(size / Math.pow(UNIT, 2));
  }
  
  
  public long getGB() {
    return Math.round(size / Math.pow(UNIT, 3));
  }
  
  
  public long getTB() {
    return Math.round(size / Math.pow(UNIT, 4));
  }
  
  
  public double getKBd() {
    return size / UNIT;
  }
  
  
  public double getMBd() {
    return size / Math.pow(UNIT, 2);
  }
  
  
  public double getGBd() {
    return size / Math.pow(UNIT, 3);
  }
  
  
  public double getTBd() {
    return size / Math.pow(UNIT, 4);
  }
  
  
  public int getReadableSize() {
    if(size <= UNIT)
      return (int) size;
    else if(getKB() <= UNIT)
      return (int) getKB();
    else if(getMB() <= UNIT)
      return (int) getMB();
    else if(getGB() <= UNIT)
      return (int) getGB();
    else
      return (int) getTB();
  }
  
  
  public String getReadableUnit() {
    if(size <= UNIT)
      return "bytes";
    else if(getKB() <= UNIT)
      return "KB";
    else if(getMB() <= UNIT)
      return "MB";
    else if(getGB() <= UNIT)
      return "GB";
    else
      return "TB";
  }
  
  
  public double getReadableDouble() {
    if(size <= UNIT)
      return (double) size;
    else if(getKBd() <= UNIT)
      return getKBd();
    else if(getMBd() <= UNIT)
      return getMBd();
    else if(getGBd() <= UNIT)
      return getGBd();
    else
      return getTBd();
  }
  
  
  public String toString() {
    DecimalFormat df = new DecimalFormat("#,##0.00");
    return df.format(getReadableDouble())
        + " " + getReadableUnit();
  }
  
  
  public static void main(String[] args) {
    Size ds = new Size(Math.round(5.5 * Math.pow(UNIT, 2)));
    System.out.println("* size = "+ ds);
  }
  
}
