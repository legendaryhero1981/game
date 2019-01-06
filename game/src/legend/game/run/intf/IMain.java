package legend.game.run.intf;

import static legend.intf.ICommon.gl;
import static legend.intf.ICommon.gs;

import legend.intf.ICommon;

public interface IMain extends ICommon{
    long SLEEP_TIME = 500;
    String RUN_FILE_LOG = "./run.log";
    String RUN_FILE_CONFIG = "./run.xml";
    String EXE_JAVA = "java.exe";
    String EXE_RUN = "run.exe";
    String BAT_RUN = "run.bat";
    String MODULE_RUN = "legend/legend.game.run.Main";
    String TIME_SECOND_MIN = "1";
    String TIME_SECOND_MAX = "60";
    String WAIT_TIME = "10";
    String REG_TIME = "60|[1-9]|[1-5]\\d";
    String REG_SPRT_CMD = "(?m)\n";
    String REG_SPRT_PATH = "[/" + gs(SPRT_FILE,2) + "]";
    String REG_PATH_NAME = "(.*" + REG_SPRT_PATH + ")(.*)";
    String N_GAME_CONFIG = "游戏配置文件";
    String N_FILE_SCRIPT = "脚本文件";
    String N_EXE = "对应的name或path或exe节点";
    String N_GAME_REPEAT = "id相同的game节点！";
    String N_GAME_INVALIDATE = "id或name或path或exe为空的game节点！";
    String ST_REPEAT_ID = "找到重复的游戏ID：" + PH_ARG0;
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
    String CMD_KILL = "-k";
    String CMD_LINK = "-l";
    String CMD_LINK_ALL = "-la";
    String CMD_CS_RUN = "wscript \"" + PH_ARG0 + "\"";
    String CMD_VBS_SH_INIT = "dim sh" + gl(1) + "set sh=WScript.CreateObject(\"WScript.Shell\")";
    String CMD_VBS_SLEEP = "WScript.Sleep " + PH_ARG0;
    String CMD_VBS_RUN = "sh.Run \"" + PH_ARG0 + "\",0,true";
    String CMD_VBS_RUN_DEL = "sh.Run \"cmd /c del /q \"\"" + PH_ARG0 + "\"\">nul 2>nul\",0,true";
    String CMD_VBS_RUN_GAME = "sh.Run \"cmd /c start /high /D \"\"" + PH_ARG0 + "\"\" " + gs("\"",4) + " \"\"" + PH_ARG1 + FILE_SUFFIX_EXE + "\"\" " + PH_ARG2 + "\",0,true";
    String CMD_VBS_RUN_AGENT = "sh.Run \"cmd /c start /high /D \"\"" + PH_ARG0 + "\"\" " + gs("\"",4) + " \"\"" + PH_ARG1 + "\"\" " + PH_ARG2 + "\",0,true";
    String CMD_VBS_RUN_PROC = "sh.Run \"cmd /c wmic process where \"\"name='" + PH_ARG0 + FILE_SUFFIX_EXE + "'\"\" call SetPriority " + PH_ARG1 + "\",0,true";
    String CMD_VBS_WMI_INIT = "dim wmi" + gl(1) + "set wmi=GetObject(\"winmgmts:{impersonationLevel=impersonate}!\\\\.\\root\\cimv2\")";
    String CMD_VBS_PROC_RUN = CMD_VBS_WMI_INIT + gl(1)
    + "dim processes,target" + gl(1)
    + "set processes=wmi.ExecQuery(\"select * from win32_process where name='" + EXE_RUN + "'\")" + gl(1)
    + "if processes.count=0 then" + gl(1)
    + "set processes=wmi.ExecQuery(\"select * from win32_process where name='" + EXE_JAVA + "' and commandline like '%" + MODULE_RUN + "%'\")" + gl(1)
    + "if processes.count=0 then" + gl(1) + "WScript.Quit" + gl(1) + "end if" + gl(1)
    + "dim regex,matches" + gl(1)
    + "set regex=New RegExp" + gl(1)
    + "regex.pattern=\".+" + gs(SPRT_FILE,2) + "\"" + gl(1)
    + "set matches=regex.Execute(processes.ItemIndex(0).CommandLine)" + gl(1)
    + "target=matches(0)&\"" + BAT_RUN + "\"" + gl(1)
    + "else target=processes.ItemIndex(0).ExecutablePath" + gl(1)
    + "end if";
    String CMD_VBS_PROC_GAME = "set games=wmi.ExecQuery(\"select * from win32_process where name='" + PH_ARG0 + FILE_SUFFIX_EXE + "'\")";
    String CMD_VBS_GAME_PRIORITY = CMD_VBS_WMI_INIT + gl(1)
    + "dim games" + gl(1) + CMD_VBS_PROC_GAME + gl(1) + "games.ItemIndex(0).SetPriority " + PH_ARG1;
    String CMD_VBS_WATCH_TERMINATE = "for each watch in wmi.ExecQuery(wql)" + gl(1) + "watch.Terminate" + gl(1) + "next";
    String CMD_VBS_GAME_KILL = CMD_VBS_WMI_INIT + gl(1)
    + "wql=\"select * from win32_process where name='" + PH_ARG0 + FILE_SUFFIX_EXE + "'\"" + gl(1)
    + CMD_VBS_WATCH_TERMINATE;
    String CMD_VBS_GAME_WATCH = "while games.Count>0" + gl(1) + "WScript.Sleep " + PH_ARG0 + gl(1)
    + "set games=wmi.ExecQuery(\"select * from win32_process where name='" + PH_ARG1 + FILE_SUFFIX_EXE + "'\")" + gl(1)
    + "Wend" + gl(1)
    + "dim names,paths,wql" + gl(1)
    + "names=Split(\"" + PH_ARG2 + "\",\"" + SPRT_CMD + "\")" + gl(1)
    + "paths=Split(\"" + PH_ARG3 + "\",\"" + SPRT_CMD + "\")" + gl(1)
    + "if Ubound(names)>0 then" + gl(1)
    + "wql=\"select * from win32_process where name='\"&names(0)&\"'\"" + gl(1)
    + "for i=1 to Ubound(names)" + gl(1) + "wql=wql&\" or name='\"&names(i)&\"'\"" + gl(1) + "next" + gl(1)
    + CMD_VBS_WATCH_TERMINATE + gl(1)
    + "end if" + gl(1)
    + "if Ubound(paths)>0 then" + gl(1)
    + "wql=\"select * from win32_process where executablepath='\"&paths(0)&\"'\"" + gl(1)
    + "for i=1 to Ubound(paths)" + gl(1) + "wql=wql&\" or executablepath='\"&paths(i)&\"'\"" + gl(1) + "next" + gl(1)
    + CMD_VBS_WATCH_TERMINATE + gl(1)
    + "end if";
    String CMD_VBS_SC_INIT = CMD_VBS_PROC_RUN + gl(1) + "dim shortcut";
    String CMD_VBS_SC_CRT = "set shortcut=sh.CreateShortcut(sh.SpecialFolders(\"Desktop\")&\"" + SPRT_FILE + PH_ARG0 + FILE_SUFFIX_LNK + "\")";
    String CMD_VBS_SC_ARG = "shortcut.Arguments=\"" + CMD_EXEC + " " + PH_ARG0 + "\"";
    String CMD_VBS_SC_IL = "shortcut.IconLocation=\"" + PH_ARG0 + SPRT_FILE + PH_ARG1 + ",0\"";
    String CMD_VBS_SC_DESC = "shortcut.Description=\"" + PH_ARG0 + "\"";
    String CMD_VBS_SC_WD = "shortcut.WorkingDirectory=\"" + PH_ARG0 + "\"";
    String CMD_VBS_SC_TP = "shortcut.TargetPath=target";
    String CMD_VBS_SC_WS = "shortcut.WindowStyle=1";
    String CMD_VBS_SC_SAVE = "shortcut.Save";
    String CMD_BAT_PROC_DEL_BY_NAME = "wmic process where \"name='" + PH_ARG0 + "'\" delete";
    String CMD_BAT_PROC_DEL_BY_PATH = "wmic process where \"executablepath='" + PH_ARG0 + "'\" delete";
    String CMD_BAT_GAME_WATCH = "setlocal enableextensions" + gl(1) + "setlocal enabledelayedexpansion" + gl(1)
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
    String ERR_CONFIG_NON = N_GAME_CONFIG + S_QUOTATION_L + RUN_FILE_CONFIG + S_QUOTATION_R + V_NON_EXISTS;
    String ERR_CONFIG_NUL = N_GAME_CONFIG + S_QUOTATION_L + RUN_FILE_CONFIG + S_QUOTATION_R + V_BY_NUL;
    String ERR_CONFIG_REPEAT = N_GAME_CONFIG + S_QUOTATION_L + RUN_FILE_CONFIG + S_QUOTATION_R + V_EXISTS + N_GAME_REPEAT;
    String ERR_ID_NON = N_GAME_CONFIG + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + N_IN + N_SPEC_ID + S_QUOTATION_L + PH_ARG1 + S_QUOTATION_R + V_NON_EXISTS;
    String ERR_ID_EXISTS = N_GAME_CONFIG + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + N_IN + N_SPEC_ID + S_QUOTATION_L + PH_ARG1 + S_QUOTATION_R + V_ARD_EXISTS;
    String ERR_INVALIDATE = N_GAME_CONFIG + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + N_IN + V_EXISTS + N_GAME_INVALIDATE;
    String ERR_EXE_NUL = N_GAME_CONFIG + S_QUOTATION_L + PH_ARG0 + S_QUOTATION_R + N_IN + N_SPEC_ID + S_QUOTATION_L + PH_ARG1 + S_QUOTATION_R + N_EXE + V_BY_NUL;
    String ERR_CREATE_FAIL = V_CRT + N_FILE_SCRIPT + V_FAIL + N_ERR_INFO + PH_ARG0;
    String ERR_RUN_FAIL = V_EXEC + N_FILE_SCRIPT + V_FAIL + N_ERR_INFO + PH_ARG0;
    String GAMES_COMMENT = "\n" + gs(4) + "游戏配置集节点结构说明：\n"
    + gs(4) + "Games节点由一个唯一节点comment和多个Game节点按顺序组成，comment节点必须在最前面。\n"
    + gs(4) + "Games::comment\t\t游戏配置集节点结构说明，对执行游戏无影响，仅此说明而已。\n"
    + gs(4) + "Games::Game\t\t\t游戏配置节点，包括执行游戏的命令行参数配置及执行游戏前和执行游戏后的BAT脚本命令配置。\n"
    + gs(4) + "Game节点由comment、name、id、path、exe、args、priority、icon、agentExecutablePath、agentArgs、before、after、beforeWait、afterWait、watchWait、watch节点按顺序组成；comment节点必须在最前面，watch节点可以有多个。\n"
    + gs(4) + "Game::comment\t\t游戏快捷方式说明，默认值同Game::name。\n"
    + gs(4) + "Game::name\t\t\t游戏快捷方式名称，一般使用游戏中文名称。\n"
    + gs(4) + "Game::id\t\t\t游戏唯一标识。\n"
    + gs(4) + "Game::path\t\t\t游戏可执行文件路径，也是Game::icon的路径。\n"
    + gs(4) + "Game::exe\t\t\t游戏可执行文件名称，不包含文件扩展名" + FILE_SUFFIX_EXE + "；若Game::agentExecutablePath非空且有效，则优先使用代理启动游戏。\n"
    + gs(4) + "Game::args\t\t\tGame::exe的命令行参数。\n"
    + gs(4) + "Game::priority\t\t游戏进程的优先级，可选值为：32（标准），64（低），128（高），256（实时），16384（低于标准），32768（高于标准）。\n"
    + gs(4) + "Game::icon\t\t\t游戏快捷方式的图标文件完整名称（包含文件扩展名）；若为空则使用游戏可执行文件中图标。\n"
    + gs(4) + "Game::agentExecutablePath\tGame::exe的代理可执行文件绝对路径名；适用于使用游戏插件启动游戏的情况，例如：上古卷轴5的skse。\n"
    + gs(4) + "Game::agentArgs\t\tGame::agentExecutablePath的命令行参数；若想使用代理参数指定游戏进程优先级，则Game::priority应该置空，否则优先使用Game::priority。\n"
    + gs(4) + "Game::before\t\t在游戏执行前需要执行的BAT脚本命令。\n"
    + gs(4) + "Game::after\t\t\t在游戏执行后需要执行的BAT脚本命令。\n"
    + gs(4) + "Game::beforeWait\t\tGame::before命令执行完后等待beforeWait秒，再执行游戏；仅当Game::before不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。\n"
    + gs(4) + "Game::afterWait\t\t执行游戏后等待afterWait秒，再执行Game::after命令；仅当Game::after不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。\n"
    + gs(4) + "Game::watchWait\t\t游戏监控进程的等待时间，每隔watchWait秒后检测一次游戏进程是否存在；仅当Game::watch不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。\n"
    + gs(4) + "Game::watch\t\t\t由Game::before或Game::after脚本启动的进程的名称（例如：editplus.exe）或进程的可执行文件路径名（例如：F:/tools/EditPlus/editplus.exe），在游戏进程结束后监控程序会自动关闭之。\n" + gs(4);
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
    + "-k [id] 根据id终止游戏配置文件" + RUN_FILE_CONFIG + "中对应的的游戏进程；如果不指定id程序则会先显示id列表（同-v），再提示输入一个id，根据id终止对应的游戏进程。" + gl(2)
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
    + "run -k" + gl(2)
    + "run -k ew" + gl(2)
    + "run -x" + gl(2)
    + "run -x ew" + gl(2)
    + "run -l" + gl(2)
    + "run -l ew" + gl(2)
    + "run -la";
}
