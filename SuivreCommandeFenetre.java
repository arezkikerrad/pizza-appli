import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;

public class SuivreCommandeFenetre extends JFrame {
    
    private JPanel panelContenu;
    private JButton btnFermer;
    private JButton btnRafraichir;
    private Fenetre fenetrePrincipale;
    private Timer timer;
    private HashMap<Integer, Timer> timersArchivage = new HashMap<>();
    
    public SuivreCommandeFenetre(Fenetre fenetrePrincipale) {
        this.fenetrePrincipale = fenetrePrincipale;
        
        
        setTitle("Suivi des commandes");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        
        JLabel labelTitre = new JLabel("Vos commandes en cours", JLabel.CENTER);
        labelTitre.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(labelTitre, BorderLayout.NORTH);
        
        
        panelContenu = new JPanel();
        panelContenu.setLayout(new BoxLayout(panelContenu, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelContenu);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
    
        
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        
        btnRafraichir = new JButton("Rafraîchir");
        btnRafraichir.setPreferredSize(new Dimension(100, 30));
        panelBas.add(btnRafraichir);
        
        btnFermer = new JButton("Fermer");
        btnFermer.setPreferredSize(new Dimension(100, 30));
        panelBas.add(btnFermer);
        
        panelPrincipal.add(panelBas, BorderLayout.SOUTH);
        
        
        add(panelPrincipal);
        
    
        btnFermer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nettoyerTimers();
                dispose();
            }
        });
        
        
        btnRafraichir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficherCommandes();
            }
        });
        
        
        timer = new Timer(1000, new ActionListener() {  
            public void actionPerformed(ActionEvent e) {
                afficherCommandes();
            }
        });
        timer.start();
        
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                nettoyerTimers();
            }
        });
        
        
        afficherCommandes();
    }

    private void nettoyerTimers() {
        if (timer != null) {
            timer.stop();
        }
        
        
        for (Timer t : timersArchivage.values()) {
            if (t != null && t.isRunning()) {
                t.stop();
            }
        }
        timersArchivage.clear();
    }
    
    private void afficherCommandes() {
        
        panelContenu.removeAll();
        
        
        if (Session.commandesActives == null || Session.commandesActives.isEmpty()) {
        
            JLabel labelAucuneCommande = new JLabel("Aucune commande en cours", JLabel.CENTER);
            labelAucuneCommande.setFont(new Font("Arial", Font.ITALIC, 14));
            labelAucuneCommande.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelContenu.add(Box.createVerticalStrut(30));
            panelContenu.add(labelAucuneCommande);
        } else {
            
            for (commande cmd : Session.commandesActives) {
                
                int tempsRestantSecondes = cmd.getTempsRestantSecondes();
                if (tempsRestantSecondes <= 0) {
                    
                    JPanel panelCommande = new JPanel();
                    panelCommande.setLayout(new BoxLayout(panelCommande, BoxLayout.Y_AXIS));
                    panelCommande.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(5, 5, 10, 5),
                        BorderFactory.createLineBorder(new Color(34, 139, 34), 2, true)  
                    ));
                    panelCommande.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                    panelCommande.setBackground(new Color(240, 255, 240));  
                    
                    
                    JLabel labelNumero = new JLabel("Commande #" + cmd.getNumero_commande());
                    labelNumero.setFont(new Font("Arial", Font.BOLD, 14));
                    labelNumero.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panelCommande.add(labelNumero);
                    panelCommande.add(Box.createVerticalStrut(10));
                    
                    
                    JLabel labelLivree = new JLabel("Commande livrée !");
                    labelLivree.setFont(new Font("Arial", Font.BOLD, 14));
                    labelLivree.setForeground(new Color(34, 139, 34));  
                    labelLivree.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panelCommande.add(labelLivree);
                    
                    panelContenu.add(panelCommande);
                    panelContenu.add(Box.createVerticalStrut(10));
                    
                    
                    final int cmdNumero = cmd.getNumero_commande();
                    if (!timersArchivage.containsKey(cmdNumero)) {
                        Timer archiveTimer = new Timer(10000, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                Session.archiverCommande(cmd);
                                afficherCommandes();
                                
                                Timer source = (Timer) e.getSource();
                                source.stop();
                                timersArchivage.remove(cmdNumero);
                            }
                        });
                        archiveTimer.setRepeats(false); 
                        archiveTimer.start();
                        timersArchivage.put(cmdNumero, archiveTimer);
                    }
                    
                    continue;
                }
                
                
                JPanel panelCommande = new JPanel();
                panelCommande.setLayout(new BoxLayout(panelCommande, BoxLayout.Y_AXIS));
                panelCommande.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 10, 5),
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)
                ));
                panelCommande.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); 
                
                
                JLabel labelNumero = new JLabel("Commande #" + cmd.getNumero_commande());
                labelNumero.setFont(new Font("Arial", Font.BOLD, 14));
                labelNumero.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelCommande.add(labelNumero);
                panelCommande.add(Box.createVerticalStrut(5));
                
                
                JPanel panelInfos = new JPanel(new GridLayout(3, 1, 5, 2));
                panelInfos.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                if (cmd.getLivreur() != null) {
                
                    JLabel labelNom = new JLabel("Livreur: " + cmd.getLivreur().getNom());
                    panelInfos.add(labelNom);
                    
                    
                    JLabel labelVehicule = new JLabel("Véhicule: " + cmd.getLivreur().getVehicule());
                    panelInfos.add(labelVehicule);
                    
                    
                    String tempsFormate = cmd.getTempsRestantFormate();
                    JLabel labelTemps = new JLabel("Temps restant: " + tempsFormate);
                    
                    
                    if (tempsRestantSecondes <= 300) { 
                        labelTemps.setForeground(Color.RED);
                    }
                    panelInfos.add(labelTemps);
                } else {
                    
                    
                    JLabel labelPreparation = new JLabel("Commande en préparation");
                    panelInfos.add(labelPreparation);
                    
                    JLabel labelAttente = new JLabel("En attente d'un livreur...");
                    panelInfos.add(labelAttente);
                }
                
                panelCommande.add(panelInfos);
                
                
                panelContenu.add(panelCommande);
                panelContenu.add(Box.createVerticalStrut(10));
            }
        }
        
        
        panelContenu.revalidate();
        panelContenu.repaint();
    }
    
    
    public void rafraichir() {
        afficherCommandes();
    }
}