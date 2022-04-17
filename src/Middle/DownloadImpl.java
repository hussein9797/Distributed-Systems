package Middle;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DownloadImpl extends UnicastRemoteObject implements DownloadInterface {

    String dirName;

    public DownloadImpl(String dirName) throws RemoteException {
        super();
       this.dirName=dirName;
    }

    @Override
    public byte[] downloadFile(String fileName) throws RemoteException {
        try {
            File file = new File(dirName+"/"+fileName);
            byte buffer[] = new byte[(int)file.length()];
            BufferedInputStream input = new
                    BufferedInputStream(new FileInputStream(dirName+"//"+fileName));
            input.read(buffer,0,buffer.length);
            input.close();
            return(buffer);
        } catch(Exception e){
            System.out.println("DownloadImpl: "+e.getMessage());
            e.printStackTrace();
            return(null);
        }
    }
}
