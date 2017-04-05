package facades;

import security.IUserFacade;
import entity.User;
import httpErrors.UserExistException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.NotFoundException;
import security.IUser;
import security.PasswordStorage;

public class UserFacade implements IUserFacade {

  EntityManagerFactory emf;

  public UserFacade(EntityManagerFactory emf) {
    this.emf = emf;
  }

  private EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  @Override
  public IUser getUserByUserId(String id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(User.class, id);
    } finally {
      em.close();
    }
  }

  /*
  Return the Roles if users could be authenticated, otherwise null
   */
  @Override
  public List<String> authenticateUser(String userName, String password) {
    IUser user = getUserByUserId(userName);
    boolean passwordOK = false;
    try {
      passwordOK = PasswordStorage.verifyPassword(password, user.getPassword());
    } catch (PasswordStorage.CannotPerformOperationException ex) {
      Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
    } catch (PasswordStorage.InvalidHashException ex) {
      Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
    }
    return user != null && passwordOK ? user.getRolesAsStrings() : null;
  }

  @Override
  public List<User> getAllUsers() {
     EntityManager em = getEntityManager();
     try {
      List<User> users = em.createQuery("select u from SEED_USER u").getResultList();
      return users;
    } finally {
      em.close();
    }
  }

  @Override
  public User addUser(User user) throws UserExistException {
    EntityManager em = getEntityManager();
     try {
      User exists = em.find(User.class, user.getUserName());
      if(exists != null){
        throw new UserExistException("This user name is taken");
      }
      em.getTransaction().begin();
      em.persist(user);
      em.getTransaction().commit();
      return user;
    } finally {
      em.close();
    }
  }
  @Override
  public boolean exist(String userName)  {
    EntityManager em = getEntityManager();
     try {
      User exists = em.find(User.class, userName);
      return exists != null;
    } finally {
      em.close();
    }
  }

  @Override
  public void removeUser(String id) {
    EntityManager em = getEntityManager();
     try {
      User user = em.find(User.class, id);
      if(user == null){
        throw new NotFoundException("No user with provided username found");
      }
      em.getTransaction().begin();
      em.remove(user);
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

}
