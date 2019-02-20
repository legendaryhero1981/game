package legend.util.intf;

import static legend.util.StringUtil.gs;

import legend.intf.ICommon;

public interface IILCode extends ICommon{
    String SPRT_LINE_NUMBER = "-";
    String REG_LINE_NUMBER = "([1-9]\\d*)(" + SPRT_LINE_NUMBER + "([1-9]\\d*))?";
    String ERR_ILCODE_NON = "ILCodes节点下必须至少有一个ILCode节点！";
    String ERR_QUERY_REGEX = "当ILCodes::mode为" + MODE_NATIVE + "且ILCode::processingMode非" + MODE_NATIVE + "时，ILCode::queryRegex不能为空！";
    String ERR_CODE_REGEX = "当ILCodes::mode为" + MODE_NATIVE + "且ILCode::processingMode为" + MODE_REPL + "时，ILCode::codeRegex不能为空且最多只能指定2个正则表达式！";
    String ERR_LINE_NUM_FORMAT = "ILCode::lineNumber为空或格式错误！";
    String ERR_LINE_NUM_VAL_LESS = "ILCode::lineNumber=" + PH_ARG0 + "中的起始行号必须小于等于终止行号！";
    String ERR_LINE_NUM_VAL_EQUAL = "ILCode::lineNumber=" + PH_ARG0 + "中的起始行号必须等于终止行号！";
    String ERR_LINE_NUM_VAL_MIN = "ILCode::lineNumber=" + PH_ARG0 + "，最小值必须为1！";
    String ERR_LINE_NUM_VAL_MAX = "ILCode::lineNumber=" + PH_ARG0 + "，最大值必须等于IL源文件中的最大行号！";
    String ERR_LINE_NUM_ADJION_ADD = "当前ILCode::lineNumber=" + PH_ARG0 + "中的起始行号必须与上一个ILCode::lineNumber=" + PH_ARG1 + "中的终止行号相等！";
    String ERR_LINE_NUM_ADJION_OTHER = "当前ILCode::lineNumber=" + PH_ARG0 + "中的起始行号必须与上一个ILCode::lineNumber=" + PH_ARG1 + "中的终止行号+1相等！";
    String ILCODES_COMMENT = "\n" + gs(4) + "ILCodes配置集节点结构说明：\n"
    + gs(4) + "ILCodes节点由一个唯一节点comment、一个唯一节点mode和多个ILCode节点按顺序组成，comment节点必须在最前面。\n"
    + gs(4) + "ILCodes::comment" + gs(8) + "ILCodes配置集节点结构说明，对IL文件的修改无影响，仅此说明而已。\n"
    + gs(4) + "ILCodes::mode" + gs(11) + "对IL源文件内容的处理模式，取值范围为：" + MODE_NATIVE + "," + MODE_REPL + "，默认值为" + MODE_NATIVE + "；取" + MODE_NATIVE + "表示先根据ILCodes::ILCode查询定位IL源文件中需要修改的内容，再自动生成已修改的IL源文件；取" + MODE_REPL + "表示根据ILCodes::ILCode直接自动生成已修改的IL源文件。\n"
    + gs(4) + "ILCodes::ILCode" + gs(9) + "ILCode配置节点，包括对IL代码的处理模式和修改IL代码片段所需的相关参数等等。\n"
    + gs(4) + "ILCode节点由processingMode、lineNumber、codeDesc、queryRegex、codeRegex、codeFragment节点按顺序组成；processingMode节点必须在最前面。\n"
    + gs(4) + "ILCode::processingMode" + gs(2) + "在自动生成已修改的IL源文件时对IL代码的处理模式，取值范围为：" + MODE_NATIVE + "," + MODE_REPL + "," + MODE_ADD + "，默认值为" + MODE_NATIVE + "；取" + MODE_NATIVE + "表示提取IL源文件的原始数据；取" + MODE_REPL + "表示提取ILCode::codeFragment作为替换数据；取" + MODE_ADD + "表示提取ILCode::codeFragment作为新增数据。\n"
    + gs(4) + "ILCode::lineNumber" + gs(6) + "IL源文件中IL代码片段的起止行号，匹配的正则表达式为：；如果要修改的代码只有1行，可以只指定一个行号，即1与1-1等效。\n"
    + gs(4) + "ILCode::codeDesc" + gs(8) + "需要修改的IL代码片段的功能描述。\n"
    + gs(4) + "ILCode::queryRegex" + gs(6) + "在IL源文件中查询定位要修改的IL代码片段时所需的正则查询表达式；仅当ILCode::processingMode不为" + MODE_NATIVE + "时有效；若ILCode::processingMode为" + MODE_REPL + "，将继续向上回溯查询ILCode::codeRegex以便于精确定位。\n"
    + gs(4) + "ILCode::codeRegex" + gs(6) + "在IL源文件中查询定位要修改的IL代码片段时所需的正则查询表达式；仅当ILCode::processingMode为" + MODE_REPL + "时有效。\n"
    + gs(4) + "ILCode::codeFragment" + gs(4) + "在IL源文件中ILCode::lineNumber位置处需要被修改的IL代码片段。\n" + gs(4);
}
