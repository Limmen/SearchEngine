/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;
import java.util.Map;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;

/**
 *Class to represent a object in the index.
 * Contains all relevant data and methods do update them if needed.
 * @author Kim
 */
public class IndexObject implements Comparable<IndexObject>
{
    private Word word;
    private ArrayList<Integer> relevance = new ArrayList();
    private ArrayList<Document> documents = new ArrayList();
    private ArrayList<Integer> occurrence = new ArrayList();
    private ArrayList<Integer> popularity = new ArrayList();
    private ArrayList<Attributes> attributes = new ArrayList();
    private ArrayList<Double> tfidf = new ArrayList();

    
    /**
     * Constructor to create a IndexObject of Word and Attribute
     * @param ord
     * @param attr 
     */
    public IndexObject(Word ord, Attributes attr)
    {
        this.word = ord;
        relevance.add(1);
        documents.add(attr.document);
        occurrence.add(attr.occurrence);
        popularity.add(attr.document.popularity);
        attributes.add(attr);
    }
    /**
     * Method to insert new documents and the data following.
     * @param attr 
     */
    public void insert(Attributes attr)
    {
        relevance.add(1);
        documents.add(attr.document);
        occurrence.add(attr.occurrence);
        popularity.add(attr.document.popularity);
        attributes.add(attr);   
    }
    /**
     * Method to insert new documents and specified relevance.
     * @param attr
     * @param rel 
     */
    public void insert(Attributes attr, int rel)
    {
        relevance.add(rel);
        documents.add(attr.document);
        occurrence.add(attr.occurrence);
        popularity.add(attr.document.popularity);
        attributes.add(attr);   
    }
    /**
     * Method to update the indexObject with new data.
     * @param attr 
     */
    public void update(Attributes attr)
    {
            int index = documents.indexOf(attr.document);
            if(index == -1)
            {
                insert(attr);
            }
            else
            {
            int oldRel = relevance.get(index);
            int oldOcc = occurrence.get(index);
            relevance.set(index, oldRel+1);
             if(oldOcc > attr.occurrence)
                occurrence.set(index, attr.occurrence);
            }
    }
    public void update(Attributes attr, int rel)
    {
        if(documents.contains(attr.document))
        {
            int index = attributes.indexOf(attr);
            relevance.set(index, rel);
        }
        if(!documents.contains(attr.document))
        {
            insert(attr, rel);
        }
    }
    /**
     * Method to calculate the term frequency-inverse document frequency(tf-idf).
     * @param documentData 
     */
    public void tfidf(Map<Document, Integer> documentData)
    {
        for(int i = 0; i< documents.size(); i++)
        {
            Document d = documents.get(i);
            double rel = relevance.get(i);
            double nrOfwords = documentData.get(d);
            double tf = rel/nrOfwords;
            double idf;
            if(documents.size()== 500)
                idf = Math.log10(500.00000001/(documents.size()));
            else
                idf = Math.log10(500.00000001/(documents.size()));
            double tfidfs = tf*idf;
            tfidf.add(tfidfs);
        }
    }
    
    public ArrayList<Document> getDocuments()
    {
        return documents;
    }
    public ArrayList<Double> getRelevance()
    {
        return tfidf;
    }
    public ArrayList<Integer> getOccurrence()
    {
        return occurrence;
    }
    public ArrayList<Integer> getPopularity()
    {
        return popularity;
    }
     public ArrayList<Attributes> getAttributes()
    {
        return attributes;
    }
    public ArrayList<Double> getTfidf()
    {
        return tfidf;
    }
    public String getWord()
    {
        return word.word;
    }
    @Override
    public int compareTo(IndexObject o) {
      return this.getWord().compareToIgnoreCase(o.getWord());
    }

}
