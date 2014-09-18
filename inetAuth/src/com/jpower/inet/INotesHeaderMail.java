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

package com.jpower.inet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 25/07/2013
 */
public class INotesHeaderMail {
  
  public static final String DELIM_SUBJECT = "<entrydata columnnumber=\"3\" name=\"$73\">";
  
  public static final String DELIM_FROM = "<entrydata columnnumber=\"2\" name=\"$93\">";
  
  public static final String DELIM_UID = "unid=";
  
  public static final String DELIM_DATE_START = "<datetime>";
  
  public static final String DELIM_DATE_END = ",";
  
  public static final String DELIM_TEXT_START = "<text>";
  
  public static final String DELIM_TEXT_END = "</text>";
  
  
  private String subject;
  
  private String from;
  
  private String uid;
  
  private Date date;
  
  
  public INotesHeaderMail() {}


  public String getSubject() {
    return subject;
  }


  public INotesHeaderMail setSubject(String subject) {
    this.subject = subject;
    return this;
  }


  public String getFrom() {
    return from;
  }


  public INotesHeaderMail setFrom(String from) {
    this.from = from;
    return this;
  }


  public String getUid() {
    return uid;
  }


  public INotesHeaderMail setUid(String uid) {
    this.uid = uid;
    return this;
  }


  public Date getDate() {
    return date;
  }


  public INotesHeaderMail setDate(Date date) {
    this.date = date;
    return this;
  }
  
  
  public static INotesHeaderMail parse(String xml) {
    if(xml == null || xml.trim().isEmpty())
      return null;
    
    INotesHeaderMail mail = new INotesHeaderMail();
    DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    
    int is = xml.indexOf(DELIM_UID) 
        + DELIM_UID.length() +1;
    int ie = xml.indexOf("\"", is);
    mail.setUid(xml.substring(is, ie));
    
    is = xml.indexOf(DELIM_FROM) + DELIM_FROM.length();
    is = xml.indexOf(DELIM_TEXT_START, is) 
        + DELIM_TEXT_START.length();
    ie = xml.indexOf(DELIM_TEXT_END, is);
    mail.setFrom(xml.substring(is, ie));
    
    is = xml.indexOf(DELIM_SUBJECT) + DELIM_SUBJECT.length();
    is = xml.indexOf(DELIM_TEXT_START, is) 
        + DELIM_TEXT_START.length();
    ie = xml.indexOf(DELIM_TEXT_END, is);
    mail.setSubject(xml.substring(is, ie));
    
    String sd;
    is = xml.indexOf(DELIM_DATE_START) + DELIM_DATE_START.length();
    ie = xml.indexOf(DELIM_DATE_END, is);
    sd = xml.substring(is, ie);
    try {
      Date d = df.parse(sd);
      d = new Date(d.getTime() - (3 * 60 * 60 * 1000));
      mail.setDate(d);
    } catch(ParseException ex) {
      ex.printStackTrace();
    }
    
    return mail;
  }


  @Override
  public String toString() {
    return "INotesHeaderMail{\n" + "  subject=" + subject + ",\n  from=" + from + ",\n  uid=" + uid + ",\n  date=" + date + "\n}";
  }

  
  public static void main(String[] args) {
    String xml = "<viewentry position=\"2\" unid=\"E8843E1A98872DE683257BA60071DA9F\" noteid=\"102B2\" siblings=\"4\">\n" +
        "<entrydata columnnumber=\"0\" name=\"$86\">\n" +
        "<number>211</number></entrydata>\n" +
        "<entrydata columnnumber=\"1\" name=\"$Importance\">\n" +
        "<number>0</number></entrydata>\n" +
        "<entrydata columnnumber=\"2\" name=\"$93\">\n" +
        "<text>F7655526 Odair Ferreira de Sousa</text></entrydata>\n" +
        "<entrydata columnnumber=\"3\" name=\"$73\">\n" +
        "<text>Acompanhamento da Qualidade do Processo de Operações BNDES</text></entrydata>\n" +
        "<entrydata columnnumber=\"4\" name=\"$70\">\n" +
        "<datetime>20130712T204336,18Z</datetime></entrydata>\n" +
        "<entrydata columnnumber=\"5\" name=\"$106\">\n" +
        "<number>2268</number></entrydata>\n" +
        "<entrydata columnnumber=\"6\" name=\"$ToStuff\">\n" +
        "<number>186</number></entrydata>\n" +
        "<entrydata columnnumber=\"7\" name=\"$97\">\n" +
        "<number>9999</number></entrydata>\n" +
        "<entrydata columnnumber=\"8\" name=\"$109\">\n" +
        "<number>0</number></entrydata>\n" +
        "</viewentry>";
    System.out.println(INotesHeaderMail.parse(xml));
  }
  
}
