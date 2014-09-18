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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 25/07/2013
 */
public class INotesXML {
  
  public static final String DELIM_MAIL_START = "<viewentry position";
  
  public static final String DELIM_MAIL_END = "</viewentry>";

  
  private List<INotesHeaderMail> mails;
  
  
  public INotesXML() {
    mails = new LinkedList<>();
  }


  public List<INotesHeaderMail> getMails() {
    return mails;
  }

  
  public List<INotesHeaderMail> parseXML(String xml) {
    if(xml == null || xml.trim().isEmpty())
      return mails;
    
    mails.clear();
    int istart = xml.indexOf(DELIM_MAIL_START);
    while(istart >= 0) {
      int iend = xml.indexOf(DELIM_MAIL_END, istart);
      mails.add(
          INotesHeaderMail.parse(
          xml.substring(istart, iend)));
      istart = xml.indexOf(DELIM_MAIL_START, iend);
    }
    return mails;
  }
  
  
  public void printMails() {
    for(INotesHeaderMail m : mails)
      System.out.println(m);
  }
  
  
  public static void main(String[] args) {
    String xml = "<readviewentries> <viewentries timestamp=\"20130725T173704,65Z\" toplevelentries=\"4\">\n" +
        "<viewentry position=\"1\" unid=\"B2E0D7D7E19BC75E83257BB300553EC2\" noteid=\"1042E\" siblings=\"4\">\n" +
        "<entrydata columnnumber=\"0\" name=\"$86\">\n" +
        "<number>211</number></entrydata>\n" +
        "<entrydata columnnumber=\"1\" name=\"$Importance\">\n" +
        "<number>0</number></entrydata>\n" +
        "<entrydata columnnumber=\"2\" name=\"$93\">\n" +
        "<text>F6036477 Juno Dani da Rold Roesler</text></entrydata>\n" +
        "<entrydata columnnumber=\"3\" name=\"$73\">\n" +
        "<text>teste</text></entrydata>\n" +
        "<entrydata columnnumber=\"4\" name=\"$70\">\n" +
        "<datetime>20130725T153106,25Z</datetime></entrydata>\n" +
        "<entrydata columnnumber=\"5\" name=\"$106\">\n" +
        "<number>1387</number></entrydata>\n" +
        "<entrydata columnnumber=\"6\" name=\"$ToStuff\">\n" +
        "<number>184</number></entrydata>\n" +
        "<entrydata columnnumber=\"7\" name=\"$97\">\n" +
        "<number>9999</number></entrydata>\n" +
        "<entrydata columnnumber=\"8\" name=\"$109\">\n" +
        "<number>0</number></entrydata>\n" +
        "</viewentry>\n" +
        "<viewentry position=\"2\" unid=\"E8843E1A98872DE683257BA60071DA9F\" noteid=\"102B2\" siblings=\"4\">\n" +
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
        "</viewentry>\n" +
        "<viewentry position=\"3\" unid=\"19C3EEE9EA5F0DEB83257BA2005D05AA\" noteid=\"10222\" siblings=\"4\">\n" +
        "<entrydata columnnumber=\"0\" name=\"$86\">\n" +
        "<number>211</number></entrydata>\n" +
        "<entrydata columnnumber=\"1\" name=\"$Importance\">\n" +
        "<number>0</number></entrydata>\n" +
        "<entrydata columnnumber=\"2\" name=\"$93\">\n" +
        "<text>DINOP-NEG OPERACOES - DINEG</text></entrydata>\n" +
        "<entrydata columnnumber=\"3\" name=\"$73\">\n" +
        "<text>Enc: Alerta _ RECOMENDAÇÃO DE AUDITORIA NR. 66.714</text></entrydata>\n" +
        "<entrydata columnnumber=\"4\" name=\"$70\">\n" +
        "<datetime>20130708T165602,51Z</datetime></entrydata>\n" +
        "<entrydata columnnumber=\"5\" name=\"$106\">\n" +
        "<number>4069</number></entrydata>\n" +
        "<entrydata columnnumber=\"6\" name=\"$ToStuff\">\n" +
        "<number>184</number></entrydata>\n" +
        "<entrydata columnnumber=\"7\" name=\"$97\">\n" +
        "<number>9999</number></entrydata>\n" +
        "<entrydata columnnumber=\"8\" name=\"$109\">\n" +
        "<number>0</number></entrydata>\n" +
        "</viewentry>\n" +
        "<viewentry position=\"4\" unid=\"C9AC40CF87C7420283257B9E005A42E9\" noteid=\"F606\" siblings=\"4\">\n" +
        "<entrydata columnnumber=\"0\" name=\"$86\">\n" +
        "<number>211</number></entrydata>\n" +
        "<entrydata columnnumber=\"1\" name=\"$Importance\">\n" +
        "<number>0</number></entrydata>\n" +
        "<entrydata columnnumber=\"2\" name=\"$93\">\n" +
        "<text>F7655526 Odair Ferreira de Sousa</text></entrydata>\n" +
        "<entrydata columnnumber=\"3\" name=\"$73\">\n" +
        "<text>Recomendação de Auditoria - Qualidade do Processo de Operações BNDES</text></entrydata>\n" +
        "<entrydata columnnumber=\"4\" name=\"$70\">\n" +
        "<datetime>20130701T135922,13Z</datetime></entrydata>\n" +
        "<entrydata columnnumber=\"5\" name=\"$106\">\n" +
        "<number>2843</number></entrydata>\n" +
        "<entrydata columnnumber=\"6\" name=\"$ToStuff\">\n" +
        "<number>185</number></entrydata>\n" +
        "<entrydata columnnumber=\"7\" name=\"$97\">\n" +
        "<number>9999</number></entrydata>\n" +
        "<entrydata columnnumber=\"8\" name=\"$109\">\n" +
        "<number>0</number></entrydata>\n" +
        "</viewentry>\n" +
        "</viewentries>\n" +
        "<dbquotasize> <dbsize>70390</dbsize>  <sizelimit>204800</sizelimit>  <warning>184320</warning>  <ignorequota>1</ignorequota>  <currentusage>70390</currentusage> </dbquotasize>  <unreadinfo>   <foldername>($Inbox)</foldername>  <unreadcount>0</unreadcount> </unreadinfo>  \n" +
        "</readviewentries>";
    
    INotesXML in = new INotesXML();
    in.parseXML(xml);
    in.printMails();
  }
  
}
