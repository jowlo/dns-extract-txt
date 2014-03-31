import java.io.*;

public class DNSInputStream extends ByteArrayInputStream {
  protected DataInputStream dataIn;

  public DNSInputStream (byte[] data, int off, int len) {
    super (data, off, len);
    dataIn = new DataInputStream (this);
  }

  public int readByte () throws IOException {
    return dataIn.readUnsignedByte ();
  }

  public int readShort () throws IOException {
    return dataIn.readUnsignedShort ();
  }

  public long readInt () throws IOException {
    return dataIn.readInt () & 0xffffffffL;
  }

  public String readString () throws IOException {
    int len = readByte ();
    if (len == 0) {
      return "";
    } else {
      byte[] buffer = new byte[len];
      dataIn.readFully (buffer);
      return new String (buffer, "latin1");
    }
  }

  public String readDomainName () throws IOException {
    if (pos >= count) throw new EOFException ("EOF reading domain name");
    if ((buf[pos] & 0xc0) == 0) {
      String label = readString ();
      if (label.length () > 0) {
        String tail = readDomainName ();
        if (tail.length () > 0)
          label = label + '.' + tail;
      }
      return label;
    } else {
      if ((buf[pos] & 0xc0) != 0xc0)
        throw new IOException ("Invalid domain name compression offset");
      int offset = readShort () & 0x3fff;
      DNSInputStream dnsIn = new DNSInputStream (buf, offset, buf.length - offset);
      String domainName = dnsIn.readDomainName ();
      dnsIn.close();
      return domainName;
    }
  }
}
