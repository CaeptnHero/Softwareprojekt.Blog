package controller;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Ließt die Daten aus der DB von der Tabelle nutzer
 */
public final class AuthenticationController {

    private static final Object lock = new Object();

    /**
     * Die Daten des USers werden aus der DB geholt und mit den Eingabedaten verglichen. Sind diese Korrekt war der Login erfolgreich
     * und der User bekommt einen hinweis dazu.
     * War die Überprüfung Feherhaft, bekommt der User einen Hinweis und wird nicht eingeloggt.
     *
     * @param username
     * @param password
     * @return Ein Objekt vom Typ Blogger,Reader oder Null
     */
    public static User login(String username, String password) {
        username = DatabaseController.escapeString(username);

        User n = null;
        try {
            n = (User) DatabaseController.getUser(username);
            if (n == null || !n.getPassword().equals(password)) {
                showMessage("Login fehlgeschlagen");
                return null;
            }
            showMessage("Login Erfolgreich");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * Es wird in der DB geschaut, ob der User orhanden ist. Wenn nicht, werden die kriterien überprüft.
     * Sind diese erfüllt, wird der User registriert, in die DB eingetragen und bekommt einen Hinweis dazu.
     * Sind die Kriterien nicht erfüllt, bekommt der User einen Hinweis
     *
     * @param username
     * @param password
     * @return True oder False, abhängig davon, ob der User registiert werden konnte oder nicht
     */
    public static boolean register(String username, String password) {
        username = DatabaseController.escapeString(username);

        User n = (User) DatabaseController.getUser(username);
        if (n == null && username.length() >= 5 && password.length() >= 5) {
            String sql = "INSERT INTO user VALUES (NULL, '" + username + "', '" + password + "', 0)";
            int success = DatabaseController.executeUpdate(sql);
            if (success != -1) {
                showMessage("Benutzer wurde registriert");
                return true;
            }
        } else if (n != null) {
            showMessage("Benutzername ist vergeben");
        } else {
            showMessage("Benutzername und Passwort müssen mindestens 5 Zeichen bestehen");
        }
        return false;
    }

    /**
     * Zeigt eine Messagebox mit einer Nachricht an
     *
     * @param msg Anzuzeigende Nachricht
     */
    private static void showMessage(String msg) {
        JFrame mbxFrame = new JFrame();
        mbxFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mbxFrame.setSize(msg.length() * 8, 80);
        mbxFrame.getContentPane().setBackground(Color.decode("#3b4252"));
        mbxFrame.setLayout(new BorderLayout());
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        mbxFrame.setLocation((screen.width - mbxFrame.getWidth()) / 2, (screen.height - mbxFrame.getHeight()) / 2);
        mbxFrame.setResizable(false);

        JLabel label = new JLabel(msg, SwingConstants.CENTER);
        label.setForeground(Color.decode("#d8dee9"));
        mbxFrame.add(label, BorderLayout.CENTER);

        JButton button = new JButton("OK");
        button.setBorderPainted(false);
        button.setBackground(Color.decode("#434c5e"));
        button.setForeground(Color.decode("#d8dee9"));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.decode("#4c566a"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.decode("#434c5e"));
            }
        });
        button.addActionListener(e -> mbxFrame.dispatchEvent(new WindowEvent(mbxFrame, WindowEvent.WINDOW_CLOSING)));
        mbxFrame.add(button, BorderLayout.SOUTH);

        mbxFrame.setUndecorated(true);
        mbxFrame.setVisible(true);
        mbxFrame.setAlwaysOnTop(true);

        Thread t = new Thread(() -> {
            synchronized (lock) {
                while (mbxFrame.isVisible()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {

                    }
                }
            }
        });
        t.start();

        mbxFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                synchronized (lock) {
                    mbxFrame.setVisible(false);
                    t.interrupt();
                }
            }
        });

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
