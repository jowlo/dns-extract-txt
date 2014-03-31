import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;

import javax.xml.bind.DatatypeConverter;
public class Encode {

  public Encode() {
    // TODO Auto-generated constructor stub
  }
  
  public static String bin2str(final String filename){
    byte[] bytes;
    try {
      bytes = Files.readAllBytes(Paths.get(filename));
    } catch (Exception e) {
      bytes = null;
    }
    String str = new String();
    str = DatatypeConverter.printBase64Binary(bytes);
    return str;
  }
  
  public static byte[] str2bin(final String filename){
    String string = null;
    try {
      string = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
    } catch (IOException e) { }
    byte[] bytes;
    bytes = DatatypeConverter.parseBase64Binary(string);    
    return bytes;
  }
  
  public static String createZoneFile(final String encString){
    String[] strArray;
    String result = "";
    strArray = encString.split("(?<=\\G.{2000})");
    result += "client\t\t\tIN\tTXT\t\"" + (strArray.length - 1) + "\"\n";
    for(int i = 0; i < strArray.length; i++){
      result += "client" + i + "\t\t\tIN\tTXT\t\"" + strArray[i] + "\"\n";  
    }
    return result;
  }
  
  public static void main(String args[]){
    switch(args[0]){
      case "e":
        System.out.print(bin2str(args[1]));
        break;
      case "d":
        try {
          System.out.write(str2bin(args[1]));
        } catch (IOException e) {}
        break;
      case "z":
        System.out.print(createZoneFile(bin2str(args[1])));
        break;
      default:
        System.out.println("Need to set options [e(ncode)|d(ecode)|z(one file)]");
        break;
    }
  }
  
  
  
}
