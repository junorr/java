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

import java.util.LinkedList;
import java.util.List;

/**
 * Representação de um caminho a ser seguido 
 * nas funções de um sistema 3270.
 * Um exemplo de caminho a ser excutado em um terminal
 * 3270:<br>
 * <pre>
 * -&gt; Tecla [Enter];
 * -&gt; Informa chave, senha e sistema;
 * -&gt; Tecla [Enter];
 * -&gt; Aguarda uma tela representada por um campo específico;
 * -&gt; Digita uma opção;
 * -&gt; Tecla [Enter];
 * -&gt; Aguarda por outra tela representada por um campo específico.
 * </pre><br>
 * Um objeto Path representando este caminho poderia ser defindo
 * da seguinte maneira:<br>
 * <pre>
 * new Path().add( new Task().setKey(Key.ENTER) )
 *     .add( new Task().setControlField( new Field(1, 3, "CIC3C01") ))
 *     .add( new Task( new Field(13, 21, "chave") ))
 *     .add( new Task( new PasswordField(14, 21, "senha") ))
 *     .add( new Task( new Field(15, 21, "aplicativo"), Key.ENTER ))
 *     .add( new Task().setControlField( new Field(1, 2, "BB68") ))
 *     .add( new Task( new Field(21, 20, "31"), Key.ENTER )
 *     .add( new Task().setControlField( new Field(1, 3, "P0130") ))
 * </pre>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 31/07/2013
 */
public class Path {

  private LinkedList<Task> tsks;
  
  private int taskDelay;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public Path() {
    tsks = new LinkedList<>();
    taskDelay = 0;
  }
  
  
  /**
   * Adiciona um objeto de tarefa Task ao caminho.
   * @param tsk objeto de tarefa Task.
   * @return Ente objeto Path modificado.
   */
  public Path add(Task tsk) {
    if(tsk != null) {
      tsks.add(tsk);
    }
    return this;
  }
  
  
  /**
   * Retorna uma lista com todas as Tasks adicionadas.
   * @return java.util.List&gt;Task&lt;
   */
  public List<Task> tasks() {
    return tsks;
  }
  
  
  /**
   * Encontra uma task pelo campo de controle.
   * @param ctrlString conteúdo do campo de controle.
   * @return objeto Task ou <code>null</code> caso
   * não seja encontrado.
   */
  public Task findTaskByControl(String ctrlString) {
    if(ctrlString == null || tsks.isEmpty())
      return null;
    
    for(Task tsk : tsks) {
      if(tsk.controlField().getContent()
          .equals(ctrlString))
        return tsk;
    }
    return null;
  }
  
  
  /**
   * Encontra uma task pelo conteúdo do campo.
   * @param fldContent conteúdo do campo.
   * @return objeto Task ou <code>null</code> caso
   * não seja encontrado.
   */
  public Task findTaskByField(String fldContent) {
    if(fldContent == null || tsks.isEmpty())
      return null;
    
    for(Task tsk : tsks) {
      Field f = tsk.findField(fldContent);
      if(f != null) return tsk;
    }
    return null;
  }
  
  
  /**
   * Encontra uma task pela posição e comprimento do campo.
   * @param row linha.
   * @param col coluna.
   * @param len comprimento.
   * @return objeto Task ou <code>null</code> caso
   * não seja encontrado.
   */
  public Task findTaskByField(int row, int col, int len) {
    if(!Cursor.isValid(row, col) || tsks.isEmpty())
      return null;
    
    for(Task tsk : tsks) {
      Field f = tsk.findField(row, col, len);
      if(f != null) return tsk;
    }
    return null;
  }
  
  
  /**
   * Encontra um campo pelo seu conteúdo.
   * @param fldContent conteúdo do campo.
   * @return objeto Field ou <code>null</code>
   * caso não seja encontrado.
   */
  public Field findField(String fldContent) {
    if(fldContent == null || tsks.isEmpty())
      return null;
    
    for(Task tsk : tsks) {
      Field f = tsk.findField(fldContent);
      if(f != null) return f;
    }
    return null;
  }
  
  
  /**
   * Encontra um campo pela sua posição e comprimento.
   * @param row linha.
   * @param col coluna.
   * @param len comprimento.
   * @return objeto Field ou <code>null</code>
   * caso não seja encontrado.
   */
  public Field findField(int row, int col, int len) {
    if(!Cursor.isValid(row, col) || tsks.isEmpty())
      return null;
    
    for(Task tsk : tsks) {
      Field f = tsk.findField(row, col, len);
      if(f != null) return f;
    }
    return null;
  }
  
  
  /**
   * Retorna o tempo de atraso (em milisegundos) 
   * na execução de cada Task.
   * @return tempo de atraso (em milisegundos) 
   * na execução de cada Task.
   */
  public int getDelayBetweenTasks() {
    return taskDelay;
  }
  
  
  /**
   * Define o tempo de atraso (em milisegundos) 
   * na execução de cada Task.
   * @param delay tempo de atraso (em milisegundos) 
   * na execução de cada Task.
   * @return Este objeto Path modificado.
   */
  public Path setDelayBetweenTasks(int delay) {
    taskDelay = delay;
    return this;
  }
  
}
