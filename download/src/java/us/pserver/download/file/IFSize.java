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

package us.pserver.download.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2016
 */
public interface IFSize {
  
  public static final IFSize ZERO = new FSize(0L);
  
  public long bytes();
  
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
  
  
  
  public enum Unit {

    ZERO(0L),
    BYTE(1L), 
    MIN(BYTE),
    KB(1024L), 
    MB(KB.bytes() * 1024L), 
    GB(MB.bytes() * 1024L), 
    TB(GB.bytes() * 1024L), 
    PB(TB.bytes() * 1024L), 
    EB(PB.bytes() * 1024L), 
    MAX(Long.MAX_VALUE),
    UNKNOWN(Long.MIN_VALUE);

    
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
      switch(this) {
        case BYTE:
          next = KB;
          break;
        case KB:
          next = MB;
          break;
        case MB:
          next = GB;
          break;
        case GB:
          next = TB;
          break;
        case TB:
          next = PB;
          break;
        case PB:
          next = EB;
          break;
        case EB:
          next = MAX;
          break;
        default:
          next = UNKNOWN;
          break;
      }
      return next;
    }


    public Unit prev() {
      Unit next = this;
      switch(next) {
        case KB:
          next = BYTE;
          break;
        case MB:
          next = KB;
          break;
        case GB:
          next = MB;
          break;
        case TB:
          next = GB;
          break;
        case PB:
          next = TB;
          break;
        case EB:
          next = PB;
          break;
        case MAX:
          next = EB;
          break;
        default:
          next = UNKNOWN;
          break;
      }
      return next;
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
