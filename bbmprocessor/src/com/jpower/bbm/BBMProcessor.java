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

package com.jpower.bbm;

import com.jpower.date.SimpleDate;
import java.io.File;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/04/2013
 */
public class BBMProcessor {
  
  public static final SimpleDate 
      
      DEFAULT_START_DATE = 
        SimpleDate.parseDate("01/03/2012"),
      
      DEFAULT_END_DATE = 
        SimpleDate.parseDate("01/03/2013");
  
  public static final String
      
      PREF_ANALISES = "analises",
      
      PREF_CONSOLIDADO = PREF_ANALISES 
        + "_consolidado_",
      
      CSV_EXT = ".csv";
      
  
  private BBMReader reader;
  
  private PrefixPool prefpool;
  
  private BBMParser parser;
  
  private BBMLog log;
  
  private BBMProgress progress;
  
  private DBProduto prodb;
  
  private WriterPool wpool;
  
  private ProdutoCountPool pcpool;
  
  private ProdutoCountWriter pcwriter;
  
  private String line;
  
  private File bbm, prodcsv, logfile, progfile;
  
  private SimpleDate startDate, endDate;
  
  private ProgressListener plistener;
  
  private boolean running;
  
  private boolean incprefix;
  
  
  public BBMProcessor() {
    prodcsv = null;
    this.bbm = null;
    prefpool = new PrefixPool();
    parser = new BBMParser();
    pcpool = new ProdutoCountPool();
    logfile = new File("./bbm.log");
    progfile = new File("./progress.properties");
    startDate = DEFAULT_START_DATE;
    endDate = DEFAULT_END_DATE;  
    running = false;
    incprefix = false;
  }
  
  
  public BBMProcessor(File bbm, File produtosCsv) {
    if(bbm == null || !bbm.exists())
      throw new IllegalArgumentException(
          "Invalid BBM File: "+ bbm);
    
    if(produtosCsv == null || !produtosCsv.exists())
      throw new IllegalArgumentException(
          "Invalid CSV File: "+ produtosCsv);
    
    prodcsv = produtosCsv;
    this.bbm = bbm;
    prefpool = new PrefixPool();
    pcpool = new ProdutoCountPool();
    logfile = new File("./bbm.log");
    progfile = new File("./bbmprogress.properties");
    startDate = DEFAULT_START_DATE;
    endDate = DEFAULT_END_DATE;
    running = false;
    this.init();
  }
  
  
  private void init() {
    reader = new BBMReader(bbm);
    parser = new BBMParser();
    log = new BBMLog(logfile);
    progress = new BBMProgress(progfile);
    prodb = new DBProduto(prodcsv).readFile();
    
    wpool = new WriterPool(bbm.getParentFile(), PREF_ANALISES);
    File f = new File(bbm.getParentFile(), PREF_CONSOLIDADO
        + String.valueOf(startDate.month()) + "_"
        + String.valueOf(startDate.year()) + "-"
        + String.valueOf(endDate.year())
        + CSV_EXT);
    if(f.exists() || f.isFile())
      this.pcpool.load(f);
    pcwriter = new ProdutoCountWriter(f);
    
    if(progfile.exists())
      progress.load();
    if(progress.getBytes() > 0)
      reader.setInc(progress.getBytes());
  }
  
  
  public ProdutoCountPool getProdutoCountPool() {
    return pcpool;
  }
  
  
  public BBMProgress getProgress() {
    return progress;
  }
  
  
  public BBMProcessor setProgressListener(ProgressListener pl) {
    this.plistener = pl;
    return this;
  }
  
  
  private void setProgress() {
    if(progress.getStringPercent() == null || !progress.getStringPercent().equals(reader.getStringPercent())) {
      String s = "Progress: "+ reader.getStringPercent();
      System.out.println("* "+s);
      log.log(s);
      if(plistener != null)
        plistener.setProgress(progress);
    }
    progress.setProgress(reader.getInc(), reader.getPercent(), reader.getStringPercent())
        .save();
  }


  public File getBbmFile() {
    return bbm;
  }


  public BBMProcessor setBbmFile(File bbm) {
    this.bbm = bbm;
    reader = new BBMReader(bbm);
    wpool = new WriterPool(bbm.getParentFile(), PREF_ANALISES);
    File f = new File(bbm.getParentFile(), PREF_CONSOLIDADO
        + String.valueOf(startDate.month()) + "_"
        + String.valueOf(startDate.year()) + "-"
        + String.valueOf(endDate.year())
        + CSV_EXT);
    if(f.exists() || f.isFile()) {
      this.pcpool.load(f);
    }
    pcwriter = new ProdutoCountWriter(f);
    
    return this;
  }


