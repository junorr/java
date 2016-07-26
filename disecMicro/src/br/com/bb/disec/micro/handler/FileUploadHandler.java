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

package br.com.bb.disec.micro.handler;

import br.com.bb.disec.micro.util.FileSize;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormData.FormValue;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.Headers;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class FileUploadHandler implements HttpHandler {
  
  public static final String DEFAULT_UPLOAD_CONFIG = "/resources/fileupload.json";
  
  private final FileUploadConfig config;
  
  
  public FileUploadHandler() {
    try {
      config = FileUploadConfig.builder()
          .load(getClass().getResource(DEFAULT_UPLOAD_CONFIG))
          .build();
    }
    catch(IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
  
  
  public FileUploadConfig getFormData() {
    return config;
  }
  

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    hse.startBlocking();
    FormParserFactory.Builder fbd = FormParserFactory.builder(true);
    fbd.setDefaultCharset("UTF-8");
    FormDataParser fp = fbd.build().createParser(hse);
    FormData data = fp.parseBlocking();
    JsonArray resp = new JsonArray();
    for(String field : data) {
      JsonObject json = new JsonObject();
      FormValue value = data.getFirst(field);
      if(value.isFile()) {
        File f = value.getPath().toFile();
        if(f.length() > config.getMaxSize().getSize()) {
          String msg = "Bad Request. Size Overflow (maxSize: "+ config.getMaxSize()+ ")";
          hse.setStatusCode(400).setReasonPhrase(msg);
          json.addProperty("success", Boolean.FALSE);
          json.addProperty("message", msg);
          json.addProperty("name", value.getFileName());
          json.addProperty("length", new FileSize(f.length()).toString());
        }
        else if(!config.getAllowedExtensions()
            .stream().filter(e->value.getFileName()
                .toLowerCase().endsWith(e.toLowerCase())
            ).findAny().isPresent()) {
          String msg = "Bad Request. Extension Not Allowed ("+ config.getAllowedExtensions().toString()+ ")";
          hse.setStatusCode(400).setReasonPhrase(msg);
          json.addProperty("success", Boolean.FALSE);
          json.addProperty("message", msg);
          json.addProperty("name", value.getFileName());
          json.addProperty("length", new FileSize(f.length()).toString());
        }
        else {
          hse.setStatusCode(200).setReasonPhrase("OK");
          json.addProperty("success", Boolean.TRUE);
          json.addProperty("message", "OK");
          json.addProperty("name", value.getFileName());
          json.addProperty("length", new FileSize(f.length()).toString());
          Files.move(
              value.getPath(), 
              config.getUploadDir(), 
              StandardCopyOption.REPLACE_EXISTING
          );
        }
        resp.add(json);
      }//isFile
    }//for
    hse.getResponseHeaders().put(
        Headers.CONTENT_TYPE, "application/json; charset=utf-8"
    );
    hse.getResponseSender().send(new GsonBuilder()
        .setPrettyPrinting().create().toJson(resp)
    );
    hse.endExchange();
  }
    
}
