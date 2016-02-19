package us.pserver.mbtest.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.menu_itens]
 */
public interface IMenuItens {

  /**
   * Get the value relative to the database
   * column [cd_item_menu: int].
   * @return The value of the column [cd_item_menu].
   */
  public java.lang.Integer getCdItemMenu();

  /**
   * Set the value relative to the database
   * column [cd_item_menu: int].
   * @param cdItemMenu The value of the column [cd_item_menu].
   * @return This modified object instance.
   */
  public IMenuItens setCdItemMenu( java.lang.Integer cdItemMenu );


  /**
   * Get the value relative to the database
   * column [nm_item_menu: varchar].
   * @return The value of the column [nm_item_menu].
   */
  public java.lang.String getNmItemMenu();

  /**
   * Set the value relative to the database
   * column [nm_item_menu: varchar].
   * @param nmItemMenu The value of the column [nm_item_menu].
   * @return This modified object instance.
   */
  public IMenuItens setNmItemMenu( java.lang.String nmItemMenu );


}
}