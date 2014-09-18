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

package com.pserver.isys;

import com.jpower.inet.FileAttachment;
import com.jpower.inet.INotesHeaderMail;
import com.jpower.inet.INotesMail;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptFileCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptStringCoder;
import us.pserver.date.SimpleDate;
import us.pserver.scron.Job;
import us.pserver.scron.Schedule;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 14/08/2013
 */
public class EmailParser {
  
  private static final CryptKey KEY = 
      new CryptKey("4c036dad7048d8d7d9fa1c42964c54ba5c676a2f53ba9ee9e18d909a997849f1",
      CryptAlgorithm.DESede_CBC_PKCS5);
  
  public static final String 
      MAIL_FROM_STR = "f6036477@bb.com.br",
      
      ENCODED_FILE_EXTENTION = ".bce",
      
      LOG_FILE_EXTENTION = ".log",
      
      TEMP_FILE_NAME = "scp_",
      
      KEY_LOGGER = "us.pserver.log.Log",
      
      KEY_SCRONV6 = "SCronV6",
      
      KEY_EMAILPARSER = "com.pserver.isys.EmailParser",
      
      DEFAULT_TEMP_DIR_NAME = "EmailCheckerTemp";
  
  
  private EmailChecker checker;
  
  private CryptStringCoder scoder;
  
