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
 * Task representa uma tarefa a ser executada em um terminal 3270,
 * como o pressionamento de uma tecla, aguardar por determinado dado,
 * definir e capturar campos, entre outras.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 31/07/2013
 */
public class Task {

  private LinkedList<Field> flds;
  
  private Key key;
  
  private Field ctrl;
  
  
  /**
   * Construtor padrão sem argumentos, cria uma tarefa vazia.
   */
  public Task() {
    flds = new LinkedList<>();
    key = null;
    ctrl = null;
  }
  
  
  /**
   * Construtor que recebe um campo a ser definido e
   * uma tecla a ser pressionada no terminal.
   * @param ctrlField campo a ser definido.
   * @param key tecla a ser pressionada.
   */
  public Task(Field ctrlField, Key key) {
    this();
    this.key = key;
    this.ctrl = ctrlField;
  }
  
  
  /**
   * Adiciona um campo a ser definido/capturado pelo terminal.
   * @param fld campo Field.
   * @return Este objeto Task modificado.
   */
  public Task add(Field fld) {
    if(fld != null) {
      flds.add(fld);
    }
    return this;
  }
  
  
  /**
   * Retorna uma lista com todos os campos adicionados à esta Task.
   * @return java.util.List&gt;Field&lt;
   */
  public List<Field> fields() {
    return flds;
  }
  
  
  /**
   * Define um campo pelo qual o terminal irá aguardar antes 
   * de executar as demais tarefas.
   * @param ctrl Campo de controle que será aguardado.
   * @return Este objeto Task modificado.
   */
  public Task setControlField(Field ctrl) {
    this.ctrl = ctrl;
    return this;
  }
  
  
  /**
   * Define uma tecla a ser processada no terminal.
   * @param key tecla a ser processada no terminal.
   * @return Este objeto Task modificado.
   */
  public Task setKey(Key key) {
    this.key = key;
    return this;
  }
  

  /**
   * Retorna a tecla processada no terminal.
   * @return tecla processada no terminal.
   */
  public Key key() {
    return key;
  }
  
  
  /**
   * Retorna o campo de controle que será aguardado.
   * @return campo de controle que será aguardado.
   */
  public Field controlField() {
    return ctrl;
  }
  
  
  /**
   * Encontra um campo com o conteúdo especificado.
   * @param content Conteúdo a ser encontrado.
   * @return Campo Field ou <code>null</code> caso não seja encontrado.
   */
  public Field findField(String content) {
    if(content == null)
      return null;
    for(Field fld : flds) {
      if(fld.getContent().equals(content))
        return fld;
    }
    return null;
  }
  
  
  /**
   * Encontra um campo com a posição e comprimento especificados.
   * @param row linha.
   * @param col coluna.
   * @param len comprimento.
   * @return Campo Field ou <code>null</code> caso não seja encontrado.
   */
  public Field findField(int row, int col, int len) {
    if(!Cursor.isValid(row, col) || len < 1)
      return null;
    for(Field fld : flds) {
      if(fld.getRow() == row
          && fld.getColumn() == col
          && fld.getLength() == len)
        return fld;
    }
    return null;
  }
  
}
