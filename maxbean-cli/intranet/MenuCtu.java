package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.menu_ctu]
 */
public class MenuCtu implements IMenuCtu {

  private java.lang.Integer cdCtuMenu;
  private java.lang.Integer cdParent;
  private java.lang.Integer cdCtu;
  private java.lang.Integer cdItemMenu;
  private java.lang.String href;

  public MenuCtu() {}

  public MenuCtu(
      java.lang.Integer cdCtuMenu, 
      java.lang.Integer cdParent, 
      java.lang.Integer cdCtu, 
      java.lang.Integer cdItemMenu, 
      java.lang.String href) {
    this.cdCtuMenu = cdCtuMenu;
    this.cdParent = cdParent;
    this.cdCtu = cdCtu;
    this.cdItemMenu = cdItemMenu;
    this.href = href;
  }

  /**
   * Get the value relative to the database
   * column [cd_ctu_menu: int].
   * @return The value of the column [cd_ctu_menu].
   */
  @Override
  public java.lang.Integer getCdCtuMenu() {
    return cdCtuMenu;
  }

  /**
   * Set the value relative to the database
   * column [cd_ctu_menu: int].
   * @param cdCtuMenu The value of the column [cd_ctu_menu].
   * @return This modified object instance.
   */
  @Override
  public MenuCtu setCdCtuMenu( java.lang.Integer cdCtuMenu ) {
    this.cdCtuMenu = cdCtuMenu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_parent: int].
   * @return The value of the column [cd_parent].
   */
  @Override
  public java.lang.Integer getCdParent() {
    return cdParent;
  }

  /**
   * Set the value relative to the database
   * column [cd_parent: int].
   * @param cdParent The value of the column [cd_parent].
   * @return This modified object instance.
   */
  @Override
  public MenuCtu setCdParent( java.lang.Integer cdParent ) {
    this.cdParent = cdParent;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_ctu: int].
   * @return The value of the column [cd_ctu].
   */
  @Override
  public java.lang.Integer getCdCtu() {
    return cdCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_ctu: int].
   * @param cdCtu The value of the column [cd_ctu].
   * @return This modified object instance.
   */
  @Override
  public MenuCtu setCdCtu( java.lang.Integer cdCtu ) {
    this.cdCtu = cdCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_item_menu: int].
   * @return The value of the column [cd_item_menu].
   */
  @Override
  public java.lang.Integer getCdItemMenu() {
    return cdItemMenu;
  }

  /**
   * Set the value relative to the database
   * column [cd_item_menu: int].
   * @param cdItemMenu The value of the column [cd_item_menu].
   * @return This modified object instance.
   */
  @Override
  public MenuCtu setCdItemMenu( java.lang.Integer cdItemMenu ) {
    this.cdItemMenu = cdItemMenu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [href: varchar].
   * @return The value of the column [href].
   */
  @Override
  public java.lang.String getHref() {
    return href;
  }

  /**
   * Set the value relative to the database
   * column [href: varchar].
   * @param href The value of the column [href].
   * @return This modified object instance.
   */
  @Override
  public MenuCtu setHref( java.lang.String href ) {
    this.href = href;
    return this;
  }


}


}