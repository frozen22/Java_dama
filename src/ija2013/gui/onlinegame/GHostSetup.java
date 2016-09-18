/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Host Setup panel
 */

package ija2013.gui.onlinegame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Component;
import java.net.InetAddress;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ija2013.connection.Server;
import ija2013.gui.GMenuFrame;
import ija2013.players.OnlinePlayer;
import ija2013.players.LocalPlayer;

/**
 * Menu pro vytvoreni servu. Obsahuje textove pole pro zadani portu.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GHostSetup extends JPanel {
    public static final long serialVersionUID = 42424214L;
        
    protected GMenuFrame parFrame;
    protected Server server;
    
    // komponenty
    private JLabel labelInfo;
    private JTextField textServerPort;
    private JButton btnCreate;
    private JButton btnBack;
    
    /**
     * Konstruktor. Incializace komponent.
     *
     * @param frame Frame v kterem je tato komponenta zobrazena
     */
    public GHostSetup(GMenuFrame frame) {
        parFrame = frame;
        initComponents();
    }

    private void initComponents() {
        final Component thisComp = this;

        textServerPort = new JTextField("11111");
        btnCreate = new JButton("Create");
        btnBack = new JButton("Back");
        labelInfo = new JLabel("Zadejte port serveru.");

        btnCreate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        labelInfo.setText("Server setting up");
                        server = new Server(Integer.parseInt(textServerPort.getText()));
                        parFrame.server = server;
                        if (server.create() == false) {
                            //--------=========ERROR=========-------
                            System.err.println("Failed to create server");
                            labelInfo.setText("Failed to create server");
                            return;
                        }

                        labelInfo.setText("Server waiting for connection");

                        if (server.waitForConnection() == false) {
                            System.err.println("Waiting for connection failed");
                            labelInfo.setText("Waiting for connection failed");
                            return;
                        }

                        labelInfo.setText("Someone connected");
                        
                        // Precteni zpravy ktera obsahuje:
                        //  1) Moji barvu
                        //       Zakodovano: 'w'|'b'
                        //  2) Jedna se o novou hru nebo pokracovani ulozene hry
                        //       Zakodovano: 'n'|'c'
                        //  3) Pokud se jedna o pokracovani nasleduje delka dat 
                        //     ktera obsahuji ulozenou hru
                        //       Zakodovano: "3215" bytu
                        String receivedMsg = server.read();
                        if (receivedMsg == null) {
                            return;
                        }
                        
                        String savedGameInBasicNotation;
                        int savedGameLen;

                        char myColor = receivedMsg.charAt(0);
                        char gameType = receivedMsg.charAt(1);

                        // Zpracovani dat o barve
                        if (myColor == 'w') {
                            parFrame.onlinePlayer1 = new LocalPlayer('w');
                            parFrame.onlinePlayer2 = new OnlinePlayer('b', server.getInputSocket(), server.getOutputSocket());
                        }
                        else if (myColor == 'b') {
                            parFrame.onlinePlayer1 = new OnlinePlayer('w', server.getInputSocket(), server.getOutputSocket());
                            parFrame.onlinePlayer2 = new LocalPlayer('b');
                        }
                        else {
                            //--------=========ERROR=========-------
                            return;
                        }

                        // Zpracovani dat o hre
                        if (gameType == 'n') { // nova hra
                            parFrame.createGame();
                        }
                        else if (gameType == 'c') { // pokracovani hry
                            // nasleduje nacteni dat s ulozenou hrou
                            savedGameLen = Integer.parseInt(receivedMsg.substring(2));
                            savedGameInBasicNotation = server.read(savedGameLen);
                            // Odeslani potvrzeni o prijmuti odeslane hry
                            server.send("ACK");
                            parFrame.createGame(savedGameInBasicNotation);
                        }
                        else { // ERROR neznama data
                            //--------=========ERROR=========-------
                            return;
                        }

                        parFrame.showMainMenu(thisComp);
                    }
                }).start();
            }
        });
        
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                parFrame.serverClose();
                parFrame.showHostConnectMenu(thisComp);
            }
        });
        
        setLayout(new GridLayout(4, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        
        JPanel portPanel = new JPanel(new GridLayout(2,1,0,0));
        portPanel.add(new JLabel("Port:"));
        portPanel.add(textServerPort);
        
        add(portPanel);
        add(btnCreate);
        add(btnBack);
        add(labelInfo);
        
        setPreferredSize(new Dimension(200,200));
        
    }
     
}
