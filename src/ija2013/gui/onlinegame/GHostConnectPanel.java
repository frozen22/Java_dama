/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   HostConnectPanel
 */

package ija2013.gui.onlinegame;

import ija2013.gui.GMenuFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BorderFactory;


/**
 * Menu s vyberem vytvoreni server nebo pripojeni k cekajicimu serveru.
 * 
 * @author Fratisek Nemec (xnemec61)
 */
public class GHostConnectPanel extends JPanel {
    public static final long serialVersionUID = 42424213L;
    
    protected GMenuFrame parFrame;
    
    // komponenty
    private JButton btnHost;
    private JButton btnConnect;
    private JButton btnBack;
   
    /**
     * Konstruktor. Incializace komponent.
     *
     * @param frame Frame v kterem je tato komponenta zobrazena
     */
    public GHostConnectPanel(GMenuFrame frame) {
        parFrame = frame;
        initComponents();
    }

    /**
     * Inicializace komponent.
     */
    private void initComponents() {
        final Component thisComp = this;

        btnHost = new JButton("Host");
        btnConnect = new JButton("Connect");
        btnBack = new JButton("Back");

        btnHost.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // zobrazeni menu pro vytvoreni serveru
                parFrame.showHostSetup(thisComp);
            }
        });

        btnConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // zobrazeni menu pro pripojeni k serveru
                parFrame.showConnectSetup(thisComp);
            }
        });

        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // zpet do hlavniho menu
                parFrame.showMainMenu(thisComp);
            }
        });

        setLayout(new GridLayout(3, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(btnHost);
        add(btnConnect);
        add(btnBack);
        setPreferredSize(new Dimension(200, 150));
    }
}
