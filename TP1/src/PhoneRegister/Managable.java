package PhoneRegister;

public interface Managable {
    public void addPerson(Person person);
    public Person removePerson(Person person);
    public Person getPerson(Person person);
    public Person getPersonByName(String name);
    public Person getPersonByPhone(String phone);
    public Person getPersonByUid(int uid);
}
