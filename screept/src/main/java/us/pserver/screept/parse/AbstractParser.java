/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;


/**
 *
 * @author juno
 */
public abstract class AbstractParser implements Parser {
  
  protected final StringBuilder source;
  
  protected AbstractParser() {
    this.source = new StringBuilder();
  }
  
  @Override
  public AbstractParser clear() {
    source.delete(0, source.length());
    return this;
  }

  @Override
  public AbstractParser append(char c) throws UnexpectedTokenException {
    if(!accept(c)) {
      throw new UnexpectedTokenException(Character.toString(c));
    }
    source.append(c);
    return this;
  }
  
}
