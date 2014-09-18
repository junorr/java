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

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 25/07/2013
 */
public class INotesMail {
  
  public static final String
      
      ALT_PRINCIPAL = "$AltPrincipal",
      
      X_IBM_COOKIE = "%%Nonce",
      
      ALT_FROM = "AltFrom",
      
      CCO = "BlindCopyTo",
      
      BODY = "Body",
      
      BODY_IMG_CIDS = "BodyImgCids",
      
      CC = "CopyTo",
      
      FROM = "From",
      
      PRINCIPAL = "Principal",
      
      TO = "SendTo",
      
      SUBJECT = "Subject",
      
      H_NAME = "h_Name",
      
      ATTACHMENT_NAME = "HaikuUploadAttachment";
  
  
  public static final String[]
      
      MISC_KEYS = {
        "$Alarm", 
        "$KeepPrivate",
        "%%PostCharset",
        "Alarms",
        "DeliveryPriority",
        "DeliveryReport",
        "Encrypt",
        "ExpandPersonalGroups",
        "Form",
        "Importance",
        "MailOptions",
        "ReturnReceipt",
        "SaveOptions",
        "Sign",
        "TrustInetCerts",
        "h_EditAction",
        "h_ImageCount",
        "h_NoSceneTrail",
        "h_SetCommand",
        "h_SetEditCurrentScene",
        "h_SetPublishAction",
        "h_SetReturnURL",
        "h_SetSaveDoc",
        "s_ConvertImage",
        "s_DisclaimerIsAdded",
        "s_IgnoreQuota",
        "s_PlainEditor",
        "s_RemoveFollowUpAlarm",
        "s_SaveFollowUp",
        "s_SaveFollowUpAlarm",
        "s_SetForwardedFrom",
        "s_SetReplyFlag",
        "s_UsePlainText",
        "s_UsePlainTextAndHTML"
      },
      
      MISC_VALUES = {
        "0",
        "0",
        "%%UTF-8",
        "0",
        "N",
        "B",
        "0",
        "1",
        "Memo",
        "2",
        "1",
        "0",
        "1",
        "0",
        "0",
        "h_Next",
        "0",
        "0",
        "h_ShimmerSendMail",
        "l_StdPageEdit",
        "h_Publish",
        "[[./&Form=l_CallListener]]",
        "1",
        "0",
        "0",
        "0",
        "0",
        "0",
        "0",
        "0",
        "0",
        "0",
        "0",
        "0"
      };
  
  public static final String JSON_UID = "sUnid: ",
      
      JSON_TO = "SendToHtml=",
      
      JSON_CC = "CopyToHtml=",
      
      JSON_CCO = "BlindCopyToHtml=",
      
      JSON_FROM = "FromHtml=",
      
      JSON_SUBJECT = "SubjectHtml=",
      
      JSON_CONTENT = "BodyHtml=",
      
      JSON_ATTACHMENT_NAMES = "h_AttachmentNames",
      
      JSON_ATTACHMENT_NAMES_END = "h_AttachmentNamesAlt",
      
      JSON_OBJECT_DELIM = "0\": \"",
      
      JSON_NUM_ATTACHMENTS = "h_NumAttachments",
      
      JSON_ATTACHMENT_LENGTHS = "h_AttachmentLengths",
      
      JSON_ATTACHMENT_LENGTHS_END = "h_AttachmentLengthsAlt";
      
      
      
      
  private String from;
  
  private String[] to;
  
  private String[] cc;
  
  private String[] cco;
  
  private String subject;
  
  private String content;
  
  private String xIbmCookie;
  
  private String uid;
  
  private Date date;
  
  private INotesHeaderMail header;
  
