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

package us.pserver.sdb.main;

import com.jpower.spj.Option;
import com.jpower.spj.ShellParser;
import java.util.Iterator;
import us.pserver.sdb.Document;
import us.pserver.sdb.Query;
import us.pserver.sdb.Result;
import us.pserver.sdb.SDBException;
import us.pserver.sdb.SimpleDB;
import us.pserver.sdb.engine.CachedFileEngine;
import us.pserver.sdb.engine.FileEngine;
import us.pserver.sdb.engine.MemoryEngine;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 17/10/2014
 */
public class Main {

  private SimpleDB sdb;
  
  private String file;
  
  
  public Main(String file) {
    this.file = file;
  }
  
  
  public SimpleDB engineFile() throws SDBException {
    return (sdb = new SimpleDB(new FileEngine(file)));
  }
  
  
  public SimpleDB engineMemory() throws SDBException {
    return (sdb = new SimpleDB(new MemoryEngine(file)));
  }
  
  
  public SimpleDB engineCachedFile() throws SDBException {
    return (sdb = new SimpleDB(new CachedFileEngine(file)));
  }
  
  
  public SimpleDB sdb() {
    return sdb;
  }
  
  
  public long put(String str) {
    Document doc = parse(str);
    if(doc == null)
      throw new SDBException("-> put: Invalid document format: \""+ str+ "\"");
    doc = sdb.put(doc);
    return doc.block();
  }
  
  
  public static long getLong(String str) {
    try {
      return Long.parseLong(str);
    } catch(NumberFormatException e) {
      return -1;
    }
  }
  
  
  public Document get(String str) {
    if(str == null)
      throw new SDBException("-> get: Invalid document format: \""+ str+ "\"");
    
    if(str.contains("(") && str.contains(")")) {
      int iob = str.indexOf("(");
      int icb = str.indexOf(")");
      str = str.substring(iob+1, icb);
    }
    
    long l = getLong(str);
    if(l >= 0) return sdb.get(l);
    else {
      Document doc = parse(str);
      if(doc == null)
        throw new SDBException("-> get: Invalid document format: \""+ str+ "\"");
      return sdb.getOne(doc);
    }
  }
  
  
  public long remove(String str) {
    Document doc = get(str);
    if(doc == null)
      throw new SDBException("-> remove: No document found for: \""+ str+ "\"");
    doc = sdb.remove(doc.block());
    return doc.block();
  }
  
  
  public Document query(String str) {
    Query q = parseQuery(str);
    System.out.println("-> query: "+ q);
    return sdb.getOne(q);
  }
  
  
  public Result list(String str) {
    if(str == null || str.isEmpty())
      throw new SDBException("-> list: Invalid query string: \""+ str+ "\"");
    if(str.startsWith("query(")) {
      Query q = parseQuery(str);
      System.out.println("-> "+ q);
      return sdb.get(q);
    } else
      return sdb.get(parse(str));
  }
  
  
  public Document update(String str, String str2) {
    if(str == null || str.isEmpty())
      throw new SDBException("-> update: Invalid query string: \""+ str+ "\"");
    if(str2 == null || str2.isEmpty())
      throw new SDBException("-> update: Invalid document: \""+ str2+ "\"");
    
    Document doc = get(str);
    if(doc == null)
      throw new SDBException("-> update: No document found for: \""+ str+ "\"");
    
    Document newdoc = parse(str2);
    if(newdoc == null)
      throw new SDBException("-> update: Invalid document: \""+ str2+ "\"");
    
    newdoc.block(doc.block());
    sdb.put(newdoc);
    return doc;
  }
  
  
  public static int reverseFind(String str, String fnd, int pos) {
    if(str == null || fnd == null
        || !str.contains(fnd))
      return -1;
    
    if(pos <= 0) pos = str.length();
    int st = pos - fnd.length();
    for(int i = st; i >= 0; i--) {
      String sub = str.substring(i, i+fnd.length());
      if(sub.equalsIgnoreCase(fnd))
        return i;
    }
    return -1;
  }
  
  
  private String extArg(String str) {
    if(str == null || !str.contains("(")
        || !str.contains(")"))
      return null;
    int iob = str.indexOf("(");
    int icb = str.indexOf(")");
    return str.substring(iob+1, icb);
  }
  

