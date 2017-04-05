package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Column(length = 40)
  private String title;
  
  @Column(length = 100)
  private String info;
  
  private String moreInfo;

  public Book(String title, String info, String moreInfo) {
    this.title = title;
    this.info = info;
    this.moreInfo = moreInfo;
  }
  public Book(String title, String info) {
    this.title = title;
    this.info = info;
    this.moreInfo = null;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getMoreInfo() {
    return moreInfo;
  }

  public void setMoreInfo(String moreInfo) {
    this.moreInfo = moreInfo;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }
  

  public Book() {
  }
  
  
  

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

 
  
}
