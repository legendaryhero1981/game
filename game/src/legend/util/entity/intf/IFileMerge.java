package legend.util.entity.intf;

import static legend.util.StringUtil.gs;

import legend.util.intf.IFileUtil;

public interface IFileMerge extends IFileUtil{
    String N_MEG_CONF = "文件整合配置文件";
    String N_MEG_NON = "path或path2或path3或mergeExecutablePath或queryRegex节点";
    String ST_FILE_MERGE_CONF = V_GNRT + N_MEG_CONF + S_DQM + CONF_FILE_MERGE + S_DQM + S_BANG;
    String ERR_MEG_NODE_NON = N_MEG_CONF + S_DQM_L + PH_ARGS0 + S_DQM_R + N_IN + N_MEG_NON + V_BY_NUL + S_BANG;
    String FILE_MERGE_COMMENT = "\n" + gs(4) + "FileMerge配置节点结构说明：\n"
    + gs(4) + "FileMerge节点由comment、path、path2、path3、mergeExecutablePath、queryRegex、pathMd5、Merges节点按顺序组成，comment节点必须在最前面。\n"
    + gs(4) + "FileMerge::comment" + gs(18) + "FileMerge配置节点结构说明，对文件整合无影响，仅此说明而已。\n"
    + gs(4) + "FileMerge::path" + gs(21) + "第一个文件输入目录的绝对路径，一般设置为最新版本修改文件的目录，同时也是保存最终整合文件的目录。\n"
    + gs(4) + "FileMerge::path2" + gs(20) + "第二个文件输入目录的绝对路径，一般设置为最新版本原始文件的目录。\n"
    + gs(4) + "FileMerge::path3" + gs(20) + "第三个文件输入目录的绝对路径，一般设置为上一版本原始文件的目录。\n"
    + gs(4) + "FileMerge::mergeExecutablePath" + gs(6) + "文件整合工具KDiff3的可执行文件路径名，负责三方文件整合的具体执行。\n"
    + gs(4) + "FileMerge::queryRegex" + gs(15) + "文件查询正则表达式，用于查询FileMerge::path、FileMerge::path2、FileMerge::path3三个目录中所有匹配的同名文件；支持引用表达式和特殊字符占位符表达式（参见file命令参数说明）。\n"
    + gs(4) + "FileMerge::pathMd5" + gs(18) + "文件输入目录的MD5码，由程序根据FileMerge::path、FileMerge::path2、FileMerge::path3三个节点值自动生成；每次执行命令时程序会检查这三个节点值是否被修改，若已修改则会清空FileMerge::Merges节点值，并且更新MD5码。\n"
    + gs(4) + "FileMerge::Merges" + gs(19) + "文件整合对象集合，包含程序最近一次执行文件整合命令后所有的文件整合对象。\n"
    + gs(4) + "Merges节点由多个Merge节点组成，可以为空。\n"
    + gs(4) + "Merges::Merge" + gs(23) + "文件整合对象，包含整合文件的相关数据。\n"
    + gs(4) + "Merge节点由path、md5节点按顺序组成。\n"
    + gs(4) + "Merge::path" + gs(25) + "整合文件的相对路径名。\n"
    + gs(4) + "Merge::md5" + gs(26) + "文件内容对应的MD5码，由程序根据FileMerge::queryRegex查询获得的FileMerge::path2、FileMerge::path3目录中同名文件的文件内容自动生成；每次执行命令时程序都会检查这两个同名文件的文件内容是否已修改，若是则会更新MD5码并进行文件整合，否则会跳过该文件的整合操作。\n"+ gs(4);
}
