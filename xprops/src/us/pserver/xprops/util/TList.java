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

package us.pserver.xprops.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/07/2015
 */
public class TList implements StringTransformer<List> {
  
  @Override
  public List apply(String str) throws IllegalArgumentException {
    Valid v = Valid.off(str).testNull("Invalid String to Transform: ")
        .test(!str.contains(":") 
            || !str.contains("[") 
            || !str.contains("]"), 
            "Invalid String to Transform: "
        );
    int id = str.indexOf(":");
    int iss = str.indexOf("[", id);
    int ies = str.indexOf("]", iss);
    v.test(id < 0 || iss < 0 || ies < 0, 
        "Invalid String to Transform: "
    );
    TClass tc = new TClass();
    Class type = tc.apply(str.substring(0, id));
    TObject to = new TObject(type);
    List list = new ArrayList();
    str = str.substring(iss+1, ies);
    int i1 = 0, i2 = 0;
    while(true) {
      i2 = str.indexOf(",", i1);
      if(i2 < 0) i2 = str.length();
      String sub = str.substring(i1, i2);
      if(sub == null || sub.isEmpty())
        break;
      list.add(to.apply(sub));
      i1 = i2+1;
      if(i1 >= str.length()) break;
    }
    return list;
  }
  
  
  @Override
  public String back(List ls) throws IllegalArgumentException {
    Valid.off(ls).testNull(List.class);
    if(ls.isEmpty()) return "";
    Class type = ls.get(0).getClass();
    TObject to = new TObject(type);
    StringBuilder build = new StringBuilder();
    build.append(type.getName()).append(":[");
    for(int i = 0; i < ls.size(); i++) {
      build.append(to.back(ls.get(i)));
      if(i < ls.size()-1)
        build.append(",");
    }
    return build.append("]").toString();
  }
  
}
