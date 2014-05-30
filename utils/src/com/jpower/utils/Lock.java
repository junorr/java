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

/**
 * A classe <code>Lock</code> implementa uma trava
 * para utilização de recursos exclusivos do sistema,
 * como operações de escrita em um arquivo, por exemplo.
 * Para sinalizar a trava, Lock utiliza um arquivo, cujo
 * caminho padrao eh o de onde foi chamado o aplicativo.
 * Este caminho pode ser modificado atraves do metodo
 * <code>setFileLock( File )</code>.
 * Deve existir apenas uma instancia de <code>Lock</code>
 * por JVM, portanto nao existem construtores publicos.
 * Uma Instancia de <code>Lock</code> pode ser obtida
 * atraves dos metodos <code>getInstance()</code> e
 * <code>getInstance( File )</code>.
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class Lock {

  private File lock;

  private static boolean locked = false;

  private static Lock instance = null;

  /**
   * Construtor privado padrao sem argumentos.
   */
  private Lock() {
    lock = new File("LOCK");
    if(lock.exists())
      locked = true;
  }//method()

  /**
   * Construtor privado que recebe como parametro um
   * <code>File</code> com o diretorio onde serah
   * criado o arquivo de trava.
   * @param dirLock Diretorio onde serah criado o
   * arquivo de trava.
   */
  private Lock(File dirLock) {
    this();
    this.setWorkDirectory(dirLock);
  }//method()

  /**
   * Metodo <code>static</code> para obter
   * uma instancia de <code>Lock</code>.
   * @return Uma instancia de <code>Lock</code>.
   */
  public static Lock getInstance() {
    if(instance != null)
      return instance;

    instance = new Lock();

    return instance;
  }//method()

  /**
   * Metodo <code>static</code> para obter
   * uma instancia de <code>Lock</code>. Recebe
   * como argumento um <code>File</code> indicando
   * o diretorio onde serah criado o arquivo de trava
   * @param dirLock Diretorio onde serah criado o
   * arquivo de trava.
   * @return Uma instancia de <code>Lock</code>.
   */
  public static Lock getInstance(File dirLock) {
    if(instance != null)
      instance.setWorkDirectory(dirLock);
    else
      instance = new Lock(dirLock);

    return instance;
  }//method()

  /**
   * Tenta obeter a trava. O sucesso na
   * obtencao eh indicado pelo valor de retorno.
   * @return <code>true</code> se a trava for obtida
   * com sucesso, <code>false</code> caso contrario.
   */
  public boolean lock() {

    this.testLockFile();

    if(locked) return false;

    try {

      locked = true;
      lock.createNewFile();

      FileOutputStream fout = new FileOutputStream(lock);
      fout.write(1);
      fout.flush();
      fout.getFD().sync();
      fout.close();

      return locked;

    } catch(Exception ex) {

      //ex.printStackTrace();
      locked = false;
      return locked;

    }//try-catch

  }//method()

  /**
   * Verifica se <code>Lock</code> estah
   * travado.
   * @return <code>true</code> caso esteja
   * travado, <code>false</code> caso contrario.
   */
  public boolean isLocked() {
    this.testLockFile();
    return locked;
  }//method()

  /**
   * Testa se o arquivo de trava existe,
   * setando <code>Lock</code> para
   * <code>locked ou unlocked</code>,
   * conforme o caso.
   */
  public void testLockFile() {
    if(lock.exists())
      locked = true;
    else
      locked = false;
  }//method()

  /**
   * Libera a trava de <code>Lock</code>, caso
   * esteja travado. caso ocorra algum erro
   * de I/O ou de seguranca do OS, serah retornado
   * <code>false</code>.
   * @return <code>false</code> se ocorrer erro
   * de I/O ou de seguranca do OS, <code>true</code>
   * caso contrario.
   */
  public boolean unlock() {
    if(!locked) return true;
    try {

      Thread.sleep(30);

      if(lock.exists())
        lock.delete();

      locked = false;
      return !locked;

    } catch(Exception ex) {
      this.unlock();
      //ex.printStackTrace();
      return false;

    }//try-catch

  }//method()

  /**
   * Seta o diretorio onde serah criado o arquivo
   * de trava. Se o paramento informado for
   * <code>null</code>, o metodo retorna sem que
   * nada ocorra.
   * @param f Diretorio onde sera manipulado o arquivo
   * de trava.
   */
  public void setWorkDirectory(File f) {
    if(f == null) return;

    if(!f.exists())
      f.mkdirs();

    boolean l = this.isLocked();
    this.unlock();
    lock = new File(f.getPath() + File.separator + "LOCK");

    if(l)
      this.lock();
  }//method()

  /**
   * Retorna o arquivo de trava de <code>Lock</code>.
   * @return Um <code>File</code> representando o
   * arquivo de trava.
   */
  public File getLockFile() {
    return lock;
  }//method()

  /**
   * Tenta obter a trava repetidamente pelo
   * tempo maximo (em milisegundos) especificado.
   * Se o tempo especificado for menor ou igual a
   * zero (0), <code>waitLock(int)</code> irah
   * bloquear ateh a obtencao da trava. Este
   * procedimento eh altamente contra indicado,
   * visto que se o arquivo de trava nao puder ser
   * criado por um problema qualquer, a aplicacao
   * irah travar indefinidamente.
   * @param millis Tempo maximo (em milisegundos)
   * que <code>waitLock(int)</code> irah bloquear
   * durante as tentativas de obtencao da trava.
   * @return <code>true</code> se a trava for obtida
   * com sucesso, <code>false</code> caso contrario.
   */
  public boolean waitLock(int millis) {
    //tenta obter a trava a primeira vez
    boolean obtainlock = this.lock();
    //Se a trava for obtida com sucesso, retorna true.
    if(obtainlock) return obtainlock;

    try {

      //intervalo entre cada tentativa de obtencao
      int dec = 10;

      //Se millis menor ou igual a zero,
      //waitLock bloqueia indefinidamente ateh
      //a obtencao da trava.
      if(millis <= 0) {
        dec = 0;
        millis = 1;
      }//if-else

      while(!obtainlock && millis > 0) {

        obtainlock = this.lock();
        Thread.sleep(10);
        millis -= dec;

      }//do-while

      return obtainlock;

    } catch(Exception ex) {

      //ex.printStackTrace();
      return obtainlock;

    }//try-catch

  }//method()

}//class
