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

package us.pserver.cdr.b64;

import com.jpower.spj.Option;
import com.jpower.spj.ShellParser;
import java.io.IOException;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.FileUtils;
import us.pserver.cdr.StringByteConverter;

/**
 * Classe principal que processa os argumentos da aplicação.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 13/08/2013
 */
public class Main {

  public static void main(String[] args) throws IOException {
    ShellParser sp = new ShellParser()
        .setAppName("Base64")
        .setAuthor("Juno Roesler")
        .setContact("juno@pserver.us")
        .setDescription("Base64 File Coder")
        .setLicense("GNU LGPL v2.1")
        .setHeaderComment("Base64Coder Author: Christian d'Heureuse")
        .setYear("2013");
    
    Option encode = new Option("-e")
        .setLongName("--encode")
        .setDescription("Encode content to Base64")
        .setAcceptArgs(false)
        .setMandatory(false);
    
    Option decode = new Option("-d")
        .setLongName("--decode")
        .setDescription("Decode content from Base64")
        .setAcceptArgs(false)
        .setMandatory(false);
    
    Option input = new Option("-i")
        .setLongName("--input")
        .setDescription("Input to encode/decode")
        .setAcceptArgs(true)
        .setMandatory(true);
        
    Option output = new Option("-o")
        .setLongName("--output")
        .setDescription("File output")
        .setAcceptArgs(true)
        .setMandatory(false);
    
    Option file = new Option("-f")
        .setLongName("--file")
        .setDescription("Read input from file")
        .setAcceptArgs(false)
        .setMandatory(false);
    
    Option gui = new Option("-g")
        .setLongName("--gui")
        .setDescription("Executes in graphical mode")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    
    Option help = new Option("-h")
        .setLongName("--help")
        .setDescription("Show this help text")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    
    sp.addOption(encode)
        .addOption(decode)
        .addOption(input)
        .addOption(output)
        .addOption(file)
        .addOption(gui)
        .addOption(help);
    
    
    System.out.println(sp.createHeader(40));
    
    sp.parseArgs(args);
    if(sp.getOption("-h").isPresent()) {
      System.out.println(sp.createUsage());
      System.exit(0);
    }
    
    if(sp.getOption("-g").isPresent()) {
      Base64Gui.main(null);
      try { Thread.currentThread().join(); }
      catch(InterruptedException e) {}
    }
    
    if(!sp.parseErrors()) {
      System.out.println(sp.createUsage());
      sp.printAllMessages(System.out);
      System.exit(1);
    }
    if(!sp.getOption("-e").isPresent()
        && !sp.getOption("-d").isPresent()) {
      System.out.println(sp.createUsage());
      System.out.println("# Invalid Options: specify encode/decode option.");
      System.exit(1);
    }
    
    Base64FileCoder filecoder = new Base64FileCoder();
    Base64StringCoder strcoder = new Base64StringCoder();
    Base64BufferCoder bufcoder = new Base64BufferCoder();
    Base64ByteCoder bytecoder = new Base64ByteCoder();
    ByteBufferConverter bufconv = new ByteBufferConverter();
    StringByteConverter strconv = new StringByteConverter();
    boolean encd = sp.getOption("-e").isPresent();
    
    if(sp.getOption("-f").isPresent() 
        && sp.getOption("-o").isPresent()) {
      boolean success = filecoder.apply(
          FileUtils.path(sp.getOption("-i").getArg(0)), 
          FileUtils.path(sp.getOption("-o").getArg(0)), encd);
      
      if(success) {
        System.out.println("* Execution OK");
      } else {
        System.out.println("# Error Executing operation");
      }
    } 
    
    else if(!sp.getOption("-f").isPresent()
        && sp.getOption("-o").isPresent()) {
      
      String ins = sp.getOption("-i").getArg(0);
      String dst = sp.getOption("-o").getArg(0);
      
      byte[] bs = bytecoder.apply(strconv.convert(ins), encd);
      boolean success = filecoder.applyFrom(
          bufconv.reverse(bs), FileUtils.path(dst), encd);
      
      if(success) {
        System.out.println("* Execution OK");
      } else {
        System.out.println("# Error Executing operation");
      }
    }
    
    else if(sp.getOption("-f").isPresent()
        && !sp.getOption("-o").isPresent()) {
      filecoder.applyTo(
          FileUtils.path(sp.getOption("-i").getArg(0)), 
          System.out, encd);
    }
    
    else {
      System.out.println(
          strcoder.apply(sp.getOption("-i").getArg(0), 
          sp.getOption("-e").isPresent()));
    }
  }
  
}
