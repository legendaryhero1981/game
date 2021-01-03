# game

# 项目介绍

PC游戏Mod修改工具集命令行程序，目前基于64位JDK11开发，建议在64位windows10操作系统上运行。

# 部署说明

1、解包后用myeclipse导入game项目；

2、将game项目的src文件夹下所有.java文件打包成game.jar；

3、用exe4j将game.jar打包成game.exe，在Java invocation选项窗口中的主类选择Module path为legend/legend.Main，在下一个选项窗口JRE中指定最低的JRE版本为9；

4、将game.exe放置在任意目录，如：D:\tools，再将D:\tools\game.exe的文件路径名添加到系统环境变量中；

5、打开命令提示符，输入 game 可以看到命令帮助信息，现在已经可以像使用windows内置命令一样使用了！

# 功能描述

```
输入 game 可以看到命令帮助信息如下：

版本：V6.1
作者：李允
主页：知乎 https://www.zhihu.com/people/legendaryhero1981


命令参数：

regex          文件名正则查询表达式，.匹配任意文件名和目录名；引号等特殊字符可使用占位符表达式；各命令参数均可使用一对反引号来表示一个引用子字符串，程序会将引用字符串当成无特殊含义的普通字符串对待（即特殊字符占位符表达式和参数分隔符字符串等都将被当成普通字符串对待）；引用字符串匹配的正则表达式为：`(.*?)`。

目前支持的所有通用特殊字符占位符表达式（英文字母不区分大小写）如下：

#BQ=n#         英文反引号（`）占位符表达式，匹配的正则表达式为：(?i)#BQ=?([1-9]?)#；BQ表示反引号，n为个数，=可以不写；基于性能考虑，n的取值范围限定为1~9，表示替换为n个反引号；例如：#BQ#（替换为1个反引号）,#BQ1#（替换为1个反引号）,#BQ=2#（替换为2个反引号）。

#SQM=n#        英文单引号（'）占位符表达式，匹配的正则表达式为：(?i)#SQM=?([1-9]?)#；SQM表示单引号，n为个数，=可以不写；基于性能考虑，n的取值范围限定为1~9，表示替换为n个单引号；例如：#SQM#（替换为1个单引号）,#SQM1#（替换为1个单引号）,#SQM=2#（替换为2个单引号）。

#DQM=n#        英文双引号（"）占位符表达式，匹配的正则表达式为：(?i)#DQM=?([1-9]?)#；DQM表示双引号，n为个数，=可以不写；基于性能考虑，n的取值范围限定为1~9，表示替换为n个双引号；例如：#DQM#（替换为1个双引号）,#DQM1#（替换为1个双引号）,#DQM=2#（替换为2个双引号）。

src            文件输入目录；可使用特殊字符占位符表达式（见regex参数）。

dest           文件输入输出目录或输入输出文件路径名；可使用特殊字符占位符表达式（见regex参数）。

backup         文件备份输出目录；可使用特殊字符占位符表达式（见regex参数）。

zipName        压缩文件名（程序会根据命令选项自动添加文件扩展名.zip或.pak）；可使用特殊字符占位符表达式（见regex参数）。

zipLevel       文件压缩级别，取值0：不压缩，1~9：1为最低压缩率，9为最高压缩率；不指定则程序智能选择最佳压缩率。

limit          查询类命令（即命令选项以-f开头的命令）的查询结果显示数量限制，即显示前limit条记录；或同名文件替换类命令（见命令选项-rfsn）的最大查询层数，即只查询limit层目录结构；取值范围为：1~2147483647，不指定或超过取值范围则取默认值2147483647。

level          文件目录最大查询层数；取值范围为：1~2147483647，不指定或超过取值范围则取默认值2147483647。

sizeExpr       文件大小表达式，匹配的正则表达式为：(0|[1-9]\d*)([TGMKtgmk]?[Bb])?[,;-]?；取值范围为：0~9223372036854775807B，不指定则取默认值0B；例如：100B（不小于100字节），10KB（不小于10千字节），1-100MB（介于1兆字节到100兆字节之间），500MB;1GB（介于500兆字节到1千兆字节之间），2,1GB（介于2千兆字节到1千兆字节之间），800,800（等于800字节）。

split          分隔符字符串，可作为二维表格式文件中的列分隔符正则表达式，例如：[,;| \t]+；不指定则取默认值[ \t]+，即只使用空格或制表符作为列分隔符正则表达式；也可作为文件或目录名称中分隔名称和版本号的分隔符，不指定则取默认值空字符串（版本号从匹配的第一个数字字符串+英文点号字符开始，版本号左边的字符串则为名称）；可使用特殊字符占位符表达式（见regex参数）。

replacement    字符串替换表达式，可作为文件名正则替换表达式（可使用特殊字符占位符表达式（见regex参数））；也可作为字符集编码名称（见命令选项-rfcs）；还可作为二维表格式文件中的列字符串替换表达式，格式为：[列号表达式@@]规则1(参数列表)[;;规则2(参数列表) ... ;;规则n(参数列表)]；若不指定列号表达式则对所有列执行指定的规则；规则具备事务性，简单规则仅由1个原子规则组成，复合规则由多个原子规则组成且不能包含终止原子规则；各事务性规则通过;;分隔，复合规则中各原子规则通过=>分隔，各参数通过,,分隔；列号表达式匹配的正则表达式为：([1-9]\d*)(?:-([1-9]\d*))?,?；例如：1（取第1列）；1,3,5（取1、3、5列）；1-3（取1、2、3列）；1,4-6（取1、4、5、6列）。

目前支持的所有列字符串替换表达式专用的特殊字符占位符表达式（英文字母不区分大小写）如下：

#ENTER=n#      换行符占位符表达式（终止原子规则专用），匹配的正则表达式为：(?i)#ENTER=?([1-9]?)#；ENTER表示换行符，n为个数，=可以不写；基于性能考虑，n的取值范围限定为1~9，表示替换为n个换行符；例如：#ENTER#（替换为1个换行符）,#ENTER1#（替换为1个换行符）,#ENTER=2#（替换为2个换行符）。

#EMPTY#        空字符串占位符表达式（所有规则通用），匹配的正则表达式为：(?i)#EMPTY#；表示一个空字符串，一般作为命令参数占位符；例如：#EMPTY#（程序将自动替换为空字符串）。

目前支持的所有原子规则（英文字母不区分大小写）如下：

LOWER(query)                                                  将匹配query的列字符串中英文字母替换为小写，匹配的正则表达式为：(?i)(LOWER)(\((.*)\))?；可以不传参数query，即LOWER与LOWER(.)等效但更高效；query为正则查询表达式，可使用特殊字符占位符表达式（见regex参数）。

