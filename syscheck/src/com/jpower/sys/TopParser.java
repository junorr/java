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
 * @version 0.0 - 27/08/2012
 */
public class TopParser implements Serializable, Parser<Cpu> {
  
  private String content;
  
  private String[] lines;
  
  private List<SysProcess> prcs;
  
  private Cpu cpu;
  
  private TextFieldParser tp;
  
  
  public TopParser(Cpu c) {
    if(c == null) c = new Cpu();
    cpu = c;
    tp = new TextFieldParser();
    content = null;
    lines = null;
    prcs = new LinkedList<>();
  }
  
  
  @Override
  public Cpu parse(String s) {
    if(s == null || s.trim().isEmpty())
      return null;
    
    content = s;
    lines = content.split("\n");
    if(lines.length < 8)
      throw new IllegalStateException(
          "Invalid top content: "+ lines.length+ " lines.");
    
    
    this.parseCpu(lines[2]);
    
    prcs = new LinkedList<>();
    for(int i = 7; i < lines.length; i++) {
      SysProcess sp = this.parseProcess(lines[i]);
      prcs.add(sp);
    }
    cpu.setProcesses(prcs);
    return cpu;
  }
  
  
  @Override
  public Cpu get() {
    return cpu;
  }
  
  
  public List<SysProcess> getProcesses() {
    return prcs;
  }
  
  
  public void parseCpu(String line) {
    if(line == null || line.length() < 45)
      return;
    
    tp.setLine(line).parse(" ", "\n");
    tp.discard(1);
    
    double d = tp.doublePop();
    if(d < 0) d = tp.doublePop();
    cpu.setUser(d);
    
    d = tp.doublePop();
    if(d < 0) d = tp.doublePop();
    cpu.setSys(d);
    
    d = tp.doublePop();
    if(d < 0) d = tp.doublePop();
    cpu.setUser(cpu.getUser() + d);
    
    d = tp.doublePop();
    if(d < 0) d = tp.doublePop();
    cpu.setIddle(d);
    
    d = tp.doublePop();
    if(d < 0) d = tp.doublePop();
    cpu.setIowait(d);
    
    d = tp.doublePop();
    if(d < 0) d = tp.doublePop();
    cpu.setHardInterruption(d);
    
    d = tp.doublePop();
    if(d < 0) d = tp.doublePop();
    cpu.setSoftInterruption(d);
  }
  
  
  public SysProcess parseProcess(String line) {
    if(line == null || line.length() < 60)
      return null;
    
    tp.setLine(line);
    tp.parse(" ");
    
    SysProcess sp = new SysProcess();
    sp.setPid((int) tp.doublePop());
    sp.setUser(tp.pop());
    tp.discard(6);
    sp.setCpu(tp.doublePop());
    sp.setMem(tp.doublePop());
    sp.setUptime(tp.pop());
    sp.setName(tp.pop());
    
    return sp;
  }
  
  
  public static void main(String[] args) {
    TopParser tp = new TopParser(null);
    tp.parse("top - 13:13:11 up 22 min,  0 users,  load average: 0.61, 0.71, 0.69\n"
        + "Tasks: 133 total,   1 running, 131 sleeping,   0 stopped,   1 zombie\n"
        + "Cpu(s):  0.2%us,  0.7%sy,  0.0%ni, 94.5%id,  4.6%wa,  0.0%hi,  0.0%si,  0.0%st\n"
        + "Mem:    798904k total,   737996k used,    60908k free,    15520k buffers\n"
        + "Swap:   816124k total,     4200k used,   811924k free,   280008k cached\n\n"
        + "  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                                \n"
        + " 1040 root      20   0 93872  40m  12m S  0.7  5.1   0:22.18 Xorg                                                   \n"
        + " 1594 juno      20   0 80900  23m  13m S  0.4  3.0   0:02.79 python                                                 \n"
        + " 1467 root      20   0     0    0    0 S  0.2  0.0   0:00.07 flush-8:0                                              \n"
        + " 1566 juno      20   0  7452 2364 1876 S  0.2  0.3   0:00.24 xscreensaver                                           \n"
        + " 2300 juno      20   0  712m 252m  12m S  0.2 32.3   2:03.36 java                                                   \n"
        + " 2358 juno      20   0  2836 1152  880 R  0.2  0.1   0:01.93 top                                                    \n"
        + "    1 root      20   0  3516 1768 1312 S  0.0  0.2   0:00.59 init                                                   \n"
        + "    2 root      20   0     0    0    0 S  0.0  0.0   0:00.00 kthreadd                                               ");
    System.out.println(tp.get());
    System.out.println(tp.getProcesses().get(0));
    
    tp.parse("top - 10:14:57 up 3 min,  1 user,  load average: 0,18, 0,25, 0,12\n"
        + "Tarefas: 218 total,   1 running, 216 sleeping,   0 stopped,   1 zombie\n"
        + "%Cpu(s):  0,9 us,  0,5 sy,  0,1 ni, 94,8 id,  3,7 wa,  0,0 hi,  0,0 si,  0,0 st\n"
        + "KiB Mem:   6021520 total,  1222280 used,  4799240 free,    60380 buffers\n"
        + "KiB Swap:   498684 total,        0 used,   498684 free,   577548 cached\n"
        + "\n"
        + "  PID USER      PR  NI  VIRT  RES  SHR S  %CPU %MEM    TIME+  COMMAND\n"
        + " 1372 root      20   0  121m  19m 5580 S   6,3  0,3   0:02.55 Xorg\n"
        + "    1 root      20   0 24596 2644 1396 S   0,0  0,0   0:00.92 init\n"
        + "    2 root      20   0     0    0    0 S   0,0  0,0   0:00.00 kthreadd\n"
        + "    3 root      20   0     0    0    0 S   0,0  0,0   0:00.02 ksoftirqd/0\n"
        + "    4 root      20   0     0    0    0 S   0,0  0,0   0:00.00 kworker/0:0\n"
        + "    5 root      20   0     0    0    0 S   0,0  0,0   0:00.41 kworker/u:0\n"
        + "    6 root      rt   0     0    0    0 S   0,0  0,0   0:00.06 migration/0\n"
        + "    7 root      rt   0     0    0    0 S   0,0  0,0   0:00.00 watchdog/0\n"
        + "    8 root      rt   0     0    0    0 S   0,0  0,0   0:00.00 migration/1\n"
        + "    9 root      20   0     0    0    0 S   0,0  0,0   0:00.00 kworker/1:0");
    System.out.println(tp.get());
    System.out.println(tp.getProcesses().get(1));
  }

}
