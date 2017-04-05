package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dbsetup.PU;
import entity.Book;
import facades.BookFacade;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("book")
public class BookResource {

  @Context
  private UriInfo context;
  
  private Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private BookFacade facade = new BookFacade(Persistence.createEntityManagerFactory(PU.getName()));

  public BookResource() {
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getBooks() {
   return gson.toJson(facade.getBooks());
  }
  
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String getBook(@PathParam("id") int id) {
   return gson.toJson(facade.getBook(id));
  }

  @RolesAllowed("User")  
  @DELETE
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteBook(@PathParam("id") int id) {
   int status = facade.deleteBook(id) ? 204 : 404;  //Return no content for ok, otherwise not found (id)
   return Response.status(status).build();
  }
  
  @RolesAllowed("User")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String addBook(String json){
    Book book = gson.fromJson(json, Book.class);
    Book bookNew = facade.addBook(book);
    return gson.toJson(bookNew, Book.class);
  }
  
  
  

  /**
   * PUT method for updating or creating an instance of BookResource
   * @param content representation for the resource
   */
  @RolesAllowed("User")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public String editBook(String json){
    Book book = gson.fromJson(json, Book.class);
    Book bookEdited = facade.editBook(book);
    return gson.toJson(bookEdited, Book.class);
  }
}
