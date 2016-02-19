package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.menu_itens]
 */
public class MenuItens implements IMenuItens {

  private java.lang.Integer cdItemMenu;
  private java.lang.String nmItemMenu;

  public MenuItens() {}

  public MenuItens(
      java.lang.Integer cdItemMenu, 
      java.lang.String nmItemMenu) {
    this.cdItemMenu = cdItemMenu;
    this.nmItemMenu = nmItemMenu;
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
  public MenuItens setCdItemMenu( java.lang.Integer cdItemMenu ) {
    this.cdItemMenu = cdItemMenu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_item_menu: varchar].
   * @return The value of the column [nm_item_menu].
   */
  @Override
  public java.lang.String getNmItemMenu() {
    return nmItemMenu;
  }

  /**
   * Set the value relative to the database
   * column [nm_item_menu: varchar].
   * @param nmItemMenu The value of the column [nm_item_menu].
   * @return This modified object instance.
   */
  @Override
  public MenuItens setNmItemMenu( java.lang.String nmItemMenu ) {
    this.nmItemMenu = nmItemMenu;
    return this;
  }


}


}