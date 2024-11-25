package gestiondespatient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.IDossierDAO;
import dao.IDossierImpl;
import metier.entity.dossier;

public class Dossier extends JDialog {
    private String patientId; // L'identifiant unique du patient pour ce dossier
    private DefaultTableModel tableModel; // Déclarer tableModel comme un champ de classe


    public Dossier(PatientListInterface patient, String patientId) {
        super(patient, "Dossier du patient", true);
        this.patientId = patientId;
        setSize(800, 600);
        setLayout(new BorderLayout());

        String[] columnNames = {"iddossier","Date_dentree", "Description", "Maladies", "Traitement"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Toutes les cellules sont éditables
            }
        };

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        
        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> {
            // Demander à l'utilisateur de saisir les informations du dossier
            String dateEntree = JOptionPane.showInputDialog(this, "Date d'entrée:");
            String description = JOptionPane.showInputDialog(this, "Description:");
            String maladies = JOptionPane.showInputDialog(this, "Maladies:");
            String traitement = JOptionPane.showInputDialog(this, "Traitement:");

            // Vérifier si toutes les informations ont été saisies
            if (dateEntree != null && description != null && maladies != null && traitement != null) {
                // Ajouter les données dans le tableau
                Object[] dossierData = {dateEntree, description, maladies, traitement};

                // Appeler la méthode insererDossier pour insérer le dossier dans la base de données
                insererDossier(table, dossierData, patientId);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez saisir toutes les informations.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        chargerDonneesExistantes();


        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Récupérer l'ID du dossier à supprimer
                String idDossier = tableModel.getValueAt(selectedRow, 0).toString();
                
                // Supprimer le dossier de la base de données
                try {
                    IDossierDAO dossierDAO = new IDossierImpl();
                    System.out.println("ID du dossier à supprimer : " + idDossier);

                    dossierDAO.supprimerDossier(idDossier);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du dossier dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                
                // Supprimer la ligne du tableau
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.", "Aucune ligne sélectionnée", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        add(buttonsPanel, BorderLayout.NORTH);

        
        // Add more dummy data if needed

    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Créer un frame parent fictif pour le dialogue
            JFrame parentFrame = new JFrame("Application Principale");
            parentFrame.setSize(800, 600);
            parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            parentFrame.setVisible(true);

            // L'ID de patient fictif pour cette démonstration
            String patientId = "123456789";

            // Créer une instance de PatientListInterface
            PatientListInterface patientListInterface = new PatientListInterface();

            // Passer cette instance au constructeur de Dossier
            Dossier dialog = new Dossier(patientListInterface, patientId);

            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    parentFrame.dispose(); // Fermer le frame parent quand le dialogue est fermé
                }
            });
            dialog.setVisible(true);
        });
    }
    
    private void chargerDonneesExistantes() {
        try {
            System.out.println("Chargement des dossiers pour le patient avec l'ID : " + patientId);
            IDossierImpl dossierDAO = new IDossierImpl();
            List<dossier> dossiers = dossierDAO.getDossiersByPatientId(patientId);
            System.out.println("Nombre de dossiers chargés : " + dossiers.size());
            tableModel.setRowCount(0);
            for (dossier d : dossiers) {
                System.out.println("Dossier chargé : " + d.getIdDossier());
                tableModel.addRow(new Object[] {
                    d.getIdDossier(),
                    d.getDateEntree(),
                    d.getDescription(),
                    d.getMaladies(),
                    d.getTraitement()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des dossiers depuis la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void insererDossier(JTable table, Object[] dossierData, String patientId) {
        // Obtenir le modèle de tableau
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        
        // Insérer le dossier en utilisant les données récupérées
        try {
            IDossierDAO dossierDAO = new IDossierImpl();
            int idDossier = dossierDAO.insererDossier(new dossier(null, (String)dossierData[0], (String)dossierData[1], (String)dossierData[2], (String)dossierData[3]), patientId);
            
            // Mettre à jour la première colonne du tableau avec l'ID récupéré
            Object[] rowData = {idDossier, dossierData[0], dossierData[1], dossierData[2], dossierData[3]};
            tableModel.addRow(rowData); // Ajouter la ligne après avoir inséré le dossier dans la base de données
        } catch (SQLException ex) {
            // Gérer l'exception SQL
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'insertion du dossier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


   
    }