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

package us.pserver.zerojs.test;

import java.io.IOException;
import java.io.StringReader;
import us.pserver.zerojs.mapper.JsonNodeMapper;
import us.pserver.zerojs.parse.JsonParser;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/04/2016
 */
public class TestJsonNodeMapper {

  
  public static void main(String[] args) throws IOException {
    //String json = "{'hello': 'world', 'array': [1, 2, 3, 4], 'objs': [{'a': 0}, {'b': {'i': 9}}, {'c': 2}]}";
    
    String json = "{\n" +
"  'b': {\n" +
"    'bool': true,\n" +
"    'iarray': {\n" +
"      '[I': [1, 2, 3]\n" +
"    },\n" +
"    'str': 'abc'\n" +
"  },\n" +
"  'chars': {\n" +
"    'java.util.LinkedHashMap': {\n" +
"      'entry#100': {\n" +
"        'class': 'java.lang.String|java.lang.Integer',\n" +
"        'key': 'd',\n" +
"        'value': 100\n" +
"      },\n" +
"      'entry#101': {\n" +
"        'class': 'java.lang.String|java.lang.Integer',\n" +
"        'key': 'e',\n" +
"        'value': 101\n" +
"      },\n" +
"      'entry#102': {\n" +
"        'class': 'java.lang.String|java.lang.Integer',\n" +
"        'key': 'f',\n" +
"        'value': 102\n" +
"      },\n" +
"      'entry#97': {\n" +
"        'class': 'java.lang.String|java.lang.Integer',\n" +
"        'key': 'a',\n" +
"        'value': 97\n" +
"      },\n" +
"      'entry#98': {\n" +
"        'class': 'java.lang.String|java.lang.Integer',\n" +
"        'key': 'b',\n" +
"        'value': 98\n" +
"      },\n" +
"      'entry#99': {\n" +
"        'class': 'java.lang.String|java.lang.Integer',\n" +
"        'key': 'c',\n" +
"        'value': 99\n" +
"      }\n" +
"    }\n" +
"  },\n" +
"  'list': {\n" +
"    'java.util.LinkedList|java.lang.Character': ['d', 'e', 'f']\n" +
"  },\n" +
"  'str': 'def'\n" +
"}";
    /**/
    //System.out.println("* json = "+ json);
    JsonParser reader = JsonParser.defaultReader(new StringReader(json));
    JsonNodeMapper mapper = new JsonNodeMapper();
    reader.addHandler(mapper);
    reader.parse();
    System.out.println("* node =\n"+ mapper.getRoot());
    
  }
  
}
