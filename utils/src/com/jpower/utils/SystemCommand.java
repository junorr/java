/*
  Direitos Autorais Reservados (c) 2009 Juno Roesler
  Contato com o autor: powernet.de@gmail.com

  Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
  termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
  Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
  versão posterior.

  Esta biblioteca é distribuído na expectativa de que seja útil, porém, SEM
  NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
  OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
  Geral Menor do GNU para mais detalhes.

  Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
  com esta biblioteca; se não, escreva para a Free Software Foundation, Inc., no
  endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
*/
package com.jpower.utils;

import java.io.*;
import java.util.Scanner;

/**
 * Classe que implementa a interface <code>Task</code>,
 * representa uma tarefa que executa um comando valido
 * do sistema operacional via <code>Runtime.exec(...):Process</code>.
 * @author Juno Roesler - powernet.de@gmail.com
 * @see <a href="http://java.sun.com/javase/6/docs/api/java/lang/Process.html"> java.lang.Process </a>
 */
public class SystemCommand implements Runnable {

  private static final long serialVersionUID = 27L;

  /**
   * Comando a ser executado.
   */
  private String command;

  /**
   * Argumentos do comando.
   */
  private String[] args;

  /**
   * Retorno do sistema.
   */
  private StringBuffer output;

  /**
   * Retorno de erro do sistema.
   */
  private StringBuffer errorOutput;

  /**
   * Indica se a tarefa esta sendo executada.
   */
  private boolean running;

  /**
   * <code>boolean</code> que indica se a tarefa
   * deve travar ateh o final da execucao do
   * processo no sistema.
   */
  private boolean wait;

  /**
   * Código de saída do processo.
   */
  private int exitcode;

  private String cmdline;

  private final Object lock = new Object();

  private boolean flushFinished = false;


  /**
   * Construtor default sem argumentos, representa
   * uma tarefa vazia que nao podera ser executada
   * antes da configuracao adequada atraves do
   * metodo <code>set(String, String ...)</code>.
   */
  public SystemCommand() {
    command = "";
    args = new String[1];
    args[0] = "";
    output = new StringBuffer();
    errorOutput = new StringBuffer();
    running = false;
    wait = true;
    exitcode = -1;
    cmdline = null;
  }//SystemTask()

  /**
   * Construtor que recebe uma <code>String</code> contendo
   * o nome da tarefa e uma lista de <code>String's</code>
   * com o comando e os argumentos da execucao, caso
   * existentes.
   * @param command Comando a ser executado no sistema.
   * @param args Argumentos do comando, caso existentes.
   */
  public SystemCommand(String command, String ... args) {
    this();
    this.command = command;
    this.args = args;
  }//SystemTask()

  /**
   * Retorna os argumentos do comando a
   * ser executado.
   * @return <code>String[]</code> contendo os
   * argumentos do comando a ser executado.
   */
  public String[] getArgs() {
    return args;
  }//getArgs()

  /**
   * Retorna o comando a ser executado.
   * @return <code>String</code> com o
   * comando a ser executado.
   */
  public String getCommand() {
    return command;
  }//getCommand()

  /**
   * Retorna a saida gerada pelo sistema para
   * comando.
   * @return <code>String</code> com a saida
   * gerada pelo sistema ao comando, caso
   * capturada com sucesso.
   */
  public String getOutput() {
    if(output != null)
      return output.toString();
    return null;
  }//getSaida()

  
  public String getCommandLine()
  {
    return cmdline;
  }

  
  /**
   * Retorna a saida de erro gerada pelo sistema para
   * comando.
   * @return <code>String</code> com a saida de erro
   * gerada pelo sistema ao comando, caso
   * capturada com sucesso.
   */
  public String getErrorOutput() {
    if(errorOutput != null)
      return errorOutput.toString();
    return null;
  }//getSaida()

  /**
   * Retorna o código de saída da execução
   * do sistema.
   * @return Código de retorno do sistema.
   */
  public int getExitCode() {
    return exitcode;
  }//method()

  /**
   * Indica se a tarefa esta rodando no
   * instante da chamada do metodo.
   * @return <code>boolean</code> indicando se
   * a tarefa encontra-se rodando.
   */
  public boolean isRunning() {
    return running;
  }//isRunning()


  /**
   * Seta os argumentos do comando a ser
   * executado.
   * @param args <code>String[]</code> contendo os
   * argumentos do comando a ser executado.
   */
  public void setArgs(String ... args) {
    this.args = args;
  }//setArgs()

