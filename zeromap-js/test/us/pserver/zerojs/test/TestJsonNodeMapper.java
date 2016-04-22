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
import us.pserver.zerojs.JsonReader;
import us.pserver.zerojs.mapper.JsonNodeMapper1;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/04/2016
 */
public class TestJsonNodeMapper {

  
  public static void main(String[] args) throws IOException {
    String json = "{'hello': 'world', 'array': [1, 2, 3, 4], 'objs': [{'a': 0}, {'b': {'i': 9}}, {'c': 2}]}";
    json = 
        "{"
        +   "'str': 'def', "
        +   "'list': ['d', 'e', 'f'], "
        +   "'b': {"
        +     "'str': 'abc', "
        +     "'iarray': {"
        +       "'[I': [1, 2, 3]"
        +     "}, "
        +     "'bool': true"
        +   "}, "
        +   "'chars': {"
        +     "'java.util.LinkedHashMap': [{"
        +       "'entry#100': {"
        +         "'key': 'a', "
        +         "'value': 97, "
        +         "'class': 'java.lang.String|java.lang.Integer'"
        +       "}"
        +     "}, {"
        +       "'entry#101': {"
        +         "'key': 'b', "
        +         "'value': 98, "
        +         "'class': 'java.lang.String|java.lang.Integer'"
        +       "}"
        +     "}, {"
        +       "'entry#102': {"
        +         "'key': 'c', "
        +         "'value': 99, "
        +         "'class': 'java.lang.String|java.lang.Integer'"
        +       "}"
        +     "}, {"
        +       "'entry#103': {"
        +         "'key': 'd', "
        +         "'value': 100, "
        +         "'class': 'java.lang.String|java.lang.Integer'"
        +       "}"
        +     "}]"
        +   "}"
        + "}";
    System.out.println("* json = "+ json);
    JsonReader reader = JsonReader.defaultReader(new StringReader(json));
    JsonNodeMapper1 mapper = new JsonNodeMapper1();
    reader.addHandler(mapper);
    reader.read();
    System.out.println("* node =\n"+ mapper.getRoot());
  }
  
}
