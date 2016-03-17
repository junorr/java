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

package us.pserver.tn3270;

import com.ino.freehost.client.IsProtectedException;
import com.ino.freehost.client.RW3270;
import com.ino.freehost.client.RW3270Char;
import com.ino.freehost.client.RW3270Field;
import com.ino.freehost.client.RWTnAction;
import com.ino.freehost.client.RWTnActionImpl;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que representa uma sessão de uso de um
 * terminal 3270. Todas os comandos e funções
 * do terminal são executados através desta classe.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/07/2013
 */
public class Session implements RWTnAction {

  /**
   * <code>DEFAULT_TIMEOUT = 10_000;</code><br>
   * Tempo padrão em milisegundos de aguardo por 
   * um determinado campo na tela;
   */
  public static final int DEFAULT_TIMEOUT = 10000;
  
  
  private RW3270 rw;
  
  private boolean connected;
  
  private int defTmout;
  
  private final Object LOCK = new Object();
  
  
  /**
   * Construtor padrão e sem argumentos, inicia
   * uma sessão desconectada.
   */
  public Session() {
    defTmout = DEFAULT_TIMEOUT;
  }
  
  
  /**
   * Retorna o objeto bruto de sessão RW3270.
   * @return objeto bruto de sessão RW3270.
   */
  public RW3270 internal() {
    return rw;
  }
  
  
  /**
   * Retorna o tempo padrão em milisegundos, de espera por um campo na tela.
   * @return tempo padrão em milisegundos, de espera por um campo na tela.
   */
  public int getDefaultTimeout() {
    return defTmout;
  }
  
  
  /**
   * Define o tempo padrão em milisegundos, de espera por um campo na tela.
   * @param tmout tempo padrão em milisegundos, de espera por um campo na tela.
   * @return este objeto Session modificado.
   */
  public Session setDefaultTimeout(int tmout) {
    if(tmout > 0) defTmout = tmout;
    return this;
  }
  
  
  @Override
  public void status(int sts) {
    if(sts == RWTnAction.CONNECTION_ERROR
        || sts == RWTnAction.DISCONNECTED_BY_REMOTE_HOST) {
      this.close();
    }
  }
  
  
  /**
   * Conecta esta sessão ao servidor.
   * @param host Endereço do servidor.
   * @param port porta do servidor.
   * @return Este objeto Session modificado.
   */
  public Session connect(String host, int port) {
    rw = new RW3270(this);
		System.out.println("rw.connect(host, port, null, 0, true);");
    rw.connect(host, port, null, 0, true);
    connected = true;
    return this;
  }
  
  
  /**
   * Conecta esta sessão ao servidor.
   * @param host Endereço do servidor.
   * @param port porta do servidor.
   * @param listener objeto que escuta por modificações no estado da sessão.
   * @return Este objeto Session modificado.
   */
  public Session connect(String host, int port, RWTnAction listener) {
    rw = new RW3270(listener);
		System.out.println("rw.connect(host, port, null, 0, true);");
    rw.connect(host, port, null, 0, true);
    connected = true;
    return this;
  }
  
  
  /**
   * Fecha a conexão com o servidor.
   * @return Este objeto Session desconectado.
   */
  public Session close() {
    if(isConnected()) {
      rw.disconnect();
      connected = false;
    }
    return this;
  }
  
  
  private void checkConn() {
    if(!connected)
      throw new IllegalStateException("Session is not connected");
  }
  
  
  /**
   * Verifica se a sessão está conectada.
   * @return <code>true</code> se a sessão estiver
   * conectada, <code>false</code> caso contrário.
   */
  public boolean isConnected() {
    return connected;
  }
  
  
  /**
   * Define se a sessão está conectada.
   * @param con <code>true</code> se a sessão estiver
   * conectada, <code>false</code> caso contrário.
   * @return este objeto Session modificado.
   */
  public Session setConnected(boolean con) {
    connected = con;
    return this;
  }
  
  
  /**
   * Define a posição do cursor.
   * @param cur objeto Cursor.
   * @return Este objeto Session modificado.
   */
  public Session setCursor(Cursor cur) {
    checkConn();
    rw.setCursor(cur);
    return this;
  }
  
  
  /**
   * Retorna a posição do cursor.
   * @return objeto Cursor.
   */
  public Cursor getCursor() {
    checkConn();
    return rw.getCursor();
  }
  
  
  /**
   * Retorna a posição do cursor.
   * @return objeto Cursor.
   */
  public Cursor cursor() {
    checkConn();
    return rw.getCursor();
  }
  
  
  /**
   * Define a posição do cursor.
   * @param row linha.
   * @param col coluna.
   * @return Este objeto Session modificado.
   */
  public Session cursor(int row, int col) {
    checkConn();
    rw.setCursor(row, col);
    return this;
  }
  
  
  /**
   * Verifica se o conteúdo da tela contém a string informada.
   * @param str string a ser verificada.
   * @return <code>true</code> se o conteúdo da tela
   * contém a string informada, <code>false</code>
   * caso contrário.
   */
  public boolean contains(String str) {
    return this.getScreen().contains(str);
  }
  

