package BD;

public class Person {
    String nom;
    String prenom;
    String password;
    String pseudo;
    private static int compteur_id = 0;
    int id;

    private final Object synch_cmp = new Object();

    Person(String nom, String prenom, String password){
        synchronized (synch_cmp) {
            id = compteur_id;
            compteur_id++;
        }
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return pseudo;
    }
}
