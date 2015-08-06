package org.kutty.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortUtil {
	
	/** 
	 * Sorts a given map in descending order according to its value
	 * @param map Map which is to be sorted
	 * @return SortedSet containing the entries of the map in descending order
	 */ 
	
	public static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
		SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
				new Comparator<Map.Entry<K,V>>() {
					public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
						int res = e1.getValue().compareTo(e2.getValue());
						return res != 0 ? -res : 1; // Special fix to preserve items with equal values
					}
				}
				);
		sortedEntries.addAll(map.entrySet()); 
		
		return sortedEntries;
	} 
}
