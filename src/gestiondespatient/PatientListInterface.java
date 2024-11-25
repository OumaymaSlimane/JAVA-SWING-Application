package gestiondespatient;



import javax.swing.*;


import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import java.util.List;


import metier.entity.Patient;
import dao.IListePatientImpl;



public class PatientListInterface extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private Dossier dossierDialog;

    public PatientListInterface() {
        setTitle("Gestion des Patients");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //dossierDialog = new Dossier(this, numCIN);


        // panelfixe à gauche
        SidePanel sidebar = new SidePanel();
        add(sidebar, BorderLayout.WEST);

        // Ajout du titre en haut à gauche
        JLabel titleLabel = new JLabel("Gestion des Patients");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Panel principal pour le contenu central
        JPanel centralPanel = new JPanel(new BorderLayout());

        // Configuration du modèle de la table
        String[] columnNames = {"NumeroPatient","Nom", "Prenom", "NumeroCIN", "NumeroTel", "Adresse", "Email", "Age", "Sexe", "Action" ,"Action2"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
          
        	@Override
            public Class<?> getColumnClass(int column) {
                return (column == 9 || column == 10) ? JButton.class : String.class;
            }
        };

       

        // Ajouter un rendu et un éditeur pour la colonne "Supprimer"
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
        table.getColumn("Action2").setCellRenderer(new DossierButtonRenderer());
        table.getColumn("Action2").setCellEditor(new DossierButtonEditor(new JCheckBox()));
    
        // Ajout du bouton "Ajouter"
        JButton ajouterBtn = new JButton("Ajouter Patient");
        ajouterBtn.addActionListener(e -> afficherDialogueAjout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ajouterBtn);
        // Panel pour contenir le bouton "Ajouter"
        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Alignement à droite
        // Ajout du bouton "Ajouter" au panneau
        addButtonPanel.add(ajouterBtn);
        // Ajout du panneau contenant le bouton "Ajouter" au-dessus de la table
        centralPanel.add(addButtonPanel, BorderLayout.NORTH); // Ajout au-dessus de la table
        // Ajout de la table à centralPanel
        centralPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        // Ajout de centralPanel au JFrame
        add(centralPanel, BorderLayout.CENTER);

        // Barre de recherche à côté du tableau
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30)); // Définir une taille plus grande pour le champ de recherche
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        JPanel searchContainerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchContainerPanel.add(searchPanel);
        centralPanel.add(searchContainerPanel, BorderLayout.SOUTH);

        // Ajout d'un écouteur pour le champ de recherche
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherPatient(searchField.getText());
                try {
                    filtrerTable(searchField.getText());

                    IListePatientImpl dao = new IListePatientImpl();
                    // Effectuez la recherche avec les termes fournis dans searchField
                    List patients = (List) dao.rechercherPatient(searchField.getText(), searchField.getText());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de la recherche des patients.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
     
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Ne rien faire ici
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrerSiNecessaire(searchField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Ne rien faire ici
            }
        });


        chargerPatientsDansTable();
        
        
     
     
     // Ici, vous ajoutez votre écouteur de modèle de table
        table.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                TableModel model = (TableModel)e.getSource();
                String numPatient = model.getValueAt(row, 0).toString();
                Object newValue = model.getValueAt(row, column);

                // Vérifiez si la colonne modifiée n'est pas celle des boutons
                if (column < model.getColumnCount() - 2) {
                    mettreAJourColonneDansBaseDeDonnees(numPatient, column, newValue);
                }
            }
        });
        // Configuration de l'éditeur de cellules pour valider les entrées utilisateur
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 1; i < columnModel.getColumnCount() - 2; i++) {
            columnModel.getColumn(i).setCellEditor(new DefaultCellEditor(new JTextField()) {
                @Override
                public boolean stopCellEditing() {
                    return super.stopCellEditing();
                }
            });
        }

        
     
        
     
        

        setVisible(true);
    }
 
	// Méthode pour restaurer l'état initial de l'interface 
    private void filtrerSiNecessaire(JTextField searchField) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            // Si le champ de recherche est vide, restaurer l'état initial de la table
            filtrerTable("");
        }
    }
    public void rechercherPatient(String query) {
        boolean patientTrouve = false;
       
       
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String nom = (String) tableModel.getValueAt(i, 1);
                String prenom = (String) tableModel.getValueAt(i, 2);
                if (nom.toLowerCase().contains(query.toLowerCase()) || prenom.toLowerCase().contains(query.toLowerCase())) {
                 
                    // Convertir l'index de ligne du modèle en l'index de ligne de la vue
                    int rowIndex = table.convertRowIndexToView(i);
                    if (rowIndex != -1) { // Vérifier si l'index de ligne est valide
                        table.setRowSelectionInterval(rowIndex, rowIndex);
                        patientTrouve = true;
                        break;
                    }
                }
            }
            if (!patientTrouve) {
                JOptionPane.showMessageDialog(this, "Patient introuvable. Veuillez l'ajouter.", "Patient introuvable", JOptionPane.INFORMATION_MESSAGE);
            }
        }


    
     
    
    public void filtrerTable(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (query.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 1, 2)); // Recherche insensible à la casse dans les colonnes 1 (nom) et 2 (prénom)
        }}
    
    
    
 
    

 // Rendu du bouton "Supprimer"
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            this.setText("Supprimer"); // Définir le texte du bouton "Supprimer"
            return this; // Retourner le bouton lui-même
        }
    }
    
 // Éditeur du bouton
    
    
 // Méthode pour supprimer un patient
    class ButtonEditor extends DefaultCellEditor {
         JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
         // Ajout d'un ActionListener au bouton de suppression dans votre JTable
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Obtenez l'index de la ligne à partir de laquelle le bouton a été déclenché
                    int row = table.convertRowIndexToModel(table.getEditingRow());
                    // Assurez-vous que nous avons une ligne valide
                    if (row != -1) {
                        // Récupérez le numéro du patient de la ligne sélectionnée
                        String numeroPatient = tableModel.getValueAt(row, 0).toString(); // Supposons que la colonne 0 contient le numéro du patient
                        try {
                            // Créez une instance de l'implémentation DAO
                            IListePatientImpl dao = new IListePatientImpl();
                            // Supprimez le patient via DAO
                            dao.supprimerPatient(numeroPatient);
                            // Supprimez la ligne de la table model après la suppression réussie de la base de données
                            tableModel.removeRow(row);
                            JOptionPane.showMessageDialog(null, "Patient supprimé avec succès.", "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du patient.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

        }
        

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
        	 this.button.setText("Supprimer"); // Utilisez "this" pour définir le texte du bouton
             return this.button;
        }
        
        @Override
        public boolean stopCellEditing() {
            // Perform deletion of patient here
            return super.stopCellEditing();
        }

      
        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

 

    // Rendu du bouton "Dossier"
    class DossierButtonRenderer extends JButton implements TableCellRenderer {
        public DossierButtonRenderer() {
            setOpaque(true); // Nécessaire pour la couleur de fond
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
        	this.setText("Dossier"); // Définir le texte du bouton "Supprimer"
            return this; // Retourner le bouton lui-même

        }
    }

    class DossierButtonEditor extends DefaultCellEditor {
         JButton button;
        
        public DossierButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Dossier");
            button.setOpaque(true);
            button.addActionListener(e -> {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                if (row >= 0) {
                    String nom = (String) tableModel.getValueAt(row, 1);
                    String prenom = (String) tableModel.getValueAt(row, 2);
                    // Récupérer l'ID du patient
                    String patientId = (String) tableModel.getValueAt(row, 0);
                    Dossier dossierDialog = new Dossier(PatientListInterface.this, patientId); // Créer un nouveau dialogue Dossier avec l'ID du patient

                    // Afficher le dialogue du dossier médical
                    dossierDialog.setTitle("Dossier de " + nom + " " + prenom);
                    // Récupérer les informations du dossier du patient et les définir dans la boîte de dialogue
               
                    dossierDialog.setVisible(true);
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                      boolean isSelected, int row, int column) {
            return button;
        }

        
    }






    
    // Méthode pour afficher le dialogue d'ajout d'un nouveau patient
    public void afficherDialogueAjout() {
    	
        JTextField nomField = new JTextField(20);
        JTextField prenomField = new JTextField(20);
        JTextField numField = new JTextField(20);
        JTextField telField = new JTextField(20);
        JTextField adresseField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JComboBox<String> sexeBox = new JComboBox<>(new String[]{"Homme", "Femme"});

        JPanel panel = new JPanel(new GridLayout(0, 1));
       
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom:"));
        panel.add(prenomField);
        panel.add(new JLabel("NumeroCIN (8 chiffres):"));
        panel.add(numField);
        panel.add(new JLabel("Téléphone:"));
        panel.add(telField);
        panel.add(new JLabel("Adresse:"));
        panel.add(adresseField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Âge:"));
        panel.add(ageField);
        panel.add(new JLabel("Sexe:"));
        panel.add(sexeBox);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Ajouter un nouveau patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
        	String numCIN = numField.getText();
        	if (!isNumeric(numCIN)) {
                JOptionPane.showMessageDialog(null, "Le numéro de Cin doit être un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (numCIN.length() != 8){
                JOptionPane.showMessageDialog(null, "Le numéro de Cin doit comporter 8 chiffres.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (isNumCinExistant(numCIN)) {
                JOptionPane.showMessageDialog(null, "Ce numéro de Cin est déjà attribué à un autre patient.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String telephone = telField.getText().trim();
            if (!telephone.matches("\\d{8}")) { // Vérifier si le numéro de téléphone a 8 chiffres
                JOptionPane.showMessageDialog(this, "Le numéro de téléphone doit contenir 8 chiffres.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Arrêter l'ajout du patient si le numéro de téléphone est invalide
            }
            
            String email = emailField.getText().trim();
            if (!email.matches("\\b[A-Za-z0-9._%+-]+@gmail\\.com\\b")) { // Vérifier si l'adresse e-mail est valide
                JOptionPane.showMessageDialog(this, "Veuillez saisir une adresse e-mail valide (xxxx@gmail.com).", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Arrêter l'ajout du patient si l'adresse e-mail est invalide
            }
        	
        	
        	
        	
        	try {
                String nom = nomField.getText().trim();
                String prenom = prenomField.getText().trim();
                numCIN = numField.getText().trim();
                telephone = telField.getText().trim();
                String adresse = adresseField.getText().trim();
                email = emailField.getText().trim();
                String age = ageField.getText().trim();
                String sexe = (String) sexeBox.getSelectedItem();

                Patient P = new Patient(null, nom, prenom,numCIN,telephone,adresse,email,age,sexe);
                IListePatientImpl dao = new IListePatientImpl();
                
                
                int generatedId = dao.ajouterPatient(P);
                
             

                // Ajouter la ligne à la JTable visuellement
                Vector<String> row = new Vector<>();
                row.add(String.valueOf(generatedId));
                row.add(nom);
                row.add(prenom);
                row.add(numCIN);
                row.add(telephone);
                row.add(adresse);
                row.add(email);
                row.add(age);
                row.add(sexe);
                tableModel.addRow(row);
                

                JOptionPane.showMessageDialog(null, "Patient ajouté avec succès.", "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du patient dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
 // Méthode pour vérifier si une chaîne est numérique
    public boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
 // Méthode pour vérifier si un numéro de Cin est déjà existant
    public boolean isNumCinExistant(String numCIN) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 3).equals(numCIN)) {
                return true;
            }
        }
        return false;
    }
 // Méthode pour mettre à jour la colonne modifiée dans la base de données
    public void mettreAJourColonneDansBaseDeDonnees(String numPatient, int column, Object newValue) {
        String nomColonne = table.getColumnName(column);

        try {
            IListePatientImpl dao = new IListePatientImpl();
            dao.mettreAJourPatient(numPatient, nomColonne, newValue.toString());
            JOptionPane.showMessageDialog(null, "Mise à jour réussie.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour dans la base de données: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            // Ici, vous devez remettre la valeur originale dans la table pour rester cohérent avec la base de données.
        }
    }

    
    
		
	
	public void chargerPatientsDansTable() {
        try {
            IListePatientImpl dao = new IListePatientImpl();
            List<Patient> listePatients = dao.obtenirTousPatients();
            
            // Assurez-vous de vider le modèle de la table avant de le remplir
            tableModel.setRowCount(0);

            for (Patient patient : listePatients) {
                Vector<Object> row = new Vector<>();
                row.add(patient.getNumeroPatient());
                row.add(patient.getNom());
                row.add(patient.getPrenom());
                row.add(patient.getNumCIN());
                row.add(patient.getTelephone());
                row.add(patient.getAdresse());
                row.add(patient.getEmail());
                row.add(patient.getAge());
                
                row.add(patient.getSexe());
                tableModel.addRow(row);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des patients de la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    


    public static void main(String[] args) {
        SwingUtilities.invokeLater(PatientListInterface::new);
    }
}