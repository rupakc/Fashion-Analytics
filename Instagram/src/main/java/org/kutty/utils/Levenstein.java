package org.kutty.utils; 

/**
 * @author Rupak Chakraborty
 * For Adobe Systems, India
 * 24 March, 2015
 * Calculates the levenstein distance between two given strings, levenstein distance is defined as the minimum
 * number of operations required to transform one string to another. The operations defined are insert, replace and delete
 */ 

public class Levenstein {

	/** 
	 * Utility function to find the minimum of 3 numbers
	 * @param a Integer corresponding to the first number
	 * @param b Integer corresponding to the second number
	 * @param c Integer corresponding to the third number
	 * @return Integer representing the minimum of the three numbers
	 */ 
	
	public static int minimum(int a,int b,int c)
	{
		if (a <= b && a <= c) { 

			return a;
		}

		else if(b <= a && b <= c) { 

			return b;
		}

		else { 

			return c;
		}
	} 

	/** 
	 * Calculates the Edit distance between two Strings i.e. the minimum number of operations needed to
	 * convert one String to another 
	 * @param s1 first string
	 * @param s2 second string
	 * @return Integer representing the edit distance
	 */ 
	
	public static int calculateDistance(String s1, String s2)
	{
		if (s1 == null && s2 == null) { 

			return 0;
		}

		if (s1 == null || s1.length() == 0) {

			return s2.length();
		}

		if (s2 == null || s2.length()== 0) { 

			return s1.length();
		}

		if (s1.equalsIgnoreCase(s2)) { 

			return 0;
		}

		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int [][]distance = new int[s1.length()+1][s2.length()+1];

		for (int i = 0; i <= s1.length(); i++) { 

			distance[i][0] = i;
		}

		for (int i = 0; i <= s2.length(); i++) { 

			distance[0][i] = i;
		}

		for (int i = 1; i <= s1.length(); i++) { 

			for (int j = 1; j <= s2.length(); j++) { 

				if (s1.charAt(i-1) == s2.charAt(j-1)) { 

					distance[i][j] = distance[i-1][j-1];
				}

				else { 

					distance[i][j] = minimum(distance[i][j-1],distance[i-1][j],distance[i-1][j-1])+1;
				}
			}
		}

		return distance[s1.length()][s2.length()];
	}
}
