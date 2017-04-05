package facades;

import entity.Book;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.NotFoundException;

public class BookFacade {

  EntityManagerFactory emf;

  public BookFacade(EntityManagerFactory emf) {
    this.emf = emf;
  }

  private EntityManager getEntityManager() {
    return emf.createEntityManager();
  }
  
  public List<Book> getBooks(){
    EntityManager em = getEntityManager();
    try{
      return em.createQuery("select b from Book b").getResultList();
    }finally{
      em.close();
    }
  }
  
    public Book getBook(int id){
    EntityManager em = getEntityManager();
    try{
      Book book = em.find(Book.class,id);
      if(book == null){
        throw new NotFoundException("No book with the provided id could be found");
      }
      return book;
    }finally{
      em.close();
    }
  }
  
  public Book addBook(String title, String info, String moreInfo){
    EntityManager em = getEntityManager();
    Book b = new Book(title,info,moreInfo);
    try{
      em.getTransaction().begin();
      em.persist(b);
      em.getTransaction().commit();
      return b;
    }finally{
      em.close();
    }
  }
  public Book addBook(Book book){
    EntityManager em = getEntityManager();
    try{
      em.getTransaction().begin();
      em.persist(book);
      em.getTransaction().commit();
      return book;
    }finally{
      em.close();
    }
  }
  
  public Book editBook(Book b){
    EntityManager em = getEntityManager();
    Book orgBook = em.find(Book.class, b.getId());
    if(orgBook == null){
      throw new NotFoundException("No book with the provided id was found");
    }
    orgBook.setTitle(b.getTitle());
    orgBook.setInfo(b.getInfo());
    orgBook.setMoreInfo(b.getMoreInfo());
    try{
      em.getTransaction().begin();
      em.merge(orgBook);
      em.getTransaction().commit();
      return b;
    }finally{
      em.close();
    }
  }
  
  public boolean deleteBook(int id){
    EntityManager em = getEntityManager();
    Book b = em.find(Book.class, id);
    if (b == null){
      throw new NotFoundException("No book with the provided id ("+id+") was found");
    }
    try{
      em.getTransaction().begin();
      em.remove(b);
      em.getTransaction().commit();
      return true;
    }finally{
      em.close();
    }
  }

}
