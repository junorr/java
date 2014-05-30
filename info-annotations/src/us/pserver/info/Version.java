/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package us.pserver.info;

/**
 * <p style="font-size: medium;">
 * Anotação de classe para prover informações sobre a
 * versões e implementação de classes e métodos.
 * <br><br>
 * Forma de Uso:
 * <br><br>
 * </p>
 * <code>&#64;Version (</code>
 * <pre>
 *    value    = "3.0",
 *    name     = "SimpleDate",
 *    date     = "2011.11.16",
 *    author   = "Juno Roesler",
 *    synopsis = "Armazenamento e manipulação de datas",
 *    old      = {
 *       "2.1 [2011.03.29]",
 *       "2.0 [2011.03.28]", 
 *       "1.2 [2011.03.28]",
 *       "1.1 [2011.03.25]", 
 *       "1.0 [2011.03.25]"
 *    }
 * )
 * </pre>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.03.29
 */
public @interface Version {
	
	String value();
  
  String name()     default "[unknown]";

	String author()		default "[unknown]";
	
	String date()			default "[unknown]";
	
	String synopsis()	default "[unknown]";
	
	String[] old()		default "";
	
}
