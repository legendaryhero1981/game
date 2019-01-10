package legend.intf;

import static legend.util.StringUtil.gl;

public interface IMain extends ICommon{
    String MAIN_IL = "il";
    String MAIN_FILE = "file";
    String MAIN_RUN = "run";
    String MAIN_EOC = "eoc";
    String MAIN_KCD = "kcd";
    String MAIN_POE = "poe";
    String HELP_MAIN = APP_INFO + "参数说明：" + gl(2)
    + "game il|file|run|eoc|kcd|poe" + gl(2)
    + "命令列表：" + gl(2)
    + "il\tIL语言帮助命令，可查询和配置管理IL指令和IL语句的帮助信息。" + gl(2)
    + "file\t游戏文件处理命令，通过正则匹配批量查询和处理目录和文件。" + gl(2)
    + "run\t参数化运行游戏，通过配置文件对所有游戏可执行文件进行统一管理。" + gl(2)
    + "eoc\t神界：原罪2 汉化文件处理。" + gl(2)
    + "kcd\t天国：拯救 汉化文件和Mod整合处理。" + gl(2)
    + "poe\t永恒之柱2：死火 汉化文件和Mod文件处理。";
}
