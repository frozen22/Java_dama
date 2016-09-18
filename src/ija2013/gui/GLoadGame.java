/**
 * Predmet: Seminar Java (IJA)
 * Autori:  Frantisek Nemec (xnemec61)
 *          Jan Opalka      (xopalk01)
 * 
 * Datum:   Duben 2013
 * Popis:   Load Game
 */


package ija2013.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import ija2013.basis.XMLGameSave;
import javax.swing.BorderFactory;


/**
 * Zakladni panel nacitani ulozene hry. Tridy ktere rozsiruji tento panel
 * implementuji metodu stisku tlacitka "Pokracovat".
 * 
 * @author Frantisek Nemec (xnemec61)
 */
public abstract class GLoadGame extends JPanel {
    public static final long serialVersionUID = 42424203L;

    // hlavni menu (frame) ve kterem se tato komponenta nachazi
    protected JFrame parentFrame;
    
    // komponenty
    protected JButton btnLoadFromXml;
    protected JButton btnLoadFromBasic;
    protected JButton btnEnd;
    protected JButton btnContinue;
    protected JTextArea textAreaBasic;
    
    /**
     * Konstruktor. Inicializa komponent, ulozeni rodicovskeho framu.
     *
     * @param parFrame Frame do ktereho je tato komponenta vlozena 
     */    
    public GLoadGame(JFrame parFrame) {
        parentFrame = parFrame;
        initComponents();
    }
    
    /**
     * Inicializace komponent. Textove pole pro primi zapis hry v zakladni 
     * notaci a robrazeni ulozenych her pri jejich nacteni ze souboru.
     * Tlacitko pro nacteni ulozene hry ze souboru v zakladni notaci a 
     * v xml formatu.
     */
    private void initComponents() {
        JPanel btnPanel = new JPanel();
        JPanel textAreaPanel = new JPanel();
        
        btnLoadFromXml = new JButton("Nacist ze souboru (XML)");
        btnLoadFromBasic = new JButton("Nacist ze souboru (basic)");
        btnContinue = new JButton("Pokracovat");
        textAreaBasic = new JTextArea();

        // pridani scroll baru
        JScrollPane sp = new JScrollPane(textAreaBasic);
        sp.setPreferredSize(new Dimension(200,300));
        textAreaPanel.add(sp);
        
        btnLoadFromBasic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // zobrazeni dialogu pro vyber souboru
                File file = getFile();
                if (file != null) {
                    String text = getFileContent(file);
                    textAreaBasic.setText(text);
                }
            }
        });
        
        btnLoadFromXml.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // zobrazeni dialogu pro vyber souboru
                File file = getFile();
                if (file != null) {
                    // nacteni obsahu souboru
                    String text = getFileContent(file);

                    if (text == null) {
                        JOptionPane.showMessageDialog(parentFrame, "Chyba pri nacitani ze souboru.");
                        return;
                    }
                    
                    // zpracovani xml formatu
                    XMLGameSave xml = new XMLGameSave(text);
                    // nacteni prevedenoho xml formatu do zakladni notace
                    text = xml.getBasic();
                    
                    if(text != null) {
                        textAreaBasic.setText(text);
                    }
                    else {
                        JOptionPane.showMessageDialog(parentFrame, "Chyba pri zpracovani dat ze souboru.");
                    }
                }
            }
        });
        
        btnEnd = new JButton("Konec");
        btnEnd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                parentFrame.dispose(); //???? tady to bude chtit predelat
            }
        });
        
        btnContinue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                btnContinueClicked();
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                
        btnPanel.setLayout(new GridLayout(4, 1,5,5));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnPanel.add(btnLoadFromBasic);
        btnPanel.add(btnLoadFromXml);
        btnPanel.add(btnContinue);
        btnPanel.add(btnEnd);
        
        add(textAreaPanel);
        add(btnPanel);
        
    }
    
    /**
     * Zobrazeni dialogu pro vyber souboru.
     * 
     * @return Vybrany soubor. Null - pokud neni soubor vybran. 
     */
    public File getFile() {
        JFileChooser fileopen = new JFileChooser("./examples/");

        int ret = fileopen.showOpenDialog(this);

        if (ret == JFileChooser.APPROVE_OPTION) {
            return fileopen.getSelectedFile();
        }

        return null;
    }
    
    /**
     * Nacteni obsahu souboru do Stringu.
     * 
     * @param file Soubor ze ktereho cteme
     * @return Obsah souboru. Null - pri chybe.
     */
    public String getFileContent(File file) {
        String text = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            // nacitani po radku
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }

            text = sb.toString();
        }
        catch (Exception e) {
            return null;
        }

        return text;
    }

    /**
     * Metoda ktera se ma provest pri kliknu na tlacitko pokracovat. 
     */
    public abstract void btnContinueClicked();

}
