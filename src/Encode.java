import java.io.*;


import java.io.Reader;

import javax.xml.bind.DatatypeConverter;
public class Encode {

  public Encode() {
    // TODO Auto-generated constructor stub
  }
  
  public static String bin2str(final String filename){
    String str = new String(); 
    try {
      RandomAccessFile f = new RandomAccessFile(filename, "r");
      byte[] bytes = new byte[(int)f.length()];
      f.read(bytes);
      //bytes = Files.readAllBytes(Paths.get(filename)); //Java 7 Only
      str = DatatypeConverter.printBase64Binary(bytes);
      f.close();
    } catch (Exception e) { }
    return (str.isEmpty())? null : str;
  }
  
  public static byte[] str2bin(final String filename){
    String str = new String();
    /*try {
      string = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
    } catch (IOException e) { }
    */ // Java7 only
    try {
        Reader reader = new BufferedReader( new FileReader (filename));
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[8192];
        int read;
        while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
            builder.append(buffer, 0, read);
        }
        str = builder.toString();
    } catch (Exception e) { }
    /*
    try {
      RandomAccessFile f = new RandomAccessFile(filename, "r");
      byte[] bytes = new byte[(int)f.length()];
      f.read(bytes);
      str = bytes.toString();
      f.close();
    } catch (Exception e) { }
    */ // Java7 only
    byte[] returnBytes;
    returnBytes = DatatypeConverter.parseBase64Binary(str);    
    return returnBytes;
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
    switch(args[0].charAt(0)){
      case 'e':
        System.out.print(bin2str(args[1]));
        break;
      case 'd':
        try {
          System.out.write(str2bin(args[1]));
        } catch (IOException e) {}
        break;
      case 'z':
        System.out.print(createZoneFile(bin2str(args[1])));
        break;
      default:
        System.out.println("Need to set options [e(ncode)|d(ecode)|z(one file)]");
        break;
    }
  }
  
  
  
}