UPPER(query)                                                  将匹配query的列字符串中英文字母替换为大写，匹配的正则表达式为：(?i)(UPPER)(\((.*)\))?；可以不传参数query，即UPPER与UPPER(.)等效但更高效；query为正则查询表达式，可使用特殊字符占位符表达式（见regex参数）。

REPLACE(query,,replacement[,,mismatch])                       将匹配query的列字符串的子串替换为replacement，将不匹配query的列字符串替换为mismatch，mismatch可以不指定；匹配的正则表达式为：(?i)(REPLACE)\((.+)\)；query为正则查询表达式，replacement为正则替换表达式，mismatch表示不匹配query时则使用该字符串替换原始字符串；可使用特殊字符占位符表达式（见regex参数）。

SINGLE-ROW(replacement)                                       根据replacement重新生成每一行数据，匹配的正则表达式为：(?i)(SINGLE-ROW)\((.+)\)；此规则为终止原子规则使用，即只能放在规则列表的最后面；replacement为行数据正则替换表达式，可使用特殊字符占位符表达式（见regex参数）和列数据占位符表达式。

FINAL-SINGLE-ROW(replacement[,,join,,prefix,,suffix])         先根据replacement重新生成每一行数据（同规则SINGLE-ROW），再使用jion把所有行数据连接成一行，最后在这行数据的首尾分别加上prefix、suffix；replacement必须指定，jion、prefix、suffix可以不指定，若不指定jion则使用空字符串连接每一行数据；匹配的正则表达式为：(?i)(FINAL-SINGLE-ROW)\((.+)\)；此规则为终止原子规则使用，即只能放在规则列表的最后面；replacement为行数据正则替换表达式，可使用特殊字符占位符表达式（见regex参数）和列数据占位符表达式；jion为行数据连接字符串，prefix为前缀字符串，suffix为后缀字符串，均可使用特殊字符占位符表达式（见regex参数）。

DIST-FINAL-SINGLE-ROW(replacement[,,join,,prefix,,suffix])    该规则的作用同规则FINAL-SINGLE-ROW，区别在于它只会从根据replacement重新生成的每一行数据中取不重复的行数据。

MULTI-ROW(replacement)                                        该规则的作用同规则SINGLE-ROW，区别在于支持特殊字符占位符表达式#ENTER=n#，以便支持多行字符串。

FINAL-MULTI-ROW(replacement[,,join,,prefix,,suffix])          该规则的作用同规则FINAL-SINGLE-ROW，区别在于支持特殊字符占位符表达式#ENTER=n#，以便支持多行字符串。

DIST-FINAL-MULTI-ROW(replacement[,,join,,prefix,,suffix])     该规则的作用同规则DIST-FINAL-SINGLE-ROW，区别在于支持特殊字符占位符表达式#ENTER=n#，以便支持多行字符串。

目前支持的所有列数据占位符表达式如下：

#n.m#          提取通过执行原子规则获得的列数据，匹配的正则表达式为：#([1-9]\d*)\.(0|[1-9]\d*)#；n为列号，m为原子规则执行顺序号；m的最小值为0，最大值为原子规则执行总数；m取0表示提取第n列的原始数据；例如：#1.0#（提取第1列的原始数据），#1.1#(提取对第1列执行了第1条原子规则后得到的数据)。

#n-m1.m2#      提取通过执行复合规则获得的列数据，匹配的正则表达式为：#([1-9]\d*)-([1-9]\d*)(?:\.([1-9]\d*))?#；n为列号，m1为复合规则执行顺序号，m2为m1中原子规则的执行顺序号；m1的最小值为1，最大值为复合规则执行总数；m2的最小值为1，最大值为m1中原子规则执行总数，m2不指定则取最大值（即#n-m1#与#n-m1.max(m2)#等效）；例如：#1-1#（提取对第1列执行了第1条复合规则后得到的数据）；#1-1.1#（提取对第1列执行了第1条复合规则中的第1条原子规则后得到的数据）。


命令选项：

~ 可添加在命令选项末尾，表示显示最简明的信息，也不会显示进度条；优先级高于+和*；若未指定则默认显示进度条；可与!或@或?连用；例如：-fd~!@?。

+ 可添加在命令选项末尾，表示输出详细信息；优先级高于*；可与!或@或?连用；例如：-fd!+@?。

* 可添加在命令选项末尾，表示模拟执行命令，不进行实际操作，仅输出详细信息；优先级低于+和~；可与!或@或?连用；例如：-fd*?@!。

! 可添加在命令选项末尾，表示不匹配查询的根目录，可与~或+或*或@或?连用；例如：file -fd!+ . d:/games 不匹配games目录，只匹配该目录中的任意文件和子目录名称。

@ 可添加在命令选项末尾，表示缓存该命令的查询结果，供后面的命令复用；某些命令不能缓存或复用查询结果，程序将智能忽略掉；复用查询结果的命令将忽略与查询相关的命令参数regex和src；当后面某个命令使用了@时，则重新缓存查询结果；可与~或!或+或*或?连用；例如：-fd!@*?。

? 可添加在命令选项末尾，表示命令开始执行前启用询问模式（输入n或N跳过，否则继续，按回车键确认：）；可与~或!或+或*或@连用；例如：-fd!+@?。


组合命令：

可以组合多个命令选项和命令参数，一次连续执行多条命令；命令选项与各命令参数的个数必须相等；命令选项及各命令参数使用::分隔；可使用*复用最近一个明确的命令选项或命令参数，将其当作该命令的前缀使用，例如：-f::*d::*dsa等价于-f::-fd::-fdsa，g:/games::*/1::*/2等价于g:/games::g:/games/1::g:/games/2；单条命令未用到的命令参数使用?占位。

组合命令示例：

file -cd*@::*::* .::*::* g:/games::*::* d:/::e:/::f:/

file -zdd+::-c+@?::* .::`.zip`$::* g:/games::g:/file::* g:/file::*/1::*/2 games::?::? 0::?::? ?::1::*

file -zi*::-cd@*::* `.zip`$::.::* g:/file::g:/games::* g:/::e:/::f:/ 1::?::?


单条命令：

file -f[~+*!@?] regex src [limit] [level]
根据regex查找src中的文件。

file -fd[~+*!@?] regex src [limit] [level]
根据regex查找src中的文件和目录及其中所有文件，相对-f增加了目录名匹配，若目录名匹配，则该目录中所有文件和目录都自动被匹配。

file -fdo[~+*!@?] regex src [limit] [level]
根据regex查找src中的目录。

