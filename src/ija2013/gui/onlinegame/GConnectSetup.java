/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Connect Setup
 */

package ija2013.gui.onlinegame;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import ija2013.connection.Client;
import ija2013.gui.GMenuFrame;


/**
 * Menu pro pripojeni clienta k server. Obsahuje textove pole pro zadani portu
 * a adresy serveru.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GConnectSetup extends JPanel {
    public static final long serialVersionUID = 42424212L;
        
    protected GMenuFrame parFrame;
    
    // komponenty
    private JButton btnConnect;
    private JButton btnBack;
    private JTextField textClientPort;
    private JTextField textClientAddr;
    private JLabel labelInfo;

    /**
     * Konstruktor. Incializace komponent.
     *
     * @param frame Frame v kterem je tato komponenta zobrazena
     */
    public GConnectSetup(GMenuFrame frame) {
        parFrame = frame;
        initComponents();
    }

    /**
     * Inicializase komponent
     */
    private void initComponents() {
        final Component thisComp = this;

        textClientPort = new JTextField("11111");
        textClientAddr = new JTextField("127.0.0.1");
        btnConnect = new JButton("Connect");
        btnBack = new JButton("Back");
        labelInfo = new JLabel("Zadejte port a adresu serveru.");

        btnConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                labelInfo.setText("Making client");

                // Vytvoreni a pripojeni klienta
                Client client = new Client(textClientAddr.getText(), Integer.parseInt(textClientPort.getText()));
                if (client.connect() == false) {
                    labelInfo.setText("Connection failed");
                    return;
                }

                labelInfo.setText("Connected");
                parFrame.client = client;

                // zobrazeni nabidky vyberu typu hry
                parFrame.showTypeOfGameMenu(thisComp);
            }
        });

        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Krok zpet do vyberu hostovani resp. pripojeni
                parFrame.clientClose();
                parFrame.showHostConnectMenu(thisComp);
            }
        });

        setLayout(new GridLayout(5, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JPanel portPanel = new JPanel(new GridLayout(2,1,0,0));
        JPanel addrPanel = new JPanel(new GridLayout(2,1,0,0));
        portPanel.add(new JLabel("Port:"));
        portPanel.add(textClientPort);
        addrPanel.add(new JLabel("Address:"));
        addrPanel.add(textClientAddr);
        
        add(portPanel);
        add(addrPanel);
        add(btnConnect);
        add(btnBack);
        add(labelInfo);

        setPreferredSize(new Dimension(200, 240));

    }
}
