package com.jpower.utils;


import java.io.File;

/**
 * Classe que indica o sistema operacional
 * na qual a JVM está rodando, pode ser acessado
 * pela variável estática OS.CURRENT
 * Os sistemas operacionais são representados
 * pelo enum interno OsType.
 * @author Juno Roesler
 */
public class OS {

  /**
   * Contém os valores possíveis
   * para os sistemas operacionais:
   * OsType.WINDOWS, OsType.LINUX
   */
  public static enum OsType { WINDOWS, LINUX }

  /**
   * Sistema operacional atual,
   * no qual a JVM está rodando.
   * Um dos tipos possíveis para OsType.
   */
  public static final OsType CURRENT = 
      (File.separator.equals("/") ?
        OsType.LINUX : OsType.WINDOWS);

  /**
   * Construtor ilegal. OS não deve ser instanciado.
   * @throws IllegalAccessException
   */
  private OS() throws IllegalAccessException {
    throw new IllegalAccessException("OS não pode ser instanciado!");
  }

  /**
   * Retorna true se o sistema operacional
   * for Linux.
   * @return boolean
   */
  public static boolean isLinux()
  {
    return (OS.CURRENT == OS.OsType.LINUX);
  }

  /**
   * Retorna true se o sistema operacional
   * for Windows.
   * @return boolean
   */
  public static boolean isWindows()
  {
    return (OS.CURRENT == OS.OsType.WINDOWS);
  }

}
