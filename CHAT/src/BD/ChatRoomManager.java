package BD;

public interface ChatRoomManager {
    public void addViewer(Person adder, Person newViewer);
    public void kickViewer(Person remover, Person removed);

    public void addModo(Person adder, Person newModo );
    public void removeModo(Person remover, Person removed);

    public void addAdmin(Person adder, Person newAdmin);
    public void removeAdmin(Person remover, Person removed);

    //public void removeMessage(Person remover, Message message);


}
