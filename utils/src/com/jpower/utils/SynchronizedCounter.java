/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author Juno
 */
public class SynchronizedCounter
    extends Observable
{

  private int count;

  private final Object lock;

  private boolean isLocked;

  private int inc;

  private int startValue;

  private List<Integer> actionValues;

  private List<ActionListener> listeners;


  public SynchronizedCounter()
  {
    lock = new Object();
    isLocked = false;
    inc = 1;
    startValue = 0;
    count = startValue;
    actionValues = new ArrayList<Integer>();
    listeners = new ArrayList<ActionListener>();
  }

  public SynchronizedCounter(int startValue)
  {
    this();
    this.startValue = startValue;
    count = startValue;
  }

  public void addActionListener(ActionListener listener)
  {
    if(listener == null)
      return;
    listeners.add(listener);
  }

  public boolean removeActionListener(ActionListener listener)
  {
    if(listener == null) return false;
    return listeners.remove(listener);
  }

  public void clearActionListeners()
  {
    listeners.clear();
  }

  public void addActionValue(int value)
  {
    actionValues.add(value);
  }

  public boolean removeActionValue(int value)
  {
    return actionValues.remove(new Integer(value));
  }

  public void clearActionValues()
  {
    actionValues.clear();
  }

  public SynchronizedCounter setIncrement(int inc)
  {
    this.inc = inc;
    return this;
  }

  public int getIncrement()
  {
    return inc;
  }

  public int getStartValue()
  {
    return startValue;
  }

  public SynchronizedCounter setStartValue(int value)
  {
    this.startValue = value;
    return this;
  }

  public int counter()
  {
    return count;
  }

  public boolean isLocked()
  {
    return isLocked;
  }

  public boolean lock()
  {
    if(isLocked)
      return false;
    isLocked = true;
    return true;
  }

  public void waitLock()
  {
    if(isLocked)
      synchronized(lock) {
        try {
          lock.wait();
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        }
      }

    this.lock();
  }

  public void unlock()
  {
    isLocked = false;
    synchronized(lock) {
      lock.notifyAll();
    }
  }

  public void add()
  {
    count += inc;
    this.fireNotify();
  }

  public void sub()
  {
    count -= inc;
    this.fireNotify();
  }

  public void lockAndAdd()
  {
    if(!this.lock())
      this.waitLock();
    this.add();
    this.unlock();
  }

  public void lockAndSub()
  {
    if(!this.lock())
      this.waitLock();
    this.sub();
    this.unlock();
  }

  private void fireNotify()
  {
    Integer value = new Integer(count);
    if(actionValues.contains(value))
      this.notifyListeners();
    this.setChanged();
    this.notifyObservers(value);
    this.clearChanged();
  }

  private void notifyListeners()
  {
    ActionEvent event = new ActionEvent(this, count, "ActionValue");
    for(ActionListener listener : listeners)
      listener.actionPerformed(event);
  }

  @Override
  public String toString()
  {
    return "[ SynchronizedCounter ] : counter( "+ count +" )";
  }


  public static void main(String[] args)
  {
    final SynchronizedCounter counter = new SynchronizedCounter();

    new Thread(new Runnable()
    {
      public void run()
      {
        System.out.println( "[THREAD 1]: Getting lock..." );
        if(counter.lock())
          System.out.println( "[THREAD 1]: Counter Locked!" );
        try {
          Thread.sleep(10000);
        } catch(InterruptedException ie) {
          ie.printStackTrace();
        }
        System.out.println( "[THREAD 1]: Unlocking counter..." );
        counter.unlock();
      }
    }).start();

    new Thread(new Runnable()
    {
      public void run()
      {
        System.out.println( "[THREAD 2]: Trying to get lock..." );
        counter.lockAndAdd();
        
        System.out.println( "[THREAD 2]: Counter Locked!" );
        System.out.println( "[THREAD 2]: Unlocking counter..." );
        counter.unlock();
        System.out.println( counter );
      }
    }).start();

    System.out.println( counter );
  }

}
