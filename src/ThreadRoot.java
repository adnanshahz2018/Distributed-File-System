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

public class ThreadRoot extends Thread{
	
	static ArrayList<FileTable> table = new ArrayList<FileTable>() ;
	
	public static ServerSocket ss = null ;
	public static Socket firstclient = null ;
    public static File file =   new File("FileTable.txt"); 
	public static String name = null;
	public static String destinationIP = "localhost" ;
	public static int numclient = 0 ;
	
	
	public static void setobj(Socket sf)	
	{
		firstclient = sf;
	}
	public static void setsocket(ServerSocket ssf)	
	{
		ss = ssf;
	}
	
	public void run() 
	{		
		
			try {
			 			
	    	//---------------------------------------------------------------
	    	System.out.println("Root Server is waiting for client request");
	    		
		    	System.out.println("Accepted connection request# " + ++numclient);	 
		    	// getting the choice of the user 
		    	String choice = getname();
		    	 
		    	if ( choice.equals("u") ||  choice.equals("U"))
				{
					receivefile();
				}
				if (choice.equals("d") ||  choice.equals("D"))
				{
					System.out.println("entering the download section in Root ");
					sendfile();
				}
				
	    	firstclient.close();

			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	} 
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 	PART: A				RECEIVE/SAVE A FILE FROM USER  || USER-UPLOAD SECTION
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	public static void receivefile() throws IOException
	{
		
		System.out.println(generaterandom());	    	
		
    	name =  getname(); 									// getting the name of the file
    	System.out.println("filename = " + name );
    	String tag = generaterandom();
		String filename = name + tag;			// make new name with generaterandom()
		
		FileTable f = new FileTable();
		f.filename = name  ;
		f.tag = tag ;
		table.add(f);
		WriteToFile("FileTable.txt", name + "," + tag);
		name = filename ;
		
		String filedata = savefile();
    	// dividing files into parts
    	String fileblockA = filedata.substring(0, filedata.length()/2);
    	String fileblockB = filedata.substring( (0 + filedata.length()/2)  , filedata.length());

    	System.out.println("data length" + filedata.length());
    	System.out.println("data A" + fileblockA);
    	System.out.println("data B" + fileblockB);
    	
    	sendA(filename, fileblockA);
    	sendB(filename, fileblockB);
    	
    	System.out.println("data sent to both ServerA 	&	 ServerB "); 
		
	}
	
	public static String getname()
	{
		String name = null ;
    	try
		{			
    		// Capturing & Reading Client's Data 
			InputStreamReader isr=new InputStreamReader(firstclient.getInputStream());
	    	BufferedReader br =new BufferedReader(isr);					
	    	name = br.readLine();
		
		}
    	 catch(Exception e){}
    	
    	return name ;
	}
	
	public static String savefile()						/// Saving file data in a file
	{
    	// creating the filename for the NEXT file
    	String filename = name + ".txt";
    	String filedata = "" ;
    	
    	
    	try
		{			
    		// Capturing & Reading Client's Data 
			InputStreamReader isr=new InputStreamReader(firstclient.getInputStream());
	    	BufferedReader br =new BufferedReader(isr);					
	    	filedata =br.readLine();
	    	
	    	System.out.println(filedata);
	    	
	    	// writing data to a file
	    	WriteToFile(filename, filedata);

		}
    	catch(Exception e){}
    	
    	return filedata ;
	}
	
	public static void WriteToFile(String filename, String filedata) throws IOException
	{
		// writing data to a file
    	FileWriter writer = new FileWriter(filename,true);    					 
		writer.write(filedata);
		writer.write('\n');
		writer.close();
	}
	public static void sendA(String name, String data) throws IOException
	{
		// sending file Name to ServerA
		@SuppressWarnings("resource")
		Socket clientA =new Socket(destinationIP,2001);
		PrintWriter toserver=new PrintWriter(clientA.getOutputStream(),true);
		toserver.println(name);	
		
		// sending file Data to ServerA
		clientA =new Socket(destinationIP,2001);
		toserver = new PrintWriter(clientA.getOutputStream(),true);
		toserver.println(data);	
		
	}
	 
	
	public static void sendB(String name, String data) throws IOException
	{
		// sending file Name to ServerB
		@SuppressWarnings("resource")
		Socket clientB =new Socket(destinationIP,2002);
		PrintWriter toserver=new PrintWriter(clientB.getOutputStream(),true);
		toserver.println(name);

		// sending file data to ServerB
		System.out.println("data for B = " + data);
		clientB =new Socket(destinationIP,2002);
		toserver=new PrintWriter(clientB.getOutputStream(),true);
		toserver.println(data);	
		clientB.close();
	}
	
	public static String generaterandom()
	{
		// create instance of Random class 
        Random rand = new Random(); 
        
        // Generate random integers in range 0 to 9 
        int rand_int1 = rand.nextInt(10); 
        
        return "." + Integer.toString(rand_int1);
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 	PART: B 				RETRIVE/PROVIDE A FILE TO THE USER     || USER-DOWNLOAD SECTION
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	public static void sendfile() throws IOException
	{
		// getting the name from USER
		String userfilename = getname();
		
		System.out.println("entered the sendfile() in root ");
		
		// checking the name in the List: table: FileTable
		// if the name is present , it will return the "tag" of the that file => else "NOTFOUND"
		String tag = searchfiletable(userfilename) ;
		
		if (tag.equals("NOTFOUND"))
		{
			System.out.println(" Requested file is NOT Present");
		}
		else
		{
			String filedata = "" ;
			filedata = filedata + requestA(userfilename+tag+".A.txt");		// For Example: userdata.5.A.txt
			filedata = filedata + requestB(userfilename+tag+".B.txt");
			
			sendtouser(filedata);
			System.out.println("filedata in ROOT " + filedata);
		}
		
	}
	
	
	public static String searchfiletable(String userfilename) throws FileNotFoundException
	{	
		table.clear();
		filltable();
		
		System.out.println("\t\t\t %%%%%  userfilename " + userfilename);
		System.out.println("searching for the file in FileTable: \n");
		for(FileTable ft: table) 
		{
			System.out.println("*\t");
			if ( ft.filename.equals(userfilename) )
			{	
				return  ft.tag ;
			}
		}
		
		return "NOTFOUND" ;
	}
	
	// filling data / objects in the table:FileTable form the Text file 
	public static void filltable() throws FileNotFoundException
	{
		String filedata = ""; 
		// pass the path to the file as a parameter 

	    @SuppressWarnings("resource")
		Scanner sc = new Scanner(file); 
	  
	    while (sc.hasNextLine()) 
	    {
	      filedata = sc.nextLine(); 
	      String data = new String(filedata);
	      String[] values = data.split(",");
	     
	      System.out.println("value-data in file  " + data );
	      	
	     	FileTable f = new FileTable();
			f.filename = values[0] ;
			
			System.out.println("\t\t\t $$$$$  filename " + f.filename);
			
			f.tag = values[1] ;
			table.add(f);
	    } 
		 System.out.println(filedata);
	}
	
	public static String requestA(String name) throws IOException
	{ 
		sendA("11RFD", name);
		String filedatafromA = getdatafromA(destinationIP, 2001);
//    	sendtouser(filedatafromA);

		return filedatafromA ;
	}
	
	
	public static String getdatafromA(String IP, int port) throws IOException
	{
		@SuppressWarnings("resource")
		Socket clientA =new Socket(IP,port);   // clientA can be clientB based on IP and port
		
		InputStreamReader isr=new InputStreamReader(clientA.getInputStream());
    	BufferedReader br =new BufferedReader(isr);					
    	name = br.readLine();
    	
    	System.out.println("\t\t Receiving Data from Server   =  " +  port  + "data  = " + name  );
    	return name ;
	}
	
	public static void sendtouser(String filedata) throws IOException
	{
		System.out.println("sending to user");
		PrintWriter toserver=new PrintWriter(firstclient.getOutputStream(),true);
		toserver.println(filedata);
	}
	
	public static String requestB(String name) throws IOException
	{
		sendB("22RFD", name);
		String filedatafromB = getdatafromA(destinationIP, 2002);
//    	sendtouser(filedatafromB);

		return filedatafromB ;

	}
}

