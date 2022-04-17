package Middle;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DownloadInterface extends Remote {

    public byte [] downloadFile(String fileName) throws RemoteException;
}
