package entitymappers;

import java.util.ArrayList;
import java.util.List;

public class User {
  public String userName;
  public List<String> roles = new ArrayList();
  public User(entity.User u) {
    this.userName = u.getUserName();
    for(entity.Role role: u.getRoles()){
      roles.add(role.getRoleName());
    }
  }
}
