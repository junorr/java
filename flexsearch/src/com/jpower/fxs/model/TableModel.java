package com.jpower.fxs.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Implementa TableModel, utilizando 
 * ColumnModel e ContentHandler para obtenção dos 
 * atributos de objetos fonte adicionados à
 * ColumnModel, que comporão os dados da tabela.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.05.18
 * @see com.jpower.fxs.table.ContentHandler
 * @see com.jpower.fxs.model.ColumnModel
 */
public class TableModel extends AbstractTableModel {
  
  private List<ColumnModel> cols;
  
  private List src;
  
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public TableModel() {
    cols = new ArrayList<ColumnModel>();
    src = new ArrayList();
  }
  
  
  /**
   * Adiciona um <code>ColumnModel</code> ao modelo.
   * @param cm <code>ColumnModel</code> da coluna a ser adicionada.
   * @return Esta instância de TableModel modificada.
   */
  public TableModel addColumn(ColumnModel cm) {
    if(cm != null) cols.add(cm);
    return this;
  }
  
  
  /**
   * Adiciona um objeto fonte à lista de onde serão extraídos
   * os dados para exibição na tabela.
   * @param o Objeto fonte dos dados a serem exibidos.
   * @return Esta instância de TableModel modificada.
   */
  public TableModel addSource(Object o) {
    if(o != null) src.add(o);
    for(int i = 0; i < cols.size(); i++) {
      cols.get(i).setSource(src);
    }
    return this;
  }
  
  
  /**
   * Define a lista contendo os objetos fonte 
   * de onde serão extraídos os dados a serem
   * exibidos pela tabela
   * @param l Lista contendo os objetos fonte.
   * @return Esta instância de TableModel modificada.
   */
  public TableModel setSource(List l) {
    if(l != null) src = l;
    for(int i = 0; i < cols.size(); i++) {
      cols.get(i).setSource(src);
    }
    return this;
  }
  
  
  /**
   * Retorna a lista contendo os <code>ColumnModel's</code>
   * @return Lista de ColumnModel
   */
  public List<ColumnModel> columns() {
    return cols;
  }
  
  
  /**
   * Limpa a lista contendo os ColumnModel's.
   * @return Esta instância de TableModel modificada.
   */
  public TableModel clearColumns() {
    cols.clear();
    return this;
  }
  

  /**
   * Retorna o número de linhas da tabela.
   * @return Número de linhas.
   */
  @Override
  public int getRowCount() {
    if(cols.isEmpty()) return 0;
    return cols.get(0).data().size();
  }

  
  /**
   * Retorna o número de colunas da tabela.
   * @return Número de colunas.
   */
  @Override
  public int getColumnCount() {
    return cols.size();
  }

  
  /**
   * Retorna o valor armazenado na célula
   * dada pelo índice da linha e coluna da
   * tabela.
   * @param rowIndex Índice da linha.
   * @param columnIndex Índice da coluna.
   * @return O objeto contido na célula.
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if(columnIndex < 0 || columnIndex > cols.size())
      return null;
    return cols.get(columnIndex).get(rowIndex);
  }


  /**
   * Retorna o nome da coluna dada pelo índice
   * informado.
   * @param columnIndex índice da coluna.
   * @return O nome da coluna.
   */
  @Override
  public String getColumnName(int columnIndex) {
    if(columnIndex < 0 || columnIndex > cols.size())
      return null;
    return cols.get(columnIndex).getTitle();
  }
  
  
  /**
   * Retorna a classe do tipo de dado contido
   * na coluna dada pelo índice especificado.
   * @param columnIndex ìndice da coluna.
   * @return A classe do tipo de dado.
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if(columnIndex < 0 || columnIndex > cols.size())
      return null;
    return cols.get(columnIndex).getType();
  }
  
  
  /**
   * Retorna <code>true</code> se a celula especificada
   * pelo índice da linha e coluna informados puder ser
   * editada.
   * @param rowIndex Índice da linha.
   * @param columnIndex Índice da coluna.
   * @return <code>true</code> se a célula puder
   * ser editada, <code>false</code> caso contrário.
   */
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }
  
}
