/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id1020.proj2.ver3;
import edu.princeton.cs.introcs.StdOut;
import java.util.ArrayList;
import java.util.Collections;
/**
 *This class represents a parsetree and is used to handle queries from
 * TinySearchEngine.java.
 * @author Kim
 */
public class ParseTree
{
    private Node root;
    private TinySearchEngine se;
    private int depth;
    String infix;
    /**
     * Constructor which crates a new parsetree with a empty root Node. 
     * @param se 
     */
    public ParseTree(TinySearchEngine se)
    {
        this.se = se;
        this.root = new Node("root");
        this.depth = 0;
    }
    /**
     * Method to insert words in the parseTree. 
     * The Nodes are placed differently depending on their type:
     * operand/operator/order.
     * The insert method is designed to support prefix-queries.
     * @param string 
     */
    public void insert(String string)
    {
        Node nod = new Node(string);
        if(root.right != null)
        {
            if(root.right.left == null)
            {
                root.right.left = nod;
                return;
            }
            if(root.right.right == null)
            {
                root.right.right = nod;
                return;
            }
            
        }
        Node temp;
        Node parent;
        if(nod.type.equalsIgnoreCase("operator") || nod.type.equalsIgnoreCase("operand"))
        {
            temp = root;
            parent = root;
            while(temp.left != null && !temp.left.type.equalsIgnoreCase("operand"))
            {
                parent = temp;
                temp = temp.left;
            }
            if(nod.type.equalsIgnoreCase("operand"))
            {
                if(temp.left == null)
                {
                    temp.left = nod;
                    return;
                }
                if(temp.right == null)
                {
                    temp.right = nod;
                    return;
                }
                if(parent.right == null)
                {
                    parent.right = nod;
                    return;
                }
                if(parent.right != null)
                {
                    temp = parent.right;
                
                if(temp.left == null)
                {
                    temp.left = nod;
                    return;
                }
                if(temp.right == null)
                {
                    temp.right = nod;
                    return;
                }   
                }
                Node temp2 = parent;
                temp = root;
                parent = root;
                while(temp.left != null)
                    {
                        if(temp.left == temp2)
                            {
                                if(temp.right != null)
                                    {
                                        temp = temp.right;
                                        if(temp.left == null)
                                            {
                                                temp.left = nod;
                                                return;
                                            }
                                        if(temp.right == null)
                                            {
                                                temp.right = nod;
                                                return;
                                            }   
                                    }
                                    
                                temp = root;
                                temp2 = parent;     
                            }
                                parent = temp;
                                temp = temp.left;
                            }
            }
            else
            {
                if(temp.left == null)
                {
                    temp.left = nod;
                    depth = depth+1;
                    return;
                }
                if(parent.right == null)
                {
                    parent.right = nod;
                    depth = depth+1;
                    return;
                }
                if(parent.right != null)
                {
                    Node temp2 = parent;
                    temp = root;
                    parent = root;
                    while(temp.left != null)
                            {
                                if(temp.left == temp2)
                                {
                                    if(temp.right == null)
                                    {
                                        temp.right = nod;
                                        depth = depth+1;
                                        return;
                                     }
                                    else
                                    {
                                        temp = root;
                                        temp2 = parent;
                                    }
                                }
                                parent = temp;
                                temp = temp.left;
                            }
                    
                }
                
            }           
        }
        if(nod.type.equalsIgnoreCase("order"))
        {
            root.right = nod;
            return;
        }
    }
    /**
     * Methodd to execute the ParseTree. This method will 
     * make calls to TinySearchEngine to searchcache() updatecache()
     * and to performe the different operations: Union(), Intersection(),
     * Difference() and sorting: Sort().
     * This method returns a reusult with the data that after executing
     * every opration/order in the tree.
     * While executing this method also builds a string to represent the
     * infix-notation of the query.
     * @return 
     */
    public ArrayList<ArrayList> execute()
    {
        ArrayList<ArrayList> data = new ArrayList<>();
        ArrayList<ArrayList> operand1 = new ArrayList();
        ArrayList<ArrayList> operand2 = new ArrayList();
        
        if(root.left.type.equalsIgnoreCase("operand"))
        {
            infix = root.left.string;
            data = se.searchCache(root.left.string);
            if(data == null)
            {
            data = se.simpleQuery(root.left.string);
            if(data != null)
            {
            se.updateCache(data,"SimpleQuery",root.left.string);
            }
            }
            if(root.right != null)
        {
             if(root.right.left == null || root.right.right == null || data == null)
            {
                return null;
            }
            else
            {
                infix = infix + " " + "orderby" + " " + root.right.left.string + " " + root.right.right.string;
                data.set(0, se.sort(data, root.right.left.string, root.right.right.string));
            }
        }
            return data;
        }
        for (int i = 0; i < depth; i++)
        {
        data = null;
        operand1 = null;
        operand2 = null;
        Node temp = root;
        Node parent = root;
        while(temp.left != null)
        {
            parent = temp;
            temp = temp.left;
        }
        if(temp.type.equalsIgnoreCase("operand"))
        {
            temp = parent;
        }
        if(temp.right.type.equalsIgnoreCase("operator"))
        {
            infix = infix + " " + temp.string;
            temp = temp.right;
        }
        String operator = temp.string;
        if(temp.left == null || temp.right == null)
        {
            return null;
        }
        
        if(temp.left.data == null)
        {
            if(infix != null)
                infix = infix + " " + "(" + temp.left.string;
            else
            {
                infix = "(" + temp.left.string;   
            }
            operand1 = se.searchCache(temp.left.string);
            
            if(operand1 == null)
            {
            operand1 = se.simpleQuery(temp.left.string);
            if(operand1 != null)
            {
            se.updateCache(operand1, "simpleQuery", temp.left.string);
            }
            }
        }
        if(temp.left.data != null)
        {
            operand1 = temp.left.data;
        }
        if(temp.right.data == null)
        {
            infix = infix + " " + operator + " " + temp.right.string + ")";
            operand2 = se.searchCache(temp.right.string);
            if(operand2 == null)
            {
            operand2 = se.simpleQuery(temp.right.string);
            if(operand2 != null)
            {
            se.updateCache(operand2, "simpleQuery", temp.right.string);
            }
            }
        }
        if(temp.right.data != null)
        {
            operand2 = temp.right.data;
            String tempInfix = "(" + infix;
            infix = tempInfix + ")";
        }
        switch(operator)
        {
            case "|": 
            data = se.searchCache(operator, operand1, operand2);
            if(data == null)
            {
            data = se.union(operand1, operand2);
            if(data != null)
            {
            se.updateCache(data, operator, operand1, operand2);
            }
            }
            temp.type="operand";
            temp.data = data;
            temp.left = null;
            temp.right = null;
                break;
                
            case "+":
            data = se.searchCache(operator, operand1, operand2);
            if(data == null)
            {
            data = se.intersection(operand1, operand2);
            if(data != null)
            {
            se.updateCache(data, operator, operand1, operand2);
            }
            }
            temp.type="operand";
            temp.data = data;
            temp.left = null;
            temp.right = null;
                break;
            
            case "-":
            data = se.searchCache(operator, operand1, operand2);
            if(data == null)
            {
            data = se.difference(operand1, operand2);
            if(data != null)
            {
            se.updateCache(data, operator, operand1, operand2);
            }
            }
            temp.type="operand";
            temp.data = data;
            temp.left = null;
            temp.right = null;
                break;
        }
        }
        if(root.right != null)
        {
            if(root.right.left == null || root.right.right == null)
            {
                return null;
            }
            else
            {
            String tempInfix = "(" + infix + ")";
            infix = tempInfix + " " + "orderby" + " " + root.right.left.string + " " + root.right.right.string;
            data.set(0, se.sort(data, root.right.left.string, root.right.right.string));
            }
        }
        return data;
    }
    /**
     * Method to return the infix notation of the query.
     * @return 
     */
    public String getInfix()
    {
        return this.infix;
    }
   /**
    * Private class to represent a Node in the ParseTree.
    */
  private class Node 
   {
        String string;
        String type;
        ArrayList<ArrayList> data;
        Node left;
        Node right;
        
        private Node(String string, String type)
        {
            this.type = type;
            this.string = string;
        }
        
        private Node(String string)
        {
            switch(string)
            {
                case "+": this.type = "operator";
                          this.string = string;
                          break;
                    
                case "-": this.type = "operator";
                          this.string = string;
                          break;
                
                case "|": this.type = "operator";
                          this.string = string;
                          break;
                    
                case"orderby": this.type="order";
                               this.string = string;
                               break;
                
                default: this.type= "operand";
                         this.string = string;
                         break;
                    
            }
           
        }
    }
}
