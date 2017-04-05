package entitymappers;

import entity.User;
import java.util.ArrayList;
import java.util.List;

public class Users {

  public List<entitymappers.User> users = new ArrayList();

  public Users(List<User> all) {
    for (entity.User u : all) {
      users.add(new entitymappers.User(u));
    }
  }
}
