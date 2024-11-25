package gestiondespatient;



import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Loadingscreen extends JFrame {

    private JProgressBar progressBar;
    private JLabel backgroundLabel;
    private ImageIcon backgroundImageIcon;

    public Loadingscreen() {
        initUI();
        simulateLoading();
    }

    private void initUI() {
        setTitle("Application de Démarrage");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Création d'un JPanel comme conteneur principal pour le contenu
        JPanel contentPanel = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 600);
            }
        };

        backgroundImageIcon = new ImageIcon("C:\\Users\\ctiza\\Desktop\\19373.jpg");
        backgroundLabel = new JLabel(backgroundImageIcon);
        backgroundLabel.setLayout(new BorderLayout());
        contentPanel.add(backgroundLabel);

        ImageIcon personImageIcon = new ImageIcon("C:\\Users\\ctiza\\Desktop\\PngItem_959201.png");
        JLabel personLabel = new JLabel(personImageIcon);
        personLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundLabel.add(personLabel, BorderLayout.CENTER);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(300, 30));
        backgroundLabel.add(progressBar, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBackgroundImage();
            }
        });

        // Ajout du contentPanel à un JScrollPane
        JScrollPane scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setContentPane(scrollPane);
    }

    private void resizeBackgroundImage() {
        Image background = backgroundImageIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        backgroundImageIcon.setImage(background);
        backgroundLabel.repaint();
    }

    private void simulateLoading() {
        Thread thread = new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                final int progress = i;
                SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            showLoginScreen();
        });
        thread.start();
    }

    private void showLoginScreen() {
        int loadingScreenWidth = getWidth();
        int loadingScreenHeight = getHeight();

        LoginScreen loginScreen = new LoginScreen();
        if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            loginScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            loginScreen.setSize(loadingScreenWidth, loadingScreenHeight);
        }
        loginScreen.setVisible(true);
        setVisible(false);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Loadingscreen().setVisible(true));
    }
}