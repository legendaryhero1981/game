package legend.game.run.intf;

import static legend.intf.ICommon.gl;
import static legend.intf.ICommon.gs;

import legend.intf.ICommon;

public interface IMain extends ICommon{
    String RUN_FILE_LOG = "./run.log";
    String RUN_FILE_CONFIG = "./run.xml";
    String TIME_SECOND_MIN = "1";
    String TIME_SECOND_MAX = "60";
    String WAIT_TIME = "10";
    String SLEEP_TIME = "1000";
    String REG_TIME = "60|[1-9]|[1-5]\\d";
    String REG_SPRT_CMD = "(?m)\n";
    String REG_SPRT_PATH = "[/" + gs(SPRT_FILE,2) + "]";
    String N_GAME_CONFIG = "游戏配置文件";
    String N_FILE_SCRIPT = "脚本文件";
    String N_EXE = "对应的name或path或exe节点";
    String N_VALIDATE = "存在id或name或path或exe为空的game节点！";
    String ST_CHOICE_ID = "请输入一个游戏ID（按回车键确认）：";
    String FILE_PREFIX = "run";
    String FILE_SUFFIX_BAT = ".bat";
    String FILE_SUFFIX_VBS = ".vbs";
    String FILE_SUFFIX_EXE = ".exe";
    String FILE_SUFFIX_LNK = ".lnk";
    String CMD_CREATE = "-c";
    String CMD_ADD = "-a";
    String CMD_DEL = "-d";
    String CMD_VIEW = "-v";
    String CMD_EXEC = "-x";
    String CMD_LINK = "-l";
    String CMD_LINK_ALL = "-la";
    String CMD_CS_RUN = "cscript \"" + PH_ARG0 + "\"";
    String CMD_VBS_SH_INIT = "dim sh" + gl(1)
    + "set sh=WScript.CreateObject(\"WScript.Shell\")";
    String CMD_VBS_SLEEP = "WScript.Sleep " + PH_ARG0;
    String CMD_VBS_RUN = "sh.Run \"" + PH_ARG0 + "\",0,true";
    String CMD_VBS_RUN_DEL = "sh.Run \"cmd /c del /q \"\"" + PH_ARG0 + "\"\">nul 2>nul\",0,true";
    String CMD_VBS_RUN_GAME = "sh.Run \"cmd /c start /high /D \"\"" + PH_ARG0 + "\"\" \"\"\"\" \"\"" + PH_ARG1 + FILE_SUFFIX_EXE + "\"\" " + PH_ARG2 + "\",0,true";
    String CMD_VBS_RUN_PROC = "sh.Run \"cmd /c wmic process where \"\"name='" + PH_ARG0 + FILE_SUFFIX_EXE + "'\"\" call SetPriority " + PH_ARG1 + "\",0,true";
    String CMD_VBS_WMI_INIT = "dim wmi,run" + gl(1)
    + "set wmi=GetObject(\"WinMgmts:\\\\.\\root\\CIMV2\")" + gl(1)
    + "set run=wmi.Execquery(\"Select * From Win32_Process Where Name='run.exe'\").ItemIndex(0)";
    String CMD_VBS_SC = "dim shortcut,path" + gl(1)
    + "path=sh.SpecialFolders(\"Desktop\")&\"" + SPRT_FILE + PH_ARG0 + FILE_SUFFIX_LNK + "\"" + gl(1)
    + "set shortcut=sh.Createshortcut(path)";
    String CMD_VBS_SC_ARG = "shortcut.Arguments=\"" + CMD_EXEC + " " + PH_ARG0 + "\"";
    String CMD_VBS_SC_IL = "shortcut.IconLocation=\"" + PH_ARG0 + SPRT_FILE + PH_ARG1 + ",0\"";
    String CMD_VBS_SC_DESC = "shortcut.Description=\"" + PH_ARG0 + "\"";
    String CMD_VBS_SC_WD = "shortcut.WorkingDirectory=\"" + PH_ARG0 + "\"";
    String CMD_VBS_SC_TP = "shortcut.TargetPath=run.ExecutablePath";
    String CMD_VBS_SC_WS = "shortcut.WindowStyle=1";
    String CMD_VBS_SC_SAVE = "shortcut.Save";
    String CMD_BAT_PROC_DEL_BY_NAME = "wmic process where \"name='" + PH_ARG0 + "'\" delete";
    String CMD_BAT_PROC_DEL_BY_PATH = "wmic process where \"executablepath='" + PH_ARG0 + "'\" delete";
    String CMD_BAT_WATCH = "setlocal enableextensions" + gl(1)
    + "setlocal enabledelayedexpansion" + gl(1)
    + ":watch" + gl(1)
    + "set pid=" + gl(1)
    + "for /f \"usebackq skip=1\" %%i in (`wmic process where \"name='" + PH_ARG0 + "'\" get processid`) do if \"!pid!\"==\"\" set pid=%%i" + gl(1)
    + "if not \"%pid%\"==\"\" (" + gl(1)
    + "choice /c y /d y /t " + PH_ARG1 + " >nul 2>nul" + gl(1)
    + "goto watch ) else (" + gl(1)
    + PH_ARG2
    + "goto quit )" + gl(1)
    + ":quit" + gl(1)
    + "exit /b 0";
    String ERR_CONFIG_NON = N_GAME_CONFIG + "\"" + RUN_FILE_CONFIG + "\"" + V_NON_EXISTS;
    String ERR_CONFIG_NUL = N_GAME_CONFIG + "\"" + RUN_FILE_CONFIG + "\"" + V_BY_NUL;
    String ERR_ID_NON = N_GAME_CONFIG + "\"" + PH_ARG0 + "\"" + N_IN + N_SPEC_ID + "\"" + PH_ARG1 + "\"" + V_NON_EXISTS;
    String ERR_ID_EXISTS = N_GAME_CONFIG + "\"" + PH_ARG0 + "\"" + N_IN + N_SPEC_ID + "\"" + PH_ARG1 + "\"" + V_ARD_EXISTS;
    String ERR_VALIDATE = N_GAME_CONFIG + "\"" + PH_ARG0 + "\"" + N_IN + N_VALIDATE;
    String ERR_EXE_NUL = N_GAME_CONFIG + "\"" + PH_ARG0 + "\"" + N_IN + N_SPEC_ID + "\"" + PH_ARG1 + "\"" + N_EXE + V_BY_NUL;
    String ERR_CREATE_FAIL = V_CRT + N_FILE_SCRIPT + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_RUN_FAIL = V_EXEC + N_FILE_SCRIPT + V_FAIL + N_ERR_INFO + PH_ARG0;
    String GAMES_COMMENT = gl(1) + gs(4) + "游戏配置集节点结构说明：" + gl(1)
    + gs(4) + "Games节点由一个唯一节点comment和多个Game节点按顺序组成，comment节点必须在最前面。" + gl(1)
    + gs(4) + "Games::comment\t游戏配置集节点结构说明，对执行游戏无影响，仅此说明而已。" + gl(1)
    + gs(4) + "Games::Game\t\t游戏配置节点，包括执行游戏的命令行参数配置及执行游戏前和执行游戏后的BAT脚本命令配置。" + gl(1)
    + gs(4) + "Game节点由comment、name、id、path、exe、args、priority、icon、agentPath、agentExe、agentArgs、before、after、beforeWait、afterWait、watchWait、watch节点按顺序组成；comment节点必须在最前面，watch节点可以有多个。" + gl(1)
    + gs(4) + "Game::comment\t\t游戏快捷方式说明，默认值同Game::name。" + gl(1)
    + gs(4) + "Game::name\t\t游戏快捷方式名称，一般使用游戏中文名称。" + gl(1)
    + gs(4) + "Game::id\t\t游戏唯一标识。" + gl(1)
    + gs(4) + "Game::path\t\t游戏可执行文件路径，也是Game::icon的路径。" + gl(1)
    + gs(4) + "Game::exe\t\t游戏可执行文件名称，不包含文件扩展名" + FILE_SUFFIX_EXE + "；若Game::agentExe非空，则优先使用Game::agentExe启动游戏。" + gl(1)
    + gs(4) + "Game::args\t\tGame::exe的命令行参数。" + gl(1)
    + gs(4) + "Game::priority\t\t游戏进程的优先级，可选值为：32（标准），64（低），128（高），256（实时），16384（低于标准），32768（高于标准）；若Game::agentExe非空且Game::agentArgs已指定优先级，则应把该节点值置空，否则优先使用该节点值。" + gl(1)
    + gs(4) + "Game::icon\t\t游戏快捷方式的图标文件完整名称（包含文件扩展名）；若为空则使用游戏可执行文件中图标。" + gl(1)
    + gs(4) + "Game::agentPath\tGame::exe的代理可执行文件路径；若为空则取值为Game::path。" + gl(1)
    + gs(4) + "Game::agentExe\t\tGame::exe的代理可执行文件名称，不包含文件扩展名" + FILE_SUFFIX_EXE + "；适用于使用游戏插件启动游戏的情况，例如：上古卷轴5的skse。" + gl(1)
    + gs(4) + "Game::agentArgs\tGame::agentExe的命令行参数。" + gl(1)
    + gs(4) + "Game::before\t\t在游戏执行前需要执行的BAT脚本命令。" + gl(1)
    + gs(4) + "Game::after\t\t在游戏执行后需要执行的BAT脚本命令。" + gl(1)
    + gs(4) + "Game::beforeWait\tGame::before命令执行完后等待beforeWait秒，再执行游戏；仅当Game::before不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。" + gl(1)
    + gs(4) + "Game::afterWait\t\t执行游戏后等待afterWait秒，再执行Game::after命令；仅当Game::after不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。"  + gl(1)
    + gs(4) + "Game::watchWait\t游戏监控进程的等待时间，每隔watchWait秒后检测一次游戏进程是否存在；仅当Game::watch不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。"  + gl(1)
    + gs(4) + "Game::watch\t\t由Game::before或Game::after脚本启动的进程的名称（例如：editplus.exe）或进程的可执行文件路径名（例如：F:/tools/EditPlus/editplus.exe），在游戏进程结束后监控程序会自动关闭之。" + gl(1) + gs(4);
    String HELP_RUN = APP_INFO + "参数说明：" + gl(2)
    + "run -c|-a|-d|-v|-x|-l|-la id path exe name [comment]" + gl(2)
    + "id\t\t游戏标识，在" + RUN_FILE_CONFIG + "文件中唯一标识一个游戏配置节点。" + gl(2)
    + "path\t\t游戏可执行文件路径。" + gl(2)
    + "exe\t\t游戏可执行文件名称（不包含扩展名" + FILE_SUFFIX_EXE + "）。" + gl(2)
    + "name\t\t游戏中文名称。" + gl(2)
    + "comment\t\t游戏快捷方式说明。" + gl(2)
    + "-c id path exe name [comment] 新建游戏配置文件" + RUN_FILE_CONFIG + "，并生成一个游戏配置节点。" + gl(2)
    + "-a id path exe name [comment] 添加一个游戏配置节点到游戏配置文件" + RUN_FILE_CONFIG + "中。" + gl(2)
    + "-d id 根据id删除游戏配置文件" + RUN_FILE_CONFIG + "中对应的一个游戏配置节点。" + gl(2)
    + "-v 显示游戏配置文件" + RUN_FILE_CONFIG + "中所有的游戏配置节点的id列表，显示格式为：id\t\tcomment。" + gl(2)
    + "-x [id] 根据id执行游戏配置文件" + RUN_FILE_CONFIG + "中对应的游戏；如果不指定id程序则会先显示id列表（同-v），再提示输入一个id，根据id执行对应的游戏。" + gl(2)
    + "-l [id] 根据id获得" + RUN_FILE_CONFIG + "中对应的游戏，并创建游戏快捷方式到桌面；如果不指定id程序则会先显示id列表（同-v），再提示输入一个id，根据id创建游戏快捷方式。" + gl(2)
    + "-la 批量创建游戏配置文件" + RUN_FILE_CONFIG + "中所有游戏的快捷方式到桌面。" + gl(2)
    + "示例：" + gl(2)
    + "run -c ew \"F:/games/The Evil Within\" EvilWithin 恶灵附身：开发者模式" + gl(2)
    + "run -a ew2 \"F:/games/The Evil Within 2\" TEW2 恶灵附身2：开发者模式" + gl(2)
    + "run -a lotf \"F:/games/Lords Of The Fallen/bin\" LordsOfTheFallen 堕落之王：开发者模式" + gl(2)
    + "run -a sg \"F:/games/The Surge/bin\" TheSurge 迸发：开发者模式" + gl(2)
    + "run -a skse \"F:/games/Skyrim Special Edition\" SkyrimSE 上古卷轴5：开发者模式" + gl(2)
    + "run -a poe2 \"F:/games/Pillars of Eternity II\" PillarsOfEternityII 永恒之柱2" + gl(2)
    + "run -a poe2-d \"F:/games/Pillars of Eternity II\" PillarsOfEternityII 永恒之柱2：开发者模式" + gl(2)
    + "run -d ew2" + gl(2)
    + "run -v" + gl(2)
    + "run -x" + gl(2)
    + "run -x ew" + gl(2)
    + "run -l" + gl(2)
    + "run -l ew" + gl(2)
    + "run -la";
}
