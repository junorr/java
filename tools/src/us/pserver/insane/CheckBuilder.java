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

package us.pserver.insane;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.function.Predicate;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/05/2016
 */
public interface CheckBuilder<T> {
  
  public Sane<T> isNotNull();
  
  public Sane<T> isEquals(Object obj);
  
  public Sane<T> isEqualsIgnoreCase(String str);
  
  public Sane<T> startsWith(String str);
  
  public Sane<T> endsWith(String str);
  
  public Sane<T> contains(String str);
  
  public Sane<T> isNotEmpty();
  
  public Sane<T> contains(Object ... objs);
  
  public Sane<T> contains(Collection list);
  
  public Sane<T> containsKey(Object obj);
  
  public Sane<T> containsValue(Object obj);
  
  public Sane<T> isNotEmptyMap();
  
  public Sane<T> isNotEmptyCollection();
  
  public Sane<T> isNotEmptyArray();
  
  public Sane<T> isGreaterThan(Number n);
  
  public Sane<T> isGreaterEqualsTo(Number n);
  
  public Sane<T> isLesserThan(Number n);
  
  public Sane<T> isLesserEqualsTo(Number n);
  
  public Sane<T> isBetween(Number min, Number max);
  
  public Sane<T> isModOf(Number n);
  
  public Sane<T> isGreaterThan(Date d);
  
  public Sane<T> isGreaterEqualsTo(Date d);
  
  public Sane<T> isLesserThan(Date d);
  
  public Sane<T> isLesserEqualsTo(Date d);
  
  public Sane<T> isBetween(Date min, Date max);
  
  public Sane<T> isGreaterThan(Instant d);
  
  public Sane<T> isGreaterEqualsTo(Instant d);
  
  public Sane<T> isLesserThan(Instant d);
  
  public Sane<T> isLesserEqualsTo(Instant d);
  
  public Sane<T> isBetween(Instant min, Instant max);
  
  public Sane<T> predicate(Predicate<T> pred);
  
  
  
  
  
  public static final class DefCheckBuilder<T> implements CheckBuilder<T> {
    
    private final Sane<T> sane;
    
    private SanityCheck<T> check;
    
    
    public DefCheckBuilder() {
      this.sane = null;
    }
    
    
    public DefCheckBuilder(Sane<T> s) {
      this.sane = Sane.of(s).get(Checkup.isNotNull());
    }
    


    @Override
    public Sane<T> isNotNull() {
      return sane.with(Checkup.isNotNull());
    }


    @Override
    public Sane<T> isEquals(Object obj) {
      return sane.with(Checkup.isEquals(obj));
    }


    @Override
    public Sane<T> isEqualsIgnoreCase(String str) {
      return sane.with((SanityCheck<T>) Checkup.isEqualsIgnoreCase(str));
    }


    @Override
    public Sane<T> startsWith(String str) {
      return sane.with((SanityCheck<T>) Checkup.startsWith(str));
    }


    @Override
    public Sane<T> endsWith(String str) {
      return sane.with((SanityCheck<T>) Checkup.endsWith(str));
    }


    @Override
    public Sane<T> contains(String str) {
      return sane.with((SanityCheck<T>) Checkup.contains(str));
    }


    @Override
    public Sane<T> isNotEmpty() {
      return sane.with((SanityCheck<T>) Checkup.isNotEmpty());
    }


    @Override
    public Sane<T> contains(Object... objs) {
      return sane.with((SanityCheck<T>) Checkup.contains(objs));
    }


    @Override
    public Sane<T> contains(Collection list) {
      return sane.with((SanityCheck<T>) Checkup.contains(list));
    }


    @Override
    public Sane<T> containsKey(Object obj) {
      return sane.with((SanityCheck<T>) Checkup.containsKey(obj));
    }


    @Override
    public Sane<T> containsValue(Object obj) {
      return sane.with((SanityCheck<T>) Checkup.containsValue(obj));
    }


    @Override
    public Sane<T> isNotEmptyMap() {
      return sane.with((SanityCheck<T>) Checkup.isNotEmptyMap());
    }


    @Override
    public Sane<T> isNotEmptyCollection() {
      return sane.with((SanityCheck<T>) Checkup.isNotEmptyCollection());
    }


    @Override
    public Sane<T> isNotEmptyArray() {
      return sane.with((SanityCheck<T>) Checkup.isNotEmptyArray());
    }


    @Override
    public Sane<T> isGreaterThan(Number n) {
      return sane.with((SanityCheck<T>) Checkup.isGreaterThan(n));
    }


    @Override
    public Sane<T> isGreaterEqualsTo(Number n) {
      return sane.with((SanityCheck<T>) Checkup.isGreaterEqualsTo(n));
    }


    @Override
    public Sane<T> isLesserThan(Number n) {
      return sane.with((SanityCheck<T>) Checkup.isLesserThan(n));
    }


    @Override
    public Sane<T> isLesserEqualsTo(Number n) {
      return sane.with((SanityCheck<T>) Checkup.isLesserEqualsTo(n));
    }


    @Override
    public Sane<T> isBetween(Number min, Number max) {
      return sane.with((SanityCheck<T>) Checkup.isBetween(min, max));
    }


    @Override
    public Sane<T> isModOf(Number n) {
      return sane.with((SanityCheck<T>) Checkup.isModOf(n));
    }


    @Override
    public Sane<T> isGreaterThan(Date d) {
      return sane.with((SanityCheck<T>) Checkup.isGreaterThan(d));
    }


    @Override
    public Sane<T> isGreaterEqualsTo(Date d) {
      return sane.with((SanityCheck<T>) Checkup.isGreaterEqualsTo(d));
    }


    @Override
    public Sane<T> isLesserThan(Date d) {
      return sane.with((SanityCheck<T>) Checkup.isLesserThan(d));
    }


    @Override
    public Sane<T> isLesserEqualsTo(Date d) {
      return sane.with((SanityCheck<T>) Checkup.isLesserEqualsTo(d));
    }


    @Override
    public Sane<T> isBetween(Date min, Date max) {
      return sane.with((SanityCheck<T>) Checkup.isBetween(min, max));
    }


    @Override
    public Sane<T> isGreaterThan(Instant d) {
      return sane.with((SanityCheck<T>) Checkup.isGreaterThan(d));
    }


    @Override
    public Sane<T> isGreaterEqualsTo(Instant d) {
      return sane.with((SanityCheck<T>) Checkup.isGreaterEqualsTo(d));
    }


    @Override
    public Sane<T> isLesserThan(Instant d) {
      return sane.with((SanityCheck<T>) Checkup.isLesserThan(d));
    }


    @Override
    public Sane<T> isLesserEqualsTo(Instant d) {
      return sane.with((SanityCheck<T>) Checkup.isLesserEqualsTo(d));
    }


    @Override
    public Sane<T> isBetween(Instant min, Instant max) {
      return sane.with((SanityCheck<T>) Checkup.isBetween(min, max));
    }


    @Override
    public Sane<T> predicate(Predicate<T> pred) {
      return sane.with((SanityCheck<T>) Checkup.of(pred));
    }
    
  }
  
}
