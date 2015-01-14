package model;

import edu.princeton.cs.introcs.StdOut;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import se.kth.id1020.Driver;
import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Sentence;
import se.kth.id1020.util.Word;
/**
 * This is a class that supports methods for creating a potentially huge Index 
 * of words.
 * The index provides functionality to search for different words to find what 
 * documents they appear in. The Index also provides a class ParseTre
 * which lets the user enter complex and nested queries where the following
 * methods are supported: Union (|), Intersection(+), Difference(-).
 * And ordering. Syntax: "Orderby relevance/popularity asc/desc.
 * The queries are entered in prefix-notation.
 * @author Kim
 */
public class TinySearchEngine implements TinySearchEngineBase
{
    private HashMap<Integer, IndexObject> myIndex;
    private Map<Document, Integer> documentData;
    private boolean sorted;
    private Sort sort;
    private BinarySearch search;
    private String infix;
    private ArrayList<CacheObject> unionCache = new ArrayList<>();
    private ArrayList<CacheObject> intersectionCache = new ArrayList<>();
    private ArrayList<CacheObject> differenceCache = new ArrayList<>();
    private ArrayList<CacheObject> simpleQueryCache = new ArrayList<>();
    /**
     * Constructor for the SearchEngine.
     */
    public TinySearchEngine()
    {
        this.myIndex = new HashMap();
        this.documentData = new HashMap();
        this.sorted = false;
        this.sort = new Sort();
        this.search = new BinarySearch();
    }
    /**
     * This is the main-method that calls the Driver.run(searchEngine) to read
     * the documents and insert the words in the index.
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
    TinySearchEngineBase searchEngine = new TinySearchEngine();
    MyDriver d = new MyDriver();
    d.run(searchEngine);
    }
    
    @Override
    public void preInserts() 
    {
        
    }
    /**
     * Method to insert words and relevant information in the index.
     * The index will only contain unique words so if the word already exists
     * in the index we just update the data in the IndexObject.
     * @param sntnc
     * @param atrbts 
     */
    @Override
    public void insert(Sentence sntnc, Attributes atrbts) 
    {
        for(Word w: sntnc.getWords())
        {
            int index = w.word.hashCode();
            if(myIndex.get(index) == null)
            {
                myIndex.put(index, new IndexObject(w, atrbts));   
            }
            else
            { 
                myIndex.get(index).update(atrbts);
            }
           if(!documentData.containsKey(atrbts.document))
           {
               documentData.put(atrbts.document, 1);
           }
           else
           {
               documentData.put(atrbts.document, documentData.get(atrbts.document) + 1);
           }
           
        }
    }
    /**
     * Method to build term frequency-inverse document frequency(tf-idf).
     */
    @Override
    public void postInserts() 
    {
            for(IndexObject i: myIndex.values())
            {
                i.tfidf(documentData);
            }
    }
    /**
     * Method that returns a infix-representation of the query.
     * @param string
     * @return 
     */
    @Override
    public String infix(String string) 
    {
        return infix;
    }
    /**
     * Method thats called when user enters a query. This method will
     * use a instance of ParseTree to interpret the query.
     * @param string
     * @return 
     */
        @Override
    public List<Document> search(String string)
    {   
        ArrayList<ArrayList> data = new ArrayList<ArrayList>();
        data.add(new ArrayList<Document>()); //documents
        data.add(new ArrayList<Double>()); //relevance
        data.add(new ArrayList<Integer>()); //occurrence
        data.add(new ArrayList<Integer>()); //popularity
        
        ParseTree tree = new ParseTree(this);
        
        String inputarray[]= StringUtils.split(string); //Splits the query on " " 
        if(inputarray.length == 0)
        {
            errorMsg();
            return null;
        }
        
        for(int i = 0; i<inputarray.length; i++) //Inserts the operators/operands in the parsetree.
        {
            tree.insert(inputarray[i]);
        }
        data = tree.execute(); //executes the parsetree (the difference operations
                               //are executed and data is returned.
        this.infix = tree.getInfix(); //gets the infix-notation.

        if(data != null)
        {
        return data.get(0);
        }
        return null;
    }
    /**
     * Method to do a simpleQuery-search in the index.
     * This methods simply takes a word as parameter and if that word
     * exists in the index, all relevant data is returned.
     * @param word
     * @return 
     */
    public ArrayList<ArrayList> simpleQuery(String word)
    {
        ArrayList<ArrayList> data = new ArrayList();

        IndexObject obj = myIndex.get(word.hashCode());
        if(obj == null)
        {
            errorMsg();
            return null;
        }
        data.add(new ArrayList<Document>(obj.getDocuments()));
        data.add(new ArrayList<Double>(obj.getRelevance()));
        data.add(new ArrayList<Integer>(obj.getOccurrence()));
        data.add(new ArrayList<Integer>(obj.getPopularity()));
        return data;
    }
    
