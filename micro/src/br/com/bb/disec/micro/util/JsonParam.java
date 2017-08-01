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

package br.com.bb.disec.micro.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/07/2017
 */
public class JsonParam {

  private final Class[] types;
  
  private final URIParam pars;
  
  
  public JsonParam(Class[] tps, URIParam params) {
    this.types = tps;
    this.pars = params;
  }
  
  
  public Object[] getParams() {
    Object[] args = new Object[types.length];
    for(int i = 0; i < types.length && i < pars.length(); i++) {
      if(char.class.isAssignableFrom(types[i])
          || Character.class.isAssignableFrom(types[i])) {
        args[i] = pars.getParam(i).charAt(0);
      }
      else if(short.class.isAssignableFrom(types[i])
          || Short.class.isAssignableFrom(types[i])) {
        args[i] = pars.getNumber(i).shortValue();
      }
      else if(int.class.isAssignableFrom(types[i])
          || Integer.class.isAssignableFrom(types[i])) {
        args[i] = pars.getNumber(i).intValue();
      }
      else if(long.class.isAssignableFrom(types[i])
          || Long.class.isAssignableFrom(types[i])) {
        args[i] = pars.getNumber(i).longValue();
      }
      else if(double.class.isAssignableFrom(types[i])
          || Double.class.isAssignableFrom(types[i])) {
        args[i] = pars.getNumber(i).doubleValue();
      }
      else if(float.class.isAssignableFrom(types[i])
          || Float.class.isAssignableFrom(types[i])) {
        args[i] = pars.getNumber(i).floatValue();
      }
      else if(boolean.class.isAssignableFrom(types[i])
          || Boolean.class.isAssignableFrom(types[i])) {
        args[i] = pars.getBoolean(i);
      }
      else if(String.class.isAssignableFrom(types[i])) {
        args[i] = pars.getParam(i);
      }
      else if(List.class.isAssignableFrom(types[i])
          || Object[].class.isAssignableFrom(types[i])) {
        Object[] objs = pars.getObjectArgs();
        List ls = new ArrayList(objs.length);
        for(int j = 0; j < pars.length(); j++) {
          ls.add(objs[j]);
        }
        args[i] = (List.class.isAssignableFrom(types[i]) ? ls : ls.toArray());
      }
      else if(String[].class.isAssignableFrom(types[i])) {
        Object[] objs = pars.getObjectArgs();
        List ls = new ArrayList(objs.length);
        for(int j = 0; j < pars.length(); j++) {
          ls.add(objs[j]);
        }
        args[i] = ls.toArray(new String[ls.size()]);
      }
    }
    return args;
  }
  
}
