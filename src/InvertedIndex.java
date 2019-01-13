import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class InvertedIndex 
{
	private SortedList[] hashArray;  
	private int arraySize;

	public InvertedIndex(int size) {
		arraySize = size;
		hashArray = new SortedList[arraySize];  
		for(int j=0; j<arraySize; j++)          
			hashArray[j] = new SortedList();
	}

	public void build(String sPath) {
		int nDocId = 0;
		File folder = new File(sPath);
		File[] listOfFolders = folder.listFiles(File::isDirectory); 
		for (int i=0 ; i < listOfFolders.length ; i++) {
			File[] listOfFiles = listOfFolders[i].listFiles();
			for (int j=0 ; j < listOfFiles.length ; j++) {
				if (listOfFiles[j].isFile()) {
					nDocId++;
					parseFile(listOfFiles[j], nDocId);
				}
			}
		}
	}

	public void parseFile(File f, int nDocId) 
	{
		BufferedReader br = null;
		try {
			 br = new BufferedReader(new FileReader(f)); //read the data from the file
			String currentLine ;
			
			while((currentLine = br.readLine()) != null) //while exists a line
			{
				StringTokenizer st = new StringTokenizer(currentLine);//seperate the word with StringTokenizer
				
				while (st.hasMoreTokens()) { //while exist words
					
					 String str = st.nextToken();
					 StringBuilder strBldr = new StringBuilder();
					 String word = null;
					 
					 for(int i=0; i<str.length();i++)
			    	 {
			    	   
					    if( str.charAt(i) != '!' && str.charAt(i) != '.' && str.charAt(i) != ',' 
						 && str.charAt(i) != '/' && str.charAt(i) != ';' && str.charAt(i) != '?' 
						 && str.charAt(i) != '"' && str.charAt(i) != '\''
						 && str.charAt(i) != '(' && str.charAt(i) != ')') { // if the word isn't contain a punctuation
					    	                                                //or the word isn't a punctuation
					    	strBldr.append(str.charAt(i));               //add each character to stringBuilder
		    	            word = strBldr.toString();                   //finally store this as a string
			            }                                                     
			         }
					 
					if(word != null) addToInvIndex(word,nDocId); //if the word is not null, add it to hash-table
				}
			}
		}catch(IOException exception0)
		{
			System.err.println("The file is terminating or file is not found!!!");
			exception0.printStackTrace();
		}finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}	
	}

	private int hashFunc(String sTerm) {
		return Math.abs(sTerm.hashCode()) % arraySize;
	}

	private void addToInvIndex(String sTerm, int nDocId) //add an term on hash-table
	{
		int hashValue = hashFunc(sTerm);
		Node current = hashArray[hashValue].find(sTerm, nDocId);
		
		if(current == null) hashArray[hashValue].insert(nDocId, sTerm,1); //if the sterm isn't exist,then insert it
		else current.nCount++; //else increase the number of displays on this doc
		
		return;
	}

	public Integer[] search(String sTermA, String sTermB)  // search and find the id of termA & termB
	{
           int hashValueA = hashFunc(sTermA); 
           int hashValueB = hashFunc(sTermB);
           
           Integer[] idA = new Integer[hashArray[hashValueA].getLength()+1];
           idA = hashArray[hashValueA].find(sTermA); //return the array of ids of docs with stermA
  
           Integer[] idB = new Integer[hashArray[hashValueB].getLength()+1];
           idB = hashArray[hashValueB].find(sTermB); //return the array of ids  of docs with stermB
           
           if(idA[0] != -1 && idB[0] != -1) //if the stermA and sTermB exist 
		   {
            boolean contains = false;
			List<Integer> idRecords = new ArrayList<Integer>();

            for(int i=0; i<idA.length;i++) idRecords.add(idA[i]); //adds all the records of termA in a List
            
            for(int i=0; i<idB.length;i++)
            {
            	for(Integer id:idRecords)         // checks if the list contains the same id
            	{
            		if(idB[i] == id)             //if id is the same , then it isn't added again
            		{
            			contains=true;
            			break;
            		}
            		contains = false;
            	}
            	
            	if(contains == false) idRecords.add(idB[i]); //if id isn't the same ,then add it  
            }
            
            Integer[] idRec  = new Integer[idRecords.size()]; 
            for(int i=0; i<idRecords.size();i++) idRec[i] = idRecords.get(i); // insert the ids of list on an array type Integer and return it
            return idRec;
		}
		else if(idA[0] != -1 && idB[0] == -1) //if the sTermA exists and sTermB isn't exist,then return only the array with stermA ids
		return idA;
	
		return new Integer[]{-1}; //if the stermA and sTermB aren't exist, then return an array type Integer with an invalid id
	}

	public Integer[] search(String sTerm, int k) { //search and find the top-k docs ids!
		int hashVal = hashFunc(sTerm);
		return hashArray[hashVal].findTopK(sTerm, k);
	}
		
	public static void main(String[] args) {
		
		InvertedIndex invIndex100Lists= new InvertedIndex(100);
		InvertedIndex invIndex500Lists = new InvertedIndex(500);
		InvertedIndex invIndex1000Lists = new InvertedIndex(1000);
		InvertedIndex invIndex2500Lists = new InvertedIndex(2500);
		InvertedIndex invIndex5000Lists = new InvertedIndex(5000);
		
/*------------------------------Test Inverted Index of 100 Lists-------------------------------------------------------------*/
		long time0 = 0;
	    for(int i=0;i<50;i++)
	    {
	    	long startTime = System.nanoTime();// hold the start time
	    	invIndex100Lists.build("C:\\Users\\kouko\\OneDrive\\Υπολογιστής\\workspace-eclipse-javaSE8\\Index\\src\\data");
	    	long stopTime = System.nanoTime();// hold the stop time
	    	time0 += stopTime - startTime; // counts the duration of build execution
	    }
		
	    int max0 = invIndex100Lists.hashArray[0].getLength();
	    int min0 = invIndex100Lists.hashArray[0].getLength();
	    double sumLength0 = 0;
	    
	    for(int i=1;i<invIndex100Lists.hashArray.length;i++) {
	    	if(invIndex100Lists.hashArray[i].getLength() > max0) max0 = invIndex100Lists.hashArray[i].getLength(); //find max length of nodes 
	    	if(invIndex100Lists.hashArray[i].getLength() < min0) min0 = invIndex100Lists.hashArray[i].getLength(); //find min length of nodes
	    	sumLength0 += invIndex100Lists.hashArray[i].getLength();
	    }
	    
	    double avgLength0 = sumLength0/100; //the average length of nodes
	    
	    long timeRareSearch0 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeRareSearch0 = System.nanoTime();// hold the start time
	    	invIndex100Lists.search("coconut", "Philippine");
	    	long stopTimeRareSearch0 = System.nanoTime();// hold the stop time
	    	timeRareSearch0 += stopTimeRareSearch0 - startTimeRareSearch0; // counts the duration of rare search execution
	    }
	    
	    long avgRareSearch0 = timeRareSearch0/50;

	    long  timeMoreFrequencySearch0 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeMoreFrequencySearch0 = System.nanoTime();// hold the start time
	    	invIndex100Lists.search("January", "February");
	    	long stopTimeMoreFrequencySearch0 = System.nanoTime();// hold the stop time
	    	 timeMoreFrequencySearch0 += stopTimeMoreFrequencySearch0 - startTimeMoreFrequencySearch0; //counts the duration of more frequent search execution
	    }
	    
	    long avgMoreFrequencySearch0 =  timeMoreFrequencySearch0/50;
	    
	    long  timeMostFrequencySearch0 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeMostFrequencySearch0 = System.nanoTime();// hold the start time
	    	invIndex100Lists.search("the", "dlrs");
	    	long stopTimeMostFrequencySearch0 = System.nanoTime();// hold the stop time
	    	timeMostFrequencySearch0 += stopTimeMostFrequencySearch0 - startTimeMostFrequencySearch0; //counts the duration of most frequent search execution
	    }
	    
	    long avgMostFrequencySearch0 = timeMostFrequencySearch0/50;
	    
	    long timeOfTop10Search0 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop10Search0 = System.nanoTime();// hold the start time
	    	invIndex100Lists.search("dlrs", 10);
	    	long stopTimeOfTop10Search0 = System.nanoTime();// hold the stop time
	    	timeOfTop10Search0 += stopTimeOfTop10Search0 - startTimeOfTop10Search0; //counts the duration of search top-10 docs execution
	    }
	    
	    long avgTimeOfTop10Search0 = timeOfTop10Search0/50;
	    
	    long timeOfTop100Search0 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop100Search0 = System.nanoTime();// hold the start time
	    	invIndex100Lists.search("dlrs", 100);
	    	long stopTimeOfTop100Search0 = System.nanoTime();// hold the stop time
	    	timeOfTop100Search0 += stopTimeOfTop100Search0 - startTimeOfTop100Search0; //counts the duration of search top-100 docs execution
	    }
	    
	    long avgTimeOfTop100Search0 = timeOfTop100Search0/50;
	    
	    long timeOfTop1000Search0 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop1000Search0 = System.nanoTime();// hold the start time
	    	invIndex100Lists.search("dlrs", 1000);
	    	long stopTimeOfTop1000Search0 = System.nanoTime();// hold the stop time
	    	timeOfTop1000Search0 += stopTimeOfTop1000Search0 - startTimeOfTop1000Search0; //counts the duration of search top-1000 docs execution
	    }
	    
	    long avgTimeOfTop1000Search0 = timeOfTop1000Search0/50;
