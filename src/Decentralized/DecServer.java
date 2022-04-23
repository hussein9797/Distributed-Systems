package Decentralized;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;

public class DecServer implements Runnable {



    private Socket clientSocket;
    public static ConcurrentHashMap<String, ArrayList<String>> distributedHashTable = new ConcurrentHashMap<String, ArrayList<String>>();

    public static String PATH_OF_FILE = "SharedFolder";
    boolean loop=true;

    @Override
    public void run() {

        try {
            DataInputStream in= new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out= new DataOutputStream(clientSocket.getOutputStream());
            while (loop){
                String input=in.readUTF();
                switch (input){
                    case "1":
                        String readKeyValue = in.readUTF();
                        String[] hashKeyValue = readKeyValue.split(";");
                        boolean resultofPut = reigstry(hashKeyValue[0],hashKeyValue[1]);
                        out.writeUTF(String.valueOf(resultofPut));
                        break;

                    case "2":

                        String key = in.readUTF();

                        try
                        {
                            List<String> value = fileSearch(key);
                            String searchDetails="";

                            ListIterator<String> iterator = value.listIterator();
                            while(iterator.hasNext())
                            {
                                searchDetails+=iterator.next()+"\n";
                            }

                            String sendDetails = "The file is present with these Peers: "+searchDetails;
                            out.writeUTF(sendDetails);	//send the locations where file can be found
                            out.flush();

                        }catch(Exception e)
                        {
                            out.writeUTF("File is not Registered");
                            System.out.println("File is not Registered");
                            break;
                        }
                        break;

                    case "3":	//Perform OBTAIN Operation

                        //get the filename to be obtained from the client
                        String fileName = in.readUTF();

                        //call to obtain the file
                        downloadFile(fileName,clientSocket);
                        break;

                    case "4":	//Exit

                        System.out.println("Client Disconnected");
                        loop = false;	//exit from the while(true) loop
                        //System.exit(0);
                        break;
                }


                }


            } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static synchronized boolean uploadFile(String key, String value)
    {
        //obtain the actual key by removing the padded values from the Key & value
        key = key.substring(0,23).replace("*","");
        value = value.substring(0,999).replace("*", "");

        //create temporary lists to store the new PeerID's for the files
        ArrayList<String> peers = new ArrayList<String>();
        ArrayList<String> check = new ArrayList<String>();

        //add peers to the list
        peers.add(value);

        String fileName = key;

        check = distributedHashTable.get(key);	//get the value(PeerID) for the Key(Filename)

        //check if file is already registered by the same peer, if not register it
        if(check == null || check.isEmpty())
        {
            distributedHashTable.put(fileName, peers);
            System.out.println("Registered "+key+ " Successfully");
        }
        else	//already Registered checking ...
        {
            Iterator<String> iterator = check.listIterator();

            while(iterator.hasNext())
            {
                String chkPid = iterator.next();

                if(chkPid.equals(value))
                {
                    //System.out.println("Already Registered !!!");
                    return true;
                }
            }

            //add new PeerID to existing FileName
            check.add(value);
            distributedHashTable.put(fileName,check);

            //FOR DOCUMENTATION PURPOSE DISPLAYING THE DHT	--- will be commented while running in actual
            //System.out.println("The Distributed Hash Table is now: "+distributedHashTable);

        }
        //if put into hashtable is successful then true
        return true;
    }
    public static List<String> search(String fileName) throws IOException
    {
        List <String> filePeers = new ArrayList<String>();

        fileName = fileName.substring(0,23).replace("*","");
        filePeers = distributedHashTable.get(fileName);	//get the Value(s) i.e location, for the Key(Filename)

        return filePeers;
    }

    /*
     * Registry(): to register the files requested by the client
     * Update in DHT the values
     */
    public static synchronized boolean reigstry(String key, String value)
    {
        //obtain the actual key by removing the padded values from the Key & value
        key = key.substring(0,23).replace("*","");
        value = value.substring(0,999).replace("*", "");

        //create temporary lists to store the new PeerID's for the files
        ArrayList<String> peers = new ArrayList<String>();
        ArrayList<String> check = new ArrayList<String>();

        //add peers to the list
        peers.add(value);

        String fileName = key;

        check = distributedHashTable.get(key);	//get the value(PeerID) for the Key(Filename)

        //check if file is already registered by the same peer, if not register it
        if(check == null || check.isEmpty())
        {
            distributedHashTable.put(fileName, peers);
            System.out.println("Registered "+key+ " Successfully");
        }
        else	//already Registered checking ...
        {
            Iterator<String> iterator = check.listIterator();

            while(iterator.hasNext())
            {
                String chkPid = iterator.next();

                if(chkPid.equals(value))
                {
                    //System.out.println("Already Registered !!!");
                    return true;
                }
            }

            //add new PeerID to existing FileName
            check.add(value);
            distributedHashTable.put(fileName,check);

            //FOR DOCUMENTATION PURPOSE DISPLAYING THE DHT	--- will be commented while running in actual
            //System.out.println("The Distributed Hash Table is now: "+distributedHashTable);

        }
        //if put into hashtable is successful then true
        return true;
    }




    public static List<String> fileSearch(String fileName) throws IOException
    {
        List <String> filePeers = new ArrayList<String>();

        fileName = fileName.substring(0,23).replace("*","");
        filePeers = distributedHashTable.get(fileName);	//get the Value(s) i.e location, for the Key(Filename)

        return filePeers;
    }





    public static void downloadFile(String fileName, Socket clientSocket)
    {
        //System.out.println("Client connected for File sharing ...");
        try
        {
            DataOutputStream dOut = new DataOutputStream(clientSocket.getOutputStream());
            //DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());

            String peerName = "peer"+(ConfigureServer.serverArgs.substring(ConfigureServer.serverArgs.length()-1));

            //System.out.println("Requested file is: "+fileName);
            //String peerForFile = dIn.readUTF();

            //The Path of the file to be downloaded
            File checkFile = new File(PATH_OF_FILE + peerName +"/" + fileName);

            //creating intput streams & buffer streams for reading
            FileInputStream fin = new FileInputStream(checkFile);
            BufferedInputStream buffReader = new BufferedInputStream(fin);

            //check if the file exists, for it to be downloaded
            if (!checkFile.exists())
            {
                System.out.println("File does not Exists");
                buffReader.close();
                return;
            }

            //get the file size
            int size = (int)checkFile.length();	//convert from long to int

            byte[] buffContent = new byte[8192];

            //send file size
            dOut.writeLong(size);

            //allocate a buffer to store contents of file

            int numOfRead = -1;	//how much is read in each read() call

            BufferedOutputStream buffOut = new BufferedOutputStream(clientSocket.getOutputStream());

            //read the bytes from file in chunks of 8192, until End of File Stream (-1) is not reached
            while((numOfRead = buffReader.read(buffContent)) != -1)
            {
                //write to client in chunks of 8192
                dOut.write(buffContent, 0, numOfRead);
            }

            System.out.println("Transferring File SUCCESS !!!");
            buffReader.close();
        }
        catch(IOException ex)
        {
            System.out.println("Exception in file sharing");
        }
    }



    }

