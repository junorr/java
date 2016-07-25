package us.pserver.mbex;

import us.pserver.mbex.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.sit_ctu]
 */
public class SitCtu implements ISitCtu {

  private java.lang.String nmUsu;
  private java.lang.Integer cdSitCtu;
  private java.lang.Integer myAge;
  private java.lang.String myName;
  private java.lang.String nmSitCtu;
  private java.lang.String cdUsu;

  public SitCtu() {}

  public SitCtu(
      java.lang.String nmUsu, 
      java.lang.Integer cdSitCtu, 
      java.lang.Integer myAge, 
      java.lang.String myName, 
      java.lang.String nmSitCtu, 
      java.lang.String cdUsu) {
    this.nmUsu = nmUsu;
    this.cdSitCtu = cdSitCtu;
    this.myAge = myAge;
    this.myName = myName;
    this.nmSitCtu = nmSitCtu;
    this.cdUsu = cdUsu;
  }

  /**
   * Get the value relative to the database
   * column [nm_usu: varchar].
   * @return The value of the column [nm_usu].
   */
  @Override
  public java.lang.String getNmUsu() {
    return nmUsu;
  }

  /**
   * Set the value relative to the database
   * column [nm_usu: varchar].
   * @param nmUsu The value of the column [nm_usu].
   * @return This modified object instance.
   */
  @Override
  public SitCtu setNmUsu( java.lang.String nmUsu ) {
    this.nmUsu = nmUsu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_sit_ctu: int].
   * @return The value of the column [cd_sit_ctu].
   */
  @Override
  public java.lang.Integer getCdSitCtu() {
    return cdSitCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_sit_ctu: int].
   * @param cdSitCtu The value of the column [cd_sit_ctu].
   * @return This modified object instance.
   */
  @Override
  public SitCtu setCdSitCtu( java.lang.Integer cdSitCtu ) {
    this.cdSitCtu = cdSitCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [my_age: int].
   * @return The value of the column [my_age].
   */
  @Override
  public java.lang.Integer getMyAge() {
    return myAge;
  }

  /**
   * Set the value relative to the database
   * column [my_age: int].
   * @param myAge The value of the column [my_age].
   * @return This modified object instance.
   */
  @Override
  public SitCtu setMyAge( java.lang.Integer myAge ) {
    this.myAge = myAge;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [my_name: text].
   * @return The value of the column [my_name].
   */
  @Override
  public java.lang.String getMyName() {
    return myName;
  }

  /**
   * Set the value relative to the database
   * column [my_name: text].
   * @param myName The value of the column [my_name].
   * @return This modified object instance.
   */
  @Override
  public SitCtu setMyName( java.lang.String myName ) {
    this.myName = myName;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_sit_ctu: varchar].
   * @return The value of the column [nm_sit_ctu].
   */
  @Override
  public java.lang.String getNmSitCtu() {
    return nmSitCtu;
  }

  /**
   * Set the value relative to the database
   * column [nm_sit_ctu: varchar].
   * @param nmSitCtu The value of the column [nm_sit_ctu].
   * @return This modified object instance.
   */
  @Override
  public SitCtu setNmSitCtu( java.lang.String nmSitCtu ) {
    this.nmSitCtu = nmSitCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_usu: char].
   * @return The value of the column [cd_usu].
   */
  @Override
  public java.lang.String getCdUsu() {
    return cdUsu;
  }

  /**
   * Set the value relative to the database
   * column [cd_usu: char].
   * @param cdUsu The value of the column [cd_usu].
   * @return This modified object instance.
   */
  @Override
  public SitCtu setCdUsu( java.lang.String cdUsu ) {
    this.cdUsu = cdUsu;
    return this;
  }


}