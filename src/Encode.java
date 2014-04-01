import java.io.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.xml.bind.DatatypeConverter;
public class Encode {

  public Encode() {
    // TODO Auto-generated constructor stub
  }
  
  public static String binfile2str(final String filename){
    String str = new String(); 
    try {
      RandomAccessFile f = new RandomAccessFile(filename, "r");
      byte[] bytes = new byte[(int)f.length()];
      f.read(bytes);
      str = bin2str(bytes);
      f.close();
    } catch (Exception e) { }
    return (str.isEmpty())? null : str;
  }
  
  private static String bin2str(byte[] bytes) {
    Deflater compresser = new Deflater();
    compresser.setInput(bytes);
    compresser.finish();
    byte[] output = new byte[bytes.length];
    int comprSize = compresser.deflate(output);
    byte[] outputCompressed = new byte[comprSize];
    System.arraycopy(output, 0, outputCompressed, 0, comprSize);
    String str = new String();
    str = DatatypeConverter.printBase64Binary(outputCompressed);
    return (str.isEmpty())? null : str;
  }

  public static byte[] strfile2bin(final String filename){
    String str = new String();
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
    return str2bin(str);
  }
  
  public static byte[] str2bin(final String str){
    byte[] comprBytes = DatatypeConverter.parseBase64Binary(str);   
    Inflater infl = new Inflater();
    infl.setInput(comprBytes);

    byte[] output = new byte[comprBytes.length*3];
    int inflSize = 0;
    try {
      inflSize = infl.inflate(output);
    } catch (DataFormatException e) {
      e.printStackTrace();
    }
    byte[] outputInfl = new byte[inflSize];
    System.arraycopy(output, 0, outputInfl, 0, inflSize);

    return outputInfl;
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
        System.out.print(binfile2str(args[1]));
        break;
      case 'd':
        try {
          System.out.write(strfile2bin(args[1]));
        } catch (IOException e) {}
        break;
      case 'z':
        System.out.print(createZoneFile(binfile2str(args[1])));
        break;
      default:
        System.out.println("Need to set options [e(ncode)|d(ecode)|z(one file)]");
        break;
    }
  }
  
  
  
}
