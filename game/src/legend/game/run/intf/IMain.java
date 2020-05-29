package legend.game.run.intf;

import static java.util.regex.Pattern.compile;
import static legend.util.StringUtil.gl;
import static legend.util.StringUtil.gs;

import java.util.regex.Pattern;

import legend.intf.ICommon;

public interface IMain extends ICommon{
    long SLEEP_TIME = 500;
    String RUN_FILE_LOG = "./run.log";
    String RUN_FILE_CONFIG = "./run.xml";
    String EXE_JAVA = "java.exe";
    String EXE_RUN = "run.exe";
    String BAT_RUN = "run-javaw.bat";
    String MODULE_RUN = "legend/legend.game.run.Main";
    String TIME_SECOND_MIN = "1";
    String TIME_SECOND_MAX = "60";
    String WAIT_TIME = "10";
    String PRIORITY_HIGH = "128";
    String REG_PRIORITY = "32|64|128|256|16384|32768";
    String REG_TIME = "60|[1-9]|[1-5]\\d";
    String N_GAME_CONFIG = "游戏配置文件";
    String N_FILE_SCRIPT = "脚本文件";
    String N_EXE = "对应的name或path或exe节点";
    String N_GAME_REPEAT = "id相同的game节点！";
    String N_GAME_INVALIDATE = "id或name或path或exe为空的game节点！";
    String ST_REPEAT_ID = "找到重复的游戏ID：" + PH_ARGS0;
    String ST_CHOICE_ID = "请输入一个游戏ID（按回车键确认）：";
    String FILE_PREFIX = "run";
    String CMD_CREATE = "-c";
    String CMD_ADD = "-a";
    String CMD_DEL = "-d";
    String CMD_VIEW = "-v";
    String CMD_EXEC = "-x";
    String CMD_KILL = "-k";
    String CMD_LINK = "-l";
    String CMD_LINK_ALL = "-la";
    String CMD_CS_RUN = "wscript \"" + PH_ARGS0 + "\"";
    String CMD_VBS_SH_INIT = "dim sh" + gl(1) + "set sh=WScript.CreateObject(\"WScript.Shell\")";
    String CMD_VBS_SLEEP = "WScript.Sleep " + PH_ARGS0;
    String CMD_VBS_RUN = "sh.Run \"" + PH_ARGS0 + "\",0,true";
    String CMD_VBS_RUN_DEL = "sh.Run \"cmd /c del /q \"\"" + PH_ARGS0 + "\"\">nul 2>nul\",0,true";
    String CMD_VBS_RUN_GAME = "sh.Run \"cmd /c start /high /D \"\"" + PH_ARGS0 + "\"\" " + gs("\"",4) + " \"\"" + PH_ARGS1 + EXT_EXE + "\"\" " + PH_ARGS2 + "\",0,true";
    String CMD_VBS_RUN_AGENT = "sh.Run \"cmd /c start /high /D \"\"" + PH_ARGS0 + "\"\" " + gs("\"",4) + " \"\"" + PH_ARGS1 + "\"\" " + PH_ARGS2 + "\",0,true";
    String CMD_VBS_RUN_PROC = "sh.Run \"cmd /c wmic process where \"\"name='" + PH_ARGS0 + EXT_EXE + "'\"\" call SetPriority " + PH_ARGS1 + "\",0,true";
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
    String CMD_VBS_PROC_GAME = "dim games" + gl(1) + "set games=wmi.ExecQuery(\"select * from win32_process where name='" + PH_ARGS0 + EXT_EXE + "'\")";
    String CMD_VBS_GAME_PRIORITY = CMD_VBS_WMI_INIT + gl(1) + CMD_VBS_PROC_GAME + gl(1)
    + "if games.count>0 then" + gl(1) + "games.ItemIndex(0).SetPriority " + PH_ARGS1 + gl(1) + "end if";
    String CMD_VBS_WATCH_TERMINATE = "for each watch in wmi.ExecQuery(wql)" + gl(1) + "watch.Terminate" + gl(1) + "next";
    String CMD_VBS_GAME_KILL = CMD_VBS_WMI_INIT + gl(1)
    + "wql=\"select * from win32_process where name='" + PH_ARGS0 + EXT_EXE + "'\"" + gl(1)
    + CMD_VBS_WATCH_TERMINATE;
    String CMD_VBS_GAME_WATCH = "while games.Count>0" + gl(1) + "WScript.Sleep " + PH_ARGS0 + gl(1)
    + "set games=wmi.ExecQuery(\"select * from win32_process where name='" + PH_ARGS1 + EXT_EXE + "'\")" + gl(1)
    + "Wend" + gl(1)
    + "dim names,paths,wql" + gl(1)
    + "names=Split(\"" + PH_ARGS2 + "\",\"" + SPRT_CMDS + "\")" + gl(1)
    + "paths=Split(\"" + PH_ARGS3 + "\",\"" + SPRT_CMDS + "\")" + gl(1)
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
    String CMD_VBS_SC_CRT = "set shortcut=sh.CreateShortcut(sh.SpecialFolders(\"Desktop\")&\"" + SPRT_FILE + PH_ARGS0 + EXT_LNK + "\")";
    String CMD_VBS_SC_ARG = "shortcut.Arguments=\"" + CMD_EXEC + " " + PH_ARGS0 + "\"";
    String CMD_VBS_SC_IL = "shortcut.IconLocation=\"" + PH_ARGS0 + SPRT_FILE + PH_ARGS1 + ",0\"";
    String CMD_VBS_SC_DESC = "shortcut.Description=\"" + PH_ARGS0 + "\"";
    String CMD_VBS_SC_WD = "shortcut.WorkingDirectory=\"" + PH_ARGS0 + "\"";
    String CMD_VBS_SC_TP = "shortcut.TargetPath=target";
    String CMD_VBS_SC_WS = "shortcut.WindowStyle=7";
    String CMD_VBS_SC_SAVE = "shortcut.Save";
    String CMD_BAT_CHCP_UTF8 = "chcp 65001";
    String CMD_BAT_PROC_DEL_BY_NAME = "wmic process where \"name='" + PH_ARGS0 + "'\" delete";
    String CMD_BAT_PROC_DEL_BY_PATH = "wmic process where \"executablepath='" + PH_ARGS0 + "'\" delete";
    String CMD_BAT_GAME_WATCH = CMD_BAT_CHCP_UTF8 + gl(1)
    + "setlocal enableextensions" + gl(1) + "setlocal enabledelayedexpansion" + gl(1)
    + ":watch" + gl(1)
    + "set pid=" + gl(1)
    + "for /f \"usebackq skip=1\" %%i in (`wmic process where \"name='" + PH_ARGS0 + "'\" get processid`) do if \"!pid!\"==\"\" set pid=%%i" + gl(1)
    + "if not \"%pid%\"==\"\" (" + gl(1)
    + "choice /c y /d y /t " + PH_ARGS1 + " >nul 2>nul" + gl(1)
    + "goto watch ) else (" + gl(1)
    + PH_ARGS2
    + "goto quit )" + gl(1)
    + ":quit" + gl(1)
    + "exit /b 0";
    String ERR_CONF_NON = N_GAME_CONFIG + S_DQM_L + RUN_FILE_CONFIG + S_DQM_R + V_NON_EXISTS + S_BANG;
    String ERR_CONF_NUL = N_GAME_CONFIG + S_DQM_L + RUN_FILE_CONFIG + S_DQM_R + V_BY_NUL + S_BANG;
    String ERR_CONF_REPEAT = N_GAME_CONFIG + S_DQM_L + RUN_FILE_CONFIG + S_DQM_R + V_EXISTS + N_GAME_REPEAT;
    String ERR_ID_NON = N_GAME_CONFIG + S_DQM_L + PH_ARGS0 + S_DQM_R + N_IN + N_SPEC_ID + S_DQM_L + PH_ARGS1 + S_DQM_R + V_NON_EXISTS + S_BANG;
    String ERR_ID_EXISTS = N_GAME_CONFIG + S_DQM_L + PH_ARGS0 + S_DQM_R + N_IN + N_SPEC_ID + S_DQM_L + PH_ARGS1 + S_DQM_R + V_ARD_EXISTS + S_BANG;
    String ERR_INVALIDATE = N_GAME_CONFIG + S_DQM_L + PH_ARGS0 + S_DQM_R + N_IN + V_EXISTS + N_GAME_INVALIDATE;
    String ERR_EXE_NUL = N_GAME_CONFIG + S_DQM_L + PH_ARGS0 + S_DQM_R + N_IN + N_SPEC_ID + S_DQM_L + PH_ARGS1 + S_DQM_R + N_EXE + V_BY_NUL + S_BANG;
    String ERR_CREATE_FILE = V_CRT + N_FILE_SCRIPT + V_FAIL + S_BANG + N_ERR_INFO + PH_ARGS0;
    String ERR_RUN_FILE = V_EXEC + N_FILE_SCRIPT + V_FAIL + S_BANG + N_ERR_INFO + PH_ARGS0;
    String GAMES_COMMENT = "\n" + gs(4) + "游戏配置集节点结构说明：\n"
    + gs(4) + "Games节点由一个唯一节点comment和多个Game节点按顺序组成，comment节点必须在最前面。\n"
    + gs(4) + "Games::comment" + gs(14) + "游戏配置集节点结构说明，对执行游戏无影响，仅此说明而已。\n"
    + gs(4) + "Games::Game" + gs(17) + "游戏配置节点，包括执行游戏的命令行参数配置及执行游戏前和执行游戏后的BAT脚本命令配置等等。\n"
    + gs(4) + "Game节点由comment、name、id、path、exe、args、priority、icon、agentExecutablePath、agentArgs、before、after、beforeWait、afterWait、watchWait、watch节点按顺序组成；comment节点必须在最前面，watch节点可以有多个。\n"
    + gs(4) + "Game::comment" + gs(15) + "游戏快捷方式说明，默认值同Game::name。\n"
    + gs(4) + "Game::name" + gs(18) + "游戏快捷方式名称，一般使用游戏中文名称。\n"
    + gs(4) + "Game::id" + gs(20) + "游戏唯一标识。\n"
    + gs(4) + "Game::path" + gs(18) + "游戏可执行文件路径，也是Game::icon的路径。\n"
    + gs(4) + "Game::exe" + gs(19) + "游戏可执行文件名称，不包含文件扩展名" + EXT_EXE + "；若Game::agentExecutablePath非空且有效，则优先使用代理启动游戏。\n"
    + gs(4) + "Game::args" + gs(18) + "Game::exe的命令行参数。\n"
    + gs(4) + "Game::priority" + gs(14) + "游戏进程的优先级，默认值为128；可选值为：32（标准），64（低），128（高），256（实时），16384（低于标准），32768（高于标准）。\n"
    + gs(4) + "Game::icon" + gs(18) + "游戏快捷方式的图标文件完整名称（包含文件扩展名）；若为空则使用游戏可执行文件中图标。\n"
    + gs(4) + "Game::agentExecutablePath" + gs(3) + "Game::exe的代理可执行文件绝对路径名；适用于使用游戏插件启动游戏的情况，例如：上古卷轴5的skse。\n"
    + gs(4) + "Game::agentArgs" + gs(13) + "Game::agentExecutablePath的命令行参数；若想使用代理参数指定游戏进程优先级，则Game::priority应该置空，否则优先使用Game::priority。\n"
    + gs(4) + "Game::before" + gs(16) + "在游戏执行前需要执行的BAT脚本命令。\n"
    + gs(4) + "Game::after" + gs(17) + "在游戏执行后需要执行的BAT脚本命令。\n"
    + gs(4) + "Game::beforeWait" + gs(12) + "Game::before命令执行完后等待beforeWait秒，再执行游戏；仅当Game::before不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。\n"
    + gs(4) + "Game::afterWait" + gs(13) + "执行游戏后等待afterWait秒，再执行Game::after命令；仅当Game::after不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。\n"
    + gs(4) + "Game::watchWait" + gs(13) + "游戏监控进程的等待时间，每隔watchWait秒后检测一次游戏进程是否存在；仅当Game::watch不为空时生效，默认值为10；基于性能考虑，取值范围为：1~60，若超过取值范围程序会取默认值。\n"
    + gs(4) + "Game::watch" + gs(17) + "由Game::before或Game::after脚本启动的进程的名称（例如：editplus.exe）或进程的可执行文件路径名（例如：F:/tools/EditPlus/editplus.exe），在游戏进程结束后监控程序会自动关闭之。\n" + gs(4);
    String HELP_RUN = APP_INFO + "参数说明：" + gl(2)
    + "run -c|-a|-d|-v|-k|-x|-l|-la id path exe name [comment]" + gl(2)
    + "id" + gs(6) + "游戏标识，在" + RUN_FILE_CONFIG + "文件中唯一标识一个游戏配置节点。" + gl(2)
    + "path" + gs(4) + "游戏可执行文件路径。" + gl(2)
    + "exe" + gs(5) + "游戏可执行文件名称（不包含扩展名" + EXT_EXE + "）。" + gl(2)
    + "name" + gs(4) + "游戏中文名称。" + gl(2)
    + "comment" + gs(1) + "游戏快捷方式说明。" + gl(2)
    + "-c id path exe name [comment] 新建游戏配置文件" + RUN_FILE_CONFIG + "，并生成一个游戏配置节点。" + gl(2)
    + "-a id path exe name [comment] 添加一个游戏配置节点到游戏配置文件" + RUN_FILE_CONFIG + "中。" + gl(2)
    + "-v 显示游戏配置文件" + RUN_FILE_CONFIG + "中所有的游戏配置节点的id列表，显示格式为：id\t\tcomment。" + gl(2)
    + "-d [id] 根据id删除游戏配置文件" + RUN_FILE_CONFIG + "中对应的一个游戏配置节点；如果不指定id程序则会先显示id列表（同-v），再提示输入一个id，根据id删除对应的游戏配置节点。" + gl(2)
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
    + "run -v" + gl(2)
    + "run -d" + gl(2)
    + "run -d ew2" + gl(2)
    + "run -k" + gl(2)
    + "run -k ew" + gl(2)
    + "run -x" + gl(2)
    + "run -x ew" + gl(2)
    + "run -l" + gl(2)
    + "run -l ew" + gl(2)
    + "run -la";
    Pattern PTRN_PRIORITY = compile(REG_PRIORITY);
    Pattern PTRN_TIME = compile(REG_TIME);
}
