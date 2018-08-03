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

package us.pserver.filex;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/05/2018
 */
public class Main {
  
  private final Path path;
  
  private final Options opts;
  
  
  public Main(Path path, Options opts) {
    this.path = path;
    this.opts = opts;
  }
  
  
  public void exec() throws IOException {
    Filex.Builder b = Filex.builder(path).withBufferAllocPolicy(ByteBuffer::allocate);
    if(opts.contains(Option.BUFFER_SIZE)) {
      int bs = opts.get(Option.BUFFER_SIZE).getInt(0);
      if(opts.contains(Option.VERBOSE)) {
        System.out.printf("withBufferSizeTip( %d )%n", bs);
      }
      b = b.withBufferSizeTip(bs);
    }
    Filex fx = b.create();
    if(opts.contains(Option.INDEX_OF)) {
      indexOf(fx, opts.get(Option.INDEX_OF));
    }
    else if(opts.contains(Option.GET)) {
      get(fx, opts.get(Option.GET));
    }
    else if(opts.contains(Option.LINE)) {
      line(fx, opts.get(Option.LINE));
    }
    else if(opts.contains(Option.LINE_WIN)) {
      line(fx, opts.get(Option.LINE_WIN));
    }
    else {
      help();
    }
  }
  
  
  public void indexOf(Filex fx, OptionArg opa) throws IOException {
    long pos = 0;
    String term = null;
    Charset cs = StandardCharsets.UTF_8;
    if(opa.getArgs().size() > 2) {
      pos = opa.getInt(0);
      term = opa.get(1);
      cs = Charset.forName(opa.get(2));
    }
    else if(opa.getArgs().size() > 1) {
      pos = opa.getInt(0);
      term = opa.get(1);
    }
    else if(opa.getArgs().size() == 1) {
      term = opa.get(0);
    }
    else {
      throw new IllegalStateException("indexOf require 1+ args");
    }
    long idx = opts.contains(Option.IGNORE_CASE)
        ? fx.indexOfIgnoreCase(pos, term, cs)
        : fx.indexOf(pos, term, cs);
    if(opts.contains(Option.VERBOSE)) {
      System.out.printf("indexOf( %d, '%s', %s ): %d%n", pos, term, cs, idx);
    }
    else {
      System.out.println(idx);
    }
  }
  
  
  public void get(Filex fx, OptionArg opa) throws IOException {
    long pos = 0;
    int len = -1;
    Charset cs = StandardCharsets.UTF_8;
    if(opa.getArgs().size() > 2) {
      pos = opa.getLong(0);
      len = opa.getInt(1);
      cs = Charset.forName(opa.get(2));
    }
    else if(opa.getArgs().size() > 1) {
      pos = opa.getLong(0);
      len = opa.getInt(1);
    }
    else if(opa.getArgs().size() > 0) {
      len = opa.getInt(0);
    }
    else {
      throw new IllegalStateException("get require 1+ args");
    }
    String cont = fx.get(pos, len, cs);
    if(opts.contains(Option.VERBOSE)) {
      System.out.printf("get( %d, %d, %s ): %s%n", pos, len, cs, cont);
    }
    else {
      System.out.println(cont);
    }
  }
  
  
  public void line(Filex fx, OptionArg opa) throws IOException {
    long pos = 0;
    if(opa.getArgs().size() > 0) {
      pos = opa.getLong(0);
    }
    String ln = fx.getUnixLine(pos);
    if(opts.contains(Option.VERBOSE)) {
      System.out.printf("line( %d ): %s%n", pos, ln);
    }
    else {
      System.out.println(ln);
    }
  }
  
  
  public void lineWin(Filex fx, OptionArg opa) throws IOException {
    long pos = 0;
    if(opa.getArgs().size() > 0) {
      pos = opa.getLong(0);
    }
    String ln = fx.getWinLine(pos);
    if(opts.contains(Option.VERBOSE)) {
      System.out.printf("line( %d ): %s%n", pos, ln);
    }
    else {
      System.out.println(ln);
    }
  }
  
  
  public static void help() {
    String help = "-------------------------------\n"
        + " Filex - File Search & Extract\n"
        + "-------------------------------\n"
        + " Usage: filex <options> <file>\n"
        + " Options:\n"
        + "   -c / --ignore-case:\n"
        + "      Enable ignore case with  -i / --index-of  option\n"
        + "   -g / --get: <pos> <len> [charset]\n"
        + "      Get content starting at [pos=0] with <len> and [charset=UTF-8]\n"
        + "   -h / --help:\n"
        + "      Prints this help text\n"
        + "   -i / --index-of: [pos] <content> [charset]\n"
        + "      Search index of <content> with [charset=UTF-8] starting at [pos=0]\n"
        + "   -l / --line-unix: [pos]\n"
        + "      Get a line (unix ending) starting at [pos=0]\n"
        + "   -w / --line-win: [pos]\n"
        + "      Get a line (windows ending) starting at [pos=0]\n"
        + "   -b / --buffer-size: <size>\n"
        + "      Set the internal buffer size\n"
        + "   -v / --verbose:\n"
        + "      Prints additional information on execution\n";
    System.out.println(help);
  }
  
  
  public static void main(String[] args) throws IOException {
    //String file = "/storage/java/filex/disecMicro.sql";
    String file = "d:/java/filex/disecMicro.sql";
    //args = new String[]{"-i", "0", "EXISTS `sqls`", "UTF-8", "-s", "500", "-v", file};
    args = new String[]{"-i", "0", "exists `SQLS`", "UTF-8", "-c", "-b", "1000", "-v", file};
    //args = new String[]{"-i", "4359", "\n", "-v", file};
    //args = new String[]{"-i", "Grupo de comandos", "-v", file};
    //args = new String[]{"-i", "EXISTS `log`;", file};
    //args = new String[]{"-l", "4360", file};
    //args = new String[]{"-g", "4360", "15", file};
    //args = new String[]{"-g", "4360", "40", file};
    //args = new String[]{"-h"};
    List<OptionArg> oar = new LinkedList<>();
    Option opt = null;
    List<String> larg = new LinkedList<>();
    for(int i = 0; i < args.length; i++) {
      if(args[i].startsWith("-")) {
        if(opt != null) {
          oar.add(new OptionArg(opt, new ArrayList<>(larg)));
          larg.clear();
        }
        opt = Option.fromString(args[i]);
      }
      else if(i < args.length -1) {
        larg.add(args[i]);
      }
    }
    if(opt != null) {
      oar.add(new OptionArg(opt, larg));
    }
    Options opts = new Options(oar);
    if(opts.contains(Option.HELP)) {
      help();
    }
    else {
      new Main(Paths.get(args[args.length -1]), new Options(oar)).exec();
    }
  }
  
}
