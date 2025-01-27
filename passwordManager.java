import java.io.*;
import java.util.*;
import javax.swing.*;

public class passwordManager {
    JFrame frame;
    JButton add,search,copyUser,copyPass,edit,delete;
    JTextField tf1;
    List<userinfo> l1;
    Stack<Integer> uf;
    Map<String,Integer> m1;
    void readUserMap() {
        try {
            FileInputStream fin=new FileInputStream(new File("map.dat"));
            ObjectInputStream ois=new ObjectInputStream(fin);
            m1=(HashMap<String,Integer>)ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    void writeUserMap() {
        try {
            FileOutputStream fos=new FileOutputStream(new File("map.dat"));
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(m1);
            oos.close();
        } catch (Exception e) {
            System.err.println(e);
        }       
    }
    void readUserDB() {
        try {
            FileInputStream fin=new FileInputStream(new File("db.dat"));
            ObjectInputStream ois=new ObjectInputStream(fin);
            l1=(ArrayList<userinfo>)ois.readObject();
            ois.close();   
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    void writeUserDB() {
        try {
            FileOutputStream fos=new FileOutputStream(new File("db.dat"));
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(l1);
            oos.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    void readUserStack() {
        try {
            FileInputStream fin=new FileInputStream(new File("stinfo.dat"));
            ObjectInputStream ois=new ObjectInputStream(fin);
            uf=(Stack<Integer>)ois.readObject();
            ois.close();   
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    void writeUserStack() {
        try {
            FileOutputStream fos=new FileOutputStream(new File("stinfo.dat"));
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(uf);
            oos.close();
        } catch (Exception e) {
            System.err.println(e);
        }        
    }
    void addUser() {
        String org,user,pass;
        System.out.println("Enter organization name : ");
        org=System.console().readLine();
        System.out.println("Enter username name : ");
        user=System.console().readLine();
        System.out.println("Enter password name : ");
        pass=System.console().readLine();
        String find=org+user;
        if(!m1.containsKey(find)) {
            l1.add(new userinfo(uf.peek(), org, user, pass));
            m1.put(find,uf.peek());
            uf.pop();
            writeUserMap();
            writeUserDB();
            writeUserStack();
            System.out.println("User entry added in database");
        } else {
            System.out.println("Organization and username combo already in database");
        }
        System.out.println("");
    }
    void deleteUser() {
        String org,user;
        System.out.println("Enter organization name : ");
        org=System.console().readLine();
        System.out.println("Enter username : ");
        user=System.console().readLine();
        String find=org+user;
        if(m1.containsKey(find)) {
            int key=m1.get(find);
            for(int i=0;i<l1.size();i++) {
                if(l1.get(i).id==key) {
                    l1.remove(i);
                    m1.remove(find);
                    uf.push(key);
                    System.out.println("User entry deleted\n");
                    writeUserDB();
                    writeUserDB();
                    writeUserStack();
                    break;
                }
            }
        } else {
            System.out.println("No entry for given username and password");
        }
    }
    void editUser() {
        String org,user;
        System.out.println("Enter organization name : ");
        org=System.console().readLine();
        System.out.println("Enter username : ");
        user=System.console().readLine();
        String find=org+user;
        if(m1.containsKey(find)) {
            String o1,u1,p1;
            int key=m1.get(org+user);
            System.out.println("Enter new organization name : ");
            o1=System.console().readLine();
            System.out.println("Enter new username : ");
            u1=System.console().readLine();
            System.out.println("Enter password name : ");
            p1=System.console().readLine();
            if(m1.containsKey(o1+u1)) {
                System.out.println("Record already exists in database");
            } else {
                m1.remove(find);
                m1.put(o1+u1,key);
                for(int i=0;i<l1.size();i++) {
                    if(l1.get(i).id==key) {
                        l1.get(i).organization=o1;
                        l1.get(i).username=u1;
                        l1.get(i).password=p1;
                    }
                }
                writeUserMap();
                writeUserDB();
                System.out.println("Record update successfully\n");
            }
        } else {
            System.out.println("User record not found\n");
        }
    }
    void searchUser() {
        System.out.println("Enter username or organization name to search: ");
        String tmp=System.console().readLine();
        tmp.toLowerCase();
        for(int i=0;i<l1.size();i++) {
            userinfo t1=l1.get(i);
            if(t1.organization.toLowerCase().contains(tmp)||t1.username.toLowerCase().contains(tmp)) {
                System.out.println("["+t1.organization+" , "+t1.username+" , "+t1.password+"]");
            }
        }
        System.out.println("");
    }
    void showAllDB() {
        if(l1.size()==0) {
            System.out.println("No entries in database");
            return;
        }
        for(int i=0;i<l1.size();i++) {
            userinfo tmp=l1.get(i);
            System.out.print("["+tmp.organization+" , "+tmp.username+" , "+tmp.password+"]\n");
        }
        System.out.println("");
    }
    void helpMessage() {
        System.out.println("help : show this message\nadd : add new user entry\ndelete : delete a user entry\nedit : edit user entry\nsearch : search a user entry\nshow : list all the user entry\nexit : exit this program\n");
    }
    public passwordManager() {
        uf=new Stack<>();
        try {
            File f1=new File("map.dat");
            File f2=new File("db.dat");
            File f3=new File("stinfo.dat");
            if(!f1.isFile()) {
                m1=new HashMap<>();
                writeUserMap();
            }
            if(!f2.isFile()) {
                l1=new ArrayList<>();
                writeUserDB();
            }
            if(!f3.isFile()) {
                uf=new Stack<>();
                for(int i=0;i<65536;i++) uf.push(i);
                writeUserStack();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        readUserMap();
        readUserDB();
        readUserStack();
        String cmd="";
        System.out.println("Password manager started\n");
        while (true) { 
            cmd=System.console().readLine();
            cmd.toLowerCase();
            switch(cmd) {
                case "add" : addUser();break;
                case "delete" : deleteUser();break;
                case "edit" : editUser();break;
                case "search" : searchUser();break;
                case "show" : showAllDB();break;
                case "help" : helpMessage();break;
                case "exit" : System.exit(0);break;
                default : 
                System.out.println("Unknown command\nType help for all commands");
            }
        }
    }
    public static void main(String args[]) {
        passwordManager pwd=new passwordManager();
    }
}