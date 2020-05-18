package legend.util;

import static java.lang.String.valueOf;
import static legend.util.ConsoleUtil.CS;
import static legend.util.StringUtil.gsph;
import static legend.util.ValueUtil.isEmpty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.util.JAXBResult;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import legend.util.intf.IJaxbUtil;

public final class JaxbUtil implements IJaxbUtil{
    private JaxbUtil(){}

    public static void convertToXml(Path path, Object object){
        convertToXml(path,object,false);
    }

    public static void convertToXml(Path path, Object object, boolean fragment){
        convertToXml(path,object,CHARSET_UTF8,fragment);
    }

    public static void convertToXml(Path path, Object object, String encoding, boolean fragment){
        try{
            Marshaller marshaller = JAXBContext.newInstance(object.getClass()).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING,encoding);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT,fragment);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path.toFile()));
            marshaller.marshal(object,bufferedWriter);
        }catch(Exception e){
            CS.s(gsph(ERR_ANLS_XML,e.toString())).l(2);
        }
    }

    public static <T> T convertToObject(Path path, Class<T> c){
        return convertToObject(path,c,false);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertToObject(Path path, Class<T> c, boolean lexical){
        T t = null;
        try{
            Source source = new SAXSource(new InputSource(new BufferedReader(new FileReader(path.toFile()))));
            JAXBResult jaxbResult = new JAXBResult(JAXBContext.newInstance(c));
            if(lexical) jaxbResult.setLexicalHandler(new LexicalHandlerImpl(jaxbResult));
            TransformerFactory.newInstance().newTransformer().transform(source,jaxbResult);
            t = (T)jaxbResult.getResult();
        }catch(Exception e){
            CS.s(gsph(ERR_ANLS_XML,e.toString())).l(2);
        }
        return t;
    }

    public static String escape(String s, boolean reverse){
        if(isEmpty(s)) return S_EMPTY;
        if(reverse) return s.replaceAll(ESCAPE_CUSP_L,XML_CUSP_L).replaceAll(ESCAPE_CUSP_R,XML_CUSP_R);
        else return s.replaceAll(XML_CUSP_L,ESCAPE_CUSP_L).replaceAll(XML_CUSP_R,ESCAPE_CUSP_R);
    }

    private static class LexicalHandlerImpl implements LexicalHandler{
        private JAXBResult jaxbResult;

        private LexicalHandlerImpl(JAXBResult jaxbResult){
            this.jaxbResult = jaxbResult;
        }

        @Override
        public void comment(char[] ch, int start, int length) throws SAXException{
            String s = gsph(XML_NOTE,valueOf(ch,start,length));
            jaxbResult.getHandler().characters(s.toCharArray(),0,s.length());
        }

        @Override
        public void startDTD(String name, String publicId, String systemId) throws SAXException{}

        @Override
        public void endDTD() throws SAXException{}

        @Override
        public void startEntity(String name) throws SAXException{}

        @Override
        public void endEntity(String name) throws SAXException{}

        @Override
        public void startCDATA() throws SAXException{}

        @Override
        public void endCDATA() throws SAXException{}
    }
}
