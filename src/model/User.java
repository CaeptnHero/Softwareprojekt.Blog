package model;

/**
 * TODO: FINISH JAVADOC COMMENT
 */
public abstract class User {
    protected int id;
    protected String username;
    protected String password;

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

    /**
     * TODO: FINISH JAVADOC COMMENT
     * @param text
     * @param oberbeitrag
     * @return
     */
    public Comment createComment(String text, Post oberbeitrag) {
        return new Comment(this, text, oberbeitrag);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
