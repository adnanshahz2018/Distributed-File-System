import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerB {
	
public static 	ServerSocket ss = null ;	
public static Socket client = null ;
public static int numclient = 0 ;
	
	public static void main( String[] args) {
		
		try
    	{
	    	//---------------------------------------------------------------
			ss = new ServerSocket(2002);  			
	    	//---------------------------------------------------------------
	    	System.out.println("ServerB is Running... ");
	    	
	    	while(true)
	    	{ 		
	    		// accepting connection
	    		client=ss.accept();										
		    	System.out.println("ServerB has Accepted connection request# " + ++numclient);
		    	
		    	// getting the choice from the Root 
		    	String choice = getname();
				
		    	if(choice.equals("22RFD"))			// Retrive File For Download
		    	{
		    		sendfile();
		    	}
		    	else
		    	{
		    		savefile(choice);		    	
		    	}
		    		    	
	    	}	    	
    	}
			catch(Exception e){		
				System.out.println(e);
			}
		
	}
	
	public static String getname()
	{
		String name = null ;
    	try
		{			
    		// Capturing & Reading Client's Data 
			InputStreamReader isr=new InputStreamReader(client.getInputStream());
	    	BufferedReader br =new BufferedReader(isr);					
	    	name = br.readLine();
	    	
		}
    	catch(Exception e){}    	

    	System.out.println(" name in B = " + name);

    	return name ;
	}
	
	
	public static void savefile(String name)
	{
		// creating the filename for the NEXT file
    	String filename = name + ".B.txt";
    	
    	System.out.println("ServerB is Saving file \n");
    	try
		{			
    		// Capturing & Reading Client's Data 
    		client = ss.accept();
			InputStreamReader isr=new InputStreamReader(client.getInputStream());
	    	BufferedReader br =new BufferedReader(isr);					
	    	String str=br.readLine();
	    	
	    	System.out.println(str);
	    	
	    	// writing data to a file
	    	FileWriter writer = new FileWriter(filename);    					 
			writer.write(str);
			writer.close();
	    					
		}catch(Exception e) {}
	    	
	}
	
	public static void sendfile() throws IOException
	{
		// getting the name of the file from the Root Server 	
		client = ss.accept();	
		InputStreamReader isr=new InputStreamReader(client.getInputStream());
    	BufferedReader br =new BufferedReader(isr);					
    	String filename = br.readLine();
		
		// Reading file data 
		String filedata = readfile(filename);  
		
		
		client = ss.accept();		
		PrintWriter toserver=new PrintWriter(client.getOutputStream(),true);
		toserver.println(filedata);
		
	}
	

	public static String readfile(String filename) throws FileNotFoundException
	{
		String filedata = ""; 
		// pass the path to the file as a parameter 
	    File file =   new File(filename); 
	    @SuppressWarnings("resource")
		Scanner sc = new Scanner(file); 
	    
	    while (sc.hasNextLine()) 
	    {
	      filedata = filedata + sc.nextLine(); 
	    } 
		 System.out.println(filedata);
		 
		 return filedata ; 
	}

}
