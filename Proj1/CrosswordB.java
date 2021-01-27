import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class CrosswordB {
	
	private DictInterface D;
	private char board [][];
	private int boardLength;
	private StringBuilder rowStr[];
	private StringBuilder colStr[];
	private char [] alphabet  = "etaoinsrhldcumfpgwybvkxjqz".toCharArray();
	private static String dictType;
	private static String fileName;
	private static String testFileName;
	
	int count = 0;
	
	public static void main(String [] args) throws IOException
	{
		fileName = args[0];
		dictType = args[1];
		testFileName = args[2];
		new CrosswordB();
	}
	
	public CrosswordB() throws IOException
	{
		Scanner fileScan = new Scanner(new FileInputStream(fileName));
		
		if (dictType.equals("DLB"))
			D = new DLB();
		else
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
		

		//***********GET LENGTH OF BOARD****************
		File fName = new File(testFileName);
		Scanner fReader = new Scanner(fName);
		int boardDim = fReader.nextInt();
		boardLength = boardDim;
		
		rowStr = new StringBuilder[boardDim];
		colStr = new StringBuilder[boardDim];

		//**********************************************
		
		
		
		// ************FILL String Builders at each block of board****************
		for(int i =0; i<boardDim;i++)
		{
			rowStr[i] = new StringBuilder();
			colStr[i] = new StringBuilder();
		}
		// ***********************************************************************
		
		
		//*********************FILL BOARD FROM THE GIVEN FILE**************************
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
				}
			}
		}
		else
		{
			System.out.println("Board size can only be 8 or less");
			System.exit(0);
		}
		fReader.close();
		//*******************************************************************************
		
		
		
		//***************PRINT ORIGINAL BOARD*********************************
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
		//**********************************************************************
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
					
					if(row == boardLength-1 && col == boardLength-1)		//we found a solution
					{
						printBoard();
						System.out.println("--This is a soution--");
						count++;
						if(!dictType.equals("DLB"))
						{
							System.exit(0);
						}

					}
					else
					{
						if(col<boardLength-1)
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
				
				
				if(row == boardLength-1 && col == boardLength-1)	//we found a solution
				{
					printBoard();
					System.out.println("--This is a solution--");
					count++;
					if(!dictType.equals("DLB"))		//end the program if it isnt a DLB
					{
						System.exit(0);
					}


				}
				else
				{
					if(col < boardLength-1)
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
		else if(board[row][col] == '-')									//hard character in the board
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
		//test if the row string has a minus in it
		//change to search after the last index of it
		if(rTemp.toString().contains("-"))
		{
			rVal = D.searchPrefix(rTemp,(rTemp.lastIndexOf("-"))+1,rTemp.length()-1);
		}
		else
			rVal = D.searchPrefix(rTemp);
		if(cTemp.toString().contains("-"))	//test if col string has a minus in it
		{
			cVal = D.searchPrefix(cTemp,cTemp.lastIndexOf("-")+1,cTemp.length()-1);
		}
		else
			cVal = D.searchPrefix(cTemp);
		//********************************************************
			
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
		else if(row==boardLength-1 && col<boardLength-1)	//at end of row but not col
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
		else if(row==boardLength-1 && col==boardLength-1)	//last spot on the board
		{
			if((rVal==2 || rVal==3)&& (cVal==2 || cVal==3))
			{
				return true;
			}
			else
				return false;
			
		}
		
		//******These are the test cases for the middle of the board*********
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
