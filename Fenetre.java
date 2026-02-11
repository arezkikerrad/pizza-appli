import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Fenetre extends JFrame {
    
    
    private JLabel labelSolde;
    private JLabel labelFidelite; 

    public Fenetre() {
        setTitle("RaPizza - Menu Principal");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        
        
        JLabel labelTitre = new JLabel("RaPizza");
        labelTitre.setFont(new Font("Arial", Font.BOLD, 32));
        labelTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(labelTitre);
        topPanel.add(Box.createVerticalStrut(10));  
        
        
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        topPanel.add(separator);
        topPanel.add(Box.createVerticalStrut(10));  
        
        
        JPanel clientPanel = new JPanel(new BorderLayout(20, 10));
        
        
        String messageAccueil;
        if (Session.clientConnecte != null) {
            messageAccueil = "Bienvenue, " + Session.clientConnecte.getNom() + " !";
        } else {
            messageAccueil = "Bienvenue, Client !";
        }
        JLabel labelNomClient = new JLabel(messageAccueil);
        
        labelNomClient.setFont(new Font("Arial", Font.BOLD, 18));
        clientPanel.add(labelNomClient, BorderLayout.WEST);
        
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        
        JButton btnVoirCompte = new JButton("Voir Son Compte");
        styleButton(btnVoirCompte);
        btnVoirCompte.setAlignmentX(Component.RIGHT_ALIGNMENT);
        rightPanel.add(btnVoirCompte);
        rightPanel.add(Box.createVerticalStrut(5));  
        
        
        
        String texteSolde;
        if (Session.clientConnecte != null) {
            texteSolde = "Solde actuel : " + Session.clientConnecte.getSolde() + " €";
        } else {
            texteSolde = "Solde actuel : 0.00 €";
        }
        labelSolde = new JLabel(texteSolde);
        labelSolde.setAlignmentX(Component.RIGHT_ALIGNMENT);
        rightPanel.add(labelSolde);
        rightPanel.add(Box.createVerticalStrut(5));  
        
        
        String texteFidelite;
        if (Session.clientConnecte != null) {
            int nbPizzas = Session.clientConnecte.getNb_pizza_achete();
            texteFidelite = "Points fidélité : " + nbPizzas + "/10 pizzas";
            
            
            
        } else {
            texteFidelite = "Points fidélité : 0/10 pizzas";
        }
        labelFidelite = new JLabel(texteFidelite);
        labelFidelite.setAlignmentX(Component.RIGHT_ALIGNMENT);
        rightPanel.add(labelFidelite);
        
        clientPanel.add(rightPanel, BorderLayout.EAST);
        
        
        topPanel.add(clientPanel);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        
        JButton btnCommander = new JButton("Commander une pizza");
        styleButton(btnCommander);
        btnCommander.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(btnCommander);
        centerPanel.add(Box.createVerticalStrut(15));  
        
        
        JButton btnSuivreCommande = new JButton("Suivre commande");
        styleButton(btnSuivreCommande);
        btnSuivreCommande.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(btnSuivreCommande);
        centerPanel.add(Box.createVerticalStrut(15));  
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnQuitter = new JButton("Quitter");
        styleButton(btnQuitter);

        
        btnQuitter.setPreferredSize(new Dimension(120, 40));  
        btnQuitter.setMaximumSize(new Dimension(120, 40));    

        bottomPanel.add(btnQuitter);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        
        add(mainPanel);
        
        
        btnCommander.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                new CommanderPizzaFenetre(Fenetre.this).setVisible(true);
            }
        });
        
        btnVoirCompte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                new VoirSoldeFenetre(Fenetre.this).setVisible(true);
            }
        });
        
        btnSuivreCommande.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Session.clientConnecte != null && !Session.commandesActives.isEmpty()) {
                    new SuivreCommandeFenetre(Fenetre.this).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(Fenetre.this, 
                        "Aucune commande active à suivre.", 
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        btnQuitter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    
    
    public void mettreAJourSolde(float nouveauSolde) {
        if (labelSolde != null) {
            labelSolde.setText("Solde actuel : " + nouveauSolde + " €");
        }
    }
    
    
    public void mettreAJourFidelite(int nbPizzas) {
        if (labelFidelite != null) {
            String texteFidelite = "Points fidélité : " + nbPizzas + "/10 pizzas";
            
            
            
            
            labelFidelite.setText(texteFidelite);
        }
    }
    
    
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(80, 180, 100));  
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));  
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }
}