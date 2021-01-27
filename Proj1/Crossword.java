import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Crossword {
	
	private DictInterface D;
	private char board [][];
	private int boardLength;
	private StringBuilder rowStr[];
	private StringBuilder colStr[];
	private char [] alphabet  = "etaoinsrhldcumfpgwybvkxjqz".toCharArray();
	private static String dicFile;
	private static String testFile;
	
	int count = 0;
	
	public static void main(String [] args) throws IOException
	{
		dicFile = args[0];
		testFile = args[1];
		new Crossword();
	}
	
	public Crossword() throws IOException
	{
		Scanner fileScan = new Scanner(new FileInputStream(dicFile));
		D = new MyDictionary();
		String fileString;
		
		//Scan Dictionary into MyDictionary
		while(fileScan.hasNext())
		{
			fileString = fileScan.nextLine();
			if(fileString.length()<9)
			{
				D.add(fileString);
			}
			
		}
		
		fileScan.close();

		// Make sure the file name for the Boggle board is valid
		
		
		//Reads board from file and fills board[][]
		File fName = new File(testFile);
		Scanner fReader = new Scanner(fName);
		int boardDim = fReader.nextInt();
		boardLength = boardDim;
		
		rowStr = new StringBuilder[boardDim];
		colStr = new StringBuilder[boardDim];
		
		// ************FILL String Builders at each block of board****************
		for(int i =0; i<boardDim;i++)
		{
			rowStr[i] = new StringBuilder();
			colStr[i] = new StringBuilder();
		}
		// ************FILL String Builders at each block of board****************
		
		
		//board can only accept 8 letter words
		//fill the board from file

		if(boardDim<=8)
		{
			fReader.nextLine();
			board = new char[boardDim][boardDim];
			
			for(int row = 0; row<boardDim; row++)
			{
				String rowString = fReader.nextLine();
				//System.out.println(rowString);
				
				for(int col = 0; col<rowString.length(); col++)
				{
					board[row][col] = rowString.charAt(col);
					
					if(board[row][col] == '-')
					{


					}
				}
			}
		}
		else
		{
			System.out.println("Board size can only be 8 or less");
			System.exit(0);
		}
		
		fReader.close();
		//test to see if board[][] filled correctly
		for(int i=0;i<boardDim; i++)
		{
			for(int j=0;j<boardDim;j++)
			{
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		solve(0,0);
		System.out.println("Number of solutions: " +count);
			
		
	}
	
	private void solve(int row, int col)
	{
		if(board[row][col] == '+')											//If the spot is free
		{
			for(int i = 0; i<26; i++)
			{
				
				if(isValid(row, col, alphabet[i]))
				{
					rowStr[row].append(alphabet[i]);
					colStr[col].append(alphabet[i]);
					
					board[row][col] = alphabet[i];
					printBoard();
					System.out.println();
					
					if(row == boardLength-1 && col == boardLength-1)		//we found a solution
					{
						printBoard();
						System.out.println("--This is a soution--");
						System.exit(0);

					}
					else
					{
						if(col<boardLength-1)		//recurse
						{
							solve(row, col+1);
						}
						else if(row < boardLength-1)
						{
							solve(row+1, 0);
						}
					}
					rowStr[row].deleteCharAt(rowStr[row].length()-1);
					colStr[col].deleteCharAt(colStr[col].length()-1);
					board[row][col] = '+';
				}
			}
		}
		else if(board[row][col] != '-' && board[row][col] != '+')		//if the spot is a specific character
		{
			char boardChar = board[row][col];
			
			if(isValid(row, col, boardChar))
			{
				rowStr[row].append(boardChar);
				colStr[col].append(boardChar);
				
				printBoard();
				System.out.println();
				
				if(row == boardLength-1 && col == boardLength-1)		//got a solution
				{
					printBoard();
					System.out.println("--This is a solution--");
					System.exit(0);


				}
				else
				{
					if(col < boardLength-1)			//recurse
					{
						solve(row,col+1);
					}
					else if(row<boardLength-1)
					{
						solve(row+1,0);
					}
						
				}
				rowStr[row].deleteCharAt(rowStr[row].length()-1);
				colStr[col].deleteCharAt(colStr[col].length()-1);
			}
			
		}
		else if(board[row][col] == '-')									//if the spot must be skipped
		{
			rowStr[row].append('-');
			colStr[col].append('-');
			
			if(col<boardLength-1)
			{
				solve(row, col+1);
			}
			else if(row < boardLength-1)
			{
				solve(row+1, 0);
			}
			rowStr[row].deleteCharAt(rowStr[row].length()-1);
			colStr[col].deleteCharAt(colStr[col].length()-1);
		}
		
		
	}
	
	private boolean isValid(int row, int col, char character)
	{
		StringBuilder rTemp = new StringBuilder(rowStr[row]);
		StringBuilder cTemp = new StringBuilder(colStr[col]);
		char letter = character;
		rTemp.append(letter);
		cTemp.append(letter);
		
		
		
		//1 or 3 is prefix
		//2 or 3 is word
		
		//********************************************************
		int rVal;
		int cVal;
		
		//if rowString contains a minus
		//we need to test after the last instance of the minus
		//the same goes for if its in the column
		if(rTemp.toString().contains("-"))
		{
			rVal = D.searchPrefix(rTemp,(rTemp.lastIndexOf("-"))+1,rTemp.length()-1);
		}
		else
			rVal = D.searchPrefix(rTemp);
		if(cTemp.toString().contains("-"))
		{
			cVal = D.searchPrefix(cTemp,cTemp.lastIndexOf("-")+1,cTemp.length()-1);
		}
		else
			cVal = D.searchPrefix(cTemp);
		//********************************************************
		
		//1 and 3 is a prefix
		//2 and 3 is a word
		
		if(row<boardLength-1 && col==boardLength-1) //at end col but not end row
		{
			if(board[row+1][col] == '-')
			{
				if((rVal==2 || rVal==3) && (cVal==2 || cVal==3))
				{
					return true;
				}
				else
					return false;
			}
			else
			{
				if((rVal==2 || rVal==3) && (cVal==1 || cVal==3))
				{
					return true;
				}
				else
					return false;
			}
		}
		else if(row==boardLength-1 && col<boardLength-1)	//row is at the end and col is less than the end
		{
			if(board[row][col+1]=='-')
			{
				if((rVal==2||rVal==3) && (cVal==2 || cVal==3))
				{
					return true;
				}
				else
					return false;
			}
			else
			{
				if((rVal==1 || rVal==3)&& (cVal==2 || cVal==3))
				{
					return true;
				}
				else
					return false;
			}
			
		}
		else if(row==boardLength-1 && col==boardLength-1)	//last spot in the board
		{
			if((rVal==2 || rVal==3)&& (cVal==2 || cVal==3))
			{
				return true;
			}
			else
				return false;
			
		}
		
		//these are all test cases for the middle of the board
		//they allow for minuses to be places in not-edge-case positions
		else if(board[row+1][col]=='-' & board[row][col+1]=='+')
		{
			if((rVal==1 || rVal==3) && (cVal==2 || cVal==3))
			{
				return true;
			}
			else
				return false;
		}
		else if(board[row+1][col]=='+' & board[row][col+1]=='-')
		{
			if((rVal==2 || rVal==3) && (cVal==1 || cVal==3))
			{
				return true;
			}
			else
				return false;
		}
		else if(board[row+1][col]=='-' & board[row][col+1]=='-')
		{
			if((rVal==2 || rVal==3) && (cVal==2 || cVal==3))
			{
				return true;
			}
			else
				return false;
		}
		else if(board[row+1][col]=='+' & board[row][col+1]=='+')
		{
			if((rVal==1 || rVal==3) && (cVal==1 || cVal==3))
			{
				return true;
			}
			else
				return false;
		}
		else
		{
			if((rVal==1 || rVal==3)&& (cVal==1 || cVal==3))
			{
				return true;
			}
			else
				return false;
		}
		
	}
	
	//prints the board
	private void printBoard()
	{
		for(int i=0; i<boardLength; i++)
		{
			for(int j= 0; j<boardLength; j++)
			{
				System.out.print(board[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	
	
}

