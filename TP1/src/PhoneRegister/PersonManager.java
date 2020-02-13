package PhoneRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonManager implements Managable {
    private HashMap<Integer, Person> persons;

    public PersonManager(){
        this.persons = new HashMap<>();
    }

    @Override
    public void addPerson(Person person) {
        this.persons.put(person.getUid(), person);
    }

    @Override
    public Person removePerson(Person person) {
        return this.persons.remove(person.getUid());
    }

    @Override
    public Person getPerson(Person person) {
        return this.persons.get(person.getUid());
    }

    @Override
    public Person getPersonByName(String name) {
        return null;
    }

    @Override
    public Person getPersonByPhone(String phone) {
        for(Map.Entry<Integer, Person> entry : this.persons.entrySet())
            if ((entry.getValue()).getNumber().equals(phone)) return entry.getValue();
        return null;
    }

    @Override
    public Person getPersonByUid(int uid) {
        for(Map.Entry<Integer, Person> entry : this.persons.entrySet())
            if (entry.getKey() == uid) return entry.getValue();
        return null;
    }

    public String printAll(){
        StringBuilder builder = new StringBuilder();
        builder.append("Phones Registry : ").append("\n");
        for(Map.Entry<Integer, Person> entry : this.persons.entrySet())
            builder.append(entry.getValue().toString());
        return builder.toString();
    }
}
