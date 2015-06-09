package com.gdc.nms.test.runner;
import java.util.ArrayList;
import java.util.Random;
/**
 * 
 * @author Gilberto Cordero Cervantes
 *
 */

public class Randomizer {
	private static final Random rand=new Random();
	
	public static int range(int min,int max){
		return (int)(rand.nextDouble()*max+min);
	}
	/**
	 * 
	 * @param min value where the range begins
	 * @param value that ends the range but not taken into account in generating numbers
	 * @return ArratList<Integer> 
	 */
	public static ArrayList<Integer> rangeWithoutRepeat(int min,int max){
		ArrayList<Integer> numbers=new ArrayList<Integer>();
		while(numbers.size()<max){
			int number=range(min,max);
			if(!numbers.contains(number)){
				numbers.add(number);
			}
		}
		return numbers;
		
	}
	
	
	public static void main(String[] args) {
		int x=Randomizer.range(1, 5);
		System.out.println(""+Randomizer.rangeWithoutRepeat(1, 6));
				System.out.println(""+x);
	}
}
