# game

# 项目介绍

PC游戏Mod修改工具集命令行程序，目前基于64位JDK10.0.2开发，建议在64位windows10操作系统上运行。

# 部署说明

1、解包后用myeclipse导入game项目；

2、将game项目的src文件夹下所有.java文件打包成game.jar；

3、用exe4j将game.jar打包成game.exe，在Java invocation选项窗口中的主类选择Module path为legend/legend.Main，在下一个选项窗口JRE中指定最低的JRE版本为9；

4、将game.exe放置在任意目录，如：D:\tools，再将D:\tools\game.exe的文件路径名添加到系统环境变量中；

5、打开命令提示符，输入 game 可以看到命令帮助信息，现在已经可以像使用windows内置命令一样使用使用了！

# 功能描述

```
目前包含5条命令，输入 game 可以看到命令帮助信息如下：

作者：李允
版本：V3.0


参数说明：

file -f[+*!@?]|-fd[+*!@?]|-fdo[+*!@?]|-fpa[+*!@?]|-fpr[+*!@?]|-fps[+*!@?]|-fpda[+*!@?]|-fpdr[+*!@?]|-fpds[+*!@?]|-fpdoa[+*!@?]|-fpdor[+*!@?]|-fpdos[+*!@?]|-fsa[+*!@?]|-fsd[+*!@?]|-fdsa[+*!@?]|-fdsd[+*!@?]|-fdosa[+*!@?]|-fdosd[+*!@?]|-fddsa[+*!@?]|-fddsd[+*!@?]|-r[+*!@?]|-rl[+*!@?]|-ru[+*!@?]|-ruf[+*!@?]|-rd[+*!@?]|-rdl[+*!@?]|-rdu[+*!@?]|-rduf[+*!@?]|-rdo[+*!@?]|-rdol[+*!@?]|-rdou[+*!@?]|-rdouf[+*!@?]|-c[+*!@?]|-cd[+*!@?]|-cdo[+*!@?]|-d[+*!@?]|-dd[+*!@?]|-ddo[+*!@?]|-dn[+*!@?]|-ddn[+*!@?]|-ddon[+*!@?]|-m[+*!@?]|-md[+*!@?]|-mdo[+*!@?]|-b[+*!@?]|-bd[+*!@?]|-bu[+*!@?]|-br[+*!@?]|-u[+*!@?]|-ud[+*!@?]|-zd[+*!@?]|-zdd[+*!@?]|-zi[+*!@?]|-pd[+*!@?]|-pdd[+*!@?]|-pi[+*!@?] regex src [dest] [backup] [sizeExpr] [replacement] [limit] [zipName] [zipLevel] [level]

命令参数：

regex			文件名查询正则表达式，.匹配任意文件名和目录名。

src			输入文件目录。

dest			输出文件目录。

backup			备份文件目录。

sizeExpr		文件大小表达式，匹配的正则表达式为：(0|[1-9]\d*)([TGMKtgmk]?[Bb])?[,;-]?+；取值范围为：0~9223372036854775807B，指定0或不指定则取默认值9223372036854775807B；例如：100B（不小于100字节），10KB（不小于10千字节），1-100MB（介于1兆字节到100兆字节之间），500MB;1GB（介于500兆字节到1千兆字节之间），2,1GB（介于2千兆字节到1千兆字节之间），800,800（等于800字节）。

replacement		文件名替换正则表达式。

zipName			压缩文件名（程序会根据命令选项自动添加文件扩展名.zip或.pak）。

zipLevel		文件压缩级别，取值0：不压缩，1~9：1为最低压缩率，9为最高压缩率；不指定则程序智能选择最佳压缩率。

limit			查询类命令（即命令选项以-f开头的命令）的查询结果显示数量限制，即显示前limit条记录；取值范围为：1~2147483647，不指定则取默认值2147483647。

level			文件目录最大查询层数；取值范围为：1~2147483647，不指定则取默认值2147483647层。

命令选项：

+ 可添加在命令选项末尾，表示输出详细信息；可与@或?连用；例如：-f+@?。

* 可添加在命令选项末尾，表示模拟执行命令，不进行实际操作，仅输出详细信息；可与@或?连用；例如：-f*?@。

! 可添加在命令选项末尾，表示不匹配查询的根目录，如：file -f! . d:/games 不匹配games目录，只匹配该目录中的任意文件和子目录名称。

@ 可添加在命令选项末尾，表示缓存该命令的查询结果，供后面的命令复用；某些命令不能缓存或复用查询结果，程序将智能忽略掉；复用查询结果的命令将忽略与查询相关的命令参数regex和src；当后面某个命令使用了@时，则重新缓存查询结果；可与+或*或?连用；例如：-f@*?或-f@+?。

? 可添加在命令选项末尾，表示命令开始执行前启用询问模式（输入n或N跳过，否则继续，按回车键确认：）；可与+或*或@连用；例如：-f+@?或-f@*?。

组合命令：

可以组合多个命令选项和命令参数，一次连续执行多条命令；命令选项与各命令参数的个数必须相等；各命令选项及各命令参数使用::分隔；可使用*复用最近一个明确的命令选项或命令参数，将其当作该命令的前缀使用，例如：-f::*d::*ds等价于-f::-fd::-fds，g:/games::*/1::*/2等价于g:/games::g:/games/1::g:/games/2；单条命令未用到的命令参数使用?占位。

组合命令示例：

file -cd*@::*::* .::*::* g:/games::*::* d:/::e:/::f:/

file -zdd+::-c+@?::* .::\.zip$::* g:/games::g:/file::* g:/file::*/1::*/2 games::?::? 0::?::? ?::1::*

file -zi*::-cd@*::* \.zip$::.::* g:/file::g:/games::* g:/::e:/::f:/ 1::?::?

单条命令：

file -f[+*!@?] regex src [limit] [level]
根据regex查找src中的文件。

file -fd[+*!@?] regex src [limit] [level]
根据regex查找src中的文件和目录及其中所有文件，相对-f增加了目录名匹配，若目录名匹配，则该目录中所有文件和目录都自动被匹配。

file -fdo[+*!@?] regex src [limit] [level]
根据regex查找src中的目录。

file -fpa[+*!@?] regex src [limit] [level]
根据regex查找src中的文件（同-f），显示文件的绝对路径名。

file -fpr[+*!@?] regex src [limit] [level]
根据regex查找src中的文件（同-f），显示文件的相对路径名（不包含src目录名称）。

file -fps[+*!@?] regex src [limit] [level]
根据regex查找src中的文件（同-f），显示文件的相对路径名（包含src目录名称）。

file -fpda[+*!@?] regex src [limit] [level]
根据regex查找src中的文件和目录及其中所有文件（同-fd），显示文件或目录的绝对路径名。

file -fpdr[+*!@?] regex src [limit] [level]
根据regex查找src中的文件和目录及其中所有文件（同-fd），显示文件或目录的相对路径名（不包含src目录名称）。

file -fpds[+*!@?] regex src [limit] [level]
根据regex查找src中的文件和目录及其中所有文件（同-fd），显示文件或目录的相对路径名（包含src目录名称）。

file -fpdoa[+*!@?] regex src [limit] [level]
根据regex查找src中的目录（同-fdo），显示目录的绝对路径名。

file -fpdor[+*!@?] regex src [limit] [level]
根据regex查找src中的目录（同-fdo），显示目录的相对路径名（不包含src目录名称）。

file -fpdos[+*!@?] regex src [limit] [level]
根据regex查找src中的目录（同-fdo），显示目录的相对路径名（包含src目录名称）。

file -fsa[+*!@?] regex src [sizeExpr] [limit] [level]
根据regex和sizeExpr查找src中的文件，按文件大小递增排序。

file -fsd[+*!@?] regex src [sizeExpr] [limit] [level]
根据regex和sizeExpr查找src中的文件，按文件大小递减排序。

file -fdsa[+*!@?] regex src [sizeExpr] [limit] [level]
根据regex和sizeExpr查找src中的文件和目录，按文件大小递增排序。

file -fdsd[+*!@?] regex src [sizeExpr] [limit] [level]
根据regex和sizeExpr查找src中的文件和目录，按文件大小递减排序。

file -fddsa[+*!@?] regex src [sizeExpr] [limit]
根据regex和sizeExpr查找src中的文件和第一级子目录，按文件和子目录大小递增排序。

file -fddsd[+*!@?] regex src [sizeExpr] [limit]
根据regex和sizeExpr查找src中的文件和第一级子目录，按文件和子目录大小递减排序。

file -fdosa[+*!@?] regex src [sizeExpr] [limit]
根据regex和sizeExpr查找src中的第一级子目录，按子目录大小递增排序。

file -fdosd[+*!@?] regex src [sizeExpr] [limit]
根据regex和sizeExpr查找src中的第一级子目录，按子目录大小递减排序。

file -r[+*!@?] regex src replacement [level]
根据regex和replacement重命名src中的文件。

file -rl[+*!@?] regex src [level]
根据regex将src中所有匹配文件名中英文字母替换为小写；regex可最多指定9个捕获组，最左边为第1个捕获组，程序只会替换捕获组中的子串，如：(.*\.)txt$ 表示只替换文件名，不会替换扩展名txt；.*\.txt$则文件名和扩展名都会被替换；也适用于-ru和-ruf。

file -ru[+*!@?] regex src [level]
根据regex将src中所有匹配文件名中英文字母替换为大写。

file -ruf[+*!@?] regex src [level]
根据regex将src中所有匹配文件名中英文单词首字母替换为大写。

file -rd[+*!@?] regex src replacement [level]
根据regex和replacement重命名src中的文件和目录。

file -rdl[+*!@?] regex src [level]
根据regex将src中所有匹配文件名和目录名中英文字母替换为小写；regex可最多指定9个捕获组，最左边为第1个捕获组，程序只会替换捕获组中的子串，如：(.*\.)txt$ 表示只替换文件名，不会替换扩展名txt；.*\.txt$则文件名和扩展名都会被替换；也适用于-rdu和-rduf。

file -rdu[+*!@?] regex src [level]
根据regex将src中所有匹配文件名和目录名中英文字母替换为大写。

file -rduf[+*!@?] regex src [level]
根据regex将src中所有匹配文件名和目录名中英文单词首字母替换为大写。

file -rdo[+*!@?] regex src replacement [level]
根据regex和replacement重命名src中的目录。

file -rdol[+*!@?] regex src [level]
根据regex将src中所有匹配的目录名中英文字母替换为小写。

file -rdou[+*!@?] regex src [level]
根据regex将src中所有匹配的目录名中英文字母替换为大写。

file -rdouf[+*!@?] regex src [level]
根据regex将src中所有匹配的目录名中英文单词首字母替换为大写。

file -c[+*!@?] regex src dest [level]
根据regex复制src中文件到dest中。

file -cd[+*!@?] regex src dest [level]
根据regex复制src中所有匹配文件和目录及其中所有文件到dest中。

file -cdo[+*!@?] regex src dest [level]
根据regex复制src中所有匹配的目录及其中所有文件到dest中。

file -d[+*!@?] regex src [level]
根据regex删除src中所有匹配文件。

file -dd[+*!@?] regex src [level]
根据regex删除src中所有匹配文件和目录及其中所有文件。

file -ddo[+*!@?] regex src [level]
根据regex删除src中所有匹配的目录及其中所有文件。

file -dn[+*!@?] regex src [level]
根据regex删除src中所有匹配的空文件。

file -ddn[+*!@?] regex src [level]
根据regex删除src中所有匹配的空文件和空目录。

file -ddon[+*!@?] regex src [level]
根据regex删除src中所有匹配的空目录。

file -m[+*!@?] regex src dest [level]
根据regex移动src中文件到dest中。

file -md[+*!@?] regex src dest [level]
根据regex移动src中所有匹配文件和目录及其中所有文件到dest中。

file -mdo[+*!@?] regex src dest [level]
根据regex移动src中所有匹配的目录及其中所有文件到dest中。

file -b[+*!@?] regex src dest backup [level]
根据regex获得src中所有匹配文件，检查这些文件在dest中是否存在，将不存在的文件备份到backup中。

file -bd[+*!@?] regex src dest backup [level]
根据regex获得src中所有匹配文件和目录及其中所有文件，检查这些文件和目录在dest中是否存在，将不存在的文件和目录备份到backup中。

file -bu[+*!@?] regex src dest backup [level]
根据regex获得src中所有匹配文件，检查这些文件在dest中是否能找到文件名称是以该文件名称为前缀的文件，若存在则先将dest中匹配的文件移动到backup中，再将该文件移动到dest中。

file -br[+*!@?] regex src dest backup [level]
根据regex获得src中所有匹配文件，检查这些文件在dest中是否能找到文件名称是该文件名称的前缀的文件，若存在则先将dest中匹配的文件移动到backup中，再将该文件移动到dest中。

file -u[+*!@?] regex src dest backup [level]
根据regex将src中所有匹配文件更新到dest中，更新时会先检查dest中是否已存在该文件，若存在则先将该文件备份到backup中，再更新之。

file -ud[+*!@?] regex src dest backup [level]
根据regex将src中所有匹配文件和目录及其中所有文件更新到dest中，更新时会先检查dest中是否已存在该文件，若存在则先将该文件备份到backup中，再更新之。

file -zd[+*!@?] regex src dest zipName [zipLevel] [level]
根据regex将src中所有匹配文件压缩到dest/zipName.zip文件中。

file -zdd[+*!@?] regex src dest zipName [zipLevel] [level]
根据regex将src中所有匹配文件和目录及其中所有文件压缩到dest/zipName.zip文件中。

file -zi[+*!@?] regex src dest [level]
根据regex将src中所有匹配文件解压缩到dest中。

file -pd[+*!@?] regex src dest zipName [zipLevel] [level]
根据regex将src中所有匹配文件打包到dest/zipName.pak文件中。

file -pdd[+*!@?] regex src dest zipName [zipLevel] [level]
根据regex将src中所有匹配文件和目录及其中所有文件打包到dest/zipName.pak文件中。

file -pi[+*!@?] regex src [level]
根据regex将src中所有匹配文件解包到该文件所在目录中。

单条命令示例：

file -f+ (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings"
查询该目录中名称以_cn.strings（忽略大小写）结尾的所有文件，.与strings中间可以包含0到2个任意字符。

file -fd+ (?i)strings$ "F:/games/Fallout 4"
查询该目录中名称以strings（忽略大小写）结尾的所有文件和目录及其中所有文件。

file -fdo+ . "F:/games/KingdomComeDeliverance/修改/Mods" 0 1
查询该目录中的第一级目录。

file -fpa+ . "F:/games/DARK SOULS REMASTERED" 20
查询该目录中的所有文件；显示文件的绝对路径名，且只显示前20条记录。

file -fpr+ . "F:/games/DARK SOULS REMASTERED"
查询该目录中的所有文件，显示文件的相对路径名（不包含该目录名称）。

file -fps+ . "F:/games/DARK SOULS REMASTERED"
查询该目录中的所有文件，显示文件的相对路径名（包含该目录名称）。

file -fpda+ . "F:/games/DARK SOULS REMASTERED"
查询该目录中的文件和目录及其中所有文件，显示文件或目录的绝对路径名。

file -fpdr+ . "F:/games/DARK SOULS REMASTERED"
查询该目录中的文件和目录及其中所有文件，显示文件或目录的相对路径名（不包含该目录名称）。

file -fpds+ . "F:/games/DARK SOULS REMASTERED"
查询该目录中的文件和目录及其中所有文件，显示文件或目录的相对路径名（包含该目录名称）。

file -fpdoa+ . "F:/games/DARK SOULS REMASTERED"
查询该目录中的所有目录，显示目录的绝对路径名。

file -fpdor+ . "F:/games/DARK SOULS REMASTERED"
查询该目录中的所有目录，显示目录的相对路径名（不包含该目录名称）。

file -fpdos+ . "F:/games/DARK SOULS REMASTERED"
查询该目录中的所有目录，显示目录的相对路径名（包含该目录名称）。

file -fsa+ . "F:/games/FINAL FANTASY XV" 1MB,1GB
查询该目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递增排序。

file -fsd+ . "F:/games/FINAL FANTASY XV" 1MB;1GB
查询该目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递减排序。

file -fdsa+ \Ajp$ "F:/games/FINAL FANTASY XV" 1MB,1GB
查询该目录中所有目录名为jp的目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递增排序。

file -fdsd+ \Ajp$ "F:/games/FINAL FANTASY XV" 1MB;1GB
查询该目录中所有目录名为jp的目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递减排序。

file -fddsa+ \Ajp$ "F:/games/DARK SOULS REMASTERED" 100KB;10MB
先查询该目录中的文件和第一级目录，再按文件或目录大小递增排序。

file -fddsd+ \Ajp$ "F:/games/DARK SOULS REMASTERED" 100KB;10MB
先查询该目录中的文件和第一级目录，再按文件或目录大小递减排序。

file -fdosa+ \Ajp$ "F:/games/DARK SOULS REMASTERED" 100KB;10MB
先查询该目录中的第一级目录，再按目录大小递增排序。

file -fdosd+ \Ajp$ "F:/games/DARK SOULS REMASTERED" 100KB;10MB
先查询该目录中的第一级目录，再按目录大小递减排序。

file -r (.*_)(?i)cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings" $1en$2
先查询（作用同-f）再以en替换掉所有匹配文件名中的cn（其余字符不变）。

file -rl (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings"
先查询（作用同-f）再将该目录中所有匹配文件名中英文字母替换为小写。

file -ru (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings"
先查询（作用同-f）再将该目录中所有匹配文件名中英文字母替换为大写。

file -ruf (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings"
先查询（作用同-f）再将该目录中所有匹配文件名中英单词首字母替换为大写。

file -rd (.*_)(?i)cn(\..{0,2}strings$) "F:/games/Fallout 4" $1en$2
先查询（作用同-fd）再以en替换掉所有匹配文件名和目录名中的cn（其余字符不变）。

file -rdl (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询（作用同-fd）再将该目录中所有匹配文件名和目录名中英文字母替换为小写。

file -rdu (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询（作用同-fd）再将该目录中所有匹配文件名和目录名中英文字母替换为大写。

file -rduf (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询（作用同-fd）再将该目录中所有匹配文件名和目录名中英单词首字母替换为大写。

file -rdo (.*_)(?i)cn(\..{0,2}strings$) "F:/games/Fallout 4" $1en$2
先查询（作用同-fd）再以en替换掉所有匹配的目录名中的cn（其余字符不变）。

file -rdol (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询（作用同-fd）再将该目录中所有匹配的目录名中英文字母替换为小写。

file -rdou (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询（作用同-fd）再将该目录中所有匹配的目录名中英文字母替换为大写。

file -rdouf (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询（作用同-fd）再将该目录中所有匹配的目录名中英单词首字母替换为大写。

file -c (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings" "F:/games/Fallout 4/备份"
先查询（作用同-f）再将 .../Strings 中所有匹配文件复制到 .../备份 目录中。

file -cd (?i).{0,2}strings$ "F:/games/Fallout 4/Data" "F:/games/Fallout 4/备份"
先查询（作用同-fd）再将 .../Data 中所有匹配文件和目录及其中所有文件复制到 .../备份 目录中。

file -cdo (?i).{0,2}strings$ "F:/games/Fallout 4/Data" "F:/games/Fallout 4/备份"
先查询（作用同-fd）再将 .../Data 中所有匹配的目录及其中所有文件复制到 .../备份 目录中。

file -d (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings"
先查询（作用同-f）再删除该目录中所有匹配文件。

file -dd "\Ade$|\Afr$|\Aru$|\Aus$" "F:/games/FINAL FANTASY XV"
先查询（作用同-fd）再删除该目录中所有匹配文件和目录及其中所有文件。

file -ddo "\Ade$|\Afr$|\Aru$|\Aus$" "F:/games/FINAL FANTASY XV"
先查询（作用同-fd）再删除该目录中所有匹配的目录及其中所有文件。

file -dn . "F:/games/FINAL FANTASY XV"
先查询（作用同-fd）再删除该目录中所有匹配的空文件。

file -ddn . "F:/games/FINAL FANTASY XV"
先查询（作用同-fd）再删除该目录中所有匹配的空文件和空目录。

file -ddon . "F:/games/FINAL FANTASY XV"
先查询（作用同-fd）再删除该目录中所有匹配的空目录。

file -m (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings" "F:/games/Fallout 4/备份"
先查询（作用同-f）再将 .../Strings中所有匹配文件移动到 .../备份 目录中。

file -md (?i).{0,2}strings$ "F:/games/Fallout 4/Data" "F:/games/Fallout 4/备份"
先查询（作用同-fd）再将 .../Data 中所有匹配文件和目录及其中所有文件移动到 .../备份 目录中。

file -mdo (?i).{0,2}strings$ "F:/games/Fallout 4/Data" "F:/games/Fallout 4/备份"
先查询（作用同-fd）再将 .../Data 中所有匹配的目录及其中所有文件移动到 .../备份 目录中。

file -b . "F:/games/FINAL FANTASY XV" "F:/迅雷下载/FINAL FANTASY XV" "F:/备份"
先查询（作用同-f）获得 F:/games/FINAL FANTASY XV 目录中所有匹配文件，检查这些文件在 F:/迅雷下载/FINAL FANTASY XV 目录中是否存在，将不存在的文件备份到 F:/备份 目录中。

file -bd \Adatas$ "F:/games/FINAL FANTASY XV" "F:/迅雷下载/FINAL FANTASY XV" "F:/备份"
先查询（作用同-fd）获得 F:/games/FINAL FANTASY XV 目录中所有匹配文件和目录及其中所有文件，检查这些文件和目录在 F:/迅雷下载/FINAL FANTASY XV 目录中是否存在，将不存在的文件和目录备份到 F:/备份 目录中。

file -bu . "F:/games/Resident Evil 4/修改/BIO4" "F:/games/Resident Evil 4/BIO4" "F:/games/Resident Evil 4/备份/BIO4"
先查询（作用同-f）获得 F:/games/Resident Evil 4/修改/BIO4 目录中所有匹配文件，检查这些文件在 F:/games/Resident Evil 4/BIO4 目录中是否能找到文件名称是以该文件名称为前缀的文件，若存在则先将 F:/games/Resident Evil 4/BIO4 目录中匹配的文件移动到 F:/games/Resident Evil 4/备份/BIO4 目录中，再将该文件移动到 F:/games/Resident Evil 4/BIO4 目录中。

file -br . "F:/games/Resident Evil 4/备份/BIO4" "F:/games/Resident Evil 4/BIO4" "F:/games/Resident Evil 4/修改/BIO4"
先查询（作用同-f）获得 F:/games/Resident Evil 4/备份/BIO4 目录中所有匹配文件，检查这些文件在 F:/games/Resident Evil 4/BIO4 目录中是否能找到文件名称是该文件名称的前缀的文件，若存在则先将 F:/games/Resident Evil 4/BIO4 目录中匹配的文件移动到 F:/games/Resident Evil 4/修改/BIO4 目录中，再将该文件移动到 F:/games/Resident Evil 4/BIO4 目录中。

file -u "F:/games/FINAL FANTASY XV" "F:/迅雷下载/FINAL FANTASY XV" "F:/备份"
先查询（作用同-f）再将 F:/games/FINAL FANTASY XV 目录中所有匹配文件更新到 F:/迅雷下载/FINAL FANTASY XV 中，若存在同名文件则先将该文件备份到 F:/备份 目录中，再更新之。

file -ud \Adatas$ "F:/games/FINAL FANTASY XV" "F:/迅雷下载/FINAL FANTASY XV" "F:/备份"
先查询（作用同-fd）再将 F:/games/FINAL FANTASY XV 目录中所有匹配文件和目录及其中所有文件更新到 F:/迅雷下载/FINAL FANTASY XV 中，若存在同名文件则先将该文件备份到 F:/备份 目录中，再更新之。

file -zd (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings" "F:/games/Fallout 4/备份" strings 1
先查询（作用同-f）再将 .../Strings 目录中所有匹配文件按压缩级别1压缩到 .../备份/strings.zip 文件中。

file -zdd (?i).{0,2}strings$ "F:/games/Fallout 4/Data" "F:/games/Fallout 4/备份" strings 1
先查询（作用同-fd）再将 .../Data 目录中所有匹配文件和目录及其中所有文件按压缩级别1压缩到 .../备份/strings.zip 文件中。

file -zi (?i)\.zip$ "F:/games/Fallout 4/备份" "F:/games/Fallout 4/Data"
先查询（作用同-f）再将 .../备份 目录中所有匹配文件解压缩到 .../Data 目录中。

file -pd . "F:/games/KingdomComeDeliverance/修改/Merge/Data" "F:/games/KingdomComeDeliverance/Mods/Merge/Data" merge 1
先查询（作用同-f）再将 .../修改/Merge/Data 目录中所有匹配文件打包到 .../Mods/Merge/Data/merge.pak 文件中。

file -pdd . "F:/games/KingdomComeDeliverance/修改/Merge/Data" "F:/games/KingdomComeDeliverance/Mods/Merge/Data" merge 1
先查询（作用同-fd）再将 .../修改/Merge/Data 目录中所有匹配文件和目录及其中所有文件打包到 .../Mods/Merge/Data/merge.pak 文件中。

file -pi (?i)\.pak$ "F:/games/KingdomComeDeliverance/修改/Mods"
先查询（作用同-f）再将 .../Mods 目录中所有匹配文件解包到该文件所在目录中。


输入 game run

参数说明：

run -c|-a|-d|-v|-x|-l|-la id path exe name [comment]

id		游戏标识，在./run.xml文件中唯一标识一个游戏配置节点。

path		游戏可执行文件路径。

exe		游戏可执行文件名称（不包含扩展名.exe）。

name		游戏中文名称。

comment		游戏快捷方式说明。

-c id path exe name [comment] 新建游戏配置文件./run.xml，并生成一个游戏配置节点。

-a id path exe name [comment] 添加一个游戏配置节点到游戏配置文件./run.xml中。

-d id 根据id删除游戏配置文件./run.xml中对应的一个游戏配置节点。

-v 显示游戏配置文件./run.xml中所有的游戏配置节点的id列表，显示格式为：id		comment。

-x [id] 根据id执行游戏配置文件./run.xml中对应的游戏；如果不指定id程序则会先显示id列表（同-v），再提示输入一个id，根据id执行对应的游戏。

-l [id] 根据id获得./run.xml中对应的游戏，并创建游戏快捷方式到桌面；如果不指定id程序则会先显示id列表（同-v），再提示输入一个id，根据id创建游戏快捷方式。

-la 批量创建游戏配置文件./run.xml中所有游戏的快捷方式到桌面。

示例：

run -c ew "F:/games/The Evil Within" EvilWithin 恶灵附身：开发者模式

run -a ew2 "F:/games/The Evil Within 2" TEW2 恶灵附身2：开发者模式

run -a lotf "F:/games/Lords Of The Fallen/bin" LordsOfTheFallen 堕落之王：开发者模式

run -a sg "F:/games/The Surge/bin" TheSurge 迸发：开发者模式

run -a skse "F:/games/Skyrim Special Edition" SkyrimSE 上古卷轴5：开发者模式

run -a poe2 "F:/games/Pillars of Eternity II" PillarsOfEternityII 永恒之柱2

run -a poe2-d "F:/games/Pillars of Eternity II" PillarsOfEternityII 永恒之柱2：开发者模式

run -d ew2

run -v

run -x

run -x ew

run -l

run -l ew

run -la


下面几个命令是针对具体的游戏的：

一、神界：原罪2

输入 game eoc

参数说明：

eoc -d|-r|-u src dest

-d      调试模式

-r      发布模式

-u      更新模式

src     输入文件路径名

dest    输出文件路径名

示例：

eoc -d "F:/games/Divinity Original Sin 2/修改/release/english.xml" "F:/games/Divinity Original Sin 2/修改/debug/english.xml"

eoc -r "F:/games/Divinity Original Sin 2/修改/debug/english.xml" "F:/games/Divinity Original Sin 2/修改/release/english.xml"

eoc -u "F:/games/Divinity Original Sin 2/修改/release/english.xml" "F:/games/Divinity Original Sin 2/修改/debug/english.xml"

更新模式说明：

主要针对调试模式的批量更新，会逐行检查调试模式中每个 content 的值，根据最开头的编号模式进行处理，如下：

1、编号模式形如 #12345#：替换编号后面的值；

2、编号模式形如 #*12345#：保留原值不替换；

3、编号模式形如 #+12345#：去掉编号左边的+号，再替换掉编号后面的值；

4、编号模式为其他形式：去掉编号并替换编号后面的值；

5、如果是新增的 content 记录（即找不到对应的 contentuid），将添加新纪录并自动生成新编号（编号值从 content记录数+1 开始累加），新编号形如 #+12345#。


二、天国：拯救

输入 game kcd

参数说明：

kcd -lm|-lma|-lmu|-lc|-lca|-lcu|-ld|-lr|-mc|-mp|-mu|-mma|-mmf|-mmc|-mmo|-mmu regex src dest merge gamePath modPath mergePath mergeExecutablePath

-lm                     全量合并翻译文件，合并所有；包含原有的、新增的和更新的记录。

-lma                    全量合并翻译文件，合并新增；包含原有的和新增的记录。

-lmu                    全量合并翻译文件，合并更新；包含原有的和更新的记录。

-lc                     差量合并翻译文件，合并所有；包含新增的和更新的记录，不包含原有的记录。

-lca                    差量合并翻译文件，合并新增；包含新增的记录，不包含原有的记录。

-lcu                    差量合并翻译文件，合并更新；包含更新的记录，不包含原有的记录。

-ld                     调试翻译模式。

-lr                     发布翻译模式。

-mc                     创建Mod配置文件./kcd.xml。

-mp                     先清空gamePath/Mods目录，再重新打包mergePath目录中文件到gamePath/Mods目录，并重新生成Mod排序文件mod_order.txt。

-mu                     先清空modPath目录，再重新解包该目录中所有Mod。

-mmf                    重新整合modPath目录中所有Mod，并重新合并所有冲突文件（即多个Mod共有的同名文件）。

-mma                    重新整合modPath目录中所有Mod，只重新合并变化了的冲突文件（即同名文件有更新、新增或删除）。

-mmc                    只合并Mod配置文件./kcd.xml中配置的所有冲突文件，不打包到gamePath/Mods目录。

-mmo                    根据Mod配置文件./kcd.xml中配置的Mod排序信息重新生成Mod排序文件mod_order.txt。

-mmu                    在mergePath目录中重新生成Mod配置文件./kcd.xml中配置的所有唯一文件（即非冲突文件），并更新这些文件的MD5码。

regex                   文件名查询正则表达式，.匹配任意文件名。

src                     文件输入目录。

dest                    文件输入或输出目录。

merge                   文件整合目录。

gamePath                游戏目录。

modPath                 Mod目录。

mergePath               Mod整合目录。

mergeExecutablePath     Mod整合工具KDiff3的可执行文件路径名。

合并翻译文件说明：先根据regex获得src和dest中匹配的所有文件，再将同名文件数据合并保存到merge中；全量合并与差量合并的区别是前者得到的合并文件是满足游戏 数据规范的文件（即可以直接作为Mod使用的文件），而后者得到的是中间文件，需要进行再编辑以满足游戏数据规范。

单条命令：

kcd -lm regex src dest merge

kcd -lma regex src dest merge

kcd -lmu regex src dest merge

kcd -lc regex src dest merge

kcd -lca regex src dest merge

kcd -lcu regex src dest merge

kcd -ld regex src dest

kcd -lr regex src dest

kcd -mc gamePath modPath mergePath mergeExecutablePath

kcd -mp

kcd -mu

kcd -mmf

kcd -mma

kcd -mmc

kcd -mmo

kcd -mmu

示例：

kcd -lm (?i)\.xml$ F:/games/KingdomComeDeliverance/修改/Mods/UltimateRealismOverhaul/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/merge

kcd -lc (?i)\.xml$ F:/games/KingdomComeDeliverance/修改/Mods/UltimateRealismOverhaul/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/diff

kcd -ld (?i)\.xml$ F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/debug

kcd -lr (?i)\.xml$ F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses/debug F:/games/KingdomComeDeliverance/修改/Game/Localization/Chineses

kcd -mc F:/games/KingdomComeDeliverance F:/games/KingdomComeDeliverance/修改/Mods F:/games/KingdomComeDeliverance/修改/Merge F:/tools/KDiff3/kdiff3.exe


三、永恒之柱2：死火

输入 game poe

参数说明：

poe -d|-e|-g regex src string

-d      解码（即格式化）JSON格式文件

-e      编码（即压缩为一行）JSON格式文件

-g      给自定义MOD对象生成36位的GUID字符串

regex   文件名查询正则表达式，.匹配任意文件名和目录名。

src     文件输入目录

string  自定义MOD对象名称字符串

单条命令：

poe -d regex src

poe -e regex src

poe -g string

示例：

poe -d (?i)\..*bundle$ "F:/games/Pillars of Eternity II/PillarsOfEternityII_Data/exported/design/gamedata"
将 .../gamedata 目录中文件扩展名以bundle结尾（忽略大小写）的所有文件进行解码。

poe -e (?i)\..*bundle$ "F:/games/Pillars of Eternity II/PillarsOfEternityII_Data/exported/design/gamedata"
将 .../gamedata 目录中文件扩展名以bundle结尾（忽略大小写）的所有文件进行编码。

poe -g Great_Sword_WarGod
获得自定义MOD对象名称Great_Sword_WarGod的GUID字符串。
```



