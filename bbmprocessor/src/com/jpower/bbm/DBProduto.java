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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 11/04/2013
 */
public class DBProduto {

  private File file;
  
  private List<Produto> produtos;
  
  
  public DBProduto() {
    file = null;
    produtos = new LinkedList<>();
  }
  
  
  public DBProduto(File f) {
    produtos = new LinkedList<>();
    this.setFile(f);
  }


  public File getFile() {
    return file;
  }


  public DBProduto setFile(File f) {
    this.file = f;
    System.out.println("* DB File: "+ file);
    return this;
  }


  public List<Produto> getProdutos() {
    return produtos;
  }


  public Produto find(Produto pf) {
    if(pf == null || produtos.isEmpty())
      return null;
    
    for(Produto p : produtos)
      if(p.equals(pf))
        return p;
    return null;
  }
  
  
  public DBProduto readFile(File f) {
    return this.setFile(f).readFile();
  }
  
  
  public DBProduto readFile() {
    if(file == null || !file.exists())
      throw new IllegalStateException(
          "Invalid DB File: "+ file);
    
    try (BufferedReader reader = 
        new BufferedReader(new FileReader(file))) {
      
      System.out.print("* Reading DB File... ");
      
      String line = reader.readLine();
      while(line != null) {
        Produto p = new Produto();
        if(p.fromString(line))
          produtos.add(p);
        line = reader.readLine();
      }
      
      System.out.println("[OK]");
      System.out.println("* Produtos carregados: "+ produtos.size());
      
    } catch(Exception e) {
      e.printStackTrace();
    }
    return this;
  }
  
  
  public static void main(String[] args) {
    File f = new File("D:/baixa/produtos.csv");
    DBProduto db = new DBProduto(f).readFile();
  }
  
}
