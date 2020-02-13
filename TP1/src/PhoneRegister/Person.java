package PhoneRegister;

public class Person {
    private static int c_uid;
    private int uid;
    private String name;
    private String number;

    public Person(String name, String number) {
        this.uid = c_uid++;
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public int getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "Person{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
