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

package com.jpower.sys;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 06/09/2012
 */
public class DiskParser implements Serializable, Parser<List<Disk>> {
  
  private String[] lines;
  
  private List<Disk> disks;
  
  private TextFieldParser tp;
  
  
  public DiskParser() {
    lines = null;
    disks = null;
    tp = new TextFieldParser();
  }
  
  
  @Override
  public List<Disk> parse(String s) {
    if(s == null || s.isEmpty())
      throw new IllegalArgumentException("Invalid content to parse: "+ s);
    
    disks = new LinkedList();
    lines = s.split("\n");
    
    for(int i = 0; i < lines.length; i++) {
      Disk d = parseDisk(lines[i]);
      if(d != null)
        disks.add(d);
    }
    return disks;
  }
  
  
  @Override
  public List<Disk> get() {
    return disks;
  }
  
  
  public Disk parseDisk(String line) {
    if(line.contains("Sist") || line.contains("File")) {
      return null;
    }
    if(line == null || line.isEmpty()
        || (!line.contains("dev")
        && !line.contains("media")))
      return null;
    
    tp.setLine(line).parse(" ");
    Disk d = new Disk();
    d.setDevice(tp.pop());
    d.setFileSystem(tp.pop());
    tp.discard(1);
    d.setUsedSpace((long) tp.doublePop());
    d.setFreeSpace((long) tp.doublePop());
    tp.discard(1);
    d.setMountPoint(tp.pop());
    
    return d;
  }
  
  
  public static void main(String[] args) {
    DiskParser dp = new DiskParser();
    String df = 
  "Sist. Arq.     Tipo     1K-blocos     Usad   Dispon. Uso% Montado em\n"
+ "/dev/sda1      ext4       7449528  2960460   4110652  42% /\n"
+ "udev           devtmpfs    442948        4    442944   1% /dev\n"
+ "tmpfs          tmpfs       180276      744    179532   1% /run\n"
+ "none           tmpfs         5120        0      5120   0% /run/lock\n"
+ "none           tmpfs       450684        0    450684   0% /run/shm\n"
+ "none           tmpfs       102400       12    102388   1% /run/user\n"
+ "juno           vboxsf   234374996 72518568 161856428  31% /media/host.juno\n"
+ "cruzer         vboxsf    15625200  8099440   7525760  52% /media/cruzer\n"
+ "storage        vboxsf   234375164 53882180 180492984  23% /media/storage\n";
        
    dp.parse(df);
    for(Disk d : dp.disks) {
      System.out.println(d);
    }
    System.out.println("----------------------------");
    
    df = 
  "Filesystem     Type     1K-blocks    Used Available Use% Mounted on\n"
+ "/dev/xvda1     ext4       8256952 1658064   6179460  22% /\n"
+ "udev           devtmpfs    838328      12    838316   1% /dev\n"
+ "tmpfs          tmpfs       338520     336    338184   1% /run\n"
+ "none           tmpfs         5120       0      5120   0% /run/lock\n"
+ "none           tmpfs       846300       0    846300   0% /run/shm\n"
+ "/dev/xvdb      ext3     153899044 1693432 144387988   2% /mnt\n";
        
    dp.parse(df);
    for(Disk d : dp.disks) {
      System.out.println(d);
    }
  }
  
}
