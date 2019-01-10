package legend.game.dos2.intf;

import static legend.util.StringUtil.gl;

import legend.intf.ICommon;

public interface IMain extends ICommon{
    String DOS2_DEBUG = "-d";
    String DOS2_RELEASE = "-r";
    String DOS2_UPDATE = "-u";
    String HELP_EOC = APP_INFO + "参数说明：" + gl(2)
    + "eoc -d|-r|-u src dest" + gl(2)
    + "-d\t调试模式" + gl(2)
    + "-r\t发布模式" + gl(2)
    + "-u\t更新模式" + gl(2)
    + "src\t输入文件路径名" + gl(2)
    + "dest\t输出文件路径名" + gl(2)
    + "示例：" + gl(2)
    + "eoc -d \"F:/games/Divinity Original Sin 2/修改/release/english.xml\" \"F:/games/Divinity Original Sin 2/修改/debug/english.xml\"" + gl(2)
    + "eoc -r \"F:/games/Divinity Original Sin 2/修改/debug/english.xml\" \"F:/games/Divinity Original Sin 2/修改/release/english.xml\"" + gl(2)
    + "eoc -u \"F:/games/Divinity Original Sin 2/修改/release/english.xml\" \"F:/games/Divinity Original Sin 2/修改/debug/english.xml\"" + gl(2)
    + "更新模式说明：" + gl(2)
    + "主要针对调试模式的批量更新，会逐行检查调试模式中每个 content 的值，根据最开头的编号模式进行处理，如下：" + gl(2)
    + "1、编号模式形如 #12345#：替换编号后面的值；" + gl(2)
    + "2、编号模式形如 #" + FLAG_MOD + "12345#：保留原值不替换；" + gl(2)
    + "3、编号模式形如 #" + FLAG_ADD + "12345#：去掉编号左边的" + FLAG_ADD + "号，再替换掉编号后面的值；" + gl(2)
    + "4、编号模式为其他形式：去掉编号并替换编号后面的值；" + gl(2)
    + "5、如果是新增的 content 记录（即找不到对应的 contentuid），将添加新纪录并自动生成新编号（编号值从 content记录数+1 开始累加），新编号形如 #" + FLAG_ADD + "12345#。";
}
