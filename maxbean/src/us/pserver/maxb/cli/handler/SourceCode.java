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

package us.pserver.maxb.cli.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import us.pserver.maxb.cli.MBOptions;
import us.pserver.maxb.cli.MaxBeanException;
import us.pserver.maxb.code.BeanReaderFactory;
import us.pserver.maxb.code.ClassCode;
import us.pserver.maxb.code.IFaceCode;
import us.pserver.maxb.sql.spec.ISchema;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.util.CamelCaseName;
import us.pserver.maxb.util.Ident;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/02/2016
 */
public class SourceCode extends AbstractHandler {
  
  private final List<IFaceCode> icode;
  
  private final List<ClassCode> ccode;
  
  private final List<BeanReaderFactory> beanf;
  

  public SourceCode(MBOptions opt, ISchema sch) {
    super(opt, sch);
    icode = new LinkedList<>();
    ccode = new LinkedList<>();
    beanf = new LinkedList<>();
  }
  
  
  public static SourceCode get(MBOptions opt, ISchema sch) {
    return new SourceCode(opt, sch);
  }
  
  
  public List<IFaceCode> getIFaceCodeList() {
    return icode;
  }
  
  
  public List<ClassCode> getClassCodeList() {
    return ccode;
  }
  
  
  public List<BeanReaderFactory> getBeanReaderList() {
    return beanf;
  }
  

  @Override
  public SourceCode handle() throws MaxBeanException {
    if(schema.isEmpty()) return this;
    Ident id = new Ident(' ', (opt.getIdentation() > 0 
        ? opt.getIdentation() : 2)
    );
    if(opt.getPackage() == null) {
      throw new MaxBeanException(
          "Package for source code must be specified"
      );
    }
    Iterator<ITable> itb = schema.getTables().iterator();
    while(itb.hasNext()) {
      ITable table = itb.next();
      IFaceCode ic = new IFaceCode(table, id, opt.getPackage());
      icode.add(ic);
      ccode.add(new ClassCode(ic));
      beanf.add(new BeanReaderFactory(table, id, opt.getPackage()));
    }
    return this;
  }
  
  
  public void save() throws MaxBeanException {
    if(icode.isEmpty() 
        && ccode.isEmpty() 
        && beanf.isEmpty()) {
      return;
    }
    File outdir = new File((opt.getOutput() != null 
        ? opt.getOutput() : new File("./")), 
        schema.getName()
    );
    outdir.mkdirs();
    //gen interface
    this.genInterface(outdir);
    //gen class
    this.genClass(outdir);
    //gen BeanReader
    this.genBeanReader(outdir);
  }
  
  
  public void genInterface(File outdir) throws MaxBeanException {
    if(icode.isEmpty()) {
      return;
    }
    try {
      File fiface = new File(outdir, "iface");
      fiface.mkdirs();
      for(IFaceCode ic : icode) {
        File f = new File(fiface, "I"+ CamelCaseName.of(
            ic.getTable().getName()).toString()+ ".java"
        );
        if(!f.exists()) f.createNewFile();
        try (FileWriter fw = new FileWriter(f, false)) {
          fw.write(ic.getCode());
        }
      }
    } 
    catch(IOException e) {
      throw new MaxBeanException(
          "Error generating interface source code", e
      );
    }
  }

  
  public void genClass(File outdir) throws MaxBeanException {
    if(ccode.isEmpty()) {
      return;
    }
    try {
      for(ClassCode cc : ccode) {
        File f = new File(outdir, CamelCaseName.of(
            cc.getTable().getName()).toString()+ ".java"
        );
        if(!f.exists()) f.createNewFile();
        try (FileWriter fw = new FileWriter(f, false)) {
          fw.write(cc.getCode());
        }
      }
    } 
    catch(IOException e) {
      throw new MaxBeanException(
          "Error generating class source code", e
      );
    }
  }

  
  public void genBeanReader(File outdir) throws MaxBeanException {
    if(beanf.isEmpty()) {
      return;
    }
    try {
      File freader = new File(outdir, "reader");
      freader.mkdirs();
      for(BeanReaderFactory br : beanf) {
        File f = new File(freader, CamelCaseName.of(
            br.getTable().getName()).toString()+ "Reader.java"
        );
        if(!f.exists()) f.createNewFile();
        try (FileWriter fw = new FileWriter(f, false)) {
          fw.write(br.getCode());
        }
      }
    } 
    catch(IOException e) {
      throw new MaxBeanException(
          "Error generating BeanReader source code", e
      );
    }
  }

}