    /**
     * Method to use the union-functionality on the search result, which means
     * that the user can search for multiple words and get a combined result.
     * @param word
     * @param data
     * @return data
     */
      public ArrayList<ArrayList> union(ArrayList<ArrayList> data, ArrayList<ArrayList> data2)
    {
                    if(data == null || data2 == null)
                    {
                        return null;
                    }
                    ArrayList<Document> docs = data.get(0);
                    ArrayList<Double> relevance = data.get(1);
                    ArrayList<Integer> occurrence = data.get(2);
                    ArrayList<Integer> popularity = data.get(3);
                    
                    ArrayList<Document> docs2 = data2.get(0);
                    ArrayList<Double> relevance2 = data2.get(1);
                    ArrayList<Integer> occurrence2 = data2.get(2);
                    ArrayList<Integer> popularity2 = data2.get(3);
                    
                    ArrayList<Document> docs3 = new ArrayList<>();
                    ArrayList<Double> relevance3 = new ArrayList<>();
                    ArrayList<Integer> occurrence3 = new ArrayList<>();
                    ArrayList<Integer> popularity3 = new ArrayList<>();
                    
                    for(int i = 0; i < docs.size(); i++)
                    {
                        docs3.add(docs.get(i));
                        relevance3.add(relevance.get(i));
                        occurrence3.add(occurrence.get(i));
                        popularity3.add(popularity.get(i));
                    }
                    
                    for(Document d: docs2)
                    {
                    if(!docs3.contains(d))
                    {
                        docs3.add(d);
                        int docIndex = docs2.indexOf(d);
                        relevance3.add(relevance2.get(docIndex));
                        occurrence3.add(occurrence2.get(docIndex));
                        popularity3.add(d.popularity);
                    }
                    else
                    {
                        int docIndex = docs3.indexOf(d);
                        int docIndex2 = docs2.indexOf(d);
                        double oldRel = (double) relevance3.get(docIndex);
                        int oldOcc = (int) occurrence3.get(docIndex);
                        relevance3.set(docIndex, oldRel+relevance2.get(docIndex2));
                        if(oldOcc > occurrence2.get(docIndex2))
                        {
                        occurrence3.set(docIndex, occurrence2.get(docIndex2));
                        }
                    }    
                    }
                    ArrayList<ArrayList> data3 = new ArrayList();
                    data3.add(docs3);
                    data3.add(relevance3);
                    data3.add(occurrence3);
                    data3.add(popularity3);
                    return data3;                    
    }
      /**
       * Method to use the intersection operation on two ArrayList containing
       * data.
       * @param data
       * @param data2
       * @return 
       */
      public ArrayList<ArrayList> intersection (ArrayList<ArrayList> data, ArrayList<ArrayList> data2)
      {
                    ArrayList<Document> docs = data.get(0);
                    ArrayList<Double> relevance = data.get(1);
                    ArrayList<Integer> occurrence = data.get(2);
                    ArrayList<Integer> popularity = data.get(3);
                    
                    ArrayList<Document> docs2 = data2.get(0);
                    ArrayList<Double> relevance2 = data2.get(1);
                    ArrayList<Integer> occurrence2 = data2.get(2);
                    ArrayList<Integer> popularity2 = data2.get(3);
                    
                    ArrayList<Document> docs3 = new ArrayList<>();
                    ArrayList<Double> relevance3 = new ArrayList<>();
                    ArrayList<Integer> occurrence3 = new ArrayList<>();
                    ArrayList<Integer> popularity3 = new ArrayList<>();
                    
                    for(Document d: docs)
                    {
                    if(docs2.contains(d))
                    {
                        docs3.add(d);
                        int docIndex = docs.indexOf(d);
                        int docIndex2 = docs2.indexOf(d);
                        double oldRel = (double) relevance.get(docIndex);
                        int oldOcc = (int) occurrence.get(docIndex);
                        relevance3.add(oldRel+relevance2.get(docIndex2));
                        if(oldOcc > occurrence2.get(docIndex2))
                        {
                        occurrence3.add(occurrence2.get(docIndex2));
                        }
                        else
                        {
                           occurrence3.add(oldOcc);
                        }
                        popularity3.add(d.popularity);
                    }
   
                    }
                    data = new ArrayList<ArrayList> ();
                    data.add(docs3);
                    data.add(relevance3);
                    data.add(occurrence3);
                    data.add(popularity3);
                    
                    return data;
      }
      /**
       * Method to do the difference operation on two arraylists containing
       * data.
       * @param data
       * @param data2
       * @return 
       */
      public ArrayList<ArrayList> difference (ArrayList<ArrayList> data, ArrayList<ArrayList> data2)
      {
                    ArrayList<Document> docs = data.get(0);
                    ArrayList<Double> relevance = data.get(1);
                    ArrayList<Integer> occurrence = data.get(2);
                    ArrayList<Integer> popularity = data.get(3);
                    
                    ArrayList<Document> docs2 = data2.get(0);
                    ArrayList<Double> relevance2 = data2.get(1);
                    ArrayList<Integer> occurrence2 = data2.get(2);
                    ArrayList<Integer> popularity2 = data2.get(3);
                    
                    ArrayList<Document> docs3 = new ArrayList<>();
                    ArrayList<Double> relevance3 = new ArrayList<>();
                    ArrayList<Integer> occurrence3 = new ArrayList<>();
                    ArrayList<Integer> popularity3 = new ArrayList<>();
                    
                    for(Document d: docs)
                    {
                    if(!docs2.contains(d))
                    {
                        int docIndex = docs.indexOf(d);
                        docs3.add(d);
                        relevance3.add(relevance.get(docIndex));
                        occurrence3.add(occurrence.get(docIndex));
                        popularity3.add(d.popularity);
                    }
   
                    }
                    data = new ArrayList<ArrayList> ();
                    data.add(docs3);
                    data.add(relevance3);
                    data.add(occurrence3);
                    data.add(popularity3);
                    
                    return data;
      }
      /**
       * Method to sort the query result on specified property/direction.
       * @param data
       * @param property
       * @param direction
       * @return 
       */
      public ArrayList<Document> sort(ArrayList<ArrayList> data, String property, String direction)
      {
          if(data == null)
          {
              return null;
          }
          ArrayList<Document> result = sort.selectionSort(data.get(0), data.get(1), data.get(2), data.get(3), property, direction);
          if(result != null)
              return result;
          else
          {
              return null;
          }
      }
      /**
       * Method to update cache-memory with a single word as operand.
       * @param data
       * @param operator
       * @param operand1 
       */
      public void updateCache(ArrayList<ArrayList> data, String operator, String operand1)
      {
          if(operator.equalsIgnoreCase("SimpleQuery"))
          {
               simpleQueryCache.add(new CacheObject(data, operator, operand1));
          }
      }
      /**
       * Method to update cachememory with two arraylists as operands, 
       * a operator and the query-result.
       * @param data
       * @param operator
       * @param operand1
       * @param operand2 
       */
      public void updateCache(ArrayList<ArrayList> data, String operator, ArrayList operand1, ArrayList operand2)
      {
          if(operator.equalsIgnoreCase("|"))
          {
              unionCache.add(new CacheObject(data, operator, operand1, operand2));
          }
          
          if(operator.equalsIgnoreCase("+"))
          {
               intersectionCache.add(new CacheObject(data, operator, operand1, operand2));
          }
          if(operator.equalsIgnoreCase("-"))
          {
               differenceCache.add(new CacheObject(data, operator, operand1, operand2));
          }
      }
     /**
      * Method to search the cachememory, if the we get a cache-hit we can
      * improve the performance of queries.
      * @param operator
      * @param operand1
      * @param operand2
      * @return 
      */
     public ArrayList<ArrayList> searchCache(String operator, ArrayList<ArrayList> operand1, ArrayList<ArrayList> operand2)
      {
              if(operand1 == null || operand2 == null)
              return null;
           if(operator.equalsIgnoreCase("|"))
          {
              for(CacheObject c: unionCache)
              {
                  if(c.equals(operand1, operand2))
                  {
                      return c.getResult();
                  }
              }
          }
          
          if(operator.equalsIgnoreCase("+"))
          {
              for(CacheObject c: intersectionCache)
              {
                  if(c.equals(operand1, operand2))
                  {
                      return c.getResult();
                  }
              }
          }
          if(operator.equalsIgnoreCase("-"))
          {
               for(CacheObject c: differenceCache)
              {
                  if(c.equals(operand1, operand2))
                  {
                      return c.getResult();
                  }
              }
          }
          return null;
      }
     /**
      * Alternative search-cache method that only takes a String operand
      * as parameter.
      * @param operand1
      * @return 
      */
      public ArrayList<ArrayList> searchCache(String operand1)
      {
              for(CacheObject c: simpleQueryCache)
              {
                  if(c.equals(operand1))
                  {
                      return c.getResult();
                  }
              }
                    return null;
      }
    /**
     * Error-Message method for error-handling to the user.
     */
    public void errorMsg()
    {
            StdOut.println("That word does not exist in the index /Invalid query");
    }
        
}

    