file -fs[~+*!@?] regex src dest [limit] [level]
根据regex查找src中的文件，且只匹配在desc目录的同一相对路径中存在的同名文件。

file -fsmd5[~+*!@?] regex src dest [limit] [level]
根据regex查找src中的文件，且只匹配在desc目录的同一相对路径中存在且文件内容相同的同名文件。

file -fds[~+*!@?] regex src dest [limit] [level]
根据regex查找src中的文件和目录及其中所有文件，相对-f增加了目录名匹配，若目录名匹配，则该目录中所有文件和目录都自动被匹配；且只匹配在desc目录的同一相对路径中存在的同名目录和文件。

file -fdos[~+*!@?] regex src dest [limit] [level]
根据regex查找src中的目录，且只匹配在desc目录的同一相对路径中存在的同名目录。

file -fdf[~+*!@?] regex src dest [limit] [level]
根据regex查找src中的文件，且只匹配在desc目录的同一相对路径中不存在的文件。

file -fdfmd5[~+*!@?] regex src dest [limit] [level]
根据regex查找src中的文件，且只匹配在desc目录的同一相对路径中存在且文件内容不同的同名文件。

file -fddf[~+*!@?] regex src dest [limit] [level]
根据regex查找src中的文件和目录及其中所有文件，相对-f增加了目录名匹配，若目录名匹配，则该目录中所有文件和目录都自动被匹配；且只匹配在desc目录的同一相对路径中不存在的目录和文件。

file -fdodf[~+*!@?] regex src dest [limit] [level]
根据regex查找src中的目录，且只匹配在desc目录的同一相对路径中不存在的目录。

file -fpa[~+*!@?] regex src [dest] [limit] [level]
根据regex查找src中的文件，显示文件的绝对路径名并将查询结果写入到文件dest。

file -fpr[~+*!@?] regex src [dest] [limit] [level]
根据regex查找src中的文件，显示文件的相对路径名并将查询结果写入到文件dest。

file -fpda[~+*!@?] regex src [dest] [limit] [level]
根据regex查找src中的文件和目录及其中所有文件（同-fd），显示文件或目录的绝对路径名并将查询结果写入到文件dest。

file -fpdr[~+*!@?] regex src [dest] [limit] [level]
根据regex查找src中的文件和目录及其中所有文件（同-fd），显示文件或目录的相对路径名并将查询结果写入到文件dest。

file -fpdoa[~+*!@?] regex src [dest] [limit] [level]
根据regex查找src中的目录（同-fdo），显示目录的绝对路径名并将查询结果写入到文件dest。

file -fpdor[~+*!@?] regex src [dest] [limit] [level]
根据regex查找src中的目录（同-fdo），显示目录的相对路径名并将查询结果写入到文件dest。

file -fsa[~+*!@?] regex src [sizeExpr] [limit] [level]
根据regex和sizeExpr查找src中的文件，按文件大小递增排序。

file -fsd[~+*!@?] regex src [sizeExpr] [limit] [level]
根据regex和sizeExpr查找src中的文件，按文件大小递减排序。

file -fdsa[~+*!@?] regex src [sizeExpr] [limit] [level]
根据regex和sizeExpr查找src中的文件和目录，按文件大小递增排序。

file -fdsd[~+*!@?] regex src [sizeExpr] [limit] [level]
根据regex和sizeExpr查找src中的文件和目录，按文件大小递减排序。

file -fddsa[~+*!@?] regex src [sizeExpr] [limit]
根据regex和sizeExpr查找src中的文件和第一级子目录，按文件和子目录大小递增排序。

file -fddsd[~+*!@?] regex src [sizeExpr] [limit]
根据regex和sizeExpr查找src中的文件和第一级子目录，按文件和子目录大小递减排序。

file -fdosa[~+*!@?] regex src [sizeExpr] [limit]
根据regex和sizeExpr查找src中的第一级子目录，按子目录大小递增排序。

file -fdosd[~+*!@?] regex src [sizeExpr] [limit]
根据regex和sizeExpr查找src中的第一级子目录，按子目录大小递减排序。

file -r[~+*!@?] regex src replacement [level]
根据regex和replacement重命名src中的文件。

file -rl[~+*!@?] regex src [level]
根据regex将src中所有匹配文件名中英文字母替换为小写；regex可最多指定9个捕获组，最左边为第1个捕获组，程序只会替换捕获组中的子串，如：(.*\.)txt$ 表示只替换文件名，不会替换扩展名txt；.*\.txt$则文件名和扩展名都会被替换；也适用于-ru和-ruf。

file -ru[~+*!@?] regex src [level]
根据regex将src中所有匹配文件名中英文字母替换为大写。

file -ruf[~+*!@?] regex src [level]
根据regex将src中所有匹配文件名中英文单词首字母替换为大写。

file -rd[~+*!@?] regex src replacement [level]
根据regex和replacement重命名src中的文件和目录。

file -rdl[~+*!@?] regex src [level]
根据regex将src中所有匹配文件名和目录名中英文字母替换为小写；regex可最多指定9个捕获组，最左边为第1个捕获组，程序只会替换捕获组中的子串，如：(.*)`.txt`$ 表示只替换文件名，不会替换扩展名.txt；.*`.txt`$则文件名和扩展名都会被替换；也适用于-rdu和-rduf。

file -rdu[~+*!@?] regex src [level]
根据regex将src中所有匹配文件名和目录名中英文字母替换为大写。

file -rduf[~+*!@?] regex src [level]
根据regex将src中所有匹配文件名和目录名中英文单词首字母替换为大写。

file -rdo[~+*!@?] regex src replacement [level]
根据regex和replacement重命名src中的目录。

file -rdol[~+*!@?] regex src [level]
根据regex将src中所有匹配的目录名中英文字母替换为小写。

file -rdou[~+*!@?] regex src [level]
根据regex将src中所有匹配的目录名中英文字母替换为大写。

file -rdouf[~+*!@?] regex src [level]
根据regex将src中所有匹配的目录名中英文单词首字母替换为大写。

file -rfbt[~+*!@?] regex src replacement [split] [level]
根据regex和replacement替换src中所有匹配的二维表格式文件中所有匹配的列。

file -rfil[~+*!@?] regex src [dest] [level]
根据配置文件dest自动替换src中所有文件名匹配regex的文件内容；若不指定dest，则根据配置文件./file-il.xml自动替换src中所有文件名匹配regex的文件内容，若配置文件./file-il.xml不存在，则会自动生成一个与该文件同名且同格式的模版文件。

file -rfsn[~+*!@?] regex src dest [limit] [level]
根据regex获得src中所有匹配文件，再使用这些文件替换dest中的所有同名文件；limit为dest的最大查询层数，level为src的最大查询层数。

