package dbsetup;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
//@WebListener  //This does not work with embedded Tomcat for testing. Must be declared in web.xml
public class DeploymentConfiguration implements ServletContextListener {
 
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    //Meant to used by an embedded Tomcat server for testing
    String pu = sce.getServletContext().getInitParameter("pu_name");
    if(pu != null){
      PU.setPU_Name(pu);
    }
    System.out.println("########### Using persistence unit: " + pu + "  ################");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
  }
}
