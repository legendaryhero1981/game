package legend.util.entity.intf;

import static java.util.regex.Pattern.compile;
import static legend.util.StringUtil.getAppPath;
import static legend.util.StringUtil.gs;

import java.util.regex.Pattern;

import legend.util.intf.IFileUtil;

public interface IZip7 extends IFileUtil{
    long ZIP7_VOL_SIZE_DEF = 1l << 30;
    String REG_ZIP7_MODE = MODE_ZIP + "|" + MODE_UNZIP;
    String REG_ZIP7_MODE_UNZIP = MODE_UNZIP_MD5 + "|" + MODE_UNZIP_DIR + "|" + MODE_UNZIP_MEG;
    String REG_ZIP7_COMP = "[013579]";
    String REG_ZIP7_VOL = "(" + REG_NUM_NATURAL + ")([bkmgBKMG])";
    String ZIP7_ARG_ZIP = "a";
    String ZIP7_ARG_UNZIP = "x";
    String ZIP7_ARG_LIST_FILE = "@";
    String ZIP7_ARG_YES_ALL = "-y";
    String ZIP7_ARG_OUT = "-o";
    String ZIP7_ARG_PW = "-p";
    String ZIP7_ARG_SPF = "-spf2";
    String ZIP7_ARG_VOL = "-v";
    String ZIP7_ARG_VOL_DEF = ZIP7_ARG_VOL + "1g";
    String ZIP7_ARG_COMP = "-mx";
    String ZIP7_VAL_COMP_DEF = "9";
    String ZIP7_ARG_COMP_DEF = ZIP7_ARG_COMP + ZIP7_VAL_COMP_DEF;
    String ZIP7_ARG_SFX = "-sfx";
    String ZIP7_ARG_SFX_GUI = ZIP7_ARG_SFX + "7z.sfx";
    String ZIP7_ARG_SFX_CON = ZIP7_ARG_SFX + "7zCon.sfx";
    String ZIP7_EXEC_PATH = getAppPath() + "/tools/7-Zip/7z.exe";
    String N_ZIP7_CONF = "7-Zip任务处理配置文件";
    String ST_FILE_SPK_CONF = V_GNRT + N_ZIP7_CONF + S_DQM + CONF_FILE_7ZIP + S_DQM + S_BANG;
    String ERR_ZIP7_EXEC_NON = "Zip7节点下的zip7ExecutablePath子节点值" + V_BY_NUL;
    String ERR_ZIP7_TASK_NON = "Zip7::Zip7Task节点下的queryRegex或queryPath或listFilePath或mode或filePath子节点值" + V_BY_NUL;
    String FILE_ZIP7_COMMENT = "\n" + gs(4) + "Zip7配置节点结构说明：\n"
    + gs(4) + "Zip7节点由comment、zip7ExecutablePath、Zip7Task节点按顺序组成，comment节点必须在最前面，Zip7Task节点可以有多个。\n"
    + gs(4) + "Zip7::comment" + gs(15) + "Zip7配置节点结构说明，对执行文件压缩解压任务无影响，仅此说明而已。\n"
    + gs(4) + "Zip7::zip7ExecutablePath" + gs(4) + "文件压缩解压工具7-Zip的可执行文件路径名，用来具体执行每一个Zip7::Zip7Task任务。\n"
    + gs(4) + "Zip7::Zip7Task" + gs(4) + "7-Zip任务对象，包含了执行每一条7z命令所需的全部参数；当Zip7Task::mode值为0时对应1条7z压缩命令，而Zip7Task::mode值为1时程序会根据Zip7Task::listFilePath中提供的压缩文件列表分解出多条命令，即每个压缩文件对应1条7z解压缩命令。\n"
    + gs(4) + "Zip7Task节点由节点queryRegex、queryLevel、queryPath、listFilePath、mode、filePath、unzipMode、password、compression、volumeSize、sfxModule、moreArgs按顺序组成。\n"
    + gs(4) + "Zip7Task::queryRegex" + gs(8) + "文件查询正则表达式，供程序内部使用file命令生成清单文件Zip7Task::listFilePath。\n"
    + gs(4) + "Zip7Task::queryLevel" + gs(8) + "文件目录最大查询层数；取值范围为：1~" + Integer.MAX_VALUE + "，不指定或超过取值范围则取默认值" + Integer.MAX_VALUE + "；供程序内部使用file命令生成清单文件Zip7Task::listFilePath。\n"
    + gs(4) + "Zip7Task::queryPath" + gs(9) + "文件查询目录，供程序内部使用file命令生成清单文件Zip7Task::listFilePath。\n"
    + gs(4) + "Zip7Task::listFilePath" + gs(6) + "程序内部使用file命令生成的清单文件路径名，为联合执行的7z命令提供参数。\n"
    + gs(4) + "Zip7Task::mode" + gs(14) + "文件压缩解压模式，取值范围为：0,1，取0表示压缩，取1表示解压缩；若指定的值超过取值范围程序会取默认值0。\n"
    + gs(4) + "Zip7Task::filePath" + gs(10) + "当Zip7Task::mode值为0时表示执行7z命令生成的压缩文件路径名；而Zip7Task::mode值为1时表示执行7z命令生成的解压缩文件的输出目录。\n"
    + gs(4) + "Zip7Task::unzipMode" + gs(9) + "压缩文件的解压缩路径处理模式，取值范围为：0,1,2；取0表示按照压缩文件内容对应的32位md5码分类，即该压缩文件的解压缩路径为Zip7Task::filePath/压缩文件名.md5码；取1表示按照压缩文件名分类，即该压缩文件的解压缩路径为Zip7Task::filePath/压缩文件名（不包含扩展名）；取2表示不分类，直接将所有压缩文件解压缩到Zip7Task::filePath目录中；若指定的值超过取值范围程序会取默认值0；仅当Zip7Task::mode值为1时有效。\n"
    + gs(4) + "Zip7Task::password" + gs(10) + "执行7z命令时需要提供的密码参数，即创建压缩文件时指定的密码或解压缩加密压缩文件时需要提供的密码。\n"
    + gs(4) + "Zip7Task::compression" + gs(7) + "文件压缩率，取值范围为：0,1,3,5,7,9，取0表示仅存储不压缩，取1表示最低压缩率，取9表示最高压缩率；若指定的值超过取值范围程序会取默认值9；仅当Zip7Task::mode值为0时有效。\n"
    + gs(4) + "Zip7Task::volumeSize" + gs(8) + "分卷压缩文件大小，对应的正则表达式为" + REG_ZIP7_VOL + "；基于性能考虑，若指定的值小于1g程序会取默认值1g；仅当Zip7Task::mode值为0时有效。\n"
    + gs(4) + "Zip7Task::sfxModule" + gs(9) + "自解压可执行文件的生成模式，取值范围为：0,1，取0表示生成GUI应用程序，取1表示生成控制台应用程序；若指定的值超过取值范围程序会取默认值0；仅当Zip7Task::mode值为0时有效。\n"
    + gs(4) + "Zip7Task::moreArgs" + gs(10) + "执行7z命令所需的其他原生参数，可以指定用空格字符间隔的多个参数；具体的参数详情可查看7z命令的帮助文件7-zip.chm。\n"
    + gs(4) + "特别说明：基于实用性考虑，程序只支持压缩（a）和解压缩（x）这两条原生命令的参数；Zip7Task节点中各子节点相对于7z原生命令参数的映射关系为：mode＝＞(0＝＞a),(1＝＞x)，filePath＝＞-o{dir_path}或base_archive_name，listFilePath＝＞@listfile，password＝＞-p{password}，compression＝＞-mx=[ 0 | 1 | 3 | 5 | 7 | 9 ]，volumeSize＝＞-v{Size}[ b | k | m | g]，sfxModule＝＞(0＝＞-sfx7z.sfx),(1＝＞-sfx7zCon.sfx)。\n" + gs(4);
    Pattern PTRN_ZIP7_MODE = compile(REG_ZIP7_MODE);
    Pattern PTRN_ZIP7_MODE_UNZIP = compile(REG_ZIP7_MODE_UNZIP);
    Pattern PTRN_ZIP7_COMP = compile(REG_ZIP7_COMP);
    Pattern PTRN_ZIP7_VOL = compile(REG_ZIP7_VOL);
}
