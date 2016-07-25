/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.test;

import us.pserver.jc.util.SortedQueue;


/**
 *
 * @author juno
 */
public class TestSortedQueue {
	
	
	public static void main(String[] args) {
		SortedQueue<Integer> sort = new SortedQueue<>();
		for(int i = 10; i > 0; i--) {
			sort.add(i);
		}
		System.out.println("[SortedQueue] size="+ sort.size());
		for(int i = 0; i < 10; i++) {
			System.out.println("[SortedQueue."+ i+ "] "+ sort.poll());
		}
		System.out.println("----------------------");
		for(int i = 10; i > 0; i--) {
			sort.add(i);
		}
		sort.sort();
		for(int i = 0; i < 10; i++) {
			System.out.println("[SortedQueue."+ i+ "] "+ sort.poll());
		}
	}
	
}