/*--------------------------------------------------------------------------------------------------------------------------*/
	    
	    
/*------------------------------Test Inverted Index of 500 Lists------------------------------------------------------------*/  
	    long time1 = 0;
	    for(int i=0;i<50;i++)
	    {
	    	long startTime = System.nanoTime();// hold the start time
	    	invIndex500Lists.build("C:\\Users\\kouko\\OneDrive\\Υπολογιστής\\workspace-eclipse-javaSE8\\Index\\src\\data");
	    	long stopTime = System.nanoTime();// hold the stop time
	    	time1 += stopTime - startTime; // counts the duration of build execution
	    }
	   
	    int max1 = invIndex500Lists.hashArray[0].getLength();
	    int min1 = invIndex500Lists.hashArray[0].getLength();
	    double sumLength1 = 0;
	    
	    for(int i=1;i<invIndex500Lists.hashArray.length;i++) {
	    	if(invIndex500Lists.hashArray[i].getLength() > max1) max1 = invIndex500Lists.hashArray[i].getLength(); //find max length of nodes
	    	if(invIndex500Lists.hashArray[i].getLength() < min1) min1 = invIndex500Lists.hashArray[i].getLength(); //find min length of nodes
	    	sumLength1 += invIndex500Lists.hashArray[i].getLength();
	    }
	    
	    double avgLength1 = sumLength1/500; //the average length of nodes
	    
	    long timeRareSearch1 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeRareSearch1 = System.nanoTime();// hold the start time
	    	invIndex500Lists.search("coconut", "Philippine");
	    	long stopTimeRareSearch1 = System.nanoTime();// hold the stop time
	    	timeRareSearch1 += stopTimeRareSearch1 - startTimeRareSearch1; // counts the duration of rare search execution
	    }
	    
	    long avgRareSearch1 = timeRareSearch1/50;
	    
	    long  timeMoreFrequencySearch1 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeMoreFrequencySearch1 = System.nanoTime();// hold the start time
	    	invIndex500Lists.search("January", "February");
	    	long stopTimeMoreFrequencySearch1 = System.nanoTime();// hold the stop time
	    	timeMoreFrequencySearch1 += stopTimeMoreFrequencySearch1 - startTimeMoreFrequencySearch1; //counts the duration of more frequent search execution
	    }
	    
	    long avgMoreFrequencySearch1 = timeMoreFrequencySearch1/50;
	    
	    
	    long timeMostFrequencySearch1 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeMostFrequencySearch1 = System.nanoTime();// hold the start time
	    	invIndex500Lists.search("the", "dlrs");
	    	long stopTimeMostFrequencySearch1 = System.nanoTime();// hold the stop time
	    	timeMostFrequencySearch1 += stopTimeMostFrequencySearch1 - startTimeMostFrequencySearch1; //counts the duration of most frequent search execution
	    }
	    
	    long avgMostFrequencySearch1 = timeMostFrequencySearch1/50;
	    
	    long timeOfTop10Search1 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop10Search1 = System.nanoTime();// hold the start time
	    	invIndex500Lists.search("dlrs", 10);
	    	long stopTimeOfTop10Search1 = System.nanoTime();// hold the stop time
	    	timeOfTop10Search1 += stopTimeOfTop10Search1 - startTimeOfTop10Search1; //counts the duration of search top-10 docs execution
	    }
	    
	    long avgTimeOfTop10Search1 = timeOfTop10Search1/50;
  
	    long timeOfTop100Search1 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop100Search1 = System.nanoTime();// hold the start time
	    	invIndex500Lists.search("dlrs", 100);
	    	long stopTimeOfTop100Search1 = System.nanoTime();// hold the stop time
	    	timeOfTop100Search1 += stopTimeOfTop100Search1 - startTimeOfTop100Search1; //counts the duration of search top-100 docs execution
	    }
	    
	    long avgTimeOfTop100Search1 = timeOfTop100Search1/50;
	    
	    long timeOfTop1000Search1 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop1000Search1 = System.nanoTime();// hold the start time
	    	invIndex500Lists.search("dlrs", 1000);
	    	long stopTimeOfTop1000Search1 = System.nanoTime();// hold the stop time
	    	timeOfTop1000Search1 += stopTimeOfTop1000Search1 - startTimeOfTop1000Search1; //counts the duration of search top-100 docs execution
	    }
	    
	    long avgTimeOfTop1000Search1 = timeOfTop1000Search1/50;
	    
