public class Node {
	public String sTerm;
	public int nDocId; 
	public int nCount;
	public Node next;                   
	public Node(String sTerm, int nDocId, int nCount) { 
		this.sTerm = sTerm; 
		this.nDocId = nDocId;
		this.nCount = nCount;
	}
}
