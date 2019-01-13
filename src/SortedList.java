public class SortedList {
	private Node first;              
	public SortedList() { first = null; }
	
	public void insert(int nDocId, String sTerm, int nCount)  
	{	
		Node newNode = new Node(sTerm, nDocId,  nCount);
		
		if (first == null || first.nDocId >= newNode.nDocId)   
		{
			newNode.next = first; //the next of newNode is the first
			first = newNode;      // newNode -> first ,each time
			return;
		}

		// Locate the node before the point of insertion
		Node current = first;
		while (current.next != null && current.next.nDocId < newNode.nDocId) 
		current = current.next;
		
		newNode.next = current.next; //the next of newNode is the next of current node
		current.next = newNode;      //the next of current is the newNode

	}  

	public Integer[] find(String sTerm)   
	{
		if(first == null) return new Integer[]{-1}; //if list is empty, return an array with invalid id
		
		Node current = first;
		Integer[] nDocId = new Integer[getLength()+1]; 
		int counter = 0;	
		
		while(current != null) {
			if(current.sTerm.equalsIgnoreCase(sTerm)) nDocId[counter++] = current.nDocId;//checks each node if it is the same term with the given term
			current = current.next;                                                      //and add it to the array nDocId
		}
		
		if(nDocId.length > 0) return nDocId; // if the nDocId has positive length, return it 
		else return new Integer[]{-1}; //else return an array with invalid id 
	} 

	public Node find(String sTerm, int nDocId)   
	{
		if(first == null) return null; //if not exists the node, return null
		
		Node current = first;
		while(!current.sTerm.equalsIgnoreCase(sTerm) && current.nDocId != nDocId) //while term of the nod it isn't same with the given term
		{                                                                         // and if the nDocId is different with the given nDocId
		 if(current.next == null) return null;    //if the next node is null,then return null
		 current = current.next;                  //else continue with the next node
	    }
		return current; //if the while loop become false, the return this node
	}
	public Integer[] findTopK(String sTerm, int k) {  // find the top-k docs ids! 
		
		if(first == null) return new Integer[]{-1};  //if list is null, return an array with an invalid id
		
		Node current = first;
		int[] countOfDisplays = new int[getLength()+1];
		int[] idRecords = new int[getLength()+1];
		int counter = 0;
		
		while(current != null)
		{
			if(current.sTerm.equalsIgnoreCase(sTerm))       //if the term of the node is the the same with the given term,              
			{	
			 countOfDisplays[counter++] = current.nCount;  //then store the number of displays and the their ids
			 idRecords[counter++] = current.nDocId;
			}                                                                           
			current = current.next;                                                   
}
		
		   int n = countOfDisplays.length;          //sort the number of displays in descending order
	       int temp0 = 0; 
	       int temp1 = 0;
	        for(int i=0; i < n; i++){  
	           for(int j=1; j < (n-i); j++){  
	               if(countOfDisplays[j-1] <countOfDisplays[j])
	               {
	                  temp0 = countOfDisplays[j-1];  
	                  countOfDisplays[j-1] = countOfDisplays[j];  
	                  countOfDisplays[j] = temp0; 
	                  
	                  temp1 = idRecords[j-1];
	                  idRecords[j-1] = idRecords[j];
	                  idRecords[j] = temp1;
	               }  
	           }  
	         }  
	        
	        Integer [] topK = new Integer[k]; 
	        if(k <= idRecords.length)
	        for(int j=0; j<k;j++) topK[j] = Integer.valueOf(idRecords[j]); //store the first-k displays on array topK
	        else 
	        for(int j=0; j<idRecords.length;j++) topK[j] = Integer.valueOf(idRecords[j]); //store the first-k displays on array topK
	        
	        
        if(topK[0] != -1) return  topK; // if the topK is not empty return it
        
        return new Integer[]{-1}; //else return an array with invalid number of displays
	}

	public int getLength() {  //return the the length of list
		int len = 0;
		Node current = first;
		while (current != null) {
			len++;
			current = current.next;
		}
		return len;
	}
	
}
