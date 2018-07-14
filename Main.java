
import java.net.*;
import java.util.Properties;
import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args)  {
		int id;
		int c = 1;
		int portofserver;
		int portasserver;
		Scanner scan = new Scanner(System.in);
		String serverName = "localhost";
		int peer;
		int count=0;
		String msgid;
		String sharedDir;
		ArrayList<Thread> thread=new ArrayList<Thread>();			
		//To store all client threads
		ArrayList<ClientThread> peers=new ArrayList<ClientThread>();		
		try {
			System.out.println("enter the peer_id");
			int peer_id=scan.nextInt();
			scan.nextLine();
			//Local Directory of the Peer
			System.out.println("ENter the shared directory"); 		
			sharedDir=scan.nextLine();
			//Properties class to read the configuration file
			Properties prop = new Properties();						
			String fileName = "config.properties";
		    	InputStream is = new FileInputStream(fileName);
		    	prop.load(is);

		    	portofserver=Integer.parseInt(prop.getProperty("peer"+peer_id+".serverport"));
		    	ServerDownload sd=new ServerDownload(portofserver,sharedDir);
		    	sd.start();

		    	portasserver=Integer.parseInt(prop.getProperty("peer"+peer_id+".port"));
			ServerThread cs=new ServerThread(portasserver,sharedDir,peer_id);
			cs.start();
			System.out.println("enter the file to be downloaded");
			String f_name=scan.nextLine();
			++count;
			msgid=peer_id+"."+count;
		    	String[] neighbours=prop.getProperty("peer"+peer_id+".next").split(","); 	//Creating a client thread for every neighbouring peer
		    	for(int i=0;i<neighbours.length;i++)
		    	{
				int connectingport=Integer.parseInt(prop.getProperty("peer"+neighbours[i]+".port"));
				int neighbouringpeer=Integer.parseInt(neighbours[i]);
				System.out.println("sending requests to"+neighbours[i]);
				ClientThread cp=new ClientThread(connectingport,neighbouringpeer,f_name,msgid,peer_id);
				Thread t=new Thread(cp);
				t.start();
				thread.add(t);
				peers.add(cp);
		    	}
		    	for(int i=0;i<thread.size();i++)
		    	{
				try {
					//Wait until all the client threads are done executing
					((Thread) thread.get(i)).join();						    
				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
		    	}
			//part on how to send data from the ConnectingPeer
		    	int[] peerswithfiles;
			System.out.println("Peers containing the file are: ");
		    	for(int i=0;i<peers.size();i++)
		    	{
				//using the stored list of client threads to read all peers containing file
				peerswithfiles=((ClientThread)peers.get(i)).getarray();			
				for(int j=0;j<peerswithfiles.length;j++)
				{	if(peerswithfiles[j]==0)
					break;
					System.out.println(peerswithfiles[j]);
				}
		    	}
		    	System.out.println("Enter the peer from where to download the file: ");
		    	int peerfromdownload=scan.nextInt();
		    	int porttodownload=Integer.parseInt(prop.getProperty("peer"+peerfromdownload+".serverport"));
		    	//invoke the thread were peer with file acts as a server for file download
			ClientasServer(peerfromdownload,porttodownload,f_name,sharedDir);
		    	System.out.println("File: "+f_name+"downloaded from peer"+peerfromdownload+"to peer"+peer_id);

		}catch(IOException io)
		{
			io.printStackTrace();
		}
	}
	/**
	@description method connects the peer requesting file to the server thread of peer with file and performs the file download
	*/
	public static void ClientasServer(int clientasserverpeerid,int clientasserverportno,String filename,String sharedDir)
	{																												//method to establish connection with Serverdownload thread to download the file			
		try{
			Socket clientasserversocket=new Socket("localhost",clientasserverportno);
			ObjectOutputStream ooos=new ObjectOutputStream(clientasserversocket.getOutputStream());
			ooos.flush();
			ObjectInputStream oois=new ObjectInputStream(clientasserversocket.getInputStream());
			ooos.writeObject(filename);
			int readbytes=(int)oois.readObject();
			System.out.println("bytes transferred: "+readbytes);
			byte[] b=new byte[readbytes];
			oois.readFully(b);
			OutputStream fileos=new FileOutputStream(sharedDir+"//"+filename);
			BufferedOutputStream bos=new BufferedOutputStream(fileos);
			bos.write(b, 0,(int) readbytes);
			System.out.println(filename+" file has be downloaded to your directory "+sharedDir);
			bos.flush();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
