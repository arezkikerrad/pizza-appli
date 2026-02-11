import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CommanderPizzaFenetre extends JFrame {

    private JList<String> listePizzas;
    private JTextArea zoneIngredients;
    private JComboBox<String> comboTailles;
    private JLabel labelPrix;
    private JTextField fieldQuantite;
    private JLabel labelPrixTotal;  
    private JButton btnCommander;
    private JButton btnAnnuler;
    private JButton btnAjouterAuPanier;
    private Fenetre fenetrePrincipale; 
    private JLabel labelFidelite;
    private JPanel panelIngredientsSupp;
    private ArrayList<JCheckBox> checkboxesIngredients;
    private float prixIngredientSupp = 1.0f;
    private JLabel labelPrixIngredients;
    private pizza pizzaSelectionnee;
    private JCheckBox checkboxPizzaGratuite;
    
    private ArrayList<PanierPizza> panier;
    private JTable tablePanier;
    private DefaultTableModel modelePanier;
    private JLabel labelPrixTotalPanier;
    private JButton btnSupprimerDuPanier;

    public CommanderPizzaFenetre(Fenetre fenetrePrincipale) {
        this.fenetrePrincipale = fenetrePrincipale;
        this.checkboxesIngredients = new ArrayList<>();
        this.panier = new ArrayList<>();
        
        setTitle("Commander une pizza");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelGauche = new JPanel(new BorderLayout());
        panelGauche.setBorder(BorderFactory.createTitledBorder("Pizzas disponibles"));

        DefaultListModel<String> modeleListe = new DefaultListModel<>();
        for (pizza pizza : Session.catalogue) {
            modeleListe.addElement(pizza.getNom());
        }

        listePizzas = new JList<>(modeleListe);
        listePizzas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollListePizzas = new JScrollPane(listePizzas);
        panelGauche.add(scrollListePizzas, BorderLayout.CENTER);

        JPanel panelCentre = new JPanel(new BorderLayout(0, 10));
        JPanel panelIngredientsBase = new JPanel(new BorderLayout());
        panelIngredientsBase.setBorder(BorderFactory.createTitledBorder("Ingrédients de base"));

        zoneIngredients = new JTextArea();
        zoneIngredients.setEditable(false);
        zoneIngredients.setLineWrap(true);
        zoneIngredients.setWrapStyleWord(true);
        JScrollPane scrollIngredients = new JScrollPane(zoneIngredients);
        panelIngredientsBase.add(scrollIngredients, BorderLayout.CENTER);

        panelIngredientsSupp = new JPanel();
        panelIngredientsSupp.setBorder(BorderFactory.createTitledBorder("Ingrédients supplémentaires (+ 1.00 € chacun)"));
        panelIngredientsSupp.setLayout(new BoxLayout(panelIngredientsSupp, BoxLayout.Y_AXIS));

        String[] ingredientsSupplementaires = {
            "Jambon", "Champignons", "Poivrons", "Olives", 
            "Ananas", "Oignons", "Bacon", "Poulet", 
            "Anchois", "Thon", "Fromage de chèvre", "Gorgonzola"
        };

        for (String ing : ingredientsSupplementaires) {
            JCheckBox checkBox = new JCheckBox(ing);
            checkBox.addActionListener(e -> mettreAJourPrixTotal());
            panelIngredientsSupp.add(checkBox);
            checkboxesIngredients.add(checkBox);
        }

        panelCentre.add(panelIngredientsBase, BorderLayout.NORTH);
        JScrollPane scrollIngredientsSupp = new JScrollPane(panelIngredientsSupp);
        scrollIngredientsSupp.setPreferredSize(new Dimension(300, 150));
        panelCentre.add(scrollIngredientsSupp, BorderLayout.CENTER);

        JPanel panelOptions = new JPanel(new GridLayout(7, 2, 5, 5));
        panelOptions.setBorder(BorderFactory.createTitledBorder("Options"));

        JLabel labelTaille = new JLabel("Taille :");
        comboTailles = new JComboBox<>(new String[]{"naine", "humaine", "ogresse"});
        JLabel labelQuantite = new JLabel("Quantité :");
        fieldQuantite = new JTextField("1");
        JLabel labelPrixTexte = new JLabel("Prix unitaire :");
        labelPrix = new JLabel("0.00 €");
        JLabel labelPrixIngredientsTexte = new JLabel("Prix ingrédients supp. :");
        labelPrixIngredients = new JLabel("0.00 €");
        JLabel labelPrixTotalTexte = new JLabel("Prix total :");
        labelPrixTotal = new JLabel("0.00 €");
        JLabel labelFideliteTexte = new JLabel("Fidélité :");
        labelFidelite = new JLabel(Session.clientConnecte != null ?
            Session.clientConnecte.getNb_pizza_achete() + "/10 pizzas" : "0/10 pizzas");
        JLabel labelPizzaGratuite = new JLabel("Pizza gratuite :");
        checkboxPizzaGratuite = new JCheckBox("Utiliser ma pizza gratuite");

        boolean pizzaGratuiteDisponible = Session.clientConnecte != null && Session.clientConnecte.getNb_pizza_achete() >= 10;
        checkboxPizzaGratuite.setEnabled(pizzaGratuiteDisponible);
        checkboxPizzaGratuite.addActionListener(e -> mettreAJourPrixTotal());

        panelOptions.add(labelTaille); panelOptions.add(comboTailles);
        panelOptions.add(labelQuantite); panelOptions.add(fieldQuantite);
        panelOptions.add(labelPrixTexte); panelOptions.add(labelPrix);
        panelOptions.add(labelPrixIngredientsTexte); panelOptions.add(labelPrixIngredients);
        panelOptions.add(labelPrixTotalTexte); panelOptions.add(labelPrixTotal);
        panelOptions.add(labelFideliteTexte); panelOptions.add(labelFidelite);
        panelOptions.add(labelPizzaGratuite); panelOptions.add(checkboxPizzaGratuite);

        btnAjouterAuPanier = new JButton("Ajouter au panier");
        JPanel panelAjouterPanier = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelAjouterPanier.add(btnAjouterAuPanier);
        panelCentre.add(panelAjouterPanier, BorderLayout.SOUTH);
        
        JPanel panelPanier = new JPanel(new BorderLayout(5, 5));
        panelPanier.setBorder(BorderFactory.createTitledBorder("Panier"));

        modelePanier = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelePanier.addColumn("Pizza");
        modelePanier.addColumn("Taille");
        modelePanier.addColumn("Quantité");
        modelePanier.addColumn("Prix");

        tablePanier = new JTable(modelePanier);
        JScrollPane scrollPanier = new JScrollPane(tablePanier);
        scrollPanier.setPreferredSize(new Dimension(400, 150));
        panelPanier.add(scrollPanier, BorderLayout.CENTER);

       
        JPanel panelInfoPanier = new JPanel(new BorderLayout());
        JPanel panelSupprimerBouton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSupprimerDuPanier = new JButton("Supprimer sélection");
        btnSupprimerDuPanier.setEnabled(false);
        panelSupprimerBouton.add(btnSupprimerDuPanier);
        
        JPanel panelPrixTotalPanier = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel labelPrixTotalPanierTexte = new JLabel("Total panier : ");
        labelPrixTotalPanier = new JLabel("0.00 €");
        panelPrixTotalPanier.add(labelPrixTotalPanierTexte);
        panelPrixTotalPanier.add(labelPrixTotalPanier);
        
        panelInfoPanier.add(panelSupprimerBouton, BorderLayout.WEST);
        panelInfoPanier.add(panelPrixTotalPanier, BorderLayout.EAST);
        panelPanier.add(panelInfoPanier, BorderLayout.SOUTH);

        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCommander = new JButton("Commander le panier");
        btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnCommander);
        panelBoutons.add(btnAnnuler);

        panel.add(panelGauche, BorderLayout.WEST);
        panel.add(panelCentre, BorderLayout.CENTER);
        
        JPanel panelDroit = new JPanel(new BorderLayout(0, 10));
        panelDroit.add(panelOptions, BorderLayout.NORTH);
        panelDroit.add(panelPanier, BorderLayout.CENTER);
        panelDroit.add(panelBoutons, BorderLayout.SOUTH);
        
        panel.add(panelDroit, BorderLayout.EAST);

        add(panel);

        listePizzas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String nomPizza = listePizzas.getSelectedValue();
                if (nomPizza != null) {
                    resetIngredientsSupplementaires();
                    afficherDetailsPizza(nomPizza);
                }
            }
        });

        comboTailles.addActionListener(e -> mettreAJourPrixTotal());

        fieldQuantite.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                mettreAJourPrixTotal();
            }
        });

        btnAjouterAuPanier.addActionListener(e -> ajouterAuPanier());

        btnSupprimerDuPanier.addActionListener(e -> supprimerDuPanier());

        tablePanier.getSelectionModel().addListSelectionListener(e -> {
            btnSupprimerDuPanier.setEnabled(tablePanier.getSelectedRow() != -1);
        });

        btnCommander.addActionListener(e -> commanderPanier());

        btnAnnuler.addActionListener(e -> dispose());

        if (listePizzas.getModel().getSize() > 0) {
            listePizzas.setSelectedIndex(0);
        }
    }

    private void afficherDetailsPizza(String nomPizza) {
        pizzaSelectionnee = null;
        for (pizza p : Session.catalogue) {
            if (p.getNom().equals(nomPizza)) {
                pizzaSelectionnee = p;
                break;
            }
        }

        if (pizzaSelectionnee != null) {
            StringBuilder sb = new StringBuilder("Nom: ").append(pizzaSelectionnee.getNom()).append("\n\nIngrédients:\n");
            if (pizzaSelectionnee.getIngredients().isEmpty()) {
                sb.append("(Aucun ingrédient enregistré)");
            } else {
                for (ingredients ing : pizzaSelectionnee.getIngredients()) {
                    sb.append("- ").append(ing.getIngredient()).append("\n");
                }
            }
            zoneIngredients.setText(sb.toString());
            mettreAJourPrixTotal();
        } else {
            zoneIngredients.setText("Pizza non trouvée.");
            labelPrix.setText("0.00 €");
            labelPrixTotal.setText("0.00 €");
        }
    }

    private void resetIngredientsSupplementaires() {
        for (JCheckBox cb : checkboxesIngredients) cb.setSelected(false);
        labelPrixIngredients.setText("0.00 €");
    }

    private int getNombreIngredientsSelectionnes() {
        int count = 0;
        for (JCheckBox cb : checkboxesIngredients) {
            if (cb.isSelected()) count++;
        }
        return count;
    }

    private ArrayList<String> getIngredientsSelectionnes() {
        ArrayList<String> list = new ArrayList<>();
        for (JCheckBox cb : checkboxesIngredients) {
            if (cb.isSelected()) list.add(cb.getText());
        }
        return list;
    }

    private float calculerPrixPizzaAvecTaille() {
        if (pizzaSelectionnee == null) return 0;
        float base = pizzaSelectionnee.getPrix();
        String taille = (String) comboTailles.getSelectedItem();
        if (taille.equals("naine")) return base - base / 3f;
        if (taille.equals("ogresse")) return base + base / 3f;
        return base;
    }

    private void mettreAJourPrixTotal() {
        if (pizzaSelectionnee == null) {
            labelPrix.setText("0.00 €");
            labelPrixIngredients.setText("0.00 €");
            labelPrixTotal.setText("0.00 €");
            return;
        }

        float prixUnitaire = calculerPrixPizzaAvecTaille();
        labelPrix.setText(String.format("%.2f €", prixUnitaire));
        int nbIng = getNombreIngredientsSelectionnes();
        float prixIng = nbIng * prixIngredientSupp;
        labelPrixIngredients.setText(String.format("%.2f €", prixIng));
        float totalUnit = prixUnitaire + prixIng;

        int quantite = 1;
        try {
            quantite = Integer.parseInt(fieldQuantite.getText().trim());
            if (quantite <= 0) quantite = 1;
        } catch (NumberFormatException e) { }

        float total = checkboxPizzaGratuite.isSelected() ? 0 : totalUnit * quantite;
        labelPrixTotal.setText(checkboxPizzaGratuite.isSelected() ? "0.00 € (Pizza gratuite)" : String.format("%.2f €", total));
    }
    
    private void ajouterAuPanier() {
        if (pizzaSelectionnee == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une pizza.");
            return;
        }
        
        try {
            int quantite = Integer.parseInt(fieldQuantite.getText().trim());
            if (quantite <= 0) {
                JOptionPane.showMessageDialog(this, "La quantité doit être supérieure à 0.");
                return;
            }
            
            String taille = (String) comboTailles.getSelectedItem();
            float prixUnitaire = calculerPrixPizzaAvecTaille();
            int nbIng = getNombreIngredientsSelectionnes();
            prixUnitaire += nbIng * prixIngredientSupp;
            
            ArrayList<String> ingredients = getIngredientsSelectionnes();
            
            if (checkboxPizzaGratuite.isSelected()) {
                if (panier.isEmpty()) { 
                    prixUnitaire = 0;
                    checkboxPizzaGratuite.setSelected(false);
                    checkboxPizzaGratuite.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "La pizza gratuite doit être la première pizza du panier.",
                        "Pizza gratuite", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
    
            PanierPizza pizzaPanier = new PanierPizza(
                pizzaSelectionnee.getNom(), taille, quantite, prixUnitaire, ingredients
            );
            panier.add(pizzaPanier);
            
            Object[] row = {
                pizzaSelectionnee.getNom(),
                taille,
                quantite,
                String.format("%.2f €", pizzaPanier.getPrixTotal())
            };
            modelePanier.addRow(row);
            
            mettreAJourTotalPanier();
            
            resetIngredientsSupplementaires();
            fieldQuantite.setText("1");
            
            JOptionPane.showMessageDialog(this, 
                "Pizza ajoutée au panier!", "Panier", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une quantité valide.");
        }
    }
    
    private void supprimerDuPanier() {
        int selectedRow = tablePanier.getSelectedRow();
        if (selectedRow != -1) {
            panier.remove(selectedRow);
            modelePanier.removeRow(selectedRow);
            mettreAJourTotalPanier();
            
            if (panier.isEmpty() && Session.clientConnecte != null && Session.clientConnecte.getNb_pizza_achete() >= 10) {
                checkboxPizzaGratuite.setEnabled(true);
            }
        }
    }
    
    private void mettreAJourTotalPanier() {
        float total = 0;
        for (PanierPizza p : panier) {
            total += p.getPrixTotal();
        }
        labelPrixTotalPanier.setText(String.format("%.2f €", total));
    }
    
    private void commanderPanier() {
        if (panier.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide!");
            return;
        }
        
        float prixTotal = 0;
        boolean contientPizzaGratuite = false;
        
        for (PanierPizza p : panier) {
            if (p.getPrixUnitaire() == 0) {
                contientPizzaGratuite = true;
            } else {
                prixTotal += p.getPrixTotal();
            }
        }
        
        if (prixTotal > Session.clientConnecte.getSolde()) {
            JOptionPane.showMessageDialog(this, "Solde insuffisant !");
            return;
        }
        
        commande c = new commande(Session.commandesActives.size() + 1, new Time(System.currentTimeMillis()));
        
        for (PanierPizza p : panier) {
            pizza pizzaObj = null;
            for (pizza piz : Session.catalogue) {
                if (piz.getNom().equals(p.getNomPizza())) {
                    pizzaObj = piz;
                    break;
                }
            }
            
            if (pizzaObj != null) {
                c.ajouterLigneCommande(pizzaObj, p.getQuantite(), p.getTaille());
            }
        }
        
        if (contientPizzaGratuite) {
            Session.clientConnecte.nb_pizza_achete = Math.max(0, Session.clientConnecte.nb_pizza_achete - 10);
        }
        
        c.setPrix_total((int)prixTotal);
        c.commencerLivraison();
        
        if (c.isCommandeRemboursee()) {
            c.setPrix_total(0);
            prixTotal = 0;
        } else {
            if (!contientPizzaGratuite) {
                Session.clientConnecte.setSolde(Session.clientConnecte.getSolde() - prixTotal);
            }
            
            int totalPizzas = 0;
            for (PanierPizza p : panier) {
                if (p.getPrixUnitaire() > 0) { 
                    totalPizzas += p.getQuantite();
                }
            }
            Session.clientConnecte.nb_pizza_achete += totalPizzas;
        }
        
        Session.clientConnecte.ajouterCommandeHistorique(c);
        Session.commandesActives.add(c);
        
        if (fenetrePrincipale != null) {
            fenetrePrincipale.mettreAJourSolde(Session.clientConnecte.getSolde());
            fenetrePrincipale.mettreAJourFidelite(Session.clientConnecte.getNb_pizza_achete());
        }
        
        StringBuilder msg = new StringBuilder();
        if (c.isCommandeRemboursee()) {
            msg.append("Commande validée mais temps de livraison supérieur à 30 minutes.\n");
            msg.append("Commande remboursée ! Les pizzas seront gratuites.\n\n");
        } else {
            msg.append("Commande validée !\n\n");
        }
        
        msg.append("Détail de la commande :\n");
        for (int i = 0; i < panier.size(); i++) {
            PanierPizza p = panier.get(i);
            msg.append(p.getQuantite()).append(" x ").append(p.getNomPizza())
               .append(" (").append(p.getTaille()).append(")\n");
               
            if (!p.getIngredientsSupplementaires().isEmpty()) {
                msg.append("   Ingrédients supplémentaires : ");
                for (int j = 0; j < p.getIngredientsSupplementaires().size(); j++) {
                    msg.append(p.getIngredientsSupplementaires().get(j));
                    if (j < p.getIngredientsSupplementaires().size() - 1) {
                        msg.append(", ");
                    }
                }
                msg.append("\n");
            }
        }
        
        msg.append("\nPrix total : ").append(String.format("%.2f €", prixTotal));
        
        JOptionPane.showMessageDialog(this, msg.toString());
        
        if (Session.clientConnecte.nb_pizza_achete >= 10) {
            JOptionPane.showMessageDialog(this, "Félicitations ! Vous avez droit à une pizza gratuite !");
        }
        
        new SuivreCommandeFenetre(fenetrePrincipale).setVisible(true);
        dispose();
    }
}