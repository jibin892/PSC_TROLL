package techsayas.in.psctrolls.psctroll;

public class Homeview {

    private String messageText;
    private String messageUser;
    private String messageTime;
   // private  String photo;
    private  String photo1;
    private  String photo;
    private  String idd;
    private  long stamp;
    private  String id;
    private  String email;
    private  String likes;

    private  String s;
    private  String postid;
    public Homeview(String messageText, String messageUser, String photo,String likes) {
        this.messageText = messageText;
        this.messageUser = messageUser;
this.photo1=photo1;
     this.photo=photo;
        this.postid=postid;
        // Initialize to current time
       this. messageTime = messageTime;
        this.s=s;
        this.idd=idd;
        this.stamp=stamp;
        this.id=id;
        this.email=email;
        this.likes=likes;


    }



    public Homeview() {
    }
    public String s()
    {
        return  s;
    }
    public String getLikes()
    {
        return  likes;
    }

    public  String getPostid()
    {
        return  postid;
    }
    public String getIdd()
    {
        return  idd;
    }
    public long getStamp()
    {
        return  stamp;
    }
    public String getId()
    {
        return  id;
    }
    public String getEmail()
    {
        return  email;
    }

    public String getPhoto()
{
    return  photo;
}
    public String getPhoto1()
    {
        return  photo1;
    }


    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }


//    public String getPhoto1() {
//        return photo1;
//    }
//
//    public void setPhoto1(String photo1) {
//        this.photo1 = photo1;
//    }
}