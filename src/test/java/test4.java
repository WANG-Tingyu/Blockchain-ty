import java.io.File;
import java.io.FileInputStream;

public class test4 {
    public static void main(String[] args) {
        File file = new File("src/main/resources/Node.fxml");
        //byte[] bytes = new byte[1024];
        //StringBuffer sb = new StringBuffer();
        //int byteread = 0;
        //FileInputStream fis = new FileInputStream(file);
        //while ((byteread = fis.read(bytes)) != -1) {
        //    String s = new String(bytes, 0, byteread);
        //    sb.append(s);
        //}
        //System.out.println(sb);
        if(file.exists()) System.out.println("找到了");
    }
}
