/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Klient
 */

package ija2013.connection;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Klientska cast komunikace po siti.
 *
 * @author Frantisek Nemec (xnemec61), Jan Opalka (xopalk01)
 */
public class Client  {
    private String serverHostname; // adresa serveru
    private int serverPort; // cislo serveru
    private Socket clientSocket;
    private InputStream sockInput;
    private OutputStream sockOutput;

    /**
     * Konstruktor.
     * 
     * @param serverHostname Adresa serveru
     * @param serverPort Port serveru
     */
    public Client(String serverHostname, int serverPort) {
        this.serverHostname = serverHostname;
        this.serverPort = serverPort;
        clientSocket = null;
        sockInput = null;
        sockOutput = null;
    }
    
    /**
     * Pripojeni k serveru.
     * 
     * @return True - pripojeni probehlo uspesne, False - neuspesne.
     */
    public boolean connect(){
        try {
            clientSocket = new Socket(serverHostname, serverPort);
            sockInput = clientSocket.getInputStream();
            sockOutput = clientSocket.getOutputStream();
        }
        catch (IOException e){
            return false;
        }
        
        return true;
    }
    
    /**
     * Odeslani zpravy na server.
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
     * Cteni ze socketu.
     * 
     * @return Precteny retezec. V pripade neuspechu null.
     */
    public String read() {
        String strRead = null;
        byte[] buf = new byte[1024]; // ?
        int bytes_read;

        try {
            bytes_read = sockInput.read(buf, 0, buf.length);
            if (bytes_read < 0) { // Chyba
                return null;
            }
            strRead = new String(buf, 0, bytes_read);
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return strRead;
    }

    /**
     * Uzavreni socketu.
     */
    public void close() {
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
            
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
    
    public InputStream getInputSocket() {
        return sockInput;
    }

    public OutputStream getOutputSocket() {
        return sockOutput;
    }
    
}