file -rfmeg[~+*!@?] regex src [level]
根据regex获得src中所有匹配的配置文件，再逐一解析这些配置文件以完成三方文件内容的整合。

file -rfspk[~+*!@?] regex src [level]
根据regex获得src中所有匹配的配置文件，再逐一解析这些配置文件以完成.spk文件和其相对应的同名.stc文件的修改。

file -rfgbk[~+*!@?] regex src dest [level]
根据regex提取src目录中所有匹配文件中的简体中文字符串，并将去重复字符后的简体中文字符串以UTF-16LE编码格式保存到文件dest；若无匹配文件或所有匹配文件中都不存在简体中文字符串，则将简体中文字符串的全集保存到文件dest。

file -rfbig5[~+*!@?] regex src dest [level]
根据regex提取src目录中所有匹配文件中的繁体中文字符串，并将去重复字符后的繁体中文字符串以UTF-16LE编码格式保存到文件dest；若无匹配文件或所有匹配文件中都不存在繁体中文字符串，则将繁体中文字符串的全集保存到文件dest。

file -rfcs[~+*!@?] regex src replacement [level]
根据regex将src中所有匹配文件的字符集编码转换为replacement编码；建议replacement的取值范围为（英文字母不区分大小写）：GBK，BIG5，UTF-8（不带BOM），UTF-8B（带BOM），UTF-16LE（带BOM），UTF-16BE（带BOM）；原始文件字符集编码将被程序自动识别，目前不支持中文简繁编码之间的相互转换。

file -c[~+*!@?] regex src dest [level]
根据regex复制src中文件到dest中。

file -cd[~+*!@?] regex src dest [level]
根据regex复制src中所有匹配文件和目录及其中所有文件到dest中。

file -cdo[~+*!@?] regex src dest [level]
根据regex复制src中所有匹配的目录及其中所有文件到dest中。

file -d[~+*!@?] regex src [level]
根据regex删除src中所有匹配文件。

file -dd[~+*!@?] regex src [level]
根据regex删除src中所有匹配文件和目录及其中所有文件。

file -ddo[~+*!@?] regex src [level]
根据regex删除src中所有匹配的目录及其中所有文件。

file -dn[~+*!@?] regex src [level]
根据regex删除src中所有匹配的空文件。

file -ddn[~+*!@?] regex src [level]
根据regex删除src中所有匹配的空文件和空目录。

file -ddon[~+*!@?] regex src [level]
根据regex删除src中所有匹配的空目录。

file -dov[~+*!@?] regex src [split] [level]
根据regex和split删除src中所有匹配文件的旧版本。

file -ddov[~+*!@?] regex src [split] [level]
根据regex和split删除src中所有匹配文件和目录的旧版本。

file -ddoov[~+*!@?] regex src [split] [level]
根据regex和split删除src中所有匹配目录的旧版本。

file -m[~+*!@?] regex src dest [level]
根据regex移动src中文件到dest中。

file -md[~+*!@?] regex src dest [level]
根据regex移动src中所有匹配文件和目录及其中所有文件到dest中。

file -mdo[~+*!@?] regex src dest [level]
根据regex移动src中所有匹配的目录及其中所有文件到dest中。

file -iu[~+*!@?] regex src dest backup [level]
根据regex获得src中所有匹配文件，检查这些文件在dest中是否能找到文件名称是以该文件名称为前缀的文件，若存在则先将dest中匹配的文件移动到backup中，再将该文件移动到dest中。

file -ir[~+*!@?] regex src dest backup [level]
根据regex获得src中所有匹配文件，检查这些文件在dest中是否能找到文件名称是该文件名称的前缀的文件，若存在则先将dest中匹配的文件移动到backup中，再将该文件移动到dest中。

file -u[~+*!@?] regex src dest [backup] [level]
根据regex将src中所有匹配文件更新到dest中；若指定了backup，则更新时会先检查dest中是否已存在该文件，若存在则先将该文件备份到backup中，再更新之。

file -ud[~+*!@?] regex src dest [backup] [level]
根据regex将src中所有匹配文件和目录及其中所有文件更新到dest中；若指定了backup，则更新时会先检查dest中是否已存在该文件，若存在则先将该文件备份到backup中，再更新之。

file -zd[~+*!@?] regex src dest zipName [zipLevel] [level]
根据regex将src中所有匹配文件压缩到“dest/zipName.zip”文件中。

file -zdd[~+*!@?] regex src dest zipName [zipLevel] [level]
根据regex将src中所有匹配文件和目录及其中所有文件压缩到“dest/zipName.zip”文件中。

file -zi[~+*!@?] regex src dest [level]
根据regex将src中所有匹配的压缩文件解压缩到dest中。

file -zidir[~+*!@?] regex src dest [level]
根据regex将src中所有匹配的压缩文件解压缩到dest中，且压缩文件的解压缩路径按照压缩文件名分类；即该压缩文件的解压缩路径为“解压缩路径/压缩文件名”（不包含扩展名）。

file -zimd5[~+*!@?] regex src dest [level]
根据regex将src中所有匹配的压缩文件解压缩到dest中，且压缩文件的解压缩路径按照压缩文件内容对应的32位md5码；即该压缩文件的解压缩路径为“解压缩路径/压缩文件名.md5码”。

file -pd[~+*!@?] regex src dest zipName [zipLevel] [level]
根据regex将src中所有匹配文件打包到“dest/zipName.pak”文件中。

file -pdd[~+*!@?] regex src dest zipName [zipLevel] [level]
根据regex将src中所有匹配文件和目录及其中所有文件打包到“dest/zipName.pak”文件中。

file -pi[~+*!@?] regex src [level]
根据regex将src中所有匹配的压缩文件解包到该文件所在目录中。

file -pidir[~+*!@?] regex src [level]
根据regex将src中所有匹配的压缩文件解包到该文件所在目录中，且压缩文件的解压缩路径按照压缩文件名分类；即该压缩文件的解压缩路径为“压缩文件路径/压缩文件名”（不包含扩展名）。

file -pimd5[~+*!@?] regex src [level]
根据regex将src中所有匹配的压缩文件解包到该文件所在目录中，且压缩文件的解压缩路径按照压缩文件内容对应的32位md5码；即该压缩文件的解压缩路径为“压缩文件路径/压缩文件名.md5码”。

file -7zip[~+*!@?] regex src [level]
根据regex查找src中的文件，再逐一解析这些配置文件后并发调用7-Zip控制台程序执行压缩或解压缩命令。

