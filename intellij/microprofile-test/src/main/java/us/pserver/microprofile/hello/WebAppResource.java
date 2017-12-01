package us.pserver.microprofile.hello;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Path("/res")
public class WebAppResource {
  
  public static final java.nio.file.Path WEBAPP_PATH = Paths.get("./webapp/");
  
  public static final String DEFAULT_RESOURCE = "index.html";
  
  
  @GET
  @Path("{resource:.*}")
  public InputStream getResource(@PathParam("resource") String name) throws IOException {
    return Files.newInputStream(WEBAPP_PATH.resolve(
        (name.isEmpty() ? DEFAULT_RESOURCE : name)),
        StandardOpenOption.READ
    );
  }
  
  @GET
  @Path("{res1}/{res2}")
  public InputStream getResource(
      @PathParam("res1") String res1,
      @PathParam("res2") String res2) throws IOException {
    return Files.newInputStream(
        WEBAPP_PATH.resolve(res1).resolve(res2),
        StandardOpenOption.READ
    );
  }
  
  @GET
  @Path("{res1}/{res2}/{res3}")
  public InputStream getResource(
      @PathParam("res1") String res1,
      @PathParam("res2") String res2,
      @PathParam("res3") String res3) throws IOException {
    return Files.newInputStream(
        WEBAPP_PATH.resolve(res1).resolve(res2).resolve(res3),
        StandardOpenOption.READ
    );
  }
  
  @GET
  @Path("{res1}/{res2}/{res3}/{res4}")
  public InputStream getResource(
      @PathParam("res1") String res1,
      @PathParam("res2") String res2,
      @PathParam("res3") String res3,
      @PathParam("res4") String res4) throws IOException {
    return Files.newInputStream(
        WEBAPP_PATH.resolve(res1).resolve(res2).resolve(res3).resolve(res4),
        StandardOpenOption.READ
    );
  }
  
}
