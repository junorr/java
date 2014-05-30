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

package com.jpower.db4o;

import com.db4o.query.Predicate;
import com.db4o.query.Query;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 27/12/2012
 */
public class SimpleStoreRetrieveTest {
  
  public static void listResult(List l) {
    System.out.println("result [size="+l.size()+"]:");
    for(Object o : l)
      System.out.println(o);
    System.out.println();
  }
  
  
  public static void main(String[] args) {
    /*
    
    DB.clear();
    
    PF p = new PF("Juno Roesler", "96864516091");
    Endereco e = new Endereco();
    Cidade bsb = new Cidade("Brasília", "DF");
    e.setCidade(bsb);
    e.setNumero(209);
    e.setRua("Cond. San Diego");
    p.setEndereco(e);
    
    PF p2 = new PF("Talitah Badra Roesler", "99999999999");
    p2.setEndereco(e);
    
    DB.getContainer().store(p);
    DB.getContainer().store(p2);
    
    System.out.println("stored: ["+p+"]");
    System.out.println("stored: ["+p2+"]");
    
    
    System.out.println();
    System.out.println("--- QbE -----------------");
    
    bsb = new Cidade("Brasília", "DF");
    Pessoa pessoa = new PF();
    Endereco end = new Endereco();
    end.setCidade(bsb);
    pessoa.setEndereco(end);
    System.out.println("Query by Cidade: "+bsb);
    List l = DB.getContainer().queryByExample(pessoa);
    listResult(l);
    
    
    System.out.println();
    System.out.println("Query by: "+Endereco.class);
    end = new Endereco();
    listResult(DB.getContainer().queryByExample(Endereco.class));
    
    
    System.out.println();
    System.out.println("SODA Query Pessoa by uf: DF");
    
    Query q = DB.getContainer().query();
    q.constrain(Pessoa.class);
    q.descend("endereco").descend("cidade").descend("uf").constrain("DF");
    listResult(q.execute());

    
    System.out.println();
    System.out.println("SODA Query Endereco by uf: DF");
    
    q = DB.getContainer().query();
    q.constrain(Endereco.class);
    q.descend("cidade").descend("uf").constrain("DF");
    listResult(q.execute());
    
    
    System.out.println();
    System.out.println("Update Cidade");
    
    Cidade c = new Cidade(null, "DF");
    List<Cidade> ls = DB.getContainer().queryByExample(c);
    if(ls != null && !ls.isEmpty()) {
      c = ls.get(0);
      c.setNome("Porto Alegre");
      c.setUf("CF");
      DB.getContainer().store(c);
    }
    
    
    System.out.println();
    System.out.println("SODA Query Pessoa by Cidade: Porto Alegre");
    
    q = DB.getContainer().query();
    q.constrain(Pessoa.class);
    q.descend("endereco").descend("cidade").descend("nome").constrain("Porto Alegre");
    listResult(q.execute());

    
    System.out.println();
    System.out.println("SODA Query Pessoa by cpf");
    
    Pessoa ps = new PJ("Some PJ", "999");
    DB.getContainer().store(ps);
    
    q = DB.getContainer().query();
    q.constrain(Pessoa.class);
    q.descend("cnpj").constrain("999");
    listResult(q.execute());
    
    
    System.out.println();
    System.out.println("Native Query Pessoa by Cidade startsWith 'Porto'");
    
    listResult(DB.getContainer().query(new Predicate<Pessoa>() {
      @Override
      public boolean match(Pessoa et) {
        if(et == null) return false;
        return et.getEndereco().getCidade()
            .getNome().startsWith("Porto");
      }
    }));
    
    
    System.out.println();
    System.out.println("SODA Query Cidade by Pessoa like 'Roes'");
    
    q = DB.getContainer().query();
    q.constrain(Pessoa.class);
    q.descend("nome").constrain("Roes").like();
    Query qc = q.descend("endereco").descend("cidade");
    
    listResult(qc.execute());
    
    
    System.out.println();
    System.out.println("Update Cidade by Pessoa like 'Badra'");
    
    q = DB.getContainer().query();
    q.constrain(Pessoa.class);
    q.descend("nome").constrain("Badra").like();
    List<Pessoa> lp = q.execute();
    
    Pessoa pb = lp.get(0);
    e = new Endereco();
    e.setNumero(209);
    e.setRua("Cond. San Diego");
    c = new Cidade("Brasília", "DF");
    e.setCidade(c);
    pb.setEndereco(e);
    System.out.println(pb);
    DB.getContainer().store(pb);
    DB.getContainer().commit();
    */
    
    System.out.println();
    System.out.println("SODA Query Pessoa like 'Badra'");
    
    Query q = DB.getContainer().query();
    q.constrain(Pessoa.class);
    q.descend("nome").constrain("Badra").like();
    
    listResult(q.execute());
    
    
    System.out.println();
    System.out.println("SODA Query Cidade by Pessoa contains 'Badra'");
    
    q = DB.getContainer().query();
    q.constrain(Pessoa.class);
    q.descend("nome").constrain("Badra").contains();
    Query qc = q.descend("endereco").descend("cidade");
    
    listResult(qc.execute());
    
    
    System.out.println();
    System.out.println("SODA Query Cidade by Pessoa like 'Roes'");
    
    q = DB.getContainer().query();
    q.constrain(Pessoa.class);
    q.descend("nome").constrain("Roes").like();
    qc = q.descend("endereco").descend("cidade");
    
    listResult(qc.execute());
    
    
    System.out.println();
    System.out.println("SODA Query Cidade by Pessoa like 'Juno'");
    
    q = DB.getContainer().query();
    q.constrain(Pessoa.class);
    q.descend("nome").constrain("Juno").like();
    qc = q.descend("endereco").descend("cidade");
    
    listResult(qc.execute());
    
    
    System.out.println();
    System.out.println("SODA Query All");
    
    q = DB.getContainer().query();
    q.constrain(Object.class);
    
    listResult(q.execute());
    
    DB.getContainer().close();
  }
  
}
