/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import edu.princeton.cs.introcs.StdOut;
import java.util.ArrayList;
import se.kth.id1020.util.Document;

/**
 *Class to represent a cacheobject for queries in the TinySearchEngine class.
 * @author Kim
 */
public class CacheObject
{
    public String operator;
    public String operand1;
    public String operand2;
    public ArrayList<ArrayList> operand3;
    public ArrayList<ArrayList> operand4;
    public ArrayList<ArrayList> data;
    /**
     * Constructors to create a object.
     * @param data
     * @param operator
     * @param operand1 
     */
    public CacheObject(ArrayList<ArrayList> data,String operator, String operand1)
    {
        this.data = data;
        this.operator = operator;
        this.operand1 = operand1;
    }
    
    public CacheObject(ArrayList<ArrayList> data, String operator, ArrayList<ArrayList> operand3, ArrayList<ArrayList> operand4)
    {
        this.data = data;
        this.operator = operator;
        this.operand3 = operand3;
        this.operand4 = operand4;
    }
    /**
     * Relative complex equals methods to support searchCache() in TinySearchEngine class.
     * @param operand1
     * @param operand2
     * @return 
     */
    public boolean equals(String operand1, String operand2)
    {
        if(this.operator.equalsIgnoreCase("|") || this.operator.equalsIgnoreCase("+"))
        {
            return (this.operand1.equalsIgnoreCase(operand2) && this.operand2.equalsIgnoreCase(operand1)) || 
                    (this.operand1.equalsIgnoreCase(operand1) && this.operand2.equalsIgnoreCase(operand2));
        }
        if(this.operator.equalsIgnoreCase("-"))
        {
            return (this.operand1.equalsIgnoreCase(operand1) && this.operand2.equalsIgnoreCase(operand2));
        }
        return false;
    }
    
    public boolean equals(String operand1)
    {
        if(this.operator.equalsIgnoreCase("SimpleQuery"))
        {
            return (this.operand1.equalsIgnoreCase(operand1));
        }
        return false;
    }
    
     public boolean equals(ArrayList<ArrayList> operand3, ArrayList<ArrayList> operand4)
    {
        if(this.operator.equalsIgnoreCase("|") || this.operator.equalsIgnoreCase("+"))
        {
            boolean equal = true;
            
            boolean temp = (equalList2(this.operand3.get(0),(operand3.get(0))) 
                    && equalList2(this.operand4.get(0),(operand4.get(0))) 
                        || (equalList2(this.operand3.get(0),(operand4.get(0))) 
                    && (equalList2(this.operand4.get(0),(operand3.get(0))))));
            if(temp == false)
            {
                return temp;
            }
            
            temp = (equalList3(this.operand3.get(1),(operand3.get(1))) 
                    && equalList3(this.operand4.get(1),(operand4.get(1))) 
                        || (equalList3(this.operand3.get(1),(operand4.get(1))) 
                    && (equalList3(this.operand4.get(1),(operand3.get(1))))));
            if(temp == false)
            {
                return temp;
            }
                    
            for(int i = 2; i < this.operand3.size(); i++)
            {
                if((equalList(this.operand3.get(i),(operand3.get(i))) 
                    && equalList(this.operand4.get(i),(operand4.get(i))) 
                        || (equalList(this.operand3.get(i),(operand4.get(i))) 
                    && (equalList(this.operand4.get(i),(operand3.get(i)))))))
                {
                    equal = true;
                }
                else
                {
                    return false;
                }
            }
            return equal;
        }
        if(this.operator.equalsIgnoreCase("-"))
        {
           boolean equal = true;
            
            boolean temp = (equalList2(this.operand3.get(0),(operand3.get(0))) 
                    && equalList2(this.operand4.get(0),(operand4.get(0))));
            if(temp == false)
            {
                return temp;
            }
            temp = (equalList3(this.operand3.get(1),(operand3.get(1))) 
                    && equalList3(this.operand4.get(1),(operand4.get(1))));
            if(temp == false)
            {
                return temp;
            }
                    
            for(int i = 1; i < this.operand3.size(); i++)
            {
                if((equalList(this.operand3.get(i),(operand3.get(i))) 
                    && equalList(this.operand4.get(i),(operand4.get(i)))))
                {
                    equal = true;
                }
                else
                {
                    return false;
                }
            }
            return equal;
        }
        return false;
    }
    
    public boolean equalList(ArrayList<Integer> operand1, ArrayList<Integer> operand2)
    {
        if(operand1.size() != operand2.size() || (operand1 == null && operand2 != null) || (operand2 == null && operand1 != null))
        {
            return false;
        }
        if(operand1 == null && operand2 == null)
        {
            return true;
        }
        return operand1.equals(operand2);
    }
        public boolean equalList2(ArrayList<Document> operand1, ArrayList<Document> operand2)
    {
        if(operand1.size() != operand2.size() || (operand1 == null && operand2 != null) || (operand2 == null && operand1 != null))
        {
            return false;
        }
        if(operand1 == null && operand2 == null)
        {
            return true;
        }

        return operand1.equals(operand2);
    }
            public boolean equalList3(ArrayList<Double> operand1, ArrayList<Double> operand2)
    {
        if(operand1.size() != operand2.size() || (operand1 == null && operand2 != null) || (operand2 == null && operand1 != null))
        {
            return false;
        }
        if(operand1 == null && operand2 == null)
        {
            return true;
        }

        return operand1.equals(operand2);
    }
    
    public ArrayList<ArrayList> getResult() 
    {
        return this.data;
    }

       
}
