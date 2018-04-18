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

package us.pserver.kumuluzee.hello2.jfs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2016
 */
public interface IFSize extends ByteValue {
  
  public static final IFSize ZERO = new FSize(0L);
  
  
  public double value();
  
  public Unit unit();
  
  
  public static IFSize from(Path p) {
    try {
      return new FSize(Files.size(p));
    } catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  public static IFSize from(BasicFileAttributes atts) {
    return new FSize(atts.size());
  }
  
  
  
  public enum Unit implements ByteValue {

    ZERO(0L),               // 0
    MIN(ZERO),              // 1
    BYTE(1L),               // 2
    KB(1024L),              // 3
    MB(KB.bytes() * 1024L), // 4
    GB(MB.bytes() * 1024L), // 5
    TB(GB.bytes() * 1024L), // 6
    PB(TB.bytes() * 1024L), // 7
    EB(PB.bytes() * 1024L), // 8
    MAX(Long.MAX_VALUE),    // 9
    UNKNOWN(Long.MIN_VALUE);// 10

    @Override
    public long bytes() {
      return bytes;
    }

    public boolean lt(Unit unit) {
      return this.compareTo(unit) < 0;
    }

    public boolean le(Unit unit) {
      return this.compareTo(unit) <= 0;
    }

    public boolean gt(Unit unit) {
      return this.compareTo(unit) > 0;
    }

    public boolean ge(Unit unit) {
      return this.compareTo(unit) >= 0;
    }

    public boolean eq(Unit unit) {
      return this.compareTo(unit) == 0;
    }

    public boolean lt(long bytes) {
      return this.bytes() < bytes;
    }

    public boolean le(long bytes) {
      return this.bytes() <= bytes;
    }

    public boolean gt(long bytes) {
      return this.bytes() > bytes;
    }

    public boolean ge(long bytes) {
      return this.bytes() >= bytes;
    }

    public boolean eq(long bytes) {
      return this.bytes() == bytes;
    }
    
    public Unit next() {
      Unit next = MAX;
      int cur = this.ordinal();
      if(cur >= 0 && cur < (Unit.values().length -2)) {
        next = Unit.values()[cur+1];
      }
      return next;
    }

    public Unit prev() {
      Unit prev = MIN;
      int cur = this.ordinal();
      if(cur > 1 && cur <= (Unit.values().length -2)) {
        prev = Unit.values()[cur-1];
      }
      return prev;
    }

    public static Unit from(long bytes) {
      if(bytes == 0) {
        return Unit.BYTE;
      }
      Unit un = MAX;
      while(un.gt(bytes)) {
        un = un.prev();
      }
      return un;
    }

    private final long bytes;

    private Unit(long bytes) {
      this.bytes = bytes;
    }

    private Unit(Unit unit) {
      this.bytes = unit.bytes();
    }

  }
  
  
}
