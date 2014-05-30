/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package com.jpower.pstat;

import com.jpower.sys.Config;
import java.io.File;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 08/10/2012
 */
@ManagedBean
@SessionScoped
public class About implements Serializable {

  public static final File MESSAGES_FILE = new File("./about.messages");
  
  public static final String
      
      KEY_BR = "PT-BR",
      
      KEY_EN = "EN",
      
      TITLE = "TITLE_";
  
  
  private Config about;
  
  private String message;
  
  private String title;
  
  private String key;
  
  
  public About() {
    System.out.println(MESSAGES_FILE.getAbsolutePath());

    if(!MESSAGES_FILE.exists()) {
      initMessages();
    } else {
      about = new Config(MESSAGES_FILE);
    }
    key = KEY_EN;
  }
  
  
  private void initMessages() {
    try {
      MESSAGES_FILE.createNewFile();
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
    System.out.println("About.initMessages()");
    
    about = new Config(MESSAGES_FILE);
    about.put(KEY_EN, 
        "PowerStat is a real-time monitoring software for hardware resources "
        + "on linux servers. It was designed to run on a web platform with "
        + "Java 7 and Apache Tomcat, using the Java Server Faces and "
        + "Primefaces technologies. <br/> "
        + "Besides monitoring basic resources such as CPU consumption, "
        + "memory use and storage units, PowerStat has a specialized module "
        + "for control and monitoring running processes. "
        + "It is also equipped with a hardware resources scanning module, "
        + "capable to detect critical levels of consumption, warning the "
        + "system administrator via e-mail. "
        + "<br/><br/> "
        + "PowerStat began to be developed in the first half of 2012 "
        + "(unnamed at the time) as interdisciplinary project of the "
        + "Information Systems course in Unis-MG. <br/> The first version "
        + "presented was developed in PHP and Javascript, using the "
        + "ExtJS library. In this second version, plus the migration "
        + "to Java, new features was added, such as the possibility of "
        + "monitoring all mounted storage units, control and monitoring "
        + "running processes, as well the scanning and warning module for "
        + "resources consumption.");
    about.put(KEY_BR, 
        "PowerStat é um software para monitoramento de recursos de hardware "
        + "em tempo real para servidores linux. Foi projetado para rodar sobre "
        + "uma plataforma web com Java 7 e Apache Tomcat, utilizando as "
        + "teconologias Java Server Faces e Primefaces. <br/> "
        + "Além de monitorar recursos básicos como consumo de CPU, memória "
        + "e utilização de unidades de armazenamento, PowerStat possui um "
        + "módulo especializado para acompanhamento e controle de processos "
        + "em execução. Também é dotado de um módulo de escaneamento "
        + "automático de recursos de hardware, capaz de detectar níveis "
        + "críticos de consumo, alertando administrador do sistema via e-mail. "
        + "<br/><br/> "
        + "PowerStat começou a ser desenvolvido no primeiro semestre de 2012 "
        + "(sem nome na época) como projeto interdisciplinar do curso de "
        + "Sistemas de Informação da Unis-MG.<br/> A primeira versão "
        + "apresentada foi desenvolvida em PHP e javascript, utilizando "
        + "a biblioteca ExtJS. Nesta segunda versão, além da migração para "
        + "Java, foram adicionadas novas funcionalidades, como a possibilidade "
        + "de monitoramento de todas as unidades de armazenamento montadas, "
        + "acompanhamento e controle de processos em execução, bem como o "
        + "módulo de escaneamento e alerta para consumo de recursos.");
    about.put(TITLE + KEY_EN, "About PowerStat");
    about.put(TITLE + KEY_BR, "Sobre o PowerStat");
    about.save();
  }
  
  
  public void setEN() {
    key = KEY_EN;
  }
  
  
  public void setBR() {
    key = KEY_BR;
  }
  
  
  public String getMessage() {
    message = about.get(key);
    return message;
  }
  
  
  public String getTitle() {
    title = about.get(TITLE + key);
    return title;
  }
  
}