/*-------------------------------------------------------------------------------------------------------------------------*/
	  
	    
/*------------------------------Test Inverted Index of 1000 Lists------------------------------------------------------------*/    
	    long time2 = 0;
	    for(int i=0;i<50;i++)
	    {
	    	long startTime = System.nanoTime();// hold the start time
	    	invIndex1000Lists.build("C:\\Users\\kouko\\OneDrive\\Υπολογιστής\\workspace-eclipse-javaSE8\\Index\\src\\data");
	    	long stopTime = System.nanoTime();// hold the stop time
	    	time2 += stopTime - startTime; // counts the duration of build execution
	    }
	    
	    int max2 = invIndex1000Lists.hashArray[0].getLength();
	    int min2 = invIndex1000Lists.hashArray[0].getLength();
	    double sumLength2 = 0;
	    
	    for(int i=1;i<invIndex1000Lists.hashArray.length;i++) {
	    	if(invIndex1000Lists.hashArray[i].getLength() > max2) max2 = invIndex1000Lists.hashArray[i].getLength(); //find max length of nodes
	    	if(invIndex1000Lists.hashArray[i].getLength() < min2) min2 = invIndex1000Lists.hashArray[i].getLength(); //find min length of nodes
	    	sumLength2 += invIndex1000Lists.hashArray[i].getLength();
	    }
	    
	    double avgLength2 = sumLength2/1000; //the average length of nodes
	    
	    long timeRareSearch2 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeRareSearch2 = System.nanoTime();// hold the start time
	    	invIndex1000Lists.search("coconut", "Philippine");
	    	long stopTimeRareSearch2 = System.nanoTime();// hold the stop time
	    	timeRareSearch2 += stopTimeRareSearch2 - startTimeRareSearch2; // counts the duration of rare search execution
	    }
	    
	    long avgRareSearch2 = timeRareSearch2/50;
	    
	    long  timeMoreFrequencySearch2 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeMoreFrequencySearch2 = System.nanoTime();// hold the start time
	    	invIndex1000Lists.search("January", "February");
	    	long stopTimeMoreFrequencySearch2 = System.nanoTime();// hold the stop time
	    	timeMoreFrequencySearch2 += stopTimeMoreFrequencySearch2 - startTimeMoreFrequencySearch2; //counts the duration of more frequent search execution
	    }
	    
	    long avgMoreFrequencySearch2 = timeMoreFrequencySearch2/50;
	    
	    long  timeMostFrequencySearch2 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeMostFrequencySearch2 = System.nanoTime();// hold the start time
	    	invIndex1000Lists.search("the", "dlrs");
	    	long stopTimeMostFrequencySearch2 = System.nanoTime();// hold the stop time
	    	timeMostFrequencySearch2 += stopTimeMostFrequencySearch2 - startTimeMostFrequencySearch2; //counts the duration of most frequent search execution
	    }
	    
	    long avgMostFrequencySearch2 = timeMostFrequencySearch2/50;
	    
	    long timeOfTop10Search2 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop10Search2 = System.nanoTime();// hold the start time
	    	invIndex1000Lists.search("dlrs", 10);
	    	long stopTimeOfTop10Search2 = System.nanoTime();// hold the stop time
	    	timeOfTop10Search2 += stopTimeOfTop10Search2 - startTimeOfTop10Search2; //counts the duration of search top-10 docs execution
	    }
	    
	    long avgTimeOfTop10Search2 = timeOfTop10Search2/50;
	    
	    long timeOfTop100Search2 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop100Search2 = System.nanoTime();// hold the start time
	    	invIndex1000Lists.search("dlrs", 100);
	    	long stopTimeOfTop100Search2 = System.nanoTime();// hold the stop time
	    	timeOfTop100Search2 += stopTimeOfTop100Search2 - startTimeOfTop100Search2; //counts the duration of search top-100 docs execution
	    }
	    
	    long avgTimeOfTop100Search2 = timeOfTop100Search2/50;
	    
	    long timeOfTop1000Search2 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop1000Search2 = System.nanoTime();// hold the start time
	    	invIndex1000Lists.search("dlrs", 1000);
	    	long stopTimeOfTop1000Search2 = System.nanoTime();// hold the stop time
	    	timeOfTop1000Search2 += stopTimeOfTop1000Search2 - startTimeOfTop1000Search2; //counts the duration of search top-1000 docs execution
	    }
	    
	    long avgTimeOfTop1000Search2 = timeOfTop1000Search2/50;
	    
