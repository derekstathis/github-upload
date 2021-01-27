
public class DLB implements DictInterface {
	
	Node root;
	private final char TERMINATE_CHAR = '^';
	
	//creates a DLB and sets root node to a new node
	public DLB()
	{
		root = new Node();
	}

	public boolean add(String s)
	{
		s = s + TERMINATE_CHAR;
		Node current = root;
		boolean added = false;
		
		for(int i = 0; i<s.length(); i++)	//go through each character and traverse through DLB
		{
			char c  = s.charAt(i);
			Node result;
			//******************************************
			if(current.childNode == null)
			{
				current.childNode = new Node(c);
				result = current.childNode;	//node that is added
				added = true;		//added
				
			}
			else
			{
				if(current.childNode == null)
				{
					current.childNode = new Node(c);
					result = current.childNode;	//node that is added
					added = true;	//added
				}
				else
				{
					Node nextPeer = current.childNode;
					while(nextPeer.sibNode != null)
					{
						if(nextPeer.value == c)
						{
							break;
						}
						nextPeer = nextPeer.sibNode;
					}
					if(nextPeer.value == c)
					{
						//return node (nextPeer, false)
						result = nextPeer;		//node that is added
						added = false;	//not added
					}
					else
					{
						nextPeer.sibNode = new Node(c);
						result = nextPeer.sibNode;		//node that is added
						added = true;	//added
						//return (nextPeer.sibNode, true)
					}
				}
			}
			current = result;
			
			//******************************************
		}
		
		return added;
		
	}
	
	public int searchPrefix(StringBuilder s)
	{
		Node current = root;
		
		//check each letter in strBldr s
		for(int i=0; i<s.length();i++)
		{
			char c = s.charAt(i);
			current = searchChildNode(current, c);
			
			//it is not in the dictionary 
			//i.e. not word or prefix
			if(current == null)
			{
				return 0;
			}
		}
		
		//now we need to see if the last val
		//is a the terminating character
		
		Node nextNode = searchChildNode(current, TERMINATE_CHAR);
		
		if(nextNode == null)
		{
			return 1;		//not a word but is a prefix
		}
		else if(nextNode.sibNode == null)
		{
			return 2;		//a word but not a prefix
		}
		else 
		{
			return 3;		//word and prefix
		}
		
	}
	
	//the only difference between this and the regular search is
	//we change the input string to start at 'start' and 'end'
	//the rest of the logic is the same
	public int searchPrefix(StringBuilder s, int start, int end)
	{
		
		//need to reduce the size of the string we are seaching for 
		String newString = s.substring(start,end+1);
		StringBuilder newSB = new StringBuilder();
		
		for(int i=0;i<newString.length();i++)
		{
			newSB.append(newString.charAt(i));
		}
		
		//*******************OLD CODE FROM BEFORE**************************
		
		Node current = root;
		
		//check each letter in strBldr s
		for(int i=0; i<newSB.length();i++)
		{
			char c = newSB.charAt(i);
			current = searchChildNode(current, c);
			
			//it is not in the dictionary 
			//i.e. not word or prefix
			if(current == null)
			{
				return 0;
			}
		}
		
		//now we need to see if the last val
		//is a the terminating character
		
		Node nextNode = searchChildNode(current, TERMINATE_CHAR);
		
		if(nextNode == null)
		{
			return 1;		//not a word but is a prefix
		}
		else if(nextNode.sibNode == null)
		{
			return 2;		//a word but not a prefix
		}
		else 
		{
			return 3;		//word and prefix
		}
	}
	
	
	
	private Node searchPeerNode(Node nodeStart, char c)
	{
		Node nextNode = nodeStart;
		while (nextNode != null)
		{
			//stop checking if found
			if (nextNode.value == c) 
				{
					break;
				}
			
			//check next node if not found
			nextNode = nextNode.sibNode;
		}
		return nextNode;
	}
	
	private Node searchChildNode(Node n, char c)
	{
		Node res = searchPeerNode(n.childNode,c);
		return res;
	}
}

//this represents a node for DLB data structure
class Node
{
	Node sibNode;
	Node childNode;
	char value;
	
	public Node()
	{	}
	
	public Node(char val)
	{
		this(val,null,null);
	}
	
	public Node(char val, Node broNode, Node childNode)
	{
		this.value = val;
		this.sibNode = broNode;
		this.childNode = childNode;
		
	}
}