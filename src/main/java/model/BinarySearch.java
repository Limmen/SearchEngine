/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import edu.princeton.cs.introcs.StdOut;
import java.util.ArrayList;

/**
 *BinarySearch class to provide searching in O(Log N).
 * @author Kim
 */
public class BinarySearch 
{
  
    public static int binSearch(String key, ArrayList<IndexObject> a) 
    {
         return binSearch(key, a, 0, a.size());
    }
    public static int binSearch(String key, ArrayList<IndexObject> a, int lo, int hi) 
    {
        if (hi <= lo) return -1;
        int mid = lo + (hi - lo) / 2;
        int cmp = a.get(mid).getWord().compareToIgnoreCase(key);
        if      (cmp > 0) return binSearch(key, a, lo, mid);
        else if (cmp < 0) return binSearch(key, a, mid+1, hi);
        else              return mid;
    }
    
      
}