file -dsrp[~+*!@?] regex src [level]
根据regex查找src中的文件，再逐一解析这些配置文件后并发调用黑暗之魂系列游戏的专用控制台程序执行打包或解包命令。

file -gl32[~+*!@?] regex src [level]
根据regex查找src中的文件，显示文件对应的36位GUID（英文字母全小写）。

file -gu32[~+*!@?] regex src [level]
根据regex查找src中的文件，显示文件对应的36位GUID（英文字母全大写）。

file -ml8[~+*!@?] regex src [level]
根据regex查找src中的文件，显示文件对应的8位MD5（英文字母全小写）。

file -mu8[~+*!@?] regex src [level]
根据regex查找src中的文件，显示文件对应的8位MD5（英文字母全大写）。

file -ml16[~+*!@?] regex src [level]
根据regex查找src中的文件，显示文件对应的16位MD5（英文字母全小写）。

file -mu16[~+*!@?] regex src [level]
根据regex查找src中的文件，显示文件对应的16位MD5（英文字母全大写）。

file -ml32[~+*!@?] regex src [level]
根据regex查找src中的文件，显示文件对应的32位MD5（英文字母全小写）。

file -mu32[~+*!@?] regex src [level]
根据regex查找src中的文件，显示文件对应的32位MD5（英文字母全大写）。

file -je[~+*!@?] regex src [level]
根据regex查找src中的文件，编码（即压缩为一行）JSON格式文件。

file -jd[~+*!@?] regex src [level]
根据regex查找src中的文件，解码（即格式化）JSON格式文件。


单条命令示例：

