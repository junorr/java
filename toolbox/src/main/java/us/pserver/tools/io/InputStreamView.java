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

package us.pserver.tools.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/10/2018
 */
public class InputStreamView {
  
  public static final int DEFAULT_BUFFER_SIZE = 32*1024;
  
  public static final int MIN_BUFFER_SIZE = 128;
  
  //private final InputStream input;
  
  //private final IBuffer buffer;
  
  private long total;
  
  
  //public InputStreamView(InputStream in, int bufsize) {
    //this.input = Objects.requireNonNull(in, "Bad null InputStream");
    //if(bufsize < MIN_BUFFER_SIZE) {
      //throw new IllegalArgumentException("Bad buffer size: "+ bufsize+ "(<"+ MIN_BUFFER_SIZE+ ")");
    //}
    //this.buffer = new Buffer(bufsize);
    //total = 0;
  //}
  
  //public InputStreamView(InputStream in) {
    //this(in, DEFAULT_BUFFER_SIZE);
  //}
  
  
  //public long find(IBuffer cont) throws IOException {
    //if(cont == null || cont.size() < 1) {
      //throw new IllegalArgumentException("Bad Buffer: "+ (cont == null ? cont : cont.size()));
    //}
    //int idx = -1;
    //do {
      ////System.out.printf("... find( %s ): buffer=%s - %s%n", cont, buffer, buffer.toUTF8String());
      //if(!buffer.isEmpty()) {
        //idx = buffer.indexOf(cont);
        //if(idx >= 0) {
          //buffer.fill(buffer.toByteArray(), idx, buffer.size() - idx);
          //return idx;
        //}
      //}
    //} while(buffer.fill(input) != -1);
    //return -1;
  //}
  
  
  //public long skip(long bytes) throws IOException {
    //long count = 0;
    //do {
      //if(!buffer.isEmpty()) {
        //if(bytes - count > buffer.size()) {
          //count += buffer.size();
        //}
        //else {
          //int len = (int)Math.min(bytes - count, buffer.size());
          //buffer.fill(buffer.toByteArray(), len, buffer.size() - len);
          //count += len;
          //break;
        //}
      //}
    //} while(buffer.fill(input) != -1);
    //return count;
  //}
  
  
  //public long flushUntil(OutputStream out, IBuffer cont) throws IOException {
    //Objects.requireNonNull(out, "Bad null OutputStream");
    //Objects.requireNonNull(cont, "Bad null Buffer");
    //int idx = -1;
    //long count = 0;
    //do {
      //if(!buffer.isEmpty()) {
        //idx = buffer.indexOf(cont);
        //int len = (idx >= 0 ? idx : buffer.size());
        //if(len > 0) {
          //count += len;
          //buffer.writeTo(out, len);
          //if(buffer.size() - len > 0) {
            //buffer.fill(buffer.toByteArray(), len, buffer.size() - len);
          //}
        //}
      //}
    //} while(idx < 0 && buffer.fill(input) != -1);
    //return count;
  //}
  
  
  //public IBuffer cacheUntil(IBuffer cont) throws IOException {
    //Objects.requireNonNull(cont, "Bad null Buffer");
    //ExpansibleBuffer cache = new ExpansibleBuffer(buffer.capacity());
    //int idx = -1;
    //do {
      //if(!buffer.isEmpty()) {
        //idx = buffer.indexOf(cont);
        //int len = (idx >= 0 ? idx : buffer.size());
        //if(len > 0) {
          //cache.fill(buffer, len);
          //buffer.fill(buffer.toByteArray(), len, buffer.size() - len);
          ////System.out.printf("... cacheUntil( %s ): buffer=%s - %s", cont, buffer, buffer.toUTF8String());
        //}
      //}
    //} while(idx < 0 && buffer.fill(input) != -1);
    //return cache;
  //}
  
}
