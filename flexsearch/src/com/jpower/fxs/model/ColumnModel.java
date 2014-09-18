package com.jpower.fxs.model;

import com.jpower.fxs.table.ContentHandler;
import com.jpower.fxs.table.Extractor;
import java.util.ArrayList;
import java.util.List;

/**
 * ColumnModel implementa um modelo de coluna
 * para tabelas e TableModel's.
 * ColumnModel utiliza a classe ContentHandler
 * para obter os dados da coluna de atributos
 * de objetos (DataSource) usando reflection.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 2.0 - 2011.05.18
 * @see com.jpower.fxs.table.ContentHandler
 */
public class ColumnModel {
  
  private Class type;
  
  private String title;
  
  private ContentHandler handler;
  
  private List source;
  
  private List data;
  
  
  /**
   * Construtor padrão que recebe o título da coluna, a
   * classe do tipo de dado e o ContentHandler responsável
   * pela extração de dados da dos objetos fonte.
   * @param title Título da coluna
   * @param datatype Classe do tipo de dado da coluna.
   * @param ch ContentHandler para extração dos dados.
   */
  public ColumnModel(String title, Class datatype, ContentHandler ch) {
    this(title, datatype);
    if(ch == null)
      throw new IllegalArgumentException(
          "ContentHandler must be NOT NULL");
    
    this.handler = ch;
  }
  
  
  /**
   * Construtor que recebe o nome da coluna e a 
   * classe do tipo de dado armazenado.
   * @param title Título da coluna.
   * @param datatype Classe do tipo de dado armazenado.
   */
  public ColumnModel(String title, Class datatype) {
    String err = null;
    if(title == null || title.trim().isEmpty())
      err = "Title";
    if(datatype == null)
      err = (err == null 
          ? "Class type"
          : err.concat(", class type"));
    if(err != null)
      throw new IllegalArgumentException(
          err.concat(" must be NOT NULL"));
    
    this.title = title;
    this.type = datatype;
    this.handler = new ContentHandler();
    this.data = new ArrayList();
    this.source = new ArrayList();
  }
  
  
  /**
   * Adiciona um extrator ao ContentHandler interno
   * responsável pela extração dos dados.
   * @param ex Extractor a ser adicionado.
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel addExtractor(Extractor ex) {
    if(ex != null) handler.add(ex);
    return this;
  }
  
  
  /**
   * Cria e adiciona um ou mais <code>Extractor</code> configurados
   * pelos <code>fields</code> informados.
   * @param fields Extractors a serem criados e configurados
   *  pelos fields especificados.
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel addFieldExtractor(String ... fields) {
    if(fields == null) return this;
    for(int i = 0; i < fields.length; i++) {
      if(fields[i] != null && !fields[i].trim().isEmpty())
        this.addExtractor(new Extractor().byField(fields[i]));
    }
    return this;
  }
  
  
  /**
   * Cria e adiciona um ou mais <code>Extractor</code> configurados
   * pelos <code>methods</code> informados.
   * @param fields Extractors a serem criados e configurados
   *  pelos methods especificados.
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel addMethodExtractor(String ... methods) {
    if(methods == null) return this;
    for(int i = 0; i < methods.length; i++) {
      if(methods[i] != null && !methods[i].trim().isEmpty())
        this.addExtractor(new Extractor().byMethod(methods[i]));
    }
    return this;
  }
  
  
  /**
   * Adiciona um objeto fonte para extração
   * dos dados da coluna.
   * @param o Objeto fonte.
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel addSource(Object o) {
    if(o == null) return this;
    source.add(o);
    data.add(handler.extract(o));
    return this;
  }
  
  
  /**
   * Define a lista de objetos fonte para
   * extração dos dados da tabela.
   * @param ns Nova lista de objetos fontes.
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel setSource(List ns) {
    if(ns != null) source = ns;
    return this.update();
  }
  
  
  /**
   * Limpa a lista de objetos fonte,
   * removendo todos os itens apensos.
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel clearSource() {
    source.clear();
    return this.clearData();
  }
  
  
  /**
   * Retorna a lista de objetos fonte.
   * @return Lista contendo os objetos fonte.
   */
  public List source() {
    return source;
  }
  
  
  /**
   * Retorna a lista de dados da coluna.
   * @return Lista contendo os dados da coluna.
   */
  public List data() {
    return data;
  }
  
  
  /**
   * Limpa a lista de dados da coluna,
   * removendo todos os objetos apensos.
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel clearData() {
    data.clear();
    return this;
  }
  
  
  /**
   * Retorna um objeto da coluna, dado pelo
   * índice da lista de dados onde está armazenado.
   * @param index Índice do objeto que deseja-se recuperar.
   * @return O objeto recuperado da lista de dados da coluna.
   */
  public Object get(int index) {
    if(index < 0 || index >= data.size())
      return null;
    return data.get(index);
  }
  
  
  /**
   * (Re)Extrai todos os dados dos
   * objetos fonte, armazenando-os na
   * lista de dados da coluna. Os dados
   * contidos anteriormente serão removidos.
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel update() {
    data.clear();
    for(int i = 0; i < source.size(); i++) {
      Object o = handler.extract(source.get(i));
      if(o != null) data.add(o);
    }
    return this;
  }
  
  
  /**
   * Retorna o título da coluna.
   * @return Título da coluna.
   */
  public String getTitle() {
    return title;
  }
  
  
  /**
   * Define o título da coluna.
   * @param title Título da coluna.
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel setTitle(String title) {
    if(title != null) this.title = title;
    return this;
  }
  
  
  /**
   * Retorna a classe do tipo de dado contido
   * na coluna.
   * @return Classe do tipo de dado.
   */
  public Class getType() {
    return type;
  }
  
  
  /**
   * Define a classe do tipo de dado contido
   * na coluna.
   * @param type
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel setType(Class type) {
    if(type != null) this.type = type;
    return this;
  }
  
  
  /**
   * Retorna o ContentHandler utilizado na
   * extração dos dados dos objetos fonte.
   * @return ContentHandler.
   */
  public ContentHandler getContentHandler() {
    return handler;
  }
  
  
  /**
   * Define o ContentHandler responsável pela extração
   * dos dados dos objetos fonte.
   * @param ch ContentHandler.
   * @return Esta instância de ColumnModel modificada.
   */
  public ColumnModel setContentHandler(ContentHandler ch) {
    if(ch != null) this.handler = ch;
    return this;
  }
  
}
