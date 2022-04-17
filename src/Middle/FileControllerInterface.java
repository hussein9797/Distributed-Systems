package Middle;

import File.CFile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface FileControllerInterface extends Remote {

  void uploadFile(String userId,String fileName,String portNumber,String localDir) throws RemoteException;

  ArrayList<CFile> fileSearch(String fileName) throws RemoteException;



}
