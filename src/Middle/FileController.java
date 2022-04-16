package Middle;

import FileStructure.File;
import FileStructure.Header;

import java.rmi.Remote;
import java.util.ArrayList;

public interface FileController extends Remote {

    void creatFile (String filename);

    int generateClientId();

    ArrayList<Header> showList();

    ArrayList<File> updateLocalFiles();

    File getFile(String filename);

    boolean uploadFile(String filename ,byte [] content);



}
