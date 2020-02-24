package BD;

import java.util.ArrayList;

public class ChatRoom implements ChatRoomsRIghts, ChatRoomsSettings, ChatRoomManager{

    int ID;
    ArrayList<Person> admin;
    ArrayList<Person> modo;
    ArrayList<Person> viewer;

    Boolean open;
    Boolean visible;
    protected String name;
    protected String description;


    private static int compteur_chat = 0;


    public ChatRoom(String name, String description, Person createur){
        admin = new ArrayList<>();
        modo = new ArrayList<>();
        viewer = new ArrayList<>();
        admin.add(createur);
        viewer.add(createur);
        this.name = name;
        this.description = description;
        this.visible = true;
        this.open = true;
        ID = compteur_chat;
        compteur_chat++;

    }

    @Override
    public String toString() {
        return this.name + " : " + this.description;
    }

    public int getID(){
        return  ID;
    }
    // SETTINGS

    public void setDescritpion(String s){
        this.description = s;
    }
    public String getDescription(){
        return description;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void show() {
        this.visible = true;
    }

    public void hide(){
        this.visible = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isOpen(){
        return open;
    }

    public void openChat(){
        open = true;
    }

    public void closeChat(){
        open = false;
    }

    // GET INFORMATION

    // RIGTHS

    public Boolean isModo(Person p){
        return modo.contains(p);
    }

    public Boolean isAdmin(Person p){
        return admin.contains(p);
    }

    public Boolean isViewer(Person p){
        return viewer.contains(p);
    }

    public Boolean superUser(Person person){
        return admin.contains(person) || modo.contains(person);
    }





    public Boolean canView(Person person){
        return viewer.contains(person);
    }

    public Boolean canRemoveMessage(Person person){
        return superUser(person);
    }

    public Boolean canKick(Person person){
        return superUser(person);
    }

    public Boolean canAdd(Person person){
        return isAdmin(person);
    }


    // MANAGER

    public void addViewer(Person adder, Person newViewer) {
        if(isAdmin(adder)){
            if(!viewer.contains(newViewer)){
                viewer.add(newViewer);
            }
        }
    }

    public void kickViewer(Person remover, Person removed) {
        if(canKick(remover)){
            removeAdmin(remover, removed);
            removeModo(remover, removed);
            if(isViewer(removed)){
                viewer.remove(removed);
            }

        }
    }

    public void addModo(Person adder, Person newModo) {
        if(isAdmin(adder)){
            if(!isModo(newModo)){
                modo.add(newModo);
            }
        }
    }

    public void removeModo(Person remover, Person removed) {
        if(isAdmin(remover)){
            if(isModo(removed)){
                modo.remove(removed);
            }
        }
    }

    public void addAdmin(Person adder, Person newAdmin) {
        if(!isAdmin(adder)){

        }
    }

    public void removeAdmin(Person remover, Person removed) {
        if(isAdmin(remover)){
            if(isAdmin(removed)){
                admin.remove(removed);
            }
        }
    }
}