  private CryptFileCoder fcoder;
  
  
  public EmailParser(EmailChecker checker) {
    if(checker == null)
      throw new IllegalArgumentException(
          "Invalid EmailChecker: "+ checker);
    this.checker = checker;
    checker.getSCronV6().dataMap()
        .put(KEY_LOGGER, checker.getSLogV2());
    checker.getSCronV6().dataMap()
        .put(KEY_EMAILPARSER, this);
    scoder = new CryptStringCoder(KEY);
    fcoder = new CryptFileCoder(KEY);
  }
  
  
  private String getContentCode(INotesMail mail) {
    if(mail == null || mail.getContent() == null)
      return null;
    
    checker.getSLogV2().info("Getting content tag: "+ mail.getSubject());
    checker.getSLogV2().info(mail.getContent());
    int itag = mail.getContent().indexOf(EmailDefinition.CONTENT_TAG.getTag())
        + EmailDefinition.CONTENT_TAG.getTag().length();
    int iend = mail.getContent().indexOf(EmailDefinition
        .CONTENT_TAG.getTag(), itag);
    if(itag < 0 || iend < 0) return null;
    return mail.getContent().substring(itag, iend);
  }
  
  
  private SimpleDate getTaggedSchedule(INotesMail mail) {
    if(mail == null || mail.getContent() == null
        || mail.getContent().trim().isEmpty())
      return null;
    
    int itag = mail.getContent().indexOf(
        EmailDefinition.CONTENT_SCHEDULE_TAG.getTag())
        + EmailDefinition.CONTENT_SCHEDULE_TAG.getTag().length();
    int iend = mail.getContent().indexOf(EmailDefinition
        .CONTENT_SCHEDULE_TAG.getTag(), itag);
    if(itag < 0 || iend < 0) return null;
    
    return SimpleDate.parseDate(scoder.decode(
        mail.getContent().substring(itag, iend)));
  }
  
  
  private Schedule getSchedule(INotesMail mail) {
    if(mail == null) return null;
    
    SimpleDate date = this.getTaggedSchedule(mail);
    Schedule sched = new Schedule()
        .startNow()
        .disableRepeat();
    if(date != null) {
      sched.startAt(date);
      checker.getSLogV2().info("Scheduled to "+ date);
    }
    return sched;
  }
  
  
  public Path encode(Path file) {
    try {
      Path tmp = Files.createTempFile(checker.getTempDir(), 
          file.getFileName().toString(), 
          ENCODED_FILE_EXTENTION);
      fcoder.encode(file, tmp);
      return tmp;
    } catch(IOException ex) {
      checker.getSLogV2().error(ex.toString());
      return null;
    }
  }
    
    
  public Path encode(String content) {
    if(content == null || content.trim().isEmpty())
      return null;
    
    try {
      Path tmp = Files.createTempFile(checker.getTempDir(), 
          TEMP_FILE_NAME, ENCODED_FILE_EXTENTION);
      if(!Files.exists(tmp))
        Files.createFile(tmp);
      
      StringByteConverter scon = new StringByteConverter();
      ByteBufferConverter bcon = new ByteBufferConverter();
      
      byte[] bs = scon.convert(content);
      ByteBuffer buf = bcon.reverse(bs);
      fcoder.applyFrom(buf, tmp, true);
      return tmp;
    } catch(IOException ex) {
      checker.getSLogV2().error(ex.toString());
      return null;
    }
  }
    
    
  public void sendResponse(INotesMail orig, Path attachment) {
    INotesMail re = new INotesMail();
    re.setFrom(MAIL_FROM_STR)
        .setTo(orig.getFrom())
        .setSubject("Re: "+ orig.getSubject())
        .setContent(orig.getContent());
    if(attachment != null && Files.exists(attachment))
      re.attach(new FileAttachment(attachment.toString()));
    
    checker.getINotesConnector().sendMail(re);
    checker.getSLogV2().info("Response e-mail sent to: "
        + re.getTo()[0]);
  }
  
  
  public Path createTempFile(String name, String ext) {
    Path tmp = null;
    try {
      tmp = Files.createTempFile(checker.getTempDir(), name, ext);
      if(!Files.exists(tmp))
        Files.createFile(tmp);
    } catch(IOException ex) {}
    return tmp;
  }
  
  
  public Path decode(Path path) {
    if(path == null || !Files.exists(path))
      return path;
    Path dec = createTempFile(
        path.getFileName().toString(), null);
    
    fcoder.apply(path, dec, false);
    return dec;
  }
   
    
  private void execScript(String code, Schedule sched, INotesMail mail) {
    if(mail == null || sched == null) return;
    
    Path orig = null;
    if(mail.attachmentsSize() > 0) {
      orig = Paths.get(mail.firstAttachment().getPath());
    }
    else if(code != null) {
      orig = Paths.get(EmailChecker.SCRIPT_DIR_NAME + code);
    }
    
    if(orig == null || !Files.exists(orig)) {
      checker.getSLogV2().error("Script execution abborted. Not found: "+ orig);
      return;
    }
    
    checker.getSCronV6().put(
        sched, new ScriptJob(
        this.decode(orig), mail));
  }
  
  
  private void execSystemCommand(String code, Schedule sched, INotesMail mail) {
    if(code == null || sched == null)
      return;
    
    Job job = null;
    if(code.equalsIgnoreCase(EmailDefinition
        .CONTENT_BLOCK_SCREEN.getTag())) {
      job = new BlockScreenJob();
    }
    else if(code.equalsIgnoreCase(EmailDefinition
        .CONTENT_LOGOFF.getTag())) {
      job = new LogoffJob();
    }
    else if(code.equalsIgnoreCase(EmailDefinition
        .CONTENT_REBOOT.getTag())) {
      job = new RebootJob();
    }
    else if(code.equalsIgnoreCase(EmailDefinition
        .CONTENT_SHUTDOWN.getTag())) {
      job = new ShutdownJob();
    }
    else if(code.equalsIgnoreCase(EmailDefinition
        .CONTENT_WTN.getTag())) {
      job = new WTNJob();
    }
    else if(code.startsWith(EmailDefinition
        .CONTENT_CMD.getTag())) {
      job = new CustomCommandJob(code, mail);
    }
    else {
      checker.getSLogV2().warning("Invalid code <"+ code+ ">");
    }
    
    if(job != null) {
      checker.getSCronV6().put(sched, job);
    }
  }

  
  public void parse(INotesHeaderMail hmail) {
    INotesMail mail = checker
        .getINotesConnector().getMail(hmail);
    if(mail == null) return;
    
    checker.getSLogV2().info("Deleting e-mail: "+ hmail.getSubject());
    checker.getINotesConnector().deleteMail(hmail);
    
    String content = this.getContentCode(mail);
    String code = null;
    
    if(content == null) {
      checker.getSLogV2().warning("Empty Content e-mail: "+ mail.getSubject());
    } else {
      code = scoder.decode(content);
      checker.getSLogV2().info("Code found: <"+ code+ ">");
    }
    
    Schedule sched = this.getSchedule(mail);
    
    if(mail.getSubject().equals(EmailDefinition
        .TITLE_EXEC_SYSTEM_COMMAND.getTag()))
      this.execSystemCommand(code, sched, mail);
    
    else if(mail.getSubject().equals(EmailDefinition
        .TITLE_EXEC_DEFINED_SCRIPT.getTag())
        || mail.getSubject().equals(EmailDefinition
        .TITLE_EXEC_ATTACHED_SCRIPT.getTag()))
      this.execScript(code, sched, mail);
  }
  
}