  private List<Attachment> attachs = new LinkedList<>();
  
  
  public INotesMail() {}
  
  
  public INotesMail attach(Attachment at) {
    if(at != null && at.getName() != null) {
      attachs.add(at);
    }
    return this;
  }
  
  
  public Attachment attachment(int idx) {
    if(idx < 0 || idx >= attachs.size()
        || attachs.isEmpty())
      return null;
    
    return attachs.get(idx);
  }
  
  
  public Attachment firstAttachment() {
    return this.attachment(0);
  }
  
  
  public Attachment lastAttachment() {
    return this.attachment(attachs.size()-1);
  }
  
  
  public Attachment rmAttachment(int idx) {
    if(idx < 0 || idx >= attachs.size()
        || attachs.isEmpty())
      return null;
    
    return attachs.remove(idx);
  }
  
  
  public boolean rmAttachment(Attachment at) {
    if(at == null || attachs.isEmpty())
      return false;
    return attachs.remove(at);
  }
  
  
  public int attachmentsSize() {
    return attachs.size();
  }
  
  
  public List<Attachment> getAttachments() {
    return attachs;
  }
  
  
  public INotesHeaderMail getHeader() {
    return header;
  }
  
  
  public INotesMail setHeader(INotesHeaderMail hd) {
    this.header = hd;
    return this;
  }


  public String getFrom() {
    return from;
  }


  public INotesMail setFrom(String from) {
    this.from = from;
    return this;
  }


  public String[] getTo() {
    return to;
  }


  public INotesMail setTo(String ... to) {
    this.to = to;
    return this;
  }


  public String[] getCc() {
    return cc;
  }


  public INotesMail setCc(String ... cc) {
    this.cc = cc;
    return this;
  }


  public String[] getCco() {
    return cco;
  }


  public INotesMail setCco(String ... cco) {
    this.cco = cco;
    return this;
  }


  public String getSubject() {
    return subject;
  }


  public INotesMail setSubject(String subject) {
    this.subject = subject;
    return this;
  }


  public String getContent() {
    return content;
  }


  public INotesMail setContent(String content) {
    this.content = content;
    return this;
  }


  public String getxIbmCookie() {
    return xIbmCookie;
  }


  public INotesMail setxIbmCookie(String xIbmCookie) {
    this.xIbmCookie = xIbmCookie;
    return this;
  }


  public String getUid() {
    return uid;
  }


  public INotesMail setUid(String uid) {
    this.uid = uid;
    return this;
  }


  public Date getDate() {
    return date;
  }


