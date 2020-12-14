package model;

import controller.DatabaseController;

import java.time.LocalDateTime;

public class Comment extends Post {
    // ctor for UI
    public Comment(User author, String text, Post parent) {
        super(author, text, parent);

        String sql = String.format("INSERT INTO Kommentar VALUES (%s, '%s')", this.getId(), text);
        DatabaseController.executeUpdate(sql);
    }

    // ctor for db
    public Comment(int id, User author, String text, LocalDateTime dateTime, Post parent) {
        super(id, author, text, dateTime, parent);
    }

    public void delete() {
        super.delete();    //eigene kommentare löschen
        this.getParent().deleteComment(this);    //sich selbst löschen
        deleteFromDatabase(); // sich selnst aus db löschen
    }

    public void deleteFromDatabase() {
        String sql = "DELETE FROM kommentar WHERE KID = " + this.getId();
        DatabaseController.executeUpdate(sql);
        sql = "DELETE FROM beitrag WHERE BID = " + this.getId();
        DatabaseController.executeUpdate(sql);
    }
}