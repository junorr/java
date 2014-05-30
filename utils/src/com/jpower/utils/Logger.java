package com.jpower.utils;

import java.util.*;
import java.io.*;

/**
 * Classe para criar e manipular arquivos de log.
 * Possui a estrutura:<br>
 * <code>Logger - version</code><br>
 * <code>--------------------</code><br>
 * <code>DATE = {@literal <dd.mm.aaaa>}</code><br>
 * <code>--------------------</code><br>
 * <code># comment text line</code><br>
 * <code>[hh:mm:ss.lll] - text log</code><br>
 * Comentarios sao adicionados atraves do metodo
 * <code>addComment(String)</code>.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.1.6 - build#1  2009-02-27
 */
public class Logger {

  private static String version = "Logger 0.1.6 - build#1  2009-02-27";

  private FileWriter writer;

  private Calendar cal;

  private File logfile;

  private boolean screenlog = false;

  private String Date;

  /**
   * Tipo a ser retornado pelo metodo
   * <code>getTime(int)</code>.
   */
  public static int DATE = 0, TIME = 1;

  /**
   * Construtor recebe como parametros um File,
   * indicando o arquivo onde sera gerado o log,
   * e uma String de cabecalho como comentario.
   * @param logfile File onde sera gerado o log.
   * @throws IOException caso ocorra erro no
   * processamento do arquivo.
   */
  public Logger(File logfile)
  throws IOException {
    if(logfile == null)
      throw new IOException("\n#Log: Arquivo de log inexistente.#");
    Date = getTime(DATE);
    this.logfile = logfile;
    String readc = null;
    if(!logfile.exists())
      logfile.createNewFile();
    else
      readc = readContent();
    writer = new FileWriter(logfile);
    prepare(readc);
  }//construtor 1

  /**
   * Prepara o cabecalho do arquivo de log.
   * @throws IOException caso ocorra erro na gravacao.
   */
  private void prepare(String readc) throws IOException {
    if(readc != null) {
      //System.out.println(readContent());
      writer.write(readc);
      int idate = readc.lastIndexOf("DATE = <");
      if(idate > 0) {
        idate += 7;
        Date = readc.substring(idate, idate+12);
        //System.out.println(Date);
      }//if
    } else {
      writer.write(version+ "\n");
      this.setDate(getTime(DATE));
    }//if-else
  }//prepare()

  /**
   * Fecha o acesso ao arquivo de log.
   * deve ser chamado no termino da utilizacao
   * de Logger.
   * @throws IOException caso ocorra erro na manipulacao do arquivo.
   */
  public void close()
  throws IOException {
    writer.flush();
    writer.close();
  }//close()

  /**
   * Forca a escrita dos dados
   * no arquivo de log.
   * @return <code>true</code> se nao ocorrer erros de escrita, <code>false</code> caso contrario.
   */
  public boolean flush() {
    try {
      writer.flush();
      return true;
    } catch(Exception ex) {
      return false;
    }//try-catch
  }//flush

  /**
   * Adiciona um registro ao arquivo de log.
   * @param sLog String a ser registrada.
   * @return boolean indicando o sucesso da operacao.
   */
  public boolean add(String sLog) {
    if(sLog == null) return false;
    try {
      if(!getTime(DATE).substring(1, 3).equals(
        Date.substring(1, 3))) {
        Date = getTime(DATE);
        setDate(Date);
      }//if

      if(screenLogEnable())
        System.out.print(getTime(TIME)+ " - ");

      writer.write(getTime(TIME)+ " - ");

      if(screenLogEnable())
        System.out.println(sLog);

      writer.write(sLog);
      writer.write("\n");
      return true;
    } catch(Exception ex) {
      return false;
    }//try-catch
  }//add()

  /**
   * Adiciona um comentario ao arquivo de log.
   * @param comment Comentario a ser registrado.
   * @return <code>boolean</code> indicando o sucesso
   * da operacao.
   */
  public boolean addComment(String comment) {
    try {
      if(!getTime(DATE).substring(1, 3).equals(
        Date.substring(1, 3))) {
        Date = getTime(DATE);
        setDate(Date);
      }//if
      String towrite = "# "+ comment;
      towrite = towrite.replaceAll("\n", "\n# ");
      towrite += "\n";
      writer.write(towrite);
      if(screenLogEnable())
        System.out.print(towrite);
      return true;
    } catch(Exception ex) {
      return false;
    }//try-catch
  }//addComment()

