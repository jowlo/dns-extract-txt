import java.io.*;
import java.util.*;

public class DNSQuery {
  private String queryHost;
  private int queryType, queryClass, queryID;
  private static int globalID;

  public DNSQuery (String host, int type, int clas) {
    StringTokenizer labels = new StringTokenizer (host, ".");
    while (labels.hasMoreTokens ())
      if (labels.nextToken ().length () > 63)
        throw new IllegalArgumentException ();
    queryHost = host;
    queryType = type;
    queryClass = clas;
    synchronized (getClass ()) {
      queryID = (++ globalID) % 65536;
    }
  }

  public String getQueryHost () {
    return queryHost;
  }

  public int getQueryType () {
    return queryType;
  }

  public int getQueryClass () {
    return queryClass;
  }

  public int getQueryID () {
    return queryID;
  }

  public byte[] extractQuery () {
    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream ();
    DataOutputStream dataOut = new DataOutputStream (byteArrayOut);
    try {
      dataOut.writeShort (queryID);
      dataOut.writeShort ((0 << 15) |
                          (0 << 11) |
                          (1 << 8));
      dataOut.writeShort (1); // # queries
      dataOut.writeShort (0); // # answers
      dataOut.writeShort (0); // # authorities
      dataOut.writeShort (0); // # additional
      StringTokenizer labels = new StringTokenizer (queryHost, ".");
      while (labels.hasMoreTokens ()) {
        String label = labels.nextToken ();
        dataOut.writeByte (label.length ());
        dataOut.writeBytes (label);
      }
      dataOut.writeByte (0);
      dataOut.writeShort (queryType);
      dataOut.writeShort (queryClass);
    } catch (IOException ignored) {
    }
    return byteArrayOut.toByteArray ();
  }
}
