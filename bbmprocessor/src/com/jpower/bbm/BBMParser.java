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

import com.jpower.date.SimpleDate;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 11/04/2013
 */
public class BBMParser {
  
  public static final String SEP = ",";
  
  
  private Analise analise;
  
  
  public BBMParser() {
    analise = null;
  }
  
  
  public Analise getLastParse() {
    return analise;
  }
  
  
  private boolean check(String s) {
    return s != null
        && !s.trim().isEmpty()
        && !s.contains("?");
  }
  
  
    //0         1        2        3           4
    //PROPOSTA, PREFIXO, PRODUTO, MODALIDADE, SITUACAO
    
    //5            6            7            8
    //DT_SITUACAO, DT_PROPOSTA, PRF_ANALISE, DT_ANALISE
    
    //9         10           11          12
    //PRF_NOTA, DT_INI_NOTA, DT_FIM_NOT, PRF_CAPAG
    
    //13            14            15          16       17
    //DT_INI_CAPAG, DT_FIM_CAPAG, CD_ESCALAO, CD_GRUP, VALOR
  public Analise parse(String line) {
    if(line == null || line.trim().isEmpty()) 
      return null;
    
    try {
    
      String[] vals = line.split(SEP);
      if(vals == null || vals.length < 18)
        return null;
    
      analise = new Analise();
    
      String oper = vals[0];
      if(!check(oper)) return null;
      oper = oper.trim().substring(1);
      analise.setOperacao(Integer.parseInt(oper));
    
      String age = oper.substring(0, 4);
      if(age.equals("0000")) return null;
      analise.setPrefixo(Integer.parseInt(age));
    
      String sit = vals[4].trim();
      if(!check(sit)) return null;
      if(sit.charAt(0) != 'F' && sit.charAt(0) != 'D')
        sit = sit.substring(1);
      
      if(!sit.contains("FORMALIZAD") 
          && !sit.contains("DESPACHAD"))
        return null;
      analise.setSituacao(sit);
    
      String sdate = vals[5].trim();
      if(!check(sdate)) return null;
      analise.setData(SimpleDate.parseDate(sdate));
    
      Produto p = new Produto();
      String sprod = vals[2].trim();
      if(!check(sprod)) return null;
      p.setCdprod(Integer.parseInt(sprod));
      
      String smod = vals[3].trim();
      if(!check(smod)) return null;
      p.setCdmod(Integer.parseInt(smod));
      analise.setProduto(p);
      
      String sinst = vals[9].trim();
      analise.setInstancia(
          Instancia.getInstancia(sinst));
      
      String sval = vals[17];
      if(!check(sval))
        analise.setValor(0);
      else
        analise.setValor((Double.parseDouble(sval) / 100));
      
      return analise;
      
    } catch(Exception e) { return null; }
  }

}
