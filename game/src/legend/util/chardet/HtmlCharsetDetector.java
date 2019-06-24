package legend.util.chardet;

import java.io.BufferedInputStream;
import java.net.URL;

public class HtmlCharsetDetector{
    public static void main(String argv[]) throws Exception{
        if(argv.length != 1 && argv.length != 2){
            System.out.println("Usage: HtmlCharsetDetector <url> [<languageHint>]");
            System.out.println("");
            System.out.println("Where <url> is http://...");
            System.out.println("For optional <languageHint>. Use following...");
            System.out.println("		1 => Japanese");
            System.out.println("		2 => Chinese");
            System.out.println("		3 => Simplified Chinese");
            System.out.println("		4 => Traditional Chinese");
            System.out.println("		5 => Korean");
            System.out.println("		6 => Dont know (default)");
            return;
        }
        int lang = (argv.length == 2) ? Integer.parseInt(argv[1]) : nsPSMDetector.ALL;
        nsDetector det = new nsDetector(lang);
        det.init(cs->System.out.println("CHARSET = " + cs));
        URL url = new URL(argv[0]);
        BufferedInputStream imp = new BufferedInputStream(url.openStream());
        byte[] buf = new byte[1024];
        boolean isAscii = true;
        for(int len;-1 != (len = imp.read(buf,0,buf.length));)
            if(!(isAscii = det.isAscii(buf,len)) && det.doIt(buf,len,false)) break;
        det.dataEnd();
        if(isAscii) System.out.println("CHARSET = ASCII");
        else{
            String prob[] = det.getProbableCharsets();
            for(int i = 0;i < prob.length;i++)
                System.out.println("Probable Charset = " + prob[i]);
        }
    }
}
