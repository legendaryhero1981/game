package legend.util.adapter;

import static legend.util.StringUtil.rph;
import static legend.util.ValueUtil.nonEmpty;

import java.util.regex.Matcher;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import legend.intf.ICommon;

public class CDataAdapter extends XmlAdapter<String,String> implements ICommon{
    @Override
    public String unmarshal(String s) throws Exception{
        if(nonEmpty(s)){
            Matcher matcher = PTRN_XML_CDATA.matcher(s);
            if(matcher.find()) return matcher.group(1);
        }
        return s;
    }

    @Override
    public String marshal(String s) throws Exception{
        if(nonEmpty(s)) return rph(XML_CDATA,PH_ARGS0,s);
        return s;
    }
}