  private Query invoke(Query q, String method) {
    if(q == null || method == null)
      return q;
    
    method = method.replace("\n", "").replace("\r", "").trim();
    if(method.startsWith("key")) {
      q = q.key(extArg(method));
    }
    else if(method.startsWith("or")) {
      q = q.or(extArg(method));
    }
    else if(method.startsWith("and")) {
      q = q.and(extArg(method));
    }
    else if(method.startsWith("not")) {
      q = q.not();
    }
    else if(method.startsWith("asNumber")) {
      //q = q.asNumber(extArg(method));
      q = q.asNumber();
    }
    else if(method.startsWith("asDate")) {
      //q = q.asDate(extArg(method));
      q = q.asDate();
    }
    else if(method.startsWith("asBoolean")) {
      //q = q.asBoolean(extArg(method));
      q = q.asBoolean();
    }
    else if(method.startsWith("equalIgnoreCase")) {
      q = q.equalIgnoreCase(extArg(method));
    }
    else if(method.startsWith("equal")) {
      q = q.equal(extArg(method));
    }
    else if(method.startsWith("containsIgnoreCase")) {
      q = q.containsIgnoreCase(extArg(method));
    }
    else if(method.startsWith("contains")) {
      q = q.contains(extArg(method));
    }
    else if(method.startsWith("greaterEquals")) {
      q = q.greaterEquals(extArg(method));
    }
    else if(method.startsWith("greater")) {
      q = q.greater(extArg(method));
    }
    else if(method.startsWith("lesserEquals")) {
      q = q.lesserEquals(extArg(method));
    }
    else if(method.startsWith("lesser")) {
      q = q.lesser(extArg(method));
    }
    else if(method.startsWith("startsWith")) {
      q = q.startsWith(extArg(method));
    }
    else if(method.startsWith("endsWith")) {
      q = q.endsWith(extArg(method));
    }
    else
      throw new SDBException("Invalid Query method: \""+ method+ "\"");
    
    return q;
  }
  
  
  public Query parseQuery(String str) throws SDBException {
    if(str == null || !str.startsWith("query("))
      throw new SDBException("Invalid query: \""+ str+ "\"");
    
    String[] meths = str.split("\\.");
    Query q = new Query(extArg(meths[0]));
    
    for(int i = 1; i < meths.length; i++) {
      q = invoke(q, meths[i]);
    }
    return q;
  }
  
  
  public Document parse(String str) throws SDBException {
    if(str.startsWith("get(")
        && str.endsWith(")"))
      return get(str);
        
    if(str == null 
        || str.isEmpty() 
        || !str.contains(":")
        || (!str.contains("{")
        &&  !str.contains("}")))
      return null;
    
    int iob = str.indexOf("{");
    int icb = str.lastIndexOf("}");
    String label = str.substring(0, iob);
    String body = str.substring(iob+1, icb);
    
    StringBuilder sb = new StringBuilder();
    char[] cs = body.toCharArray();
    Document doc = new Document(label);
    String key = null;
    Object value = null;
    boolean cont = false;
    int opbr = 0;
    for(int i = 0; i < cs.length; i++) {
      if(cs[i] == '}') {
        sb.append(cs[i]);
        opbr--;
        if(opbr == 0) {
          value = parse(sb.toString());
          sb.delete(0, sb.length());
          cont = false;
        }
      }
      
      if(cont) {
        sb.append(cs[i]);
        continue;
      }
      
      if(cs[i] == ':') {
        key = sb.toString();
        sb.delete(0, sb.length());
      }
      else if(cs[i] == ',') {
        value = sb.toString();
        if(value.toString().startsWith("get(")
            && value.toString().endsWith(")"))
          value = get(value.toString());
        sb.delete(0, sb.length());
      }
      else if(cs[i] == '{') {
        sb.append(cs[i]);
        cont = true;
        opbr++;
      }
      else {
        sb.append(cs[i]);
      }
      
      if(key != null && value != null) {
        doc.put(key, value);
        key = null;
        value = null;
      }
    }
    
    if(key != null && sb.length() > 0) {
      value = sb.toString();
      if(value.toString().startsWith("get(")
          && value.toString().endsWith(")"))
        value = get(value.toString());
    }
    
    if(key != null && value != null) {
      doc.put(key, value);
    }
    return doc;
  }
  
  
  public static String doc2str(Document doc) {
    if(doc == null) return null;
    StringBuffer sb = new StringBuffer();
    sb.append(doc.label())
        .append("{");
    Iterator<String> it = doc.map().keySet().iterator();
    while(it.hasNext()) {
      String key = it.next();
      sb.append(key).append(":");
      Object v = doc.get(key);
      if(v instanceof Document)
        v = doc2str((Document)v);
      sb.append(v.toString());
      if(it.hasNext())
        sb.append(",");
    }
    return sb.append("}").toString();
  }
  
  
  public static void main(String[] args) {
    //args = new String[]{"-h"};
    //args = new String[]{"file.db", "-e", "cached", "-p", "creds{user:juno,pass:123456}"};
    //args = new String[]{"file.db", "-e", "cached", "-p", "server{name:102,ip:172.29.14.102,creds:get(1)}"};
    //args = new String[]{"file.db", "-e", "cached", "-p", "server{name:103,ip:172.29.14.103,apps:7}"};
    //args = new String[]{"file.db", "-e", "cached", "-p", "server{name:104,ip:172.29.14.104,apps:4}"};
    //args = new String[]{"file.db", "-e", "cached", "-p", "server{name:102,ip:172.29.14.102}"};
    //args = new String[]{"file.db", "-e", "cached", "-g", "server{name:102}"};
    //args = new String[]{"file.db", "-e", "cached", "-g", "server{name:102}", "-k", "creds", "-b"};
    //args = new String[]{"file.db", "-e", "cached", "-g", "get(1)"};
    //args = new String[]{"file.db", "-e", "cached", "-r", "get(creds{user:juno})"};
    args = new String[]{"file.db", "-e", "cached", "-q", "query(server)\n    .key(apps)\n    .asNumber.lesser(5).and(name).not.equal(102)", "-b"};
    //args = new String[]{"file.db", "-e", "cached", "-l", "query(server)"};
    //args = new String[]{"file.db", "-e", "cached", "-l", "server{name:102}"};
    //args = new String[]{"file.db", "-e", "cached", "-u", "get(server{name:102})", "server{name:102,ip:172.29.14.102,apps:3}"};
    
    ShellParser sp = new ShellParser()
        .setAppName("SimpleDB")
        .setAuthor("Juno Roesler")
        .setContact("juno@pserver.us")
        .setDescription("Command Line Interface")
        .setLicense("GNU/LGPLv3")
        .setYear("2014");
    
    Option o = Option.EMPTY_OPTION;
    o.setMandatory(true)
        .setAcceptArgs(true)
        .setDescription("Database file")
        .setExclusive(false);
    sp.addOption(o);
    
    o = new Option("-e")
        .setMandatory(false)
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setDescription("Database Engine {file|cached|memory}")
        .setExclusive(false)
        .setLongName("--engine");
    sp.addOption(o);
    
    o = new Option("-p")
        .setMandatory(false)
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setDescription("Insert a document in the database\n    -> Document Format: \"label{key:value,key2:value2...}\"")
        .setExclusive(false)
        .setLongName("--put");
    sp.addOption(o);
    
    o = new Option("-g")
        .setMandatory(false)
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setDescription("Retrieve a document from database {<document>|<get(arg)>}")
        .setExclusive(false)
        .setLongName("--get");
    sp.addOption(o);
    
    o = new Option("-k")
        .setMandatory(false)
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setDescription("Return the value for <key> from the document")
        .setExclusive(false)
        .setLongName("--key");
    sp.addOption(o);
    
    o = new Option("-b")
        .setMandatory(false)
        .setAcceptArgs(false)
        .setArgsSeparator(" ")
        .setDescription("Return the index block from the document")
        .setExclusive(false)
        .setLongName("--block");
    sp.addOption(o);
    
    o = new Option("-q")
        .setMandatory(false)
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setDescription("Query database"
            + "\n    -> Query Format: \"query(<label>).key(<k>).method1(<arg1>).method2(<arg2>)...\""
            + "\n    -> Methods: or(key), and(key), not(), asNumber(), asDate(), asBoolean(), "
            + "\n                equal(val), equalIgnoreCase(val), contains(val), "
            + "\n                containsIgnoreCase(val), greater(val), greaterEquals(val), "
            + "\n                lesser(val), lesserEquals(val), startsWith(val), endsWith(val)")
        .setExclusive(false)
        .setLongName("--query");
    sp.addOption(o);
    
    o = new Option("-r")
        .setMandatory(false)
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setDescription("Remove a document from database {<get(arg)>}")
        .setExclusive(false)
        .setLongName("--remove");
    sp.addOption(o);
    
    o = new Option("-l")
        .setMandatory(false)
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setDescription("Lists all matches for <query>")
        .setExclusive(false)
        .setLongName("--list");
    sp.addOption(o);
    
    o = new Option("-u")
        .setMandatory(false)
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setDescription("Update a document {get(<block>), <document>}")
        .setExclusive(false)
        .setLongName("--update");
    sp.addOption(o);
    
    o = new Option("-h")
        .setMandatory(false)
        .setAcceptArgs(true)
        .setArgsSeparator(" ")
        .setDescription("Show this help text")
        .setExclusive(false)
        .setLongName("--help");
    sp.addOption(o);
    
    sp.parseArgs(args);
    if(sp.isOptionPresent("-h")) {
      System.out.println(sp.createHeader(40));
      System.out.println(sp.createUsage());
      System.exit(0);
    }
    if(!sp.parseErrors()) {
      System.out.println(sp.createHeader(40));
      System.out.println(sp.createUsage());
      sp.printWarningMessages(System.out);
      sp.printErrorMessages(System.err);
    }
    
    Main m = new Main(sp.getOption(Option.EMPTY).getFirstArg());
    
    if(sp.isOptionPresent("-e")) {
      String arg = sp.getOption("-e").getFirstArg();
      if(arg.equalsIgnoreCase("file"))
        m.engineFile();
      else if(arg.equalsIgnoreCase("cached"))
        m.engineCachedFile();
      else if(arg.equalsIgnoreCase("memory"))
        m.engineMemory();
      else {
        System.out.println(sp.createHeader(40));
        System.out.println(sp.createUsage());
        System.out.println("# Invalid Engine: "+ arg);
        System.exit(1);
      }
    } else {
      m.engineCachedFile();
    }
    
    Document doc = null;
    
    try {
      if(sp.isOptionPresent("-p")) {
        String arg = sp.getOption("-p").getFirstArg();
        System.out.println(m.put(arg));
      }
      else if(sp.isOptionPresent("-g")) {
        String arg = sp.getOption("-g").getFirstArg();
        doc = m.get(arg);
        if(doc == null)
          throw new SDBException("-> get: No document found for: \""+ arg+ "\"");
        if(sp.isOptionPresent("-k")) {
          arg = sp.getOption("-k").getFirstArg();
          Object v = doc.get(arg);
          if(v instanceof Document) {
            if(sp.isOptionPresent("-b")) {
              v = ((Document)v).block();          
            } else {
              v = doc2str((Document)v);
            }
          }
          System.out.println(v.toString());
        } else if(sp.isOptionPresent("-b")) {
          System.out.println(doc.block());
        } else {
          System.out.println(doc2str(doc));
        }
      }
      else if(sp.isOptionPresent("-q")) {
        String arg = sp.getOption("-q").getFirstArg();
        doc = m.query(arg);
        if(doc == null)
          throw new SDBException("-> query: No document found for: \""+ arg+ "\"");
        if(sp.isOptionPresent("-k")) {
          arg = sp.getOption("-k").getFirstArg();
          Object v = doc.get(arg);
          if(v instanceof Document) {
            if(sp.isOptionPresent("-b")) {
              v = ((Document)v).block();          
            } else {
              v = doc2str((Document)v);
            }
          }
          System.out.println(v.toString());
        } else if(sp.isOptionPresent("-b")) {
          System.out.println(doc.block());
        } else {
          System.out.println(doc2str(doc));
        }
      }
      else if(sp.isOptionPresent("-r")) {
        String arg = sp.getOption("-r").getFirstArg();
        System.out.println(m.remove(arg));
      }
      else if(sp.isOptionPresent("-l")) {
        String arg = sp.getOption("-l").getFirstArg();
        Result rs = m.list(arg);
        System.out.println("result list: "+ rs.size());
        for(int i = 0; i < rs.size(); i++) {
          Document d = rs.get(i);
          System.out.println("  "+ d.block()+ ": "+ doc2str(d));
        }
      }
      else if(sp.isOptionPresent("-u")) {
        String arg = sp.getOption("-u").getFirstArg();
        String arg2 = sp.getOption("-u").getArg(1);
        System.out.println(doc2str(m.update(arg, arg2)));
      }
    } 
    catch(SDBException e) {
      System.err.println("# "+ e.getMessage());
    }
    finally {
      m.sdb().close();
    }
  }
  
}
