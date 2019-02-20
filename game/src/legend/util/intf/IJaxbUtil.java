package legend.util.intf;

import legend.intf.ICommon;

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
}
