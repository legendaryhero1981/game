package legend.util.intf;

import static legend.util.ValueUtil.isEmpty;

import java.util.Optional;

import legend.intf.ICommon;
import legend.util.param.SingleValue;

public interface IJaxbUtil extends ICommon{
    String XML_AND = "&";
    String XML_QUOTE_S = "'";
    String XML_QUOTE_D = "\"";
    String XML_CUSP_L = "<";
    String XML_CUSP_R = ">";
    String ESCAPE_AND = "&amp;";
    String ESCAPE_QUOTE_S = "&quot;";
    String ESCAPE_QUOTE_D = "&apos;";
    String ESCAPE_CUSP_L = "&lt;";
    String ESCAPE_CUSP_R = "&gt;";
    String ERR_ANLS_XML = "XML" + N_FLE + V_ANLS + V_FAIL + N_ERR_INFO + PH_ARG0;

    static String esc(String s, String... args){
        if(isEmpty(s)) return S_EMPTY;
        if(isEmpty(args)) return s;
        SingleValue<String> value = new SingleValue<>(s);
        Optional<SingleValue<String>> opt = Optional.of(value);
        for(String arg : args)
            switch(arg){
                case XML_AND:
                opt.filter(v->v.get().contains(XML_AND)).ifPresent(v->v.set(v.get().replaceAll(XML_AND,ESCAPE_AND)));
                break;
                case XML_QUOTE_S:
                opt.filter(v->v.get().contains(XML_QUOTE_S)).ifPresent(v->v.set(v.get().replaceAll(XML_QUOTE_S,ESCAPE_QUOTE_S)));
                break;
                case XML_QUOTE_D:
                opt.filter(v->v.get().contains(XML_QUOTE_D)).ifPresent(v->v.set(v.get().replaceAll(XML_QUOTE_D,ESCAPE_QUOTE_D)));
                break;
                case XML_CUSP_L:
                opt.filter(v->v.get().contains(XML_CUSP_L)).ifPresent(v->v.set(v.get().replaceAll(XML_CUSP_L,ESCAPE_CUSP_L)));
                break;
                case XML_CUSP_R:
                opt.filter(v->v.get().contains(XML_CUSP_R)).ifPresent(v->v.set(v.get().replaceAll(XML_CUSP_R,ESCAPE_CUSP_R)));
            }
        return value.get();
    }
}
