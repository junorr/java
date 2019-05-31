/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.util.Optional;


/**
 *
 * @author juno
 */
public interface Delimiter extends Statement<Void> {
  
  public static final Delimiter BLOCK_START = new Delimiter() {
    int priority = Integer.MIN_VALUE;
    public Optional<Void> resolve(Memory m, Stack s) { return Optional.empty(); }
    public int priority() { return priority; }
    public void priority(int p) { priority = p; }
  };
  
  public static final Delimiter BLOCK_END = new Delimiter() {
    int priority = Integer.MIN_VALUE + 1;
    public Optional<Void> resolve(Memory m, Stack s) { return Optional.empty(); }
    public int priority() { return priority; }
    public void priority(int p) { priority = p; }
  };
  
  public static final Delimiter ARGS_START = new Delimiter() {
    int priority = Integer.MIN_VALUE + 2;
    public Optional<Void> resolve(Memory m, Stack s) { return Optional.empty(); }
    public int priority() { return priority; }
    public void priority(int p) { priority = p; }
  };
  
  public static final Delimiter ARGS_END = new Delimiter() {
    int priority = Integer.MIN_VALUE + 3;
    public Optional<Void> resolve(Memory m, Stack s) { return Optional.empty(); }
    public int priority() { return priority; }
    public void priority(int p) { priority = p; }
  };
  
}
