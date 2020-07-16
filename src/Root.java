import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


// My IPv4 Address  = 172.16.11.193			// adnan
// Destination IPv4   = 172.16.11.105 		// mubashar
// 172.16.11.193 nadeem
//172.16.11.40

public class Root {
	
	static ArrayList<FileTable> table = new ArrayList<FileTable>() ;
	
    public static File file =   new File("FileTable.txt"); 
	public static String name = null;
	public static String destinationIP = "localhost" ;
	public static int numclient = 0 ;
	
	public static void main( String[] args) throws IOException 
	{		
		
	    	@SuppressWarnings("resource")
	    	//---------------------------------------------------------------
			ServerSocket ss = new ServerSocket(2000);  			
	    	//---------------------------------------------------------------
	    	System.out.println("Root Server is waiting for client request");
	    	
	    	while(true)
	    	{ 		
	    		// accepting connection
	    		 Socket firstclient = null;
	    		 try
	    		 {
	    			 firstclient =ss.accept();	
		    	
			    	System.out.println("Accepted connection request# " + ++numclient);	 
			    	
					ThreadRoot tr = new ThreadRoot();
					tr.setsocket(ss);
					tr.setobj(firstclient);
					tr.start();
		    	 }
	    		 catch(Exception e)
	    		 {
	    			 System.out.println(e);
	    			 
	    		 }
				
				
				
				
				
		    }// end of while()
	    	
    	
	} 
	
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 	PART: A				RECEIVE/SAVE A FILE FROM USER  || USER-UPLOAD SECTION
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

