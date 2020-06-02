package legend.util.entity.intf;

import static java.util.regex.Pattern.compile;
import static legend.util.StringUtil.gs;

import java.util.regex.Pattern;

import legend.util.intf.IFileUtil;

public interface IILCode extends IFileUtil{
    int SIZE_IL_HEADER = 7;
    int SIZE_IL_TAIL = 2;
    int SIZE_IL_PARTITION = 10000;
    int SIZE_IL_PARTITION_MIN = 100;
    String SPRT_LINE_NUMBER = "-";
    String REG_LINE_NUMBER = "(" + REG_NUM_NATURAL + ")(" + SPRT_LINE_NUMBER + "(" + REG_NUM_NATURAL + "))?";
    String N_IL_CONF = EXT_IL + "编码文件的配置文件";
    String ST_FILE_IL_MISMATCH = "IL配置文件与IL源文件内容不匹配！";
    String ST_FILE_IL_CONF = V_GNRT + N_IL_CONF + S_DQM + CONF_FILE_IL + S_DQM + S_BANG;
    String ERR_ILCODE_NUL = "ILCodes节点下必须至少有一个ILCode节点！";
    String ERR_QUERY_REGEX = "当ILCodes::mode为" + MODE_NATIVE + "且ILCode::processingMode非" + MODE_NATIVE + "时，ILCode::queryRegex不能为空！";
    String ERR_CODE_REGEX = "当ILCodes::mode为" + MODE_NATIVE + "且ILCode::processingMode为" + MODE_REPL + "时，ILCode::codeRegex不能为空且最多只能指定2个正则表达式！";
    String ERR_LINE_NUM_FORMAT = "ILCode::lineNumber为空或格式错误！";
    String ERR_LINE_NUM_VAL_LESS = "ILCode::lineNumber=" + PH_ARGS0 + "中的起始行号必须小于等于终止行号！";
    String ERR_LINE_NUM_VAL_EQUAL = "ILCode::lineNumber=" + PH_ARGS0 + "中的起始行号必须等于终止行号！";
    String ERR_LINE_NUM_VAL_MIN = "ILCode::lineNumber=" + PH_ARGS0 + "，行号最小值必须为1！";
    String ERR_LINE_NUM_VAL_MAX = "ILCode::lineNumber=" + PH_ARGS0 + "，行号最大值必须等于IL源文件中的最大行号" + PH_ARGS1 + "！";
    String ERR_LINE_NUM_ADJION_ADD = "当前ILCode::lineNumber=" + PH_ARGS0 + "中的起始行号必须与上一个ILCode::lineNumber=" + PH_ARGS1 + "中的终止行号相等！";
    String ERR_LINE_NUM_ADJION_OTHER = "当前ILCode::lineNumber=" + PH_ARGS0 + "中的起始行号必须与上一个ILCode::lineNumber=" + PH_ARGS1 + "中的终止行号+1相等！";
    String ILCODES_COMMENT = "\n" + gs(4) + "ILCodes配置集节点结构说明：\n"
    + gs(4) + "ILCodes节点由节点comment、mode、header、tail、partition和多个ILCode节点按顺序组成，comment节点必须在最前面。\n"
    + gs(4) + "ILCodes::comment" + gs(12) + "ILCodes配置集节点结构说明，对IL文件的修改无影响，仅此说明而已。\n"
    + gs(4) + "ILCodes::mode" + gs(15) + "对IL源文件内容的处理模式，取值范围为：" + MODE_NATIVE + "," + MODE_REPL + "，默认值为" + MODE_NATIVE + "；取" + MODE_NATIVE + "表示先根据ILCodes::ILCode节点下的ILCode::queryRegex节点和ILCode::codeRegex节点值查询定位IL源文件中需要修改的内容（此时将忽略ILCode::processingMode为0的所有ILCode节点，且会更新ILCode::processingMode不为0的所有ILCode节点的ILCode::lineNumber节点值），再自动生成已修改的IL源文件；取" + MODE_REPL + "表示根据ILCodes::ILCode节点下的ILCode::lineNumber节点值直接自动生成已修改的IL源文件。\n"
    + gs(4) + "ILCodes::header" + gs(13) + "IL源文件内容的头部信息行数，表示查询时忽略掉前header行，仅当mode节点值为" + MODE_NATIVE + "时有效；取值范围为：0~" + Integer.MAX_VALUE + "，不指定或超过取值范围则取默认值" + SIZE_IL_HEADER + "。\n"
    + gs(4) + "ILCodes::tail" + gs(15) + "IL源文件内容的尾部信息行数，表示查询时忽略掉后tail行，仅当mode节点值为" + MODE_NATIVE + "时有效；取值范围为：0~" + Integer.MAX_VALUE + "，不指定或超过取值范围则取默认值" + SIZE_IL_TAIL + "。\n"
    + gs(4) + "ILCodes::partition" + gs(10) + "IL源文件内容的分区行数，表示查询时按照每个分区partition行进行并行查询，仅当mode节点值为" + MODE_NATIVE + "时有效；取值范围为：" + SIZE_IL_PARTITION_MIN + "~" + Integer.MAX_VALUE + "，不指定或超过取值范围则取默认值" + SIZE_IL_PARTITION + "。\n"
    + gs(4) + "ILCodes::ILCode" + gs(13) + "ILCode配置节点，包括对IL代码的处理模式和修改IL代码片段所需的相关参数等等。\n"
    + gs(4) + "ILCode节点由processingMode、quoteMode、lineNumber、codeDesc、queryRegex、codeRegex、codeFragment节点按顺序组成，processingMode节点必须在最前面。\n"
    + gs(4) + "ILCode::processingMode" + gs(6) + "在自动生成已修改的IL源文件时对IL代码的处理模式，取值范围为：" + MODE_NATIVE + "," + MODE_REPL + "," + MODE_ADD + "，默认值为" + MODE_NATIVE + "；取" + MODE_NATIVE + "表示提取IL源文件的原始数据；取" + MODE_REPL + "表示提取ILCode::codeFragment作为替换数据，此时ILCode::queryRegex节点和ILCode::codeRegex节点都不能为空，且ILCode::codeRegex节点值最多只能指定2个正则查询表达式，分别匹配起始代码行和终止代码行，只指定1个则表示只唯一匹配一行待替换代码；取" + MODE_ADD + "表示提取ILCode::codeFragment作为新增数据，此时ILCode::queryRegex节点不能为空，且ILCode::codeRegex节点值将被忽略。\n"
    + gs(4) + "ILCode::quoteMode" + gs(11) + "对ILCode::queryRegex节点和ILCode::codeRegex节点表示的正则查询表达式是否启用引用模式，取值范围为：" + MODE_NATIVE + "," + MODE_REPL + "，默认值为" + MODE_NATIVE + "；取" + MODE_NATIVE + "表示不启用引用模式；取"  + MODE_REPL + "表示启用引用模式，此时正则查询表达式将被当成无特殊含义的普通字符串对待。\n"
    + gs(4) + "ILCode::lineNumber" + gs(10) + "IL源文件中IL代码片段的起止行号，匹配的正则表达式为：" + REG_LINE_NUMBER + "；取值范围：最小值为1，最大值为IL源文件的最大行号；如果要修改的代码只有1行，可以只指定一个行号，即1与1-1等效。\n"
    + gs(4) + "ILCode::codeDesc" + gs(12) + "需要修改的IL代码片段的功能描述。\n"
    + gs(4) + "ILCode::queryRegex" + gs(10) + "在IL源文件中查询定位要修改的IL代码片段时所需的正则查询表达式；数据格式为每行一个表达式，忽略表达式首尾的空白字符和所有的空白行；仅当ILCode::processingMode不为" + MODE_NATIVE + "时有效；若ILCode::processingMode为" + MODE_REPL + "，将继续向上回溯查询ILCode::codeRegex以正确定位。\n"
    + gs(4) + "ILCode::codeRegex" + gs(11) + "在IL源文件中查询定位要修改的IL代码片段时所需的正则查询表达式；数据格式为每行一个表达式，忽略表达式首尾的空白字符和所有的空白行，最多只能指定2个表达式以匹配起始和终止IL代码行；仅当ILCode::processingMode为" + MODE_REPL + "时有效。\n"
    + gs(4) + "ILCode::codeFragment" + gs(8) + "在IL源文件中ILCode::lineNumber位置处需要被替换或新增的IL代码片段；忽略IL代码片段中所有的空白行。\n" + gs(4);
    Pattern PTRN_LINE_NUMBER = compile(REG_LINE_NUMBER);
}
