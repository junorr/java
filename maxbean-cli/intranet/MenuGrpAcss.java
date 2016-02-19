package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.menu_grp_acss]
 */
public class MenuGrpAcss implements IMenuGrpAcss {

  private java.lang.Integer cdCtuMenu;
  private java.lang.Integer cdGrpAcss;

  public MenuGrpAcss() {}

  public MenuGrpAcss(
      java.lang.Integer cdCtuMenu, 
      java.lang.Integer cdGrpAcss) {
    this.cdCtuMenu = cdCtuMenu;
    this.cdGrpAcss = cdGrpAcss;
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
  public MenuGrpAcss setCdCtuMenu( java.lang.Integer cdCtuMenu ) {
    this.cdCtuMenu = cdCtuMenu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_grp_acss: int].
   * @return The value of the column [cd_grp_acss].
   */
  @Override
  public java.lang.Integer getCdGrpAcss() {
    return cdGrpAcss;
  }

  /**
   * Set the value relative to the database
   * column [cd_grp_acss: int].
   * @param cdGrpAcss The value of the column [cd_grp_acss].
   * @return This modified object instance.
   */
  @Override
  public MenuGrpAcss setCdGrpAcss( java.lang.Integer cdGrpAcss ) {
    this.cdGrpAcss = cdGrpAcss;
    return this;
  }


}


}