  public INotesMail setDate(Date date) {
    this.date = date;
    return this;
  }
  
  
  private String toString(String[] array) {
    if(array == null || array.length < 1)
      return "";
    
    String s = "";
    for(int i = 0; i < array.length; i++) {
      s += array[i];
      if(i < array.length -1)
        s += ", ";
    }
    return s;
  }
  
  
  private static String getJsonContent(String json, String tag, String delim) {
    if(json == null || tag == null || delim == null)
      return null;
    
    int is = json.indexOf(tag) + tag.length();
    int i2 = json.indexOf(delim, is) + delim.length();
    if(i2 >= is) is = i2;
    int ie = json.indexOf(delim, is);
    return json.substring(is, ie);
  }
  
  
  private static int parseNumAttachments(String json) {
    if(json == null || json.trim().isEmpty())
      return -1;
    
    int is = json.indexOf(JSON_NUM_ATTACHMENTS);
    int ie = json.indexOf(JSON_ATTACHMENT_NAMES);
    if(is < 0 || ie < 0) return -1;
    
    String tmp = getJsonFromIndex(json, is, ie, JSON_OBJECT_DELIM, "\"");
    try { return Integer.parseInt(tmp); }
    catch(NumberFormatException e) {
      return -1;
    }
  }
  
  
  private static void parseAttachmentNames(String json, 
      int attachsSize, INotesMail mail) {
    
    if(json == null || json.isEmpty()
        || attachsSize < 1 || mail == null)
      return;
    
    int is = json.indexOf(JSON_ATTACHMENT_NAMES);
    int ie = json.indexOf(JSON_ATTACHMENT_NAMES_END, is);
    if(ie < 0) return;
    
    for(int i = 0; i < attachsSize; i++) {
      String tmp = getJsonFromIndex(json, is, ie, JSON_OBJECT_DELIM, "\"");
      if(tmp == null) continue;
      mail.attach(new Attachment().setName(tmp));
      is = json.indexOf(tmp, is);
      if(is < 0) break;
    }
  }
  
  
  private static void parseAttachmentLengths(String json, INotesMail mail) {
    if(json == null || json.isEmpty()
        || mail == null)
      return;
    
    int is = json.indexOf(JSON_ATTACHMENT_LENGTHS);
    int ie = json.indexOf(JSON_ATTACHMENT_LENGTHS_END, is);
    if(is < 0 || ie < 0) return;
    
    for(int i = 0; i < mail.attachmentsSize(); i++) {
      String tmp = getJsonFromIndex(json, is, ie, JSON_OBJECT_DELIM, "\"");
      try { mail.attachment(i).setLength(
          Long.parseLong(tmp)); }
      catch(NumberFormatException e) {};
      is = json.indexOf(tmp, is);
    }
  }
  
  
  private static void parseAttachments(String json, INotesMail mail) {
    if(json == null || json.trim().isEmpty()
        || mail == null)
      return;
    
    int numAttachs = parseNumAttachments(json);
    if(numAttachs < 1) return;
    parseAttachmentNames(json, numAttachs, mail);
    parseAttachmentLengths(json, mail);
  }
  
  
  private static String getJsonFromIndex(String json, 
      int start, int end, 
      String startDelim, String endDelim) {
    
    if(json == null || startDelim == null 
        || endDelim == null
        || start < 0 
        || start >= end
        || end < start 
        || end >= json.length())
      return null;
    
    String sub = json.substring(start, end);
    if(sub == null 
        || (!sub.contains(startDelim) 
        && !sub.contains(endDelim)))
      return null;
    
    int is = sub.indexOf(startDelim) + startDelim.length();
    int ie = sub.indexOf(endDelim, is);
    if(is < 0 || ie < 0) return null;
    return sub.substring(is, ie);
  }
  
  
  public static INotesMail parse(String json) {
    if(json == null || json.trim().isEmpty())
      return null;
    
    INotesMail mail = new INotesMail();
    String delim = "'";
    String ldelim = ", ";
    mail.setFrom(getJsonContent(json, JSON_FROM, delim));
    mail.setTo(getJsonContent(json, JSON_TO, delim).split(ldelim));
    mail.setCc(getJsonContent(json, JSON_CC, delim).split(ldelim));
    mail.setCco(getJsonContent(json, JSON_CCO, delim).split(ldelim));
    mail.setUid(getJsonContent(json, JSON_UID, delim));
    mail.setSubject(getJsonContent(json, JSON_SUBJECT, delim));
    mail.setContent(getJsonContent(json, JSON_CONTENT, delim));
    parseAttachments(json, mail);
    return mail;
  }
  
  
  public List<NameValuePair> toFormParams() {
    LinkedList<NameValuePair> list
        = new LinkedList<>();
    list.add(new BasicNameValuePair(ALT_PRINCIPAL, from));
    list.add(new BasicNameValuePair(X_IBM_COOKIE, xIbmCookie));
    list.add(new BasicNameValuePair(ALT_FROM, from));
    list.add(new BasicNameValuePair(CCO, toString(cco)));
    list.add(new BasicNameValuePair(BODY, content));
    //list.add(new BasicNameValuePair(BODY_IMG_CIDS, from));
    list.add(new BasicNameValuePair(CC, toString(cc)));
    list.add(new BasicNameValuePair(FROM, from));
    list.add(new BasicNameValuePair(PRINCIPAL, from));
    list.add(new BasicNameValuePair(TO, toString(to)));
    list.add(new BasicNameValuePair(SUBJECT, subject));
    list.add(new BasicNameValuePair(H_NAME, subject));
    
    for(int i = 0; i < MISC_KEYS.length; i++) {
      list.add(new BasicNameValuePair(MISC_KEYS[i], MISC_VALUES[i]));
    }
    
    return list;
  }
  
  
  @Override
  public String toString() {
    String s = "INotesMail{" + "\n  from=" + from 
        + ",\n  to=" + toString(to) 
        + ",\n  cc=" + toString(cc) 
        + ",\n  cco=" + toString(cco) 
        + ",\n  subject=" + subject 
        + ",\n  content=" + content 
        + ",\n  xIbmCookie=" + xIbmCookie 
        + ",\n  uid=" + uid;
    if(attachs.isEmpty())
      return s + "\n}";
    
    for(int i = 0; i < attachs.size(); i++) {
      s += ",\n  attachment-"+ i+ ": "+ attachs.get(i);
    }
    return s + "\n}";
  }

  
  public static void main(String[] args) {
    String json = "{\n" +
        "sUnid: 'E8843E1A98872DE683257BA60071DA9F', \n" +
        "fnItems: function(){var BodyHtml, StatusUpdateHtml, SendToHtml, CopyToHtml, BlindCopyToHtml, FromHtml, SubjectHtml, BodyParentHtml, HeaderParentHtml, FooterParentHtml, s_MailHistoryBodyVar, StatusUpdateParentHtml, SendToParentHtml, AltSendToParentHtml, CopyToParentHtml, AltCopyToParentHtml, BlindCopyToParentHtml, AltBlindCopyToParentHtml, x_NameLanguageTagsParentHtml, FromParentHtml, AltFromParentHtml, x_LangFromParentHtml, PrincipalParentHtml, x_AltPrincipalParentHtml, x_LangPrincipalParentHtml, SubjectParentHtml, PostedDateParentHtml, SealedParentHtml, fBodyParentSpecial, HeaderHtml, FooterHtml;var BqT, GaS,x_KeepPrivateHtml,x_KeepPrivateParentHtml;StatusUpdateHtml='';\n" +
        "SendToHtml='F4615930 James Radde/BancodoBrasil@BancodoBrasil';\n" +
        "CopyToHtml='F9882804 Wilson Jose Domingos/BancodoBrasil@BancodoBrasil, F6036477 Juno Dani da Rold Roesler/BancodoBrasil@BancodoBrasil';\n" +
        "BlindCopyToHtml='';\n" +
        "FromHtml='F7655526 Odair Ferreira de Sousa/BancodoBrasil';\n" +
        "SubjectHtml='Acompanhamento da Qualidade do Processo de Operações BNDES';\n" +
        "x_KeepPrivateHtml='0';\n" +
        "BodyHtml='<font face=\"Default Sans Serif,Verdana,Arial,Helvetica,sans-serif\" size=\"2\"\\> <span\\>James, segue conforme solicitado:<br\\><br\\>1 - Criação de parâmetros para montagem de base de dados das ocorrências de rejeições e devoluções do BNDES.<br\\>2 - Implementação de procedimentos para registro e acompanhamento operacional das ocorrências.<br\\>3 - Implementação de relatórios de acompanhamento gerencial no portal da Dinop.<br\\>4 - Avaliação periódica do desempenho dos centros na regularização das ocorrências.<br\\>5 - Diagnóstico de necessidades de treinamento com base nas carências identificadas.<br\\><br\\><font size=\"2\" face=\"Default Sans Serif,Verdana,Arial,Helvetica,sans-serif\"\\><div\\><font style=\"FONT-FAMILY: Default Sans Serif,Verdana,Arial,Helvetica,sans-serif\" size=\"1\"\\><span style=\"FONT-WEIGHT: bold\"\\><font color=\"#000000\"\\><br\\><br\\>ODAIR FERREIRA DE SOUSA</font\\></span\\><br style=\"FONT-WEIGHT: bold\"\\><span style=\"FONT-WEIGHT: bold; TEXT-DECORATION: underline\"\\><font color=\"#000000\"\\>Gerente de Divisão&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; </font\\></span\\><br style=\"FONT-WEIGHT: bold\"\\><span style=\"FONT-WEIGHT: bold\"\\><font color=\"#999999\"\\>DINOP - Diretoria de Apoio aos Negócios e Operações</font\\></span\\><br style=\"FONT-WEIGHT: bold\"\\><span style=\"FONT-WEIGHT: bold\"\\><font color=\"#999999\"\\>(61) 3108.1264 - (61) 9165.4342 - odair@bb.com.br</font\\></span\\></font\\><br\\><br\\></div\\></font\\></span\\></font\\>';\n" +
        "HeaderHtml='';\n" +
        "FooterHtml='';\n" +
        "     var sFormContentJson; var sHeaderHtml, sFooterHtml;\n" +
        " var sBodyEEXML, sBodyEEJSON; return{BodyHtml: BodyHtml, StatusUpdateHtml: StatusUpdateHtml, SendToHtml: SendToHtml, CopyToHtml: CopyToHtml, BlindCopyToHtml: BlindCopyToHtml, FromHtml: FromHtml, SubjectHtml: SubjectHtml, BodyParentHtml: s_MailHistoryBodyVar || BodyParentHtml, HeaderParentHtml: !s_MailHistoryBodyVar ? HeaderParentHtml : '', FooterParentHtml: !s_MailHistoryBodyVar ? FooterParentHtml : '', fBodyParentSpecial: fBodyParentSpecial, StatusUpdateParentHtml: StatusUpdateParentHtml, SendToParentHtml: SendToParentHtml, AltSendToParentHtml: AltSendToParentHtml, CopyToParentHtml: CopyToParentHtml, AltCopyToParentHtml: AltCopyToParentHtml, BlindCopyToParentHtml: BlindCopyToParentHtml, AltBlindCopyToParentHtml: AltBlindCopyToParentHtml, x_NameLanguageTagsParentHtml: x_NameLanguageTagsParentHtml, FromParentHtml: FromParentHtml, AltFromParentHtml: AltFromParentHtml, x_LangFromParentHtml: x_LangFromParentHtml, PrincipalParentHtml: PrincipalParentHtml, x_AltPrincipalParentHtml: x_AltPrincipalParentHtml, x_LangPrincipalParentHtml: x_LangPrincipalParentHtml, SubjectParentHtml: SubjectParentHtml, SealedParentHtml: SealedParentHtml, BqT: BqT, GaS: GaS, x_KeepPrivateHtml:x_KeepPrivateHtml, x_KeepPrivateParentHtml:x_KeepPrivateParentHtml, GRR: [], BodyEEXML: sBodyEEXML ,BodyEEJSON: sBodyEEJSON ,FormContentJson : sFormContentJson ,HeaderHtml: sHeaderHtml ,FooterHtml: sFooterHtml};}, /*  */ DXX: ({\n" +
        "\"item\": [\n" +
        "{\n" +
        "\"@name\": \"RR2\",\n" +
        "\"textlist\": {\n" +
        "\"text\": [\n" +
        "{\n" +
        "\"0\": \"\"\n" +
        "}" +
        "\"@name\": \"h_NumAttachments\",\n" +
        "\"numberlist\": {\n" +
        "\"number\": [\n" +
        "{\n" +
        "\"0\": \"3\"\n" +
        "}\n" +
        "]\n" +
        "}\n" +
        "},\n" +
        "{\"@name\": \"h_AttachmentNames\",\n" +
        "\"textlist\": {\n" +
        "\"text\": [\n" +
        "{\n" +
        "\"0\": \"Produtividade.pdf\"\n" +
        "},\n" +
        "{\n" +
        "\"0\": \"Produtividade.xlsx\"\n" +
        "},\n" +
        "{\n" +
        "\"0\": \"gsv_Calc_Prod.txt\"\n" +
        "}\n" +
        "]\n" +
        "}\n" +
        "},\n" +
        "{\n" +
        "\"@name\": \"h_AttachmentNamesAlt\",\n" +
        "\"text\": {\n" +
        "\"0\": \"QPNULL\"\n" +
        "}\n" +
        "},\n" +
        "{\n" +
        "\"@name\": \"h_AttachmentLengths\",\n" +
        "\"numberlist\": {\n" +
        "\"number\": [\n" +
        "{\n" +
        "\"0\": \"41067\"\n" +
        "},\n" +
        "{\n" +
        "\"0\": \"960859\"\n" +
        "},\n" +
        "{\n" +
        "\"0\": \"3420\"\n" +
        "}\n" +
        "]\n" +
        "}\n" +
        "},\n" +
        "{\n" +
        "\"@name\": \"h_AttachmentLengthsAlt\",";
    
    System.out.println(INotesMail.parse(json));
  }
  
}
