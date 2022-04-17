package Server;

import Middle.FileControllerImpl;
import Middle.FileControllerInterface;
import jdk.dynalink.beans.StaticClass;

import java.rmi.registry.Registry;
import   java.rmi.server.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server implements Runnable {

    @Override
    public void run() {
        {
            try {
            Registry registry= LocateRegistry.createRegistry(5555);
                FileControllerImpl FilerControllerInterface = new FileControllerImpl();
                registry.bind("FileControllerImpl", FilerControllerInterface);

                System.out.println(" Server is ready.");
            } catch (Exception e) {
                System.out.println(" Server failed: " + e);
            }
        }

    }
    public static void main(String [] args)
    {
        new Server().run();


    }



}