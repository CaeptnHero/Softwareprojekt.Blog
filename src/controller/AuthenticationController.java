package controller;

import model.User;

import javax.swing.*;

/**
 * Ließt die Daten aus der DB von der Tabelle nutzer
 *
 * @author
 */
public class AuthenticationController {

    /**
     * Es wird geschaut, ob der angegeben User in der DB ist und das Passwort übereinstimmt
     *
     * @param username
     * @param password
     * @return Ein Objekt vom Typ Blogger,Reader oder Null
     * @author Andre Martens
     */
    public User login(String username, String password) {
        username = DatabaseController.escapeString(username);

        User n = null;
        try {
            n = (User) DatabaseController.getUser(username);
            if (n == null || !n.getPassword().equals(password)) {
                System.out.println("Login fehlgeschlagen");
                JOptionPane.showMessageDialog(null, "Login fehlgeschlagen");
                return null;
            }
            System.out.println(n);
            System.out.println("Login erfolgreich");
            JOptionPane.showMessageDialog(null, "Login Erfolgreich");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * Es wird bei der Registrierung geschaut, ob ein Nutzer mit dem angegeben Nutzer bereits in der DB existiert, wenn nicht, dann wird dieser hinzugefügt
     *
     * @param username
     * @param password
     * @return True oder False abhängig davon, ob der User registiert werden konnte oder nicht
     * @author Andre Martens
     */
    public boolean register(String username, String password) {
        username = DatabaseController.escapeString(username);

        User n = (User) DatabaseController.getUser(username);
        if (n == null && username.length() >= 5 && password.length() >= 5) {
            String sql = "INSERT INTO nutzer (nid, nutzername, passwort, istBlogger) VALUES (NULL, '" + username + "', '" + password + "', 0)"; //TODO: niemand der sich registriert ist momentan ein blogger
            int erfolgreich = DatabaseController.executeUpdate(sql);
            if (erfolgreich != -1) {
                JOptionPane.showMessageDialog(null, "Benutzer wurde registirert");
                return true;
            }
        } else if (n != null) {
            JOptionPane.showMessageDialog(null, "Benutzername ist vergeben");
        } else {
            System.out.println("Fehlerhafte Registrierung");
            JOptionPane.showMessageDialog(null, "Benutzername und Passwort müssen mindestens 5 Zeichen bestehen");
        }
        return false;
    }
}
