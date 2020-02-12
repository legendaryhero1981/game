package legend.util.entity.intf;

import static legend.util.StringUtil.getAppPath;
import static legend.util.StringUtil.gs;

import legend.util.intf.IFileUtil;

public interface IZip7 extends IFileUtil{
    String EXECUTABLE_PATH = getAppPath() + "/tools/7-Zip/7z.exe";
    String N_7ZIP_CONF = "7-Zip任务处理配置文件";
    String ST_FILE_SPK_CONF = V_GNRT + N_7ZIP_CONF + S_DQM + CONF_FILE_7ZIP + S_DQM + S_BANG;
    String FILE_7ZIP_COMMENT = "\n" + gs(4) + "Zip7配置节点结构说明：\n"
    + gs(4);
}