/*--------------------------------------------------------------------------------------------------------------------------*/   
		
/*------------------------------Test Inverted Index of 2500 Lists-----------------------------------------------------------*/  
	    long time3 = 0;
	    for(int i=0;i<50;i++)
	    {
	    	long startTime = System.nanoTime();// hold the start time
	    	invIndex2500Lists.build("C:\\Users\\kouko\\OneDrive\\Υπολογιστής\\workspace-eclipse-javaSE8\\Index\\src\\data");
	    	long stopTime = System.nanoTime();// hold the stop time
	    	time3 += stopTime - startTime; // counts the duration of build execution
	    }
	    
	    int max3 = invIndex2500Lists.hashArray[0].getLength();
	    int min3 = invIndex2500Lists.hashArray[0].getLength();
	    double sumLength3 = 0;
	    
	    for(int i=1;i<invIndex2500Lists.hashArray.length;i++) {
	    	if(invIndex2500Lists.hashArray[i].getLength() > max3) max3 = invIndex2500Lists.hashArray[i].getLength(); //find max length of nodes
	    	if(invIndex2500Lists.hashArray[i].getLength() < min3) min3 = invIndex2500Lists.hashArray[i].getLength(); //find min length of nodes
	    	sumLength3 += invIndex2500Lists.hashArray[i].getLength();
	    }
	    
	    double avgLength3 = sumLength3/2500; //the average length of nodes
	    
	    long timeRareSearch3 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeRareSearch3 = System.nanoTime();// hold the start time
	    	invIndex2500Lists.search("coconut", "Philippine");
	    	long stopTimeRareSearch3 = System.nanoTime();// hold the stop time
	    	timeRareSearch3 += stopTimeRareSearch3 - startTimeRareSearch3; //counts the duration of rare search execution
	    }
	    
	    long avgRareSearch3 = timeRareSearch3/50;
	    
	    long  timeMoreFrequencySearch3 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeMoreFrequencySearch3 = System.nanoTime();// hold the start time
	    	invIndex2500Lists.search("January", "February");
	    	long stopTimeMoreFrequencySearch3 = System.nanoTime();// hold the stop time
	    	timeMoreFrequencySearch3 += stopTimeMoreFrequencySearch3 - startTimeMoreFrequencySearch3; //counts the duration of more frequent search execution
	    }
	    
	    long avgMoreFrequencySearch3 = timeMoreFrequencySearch3/50;
	    
	    long  timeMostFrequencySearch3 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeMostFrequencySearch3 = System.nanoTime();// hold the start time
	    	invIndex2500Lists.search("the", "dlrs");
	    	long stopTimeMostFrequencySearch3 = System.nanoTime();// hold the stop time
	    	timeMostFrequencySearch3 += stopTimeMostFrequencySearch3 - startTimeMostFrequencySearch3; //counts the duration of most frequent search execution
	    }
	    
	    long avgMostFrequencySearch3 = timeMostFrequencySearch3/50;
	    
	    long timeOfTop10Search3 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop10Search3 = System.nanoTime();// hold the start time
	    	invIndex2500Lists.search("dlrs", 10);
	    	long stopTimeOfTop10Search3 = System.nanoTime();// hold the stop time
	    	timeOfTop10Search3 += stopTimeOfTop10Search3 - startTimeOfTop10Search3; //counts the duration of search top-10 docs execution
	    }
	    
	    long avgTimeOfTop10Search3 = timeOfTop10Search3/50;
	    
	    
	    long timeOfTop100Search3 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop100Search3 = System.nanoTime();// hold the start time
	    	invIndex2500Lists.search("dlrs", 100);
	    	long stopTimeOfTop100Search3 = System.nanoTime();// hold the stop time
	    	timeOfTop100Search3 += stopTimeOfTop100Search3 - startTimeOfTop100Search3; //counts the duration of search top-100 docs execution
	    }
	    
	    long avgTimeOfTop100Search3 = timeOfTop100Search3/50;
	    
	    long timeOfTop1000Search3 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop1000Search3 = System.nanoTime();// hold the start time
	    	invIndex2500Lists.search("dlrs", 1000);
	    	long stopTimeOfTop1000Search3 = System.nanoTime();// hold the stop time
	    	timeOfTop1000Search3 += stopTimeOfTop1000Search3 - startTimeOfTop1000Search3; //counts the duration of search top-1000 docs execution
	    }
	    
	    long avgTimeOfTop1000Search3 = timeOfTop1000Search3/50;
	    
