
package facades;

import dbsetup.PU;
import entity.Book;
import javax.persistence.Persistence;


public class MakeTestBooks {
  
  public static void main(String[] args) {
    BookFacade f = new BookFacade(Persistence.createEntityManagerFactory(PU.getName(), null));
    f.addBook("Learn React", "Learn this cool framework", "And while you learn, learn a lot of ES 6 features");
    f.addBook("Learn MobX", "Learn this cool framework", "");
    for(Book b : f.getBooks()){
      System.out.println(b.getId()+", "+b.getTitle());
    }
  }
}
