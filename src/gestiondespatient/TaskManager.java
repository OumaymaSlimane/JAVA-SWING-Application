package gestiondespatient;

import java.util.List;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import dao.IListePatientImpl;
import dao.ITaskManagerImpl;
import metier.entity.Task;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Vector;
import java.util.prefs.*;

public class TaskManager extends JFrame {
    private DefaultTableModel tableModel;
    private JTable taskTable;
    private Preferences preferences;
    private ITaskManagerImpl taskManagerDao; // Déclaration du DAO ici


    private JTextField taskField;
    private JTextField startDateField;
    private JTextField durationField;

    public TaskManager() {
        setTitle("Gestionnaire de tâches");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        
     // panelfixe à gauche
        SidePanel sidebar = new SidePanel();
        add(sidebar, BorderLayout.WEST);


        taskField = new JTextField(20);
        startDateField = new JTextField(10);
        durationField = new JTextField(5);


        // Panel pour le champ de saisie et le bouton d'ajout
        JPanel inputPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        inputPanel.add(new JLabel("Tâche :"));
        inputPanel.add(taskField);
        inputPanel.add(new JLabel("Date de début (AAAA-MM-JJ) :"));
        inputPanel.add(startDateField);
        inputPanel.add(new JLabel("Durée (jours) :"));
        inputPanel.add(durationField);
        inputPanel.add(addButton);

        // Modèle de tableau pour les tâches
        tableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 || columnIndex == 5 ? JComboBox.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 ; // Rendre toutes les colonnes éditables sauf la première (Numéro)
            }
        };
        tableModel.addColumn("id");
        tableModel.addColumn("description");
        tableModel.addColumn("priorite");
        tableModel.addColumn("date_debut");
        tableModel.addColumn("echeance");
        tableModel.addColumn("etat");
        tableModel.addColumn("Supprimer"); // Ajout de la colonne Supprimer
        taskTable = new JTable(tableModel);
        setupComboBoxes();
        
        tableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    // Assurez-vous que la colonne modifiée n'est pas la colonne du bouton Supprimer
                    if (column < tableModel.getColumnCount() - 1) {
                        updateTaskInDatabase(row, column);
                    }
                }
            }
        });


        // Personnaliser la colonne Supprimer
        TableColumnModel columnModel = taskTable.getColumnModel();
        columnModel.getColumn(6).setCellRenderer(new DeleteButtonRenderer());
        columnModel.getColumn(6).setCellEditor(new DeleteButtonEditor(new JCheckBox(), taskTable)); // Modification ici
        columnModel.getColumn(6).setPreferredWidth(80);

       
        // Panel pour la liste des tâches
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Liste des tâches"));
        tablePanel.add(new JScrollPane(taskTable), BorderLayout.CENTER);

        // Panel global
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);
        try {
            this.taskManagerDao = new ITaskManagerImpl(); // Initialisation du DAO
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }
        chargerTachesDansTable();
        setVisible(true);
        
        
    }

    private void addTask() {
        String taskDescription = taskField.getText().trim();
        String startDateText = startDateField.getText().trim();
        String durationText = durationField.getText().trim();

        if (!taskDescription.isEmpty() && !startDateText.isEmpty() && !durationText.isEmpty()) {
            // Initialisation de DAO ici pour s'assurer qu'il est prêt à chaque ajout
            try {
                this.taskManagerDao = new ITaskManagerImpl();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                return;  
            }

            try {
                LocalDate startDate = LocalDate.parse(startDateText);
                int duration = Integer.parseInt(durationText);
                LocalDate deadline = startDate.plusDays(duration);

                String priority = JOptionPane.showInputDialog(this, "Sélectionnez la priorité de la tâche :", "Priorité", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Peu important", "Important"}, "Peu important").toString();
                String state = JOptionPane.showInputDialog(this, "Sélectionnez l'état de la tâche :", "État", JOptionPane.QUESTION_MESSAGE, null, new String[]{"À faire", "En cours", "Terminé"}, "À faire").toString();

                Task newTask = new Task(0, taskDescription, priority, startDate, deadline, state);
                int generatedId = taskManagerDao.insererTask(newTask);  // Insérer la tâche dans la base de données

                String[] rowData = {
                        String.valueOf(generatedId),
                        taskDescription,
                        priority,
                        startDate.toString(),
                        deadline.toString(),
                        state,
                        "Supprimer"
                };
                tableModel.addRow(rowData);
                taskField.setText("");
                startDateField.setText("");
                durationField.setText("");

                JOptionPane.showMessageDialog(this, "Tâche ajoutée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Format de date ou durée invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la tâche dans la base de données.", "Erreur Base de Données", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void setupComboBoxes() {
        TableColumnModel columnModel = taskTable.getColumnModel();
        TableColumn priorityColumn = columnModel.getColumn(2);
        TableColumn stateColumn = columnModel.getColumn(5);

        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"Peu important", "Important"});
        priorityColumn.setCellEditor(new DefaultCellEditor(priorityComboBox));

        JComboBox<String> stateComboBox = new JComboBox<>(new String[]{"À faire", "En cours", "Terminé"});
        stateColumn.setCellEditor(new DefaultCellEditor(stateComboBox));
    }

   
   

 

    class DeleteButtonEditor extends DefaultCellEditor {
        private JButton button;
        private JTable taskTable;
        private ITaskManagerImpl dao;  // DAO pour gérer les tâches

        public DeleteButtonEditor(JCheckBox checkBox, JTable taskTable) {
            super(checkBox);
            this.taskTable = taskTable;
            this.dao = createDao();
            setupButton();
        }

        private ITaskManagerImpl createDao() {
            try {
                return new ITaskManagerImpl();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données.", "Erreur Base de Données", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return null;
            }
        }

        private void setupButton() {
            button = new JButton("Supprimer");
            button.setOpaque(true);
            button.addActionListener(e -> performDeleteAction());
        }

        private void performDeleteAction() {
            int modelRow = taskTable.convertRowIndexToModel(taskTable.getEditingRow());
            if (modelRow != -1) { // S'assure que la ligne est valide
                DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
                Object taskIdObj = model.getValueAt(modelRow, 0);  // Supposons que l'ID est dans la première colonne

                try {
                    Integer taskId = Integer.valueOf(taskIdObj.toString()); // Convertir l'ID en Integer
                    dao.supprimerTask(taskId);
                    model.removeRow(modelRow);
                    JOptionPane.showMessageDialog(taskTable, "Tâche supprimée avec succès.", "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(taskTable, "Erreur lors de la suppression de la tâche: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(taskTable, "Erreur technique : Impossible de récupérer la ligne en cours d'édition.", "Erreur de suppression", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return null;  // La valeur retournée n'est pas utilisée pour un bouton.
        }
    }



class DeleteButtonRenderer extends JButton implements TableCellRenderer {
    public DeleteButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setText("Supprimer"); // Définir le texte du bouton "Supprimer"
        return this;
    }
}
private void updateTaskInDatabase(int row, int column) {
    int taskId = Integer.parseInt(taskTable.getValueAt(row, 0).toString());
    String columnName = taskTable.getColumnName(column);
    Object cellValue = taskTable.getValueAt(row, column);
    String newValue;

    try {
        // Adapter les noms des colonnes et les formats de données selon les types
        switch (columnName) {
            case "description":
                columnName = "description"; // Assurez-vous que c'est le nom correct dans la base de données
                newValue = cellValue.toString();
                break;
            case "priorite":
                columnName = "priorite"; // Assurez-vous que c'est le nom correct dans la base de données
                newValue = cellValue.toString();
                break;
            case "echeance":
                columnName = "echeance"; // Assurez-vous que c'est le nom correct dans la base de données
                LocalDate dateEcheance = LocalDate.parse(cellValue.toString());
                newValue = dateEcheance.format(DateTimeFormatter.ISO_LOCAL_DATE);
                break;
            case "date_debut":
                columnName = "date_debut"; // Assurez-vous que c'est le nom correct dans la base de données
                LocalDate dateDebut = LocalDate.parse(cellValue.toString());
                newValue = dateDebut.format(DateTimeFormatter.ISO_LOCAL_DATE);
                break;
            case "etat":
                columnName = "etat"; // Assurez-vous que c'est le nom correct dans la base de données
                newValue = cellValue.toString();
                break;
            default:
                throw new IllegalArgumentException("Nom de colonne non géré: " + columnName);
        }

        // Appel de la méthode DAO pour mettre à jour la base de données
        taskManagerDao.updateTask(taskId, columnName, newValue);
        JOptionPane.showMessageDialog(this, "Mise à jour réussie.", "Mise à jour", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour de la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Erreur lors de la conversion de l'ID de tâche.", "Erreur", JOptionPane.ERROR_MESSAGE);
    } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(this, "Format de date invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
public void chargerTachesDansTable() {
   

    try {
    	
        ITaskManagerImpl dao = new ITaskManagerImpl();

        List<Task> listeTaches = dao.getAllTasks();
        tableModel.setRowCount(0);

        for (Task tache : listeTaches) {
            Vector<Object> row = new Vector<>();
            row.add(tache.getId());
            row.add(tache.getDescription());
            row.add(tache.getPriorite());
            row.add(tache.getDateDebut().toString());
            row.add(tache.getEcheance().toString());
            row.add(tache.getEtat());
            row.add("Supprimer");
            tableModel.addRow(row);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur lors du chargement des tâches de la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TaskManager taskManager = new TaskManager();
                taskManager.setVisible(true);
            }
        });
    }
    

}