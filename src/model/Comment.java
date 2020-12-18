package model;

import controller.DatabaseController;

import java.time.LocalDateTime;

public class Comment extends Post {
    /**
     * TODO: FINISH JAVADOC COMMENT
     * Konstruktor fuers user interface
     * @param author
     * @param text
     * @param parent
     */
    public Comment(User author, String text, Post parent) {
        super(author, text, parent);

        String sql = String.format("INSERT INTO comment VALUES (%s, '%s')", this.getId(), text);
        DatabaseController.executeUpdate(sql);
    }

    /**
     * Konstruktor fuer die Datenbank
     * @param id
     * @param author
     * @param text
     * @param dateTime
     * @param parent
     */
    public Comment(int id, User author, String text, LocalDateTime dateTime, Post parent) {
        super(id, author, text, dateTime, parent);
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     */
    public void delete() {
        super.delete();    //eigene kommentare löschen
        this.getParent().deleteComment(this);    //sich selbst löschen
        deleteFromDatabase(); // sich selnst aus db löschen
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     */
    public void deleteFromDatabase() {
        String sql = "DELETE FROM comment WHERE CID = " + this.getId();
        DatabaseController.executeUpdate(sql);
        sql = "DELETE FROM post WHERE PID = " + this.getId();
        DatabaseController.executeUpdate(sql);
    }
}