file -f+ (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings"
查询该目录中名称以_cn.strings（忽略大小写）结尾的所有文件，.与strings中间可以包含0到2个任意字符。

file -fd+ (?i)strings$ "F:/games/Fallout 4"
查询该目录中名称以strings（忽略大小写）结尾的所有文件和目录及其中所有文件。

file -fdo+ . "F:/games/KingdomComeDeliverance/修改/Mods" 0 1
查询该目录中的第一级目录。

file -fs+ . "F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data" "D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data"
查询“F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录中的所有文件；且只匹配在“D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录的同一相对路径中存在的同名文件。

file -fsmd5+ (?i)\.param$ "D:/Sekiro Shadows Die Twice/param/gameparam/gameparam-parambnd" "G:/games/DSParamEditor/gameparam-parambnd"
查询“D:/Sekiro Shadows Die Twice/param/gameparam/gameparam-parambnd”目录中的所有文件；且只匹配在“G:/games/DSParamEditor/gameparam-parambnd”目录的同一相对路径中存在且文件内容相同的同名文件。

file -fds+ . "F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data" "D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data"
查询“F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录中的所有文件；且只匹配在“D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录的同一相对路径中存在的同名目录和文件。

file -fdos+ . "F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data" "D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data"
查询“F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录中的所有文件；且只匹配在“D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录的同一相对路径中存在的同名目录。

file -fdf+ . "F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data" "D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data"
查询“F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录中的所有文件；且只匹配在“D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录的同一相对路径中不存在的文件。

file -fdfmd5+ (?i)\.param$ "D:/Sekiro Shadows Die Twice/param/gameparam/gameparam-parambnd" "G:/games/DSParamEditor/gameparam-parambnd"
查询“D:/Sekiro Shadows Die Twice/param/gameparam/gameparam-parambnd”目录中的所有文件；且只匹配在“G:/games/DSParamEditor/gameparam-parambnd”目录的同一相对路径中存在且文件内容不同的同名文件。

file -fddf+ . "F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data" "D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data"
查询“F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录中的所有文件；且只匹配在“D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录的同一相对路径中不存在的目录和文件。

file -fdodf+ . "F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data" "D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data"
查询“F:/games/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录中的所有文件；且只匹配在“D:/360安全浏览器下载/Pillars of Eternity II Deadfire/PillarsOfEternityII_Data”目录的同一相对路径中不存在的目录。

file -fpa+ . "F:/games/DARK SOULS REMASTERED" file-list.txt 20
查询该目录中的所有文件；显示文件的绝对路径名，且只显示前20条记录，并将查询结果写入到文件file-list.txt。

file -fpr+ . "F:/games/DARK SOULS REMASTERED" file-list.txt
查询该目录中的所有文件，显示文件的相对路径名并将查询结果写入到文件file-list.txt。

file -fpda+ . "F:/games/DARK SOULS REMASTERED" file-list.txt
查询该目录中的文件和目录及其中所有文件，显示文件或目录的绝对路径名并将查询结果写入到文件file-list.txt。

file -fpdr+ . "F:/games/DARK SOULS REMASTERED" file-list.txt
查询该目录中的文件和目录及其中所有文件，显示文件或目录的相对路径名并将查询结果写入到文件file-list.txt。

file -fpdoa+ . "F:/games/DARK SOULS REMASTERED" file-list.txt
查询该目录中的所有目录，显示目录的绝对路径名并将查询结果写入到文件file-list.txt。

file -fpdor+ . "F:/games/DARK SOULS REMASTERED" file-list.txt
查询该目录中的所有目录，显示目录的相对路径名并将查询结果写入到文件file-list.txt。

file -fsa+ . "F:/games/FINAL FANTASY XV" 1MB,1GB
查询该目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递增排序。

file -fsd+ . "F:/games/FINAL FANTASY XV" 1MB;1GB
查询该目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递减排序。

file -fdsa+ \Ajp$ "F:/games/FINAL FANTASY XV" 1MB,1GB
查询该目录中所有目录名为jp的目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递增排序。

file -fdsd+ \Ajp$ "F:/games/FINAL FANTASY XV" 1MB;1GB
查询该目录中所有目录名为jp的目录中大小介于1兆字节到1千兆字节之间的所有文件，再按文件大小递减排序。

file -fddsa+ \Ajp$ "F:/games/DARK SOULS REMASTERED" 100KB;10MB
先查询该目录中的文件和第一级子目录，再按文件或目录大小递增排序。

file -fddsd+ \Ajp$ "F:/games/DARK SOULS REMASTERED" 100KB;10MB
先查询该目录中的文件和第一级子目录，再按文件或目录大小递减排序。

file -fdosa+ \Ajp$ "F:/games/DARK SOULS REMASTERED" 100KB;10MB
先查询该目录中的第一级子目录，再按目录大小递增排序。

file -fdosd+ \Ajp$ "F:/games/DARK SOULS REMASTERED" 100KB;10MB
先查询该目录中的第一级子目录，再按目录大小递减排序。

file -r (.*_)(?i)cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings" $1en$2
先查询再以en替换掉所有匹配文件名中的cn（其余字符不变）。

file -rl (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings"
先查询再将该目录中所有匹配文件名中英文字母替换为小写。

file -ru (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings"
先查询再将该目录中所有匹配文件名中英文字母替换为大写。

file -ruf (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings"
先查询再将该目录中所有匹配文件名中英单词首字母替换为大写。

file -rd (.*_)(?i)cn(\..{0,2}strings$) "F:/games/Fallout 4" $1en$2
先查询再以en替换掉所有匹配文件名和目录名中的cn（其余字符不变）。

file -rdl (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询再将该目录中所有匹配文件名和目录名中英文字母替换为小写。

file -rdu (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询再将该目录中所有匹配文件名和目录名中英文字母替换为大写。

file -rduf (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询再将该目录中所有匹配文件名和目录名中英单词首字母替换为大写。

file -rdo (.*_)(?i)cn(\..{0,2}strings$) "F:/games/Fallout 4" $1en$2
先查询再以en替换掉所有匹配的目录名中的cn（其余字符不变）。

file -rdol (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询再将该目录中所有匹配的目录名中英文字母替换为小写。

file -rdou (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询再将该目录中所有匹配的目录名中英文字母替换为大写。

file -rdouf (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4"
先查询再将该目录中所有匹配的目录名中英单词首字母替换为大写。

file -rfbt* (?i)\A`temp1.txt`$ E:/Decompile/DLL-ildasm "1@@LOWER;;UPPER=>REPLACE(\.,,_);;SINGLE-ROW(String INST_#1-1# = #DQM##1.1##DQM#;)" "\t+" 1
先查询再对该目录中名称（忽略大小写）为temp1.txt的文件数据执行一系列有序的替换规则：
1、对每行的第1列数据执行原子规则LOWER：将英文字母全部替换为小写；
2、对每行的第1列数据执行复合规则UPPER=>REPLACE：先将英文字母替换为大写，再将.替换为_；
3、对每行数据执行原子规则SINGLE-ROW：将数据替换为String INST_#1-1# = #DQM##1.1##DQM#;；
例如：temp1.txt文件中有1行数据为：“Beq.S	如果两个值相等，则将控制转移到目标指令（短格式）。”，则执行命令后该文件数据变为：“String INST_BEQ_S = "beq.s"”。

file -rfbt* (?i)\A`temp1.txt`$ E:/Decompile/DLL-ildasm "1@@UPPER=>REPLACE(\.,,_);;SINGLE-ROW(addInstruction(INST_#1-1#,#DQM##2.0##DQM#,#DQM=2#);)" "\t+" 1
先查询再对该目录中名称（忽略大小写）为temp1.txt的文件数据执行一系列有序的替换规则：
1、对每行的第1列数据执行复合规则UPPER=>REPLACE：先将英文字母替换为大写，再将.替换为_；
2、对每行数据执行原子规则SINGLE-ROW：将数据替换为addInstruction(INST_#1-1#,#DQM##2.0##DQM#,#DQM=2#);；
例如：temp1.txt文件中有1行数据为：“Beq.S	如果两个值相等，则将控制转移到目标指令（短格式）。”，则执行命令后该文件数据变为：“addInstruction(INST_BEQ_S,"如果两个值相等，则将控制转移到目标指令（短格式）。","");”。

file -rfbt* (?i)`native.log`$ d:/games  "REPLACE(.*?--initialize-at-run-time=(.+?) .*,,$1,,#EMPTY#);;FINAL-SINGLE-ROW(#1.1#,,`,`,,--initialize-at-run-time=)" "\n" 1
先查询再对该目录中名称（忽略大小写）为native.log的文件数据执行一系列有序的替换规则：
1、对每行的第1列数据执行原子规则REPLACE：将所有匹配的列字符串替换为捕获组1，且将所有不匹配的列字符串替换为空字符串；
2、对每行数据执行原子规则FINAL-SINGLE-ROW：将数据替换为--initialize-at-run-time=concat(#1.1#,',')；
例如：native.log文件中有3行数据为：“Detailed message:
Error: Class initialization of com.sun.xml.bind.v2.ContextFactory failed. Use the option --initialize-at-run-time=com.sun.xml.bind.v2.ContextFactory to explicitly request delayed initialization of this class.
Error: Class initialization of legend.Main failed. Use the option --initialize-at-run-time=legend.Main to explicitly request delayed initialization of this class.”，则执行命令后该文件数据变为最终一行数据：“--initialize-at-run-time=com.sun.xml.bind.v2.ContextFactory,legend.Main”。

file -rfbt* (?i)`native.log`$ d:/games  "REPLACE(.*?--initialize-at-run-time=(.+?) .*,,$1,,#EMPTY#);;DIST-FINAL-SINGLE-ROW(#1.1#,,`,`,,--initialize-at-run-time=)" "\n" 1
先查询再对该目录中名称（忽略大小写）为native.log的文件数据执行一系列有序的替换规则：
1、对每行的第1列数据执行原子规则REPLACE：将所有匹配的列字符串替换为捕获组1，且将所有不匹配的列字符串替换为空字符串；
2、对每行数据执行原子规则DIST-FINAL-SINGLE-ROW：将数据替换为--initialize-at-run-time=concat(#1.1#,',')；
例如：native.log文件中有3行数据为：“Detailed message:
Error: Class initialization of com.sun.xml.bind.v2.ContextFactory failed. Use the option --initialize-at-run-time=com.sun.xml.bind.v2.ContextFactory to explicitly request delayed initialization of this class.
Error: Class initialization of com.sun.xml.bind.v2.ContextFactory failed. Use the option --initialize-at-run-time=com.sun.xml.bind.v2.ContextFactory to explicitly request delayed initialization of this class.”，则执行命令后该文件数据变为最终一行数据：“--initialize-at-run-time=com.sun.xml.bind.v2.ContextFactory”。

file -rfil* (?i)`.il`$ E:/Decompile/DLL-ildasm
根据配置文件./file-il.xml自动替换“E:/Decompile/DLL-ildasm”目录中所有文件扩展名为.il的文件内容。

file -rfil* (?i)`.il`$ E:/Decompile/DLL-ildasm E:/Decompile/DLL-ildasm/il.xml
根据配置文件il.xml自动替换“E:/Decompile/DLL-ildasm”目录中所有文件扩展名为.il的文件内容。

file -rfsn* (?i)\A`JetBrains.Platform.Shell.dll`$ E:/Decompile/ReSharper C:/Users/liyun/AppData/Local/JetBrains/Installations 2
先查询获得“.../ReSharper”目录中所有匹配文件，再使用这些文件替换“.../Installations”目录及其第一层子目录中的所有同名文件。

file -rfmeg* (?i)`file-merge.xml`$ . 1
先查询获得当前目录中（不包含子目录）文件名以file-merge.xml结尾（英文字母忽略大小写）的所有配置文件，再逐一解析这些配置文件以完成三方文件内容的整合。

file -rfspk* (?i)`file-spk.xml`$ . 1
先查询获得当前目录中（不包含子目录）文件名以file-spk.xml结尾（英文字母忽略大小写）的所有配置文件，再逐一解析这些配置文件以完成.spk文件和其相对应的同名.stc文件的修改。

file -rfgbk* (?i)`.json`$ "E:/Decompile/Code/IL/Pathfinder Kingmaker" D:/games/font_schinese.txt
提取“.../Pathfinder Kingmaker”目录中所有文件扩展名为.json的文件中的简体中文字符串，并将去重复字符后的简体中文字符串以UTF-16LE编码格式保存到文件“.../font_schinese.txt”。

file -rfbig5* (?i)`.json`$ "E:/Decompile/Code/IL/Pathfinder Kingmaker" D:/games/font_tchinese.txt
提取“.../Pathfinder Kingmaker”目录中所有文件扩展名为.json的文件中的繁体中文字符串，并将去重复字符后的繁体中文字符串以UTF-16LE编码格式保存到文件“.../font_tchinese.txt”。

file -rfcs* (?i)`.txt`$ E:/Decompile/DLL-ildasm gbk
先查询再将“E:/Decompile/DLL-ildasm”目录中所有扩展名为.txt的文件的字符集编码转换为gbk编码。

file -c (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings" "F:/games/Fallout 4/备份"
先查询再将“.../Strings”目录中所有匹配文件复制到“.../备份”目录中。

file -cd (?i).{0,2}strings$ "F:/games/Fallout 4/Data" "F:/games/Fallout 4/备份"
先查询再将“.../Data”目录中所有匹配文件和目录及其中所有文件复制到“.../备份”目录中。

file -cdo (?i).{0,2}strings$ "F:/games/Fallout 4/Data" "F:/games/Fallout 4/备份"
先查询再将“.../Data”目录中所有匹配的目录及其中所有文件复制到“.../备份”目录中。

file -d (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings"
先查询再删除该目录中所有匹配文件。

file -dd "\Ade$|\Afr$|\Aru$|\Aus$" "F:/games/FINAL FANTASY XV"
先查询再删除该目录中所有匹配文件和目录及其中所有文件。

file -ddo "\Ade$|\Afr$|\Aru$|\Aus$" "F:/games/FINAL FANTASY XV"
先查询再删除该目录中所有匹配的目录及其中所有文件。

file -dn . "F:/games/FINAL FANTASY XV"
先查询再删除该目录中所有匹配的空文件。

file -ddn . "F:/games/FINAL FANTASY XV"
先查询再删除该目录中所有匹配的空文件和空目录。

file -ddon . "F:/games/FINAL FANTASY XV"
先查询再删除该目录中所有匹配的空目录。

file -dov!* . "E:/Java/eclipse/SpringToolSuite/plugins" _
先查询再删除该目录中所有匹配文件的旧版本。

file -ddov!* . "E:/Java/eclipse/SpringToolSuite/plugins" _
先查询再删除该目录中所有匹配文件和目录的旧版本。

file -ddoov!* . "E:/Java/eclipse/SpringToolSuite/plugins" _
先查询再删除该目录中所有匹配目录的旧版本。

file -m (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings" "F:/games/Fallout 4/备份"
先查询再将“.../Strings”目录中所有匹配文件移动到“.../备份”目录中。

file -md (?i).{0,2}strings$ "F:/games/Fallout 4/Data" "F:/games/Fallout 4/备份"
先查询再将“.../Data”目录中所有匹配文件和目录及其中所有文件移动到“.../备份”目录中。

file -mdo (?i).{0,2}strings$ "F:/games/Fallout 4/Data" "F:/games/Fallout 4/备份"
先查询再将“.../Data”目录中所有匹配的目录及其中所有文件移动到“.../备份”目录中。

file -iu . "F:/games/Resident Evil 4/修改/BIO4" "F:/games/Resident Evil 4/BIO4" "F:/games/Resident Evil 4/备份/BIO4"
先查询获得“F:/games/Resident Evil 4/修改/BIO4”目录中所有匹配文件，检查这些文件在“F:/games/Resident Evil 4/BIO4”目录中是否能找到文件名称是以该文件名称为前缀的文件，若存在则先将“F:/games/Resident Evil 4/BIO4”目录中匹配的文件移动到“F:/games/Resident Evil 4/备份/BIO4”目录中，再将该文件移动到“F:/games/Resident Evil 4/BIO4”目录中。

file -ir . "F:/games/Resident Evil 4/备份/BIO4" "F:/games/Resident Evil 4/BIO4" "F:/games/Resident Evil 4/修改/BIO4"
先查询获得“F:/games/Resident Evil 4/备份/BIO4”目录中所有匹配文件，检查这些文件在“F:/games/Resident Evil 4/BIO4”目录中是否能找到文件名称是该文件名称的前缀的文件，若存在则先将“F:/games/Resident Evil 4/BIO4”目录中匹配的文件移动到“F:/games/Resident Evil 4/修改/BIO4”目录中，再将该文件移动到“F:/games/Resident Evil 4/BIO4”目录中。

file -u "F:/games/FINAL FANTASY XV" "F:/迅雷下载/FINAL FANTASY XV" "F:/备份"
先查询再将“F:/games/FINAL FANTASY XV”目录中所有匹配文件更新到“F:/迅雷下载/FINAL FANTASY XV”中，若存在同名文件则先将该文件备份到“F:/备份”目录中，再更新之。

file -ud \Adatas$ "F:/games/FINAL FANTASY XV" "F:/迅雷下载/FINAL FANTASY XV" "F:/备份"
先查询再将“F:/games/FINAL FANTASY XV”目录中所有匹配文件和目录及其中所有文件更新到“F:/迅雷下载/FINAL FANTASY XV”中，若存在同名文件则先将该文件备份到“F:/备份”目录中，再更新之。

file -zd (?i)_cn(\..{0,2}strings$) "F:/games/Fallout 4/Data/Strings" "F:/games/Fallout 4/备份" strings 1
先查询再将“.../Strings”目录中所有匹配文件按压缩级别1压缩到“.../备份/strings.zip”文件中。

file -zdd (?i).{0,2}strings$ "F:/games/Fallout 4/Data" "F:/games/Fallout 4/备份" strings 1
先查询再将“.../Data”目录中所有匹配文件和目录及其中所有文件按压缩级别1压缩到“.../备份/strings.zip”文件中。

file -zi (?i)`.zip`$ "F:/games/Fallout 4/备份" "F:/games/Fallout 4/Data"
先查询再将“.../备份”目录中所有匹配文件解压缩到“.../Data”目录中。

file -zidir (?i)`.zip`$ "F:/games/Fallout 4/备份" "F:/games/Fallout 4/Data"
先查询再将“.../备份”目录中所有匹配文件解压缩到“ .../Data/压缩文件名”（不包含扩展名）目录中。

file -zimd5 (?i)`.zip`$ "F:/games/Fallout 4/备份" "F:/games/Fallout 4/Data"
先查询再将“.../备份”目录中所有匹配文件解压缩到“.../Data/压缩文件名.md5码”目录中。

file -pd . "F:/games/KingdomComeDeliverance/修改/Merge/Data" "F:/games/KingdomComeDeliverance/Mods/Merge/Data" merge 1
先查询再将“.../修改/Merge/Data”目录中所有匹配文件打包到“.../Mods/Merge/Data/merge.pak”文件中。

file -pdd . "F:/games/KingdomComeDeliverance/修改/Merge/Data" "F:/games/KingdomComeDeliverance/Mods/Merge/Data" merge 1
先查询再将“.../修改/Merge/Data”目录中所有匹配文件和目录及其中所有文件打包到“.../Mods/Merge/Data/merge.pak”文件中。

file -pi (?i)`.pak`$ "F:/games/KingdomComeDeliverance/修改/Mods"
先查询再将“.../Mods”目录中所有匹配文件解包到该文件所在目录中。

file -pidir (?i)`.pak`$ "F:/games/KingdomComeDeliverance/修改/Mods"
先查询再将“ .../Mods”目录中所有匹配文件解包到“该文件所在目录/压缩文件名”（不包含扩展名）中。

file -pimd5 (?i)`.pak`$ "F:/games/KingdomComeDeliverance/修改/Mods"
先查询再将“.../Mods”目录中所有匹配文件解包到“该文件所在目录/压缩文件名.md5码”中。

file -7zip+ (?i)`file-7zip.xml`$ . 1
先查询获得当前目录中（不包含子目录）文件名以file-7zip.xml结尾（英文字母忽略大小写）的所有配置文件，再逐一解析这些配置文件后并发调用7-Zip控制台程序执行压缩或解压命令。

file -dsrp+ (?i)`file-dsrp.xml`$ . 1
先查询获得当前目录中（不包含子目录）文件名以file-dsrp.xml结尾（英文字母忽略大小写）的所有配置文件，再逐一解析这些配置文件后并发调用黑暗之魂系列游戏的专用控制台程序执行打包或解包命令。

file -gl32+ (?i)`assembly-csharp.dll` "F:/games/Pathfinder Kingmaker Beneath the Stolen Lands/Kingmaker_Data/Managed"
显示该目录中名称为Assembly-CSharp.dll的文件对应的36位GUID（英文字母全小写）。

file -gu32+ (?i)`assembly-csharp.dll` "F:/games/Pathfinder Kingmaker Beneath the Stolen Lands/Kingmaker_Data/Managed"
显示该目录中名称为Assembly-CSharp.dll的文件对应的36位GUID（英文字母全大写）。

file -ml8+ (?i)`assembly-csharp.dll` "F:/games/Pathfinder Kingmaker Beneath the Stolen Lands/Kingmaker_Data/Managed"
显示该目录中名称为Assembly-CSharp.dll的文件对应的8位MD5（英文字母全小写）。

file -mu8+ (?i)`assembly-csharp.dll` "F:/games/Pathfinder Kingmaker Beneath the Stolen Lands/Kingmaker_Data/Managed"
显示该目录中名称为Assembly-CSharp.dll的文件对应的8位MD5（英文字母全大写）。

file -ml16+ (?i)`assembly-csharp.dll` "F:/games/Pathfinder Kingmaker Beneath the Stolen Lands/Kingmaker_Data/Managed"
显示该目录中名称为Assembly-CSharp.dll的文件对应的16位MD5（英文字母全小写）。

file -mu16+ (?i)`assembly-csharp.dll` "F:/games/Pathfinder Kingmaker Beneath the Stolen Lands/Kingmaker_Data/Managed"
显示该目录中名称为Assembly-CSharp.dll的文件对应的16位MD5（英文字母全大写）。

file -ml32+ (?i)`assembly-csharp.dll` "F:/games/Pathfinder Kingmaker Beneath the Stolen Lands/Kingmaker_Data/Managed"
显示该目录中名称为Assembly-CSharp.dll的文件对应的32位MD5（英文字母全小写）。

file -mu32+ (?i)`assembly-csharp.dll` "F:/games/Pathfinder Kingmaker Beneath the Stolen Lands/Kingmaker_Data/Managed"
显示该目录中名称为Assembly-CSharp.dll的文件对应的32位MD5（英文字母全大写）。

file -je (?i)\..*bundle$ "g:/games/Pillars of Eternity II"
查询该目录中名称以.bundle结尾（.与bundle之间可以包含0或多个字符）的所有文件，编码（即压缩为一行）JSON格式文件。

file -jd (?i)\..*bundle$ "g:/games/Pillars of Eternity II"
查询该目录中名称以.bundle结尾（.与bundle之间可以包含0或多个字符）的所有文件，解码（即格式化）JSON格式文件。


输入 game run

参数说明：

run -c|-a|-d|-v|-k|-x|-l|-la id path exe name [comment]

id      游戏标识，在./run.xml文件中唯一标识一个游戏配置节点。

path    游戏可执行文件路径。

exe     游戏可执行文件名称（不包含扩展名.exe）。

name    游戏中文名称。

comment 游戏快捷方式说明。

-c id path exe name [comment] 新建游戏配置文件./run.xml，并生成一个游戏配置节点。

-a id path exe name [comment] 添加一个游戏配置节点到游戏配置文件./run.xml中。

-v 显示游戏配置文件./run.xml中所有的游戏配置节点的id列表，显示格式为：id		comment。

-d [id] 根据id删除游戏配置文件./run.xml中对应的一个游戏配置节点；如果不指定id程序则会先显示id列表（同-v），再提示输入一个id，根据id删除对应的游戏配置节点。

-k [id] 根据id终止游戏配置文件./run.xml中对应的的游戏进程；如果不指定id程序则会先显示id列表（同-v），再提示输入一个id，根据id终止对应的游戏进程。

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

run -v

run -d

run -d ew2

run -k

run -k ew

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
