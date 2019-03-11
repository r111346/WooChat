package ua.woochat.app;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

@XmlRootElement
public class Group implements UsersAndGroups {
    @XmlElement
    private String groupID;

    private String groupName;
    @XmlElementWrapper(name="Users-List", nillable = true)
    @XmlElement(name="user")
    private Set<String> usersList = new LinkedHashSet<>();

    private Set<String> onlineUsersList = new LinkedHashSet<>();

    private Queue<HistoryMessage> queue = null;

    public Group() {
    }

    public Group(String idGroup, String groupName) {
        this.groupID = idGroup;
        this.groupName = groupName;
        queue = new ArrayBlockingQueue<HistoryMessage>(20);
        saveGroup();
    }

    public Set<String> getUsersList() {
        return usersList;
    }

    public void addUser (String login){
        usersList.add(login);
        saveGroup();
    }

    public void removeUser (String login){
        usersList.remove(login);
        saveGroup();
    }

    public Set<String> getOnlineUsersList () {
        return onlineUsersList;
    }

    public void addOnlineUser (String login){
        onlineUsersList.add(login);
        saveGroup();
    }

    public void removeOnlineUser (String login){
        onlineUsersList.remove(login);
        saveGroup();
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    @XmlElement(name="Title-group")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
        saveGroup();
    }

    @XmlElement(name="History-Message")
    public Queue<HistoryMessage> getQueue() {
        return queue;
    }

    public void setQueue(Queue<HistoryMessage> queue) {
        this.queue = queue;
        saveGroup();
    }

    public void addToListMessage(HistoryMessage historyMessage) {
        if (!queue.offer(historyMessage)) {
            queue.poll();
            queue.offer(historyMessage);
        }
        saveGroup();
    }

    public void saveGroup() {
        String path = new File("").getAbsolutePath();
        File file = new File(path + "/Server/src/main/resources/Group/" + this.getGroupID() + ".xml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream stream = new FileOutputStream(file);
            HandleXml.marshalling(Group.class, this, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    // создает список групп юзера из файлов по списку стрингов
    public static Set<Group> groupUser (Set<String> groups) {
        Set<Group> groupSet = new LinkedHashSet<>();
        String path = new File("").getAbsolutePath();
        File file;
        Group group;
        for (String entry : groups) {
            file = new File(path + "/Server/src/main/resources/Group/" + entry + ".xml");
            if (file.isFile()) {
                group = (Group) HandleXml.unMarshalling(file, Group.class);
                groupSet.add(group);
            }
        }
        return groupSet;
    }

    // создает очередь исторических сообщений по определенной группе
    public static Queue<HistoryMessage> groupSingIn(Group group) {  //переделать, чтобы выводило нужное сообщение, когда пользователь уже подключен к чату
        String path = new File("").getAbsolutePath();
        File file = new File(path + "/Server/src/main/resources/Group/" + group.getGroupID() + ".xml");
        Queue<HistoryMessage> historyMessages = null;
        if (file.isFile()) {
            group = (Group) HandleXml.unMarshalling(file, Group.class);
            historyMessages = group.getQueue();
        }
        return historyMessages;
    }

    @Override
    public String toString() {

        return "Group{" +
                "groupID='" + groupID + '\'' +
                ", usersList=" + usersList +
                ", onlineUsersList=" + onlineUsersList +
                ", message=" + getQueue() +
                '}';
    }

}