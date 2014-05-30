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

package com.jpower.tn3270.script;

import com.jpower.spj.Option;
import com.jpower.spj.ShellParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 08/08/2013
 */
public class Main {
  
  static ShellParser sp;
  

  static void printHeader() {
    System.out.println();
    System.out.println(sp.createHeader(35));
    System.out.flush();
  }
  
  
  static void printUsage() {
    System.out.println(sp.createUsage());
    System.out.flush();
  }
  
  
  static void printMessages(String s) {
    if(s != null)
      System.out.println("\n# "+ s);
    else
      sp.printAllMessages(System.out);
    System.out.flush();
  }
  
  
  public static void main(String[] args) {
    sp = new ShellParser();
    Option def = Option.EMPTY_OPTION
        .setDescription("Script file to process")
        .setMandatory(true);
    
    Option ver = new Option("-v")
        .setLongName("--verbose")
        .setMandatory(false)
        .setDescription("Prints to stdout each execution command")
        .setAcceptArgs(false);

    Option chk = new Option("-c")
        .setLongName("--check")
        .setAcceptArgs(false)
        .setMandatory(false)
        .setDescription("Check for sintax errors on the script file");
    
    Option out = new Option("-o")
        .setLongName("--output")
        .setAcceptArgs(true)
        .setMandatory(false)
        .setExclusive(false)
        .setDescription("File to redirecte stdout");
    
    Option hlp = new Option("-h")
        .setLongName("--help")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false)
        .setDescription("Show this help text");
    
    sp.addOption(def)
        .addOption(ver)
        .addOption(chk)
        .addOption(hlp)
        .addOption(out)
        .setAppName("TN3270S")
        .setDescription("Script Executor\n")
        .setAuthor("Juno Roesler")
        .setContact("juno.rr@gmail.com")
        .setYear("2013")
        .setLicense("GNU LGPL v2.1");
    
    printHeader();
    
    sp.parseArgs(args);
    
    if(sp.getOption("-h").isPresent()) {
      printUsage();
      System.exit(0);
    }
    if(!sp.parseErrors()) {
      printUsage();
      printMessages(null);
      System.exit(2);
    }
    
    ScriptProcessor spr = new ScriptProcessor()
        .setVerbose(sp.getOption("-v").isPresent())
        .setSimulate(sp.getOption("-c").isPresent());
    
    if(sp.getOption("-o").isPresent()
        && sp.getOption("-o").getArg(0) != null) {
      try {
        PrintStream output = new PrintStream(
            sp.getOption("-o").getArg(0));
        spr.setStdout(output);
      } catch(FileNotFoundException e) {}
    }
    
    File f = new File(sp.getOption(Option.EMPTY).getArg(0));
    if(!f.exists()) {
      printUsage();
      System.err.println("# Script file dont exists: "+ f.getAbsolutePath());
      System.exit(1);
    }
    
    spr.process(spr.readScript(f));
    spr.getSession().close();
    spr.getStdout().flush();
    spr.getStdout().close();
  }
  
}
