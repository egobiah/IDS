package BD;

import java.io.Serializable;
import java.util.Objects;

public class Person implements Serializable {

    String nom;
    String prenom;
    String password;
    String pseudo;
    //private static int compteur_id = 0;
   // int id;

   // private final Object synch_cmp = new Object();

    public Person(String pseudo, String password) {
        /*
        synchronized (synch_cmp) {
          id = compteur_id;
            compteur_id++;
        }


         */

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

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return getPassword().equals(person.getPassword()) &&
                getPseudo().equals(person.getPseudo());
    }

    /*
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


 */
}