  /**
   * Seta o comando a ser executado pelo sistema
   * @param command Comando a ser executado pelo
   * sistema.
   */
  public void setCommand(String command) {
    this.command = command;
  }//setCommand()

  /**
   * Metodo sobrescrito da interface
   * <code>Runnable</code>, que executa
   * a tarefa configurada. A saida do
   * sistema gerada para o comando, poderah
   * ser obtida atraves do metodo
   * <code>getSaida():String</code>, caso
   * seja capturada com sucesso.
   */
  public void run() {
    if(command == null) return;
    try {

      running = true;

      String temp = command;
      for(int y = 0; y < args.length; y++) {
        temp += " "+ args[y];
      }//for

      cmdline = temp;

      Process proc = Runtime.getRuntime().exec(temp);

      if(wait) {
        //Captura a saída do processo
        FlushStream f = new FlushStream(
          proc.getInputStream());

        f.setStringBuffer(output);

        f.flush();

        //Captura a saída de erros do processo
        FlushStream fe = new FlushStream(
          proc.getErrorStream());

        fe.setStringBuffer(errorOutput);

        fe.flush();

        //Aguarda o fim do processo
        proc.waitFor();

        if(!flushFinished)
          synchronized(lock) {
            lock.wait();
          }

        exitcode = proc.exitValue();
      }//if

      running = false;
    } catch(Exception e){
      e.printStackTrace();
      exitcode = 9;
      running = false;
    }
  }//run()

  /**
   * Retorna <code>SystemTask</code> representada
   * em uma <code>String</code>, contendo as
   * configuracoes.
   * @return <code>String</code> representando
   * <code>SystemTask</code>.
   */
  @Override
  public String toString() {
    String s =
      "   SystemCommand\n"+
      "    Command  : "+ command+ "\n";
    String ag = "";
    for(int i = 0; i < args.length; i++) {
      ag += args[i]+ " ";
    }//for

    s +=
      "    Arguments: "+ ag+ "\n"+
      "    Running  : "+ running;
    return s;
  }//toString()

  /**
   * Seta se a tarefa deve ou nao travar ate
   * o final do Processo em execucao no sistema
   * (<code>default: FALSE</code>).
   * @param wait <code>boolean</code> indicando
   * se a tarefa irah esperar ateh o final da
   * execucao do processo no sistema.
   */
  public void waitProcessEnd(boolean wait) {
    this.wait = wait;
  }//waitEndProcess()

  /**
   * Retorna um <code>boolean</code> indicando se
   * a tarefa ira esperar ou nao, pelo final da execucao
   * do processo no sistema.
   * @return <code>boolean</code> indicando se a tarefa
   * irah travar ateh o final da execucao do processo.
   */
  public boolean waitProcessEnd() {
    return wait;
  }//waitEndProcess()


  public static void main(String[] args)
  {
    SystemCommand sc = new SystemCommand();
    sc.waitProcessEnd(true);

    Scanner scan = new Scanner(System.in);

    System.out.print("Command: ");
    sc.setCommand(
        scan.nextLine());
    System.out.println();

    System.out.print("Args...: ");
    sc.setArgs(
        scan.nextLine());
    System.out.println();

    System.out.println("sc.run()");
    sc.run();
    System.out.println(sc.getCommandLine());
    System.out.println("Exit code: "+ sc.getExitCode());
    System.out.println("-- Output --");
    System.out.println(sc.getOutput());
    System.out.println("-- Error Output --");
    System.out.println(sc.getErrorOutput());
  }



  class FlushStream implements Runnable {

    private InputStream in;

    private StringBuffer out;


    public FlushStream()
    {
      in = null;
      out = new StringBuffer();
    }

    public FlushStream(InputStream input) {
      out = new StringBuffer();
      in = input;
    }//method()

    public void setStream(InputStream input)
    {
      in = input;
    }

    public void setStringBuffer(StringBuffer buf)
    {
      if(buf == null) return;
      out = buf;
    }

    public StringBuffer getOutput() {
      return out;
    }//method()

    public void flush() {
      if(in == null) return;
      Thread th = new Thread(this);
      th.start();
    }//method()

    public void run() {
      try {

        int c = -1;
        while( (c = in.read()) > 0 )
          out.append( (char) c);

      } catch(IOException ex) {
        out.append(ex.getMessage());
      } finally {
        flushFinished = true;
        synchronized(lock) {
          lock.notifyAll();
        }
      }//try-catch
    }//method()

  }//inner_class


}//SystemTask.class
