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

package us.pserver.download.file;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/12/2016
 */
public class StringPad {
  
  private final String str;
  
  
  private StringPad(String str) {
    Objects.requireNonNull(str, "Bad Null String");
    this.str = str;
  }
  
  
  public StringPad with(String str) {
    return new StringPad(str);
  }
  
  
  public static StringPad of(String str) {
    return new StringPad(str);
  }
  

  public String lpad(String pad, int length) {
    Objects.requireNonNull(pad, "Bad Null String Pad");
    if(length <= str.length()) return str;
    StringBuilder sb = new StringBuilder(str);
    while(sb.length() < length) {
      sb.insert(0, pad);
    }
    if(length < sb.length()) {
      sb.delete(0, (sb.length() - length));
    }
    return sb.toString();
  }
  

  public String rpad(String pad, int length) {
    Objects.requireNonNull(pad, "Bad Null String Pad");
    if(length <= str.length()) return str;
    StringBuilder sb = new StringBuilder(str);
    while(sb.length() < length) {
      sb.append(pad);
    }
    if(length < sb.length()) {
      sb.delete((sb.length() - length), sb.length());
    }
    return sb.toString();
  }
  

  public String cpad(String pad, int length) {
    Objects.requireNonNull(pad, "Bad Null String Pad");
    if(length <= str.length()) return str;
    StringBuilder sb = new StringBuilder();
    while((sb.length() + str.length()) < length) {
      sb.append(pad);
    }
    sb.insert((sb.length() / 2), str);
    boolean tail = true;
    while(sb.length() > length) {
      if(tail) sb.deleteCharAt(sb.length() -1);
      else sb.deleteCharAt(0);
      tail = !tail;
    }
    return sb.toString();
  }
  
  
  public String concat(String sep, int length, String ... args) {
    Objects.requireNonNull(sep, "Bad Null String Separator");
    Objects.requireNonNull(args, "Bad Null String Arguments");
    if(sep.length() == 0 || args.length == 0) {
      throw new IllegalArgumentException("Bad String Length: '"+ sep+ "', "+ Arrays.toString(args));
    }
    int[] iargs = new int[args.length];
    StringBuilder sb = new StringBuilder(str);
    iargs[0] = sb.length();
    for(int i = 0; i < args.length; i++) {
      sb.append(args[i]);
      if(i < iargs.length -1) {
        iargs[i+1] = sb.length();
      }
    }
    int argslen = Arrays.asList(args).stream().map(String::length).reduce(0, Integer::sum) + str.length();
    if(length < argslen) {
      return sb.toString();
    }
    while(sb.length() < length) {
      for(int i = 0; i < args.length; i++) {
        sb.insert(iargs[i], sep);
        for(int j = i+1; j < iargs.length; j++) {
          iargs[j] += sep.length();
        }
      }
    }
    int i = iargs.length -1;
    while(sb.length() > length) {
      sb.deleteCharAt(iargs[i]);
      iargs[i] -= 1;
      if(i-- < 0) i = iargs.length;
    }
    return sb.toString();
  }
  
}
