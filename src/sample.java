import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class sample {
	static ArrayList<HashSet<String>> test = new ArrayList<HashSet<String>>();
	static HashMap<HashSet<String>,Integer> total = new HashMap<HashSet<String>,Integer>();


		static void can(HashMap<HashSet<String>,Integer> m, int k, double sup)
		{
			HashMap<HashSet<String>,Integer> temp = new HashMap<HashSet<String>,Integer>();
			temp=gen(m,k,sup); //call to generate candidates for level k with frequent itemsets from k-1 passed as m
			
			//get the candidates and calculate the frequent items for current level
			if(temp.size()!=0)
			{
			System.out.println("\nFrequent itemsets in Level "+k);
			System.out.print(temp.size());
			}
			//if there are no frequent itemsets for a certain level stop recursing
			//or if the last level is reached, can add another condition k==n
			if(temp.size()==0) 
			{
			return; //stop
			}
			else
			{
				total.putAll(temp); //add the frequent itemsets to end result
				can(temp,k+1,sup); //recurse for level k+1
			}

		}
		
		
		static HashMap gen(HashMap<HashSet<String>,Integer> h,int k,double sup)
		{
			HashMap<HashSet<String>,Integer> map = new HashMap<HashSet<String>,Integer>();
				HashSet<String> newPrefix = new HashSet<String>();
				
				Iterator<Map.Entry<HashSet<String>,Integer>> iterator = h.entrySet().iterator();
				int count;
				
				HashSet<String> th = new HashSet<String>();
				HashSet<String> prefix = new HashSet<String>();
				
				
		        while(iterator.hasNext()) //outer loop for merging 
		        {
		        	Map.Entry<HashSet<String>, Integer> entry = iterator.next();
		        	newPrefix = entry.getKey();
		        	Iterator<Map.Entry<HashSet<String>,Integer>> iter = h.entrySet().iterator();
		        	 
		        	
		            while(iter.hasNext()) //inner loop for merging
					{	
		            	prefix.clear();
		            	th = iter.next().getKey();
		            	if(th.equals(newPrefix)) //if same element do not make a set 
		            	{
		            		continue;
		            	}
						
		            	prefix.addAll(newPrefix); 
						prefix.addAll(th); //merge two hashsets into third
						count=0;
						
						if(prefix.size()==k)
						{//if prefix size correct calculate frequency
							  for (HashSet<String> chk : test) //test has 100 hashsets derived from rows
				        	  {
				        		  if(chk.containsAll(prefix)) //if candidate is a subset
				        		  {
				        			  count++;
				        			  //update frequency
				        		  }
				        	  }
				          //if frequency above support threshold then make a new instance of hashset and save in hashmap
				          if(count>=sup*100)
				          {
				        	HashSet<String> add = new HashSet<String>(prefix); 
				        	map.put(add,count);
				          }
						}
						
					}
		            iterator.remove();
		        }
		          return map;  //return hashmap of current level
		}
		
	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException {

		Scanner dataFile = new Scanner(new File("association-rule-test-data.txt")); //Enter the path of association rules here
	
		HashMap<HashSet<String>,Integer> h = new HashMap<HashSet<String>,Integer>(); //for frequent 1-itemsets
		
		String temp;
		int count = 0;
		int d;
		int index;
		
		
		
		
		while(dataFile.hasNextLine()){
            String line = dataFile.nextLine();
            count++;
        
            Scanner scanner = new Scanner(line);
            scanner.useDelimiter("\\n");
         
            while(scanner.hasNextLine()){
            	String input = scanner.nextLine();
                HashSet<String> s = new HashSet<String>();
                String val;
        		d=-1;
        		Scanner in = new Scanner(input).useDelimiter("\\t");
        			while(in.hasNext())
        			{
        				if(d==-1)
        				{
        					d=0;
        					in.next();
        					continue;
        				}
        				temp = in.next();
        				index = d+1;

        				if(d<100)  //use of this condition specific to this dataset, remove if using another dataset
            			val = Integer.toString(index)+temp;
            			else
        				val = temp;
        				
        				HashSet<String> hs = new HashSet<String>();
        			
        				hs.add(val); //for hashmap h
        				s.add(val); //for hashmap test
        				
        				if(h.containsKey(hs))
        				{
        				h.put(hs,h.get(hs)+1);
        				}
        				else
        				{
        				h.put(hs,1);
        				}
        				d++;
        			} 
        			test.add(s);
            }
            scanner.close();
        }
        dataFile.close();
        
        System.out.println("Implementation of Part 1:");
        
        double[] iarr = new double[] {0.3,0.4,0.5,0.6,0.7}; 
        for(double i: iarr)
        {
        	total.clear();
        	HashMap<HashSet<String>,Integer> t = new HashMap<HashSet<String>,Integer>(h); //new HashMaps for each support threshold instance
        	System.out.println("\nCalculating for "+i*100+"% support threshold:");
        	//loop for 
        	for(Iterator<Map.Entry<HashSet<String>, Integer>> it = t.entrySet().iterator(); it.hasNext();)
            {
            	  Map.Entry<HashSet<String>, Integer> entry = it.next();
                  if(entry.getValue()<i*100) {  //CHANGE THIS TO DESIRED PERCENTAGE VALUE
                    it.remove();
                  }
            }
            //h now has the most frequent itemsets 
            System.out.println("\nFrequent itemsets in Level 1");
    		System.out.print(t.size());
            
//    		System.out.println(test);
            total.putAll(t); //added frequent 1-itemsets to aggregator before calling recursive function  
            can(t,2,i);  //Call starts from here
            
            
            System.out.println("\n\nTotal:");
            System.out.println(total.size());
        	
        }
        
	}
}

