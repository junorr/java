/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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


package us.pserver.scronv6.test;

import us.pserver.scron.ExecutionContext;
import us.pserver.scron.Job;
import us.pserver.scronv6.SCronV6;
import us.pserver.scron.Schedule;

/**
 * Como exemplo de uso de <code>SimpleCron</code>,
 * imaginemos uma tarefa de cálculo de números, agendada para
 * execução a cada 10 segundos e outra tarefa para
 * exibir o resultado do cálculo.<br>
 * Para que possamos fornecer e obter dados da tarefa agendada, 
 * utilizaremos o mapa de persistencia de objetos fornecido por 
 * <code>SimpleCron</code>.<br><br>
 * 
 * <span style='color: GRAY; font-family: monospace; font-size: 12px;'>
 *  //Iniciando SimpleCron
 * </span>
 * <pre>
 *  SimpleCron sc = new SimpleCron();
 * </pre> 
 * <span style='color: GRAY; font-family: monospace; font-size: 12px;'>
 *  //inserindo dados de cálculo no mapa de persistência
 * </span>
 * <pre>
 *  Integer num1 = 5;
 *  Integer num2 = 10;
 *  
 *  final String NUM1 = "calc.num1";
 *  final String NUM2 = "calc.num2";
 *  final String RESULT = "calc.result";
 *  
 *  sc.getDataMap().put(NUM1, num1);
 *  sc.getDataMap().put(NUM2, num2);
 * </pre> 
 * <span style='color: GRAY; font-family: monospace; font-size: 12px;'>
 *  //criando os agendamentos a cada 10 segundos
 * </span>
 * <pre>
 *  Schedule calcSchedule = new Schedule()
 *      .startNow()
 *      .repeatInSeconds(10);
 *  
 *  Schedule showSchedule = new Schedule()
 *      .startAt(50)
 *      .repeatInSeconds(10);
 * </pre> 
 * <span style='color: GRAY; font-family: monospace; font-size: 12px;'>
 *  //Criando a tarefa de cálculo
 * </span>
 * <pre>
 *  Job calc = new Job() {
 *    
 *    public void execute(ExecutionContext c) {
 *      Integer num1 = (Integer) c.dataMap().get(NUM1);
 *      Integer num2 = (Integer) c.dataMap().get(NUM2);
 *      Integer result = num1 * num2;
 *      System.out.println("* Calc: [" + num1 + " * " + num2 + "]");
 *      c.dataMap().put(RESULT, result);
 *    }
 *    
 *    public void error(Throwable th) {
 *      th.printStackTrace();
 *    }
 *        
 *  };
 * </pre> 
 * <span style='color: GRAY; font-family: monospace; font-size: 12px;'>
 *  //Criando a tarefa de exibição do relsultado
 * </span>
 * <pre>
 *  Job show = new Job() {
 *    
 *    public void execute(ExecutionContext c) {
 *      System.out.print("  result=");
 *      System.out.println(c.dataMap().get(RESULT));
 *      System.out.println("  executed: "+ c.schedule());
 *      System.out.println();
 *      
 *      c.dataMap().put(NUM1, (int) (Math.random() * 10));
 *      c.dataMap().put(NUM2, (int) (Math.random() * 100));
 *    }
 *    
 *    public void error(Throwable th) {
 *      th.printStackTrace();
 *    }
 *        
 *  };
 *  
 * </pre> 
 * <span style='color: GRAY; font-family: monospace; font-size: 12px;'>
 *  //Finalmente agendamos ambas as tarefas para execução
 * </span>
 * <pre>
 *  sc.schedule(calcSchedule, calc)
 *      .schedule(showSchedule, show);
 * </pre>
 * 
 * Abaixo podemos ver um exemplo de saída no terminal, 
 * correspondente à este código.
 * <div style='width: 400px; height: 195px; background-color: #707070'>
 * <span style='color: #00FF00; font-family: monospace; 
 * font-size: 12px; font-weight: bold; position: relative; 
 * left: 25px; top: 20px;'>
 * * Calc: [5 * 10]<br>
 * &nbsp;&nbsp;result=50<br>
 * <br>
 * * Calc: [1 * 97]<br>
 * &nbsp;&nbsp;result=97<br>
 * <br>
 * * Calc: [9 * 19]<br>
 * &nbsp;&nbsp;result=171<br>
 * <br>
 * * Calc: [6 * 48]<br>
 * &nbsp;&nbsp;result=288<br>
 * </span>
 * </div>
 * 
 * @version 0.1 - 15/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class UsageExample {

  
  
  public static void main(String[] args) {
    //Iniciar SCronV6
    SCronV6 sc = new SCronV6();
    
    //inserir dados de cálculo no mapa de persistência
    Integer num1 = 5;
    Integer num2 = 10;
    
    final String NUM1 = "calc.num1";
    final String NUM2 = "calc.num2";
    final String RESULT = "calc.result";
    
    sc.dataMap().put(NUM1, num1);
    sc.dataMap().put(NUM2, num2);
    
    //criar os agendamentos a cada 2 minutos
    Schedule calcSchedule = new Schedule()
        .startNow()
        .repeatInSeconds(10);
    
    Schedule showSchedule = new Schedule()
        .startDelayed(100)
        .repeatInSeconds(10);
    
    //Criar a tarefa de cálculo
    Job calc = new Job() {
      public void execute(ExecutionContext c) {
        Integer num1 = (Integer) c.dataMap().get(NUM1);
        Integer num2 = (Integer) c.dataMap().get(NUM2);
        Integer result = num1 * num2;
        System.out.println("* Calc: [" + num1 + " * " + num2 + "]");
        c.dataMap().put(RESULT, result);
      }
      
      public void error(Throwable th) {
        th.printStackTrace();
      }
    };
    
    //Criar a tarefa de exibição do relsultado
    Job show = new Job() {
      public void execute(ExecutionContext c) {
        System.out.print("  result=");
        System.out.println(c.dataMap().get(RESULT));
        System.out.println("  executed: "+ c.schedule());
        System.out.println();
        
        c.dataMap().put(NUM1, (int) (Math.random() * 10));
        c.dataMap().put(NUM2, (int) (Math.random() * 100));
      }
      
      public void error(Throwable th) {
        th.printStackTrace();
      }
    };
    
    //Finalmente agendamos ambas as tarefas para execução
    sc.put(calcSchedule, calc)
        .put(showSchedule, show);
    sc.jobs().printAll();
  }
  
  
}
