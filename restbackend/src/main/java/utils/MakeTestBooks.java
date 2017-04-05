
package utils;


import dbsetup.PU;
import entity.Book;
import facades.BookFacade;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;


public class MakeTestBooks {
  
  public static void main(String[] args) {
    
    EntityManager em = Persistence.createEntityManagerFactory(PU.getName()).createEntityManager();
    
    try{
      em.getTransaction().begin();
      System.out.println("Deleted (from Book): "+em.createQuery("DELETE from Book").executeUpdate());
      em.getTransaction().commit();
    }
    
    finally{
      em.close();
    }
    BookFacade f = new BookFacade(Persistence.createEntityManagerFactory(PU.getName()));
    f.addBook("Learn AAA", "xxxx", "zzzz");
    f.addBook("Learn BBB", "yyyy", "xxxx");
    
  }
}