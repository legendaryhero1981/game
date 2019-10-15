package legend.util.entity.intf;

import static legend.util.StringUtil.gs;

import legend.util.intf.IFileUtil;

public interface IFileSPK extends IFileUtil{
    String N_SPK_CONFIG = EXT_SPK + "编码文件的修改配置文件";
    String N_INVALIDATE = "path或path2或path3或mergeExecutablePath或queryRegex节点为空！";
    String ST_FILE_SPK_CONF = "已生成" + N_SPK_CONFIG + S_DQM + CONF_FILE_SPK + S_DQM + S_BANG;
    String ERR_CONFIG_NON = N_SPK_CONFIG + S_DQM_L + PH_ARG0 + S_DQM_R + V_NON_EXISTS;
    String ERR_CONFIG_INVALIDATE = N_SPK_CONFIG + S_DQM_L + PH_ARG0 + S_DQM_R + N_IN + N_INVALIDATE;
    String FILE_SPK_COMMENT = "\n" + gs(4) + "FileMerge配置节点结构说明：\n"
    + gs(4) + "FileMerge节点由comment、path、path2、path3、mergeExecutablePath、queryRegex、pathMd5、Merges节点按顺序组成，comment节点必须在最前面。\n"
    + gs(4) + "FileMerge::comment" + gs(18) + "FileMerge配置节点结构说明，对文件整合无影响，仅此说明而已。\n"
    + gs(4) + "Merge::md5" + gs(26) + "\n"+ gs(4);
}
