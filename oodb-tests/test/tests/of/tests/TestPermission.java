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

package tests.of.tests;

import java.util.Set;
import oodb.tests.beans.PermEntity;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public class TestPermission {
  
  public static void main(String[] args) {
    int perm = 777;
    System.out.println("* perm="+ perm);
    Set<PermEntity> prs = PermEntity.fromCode(String.valueOf(perm));
    prs.forEach(System.out::println);
    perm = PermEntity.toCode(prs);
    System.out.println("* perm="+ perm);
    System.out.println("* code="+ PermEntity.toStringCode(prs));
    System.out.println("* strn="+ PermEntity.toString(prs));
    prs = PermEntity.fromCode(perm);
    prs.forEach(System.out::println);
    System.out.println();
    
    perm = 600;
    System.out.println("* perm="+ perm);
    prs = PermEntity.fromCode(String.valueOf(perm));
    prs.forEach(System.out::println);
    perm = PermEntity.toCode(prs);
    System.out.println("* perm="+ perm);
    System.out.println("* code="+ PermEntity.toStringCode(prs));
    System.out.println("* strn="+ PermEntity.toString(prs));
    prs = PermEntity.fromCode(perm);
    prs.forEach(System.out::println);
    System.out.println();
    
    perm = 493;
    System.out.println("* perm="+ perm);
    prs = PermEntity.fromCode(perm);
    prs.forEach(System.out::println);
    perm = PermEntity.toCode(prs);
    System.out.println("* perm="+ perm);
    System.out.println("* code="+ PermEntity.toStringCode(prs));
    System.out.println("* strn="+ PermEntity.toString(prs));
    prs = PermEntity.fromCode(perm);
    prs.forEach(System.out::println);
    System.out.println();
    
    perm = 751;
    System.out.println("* perm="+ perm);
    prs = PermEntity.fromCode(String.valueOf(perm));
    prs.forEach(System.out::println);
    perm = PermEntity.toCode(prs);
    System.out.println("* perm="+ perm);
    System.out.println("* code="+ PermEntity.toStringCode(prs));
    System.out.println("* strn="+ PermEntity.toString(prs));
    prs = PermEntity.fromCode(perm);
    prs.forEach(System.out::println);
    System.out.println();
    
    perm = 650;
    System.out.println("* perm="+ perm);
    prs = PermEntity.fromCode(String.valueOf(perm));
    prs.forEach(System.out::println);
    perm = PermEntity.toCode(prs);
    System.out.println("* perm="+ perm);
    System.out.println("* code="+ PermEntity.toStringCode(prs));
    System.out.println("* strn="+ PermEntity.toString(prs));
    prs = PermEntity.fromCode(perm);
    prs.forEach(System.out::println);
    System.out.println();
    
    perm = 100;
    System.out.println("* perm="+ perm);
    prs = PermEntity.fromCode(String.valueOf(perm));
    prs.forEach(System.out::println);
    perm = PermEntity.toCode(prs);
    System.out.println("* perm="+ perm);
    System.out.println("* code="+ PermEntity.toStringCode(prs));
    System.out.println("* strn="+ PermEntity.toString(prs));
    prs = PermEntity.fromCode(perm);
    prs.forEach(System.out::println);
    System.out.println();
    
    perm = 1;
    System.out.println("* perm="+ perm);
    prs = PermEntity.fromCode(String.valueOf(perm));
    prs.forEach(System.out::println);
    perm = PermEntity.toCode(prs);
    System.out.println("* perm="+ perm);
    System.out.println("* code="+ PermEntity.toStringCode(prs));
    System.out.println("* strn="+ PermEntity.toString(prs));
    prs = PermEntity.fromCode(perm);
    prs.forEach(System.out::println);
    System.out.println();
    
    perm = 1001;
    System.out.println("* perm="+ perm);
    prs = PermEntity.fromCode(perm);
    prs.forEach(System.out::println);
    perm = PermEntity.toCode(prs);
    System.out.println("* perm="+ perm);
    System.out.println("* code="+ PermEntity.toStringCode(prs));
    System.out.println("* strn="+ PermEntity.toString(prs));
    prs = PermEntity.fromCode(perm);
    prs.forEach(System.out::println);
    System.out.println();
  }
  
}