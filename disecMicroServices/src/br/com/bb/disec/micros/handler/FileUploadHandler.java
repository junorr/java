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

package br.com.bb.disec.micros.handler;

import br.com.bb.disec.micro.ServerSetup;
import br.com.bb.disec.micros.conf.FileUploadConfig;
import br.com.bb.disec.micros.util.FileSize;
import br.com.bb.disec.micro.handler.JsonHandler;
import br.com.bb.disec.micro.util.URIParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormData.FormValue;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.Methods;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class FileUploadHandler implements JsonHandler {
  
  private final Gson gson;
  
  
  public FileUploadHandler() {
    gson = new GsonBuilder().setPrettyPrinting().create();
  }
  
  
  private String getExtFileName(FormValue value) {
    String fname = value.getFileName();
    String ext = fname.substring(fname.lastIndexOf("."));
    return "_" + LocalDateTime.now().toString()
        .replace("-", "")
        .replace(":", "")
        .replace(".", "") 
        + ext;
  }
  
  
  private JsonObject buildJson(boolean success, String msg, String name, String path, long size) {
    JsonObject json = new JsonObject();
    json.addProperty("success", success);
    json.addProperty("message", Objects.toString(msg));
    json.addProperty("name", Objects.toString(name));
    json.addProperty("path", Objects.toString(path));
    FileSize fs = (size > 0 ? new FileSize(size) : null);
    json.addProperty("length", Objects.toString(fs));
    return json;
  }
  
  
  private boolean isUploadAllowed(FileUploadConfig fuck, FormValue value) throws IOException {
    File f = value.getPath().toFile();
    if(f.length() > fuck.getMaxSize().getSize()) {
      throw new IOException("Max Size Exceeded ("+ fuck.getMaxSize()+ ")");
    }
    else if(!fuck.getAllowedExtensions().isEmpty() 
        && !fuck.getAllowedExtensions()
            .stream().filter(e->value.getFileName()
            .toLowerCase().endsWith(e.toLowerCase()))
            .findAny().isPresent()) {
      throw new IOException("Extension Not Allowed ("+ fuck.getAllowedExtensions()+ ")");
    }
    return true;
  }
  
  
  private FileUploadConfig readConfig(URIParam pars) throws IOException {
    if(pars.length() < 2) {
      throw new IOException("Missing upload Group/Name");
    }
    FileUploadConfig fuc = FileUploadConfig
        .builder()
        .load(pars.getParam(0), pars.getParam(1))
        .build();
    return fuc;
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    if(!Methods.POST.equals(hse.getRequestMethod())) {
      hse.setStatusCode(400).endExchange();
      return;
    }
    hse.startBlocking();
    JsonArray resp = new JsonArray();
    URIParam pars = new URIParam(hse.getRequestURI());
    FileUploadConfig fuck = null;
    try {
      fuck = readConfig(pars);
      System.out.println("[/upload] "+ fuck);
    } catch(IOException e) {
      e.printStackTrace();
      hse.setStatusCode(400);
      hse.endExchange();
      return;
    }
    if(fuck != null) {
      FormParserFactory.Builder fact = FormParserFactory.builder(true);
      fact.setDefaultCharset("UTF-8");
      FormDataParser fdp = fact.build().createParser(hse);
      FormData data = fdp.parseBlocking();
      for(String field : data) {
        resp.add(this.doUpload(
            fuck, data.getFirst(field), 
            pars.getParam(0) + "-" + pars.getParam(1))
        );
      }//for
    }//fuck != null
    this.setStatus(hse, resp);
    this.putJsonHeader(hse);
    System.out.println("[/upload] response: "+ gson.toJson(resp));
    hse.getResponseSender().send(gson.toJson(resp) + "\n");
    hse.endExchange();
  }
  
  
  private void setStatus(HttpServerExchange hse, JsonArray array) {
    boolean success = StreamSupport.stream(array.spliterator(), false)
        .filter(e->e.getAsJsonObject()
            .get("success").getAsBoolean()
        ).findAny().isPresent();
    if(!success) {
      hse.setStatusCode(400).setReasonPhrase("Bad Request");
    }
  }
  
  
  private JsonElement doUpload(FileUploadConfig fuck, FormValue value, String fileName) {
    JsonElement elt = null;
    long fsize = 0;
    if(value.isFile()) {
      try {
        if(isUploadAllowed(fuck, value)) {
          fsize = Files.size(value.getPath());
          Path dest = fuck.getUploadDir().resolve(
              fileName + getExtFileName(value)
          );
          Files.move(
              value.getPath(), dest, 
              StandardCopyOption.REPLACE_EXISTING
          );
          elt = buildJson(true, "OK", 
              value.getFileName(), 
              dest.toString(), fsize
          );
        }
      } catch(IOException e) {
        e.printStackTrace();
        elt = buildJson(false, 
            e.getMessage(), 
            value.getFileName(), 
            value.getPath().toString(), 
            fsize
        );
      }
    }
    return elt;
  }
  
}
