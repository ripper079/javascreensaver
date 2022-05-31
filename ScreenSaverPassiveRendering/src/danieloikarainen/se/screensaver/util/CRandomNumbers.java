package danieloikarainen.se.screensaver.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CRandomNumbers 
{
	public static void main(String[] args) 
	{		
		List<Integer> myNumbers = CRandomNumbers.genRandomListNumbers(0, 6, 5);
		
		for (Integer numb : myNumbers)
			System.out.print(numb + "\t");
		
	}
	
	//Generates a list from pStartNumb to (inclusive) pEndNumb
	public static List<Integer> generateOrderedIntList(int pStartNumb, int pEndNumb)
	{
		return 	
				IntStream.iterate(pStartNumb, i -> i+1)
							.takeWhile(e -> e <= pEndNumb)
							.boxed()
							.collect(Collectors.toList());
	}
	
	
	//pCountRandomNumbers cant be > (pEndNumb - pStartNumb)+1
	public static List<Integer> genRandomListNumbers(int pStartNumb, int pEndNumb, int pCountRandomNumbers)
	{
		List<Integer> listPossibleValues = generateOrderedIntList(pStartNumb, pEndNumb);
		List<Integer> randomListNumbers = new ArrayList<>(pCountRandomNumbers);
		
		moveNumbers(listPossibleValues, randomListNumbers, pCountRandomNumbers);
		//The randomList is unordered now
		
				
		return sortList(randomListNumbers);
	}
	
	
	private static void moveNumbers(List<Integer> pFromList, List<Integer> pToList, int pCountRandomNumbers)
	{		
		Random randomGenerator = new Random();		
		for (int i = 0; i < pCountRandomNumbers; i++) 
		{
			int randomIndex = randomGenerator.nextInt(pFromList.size());
			pToList.add(pFromList.get(randomIndex));
			pFromList.remove(randomIndex);			
		}
	}
	

	
	private static List<Integer> sortList(List<Integer> pListToSort) 
	{
		List<Integer> sortedList = pListToSort.stream()
							.sorted().collect(Collectors.toList());
		return sortedList;
	}
	
	
}