  /**
   * Limpa o arquivo de log e o buffer.
   * @throws IOException caso ocorra erro na
   * manipulacao do arquivo.
   */
  public void clear()
  throws IOException {
    logfile.delete();
    logfile.createNewFile();
    writer = new FileWriter(logfile);
    prepare(null);
  }//clear()

  /**
   * Retorna uma String formatada contendo a
   * data ou a hora atual, conforme o parametro
   * informado, que pode ser
   * <code>Logger.DATE ou Logger.TIME</code>.
   * Formato:</br>
   * DATE: <code><dd.mm.aaaa></code></br>
   * TIME: <code>[hh:mm:ss.lll]</code></br>
   * @param type DATE ou TIME.
   * @return String contendo o tempo atual.
   */
  public String getTime(int type) {
    cal = Calendar.getInstance();
    String time;
    if(type == DATE) {
      time = "<";
      int d = cal.get(cal.DAY_OF_MONTH);
      int m = cal.get(cal.MONTH)+1;
      int y = cal.get(cal.YEAR);
      String z = "";
      if(d < 10) z = "0";
      time += z+d+".";
      z = "";
      if(m < 10) z = "0";
      time += z+m+".";
      time += y+">";
    } else if(type == TIME) {
      String z = "";
      time = "[";
      int h = cal.get(cal.HOUR_OF_DAY);
      int n = cal.get(cal.MINUTE);
      int s = cal.get(cal.SECOND);
      int l = cal.get(cal.MILLISECOND);
      if(h < 10) z = "0";
      time += z+h+":";
      z = "";
      if(n < 10) z = "0";
      time += z+n+":";
      z = "";
      if(s < 10) z = "0";
      time += z+s+".";
      if(l < 10) z = "00"+l;
      else if(l < 100) z = "0"+l;
      else z = ""+l;
      time += z+"]";
    } else
      time = "null";

    return time;
  }//getTime()

  /**
   * Le o arquivo de log e retorna seu conteudo
   * @return Conteudo do arquivo de log.
   */
  private String readContent() {
    try {
      FileReader r = new FileReader(logfile);
      char c;
      int i;
      StringBuffer buff = new StringBuffer();
      while( (i = r.read()) != -1) {
        c = (char) i;
        buff.append(c);
      }//while
      return buff.toString();
    } catch(IOException io) {
      return null;
    }
  }//readContent()


  /**
   * Imprime o conteudo de Log no
   * PrintStream indicado.
   * Equivalente ao Properties.list().
   * @param ps PrintStream onde sera listado.
   * @throws IOException caso ocorra erro lendo o arquivo de log.
   */
  public void print(PrintStream ps) {
    ps.println(readContent());
  }//list()

  /**
   * Retorna um booleano indicando se a funcao
   * ScreenLog esta habilitada.
   * @return True: Funcao habilitada; False: Funcao desabilitada.
   */
  public boolean screenLogEnable() {
    return screenlog;
  }//screenLogEnable()

  /**
   * Seta o arquivo de log.
   * @param log Arquivo de log.
   * @throws IOException caso ocorra erro na manipulacao do arquivo
   */
  public void setFile(File log)
  throws IOException {
    logfile = log;
    String readc = null;
    if(logfile.exists())
      readc = readContent();
    writer = new FileWriter(log);
    prepare(readc);
  }//setFile()

  /**
   * Adiciona uma data ao arquivo de log.
   * @param date Data a ser gravada.
   * @throws IOException caso ocorra erro na gravacao.
   */
  public void setDate(String date)
  throws IOException {
    writer.write("\n");
    writer.write("--------------------\n");
    writer.write("DATE = "+ getTime(DATE)+ "\n");
    writer.write("--------------------\n");
  }//setDate()

  /**
   * Habilita/Desabilita a funcao ScreenLog.
   * Esta funcao inicia Desabilitada por padrao.
   * Quando habilitada, todas as ocorrencias
   * adicionadas sao automaticamente impressas
   * na tela. O arquivo de log continua recebendo
   * as ocorrencias normalmente.
   * @param enable Habilita ou desbilita a funcao.
   */
  public void setScreenLog(boolean enable) {
    screenlog = enable;
  }

}
