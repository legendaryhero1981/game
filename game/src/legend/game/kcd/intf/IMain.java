package legend.game.kcd.intf;

import static legend.util.StringUtil.gl;

import legend.intf.ICommon;

public interface IMain extends ICommon{
    String KCD_FILE_CONFIG = "./kcd.xml";
    String KCD_FILE_ORDER = "mod_order.txt";
    String N_KCD_CONFIG = "Mod配置文件";
    String N_KCD_CFG = "Config节点内有任一个节点";
    String N_KCD_MOD_PATH = "Config节点内的modPath节点对应的目录";
    String N_KCD_MOD = "Mods节点内有任一个Mod节点的mod节点";
    String MOD_ORDER_MERGE = "0";
    String MOD_ORDER_CONFLICT = "1";
    String MOD_ORDER_INGNORE = "999";
    String MOD_INGNORE_DESC = "该Mod中无任何文件与其它Mod冲突，具有最低的Mod排序优先级，不会添加到Mod排序文件 " + KCD_FILE_ORDER + " 中。";
    String MOD_MERGE_DESC = "整合Mod，包含所有其它Mod的非冲突文件，具有最高的Mod排序优先级。";
    String MOD_MODS = "Mods";
    String MOD_MERGE = "Merge";
    String MOD_REPAIR = "Repair";
    String MOD_CONFLICT = "Conflict";
    String MOD_DATA = "data";
    String MOD_LOCAL = "localization";
    String MOD_CHS = "chineses";
    String MOD_ENG = "english";
    String MOD_LOCAL_CHS = MOD_LOCAL + SPRT_FILE + MOD_CHS;
    String MOD_LOCAL_ENG = MOD_LOCAL + SPRT_FILE + MOD_ENG;
    String PAK_CHINESES = "chineses_xml" + EXT_PAK;
    String PAK_ENGLISH = "english_xml" + EXT_PAK;
    String PAK_MERGE = MOD_MERGE + EXT_PAK;
    String REG_MOD_LOCAL = "(?i).*_xml\\" + EXT_PAK + "$";
    String REG_MOD_PAK = "(?i)\\" + EXT_PAK + "$";
    String REG_MOD_NOT_PAK = ".*(?i)(?<!\\" + EXT_PAK + ")$";
    String REG_ORDER = "\\d|[1-9]\\d{1,2}";
    String KCD_LOC_MRG = "-lm";
    String KCD_LOC_MRG_A = "-lma";
    String KCD_LOC_MRG_U = "-lmu";
    String KCD_LOC_CMP = "-lc";
    String KCD_LOC_CMP_A = "-lca";
    String KCD_LOC_CMP_U = "-lcu";
    String KCD_LOC_DBG = "-ld";
    String KCD_LOC_RLS = "-lr";
    String KCD_MOD_CRT = "-mc";
    String KCD_MOD_PAK = "-mp";
    String KCD_MOD_UNPAK = "-mu";
    String KCD_MOD_MRG_A = "-mma";
    String KCD_MOD_MRG_F = "-mmf";
    String KCD_MOD_MRG_C = "-mmc";
    String KCD_MOD_MRG_O = "-mmo";
    String KCD_MOD_MRG_U = "-mmu";
    String ERR_NOT_FIND = "没找到任何匹配的文件！";
    String ERR_EXISTS_MERGE = "Mod目录 " + PH_ARGS0 + " 中存在名称为 " + MOD_MERGE + " 的子目录！";
    String ERR_KCD_NON = N_KCD_CONFIG + "\"" + KCD_FILE_CONFIG + "\"" + V_NON_EXISTS;
    String ERR_KCD_NUL_CFG = N_KCD_CONFIG + "\"" + KCD_FILE_CONFIG + "\"" + N_IN + N_KCD_CFG + V_BY_NUL;
    String ERR_KCD_MOD_PATH = N_KCD_CONFIG + "\"" + KCD_FILE_CONFIG + "\"" + N_IN + N_KCD_MOD_PATH + V_NON_EXISTS;
    String ERR_KCD_NUL_MOD = N_KCD_CONFIG + "\"" + KCD_FILE_CONFIG + "\"" + N_IN + N_KCD_MOD + V_BY_NUL;
    String HELP_KCD = APP_INFO + "参数说明：" + gl(2)
    + "kcd -lm|-lma|-lmu|-lc|-lca|-lcu|-ld|-lr|-mc|-mp|-mu|-mma|-mmf|-mmc|-mmo|-mmu regex src dest merge gamePath modPath mergePath mergeExecutablePath" + gl(2)
    + "-lm\t\t\t全量合并翻译文件，合并所有；包含原有的、新增的和更新的记录。" + gl(2)
    + "-lma\t\t\t全量合并翻译文件，合并新增；包含原有的和新增的记录。" + gl(2)
    + "-lmu\t\t\t全量合并翻译文件，合并更新；包含原有的和更新的记录。" + gl(2)
    + "-lc\t\t\t差量合并翻译文件，合并所有；包含新增的和更新的记录，不包含原有的记录。" + gl(2)
    + "-lca\t\t\t差量合并翻译文件，合并新增；包含新增的记录，不包含原有的记录。" + gl(2)
    + "-lcu\t\t\t差量合并翻译文件，合并更新；包含更新的记录，不包含原有的记录。" + gl(2)
    + "-ld\t\t\t调试翻译模式。" + gl(2)
    + "-lr\t\t\t发布翻译模式。" + gl(2)
    + "-mc\t\t\t创建Mod配置文件" + KCD_FILE_CONFIG + "。" + gl(2)
    + "-mp\t\t\t先清空gamePath/Mods目录，再重新打包mergePath目录中文件到gamePath/Mods目录，并重新生成Mod排序文件" + KCD_FILE_ORDER + "。" + gl(2)
    + "-mu\t\t\t先清空modPath目录，再重新解包该目录中所有Mod。" + gl(2)
    + "-mmf\t\t\t重新整合modPath目录中所有Mod，并重新合并所有冲突文件（即多个Mod共有的同名文件）。" + gl(2)
    + "-mma\t\t\t重新整合modPath目录中所有Mod，只重新合并变化了的冲突文件（即同名文件有更新、新增或删除）。" + gl(2)
    + "-mmc\t\t\t只合并Mod配置文件" + KCD_FILE_CONFIG + "中配置的所有冲突文件，不打包到gamePath/Mods目录。" + gl(2)
    + "-mmo\t\t\t根据Mod配置文件" + KCD_FILE_CONFIG + "中配置的Mod排序信息重新生成Mod排序文件" + KCD_FILE_ORDER + "。" + gl(2)
    + "-mmu\t\t\t在mergePath目录中重新生成Mod配置文件" + KCD_FILE_CONFIG + "中配置的所有唯一文件（即非冲突文件），并更新这些文件的MD5码。" + gl(2)
    + "regex\t\t\t文件名查询正则表达式，.匹配任意文件名。" + gl(2)
    + "src\t\t\t文件输入目录。" + gl(2)
    + "dest\t\t\t文件输入或输出目录。" + gl(2)
    + "merge\t\t\t文件整合目录。" + gl(2)
    + "gamePath\t\t游戏目录。" + gl(2)
    + "modPath\t\t\tMod目录。" + gl(2)
    + "mergePath\t\tMod整合目录。" + gl(2)
    + "mergeExecutablePath\tMod整合工具KDiff3的可执行文件路径名。" + gl(2)
    + "合并翻译文件说明：先根据regex获得src和dest中匹配的所有文件，再将同名文件数据合并保存到merge中；全量合并与差量合并的区别是前者得到的合并文件是满足游戏数据规范的文件（即可以直接作为Mod使用的文件），而后者得到的是中间文件，需要进行再编辑以满足游戏数据规范。" + gl(2)
    + "单条命令：" + gl(2)
    + "kcd -lm regex src dest merge" + gl(2)
    + "kcd -lma regex src dest merge" + gl(2)
    + "kcd -lmu regex src dest merge" + gl(2)
    + "kcd -lc regex src dest merge" + gl(2)
    + "kcd -lca regex src dest merge" + gl(2)
    + "kcd -lcu regex src dest merge" + gl(2)
    + "kcd -ld regex src dest" + gl(2)
    + "kcd -lr regex src dest" + gl(2)
    + "kcd -mc gamePath modPath mergePath mergeExecutablePath" + gl(2)
    + "kcd -mp" + gl(2)
    + "kcd -mu" + gl(2)
    + "kcd -mmf" + gl(2)
    + "kcd -mma" + gl(2)
    + "kcd -mmc" + gl(2)
    + "kcd -mmo" + gl(2)
    + "kcd -mmu" + gl(2)
    + "示例：" + gl(2)
    + "kcd -lm (?i)\\.xml$ F:/games/KingdomComeDeliverance/修改/Mods/UltimateRealismOverhaul/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/merge" + gl(2)
    + "kcd -lc (?i)\\.xml$ F:/games/KingdomComeDeliverance/修改/Mods/UltimateRealismOverhaul/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/diff" + gl(2)
    + "kcd -ld (?i)\\.xml$ F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/debug" + gl(2)
    + "kcd -lr (?i)\\.xml$ F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/debug F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses" + gl(2)
    + "kcd -mc F:/games/KingdomComeDeliverance F:/games/KingdomComeDeliverance/修改/Mods F:/games/KingdomComeDeliverance/修改/Merge F:/tools/KDiff3/kdiff3.exe";
}
