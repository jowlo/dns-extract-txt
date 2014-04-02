import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExtractTXT {
  
  private String hostname;
  private String nameserver;
  private static HashMap<Integer, String> resMap= new HashMap<Integer, String>();
    
  public ExtractTXT(final String pHostname, final String pNameserver) {
    nameserver = pNameserver;
    hostname = pHostname;
  }
  
  
  public static String extract (final String pHostname, final String pNameserver) {
    String txt = new String();
    DNSQuery query = new DNSQuery (pHostname, 255, 1); // 255 = any, 1 = DNS Class Internet
    try {
      Socket socket = new Socket (pNameserver, 53); // 53 = DNS DEFAULT_PORT
      socket.setSoTimeout (10000);
      sendQuery (query, socket);
      txt = getResponse (query, socket);
      socket.close ();
      //printRRs (query);
    } catch (IOException ex) {
      System.out.println (ex);
    }
    return txt;
  }
  
  public static void sendQuery (DNSQuery query, Socket socket) throws IOException {
    BufferedOutputStream bufferedOut =
      new BufferedOutputStream (socket.getOutputStream ());
    DataOutputStream dataOut = new DataOutputStream (bufferedOut);
    byte[] data = query.extractQuery ();
    dataOut.writeShort (data.length);
    dataOut.write (data);
    dataOut.flush ();
  }
  
  public static String getResponse (DNSQuery query, Socket socket) throws IOException {
    InputStream bufferedIn =
      new BufferedInputStream (socket.getInputStream ());
    DataInputStream dataIn = new DataInputStream (bufferedIn);
    int responseLength = dataIn.readUnsignedShort ();
    byte[] data = new byte[responseLength];
    dataIn.readFully (data);
    return receiveResponse (data, responseLength);
  }

  public static String receiveResponse (byte[] data, int length) throws IOException {
    DNSInputStream dnsIn = new DNSInputStream (data, 0, length);
    
    /*int id = dnsIn.readShort ();
    int flags = dnsIn.readShort ();*/
    dnsIn.skip(4);
    
    int numQueries = dnsIn.readShort ();
    
    /*int numAnswers = dnsIn.readShort ();
    int numAuthorities = dnsIn.readShort ();
    int numAdditional = dnsIn.readShort ();*/
    dnsIn.skip(6);
    while (numQueries -- > 0) { // discard questions
      String queryName = dnsIn.readDomainName ();
      /*int queryType = dnsIn.readShort ();
      int queryClass = dnsIn.readShort ();*/
      dnsIn.skip(4);
    }
    String rrName = dnsIn.readDomainName ();
    
    /*int rrType = dnsIn.readShort ();
    int rrClass = dnsIn.readShort ();
    long rrTTL = dnsIn.readInt ();
    int rrDataLen = dnsIn.readShort ();
    */
    dnsIn.skip(10);
    String s;
    String result = new String();
    try{
      while (!(s = dnsIn.readString ()).isEmpty())
         result += s;
    } catch (Exception e){}
    return result;
  }
  
  public static String collect(final String pDomain, final String pNameserver){
    StringBuilder collect = new StringBuilder();
    ExecutorService es = Executors.newCachedThreadPool();
    for(int i=0; i<= Integer.valueOf(extract(pDomain, pNameserver)); i++){
      //collect.append(extract(pDomain.substring(0, pDomain.indexOf('.')) + i +pDomain.substring(pDomain.indexOf('.')), pNameserver));
      //      System.out.println(pDomain.substring(0, pDomain.indexOf('.')) + i +pDomain.substring(pDomain.indexOf('.')));
      es.execute(new Query(pDomain.substring(0, pDomain.indexOf('.')) + i +pDomain.substring(pDomain.indexOf('.')), pNameserver, i));

      }
    es.shutdown();
    try {
      es.awaitTermination(5, TimeUnit.MINUTES);
    } catch (InterruptedException e) { e.printStackTrace(); }
    
    for(int i=0; i < resMap.size(); i++){
      collect.append(resMap.get(i));
    }
    return collect.toString();
  }
  
  public static void main (String[] args) {
    switch(args[0].charAt(0)){
      case 'c':
        System.out.print(collect(args[1], args[2]));
        break;
      case 'd':
        try {
          System.out.write(Encode.str2bin(collect(args[1], args[2])));
        } catch (IOException e) {}
        break;
      case 'f':
        try {
          FileOutputStream f = new FileOutputStream(args[3]);
          f.write(Encode.str2bin(collect(args[1], args[2])));
          f.close();
        }
        catch(IOException e){}
        break;
      default:
        System.out.println("Need to set options! \n"
            + "\t usage: ExtractTXT [c(ollect)|d(collect and _d_ecode)] (subdomain) (nameserver) [c(collect)|d(collect and _d_ecode)]");
        break;
    }
  }


  public static void setArrVar(final String pVal, final int pKey) {
    resMap.put(pKey, pVal);
  }

}

class Query implements Runnable {
  private String hostname, nameserver; 
  private final int arrPos;
  public Query (final String pHostname, final String pNameserver, final int parrPos) {
    hostname = pHostname;
    nameserver = pNameserver;
    arrPos = parrPos;
  }
  @Override
  public void run() {
    ExtractTXT.setArrVar(ExtractTXT.extract(hostname, nameserver), arrPos);
      String txt = new String();
      DNSQuery query = new DNSQuery (hostname, 255, 1); // 255 = any, 1 = DNS Class Internet
      try {
        Socket socket = new Socket (nameserver, 53); // 53 = DNS DEFAULT_PORT
        socket.setSoTimeout (0);
        ExtractTXT.sendQuery (query, socket);
        txt = ExtractTXT.getResponse (query, socket);
        socket.close ();
        //printRRs (query);
      } catch (IOException ex) {
        System.out.println ("in Query" + ex);
      }
      ExtractTXT.setArrVar(txt, arrPos);
   }
  }
  
