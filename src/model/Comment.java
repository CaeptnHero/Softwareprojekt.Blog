package model;

import controller.DatabaseController;

import java.time.LocalDateTime;

public class Comment extends Post {

    /**
     * Konstruktor fuers user interface
     *
     * @param author verfasser des Kommentars
     * @param text text des Kommentars
     * @param parent Oberbeitrag des Kommentars
     */
    public Comment(User author, String text, Post parent) {
        super(author, text, parent);

        String sql = String.format("INSERT INTO comment VALUES (%s, '%s')", this.getId(), text);
        DatabaseController.executeUpdate(sql);
    }

    /**
     * Konstruktor fuer die Datenbank
     *
     * @param id Identifikator des Kommentars
     * @param author verfasser des Kommentars
     * @param text text des Kommentars
     * @param dateTime zeitpunkt des erstellens
     * @param parent Oberbeitrag des Kommentars
     */
    public Comment(int id, User author, String text, LocalDateTime dateTime, Post parent) {
        super(id, author, text, dateTime, parent);
    }

    /**
     * Loescht sich selbst
     */
    public void delete() {
        super.delete();    //eigene kommentare löschen
        this.getParent().deleteComment(this);    //sich selbst löschen
        deleteFromDatabase(); // sich selnst aus db löschen
    }

    /**
     * Loescht sich selbst aus der Datenbank
     */
    public void deleteFromDatabase() {
        String sql = "DELETE FROM comment WHERE CID = " + this.getId();
        DatabaseController.executeUpdate(sql);
        sql = "DELETE FROM post WHERE PID = " + this.getId();
        DatabaseController.executeUpdate(sql);
    }

}
