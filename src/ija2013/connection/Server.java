/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Server
 */

package ija2013.connection;


import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


/**
 * Serverova cast komunikace po siti.
 *
 * @author Frantisek Nemec (xnemec61), Jan Opalka (xopalk01)
 */
public class Server {
    private int serverPort;          // cislo portu server
    private ServerSocket serverSock; // socket serveru
    private Socket clientSock;       // socket pripojeneno klienta
    private InputStream sockInput;
    private OutputStream sockOutput;
    
    /**
     * Konstruktor.
     * 
     * @param serverPort Cislo portu na kterem je server spusten
     */
    public Server(int serverPort) {
        this.serverPort = serverPort;
        serverSock = null;
        clientSock = null;
        sockInput = null;
        sockOutput = null;
    }
    
    /**
     * Vytvoreni socketu.
     * 
     * @return True - vytvoreni bylo uspesne, False - neuspesne
     */
    public boolean create(){
        try {
            serverSock = new ServerSocket(serverPort);
        }
        catch (IOException e){
            e.printStackTrace(System.err);
            return false;
        }
        
        return true;
    }
    
    /**
     * Cekani na pripojeni klienta.
     * 
     * @return True - uspesne pripojeni klienta, False - accept error
     */
    public boolean waitForConnection() {
        try {
            clientSock = serverSock.accept();
            sockInput = clientSock.getInputStream();
            sockOutput = clientSock.getOutputStream();
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
            return false;
        }
        
        return true;
    }
    
    
    /**
     * Cteni ze socketu.
     * 
     * @return Precteny retezec. Null v pripade chyby.
     */
    public String read() {
        String strRead = null;
        byte[] buf = new byte[1024];
        int bytes_read;

        try {
            
            bytes_read = sockInput.read(buf, 0, buf.length); // cteni
            if (bytes_read < 0) { // kontrola precteni
                return null;
            }

            strRead = new String(buf, 0, bytes_read); // konverze do retezce
        }
        catch (IOException e) {
            return null;
        }
        
        return strRead;
    }
    
    //!!! pripano po prepsani do cpp
    /**
     * Cteni urcite velikosti dat ze socketu.
     * 
     * @param dataLen Velikost data ktera budou nactena
     * @return Prectena data
     */
    public String read(int dataLen) {
        String strRead = "";
        byte[] buf = new byte[1024];
        int bytes_read;
        int dataLenToRead = dataLen;
        
        try {
            while (dataLenToRead != 0){
                bytes_read = sockInput.read(buf, 0, buf.length); // cteni
                if (bytes_read < 0) { // cteni selhalo
                    return null;
                }

                strRead += new String(buf, 0, bytes_read);
                dataLenToRead -= bytes_read;
            }
        }
        catch (IOException e) {
            return null;
        }
        
        return strRead;
    }
    
    //!!! pripano po prepsani do cpp
    /**
     * Odeslani zpravy pripojenemu klientovy.
     * 
     * @param dataString Odesilana zprava.
     */
    public void send(String dataString) {
        byte[] data = dataString.getBytes();

        try {
            sockOutput.write(data, 0, data.length);
            sockOutput.flush();
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
    
    /**
     * Uzavreni socketu.
     */
    public void close() {
        try {
            if (clientSock != null && !clientSock.isClosed()) {
                clientSock.close();
            }
            if (serverSock != null && !serverSock.isClosed()){
                serverSock.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
    
    public InputStream getInputSocket(){
        return sockInput;
    }
    
    public OutputStream getOutputSocket() {
        return sockOutput;
    }
    
}
