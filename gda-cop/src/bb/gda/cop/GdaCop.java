/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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
package bb.gda.cop;

import com.jpower.date.SimpleDate;
import com.jpower.sisbb.Field;
import com.jpower.sisbb.Screen;
import com.jpower.sisbb.Session;
import com.jpower.sisbb.Sisbb;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/07/2012
 */
public class GdaCop {

  
  public static final String INDEX_KEY = "INDEX";
  
  private static Sisbb s;
  
  private static int index = 0;
  
  private static Properties p = new Properties();
  
  private static File pf = new File("c:/baixa/gda-cop.properties");
  
  
  public static void init() {
    if(pf.exists())
      loadIndex(pf);
    
    /*
    try {
      Sisbb.DELAY = 40;
      Session ses = new Session("A");
      s = new Sisbb(ses);
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    */
  }
  
  
  public static void saveIndex(File f) {
    try {
      p.setProperty(INDEX_KEY, String.valueOf(index));
      FileOutputStream fos = new FileOutputStream(f);
      p.store(fos, null);
      fos.flush();
      fos.close();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public static void loadIndex(File f) {
    try {
      FileInputStream fis = new FileInputStream(f);
      p.load(fis);
      index = Integer.parseInt(p.getProperty(INDEX_KEY));
      fis.close();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public static void readWrite(String readFile, String writeFile) {
    try {
      //init();
      
      File rf = new File(readFile);
      FileReader fr = new FileReader(rf);
      BufferedReader br = new BufferedReader(fr);
      
      File wf = new File(writeFile);
      boolean append = wf.exists();
      if(!append) wf.createNewFile();
      
      FileWriter fw = new FileWriter(wf, append);
      BufferedWriter bw = new BufferedWriter(fw);
      
      bw.write("PREF;OPERACAO ;SITUACAO   ;PRODU;MODAL;PRMOD        ;DATA      ;VALOR");
      bw.newLine();
      
      String line = null;
      int l = 0;
      while((line = br.readLine()) != null) {
        
        if(l++ <= index) continue;
        else saveIndex(pf);
        
        index++;
        if(index % 10000 == 0) System.out.println(index);
        line = compute(line);
        if(line != null && !line.equals("null")) {
          bw.write(line);
          bw.newLine();
          l++;
          bw.flush();
          fw.flush();
        } 
      }
      System.out.println("* lines: "+l);
      
      bw.flush();
      bw.close();
      br.close();
      
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public static String compute(String line) {
    if(line.length() < 225) return "null";
    
    String oper = line.substring(1, 10);
    if(oper == null || oper.trim().isEmpty() 
        || oper.contains("?"))
      return "null";
    String age = oper.substring(0, 4);
    
    //FORMALIZADA
    String situacao = line.substring(41, 52);
    
    if(!situacao.equals("FORMALIZADA")
        && !situacao.contains("DESPACHAD"))
      return "null";
    
    /*
    if(situacao.equals("ACOLHIDA   ")
        || situacao.contains("?")
        || situacao.trim().isEmpty()) return "null";
    */
    
    String sdate = line.substring(56, 66);
    if(sdate == null || sdate.trim().isEmpty() 
        || sdate.contains("?")) return "null";
    
    SimpleDate date = SimpleDate.parseDate(sdate);
    SimpleDate start = SimpleDate.parseDate("30/11/2012", SimpleDate.DDMMYYYY_SLASH);
    SimpleDate end = SimpleDate.parseDate("31/12/2012", SimpleDate.DDMMYYYY_SLASH);
    
    if(!date.isBetween(start, end)) return "null";
    
    /*
    String lnCred = getLinhaCredito(age, oper);
    if(lnCred == null || lnCred.trim().isEmpty())
      return "null";
    */
    
    String pref = line.substring(84, 88);
    if(pref.contains("?")) return "null";
    if(pref.equals("    ")) pref = age;
    
    String valor = line.substring(198, 225);
    if(valor.contains("?")) return "null";
    
    int idx = -1;
    for(int i = 0; i < valor.length(); i++) {
      if(valor.charAt(i) != '0') {
        idx = i;
        break;
      }
    }
    if(idx < 0) valor = "0";
    else valor = valor.substring(idx);
    
    String produto = line.substring(21, 26);
    String modalidade = line.substring(30, 35);
    String D = ";";
    
    
    StringBuilder sb = new StringBuilder();
    sb.append(pref).append(D)
        .append(oper).append(D)
        .append(situacao).append(D)
        .append(produto).append(D)
        .append(modalidade).append(D)
        .append(produto).append("010").append(modalidade).append(D)
        .append(date.format(SimpleDate.DDMMYYYY_SLASH)).append(D)
        //.append(lnCred).append(D)
        .append(valor);
    
    return sb.toString();
  }
  
  
  public static String getLinhaCredito(String age, String oper) {
    if(age == null || oper == null) return null;
    Screen sc = new Screen("COPM1121");
    sc.addArg(new Field(new Point(6, 72), age));
    sc.addArg(new Field(new Point(7, 72), oper));
    sc.addArg(new Field(new Point(21, 47), "11"));
    sc.setKey(Sisbb.ENTER_STR);
    
    s.setCursor(new Point(7, 72));
    s.deleteLine();
    
    sc.exec(s);
    
    sc = new Screen("COPM1124");
    sc.addField(new Field("linha", new Point(4, 28), 40));
    sc.setKey(Sisbb.F3_STR);
    sc.exec(s);
    
    return sc.getField("linha").getContent();
  }
  
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    readWrite("c:/Baixa/BBM.COPF560A.O9910.D9953.D130111.SS000121.TXT", 
        "c:/Baixa/gda_2013-01-11.txt");
  }
}
