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
 *Class to handle the sorting for our query-results.
 * The class uses SelectionSort.
 * @author Kim
 */
public class Sort 
{
    public ArrayList<Document> selectionSort(ArrayList<Document> documents, ArrayList<Double> relevance,
            ArrayList<Integer> occurance, ArrayList<Integer> popularity, String property, String direction)
    {
        if(property.equals("popularity"))
        {
            int[] popularity2 = new int[popularity.size()];
            Document[] documents2 = new Document[documents.size()];
            for(int i = 0; i < documents.size(); i++)
            {
                popularity2[i] = popularity.get(i);
                documents2[i] = documents.get(i);
                //StdOut.println(documents.get(i).toString() + " " + popularity.get(i));
            }
            return selectionSort(documents2, popularity2, direction);
        }
        if(property.equals("relevance"))
        {
            double[] relevance2 = new double[relevance.size()];
            Document[] documents2 = new Document[documents.size()];
            for(int i = 0; i < documents.size(); i++)
            {
                relevance2[i] = relevance.get(i);
                documents2[i] = documents.get(i);
                //StdOut.println(documents.get(i).toString() + " " + relevance.get(i));
            }
            return selectionSort(documents2, relevance2, direction);
        }
        if(property.equals("occurrence"))
        {
            int[] occurance2 = new int[occurance.size()];
            Document[] documents2 = new Document[documents.size()];
            for(int i = 0; i < documents.size(); i++)
            {
                occurance2[i] = occurance.get(i);
                documents2[i] = documents.get(i);
                //StdOut.println(documents.get(i).toString() + " " + occurance.get(i));
            }
            return selectionSort(documents2, occurance2, direction);
        }
        
        return null;
    }
    
    public ArrayList<Document> selectionSort(Document[] documents,int[] property,String direction)
    {
        ArrayList<Document> sortedDocs = new ArrayList();

        for(int i = 0; i<documents.length; i++)
        {
            int min = i;
            for(int j = i+1; j<documents.length; j++)
            {
                if(direction.equals("asc"))
                {
                if(property[j] < property[min])
                {
                    min = j;
                }
                }
                if(direction.equals("desc"))
                {
                  if(property[j] > property[min])
                {
                    min = j;
                }  
                }
            }
            sortedDocs.add(documents[min]);
            
            if (min != i) 
            {
            final int temp = property[i];
            property[i] = property[min];
            property[min] = temp;
            
            final Document temp2 = documents[i];
            documents[i] = documents[min];
            documents[min] = temp2;
            }
            
        }
        
        return sortedDocs;
    }
    
     public ArrayList<Document> selectionSort(Document[] documents,double[] property,String direction)
    {
        ArrayList<Document> sortedDocs = new ArrayList();

        for(int i = 0; i<documents.length; i++)
        {
            int min = i;
            for(int j = i+1; j<documents.length; j++)
            {
                if(direction.equals("asc"))
                {
                if(property[j] < property[min])
                {
                    min = j;
                }
                }
                if(direction.equals("desc"))
                {
                  if(property[j] > property[min])
                {
                    min = j;
                }  
                }
            }
            sortedDocs.add(documents[min]);
            
            if (min != i) 
            {
            final double temp = property[i];
            property[i] = property[min];
            property[min] = temp;
            
            final Document temp2 = documents[i];
            documents[i] = documents[min];
            documents[min] = temp2;
            }
            
        }
        
        return sortedDocs;
    }
    
    
}
