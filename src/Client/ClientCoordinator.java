package Client;

import File.CFile;
import Middle.DownloadInterface;
import Middle.FileControllerInterface;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class ClientCoordinator implements Runnable {

   String portNumber=null;
   String dirPath=null;
   String targetFile=null;

    public ClientCoordinator(String portNumber, String dirPath) {
        this.portNumber = portNumber;
        this.dirPath = dirPath;
    }

    @Override
    public void run() {
        String userId=null;

        try {
            Registry registry = LocateRegistry.getRegistry(5555);
            FileControllerInterface fileControllerStub= (FileControllerInterface) registry.lookup("FileControllerImpl");

            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter user Id");
            userId=bufferedReader.readLine();

            File directoryList = new File(dirPath);
            String[] store = directoryList.list();
            int counter=0;
            while(counter<store.length)
            {
                File currentFile = new File(store[counter]);
                fileControllerStub.uploadFile(userId, currentFile.getName(),portNumber,dirPath);
                counter++;
            }

            ArrayList<CFile> files =new ArrayList<CFile>();
            System.out.println("enter the file name you want to search");
            while((targetFile=bufferedReader.readLine())!=null){
                files=fileControllerStub.fileSearch(targetFile);
                for (CFile file : files) {
                    System.out.println("User ID's have the target file" + file.getUserId());
                }
                System.out.println("Enter the User ID  to connect to his node and download the file from it");
                userId= bufferedReader.readLine();
                downloadFromPeer(userId,files);
                break;
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        }


    }

    public void downloadFromPeer(String userId,ArrayList<CFile> cFiles) throws NotBoundException, RemoteException, MalformedURLException, IOException{
        //get port
        String portForAnotherNode=null;
        String sourceDir=null;
        for(int i=0;i<cFiles.size();i++){
            if(userId.equals(cFiles.get(i).getUserId())){
                portForAnotherNode=cFiles.get(i).getPortNumber();
                sourceDir=cFiles.get(i).getDirectory();
            }
        }
        Registry registry =LocateRegistry.getRegistry();
//        DownloadInterface DownLoadStub = (DownloadInterface) registry.lookup("rmi://localhost:"+portForAnotherNode+"/FileServer");
//        byte[] filetoDownload=DownLoadStub.downloadFile(targetFile);
        String source = sourceDir+"\\"+targetFile;
        //directory where file will be copied
        String target =dirPath;
        InputStream is = null;
        OutputStream os = null;
        try {
            File srcFile = new File(source);
            File destFile = new File(target);
            System.out.println("file "+destFile);
            if(!destFile.exists())
            {
                destFile.createNewFile();
            }
            is = new FileInputStream(srcFile);

            os = new FileOutputStream(target+"\\"+srcFile.getName());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            is.close();
            os.close();
        }
    }



    }

