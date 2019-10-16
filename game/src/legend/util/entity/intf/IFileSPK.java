package legend.util.entity.intf;

import static java.util.regex.Pattern.compile;
import static legend.util.StringUtil.gs;

import java.util.regex.Pattern;

import legend.util.intf.IFileUtil;

public interface IFileSPK extends IFileUtil{
    String REG_SPK_SIZE = "\\A" + REG_NUM_NATURAL + "$";
    String REG_SPK_SIZE_EXPR = "\\A(" + REG_NUM_NATURAL + ")([-,;](" + REG_NUM_NATURAL + "))?$";
    Pattern PTN_SPK_SIZE = compile(REG_SPK_SIZE);
    Pattern PTN_SPK_SIZE_EXPR = compile(REG_SPK_SIZE_EXPR);
    String N_SPK_CONF = EXT_SPK + "编码文件的配置文件";
    String N_SPKF_BODY_INFO = "SPKFormat::BodyInfo节点下的headerSize或headerFlag或filePathExpr或fileSizeExpr子节点值";
    String N_SPKF_LIST_INFO = "SPKFormat::ListInfo节点下的headerSize或headerFlag或filePathExpr或fileSizeExpr子节点值";
    String N_SPKF_TAIL_INFO = "SPKFormat::TailInfo节点下的headerSize或headerFlag子节点值";
    String N_STCF_HEADER_INFO = "STCFormat::HeaderInfo节点下的headerSize或headerFlag子节点值";
    String N_STCF_BODY_INFO = "STCFormat::BodyInfo节点下的headerSize或fileSizeExpr或fileStartPosExpr子节点值";
    String N_STCF_LIST_INFO = "STCFormat::ListInfo节点下的headerFlag或filePathExpr子节点值";
    String ST_FILE_SPK_CONF = "已生成" + N_SPK_CONF + S_DQM + CONF_FILE_SPK + S_DQM + S_BANG;
    String ERR_CONF_NON = N_SPK_CONF + S_DQM_L + PH_ARG0 + S_DQM_R + V_NON_EXISTS;
    String ERR_CONF_SPKC_NON = "unpackPath或filePath或path3或fileName或queryRegex节点值" + V_BY_NUL;
    String ERR_CONF_SPKH_NON = PH_ARG0 + V_BY_NUL;
    String ERR_CONF_SPKH_EXPR = PH_ARG0 + "节点值对应的正则表达式描述" + V_ERR;
    String FILE_SPK_COMMENT = "\n" + gs(4) + "FileSPK配置节点结构说明：\n"
    + gs(4) + "FileSPK节点由comment、SPKCode节点按顺序组成，comment节点必须在最前面，SPKCode节点可以有多个。\n"
    + gs(4) + "FileSPK::comment" + gs(24) + "FileSPK配置节点结构说明，对文件整合无影响，仅此说明而已。\n"
    + gs(4) + "FileSPK::SPKCode" + gs(24) + "SPK编码对象，包含了自动修改" + EXT_SPK + "文件和其相对应的同名" + EXT_STC + "文件所需的所有参数。\n"
    + gs(4) + "SPKCode节点由节点unpackPath、filePath、fileName、queryRegex、SPKFormat、STCFormat按顺序组成。\n"
    + gs(4) + "SPKCode::unpackPath" + gs(21) + EXT_SPK + "文件解包路径，也是SPKCode::queryRegex进行正则查询匹配的输入路径。\n"
    + gs(4) + "SPKCode::filePath" + gs(23) + EXT_SPK + "文件和其相对应的同名" + EXT_STC + "文件路径，与SPKCode::fileName联合以用于读取文件内容。\n"
    + gs(4) + "SPKCode::fileName" + gs(23) + EXT_SPK + "文件和其相对应的同名" + EXT_STC + "文件名称，与SPKCode::filePath联合以用于读取文件内容。\n"
    + gs(4) + "SPKCode::queryRegex" + gs(21) + "文件名正则查询表达式，用于查询SPKCode::unpackPath路径下的所有文件；支持引用表达式和特殊字符占位符表达式（参见file命令参数说明）。\n"
    + gs(4) + "SPKCode::SPKFormat" + gs(22) + EXT_SPK + "文件数据格式对象，用于解析该文件的数据结构。\n"
    + gs(4) + "SPKFormat节点由节点BodyInfo、ListInfo、TailInfo按顺序组成，描述了整个" + EXT_SPK + "文件的数据结构。\n"
    + gs(4) + "SPKFormat::BodyInfo" + gs(21) + EXT_SPK + "文件中对象主体信息。\n"
    + gs(4) + "SPKFormat::BodyInfo节点由节点headerSize、headerFlag、filePathExpr、fileSizeExpr按顺序组成。\n"
    + gs(4) + "SPKFormat::BodyInfo::headerSize" + gs(9) + "对象主体的头部大小（以字节为单位的自然数，下同）。\n"
    + gs(4) + "SPKFormat::BodyInfo::headerFlag" + gs(9) + "对象主体的头部起始标志。\n"
    + gs(4) + "SPKFormat::BodyInfo::filePathExpr" + gs(7) + "对象主体中文件相对路径名表达式。\n"
    + gs(4) + "SPKFormat::BodyInfo::fileSizeExpr" + gs(7) + "对象主体中文件大小表达式。\n"
    + gs(4) + "SPKFormat::ListInfo" + gs(21) + EXT_SPK + "文件中对象列表信息。\n"
    + gs(4) + "SPKFormat::ListInfo节点由节点headerSize、headerFlag、filePathExpr、fileSizeExpr按顺序组成。\n"
    + gs(4) + "SPKFormat::ListInfo::headerSize" + gs(9) + "对象列表的头部大小。\n"
    + gs(4) + "SPKFormat::ListInfo::headerFlag" + gs(9) + "对象列表的头部起始标志。\n"
    + gs(4) + "SPKFormat::ListInfo::filePathExpr" + gs(7) + "对象列表中文件相对路径名表达式。\n"
    + gs(4) + "SPKFormat::ListInfo::fileSizeExpr" + gs(7) + "对象列表中文件大小表达式。\n"
    + gs(4) + "SPKFormat::TailInfo" + gs(21) + EXT_SPK + "文件中文件尾部信息。\n"
    + gs(4) + "SPKFormat::TailInfo节点由节点headerSize、headerFlag按顺序组成。\n"
    + gs(4) + "SPKFormat::TailInfo::headerSize" + gs(9) + "文件尾部的头部大小。\n"
    + gs(4) + "SPKFormat::TailInfo::headerFlag" + gs(9) + "文件尾部的头部起始标志。\n"
    + gs(4) + "SPKCode::STCFormat" + gs(22) + EXT_STC + "文件数据格式对象，用于解析该文件的数据结构。\n"
    + gs(4) + "STCFormat节点由节点HeaderInfo、BodyInfo、ListInfo按顺序组成，描述了整个" + EXT_STC + "文件的数据结构。\n"
    + gs(4) + "STCFormat::HeaderInfo" + gs(19) + EXT_STC + "文件中文件头部信息。\n"
    + gs(4) + "STCFormat::HeaderInfo节点由节点headerSize、headerFlag按顺序组成。\n"
    + gs(4) + "STCFormat::HeaderInfo::headerSize" + gs(7) + "文件头部的头部大小。\n"
    + gs(4) + "STCFormat::HeaderInfo::headerFlag" + gs(7) + "文件头部的头部起始标志。\n"
    + gs(4) + "STCFormat::BodyInfo" + gs(21) + EXT_STC + "文件中对象主体信息。\n"
    + gs(4) + "STCFormat::BodyInfo节点由节点headerSize、fileSizeExpr、fileStartPosExpr按顺序组成。\n"
    + gs(4) + "STCFormat::BodyInfo::headerSize" + gs(9) + "对象主体的头部大小。\n"
    + gs(4) + "STCFormat::BodyInfo::fileSizeExpr" + gs(7) + "对象主体中文件大小表达式。\n"
    + gs(4) + "STCFormat::BodyInfo::fileStartPosExpr" + gs(3) + "对象主体中文件起始位置表达式。\n"
    + gs(4) + "STCFormat::ListInfo" + gs(21) + EXT_STC + "文件中对象列表信息。\n"
    + gs(4) + "STCFormat::ListInfo节点由节点headerFlag、filePathExpr按顺序组成。\n"
    + gs(4) + "STCFormat::ListInfo::headerFlag" + gs(9) + "对象列表的头部起始标志。\n"
    + gs(4) + "STCFormat::ListInfo::filePathExpr" + gs(7) + "对象列表中文件相对路径名表达式。\n" + gs(4);
}
