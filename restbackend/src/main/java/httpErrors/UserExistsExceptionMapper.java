package httpErrors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import javax.servlet.ServletContext;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;



  
@Provider
public class UserExistsExceptionMapper implements ExceptionMapper<UserExistException> {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Context
  ServletContext context;

  @Override
  public Response toResponse(UserExistException ex) {
    JsonObject error = new JsonObject();
    JsonObject errorDetail = new JsonObject();
    String msg = ex.getMessage();
    msg = (msg == null || msg.equals(""))?"This User Name was taken" : msg;
    errorDetail.addProperty("message", msg);
    error.add("error", errorDetail);
    return Response.status(400).entity(gson.toJson(error)).type(MediaType.APPLICATION_JSON).build();
  }
}