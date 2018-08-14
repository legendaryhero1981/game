package legend.game.poe2.intf;

import static legend.intf.ICommon.gl;

import legend.intf.ICommon;

public interface IMain extends ICommon{
    String POE_DATA_ENCODE = "-e";
    String POE_DATA_DECODE = "-d";
    String POE_OBJ_GUID = "-g";
    String HELP_POE = APP_INFO + "参数说明：" + gl(2)
    + "poe -d|-e|-g regex src string" + gl(2)
    + "-d\t解码（即格式化）JSON格式文件" + gl(2)
    + "-e\t编码（即压缩为一行）JSON格式文件" + gl(2)
    + "-g\t给自定义MOD对象生成36位的GUID字符串" + gl(2)
    + "regex\t文件名查询正则表达式，.匹配任意文件名和目录名。" + gl(2)
    + "src\t文件输入目录" + gl(2)
    + "string\t自定义MOD对象名称字符串" + gl(2)
    + "单条命令：" + gl(2)
    + "poe -d regex src" + gl(2)
    + "poe -e regex src" + gl(2)
    + "poe -g string" + gl(2)
    + "示例：" + gl(2)
    + "poe -d (?i)\\..*bundle$ \"F:/games/Pillars of Eternity II/PillarsOfEternityII_Data/exported/design/gamedata\"" + gl(1)
    + "将 .../gamedata 目录中文件扩展名以bundle结尾（忽略大小写）的所有文件进行解码。" + gl(2)
    + "poe -e (?i)\\..*bundle$ \"F:/games/Pillars of Eternity II/PillarsOfEternityII_Data/exported/design/gamedata\"" + gl(1)
    + "将 .../gamedata 目录中文件扩展名以bundle结尾（忽略大小写）的所有文件进行编码。" + gl(2)
    + "poe -g Great_Sword_WarGod" + gl(1)
    + "获得自定义MOD对象名称Great_Sword_WarGod的GUID字符串。";
}
