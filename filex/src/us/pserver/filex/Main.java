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
      b = b.withBufferSize(opts.get(Option.BUFFER_SIZE).getInt(0));
    }
    Filex fx = b.create();
    if(opts.contains(Option.IGNORE_CASE)) {
      fx.caseSensitiveOff();
    }
    if(opts.contains(Option.INDEXOF)) {
      indexOf(fx, opts.get(Option.INDEXOF));
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
  
  
  public void help() {
    String help = "--------------------------"
        + " Filex - Search & Extract"
        + "--------------------------"
        + " Usage: filex <options> <file>"
        + "  -c / --ignore-case:               -> Disable case sensitivity"
        + "  -g / --get: <pos> <len> [charset] -> Get the content specified"
        + "  -h / --help:                      -> Prints this help text"
        + "  -i / --index-of: [pos] <content>  -> Search the <content> index starting at [pos]"
        + "  -l / --line-unix: [pos]           -> Get a line (unix ending) starting at [pos]"
        + "  -w / --line-win: [pos]            -> Get a line (windows ending) starting at [pos]"
        + "  -s / --buffer-size: <size>        -> Set the internal buffer size"
        + "  -v / --verbose:                   -> Prints additional information on execution";
    System.out.println(help);
  }
  
  
  public void indexOf(Filex fx, OptionArg opa) throws IOException {
    long idx = -1;
    long pos = -1;
    String term = null;
    if(opa.getArgs().size() > 1) {
      pos = opa.getInt(0);
      term = opa.get(1);
      idx = fx.indexOf(opa.getLong(0), opa.get(1));
    }
    else if(opa.getArgs().size() == 1) {
      term = opa.get(0);
      idx = fx.indexOf(opa.get(0));
    }
    else {
      throw new IllegalStateException("indexOf require 1+ args");
    }
    if(opts.contains(Option.VERBOSE)) {
      if(pos >= 0) {
        System.out.printf("indexOf( %d, %s ): %d%n", pos, term, idx);
      }
      else {
        System.out.printf("indexOf( %s ): %d%n", term, idx);
      }
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
  
  
  public static void main(String[] args) throws IOException {
    args = new String[]{"-i", "microdoc", "-v", "/storage/java/filex/disecMicro.sql"};
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
      else {
        larg.add(args[i]);
      }
    }
    new Main(Paths.get(args[args.length -1]), new Options(oar)).exec();
  }
  
}
