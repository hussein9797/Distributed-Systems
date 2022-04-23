package Client;

import Middle.DownloadImpl;
import Middle.DownloadInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client  {


    public static void main(String [] args) throws IOException {

        BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
        String PortNumber=null;
        System.out.println("Enter the port number on which peer needs to be registered");

        PortNumber=inp.readLine();
        System.out.println("Enter the directory path");
        String directoryName = inp.readLine();

        try{
            Registry registry= LocateRegistry.createRegistry(Integer.parseInt(PortNumber));
            DownloadInterface fi = new DownloadImpl(directoryName);
            System.out.println("Directory name  "+directoryName);

            registry.rebind("rmi://localhost:"+PortNumber+"/FileServer", fi);
        } catch(Exception e) {
            System.err.println("FileServer exception: "+ e.getMessage());
            e.printStackTrace();
        }
        new ClientCoordinator(PortNumber,directoryName).run();


    }
}

