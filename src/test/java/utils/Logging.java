package utils;

public class Logging {

    public static void e(Object msg){
        e(msg, null);
    }

    public static void e(Object msg, Throwable t){
        System.out.println("[ERROR]"+msg);
        if (t != null){
            t.printStackTrace();
        }
    }

    public static void w(Object msg){
        System.out.println("[WARNING]"+msg);
    }

    public static void i(Object msg){
        System.out.println("[INFO]"+msg);
    }

}