  /**
   * Encontra a posição da string na tela.
   * @param str string a ser encontrada.
   * @return objeto Cursor.
   */
  public Cursor find(String str) {
    int i = this.getScreen().indexOf(str);
    if(i < 0) return new Cursor();
    return new Cursor(i+1);
  }
  
  
  /**
   * Retorna um array com todos os campos da tela.
   * @return array com todos os campos da tela.
   */
  public Field[] getFields() {
    List<RW3270Field> fls = rw.getFields();
    if(fls == null || fls.isEmpty())
      return null;
    
    Field[] fields = new Field[fls.size()];
    for(int i = 0; i < fls.size(); i++) {
      fields[i] = Field.from(fls.get(i));
    }
    return fields;
  }
  
  
  /**
   * Retorna um array com todos os caracteres da tela.
   * @return array com todos os caracteres da tela.
   */
  public Char[] getChars() {
    RW3270Char[] chars = rw.getDataBuffer();
    if(chars == null || chars.length == 0)
      return null;
    Char[] cs = new Char[chars.length];
    for(int i = 0; i < chars.length; i++) {
      cs[i] = Char.from(chars[i]);
    }
    return cs;
  }
  
  
  /**
   * Retorna um array com todos os campos desprotegidos contra modificação da tela.
   * @return array com todos os campos desprotegidos contra modificação da tela.
   */
  public Field[] getUnprotectedFields() {
    List<Field> unp = new LinkedList<>();
    Field[] fls = this.getFields();
    for(Field f : fls) {
      if(!f.isProtected())
        unp.add(f);
    }
    fls = new Field[unp.size()];
    return unp.toArray(fls);
  }
  
  
  /**
   * Retorna o campo que está na posição informada.
   * @param cur objeto Cursor da posição.
   * @return objeto Field sob a posição informada.
   */
  public Field getFieldAt(Cursor cur) {
    checkConn();
    RW3270Field f = rw.getFieldAt(cur);
    if(f == null) return null;
    return Field.from(f);
  }
  
  
  /**
   * Define um campo na tela.
   * @param fld Campo a ser definido.
   * @return Este objeto Session modificado.
   */
  public Session set(Field fld) {
    checkConn();
    if(fld != null) {
      rw.setField(fld.getCursor(), fld.getContent());
    }
    return this;
  }
  
  
  /**
   * Define um conteúdo na tela.
   * @param cur objeto Cursor da posição.
   * @param str conteúdo a ser definido.
   * @return este objeto Session modificado.
   */
  public Session set(Cursor cur, String str) {
    checkConn();
    if(cur != null && str != null)
      rw.setField(cur, str);
    return this;
  }
  
  
  /**
   * Define um conteúdo na posição atual do cursor.
   * @param str conteúdo a ser definido.
   * @return Este objeto Session modificado.
   */
  public Session set(String str) {
    return this.set(cursor(), str);
  }
  
  
  /**
   * Retorna o campo sob a posição atual do cursor.
   * @return objeto Field sob a posição atual do cursor.
   */
  public Field get() {
    return this.getFieldAt(cursor());
  }
  
  
  /**
   * Retorna o conteúdo sob a posição atual do cursor, com o comprimento especificado.
   * @param length comprimento do conteúdo a ser retornado.
   * @return conteúdo sob a posição atual do cursor.
   */
  public String get(int length) {
    return this.get(cursor(), length);
  }
  
  
  /**
   * Retorna o conteúdo sob a posição do cursor, com o comprimento especificado.
   * @param cur Cursor com a posição atual.
   * @param len comprimento do conteúdo a ser retornado.
   * @return conteúdo sob a posição atual do cursor.
   */
  public String get(Cursor cur, int len) {
    checkConn();
    return rw.getString(cur, len);
  }
  
  
  /**
   * Retorna o conteúdo do campo informado.
   * @param fld Conteúdo do campo informado.
   * @return conteúdo do campo informado.
   */
  public String get(Field fld) {
    if(fld == null) return null;
    fld.setContent(this.get(fld.getCursor(), fld.getLength()));
    return fld.getContent();
  }
  
  
  /**
   * Retorna todo conteúdo da tela atual, sem quebras de linha.
   * @return todo conteúdo da tela atual, sem quebras de linha.
   */
  public String getScreen() {
    char[] cs = rw.getDisplay();
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < cs.length; i++) {
      //sb.append(cs[i]).append("[").append((int)cs[i]).append("-");
      sb.append(cs[i]);
    }
    return sb.toString();
  }
  
  
  /**
   * Retorna todo conteúdo da tela atual, COM quebras de linha.
   * @return todo conteúdo da tela atual, COM quebras de linha.
   */
  public String getScreenln() {
    char[] cs = rw.getDisplay();
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < cs.length; i++) {
      //sb.append(cs[i]).append("[").append((int)cs[i]).append("-");
      sb.append(cs[i]);
      if(i%80 == 0) sb.append('\n');
    }
    return sb.toString();
  }
  
  
  /**
   * Aguarda aparecer o conteúdo na posição indicada, pelo tempo informado, 
   * com um número máximo de tentativas em caso de falha.
   * @param cur posição.
   * @param str conteúdo.
   * @param timeout tempo em milisegundos a aguardar até aparecer o conteúdo.
   * @param tries número máximo de tentativas em caso de falha.
   * @return <code>true</code> se o conteúdo com os parâmetro informados aparecer,
   * <code>false</code> caso contrário.
   */
  private boolean waitFor(Cursor cur, String str, long timeout, int tries) {
    checkConn();
    if(cur == null || str == null) return false;
    String get = this.get(cur, str.length());
    if(get.equals(str)) return true;
    else if(tries <= 0) return false;
    synchronized(LOCK) {
      try { LOCK.wait(timeout); }
      catch(InterruptedException e) {}
    }
    return waitFor(cur, str, timeout, tries -1);
  }
  
  
  /**
   * Aguarda aparecer o conteúdo na posição indicada.
   * @param cur posição.
   * @param str conteúdo.
   * @return <code>true</code> se o conteúdo com os parâmetro informados aparecer,
   * <code>false</code> caso contrário.
   */
  public boolean waitFor(Cursor cur, String str) {
    return this.waitFor(cur, str, (defTmout / 6), 6);
  }
  
  
  /**
   * Aguarda aparecer o conteúdo no campo informado.
   * @param fld Campo Field.
   * @return <code>true</code> se o conteúdo com os parâmetro informados aparecer,
   * <code>false</code> caso contrário.
   */
  public boolean waitFor(Field fld) {
    if(fld == null) return false;
    return this.waitFor(fld.getCursor(), fld.getContent());
  }
  
  
  /**
   * Aguarda aparecer o conteúdo na posição indicada, pelo tempo informado, 
   * com um número máximo de tentativas em caso de falha, OU aguarda pelo segundo
   * conteúdo na segundo posição indicada em casa de falha de todas as tentativas.
   * @param cur posição do primeiro conteúdo.
   * @param str primeiro conteúdo.
   * @param cur2 posição do segundo conteúdo.
   * @param str2 segundo conteúdo.
   * @param timeout tempo em milisegundos a aguardar até aparecer o conteúdo.
   * @param tries número máximo de tentativas em caso de falha.
   * @return <code>true</code> se um dos conteúdos com os parâmetro informados aparecer,
   * <code>false</code> caso contrário.
   */
  private boolean waitElse(Cursor cur, String str, 
      Cursor cur2, String str2, long timeout, int tries) {
    checkConn();
    if(cur == null || str == null) return false;
    if(cur2 == null || str2 == null) return false;
    String get = this.get(cur, str.length());
    String get2 = this.get(cur2, str2.length());
    if(get.equals(str) || get2.equals(str2)) return true;
    else if(tries <= 0) return false;
    synchronized(LOCK) {
      try { LOCK.wait(timeout); }
      catch(InterruptedException e) {}
    }
    return waitElse(cur, str, cur2, str2, timeout, tries -1);
  }
  
  
  /**
   * Aguarda aparecer o conteúdo na posição indicada OU aguarda pelo segundo
   * conteúdo na segundo posição indicada em casa de falha.
   * @param cur posição do primeiro conteúdo.
   * @param str primeiro conteúdo.
   * @param cur2 posição do segundo conteúdo.
   * @param str2 segundo conteúdo.
   * @return <code>true</code> se um dos conteúdos com os parâmetro informados aparecer,
   * <code>false</code> caso contrário.
   */
  public boolean waitElse(Cursor cur, String str, Cursor cur2, String str2) {
    return this.waitElse(cur, str, cur2, str2, (defTmout / 6), 6);
  }
  
  
  /**
   * Aguarda aparecer o conteúdo do campo informado, OU aguarda pelo segundo
   * conteúdo do segundo campo indicado em casa de falha.
   * @param fld primeiro campo a ser aguardado.
   * @param fld2 segundo campo a ser aguardado em caso de falha.
   * @return <code>true</code> se um dos conteúdos com os parâmetro informados aparecer,
   * <code>false</code> caso contrário.
   */
  public boolean waitElse(Field fld, Field fld2) {
    if(fld == null || fld2 == null) return false;
    return this.waitElse(fld.getCursor(), fld.getContent(), 
        fld2.getCursor(), fld2.getContent());
  }
  
  
  /**
   * Executa uma tarefa Task no terminal, retornando-a com os parâmetros definidos.
   * @param tsk Tarefa a ser executada.
   * @return tarefa modificada com os parâmetros definidos.
   */
  public Task execute(Task tsk) {
    if(tsk == null
        || tsk.controlField() == null
        || tsk.controlField().getContent() == null)
      return tsk;
    
    if(this.waitFor(tsk.controlField())) {
      for(Field fld : tsk.fields()) {
        if(fld.isProtected())
          this.get(fld);
        else
          this.set(fld);
      }
    }//if
    if(tsk.key() != null)
      this.sendKey(tsk.key());
    return tsk;
  }
  
  
  /**
   * Executa um caminho Path no terminal, retornando-o com os parâmetros definidos.
   * @param pth Caminho a ser executado.
   * @return caminho modificado com os parâmetros definidos.
   */
  public Path execute(Path pth) {
    if(pth == null || pth.tasks().isEmpty())
      return pth;
    
    if(pth.getDelayBetweenTasks() > 0)
      for(Task tsk : pth.tasks()) {
        this.execute(tsk);
        this.delay(pth.getDelayBetweenTasks());
      }
    else
      for(Task tsk : pth.tasks()) {
        this.execute(tsk);
      }
    return pth;
  }
  
  
  /**
   * Aguarda pelo tempo definido em milisegundos.
   * @param delay tempo em milisegundos.
   */
  public void delay(int delay) {
    if(delay > 0) {
      synchronized(LOCK) {
        try { LOCK.wait(delay); }
        catch(InterruptedException e) {}
      }
    }
  }
  
  
  /**
   * Executa o pressionamento da tecla [Enter].
   * @return Este objeto Session modificado.
   */
  public Session enter() {
    checkConn();
    rw.enter();
    return this;
  }
  
  
  /**
   * Executa o pressionamento da tecla [Delete].
   * @return Este objeto Session modificado.
   */
  public Session delete() {
    checkConn();
    try {rw.delete();}
    catch(IsProtectedException e){}
    return this;
  }
  
  
  /**
   * Executa o pressionamento da tecla [Backspace].
   * @return Este objeto Session modificado.
   */
  public Session backspace() {
    checkConn();
    try {rw.backspace();}
    catch(IsProtectedException e){}
    return this;
  }
  
  
  /**
   * Executa o pressionamento da tecla [Tab].
   * @return Este objeto Session modificado.
   */
  public Session tab() {
    checkConn();
    rw.tab();
    return this;
  }
  
  
  /**
   * Executa o pressionamento das teclas [Shift + Tab].
   * @return Este objeto Session modificado.
   */
  public Session backTab() {
    checkConn();
    rw.backTab();
    return this;
  }
  
  
  /**
   * Executa o pressionamento da tecla [Home].
   * @return Este objeto Session modificado.
   */
  public Session home() {
    checkConn();
    rw.home();
    return this;
  }
  

  /**
   * Move o cursor uma posição para a esquerda.
   * @return Este objeto Session modificado.
   */
  public Session cursorLeft() {
    checkConn();
    rw.left();
    return this;
  }
  
  
  /**
   * Move o cursor uma posição para a direita.
   * @return Este objeto Session modificado.
   */
  public Session cursorRight() {
    checkConn();
    rw.right();
    return this;
  }
  
  
  /**
   * Move o cursor uma posição para cima.
   * @return Este objeto Session modificado.
   */
  public Session cursorUp() {
    checkConn();
    rw.up();
    return this;
  }
  
  
  /**
   * Move o cursor uma posição para baixo.
   * @return Este objeto Session modificado.
   */
  public Session cursorDown() {
    checkConn();
    rw.down();
    return this;
  }
  
  
  /**
   * Executa o pressionamento da tecla informada.
   * @param key tecla a ser executada.
   * @return Este objeto Session modificado.
   */
  public Session sendKey(Key key) {
    checkConn();
    switch(key) {
      case ENTER:
        this.enter();
        break;
      case BACKSPACE:
        this.backspace();
        break;
      case HOME:
        this.home();
        break;
      case DELETE:
        this.delete();
        break;
      case LEFT:
        this.cursorLeft();
        break;
      case RIGHT:
        this.cursorRight();
        break;
      case UP:
        this.cursorUp();
        break;
      case DOWN:
        this.cursorDown();
        break;
      case TAB:
        this.tab();
        break;
      case BACK_TAB:
      case SHIFT_TAB:
        this.backTab();
        break;
      default:
        rw.PF(key.value());
        break;
    }
    return this;
  }
  
  
  /**
   * Aguarda por novos dados pela conexão.
   */
  public void waitForNewData() {
    rw.waitForNewData();
  }


  @Override
  public void incomingData() {
    synchronized(LOCK) {
      LOCK.notifyAll();
    }
  }


  @Override
  public void cursorMove(int oldPos, int newPos) {
    this.setCursor(new Cursor(newPos));
  }


  @Override
  public void broadcastMessage(String msg) {
    System.out.println(msg);
  }


  @Override
  public void beep() {
    System.out.print("\07");
    System.out.flush(); 
  }
  
}
