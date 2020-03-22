package BD;

public interface ChatRoomsRIghts {
    public Boolean superUser(Person person);
    public Boolean canView(Person person);
    public Boolean canRemoveMessage(Person person);
    public Boolean canKick(Person person);
    public Boolean canAdd(Person person);
    public Boolean isModo(Person p);
    public Boolean isAdmin(Person p);
    public Boolean isViewer(Person p);
}
