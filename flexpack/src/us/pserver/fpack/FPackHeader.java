/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca e um software livre; voce pode redistribui-la e/ou modifica-la sob os
 * termos da Licenca Publica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versao 2.1 da Licenca, ou qualquer
 * versao posterior.
 * 
 * Esta biblioteca eh distribuida na expectativa de que seja util, porem, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE
 * OU ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto
 * com esta biblioteca; se nao, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.fpack;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import us.pserver.tools.UTF8String;
import us.pserver.valid.Valid;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class FPackHeader {

  
  private String name;
  
  private long position;
  
  private long size;
  
  
  protected FPackHeader() {
    name = null;
    position = 0;
    size = 0;
    
  }
  
  
  public FPackHeader(String name) {
    this.name = Valid.off(name).forEmpty()
        .getOrFail("Invalid name: ");
    position = 0;
    size = 0;
  }
  
  
  public FPackHeader(String name, long pos, long size) {
    this.name = Valid.off(name).forEmpty()
        .getOrFail("Invalid name: ");
    position = pos;
    this.size = size;
  }


  public String getName() {
    return name;
  }


  public FPackHeader setName(String name) {
    this.name = name;
    return this;
  }


  public long getPosition() {
    return position;
  }


  public FPackHeader setPosition(long position) {
    this.position = position;
    return this;
  }


  public long getSize() {
    return size;
  }


  public FPackHeader setSize(long size) {
    this.size = size;
    return this;
  }


  private static String findValue(String str, String name, String token) {
    int is = str.indexOf(name);
    is = str.indexOf(":", is);
    int ie = str.indexOf(token, is);
    String found = null;
    if(is >= 0 && is < ie) 
      found = str.substring(is+1, ie).trim();
    //System.out.println("* findValue('"+ str+ "', '"+ name+ "'): "+ found);
    return str.substring(is+1, ie).trim();
  }
  
  
  public static FPackHeader fromString(String str) {
    if(str == null || str.isEmpty()
        || !str.contains("name")
        || !str.contains("position")
        || !str.contains("size")) {
      return null;
    }
    FPackHeader hd = new FPackHeader(
        findValue(str, "name", ",").replace("\"", ""))
        .setPosition(Long.parseLong(
            findValue(str, "position", ","))
        ).setSize(Long.parseLong(
            findValue(str, "size", "}"))
        );
    return hd;
  }
  
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"name\":\"")
        .append(name)
        .append("\",\"position\":")
        .append(position)
        .append(",\"size\":")
        .append(size).append("}");
    return sb.toString();
  }
  
  
  public void write(OutputStream out) throws IOException {
    Valid.off(out).forNull()
        .fail(OutputStream.class);
    byte[] bs = new UTF8String(
        this.toString()).getBytes();
    out.write(bs);
  }
  
  
  
  public static void main(String[] args) throws IOException {
    FPackHeader hd = new FPackHeader("header")
        .setPosition(52).setSize(120);
    String tostr = hd.toString();
    System.out.println("* header.toString()  : "+ tostr);
    hd = FPackHeader.fromString(tostr);
    System.out.println("* header.fromString(): "+ hd.toString());
  }
  
}
