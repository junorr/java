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

package us.pserver.sdb;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/09/2014
 */
public class SimpleDB {

  public static final byte BYTE_INDEX_START = 35;
  
  public static final byte BYTE_BLOCK_START = 62;
  
  public static final byte BYTE_END = 10;
  
  
  private FileHandler handler;
  
  private List<Index> ids;
  
  
  public SimpleDB(String file) throws SDBException {
    if(file == null || file.isEmpty())
      throw new IllegalArgumentException("Invalid file: "+ file);
    try {
      handler = new FileHandler(file);
    } catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
    ids = new LinkedList<>();
  }
  
  
  private void init() {
    try {
      while(true) {
        byte b = handler.readByte();
        if(b == BYTE_INDEX_START) {
          String str = handler.readLine();
          ids.add(Index.fromXml(str));
          handler.nextBlock();
        }
        else {
          handler.seek(handler.position() -1);
          break;
        }
      }
    } catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
  
  public Document put(Document doc) {
    if(doc == null) return doc;
    try {
      if(doc.block() >= 0) {
        handler.seekBlock(doc.block());
        handler.deleteCurrentLine();
        handler.moveToEnd().nextBlock();
        doc.block(handler.getBlock());
        handler.writeLine(doc.toXml());
        return doc;
      }
      else {
        handler.moveToEnd().nextBlock();
        doc.block(handler.getBlock());
        handler.writeLine(doc.toXml());
        return doc;
      }
    }
    catch(IOException e) {
      throw new SDBException(e.getMessage(), e);
    }
  }
  
}
