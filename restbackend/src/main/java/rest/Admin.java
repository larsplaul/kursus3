package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Role;
import httpErrors.UserExistException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import security.IUserFacade;
import security.PasswordStorage;
import security.UserFacadeFactory;

@Path("users")

public class Admin {
   
  IUserFacade facade = UserFacadeFactory.getInstance();
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  
  @RolesAllowed("Admin")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getAll(){
    entitymappers.Users all = new entitymappers.Users(facade.getAllUsers());
    return gson.toJson(all);
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("check/{userName}")
  public Response doesExist(@PathParam("userName") String userName) throws UserExistException{
    boolean res = facade.exist(userName);
    if(res ){
      throw new UserExistException("This username is taken");
    }
    return Response.status(204).build();
  }
  
  @RolesAllowed("Admin")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String addUser(String userStr) throws PasswordStorage.CannotPerformOperationException, UserExistException{
    entitymappers.UserPW user = gson.fromJson(userStr, entitymappers.UserPW.class);
    entity.User entityUser = new entity.User(user.userName,user.password);
    for(String role : user.roles){
      entityUser.addRole(new Role(role));
    }
    entity.User addedUser = facade.addUser(entityUser);
    entitymappers.User userToReturn = new entitymappers.User(addedUser);
    return gson.toJson(userToReturn);
  }
  
  @RolesAllowed("Admin")
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{id}")
  public Response delete(@PathParam("id") String id){
    facade.removeUser(id);
    return Response.status(Response.Status.NO_CONTENT).build();
  }
 
}
