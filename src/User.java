import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class User {
	
	public static Socket client = null ;
	public static String name = null ; 

	public static void main(String[] args) throws IOException
	{
		client =new Socket("localhost",2000);
		System.out.println("Welcome To the System \n 	Upload file = [u] , Download File = [D]\n");
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System. in);
    	name = scanner. nextLine();
	 
    	// User is going to upload a file
		if ( name.equals("u") ||  name.equals("U"))		
		{
			PrintWriter toserver=new PrintWriter(client.getOutputStream(),true);		
			toserver.println(name);
			uploadfile();
		}
		
		// User is going to download a file
		if ( name.equals("d") ||  name.equals("D"))
		{
			PrintWriter toserver=new PrintWriter(client.getOutputStream(),true);		
			toserver.println(name);
			downloadfile();
		}
		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//   	PART: A		       UPLOAD A FILE
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void uploadfile() throws IOException
	{
		System.out.println("  Upload FIleName: " );
				
		// Sending a the file Name 
    	@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System. in);
    	name = scanner. nextLine();    					
    	sendname(name);
		//----------------------------- WRITE THE LINE--------------------------------------
    	System.out.println("File Data: ");
    	PrintWriter toserver=new PrintWriter(client.getOutputStream(),true);
    	toserver.println(scanner.nextLine());
    	
    	//-----------------------------CHOOSE THE FILE-------- w
//    	System.out.println("File Data: ");
//    	PrintWriter toserver=new PrintWriter(client.getOutputStream(),true);
//    	toserver.println(readfile());
	}
	
	
	public static String readfile() throws FileNotFoundException
	{
		String filedata = "";
		String filename ;
		
		// Sending a the file Name 
    	@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System. in);
    	System.out.println("File Name or File Path : ");
    	filename = scanner. nextLine();    					
    	
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
	
	public static void sendname(String name) throws IOException
	{							
    	PrintWriter toserver=new PrintWriter(client.getOutputStream(),true);
		toserver.println(name);
	}
	
	
	public static void sendfile() throws FileNotFoundException
	{
		@SuppressWarnings({ "unused", "resource" })
		FileReader file = new FileReader(name + ".txt");
	}

	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 	PART: B 				DOWNLOAD A FILE 
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	public static void downloadfile() throws IOException
	{
		
		// Sending a the file Name d
    	@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System. in);
    	System.out.println("File name : ");

    	name = scanner. nextLine();  
    	System.out.println("sending name ");
    	sendname(name);
		System.out.println("name is sent");
		
		// Receiving data from the Root Server 
		InputStreamReader isr=new InputStreamReader(client.getInputStream());
		BufferedReader br =new BufferedReader(isr);					
	  	String data = br.readLine();
				
	    System.out.println("Writing the Received data in the File ");
	    	// writing data to a file
	    	FileWriter writer = new FileWriter(name + ".User.txt");    					 
			writer.write(data);
			writer.close();
	}
	

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
