
import com.jpower.fxs.model.ColumnModel;
import com.jpower.fxs.model.TableModel;
import com.jpower.fxs.table.Extractor;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.05.13
 */
public class TestTableModel {
  
  static class Banco {
    String nome;
    int numAge;
    String nomeAge;
  }
  
  static class Conta {
    int numero;
    Banco banco;
    double saldo;
    Cliente titular;
  }
  
  static class Cliente {
    String nome;
    String telefone;
    int[] cpf;
  }
  
  public static void main(String[] args) {
    
    Banco bb = new Banco();
    bb.nome = "Banco do Brasil";
    bb.nomeAge = "Capão da Canoa";
    bb.numAge = 3661;
    
    Cliente coio = new Cliente();
    coio.cpf = new int[] { 9, 6, 8, 6, 4, 5, 1, 6, 0, 9, 1 };
    coio.nome = "Samoco Coio";
    coio.telefone = "(41) 9921-9736";
    
    Conta cc = new Conta();
    cc.banco = bb;
    cc.numero = 6036477;
    cc.saldo = 300.0;
    cc.titular = coio;
    
    ColumnModel cbanco = new ColumnModel("Banco", String.class);
    cbanco.addFieldExtractor("banco", "nome");
    
    ColumnModel cpref = new ColumnModel("Prefixo", int.class);
    cpref.addFieldExtractor("banco", "numAge");
    
    ColumnModel cage = new ColumnModel("Agencia", String.class);
    cage.addFieldExtractor("banco", "nomeAge");
    
    ColumnModel ccta = new ColumnModel("Conta", int.class);
    ccta.addFieldExtractor("numero");
    
    ColumnModel ctitular = new ColumnModel("Titular", String.class);
    ctitular.addFieldExtractor("titular", "nome");
    
    ColumnModel csaldo = new ColumnModel("Saldo", double.class);
    csaldo.addFieldExtractor("saldo");
    
    TableModel tm = new TableModel();
    tm.addColumn(cbanco)
        .addColumn(cpref)
        .addColumn(cage)
        .addColumn(ccta)
        .addColumn(ctitular)
        .addColumn(csaldo);
    tm.addSource(cc);
    
    JFrame f = new JFrame("Table Test");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLocationRelativeTo(null);
    f.setSize(400, 300);
    
    JXTable tb = new JXTable(tm);
    tb.setColumnControlVisible(true);
    tb.packAll();
    JScrollPane sp = new JScrollPane(tb);
    sp.setSize(380, 250);
    f.add(sp);
    
    f.setVisible(true);
  }
  
}
