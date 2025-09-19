package mx.cti;

import javax.xml.bind.DatatypeConverter;

import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;


public class Unwrap {
  static public String unwraper(String ddl) throws Exception {
    byte[] charmap = new byte[] { 
      (byte)0X3d, (byte)0X65, (byte)0X85, (byte)0Xb3, (byte)0X18, (byte)0Xdb, (byte)0Xe2, (byte)0X87, (byte)0Xf1, (byte)0X52, (byte)0Xab, (byte)0X63, (byte)0X4b, (byte)0Xb5, (byte)0Xa0, (byte)0X5f, (byte)0X7d, (byte)0X68, (byte)0X7b, (byte)0X9b, (byte)0X24, (byte)0Xc2, (byte)0X28, (byte)0X67, (byte)0X8a, (byte)0Xde, (byte)0Xa4, (byte)0X26, (byte)0X1e, (byte)0X03, (byte)0Xeb, (byte)0X17
    , (byte)0X6f, (byte)0X34, (byte)0X3e, (byte)0X7a, (byte)0X3f, (byte)0Xd2, (byte)0Xa9, (byte)0X6a, (byte)0X0f, (byte)0Xe9, (byte)0X35, (byte)0X56, (byte)0X1f, (byte)0Xb1, (byte)0X4d, (byte)0X10, (byte)0X78, (byte)0Xd9, (byte)0X75, (byte)0Xf6, (byte)0Xbc, (byte)0X41, (byte)0X04, (byte)0X81, (byte)0X61, (byte)0X06, (byte)0Xf9, (byte)0Xad, (byte)0Xd6, (byte)0Xd5, (byte)0X29, (byte)0X7e
    , (byte)0X86, (byte)0X9e, (byte)0X79, (byte)0Xe5, (byte)0X05, (byte)0Xba, (byte)0X84, (byte)0Xcc, (byte)0X6e, (byte)0X27, (byte)0X8e, (byte)0Xb0, (byte)0X5d, (byte)0Xa8, (byte)0Xf3, (byte)0X9f, (byte)0Xd0, (byte)0Xa2, (byte)0X71, (byte)0Xb8, (byte)0X58, (byte)0Xdd, (byte)0X2c, (byte)0X38, (byte)0X99, (byte)0X4c, (byte)0X48, (byte)0X07, (byte)0X55, (byte)0Xe4, (byte)0X53, (byte)0X8c
    , (byte)0X46, (byte)0Xb6, (byte)0X2d, (byte)0Xa5, (byte)0Xaf, (byte)0X32, (byte)0X22, (byte)0X40, (byte)0Xdc, (byte)0X50, (byte)0Xc3, (byte)0Xa1, (byte)0X25, (byte)0X8b, (byte)0X9c, (byte)0X16, (byte)0X60, (byte)0X5c, (byte)0Xcf, (byte)0Xfd, (byte)0X0c, (byte)0X98, (byte)0X1c, (byte)0Xd4, (byte)0X37, (byte)0X6d, (byte)0X3c, (byte)0X3a, (byte)0X30, (byte)0Xe8, (byte)0X6c, (byte)0X31
    , (byte)0X47, (byte)0Xf5, (byte)0X33, (byte)0Xda, (byte)0X43, (byte)0Xc8, (byte)0Xe3, (byte)0X5e, (byte)0X19, (byte)0X94, (byte)0Xec, (byte)0Xe6, (byte)0Xa3, (byte)0X95, (byte)0X14, (byte)0Xe0, (byte)0X9d, (byte)0X64, (byte)0Xfa, (byte)0X59, (byte)0X15, (byte)0Xc5, (byte)0X2f, (byte)0Xca, (byte)0Xbb, (byte)0X0b, (byte)0Xdf, (byte)0Xf2, (byte)0X97, (byte)0Xbf, (byte)0X0a, (byte)0X76
    , (byte)0Xb4, (byte)0X49, (byte)0X44, (byte)0X5a, (byte)0X1d, (byte)0Xf0, (byte)0X00, (byte)0X96, (byte)0X21, (byte)0X80, (byte)0X7f, (byte)0X1a, (byte)0X82, (byte)0X39, (byte)0X4f, (byte)0Xc1, (byte)0Xa7, (byte)0Xd7, (byte)0X0d, (byte)0Xd1, (byte)0Xd8, (byte)0Xff, (byte)0X13, (byte)0X93, (byte)0X70, (byte)0Xee, (byte)0X5b, (byte)0Xef, (byte)0Xbe, (byte)0X09, (byte)0Xb9, (byte)0X77
    , (byte)0X72, (byte)0Xe7, (byte)0Xb2, (byte)0X54, (byte)0Xb7, (byte)0X2a, (byte)0Xc7, (byte)0X73, (byte)0X90, (byte)0X66, (byte)0X20, (byte)0X0e, (byte)0X51, (byte)0Xed, (byte)0Xf8, (byte)0X7c, (byte)0X8f, (byte)0X2e, (byte)0Xf4, (byte)0X12, (byte)0Xc6, (byte)0X2b, (byte)0X83, (byte)0Xcd, (byte)0Xac, (byte)0Xcb, (byte)0X3b, (byte)0Xc4, (byte)0X4e, (byte)0Xc0, (byte)0X69, (byte)0X36
    , (byte)0X62, (byte)0X02, (byte)0Xae, (byte)0X88, (byte)0Xfc, (byte)0Xaa, (byte)0X42, (byte)0X08, (byte)0Xa6, (byte)0X45, (byte)0X57, (byte)0Xd3, (byte)0X9a, (byte)0Xbd, (byte)0Xe1, (byte)0X23, (byte)0X8d, (byte)0X92, (byte)0X4a, (byte)0X11, (byte)0X89, (byte)0X74, (byte)0X6b, (byte)0X91, (byte)0Xfb, (byte)0Xfe, (byte)0Xc9, (byte)0X01, (byte)0Xea, (byte)0X1b, (byte)0Xf7, (byte)0Xce };

    String line;
    Reader inputString = new StringReader(ddl);
    BufferedReader br = new BufferedReader(inputString);
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    int l = 0;
    String s = "";
    while ((line = br.readLine()) != null) {
      if (l>0) {
        l -= line.length()+1;
        s += line;
      } else if (l<0) {
        l = 0;
        byte[] b = DatatypeConverter.parseBase64Binary(s);

        byte[] c = new byte[b.length-20];

        for (int i = 20; i < b.length; i++)
          c[i-20] = (byte)(charmap[b[i]&255]);

        InputStream is = new InflaterInputStream(new ByteArrayInputStream(c));
        byte[] buffer = new byte[1024];
        int len;
        while((len = is.read(buffer)) > 0)
          result.write(buffer, 0, len);
        
      }
      if (line.matches("^[0-9a-f]+ ([0-9a-f]+)$")) {
        l = Integer.parseInt(line.substring(1+line.lastIndexOf(' ')),16);
        s = "";
      }
    }
    br.close();
    //System.out.println(result.toString(StandardCharsets.UTF_8.name()));
    return result.toString(StandardCharsets.UTF_8.name());
  }
}