package model;

/**
 * TODO: FINISH JAVADOC COMMENT
 */
public abstract class User {
    private int id;
    private String username;
    private String password;
    private boolean isBlogger;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isBlogger = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsBlogger() {
        return isBlogger;
    }

    public void setIsBlogger(boolean isBlogger) {
        this.isBlogger = isBlogger;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getUsername(), getClass().getSimpleName());
    }

    /**
     * Erstellt einen neuen Kommentar
     *
     * @param text Text des Kommentars
     * @param parent Oberbeitrag des Kommentars
     * @return Erstellter Kommentar
     */
    public Comment createComment(String text, Post parent) {
        return new Comment(this, text, parent);
    }
}