  public File getProdutosFile() {
    return prodcsv;
  }


  public BBMProcessor setProdutosFile(File prodcsv) {
    this.prodcsv = prodcsv;
    System.out.println("* setProdutosFile: "+ prodcsv);
    prodb = new DBProduto(prodcsv).readFile();
    return this;
  }
  
  
  public DBProduto getDBProduto() {
    return prodb;
  }


  public File getLogFile() {
    return logfile;
  }


  public BBMProcessor setLogFile(File logfile) {
    this.logfile = logfile;
    log = new BBMLog(logfile);
    return this;
  }


  public File getProgressFile() {
    return progfile;
  }


  public BBMProcessor setProgressFile(File progfile) {
    this.progfile = progfile;
    progress = new BBMProgress(progfile);
    if(progfile.exists())
      progress.load();
    if(reader != null && progress.getBytes() > 0)
      reader.setInc(progress.getBytes());
    return this;
  }


  public SimpleDate getStartDate() {
    return startDate;
  }


  public BBMProcessor setStartDate(SimpleDate startDate) {
    this.startDate = startDate;
    return this;
  }


  public SimpleDate getEndDate() {
    return endDate;
  }


  public BBMProcessor setEndDate(SimpleDate endDate) {
    this.endDate = endDate;
    return this;
  }


  public BBMProcessor setPrefixFile(File prefile, boolean include) {
    if(prefile != null && prefile.exists() 
        && prefile.isFile()) {
      prefpool.setFile(prefile).load();
      incprefix = include;
    }
    return this;
  }
  
  
  public boolean setStringStartDate(String start) {
    try {
      startDate = SimpleDate.parseDate(start);
      return true;
    } catch(IllegalArgumentException e) {
      return false;
    }
  }
  
  
  public boolean setStringEndDate(String end) {
    try {
      endDate = SimpleDate.parseDate(end);
      return true;
    } catch(IllegalArgumentException e) {
      return false;
    }
  }
  
  
  public boolean isRunning() {
    return running;
  }
  
  
  public BBMProcessor stop() {
    running = false;
    return this;
  }
  
  
  public BBMProcessor start() {
    running = true;
    line = reader.read();
    while(line != null && running) {
      Analise a = parser.parse(line);
    
      if(a != null) {
        Produto p = prodb.find(a.getProduto());
        if(p != null)
          a.setProduto(p);
        else {
          String s = "[WARN] Cant find Produto: "
              + a.getProduto().getPrmod();
          log.log(s);
          System.out.println("* "+ s);
          s = "{pref="+ a.getPrefixo()+ ", oper="+ a.getOperacao()+ "}";
          log.log(s);
          System.out.println("  "+ s);
        }
        
        boolean include = false;
        if(prefpool.isEmpty()) include = true;
        else
          include = (prefpool.contains(a.getPrefixo()) && incprefix)
              || (!prefpool.contains(a.getPrefixo()) && !incprefix);
        
        if(!include) System.out.println("* Not Include: "+ a.getPrefixo());
        
        if(include && isInDatePeriod(a)) {
          AnaliseWriter aw = wpool.getWriter(a.getData());
          aw.write(a);
          pcpool.inc(a);
        } 
      }
    
      this.setProgress();
      line = reader.read();
    }
    
    if(!pcpool.isEmpty()) {
      pcpool.write(pcwriter);
      pcwriter.close();
    }
    
    String s = "FINISHED!";
    System.out.println("* "+ s);
    log.log(s);
    log.close();
    wpool.close();
    reader.close();
    return this;
  }
  
  
  private boolean isInDatePeriod(Analise a) {
    if(a == null || a.getData() == null)
      return false;
    return a.getData().isBetween(startDate, endDate);
  }

  
  public static void main(String[] args) {
    BBMProcessor proc = new BBMProcessor()
        .setBbmFile(new File("D:/baixa/BBM.COPF560A.O9910.D9953.D130411.SS000121.TXT"))
        .setProdutosFile(new File("D:/baixa/produtos.csv"))
        .setLogFile(new File("d:/baixa/bbm.log"))
        .setProgressFile(new File("d:/baixa/bbmprogress.properties"))
        .start();
  }
  
}
