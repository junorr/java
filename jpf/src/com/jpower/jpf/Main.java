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

package com.jpower.jpf;

import com.jpower.spj.Error;
import com.jpower.spj.Option;
import com.jpower.spj.ShellParser;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 09/11/2012
 */
public class Main {

  private ShellParser shell;
  
  private String file;
  
  private PrintStream out;
  
  private String [] starts;
  
  private String[] ends;
  
  private String insert;
  
  private String location;
  
  
  public Main() {
    shell = new ShellParser();
    shell.addOption(new Option("-f")
        .setDescription("File to parse")
        .setLongName("--file")
        .setMandatory(true));
    shell.addOption(new Option("-s")
        .setDescription("Start parsing string")
        .setLongName("--start")
        .setMandatory(true));
    shell.addOption(new Option("-e")
        .setDescription("End parsing string")
        .setLongName("--end")
        .setMandatory(true));
    shell.addOption(new Option("-o")
        .setDescription("Output of the parse")
        .setDefaultvalue("stdout")
        .setMandatory(false)
        .setLongName("--output"));
    shell.addOption(new Option("-h")
        .setDescription("Show this help text")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false)
        .setLongName("--help"));
    shell.addOption(new Option("-i")
        .setLongName("--insert")
        .setAcceptArgs(true)
        .setMandatory(false)
        .setExclusive(false)
        .setDescription("Text to insert in the output"));
    shell.addOption(new Option("-l")
        .setAcceptArgs(true)
        .setDefaultvalue("start")
        .setDescription("[start | end] Location where the text will be inserted")
        .setExclusive(false)
        .setLongName("--location")
        .setMandatory(false));
    shell.setAppName("jpf")
        .setAuthor("Juno Roesler")
        .setContact("juno.rr@gmail.com")
        .setDescription("Java File Parser");
  }
  
  
  public void file(Option o) {
    if(o == null || o.arguments().isEmpty())
      return;
    
    file = o.getFirstArg();
    if(file == null || file.isEmpty()) {
      shell.errors().add(
          new Error(Error.LEVEL.ERROR, 
          "File do not exists: "+ file));
      help();
    }
  }
  
  
  public void start(Option o) {
    if(o == null || o.arguments().isEmpty())
      return;
    
    String start = "";
    for(String s : o.arguments()) {
      if(!start.isEmpty()) start += " ";
      start += s;
    }
    
    if(start.contains(",")) {
      starts = start.split(",");
    } else {
      starts = new String[] { start };
    }
  }
  
  
  public void insert(Option o) {
    if(o == null || o.arguments().isEmpty())
      return;
    
    insert = o.getFirstArg();
  }
  
  
  public void location(Option o) {
    if(o == null)
      return;
    
    if(o.arguments().isEmpty())
      location = o.getDefaultvalue().toString();
    else
      location = o.getFirstArg();
  }
  
  
  public void end(Option o) {
    if(o == null || o.arguments().isEmpty())
      return;
    
    String end = "";
    for(String s : o.arguments()) {
      if(!end.isEmpty()) end += " ";
      end += s;
    }
    
    if(end.contains(",")) {
      ends = end.split(",");
    } else {
      ends = new String[] { end };
    }
  }
  
  
  public void run() {
    for(int i = 0; i < starts.length; i++) {
      AsyncFileProcessor fp = new AsyncFileProcessor();
      fp.setInputFile(file);
      if(shell.isOptionPresent("-o")) {
        fp.setOutput(this.getPrinter(
            shell.getOption("-o").getFirstArg()+ i));
      } else {
        fp.setOutput(this.getPrinter(
            shell.getOption("-o").getDefaultvalue().toString()));
      }
      fp.setStartString(starts[i]);
      if(ends == null || ends.length <= i) {
        shell.errors().add(new Error(
            Error.LEVEL.ERROR, 
            "Number of start/end arguments does'nt match"));
        help();
      }
      if(insert != null) {
        fp.setTextInsert(insert)
            .setTextLocation(location);
      }
      fp.setEndString(ends[i]);
      fp.run();
    }
  }
  
  
  private PrintStream getPrinter(String s) {
    if(s == null || s.isEmpty())
      return null;
    if(s.equals("stdout"))
      return System.out;
    else {
      try {
        return new PrintStream(s);
      } catch(FileNotFoundException ex) {
        shell.errors().add(new Error(
            Error.LEVEL.ERROR, ex.getMessage()));
        help();
      }
    }
    return null;
  }
  
  
  public void help() {
    System.out.println(shell.createHeader(40));
    System.out.println(shell.createUsage());
    try {
      Thread.sleep(200);
    } catch(InterruptedException e) {}
    shell.printAllMessages(System.err);
    System.exit(1);
  }
  
  
  public static void main(String[] args) {
    /*args = new String[] {
      "-f",
      "C:\\Users\\juno\\Documents\\OPR_424.txt",
      "-o",
      "C:\\Users\\juno\\Documents\\FR_OUT.txt",
      "-s",
      "236800076",
      "-e",
      "273100363"
    };*/
    
    Main m = new Main();
    m.shell.parseArgs(args);
    if(!m.shell.parseErrors()
        || m.shell.isOptionPresent("-h"))
      m.help();
    
    m.file(m.shell.getOption("-f"));
    m.start(m.shell.getOption("-s"));
    m.end(m.shell.getOption("-e"));
    m.insert(m.shell.getOption("-i"));
    m.location(m.shell.getOption("-l"));
    m.run();
  }
  
}
