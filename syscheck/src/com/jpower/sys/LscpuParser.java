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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/08/2012
 */
public class LscpuParser implements Serializable, Parser<Cpu> {
  
  private String content;
  
  private String[] lines;
  
  private Cpu cpu;
  
  private TextFieldParser tp;
  
  
  public LscpuParser() {
    content = null;
    lines = null;
    tp = new TextFieldParser();
    cpu = new Cpu();
  }
  
  
  @Override
  public Cpu parse(String s) {
    if(s == null || s.isEmpty())
      return null;
    
    content = s;
    lines = content.split("\n");
    cpu.setArchitecture(lines[0].split(":")[1].trim())
        .setMode(lines[1].split(":")[1].trim())
        .setCores(Integer.parseInt(lines[3].split(":")[1].trim()))
        .setThreadsPerCore(Integer.parseInt(lines[5].split(":")[1].trim()));
    
    int id = 0;
    int mhz = 0;
    int arch = 0;
    int mode = 0;
    int cores = 0;
    int thc = 0;
    for(int i = 0; i < lines.length; i++) {
      if(arch == 0 && (lines[i].contains("Arq") || lines[i].contains("Arch")))
        arch = i;
      else if(mode == 0 && lines[i].contains("op-mode"))
        mode = i;
      else if(cores == 0 && lines[i].contains("CPU"))
        cores = i;
      else if(thc == 0 && lines[i].contains("Thread"))
        thc = i;
      else if(id == 0 && lines[i].contains("ID"))
        id = i;
      else if(mhz == 0 && lines[i].contains("MHz"))
        mhz = i;
    }
    
    return cpu.setArchitecture(lines[arch].split(":")[1].trim())
        .setMode(lines[mode].split(":")[1].trim())
        .setCores(Integer.parseInt(lines[cores].split(":")[1].trim()))
        .setThreadsPerCore(Integer.parseInt(lines[thc].split(":")[1].trim()))
        .setId(lines[id].split(":")[1].trim())
        .setMhz(Double.parseDouble(lines[mhz].split(":")[1].trim()));
  }
  
  
  @Override
  public Cpu get() {
    return cpu;
  }
  
  
  public static void main(String[] args) {
    LscpuParser lp = new LscpuParser();
    String s = "Arquitetura:           i686\n"
        + "CPU op-mode(s):        32-bit, 64-bit\n"
        + "Byte Order:            Little Endian\n"
        + "CPU(s):                1\n"
        + "On-line CPU(s) list:   0\n"
        + "Thread(s) por núcleo: 1\n"
        + "Núcleo(s) por soquete:1\n"
        + "Soquete(s):            1\n"
        + "ID do fabricante:      GenuineIntel\n"
        + "CPU family:            6\n"
        + "Modelo:                42\n"
        + "Stepping:              7\n"
        + "CPU MHz:               3162.454\n"
        + "BogoMIPS:              6324.90\n"
        + "L1d cache:             32K\n"
        + "L1d cache:             32K\n"
        + "L2d cache:             6144K\n";
        
    System.out.println(lp.parse(s));
  }

}
