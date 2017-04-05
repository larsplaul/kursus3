package test;

import dbsetup.PU;
import facades.BookFacade;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;


public class TestBooks {
  
  public static void make(){
    TestBooks.main(null);
  }
  
  public static void main(String[] args) {
    System.out.println("Persistence UNIT USed: "+PU.getName());
    EntityManager em = Persistence.createEntityManagerFactory(PU.getName()).createEntityManager();
    
    try{
      em.getTransaction().begin();
      System.out.println("Deleted (from Book): "+em.createQuery("DELETE from Book").executeUpdate());
      em.getTransaction().commit();
    }
    
    finally{
      em.close();
    }
    BookFacade f = new BookFacade(Persistence.createEntityManagerFactory(PU.getName(), null));
    f.addBook("Learn React", "xxxx", "zzzz");
    f.addBook("Learn MobX", "yyyy", "xxxx");
    
  }
}