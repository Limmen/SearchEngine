package model;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.DataSource;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Sentence;
import view.GUI;
import edu.princeton.cs.introcs.In;

/**
 * Created by Mahmoud Ismail.
 */
public class MyDriver 
{
	GUI g = new GUI();
	
    public void run(TinySearchEngineBase se) throws Exception 
    {
        buildTheIndex(se);
        testTheEngine(se);
    }

     void testTheEngine(TinySearchEngineBase se) throws IOException {
        //In input = new In(new Scanner(System.in));
    	 Boolean bool = true;
        while (true) {
        	if(bool == true)
        	{
            System.out.print("Search: ");
            bool = false;
        	}
            if(g.checkQuery())
            {
            String query = g.getQuery();
            //String query = input.readLine();
            if (query.equals("?exit")) {
                break;
            }
            long t1 = System.nanoTime();
            List<Document> res = se.search(query);
            long e = System.nanoTime() - t1;
            if (res == null) {
                res = new ArrayList<Document>();
            }
            System.out.println("Infix: " + se.infix(query));
            System.out.println("got " + res.size() + " results in " + convertnanoTimeToString(e));
            for (Document r : res) {
                System.out.println(r);
            }
            bool = true;
            }
        }
    }

    static void buildTheIndex(final TinySearchEngineBase se) throws Exception {
        long t1 = System.nanoTime();
        se.preInserts();
        DataSource.run(new DataSource.SentenceHandler() {
            @Override
            public void handle(Sentence sentence, Attributes attr) {
                se.insert(sentence, attr);
            }
        });
        se.postInserts();
        long e = System.nanoTime() - t1;
        System.out.println("Building the index done in " + convertnanoTimeToString(e));
    }


    static String convertnanoTimeToString(long elapsed) {
        long min = TimeUnit.MINUTES.convert(elapsed, TimeUnit.NANOSECONDS);
        long remMin = elapsed - TimeUnit.NANOSECONDS.convert(min, TimeUnit.MINUTES);
        long sec = TimeUnit.SECONDS.convert(remMin, TimeUnit.NANOSECONDS);
        long remSec = remMin - TimeUnit.NANOSECONDS.convert(sec, TimeUnit.SECONDS);
        long milis = TimeUnit.MILLISECONDS.convert(remSec, TimeUnit.NANOSECONDS);
        long remMilis = remSec - TimeUnit.NANOSECONDS.convert(milis, TimeUnit.MILLISECONDS);
        long micros = TimeUnit.MICROSECONDS.convert(remMilis, TimeUnit.NANOSECONDS);
        long remMicros = remMilis - TimeUnit.NANOSECONDS.convert(micros, TimeUnit.MICROSECONDS);
        
        return min + "m " + sec + "s " + milis + "ms " + micros + "Âµs " + remMicros + "ns";
    }
}
