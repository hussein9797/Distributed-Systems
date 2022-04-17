package Middle;

import File.CFile;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class FileControllerImpl   extends UnicastRemoteObject implements FileControllerInterface  {


    ArrayList<CFile> Files;


    public FileControllerImpl() throws RemoteException {
        super();
        Files=new ArrayList<CFile>();
    }

    @Override
    public void uploadFile(String userId, String fileName, String portNumber, String localDir) throws RemoteException {
     CFile F=new CFile();
     F.setUserId(userId);
     F.setFileName(fileName);
     F.setPortNumber(portNumber);
     F.setDirectory(localDir);
     Files.add(F);

    }

    @Override
    public ArrayList<CFile> fileSearch(String fileName) throws RemoteException{
       ArrayList<CFile> FoundFiles= new ArrayList<CFile>();
        for (CFile F: this.Files) {
            if (F.getFileName().equalsIgnoreCase(fileName))
                FoundFiles.add(F);
        }
        return FoundFiles;

    }
}
