package cz.muni.fi.sdipr.web;

/**
 * @author matejkobza
 */
public interface OAuthAbstractBean {

  final static String APPLICATION_NAME = "gtin";
  
  /**
   * Create your own method for isAuthenticated user
   *
   * @return true if user is authenticated false otherwise
   */
  public Boolean getIsAuthenticated();

}
