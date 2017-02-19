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

package us.pserver.sdb.filedriver.test;

import java.nio.ByteBuffer;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/02/2017
 */
public class JsonCreator {

  private final StringBuilder builder;
  
  private final int depth;
  
  private final int fields;
  
  private final int arrays;
  
  private final int objects;
  
  private final int arraySize;
  
  
  public JsonCreator() {
    builder = new StringBuilder();
    depth = 2;
    fields = 3;
    arrays = 1;
    objects = 2;
    arraySize = 5;
  }


  public JsonCreator(StringBuilder builder, int depth, int fields, int arrays, int objects, int arraySize) {
    this.builder = builder;
    this.depth = depth;
    this.fields = fields;
    this.arrays = arrays;
    this.objects = objects;
    this.arraySize = arraySize;
  }
  
  
  public JsonCreator withObjects(int objects) {
    return new JsonCreator(builder, depth, fields, arrays, objects, arraySize);
  }
  
  
  public JsonCreator withBuilder(StringBuilder builder) {
    return new JsonCreator(builder, depth, fields, arrays, objects, arraySize);
  }
  
  
  public JsonCreator withDepth(int depth) {
    return new JsonCreator(builder, depth, fields, arrays, objects, arraySize);
  }
  
  
  public JsonCreator withFields(int fields) {
    return new JsonCreator(builder, depth, fields, arrays, objects, arraySize);
  }
  
  
  public JsonCreator withArrays(int arrays) {
    return new JsonCreator(builder, depth, fields, arrays, objects, arraySize);
  }
  
  
  public JsonCreator withArraySize(int arraySize) {
    return new JsonCreator(builder, depth, fields, arrays, objects, arraySize);
  }


  public StringBuilder getBuilder() {
    return builder;
  }


  public int getDepth() {
    return depth;
  }


  public int getFields() {
    return fields;
  }


  public int getArrays() {
    return arrays;
  }


  public int getObjects() {
    return objects;
  }


  public int getArraySize() {
    return arraySize;
  }
  
  
  public String create() {
    objectStart();
    int realDepth = 0;
    int objSize = objects;
    try {
      realDepth = depth / objects;
    } catch(ArithmeticException e) {
      objSize = 0;
    }
    int num = (int) (Math.random() * 100);
    for(int i = num; i < (num+fields); i++) {
      field("f"+i);
      value(i+1);
      next();
    }
    num = (int) (Math.random() * 100);
    for(int i = num; i < (num+objSize); i++) {
      field("o"+i);
      builder.append(new JsonCreator(
          new StringBuilder(), 
          realDepth-1, 
          fields, 
          arrays, 
          realDepth-1, 
          arraySize
      ).create());
      next();
    }
    num = 0;
    for(int i = num; i < (num+arrays); i++) {
      field("a"+i);
      arrayStart();
      for(int j = 0; j < arraySize; j++) {
        value(j+1);
        if(j < arraySize-1) next();
      }
      arrayEnd();
      next();
    }
    builder.delete(builder.length() -2, builder.length());
    objectEnd();
    return builder.toString();
  }
  
  
  private void next() {
    builder.append(", ");
  }
  
  
  private void field(String f) {
    builder.append("\"").append(f).append("\": ");
  }
  
  
  private void value(String v) {
    builder.append("\"").append(v).append("\"");
  }
  
  
  private void value(Object o) {
    builder.append("\"").append(o).append("\"");
  }
  
  
  private void arrayStart() {
    builder.append("[");
  }
  
  
  private void arrayEnd() {
    builder.append("]");
  }
  
  
  private void objectStart() {
    builder.append("{");
  }
  
  
  private void objectEnd() {
    builder.append("}");
  }
  
  
  public ByteBuffer createBuffer() {
    byte[] bs = UTF8String.from(create()).getBytes();
    ByteBuffer buf = ByteBuffer.allocate(bs.length);
    buf.put(bs);
    return buf;
  }
  
  
  public static void main(String[] args) {
    System.out.println(new JsonCreator().withDepth(4).create());
  }
  
}
