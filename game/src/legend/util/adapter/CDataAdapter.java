package legend.util.adapter;

import static legend.util.ValueUtil.nonEmpty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import legend.intf.ICommon;
import legend.util.StringUtil;

public class CDataAdapter extends XmlAdapter<String,String> implements ICommon{
    private final Pattern pattern = Pattern.compile(REG_XML_CDATA);

    @Override
    public String unmarshal(String s) throws Exception{
        if(nonEmpty(s)){
            Matcher matcher = pattern.matcher(s);
            if(matcher.find()) return matcher.group(1);
        }
        return s;
    }

    @Override
    public String marshal(String s) throws Exception{
        if(nonEmpty(s)) return StringUtil.rph(XML_CDATA,PH_ARGS0,s);
        return s;
    }
}