/*-------------------------------------------------------------------------------------------------------------------------*/     
	    
/*------------------------------Test Inverted Index of 5000 Lists----------------------------------------------------------*/   
	    
	    long time4 = 0;
	    for(int i=0;i<50;i++)
	    {
	    	long startTime = System.nanoTime();// hold the start time
	    	invIndex5000Lists.build("C:\\Users\\kouko\\OneDrive\\Υπολογιστής\\workspace-eclipse-javaSE8\\Index\\src\\data");
	    	long stopTime = System.nanoTime();// hold the stop time
	    	time4 += stopTime - startTime; // counts the duration of build execution
	    }
	    
	    int max4 = invIndex5000Lists.hashArray[0].getLength();
	    int min4 = invIndex5000Lists.hashArray[0].getLength();
	    double sumLength4 = 0;
	    
	    for(int i=1;i<invIndex5000Lists.hashArray.length;i++) {
	    	if(invIndex5000Lists.hashArray[i].getLength() > max4) max4 = invIndex5000Lists.hashArray[i].getLength(); //find max length of nodes
	    	if(invIndex5000Lists.hashArray[i].getLength() < min4) min4 = invIndex5000Lists.hashArray[i].getLength(); //find min length of nodes
	    	sumLength4 += invIndex5000Lists.hashArray[i].getLength();
	    }
	    
	    double avgLength4 = sumLength4/5000; //the average length of nodes
	    
	    long timeRareSearch4 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeRareSearch4 = System.nanoTime();// hold the start time
	    	invIndex5000Lists.search("coconut", "Philippine");
	    	long stopTimeRareSearch4 = System.nanoTime();// hold the stop time
	    	timeRareSearch4 += stopTimeRareSearch4 - startTimeRareSearch4; //counts the duration of rare search execution
	    }
	    
	    long avgRareSearch4 = timeRareSearch4/50;
	    
	    long timeMoreFrequencySearch4 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeMoreFrequencySearch4 = System.nanoTime();// hold the start time
	    	invIndex5000Lists.search("January", "February");
	    	long stopTimeMoreFrequencySearch4 = System.nanoTime();// hold the stop time
	    	timeMoreFrequencySearch2 += stopTimeMoreFrequencySearch4 - startTimeMoreFrequencySearch4; //counts the duration of more frequent search execution
	    }
	    
	    long avgMoreFrequencySearch4 = timeMoreFrequencySearch4/50;
	    
	    long timeMostFrequencySearch4 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeMostFrequencySearch4 = System.nanoTime();// hold the start time
	    	invIndex5000Lists.search("the", "dlrs");
	    	long stopTimeMostFrequencySearch4 = System.nanoTime();// hold the stop time
	    	timeMostFrequencySearch4 += stopTimeMostFrequencySearch4 - startTimeMostFrequencySearch4; //counts the duration of most frequent search execution
	    }
	    
	    long avgMostFrequencySearch4 = timeMostFrequencySearch4/50;
	    
	    long timeOfTop10Search4 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop10Search4 = System.nanoTime();// hold the start time
	    	invIndex5000Lists.search("dlrs", 10);
	    	long stopTimeOfTop10Search4 = System.nanoTime();// hold the stop time
	    	timeOfTop10Search4 += stopTimeOfTop10Search4 - startTimeOfTop10Search4; //counts the duration of search top-10 docs execution
	    }
	    
	    long avgTimeOfTop10Search4 = timeOfTop10Search4/50;
	    
	    long timeOfTop100Search4 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop100Search4 = System.nanoTime();// hold the start time
	    	invIndex5000Lists.search("dlrs", 100);
	    	long stopTimeOfTop100Search4 = System.nanoTime();// hold the stop time
	    	timeOfTop100Search4 += stopTimeOfTop100Search4 - startTimeOfTop100Search4; //counts the duration of search top-100 docs execution
	    }
	    
	    long avgTimeOfTop100Search4 = timeOfTop100Search4/50;
	    
	    long timeOfTop1000Search4 = 0;
	    for(int i=0;i<50;i++) {
	    	long startTimeOfTop1000Search4 = System.nanoTime();// hold the start time
	    	invIndex5000Lists.search("dlrs", 1000);
	    	long stopTimeOfTop1000Search4 = System.nanoTime();// hold the stop time
	    	timeOfTop1000Search4 += stopTimeOfTop1000Search4 - startTimeOfTop1000Search4; //counts the duration of search top-1000 docs execution
	    }
	    
	   long avgTimeOfTop1000Search4 = timeOfTop1000Search4/50;
	    
