/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Hlavni menu
 */
 
package ija2013.gui;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

/**
 * Hlavni menu aplikace. Obsahuje tlacitka s vyberem hry (local versus AI, local
 * vice hracu, online hra.
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public class GMainMenu extends JPanel {
    public static final long serialVersionUID = 42424204L;
    
    protected GMenuFrame parFrame;

    // komponenty
    private JButton btnLocalAI;
    private JButton btnLocalMP;
    private JButton btnOnlineMP;
    private JButton btnReplay;
    private JButton btnEnd;
    
    public GMainMenu(GMenuFrame frame) {
        this.parFrame = frame;
        initComponents();
    }

    /**
     * Inicializace komponent menu.
     */
    private void initComponents() {
        // urceno pro predani odkazu na tuto komponentu do udalosti stisknuti 
        // na tlacitka
        final Component thisComp = this;
        
        btnLocalAI = new JButton("Local AI");
        btnLocalMP = new JButton("Local MP");
        btnOnlineMP = new JButton("Online MP");
        btnReplay = new JButton("Replay");
        btnEnd = new JButton("Exit");
        
        btnLocalAI.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                parFrame.setTypeOfGame(GMenuFrame.creatingGameTypes.localAI);
                parFrame.showTypeOfGameMenu(thisComp);
            }
        });
        
        btnLocalMP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                parFrame.setTypeOfGame(GMenuFrame.creatingGameTypes.localMP);
                parFrame.showTypeOfGameMenu(thisComp);
            }
        });
        
        btnOnlineMP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                parFrame.setTypeOfGame(GMenuFrame.creatingGameTypes.onlineMP);
                parFrame.showHostConnectMenu(thisComp);
            }
        });
        
        btnReplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                GReplayFrame repFrame = new GReplayFrame();
                repFrame.setVisible(true);
            }
        });

        btnEnd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                System.exit(0);
            }
        });

        setLayout(new GridLayout(5, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(btnLocalAI);
        add(btnLocalMP);
        add(btnOnlineMP);
        add(btnReplay);
        add(btnEnd);

        setPreferredSize(new Dimension(200,240));
        
    }

      
}
