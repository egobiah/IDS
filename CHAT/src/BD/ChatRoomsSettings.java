package BD;

public interface ChatRoomsSettings {
    public Boolean isVisible();
    public void show();
    public void hide();

    public String getName();
    public void setName(String name);

    public Boolean isOpen();
    public void openChat();
    public void closeChat();

    public void setDescritpion(String s);
    public String getDescription();

}