/*-------------------------------------------------------------------------------------------------------------------------------------------*/
	    
		
		
/*----------------------------DISPLAY THE RESULTS OF EXPERIMENTATION---------------------------------------------------------------------------------------------------------------------------------*/	
		
		
		System.out.println("Time elapsed for build the Inverted Index with 100 lists: "+ time0/50 + " nanoseconds"); 
		
		System.out.println("Time elapsed for build the Inverted Index with 500 lists: "+ time1/50 + " nanoseconds"); 
		
		System.out.println("Time elapsed for build the Inverted Index with 1000 lists: "+ time2/50 + " nanoseconds"); 
		
		System.out.println("Time elapsed for build the Inverted Index with 2500 lists: "+ time3/50 + " nanoseconds"); 
		
		System.out.println("Time elapsed for build the Inverted Index with 5000 lists: "+ time4/50 + " nanoseconds"); 
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The average of nodes on Inverted Index with 100 lists: "+ avgLength0);
		System.out.println("The maximum number of nodes on Inverted Index with 100 lists: "+ max0);
		System.out.println("The minimum number of nodes on Inverted Index with 100 lists: "+ min0);
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The average of nodes on Inverted Index with 500 lists: "+ avgLength1);
		System.out.println("The maximum number of nodes on Inverted Index with 500 lists: "+ max1);
		System.out.println("The minimum number of nodes on Inverted Index with 500 lists: "+ min1);
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The average of nodes on Inverted Index with 1000 lists: "+ avgLength2);
		System.out.println("The maximum number of nodes on Inverted Index with 1000 lists: "+ max2);
		System.out.println("The minimum number of nodes on Inverted Index with 1000 lists: "+ min2);
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The average of nodes on Inverted Index with 2500 lists: "+ avgLength3);
		System.out.println("The maximum number of nodes on Inverted Index with 2500 lists: "+ max3);
		System.out.println("The minimum number of nodes on Inverted Index with 2500 lists: "+ min3);
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The average of nodes on Inverted Index with 5000 lists: "+ avgLength4);
		System.out.println("The maximum number of nodes on Inverted Index with 5000 lists: "+ max4);
		System.out.println("The minimum number of nodes on Inverted Index with 5000 lists: "+ min4);
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The average time of rare terms on Inverted Index with 100 lists: "+ avgRareSearch0 +" nanoseconds");
		System.out.println("The average time of rare terms on Inverted Index with 500 lists: "+ avgRareSearch1 +" nanoseconds");
		System.out.println("The average time of rare terms on Inverted Index with 1000 lists: "+ avgRareSearch2 +" nanoseconds");
		System.out.println("The average time of rare terms on Inverted Index with 2500 lists: "+ avgRareSearch3 +" nanoseconds");
		System.out.println("The average time of rare terms on Inverted Index with 5000 lists: "+ avgRareSearch4 +" nanoseconds");
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The average time of more frequent terms on Inverted Index with 100 lists: "+ avgMoreFrequencySearch0 + " nanoseconds");
		System.out.println("The average time of more frequent terms on Inverted Index with 500 lists: "+ avgMoreFrequencySearch1 + " nanoseconds");
		System.out.println("The average time of more frequent terms on Inverted Index with 1000 lists: "+  avgMoreFrequencySearch2 + " nanoseconds");
		System.out.println("The average time of more frequent terms on Inverted Index with 2500 lists: "+  avgMoreFrequencySearch3 + " nanoseconds");
		System.out.println("The average time of more frequent terms on Inverted Index with 5000 lists: "+  avgMoreFrequencySearch4 + " nanoseconds");
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The average time of most frequent terms on Inverted Index with 100 lists: "+  avgMostFrequencySearch0 + " nanoseconds");
		System.out.println("The average time of most frequent terms on Inverted Index with 500 lists: "+  avgMostFrequencySearch1 + " nanoseconds");
		System.out.println("The average time of most frequent terms on Inverted Index with 1000 lists: "+  avgMostFrequencySearch2 + " nanoseconds");
		System.out.println("The average time of most frequent terms on Inverted Index with 2500 lists: "+  avgMostFrequencySearch3 + " nanoseconds");
		System.out.println("The average time of most frequent terms on Inverted Index with 5000 lists: "+  avgMostFrequencySearch4 + " nanoseconds");
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The time of top-10 records which include the term \"dlrs\" on Inverted Index with 100 lists: "+ avgTimeOfTop10Search0 + " nanoseconds");
		System.out.println("The time of top-10 records which include the term \"dlrs\" on Inverted Index with 500 lists: "+ avgTimeOfTop10Search1 + " nanoseconds");
		System.out.println("The time of top-10 records which include the term \"dlrs\" on Inverted Index with 1000 lists: "+ avgTimeOfTop10Search2 + " nanoseconds");
		System.out.println("The time of top-10 records which include the term \"dlrs\" on Inverted Index with 2500 lists: "+ avgTimeOfTop10Search3 + " nanoseconds");
		System.out.println("The time of top-10 records which include the term \"dlrs\" on Inverted Index with 5000 lists: "+ avgTimeOfTop10Search4 + " nanoseconds");
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The time of top-100 records which include the term \"dlrs\" on Inverted Index with 100 lists: "+ avgTimeOfTop100Search0 + " nanoseconds");
		System.out.println("The time of top-100 records which include the term \"dlrs\" on Inverted Index with 500 lists: "+ avgTimeOfTop100Search1 + " nanoseconds");
		System.out.println("The time of top-100 records which include the term \"dlrs\" on Inverted Index with 1000 lists: "+ avgTimeOfTop100Search2 + " nanoseconds");
		System.out.println("The time of top-100 records which include the term \"dlrs\" on Inverted Index with 2500 lists: "+ avgTimeOfTop100Search3 + " nanoseconds");
		System.out.println("The time of top-100 records which include the term \"dlrs\" on Inverted Index with 5000 lists: "+ avgTimeOfTop100Search4 + " nanoseconds");
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("The time of top-1000 records which include the term \"dlrs\" on Inverted Index with 100 lists: "+ avgTimeOfTop1000Search0 + " nanoseconds");
		System.out.println("The time of top-1000 records which include the term \"dlrs\" on Inverted Index with 500 lists: "+ avgTimeOfTop1000Search1 + " nanoseconds");
		System.out.println("The time of top-1000 records which include the term \"dlrs\" on Inverted Index with 1000 lists: "+ avgTimeOfTop1000Search2 + " nanoseconds");
		System.out.println("The time of top-1000 records which include the term \"dlrs\" on Inverted Index with 2500 lists: "+ avgTimeOfTop1000Search3 + " nanoseconds");
		System.out.println("The time of top-1000 records which include the term \"dlrs\" on Inverted Index with 5000 lists: "+ avgTimeOfTop1000Search4 + " nanoseconds");
		
		
/*--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/	
		
	}
}
