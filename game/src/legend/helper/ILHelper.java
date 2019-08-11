package legend.helper;

import static java.nio.file.Paths.get;
import static legend.util.ConsoleUtil.CS;
import static legend.util.FileUtil.existsPath;
import static legend.util.JaxbUtil.convertToJavaBean;
import static legend.util.JaxbUtil.convertToXml;
import static legend.util.ValueUtil.isEmpty;
import static legend.util.ValueUtil.nonEmpty;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

import legend.helper.entity.Helper;
import legend.helper.entity.IL;
import legend.helper.intf.IILHelper;

public class ILHelper implements IILHelper{
    private static final Path ilPath;
    private static final IL il;
    private static final StringBuilder cache;
    private static final HelperComparator helperComparator;
    static{
        ilPath = get(IL_FILE_CONFIG);
        if(existsPath(ilPath)) il = convertToJavaBean(ilPath,IL.class);
        else il = new IL();
        cache = new StringBuilder();
        helperComparator = new HelperComparator();
    }

    public static void main(String[] args){
        runCmd(args);
    }

    public static void runCmd(String[] args){
        if(il.getInstructions().isEmpty()) generateIL();
        Optional<String[]> optional = Optional.of(args);
        optional.filter(s->isEmpty(s)).ifPresent(s->{
            cache.append(HELP_IL);
            cache.append(DESC_CMD_INST);
            il.getInstructions().stream().sorted(helperComparator).forEach(helper->cache.append(helper));
            cache.append(DESC_CMD_STAT);
            il.getStatements().stream().sorted(helperComparator).forEach(helper->cache.append(helper));
            CS.showHelp(cache.toString());
        });
        switch(args[0]){
            case CMD_INST:
            optional.filter(s->s.length == 1).ifPresent(s->{
                cache.append(DESC_CMD_INST);
                il.getInstructions().stream().sorted(helperComparator).forEach(helper->cache.append(helper));
                CS.sl(cache.toString());
            });
            optional.filter(s->s.length > 1).ifPresent(s->{
                Helper helper = il.refreshInstructionMap().get(s[1]);
                if(nonEmpty(helper)) CS.showHelp(helper.toString());
            });
            break;
            case CMD_STAT:
            optional.filter(s->s.length == 1).ifPresent(s->{
                cache.append(DESC_CMD_STAT);
                il.getStatements().stream().sorted(helperComparator).forEach(helper->cache.append(helper));
                CS.sl(cache.toString());
            });
            optional.filter(s->s.length > 1).ifPresent(s->{
                Helper helper = il.refreshStatementMap().get(s[1]);
                if(nonEmpty(helper)) CS.showHelp(helper.toString());
            });
            break;
            default:
            CS.sl(HELP_IL);
        }
    }

    private static void generateIL(){
        // 生成IL指令帮助信息
        addInstruction(INST_ADD,"将两个值相加并将结果推送到计算堆栈上。","");
        addInstruction(INST_ADD_OVF,"将两个整数相加，执行溢出检查，并且将结果推送到计算堆栈上。","");
        addInstruction(INST_ADD_OVF_UN,"将两个无符号整数值相加，执行溢出检查，并且将结果推送到计算堆栈上。","");
        addInstruction(INST_AND,"计算两个值的按位“与”并将结果推送到计算堆栈上。","");
        addInstruction(INST_ARGLIST,"返回指向当前方法的参数列表的非托管指针。","");
        addInstruction(INST_BEQ,"如果两个值相等，则将控制转移到目标指令。","");
        addInstruction(INST_BEQ_S,"如果两个值相等，则将控制转移到目标指令（短格式）。","");
        addInstruction(INST_BGE,"如果第一个值大于或等于第二个值，则将控制转移到目标指令。","");
        addInstruction(INST_BGE_S,"如果第一个值大于或等于第二个值，则将控制转移到目标指令（短格式）。","");
        addInstruction(INST_BGE_UN,"当比较无符号整数值或不可排序的浮点型值时，如果第一个值大于第二个值，则将控制转移到目标指令。","");
        addInstruction(INST_BGE_UN_S,"当比较无符号整数值或不可排序的浮点型值时，如果第一个值大于第二个值，则将控制转移到目标指令（短格式）。","");
        addInstruction(INST_BGT,"如果第一个值大于第二个值，则将控制转移到目标指令。","");
        addInstruction(INST_BGT_S,"如果第一个值大于第二个值，则将控制转移到目标指令（短格式）。","");
        addInstruction(INST_BGT_UN,"当比较无符号整数值或不可排序的浮点型值时，如果第一个值大于第二个值，则将控制转移到目标指令。","");
        addInstruction(INST_BGT_UN_S,"当比较无符号整数值或不可排序的浮点型值时，如果第一个值大于第二个值，则将控制转移到目标指令（短格式）。","");
        addInstruction(INST_BLE,"如果第一个值小于或等于第二个值，则将控制转移到目标指令。","");
        addInstruction(INST_BLE_S,"如果第一个值小于或等于第二个值，则将控制转移到目标指令（短格式）。","");
        addInstruction(INST_BLE_UN,"当比较无符号整数值或不可排序的浮点型值时，如果第一个值小于或等于第二个值，则将控制转移到目标指令。","");
        addInstruction(INST_BLE_UN_S,"当比较无符号整数值或不可排序的浮点值时，如果第一个值小于或等于第二个值，则将控制权转移到目标指令（短格式）。","");
        addInstruction(INST_BLT,"如果第一个值小于第二个值，则将控制转移到目标指令。","");
        addInstruction(INST_BLT_S,"如果第一个值小于第二个值，则将控制转移到目标指令（短格式）。","");
        addInstruction(INST_BLT_UN,"当比较无符号整数值或不可排序的浮点型值时，如果第一个值小于第二个值，则将控制转移到目标指令。","");
        addInstruction(INST_BLT_UN_S,"当比较无符号整数值或不可排序的浮点型值时，如果第一个值小于第二个值，则将控制转移到目标指令（短格式）。","");
        addInstruction(INST_BNE_UN,"当两个无符号整数值或不可排序的浮点型值不相等时，将控制转移到目标指令。","");
        addInstruction(INST_BNE_UN_S,"当两个无符号整数值或不可排序的浮点型值不相等时，将控制转移到目标指令（短格式）。","");
        addInstruction(INST_BOX,"将值类转换为对象引用（O 类型）。","");
        addInstruction(INST_BR,"无条件地将控制转移到目标指令。","");
        addInstruction(INST_BR_S,"无条件地将控制转移到目标指令（短格式）。","");
        addInstruction(INST_BREAK,"向公共语言结构 (CLI) 发出信号以通知调试器已撞上了一个断点。","");
        addInstruction(INST_BRFALSE,"如果 value 为 false、空引用（Visual Basic 中的 Nothing）或零，则将控制转移到目标指令。","");
        addInstruction(INST_BRFALSE_S,"如果 value 为 false、空引用或零，则将控制转移到目标指令。","");
        addInstruction(INST_BRTRUE,"如果 value 为 true、非空或非零，则将控制转移到目标指令。","");
        addInstruction(INST_BRTRUE_S,"如果 value 为 true、非空或非零，则将控制转移到目标指令（短格式）。","");
        addInstruction(INST_CALL,"调用由传递的方法说明符指示的方法。","");
        addInstruction(INST_CALLI,"通过调用约定描述的参数调用在计算堆栈上指示的方法（作为指向入口点的指针）。","");
        addInstruction(INST_CALLVIRT,"对对象调用后期绑定方法，并且将返回值推送到计算堆栈上。","");
        addInstruction(INST_CASTCLASS,"尝试将引用传递的对象转换为指定的类。","");
        addInstruction(INST_CEQ,"比较两个值。如果这两个值相等，则将整数值 1 (int32) 推送到计算堆栈上；否则，将 0 (int32) 推送到计算堆栈上。","");
        addInstruction(INST_CGT,"比较两个值。如果第一个值大于第二个值，则将整数值 1 (int32) 推送到计算堆栈上；反之，将 0 (int32) 推送到计算堆栈上。","");
        addInstruction(INST_CGT_UN,"比较两个无符号的或不可排序的值。如果第一个值大于第二个值，则将整数值 1 (int32) 推送到计算堆栈上；反之，将 0 (int32) 推送到计算堆栈上。","");
        addInstruction(INST_CKFINITE,"如果值不是有限数，则引发 ArithmeticException。","");
        addInstruction(INST_CLT,"比较两个值。如果第一个值小于第二个值，则将整数值 1 (int32) 推送到计算堆栈上；反之，将 0 (int32) 推送到计算堆栈上。","");
        addInstruction(INST_CLT_UN,"比较无符号的或不可排序的值 value1 和 value2。如果 value1 小于 value2，则将整数值 1 (int32 ) 推送到计算堆栈上；反之，将 0 ( int32 ) 推送到计算堆栈上。","");
        addInstruction(INST_CONSTRAINED,"约束要对其进行虚方法调用的类型。","");
        addInstruction(INST_CONV_I,"将位于计算堆栈顶部的值转换为 native int。","");
        addInstruction(INST_CONV_I1,"将位于计算堆栈顶部的值转换为 int8，然后将其扩展（填充）为 int32。","");
        addInstruction(INST_CONV_I2,"将位于计算堆栈顶部的值转换为 int16，然后将其扩展（填充）为 int32。","");
        addInstruction(INST_CONV_I4,"将位于计算堆栈顶部的值转换为 int32。","");
        addInstruction(INST_CONV_I8,"将位于计算堆栈顶部的值转换为 int64。","");
        addInstruction(INST_CONV_OVF_I,"将位于计算堆栈顶部的有符号值转换为有符号 native int，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_I_UN,"将位于计算堆栈顶部的无符号值转换为有符号 native int，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_I1,"将位于计算堆栈顶部的有符号值转换为有符号 int8 并将其扩展为 int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_I1_UN,"将位于计算堆栈顶部的无符号值转换为有符号 int8 并将其扩展为 int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_I2,"将位于计算堆栈顶部的有符号值转换为有符号 int16 并将其扩展为 int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_I2_UN,"将位于计算堆栈顶部的无符号值转换为有符号 int16 并将其扩展为 int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_I4,"将位于计算堆栈顶部的有符号值转换为有符号 int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_I4_UN,"将位于计算堆栈顶部的无符号值转换为有符号 int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_I8,"将位于计算堆栈顶部的有符号值转换为有符号 int64，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_I8_UN,"将位于计算堆栈顶部的无符号值转换为有符号 int64，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_U,"将位于计算堆栈顶部的有符号值转换为 unsigned native int，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_U_UN,"将位于计算堆栈顶部的无符号值转换为 unsigned native int，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_U1,"将位于计算堆栈顶部的有符号值转换为 unsigned int8 并将其扩展为 int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_U1_UN,"将位于计算堆栈顶部的无符号值转换为 unsigned int8 并将其扩展为 int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_U2,"将位于计算堆栈顶部的有符号值转换为 unsigned int16 并将其扩展为 int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_U2_UN,"将位于计算堆栈顶部的无符号值转换为 unsigned int16 并将其扩展为 int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_U4,"将位于计算堆栈顶部的有符号值转换为 unsigned int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_U4_UN,"将位于计算堆栈顶部的无符号值转换为 unsigned int32，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_U8,"将位于计算堆栈顶部的有符号值转换为 unsigned int64，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_OVF_U8_UN,"将位于计算堆栈顶部的无符号值转换为 unsigned int64，并在溢出时引发 OverflowException。","");
        addInstruction(INST_CONV_R_UN,"将位于计算堆栈顶部的无符号整数值转换为 float32。","");
        addInstruction(INST_CONV_R4,"将位于计算堆栈顶部的值转换为 float32。","");
        addInstruction(INST_CONV_R8,"将位于计算堆栈顶部的值转换为 float64。","");
        addInstruction(INST_CONV_U,"将位于计算堆栈顶部的值转换为 unsigned native int，然后将其扩展为 native int。","");
        addInstruction(INST_CONV_U1,"将位于计算堆栈顶部的值转换为 unsigned int8，然后将其扩展为 int32。","");
        addInstruction(INST_CONV_U2,"将位于计算堆栈顶部的值转换为 unsigned int16，然后将其扩展为 int32。","");
        addInstruction(INST_CONV_U4,"将位于计算堆栈顶部的值转换为 unsigned int32，然后将其扩展为 int32。","");
        addInstruction(INST_CONV_U8,"将位于计算堆栈顶部的值转换为 unsigned int64，然后将其扩展为 int64。","");
        addInstruction(INST_CPBLK,"将指定数目的字节从源地址复制到目标地址。","");
        addInstruction(INST_CPOBJ,"将位于对象（&、* 或 native int 类型）地址的值类型复制到目标对象（&、* 或 native int 类型）的地址。","");
        addInstruction(INST_DIV,"将两个值相除并将结果作为浮点（F 类型）或商（int32 类型）推送到计算堆栈上。","");
        addInstruction(INST_DIV_UN,"两个无符号整数值相除并将结果 ( int32 ) 推送到计算堆栈上。","");
        addInstruction(INST_DUP,"复制计算堆栈上当前最顶端的值，然后将副本推送到计算堆栈上。","");
        addInstruction(INST_ENDFILTER,"将控制从异常的 filter 子句转移回公共语言结构 (CLI) 异常处理程序。","");
        addInstruction(INST_ENDFINALLY,"将控制从异常块的 fault 或 finally 子句转移回公共语言结构 (CLI) 异常处理程序。","");
        addInstruction(INST_INITBLK,"将位于特定地址的内存的指定块初始化为给定大小和初始值。","");
        addInstruction(INST_INITOBJ,"将位于指定地址的值类型的每个字段初始化为空引用或适当的基元类型的 0。","");
        addInstruction(INST_ISINST,"测试对象引用（O 类型）是否为特定类的实例。","");
        addInstruction(INST_JMP,"退出当前方法并跳至指定方法。","");
        addInstruction(INST_LDARG,"将参数（由指定索引值引用）加载到堆栈上。","");
        addInstruction(INST_LDARG_0,"将索引为 0 的参数加载到计算堆栈上。","");
        addInstruction(INST_LDARG_1,"将索引为 1 的参数加载到计算堆栈上。","");
        addInstruction(INST_LDARG_2,"将索引为 2 的参数加载到计算堆栈上。","");
        addInstruction(INST_LDARG_3,"将索引为 3 的参数加载到计算堆栈上。","");
        addInstruction(INST_LDARG_S,"将参数（由指定的短格式索引引用）加载到计算堆栈上。","");
        addInstruction(INST_LDARGA,"将参数地址加载到计算堆栈上。","");
        addInstruction(INST_LDARGA_S,"以短格式将参数地址加载到计算堆栈上。","");
        addInstruction(INST_LDC_I4,"将所提供的 int32 类型的值作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_0,"将整数值 0 作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_1,"将整数值 1 作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_2,"将整数值 2 作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_3,"将整数值 3 作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_4,"将整数值 4 作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_5,"将整数值 5 作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_6,"将整数值 6 作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_7,"将整数值 7 作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_8,"将整数值 8 作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_M1,"将整数值 -1 作为 int32 推送到计算堆栈上。","");
        addInstruction(INST_LDC_I4_S,"将提供的 int8 值作为 int32 推送到计算堆栈上（短格式）。","");
        addInstruction(INST_LDC_I8,"将所提供的 int64 类型的值作为 int64 推送到计算堆栈上。","");
        addInstruction(INST_LDC_R4,"将所提供的 float32 类型的值作为 F (float) 类型推送到计算堆栈上。","");
        addInstruction(INST_LDC_R8,"将所提供的 float64 类型的值作为 F (float) 类型推送到计算堆栈上。","");
        addInstruction(INST_LDELEM,"按照指令中指定的类型，将指定数组索引中的元素加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_I,"将位于指定数组索引处的 native int 类型的元素作为 native int 加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_I1,"将位于指定数组索引处的 int8 类型的元素作为 int32 加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_I2,"将位于指定数组索引处的 int16 类型的元素作为 int32 加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_I4,"将位于指定数组索引处的 int32 类型的元素作为 int32 加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_I8,"将位于指定数组索引处的 int64 类型的元素作为 int64 加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_R4,"将位于指定数组索引处的 float32 类型的元素作为 F 类型（浮点型）加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_R8,"将位于指定数组索引处的 float64 类型的元素作为 F 类型（浮点型）加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_REF,"将位于指定数组索引处的包含对象引用的元素作为 O 类型（对象引用）加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_U1,"将位于指定数组索引处的 unsigned int8 类型的元素作为 int32 加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_U2,"将位于指定数组索引处的 unsigned int16 类型的元素作为 int32 加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEM_U4,"将位于指定数组索引处的 unsigned int32 类型的元素作为 int32 加载到计算堆栈的顶部。","");
        addInstruction(INST_LDELEMA,"将位于指定数组索引的数组元素的地址作为 & 类型（托管指针）加载到计算堆栈的顶部。","");
        addInstruction(INST_LDFLD,"查找对象中其引用当前位于计算堆栈的字段的值。","");
        addInstruction(INST_LDFLDA,"查找对象中其引用当前位于计算堆栈的字段的地址。","");
        addInstruction(INST_LDFTN,"将指向实现特定方法的本机代码的非托管指针（native int 类型）推送到计算堆栈上。","");
        addInstruction(INST_LDIND_I,"将 native int 类型的值作为 native int 间接加载到计算堆栈上。","");
        addInstruction(INST_LDIND_I1,"将 int8 类型的值作为 int32 间接加载到计算堆栈上。","");
        addInstruction(INST_LDIND_I2,"将 int16 类型的值作为 int32 间接加载到计算堆栈上。","");
        addInstruction(INST_LDIND_I4,"将 int32 类型的值作为 int32 间接加载到计算堆栈上。","");
        addInstruction(INST_LDIND_I8,"将 int64 类型的值作为 int64 间接加载到计算堆栈上。","");
        addInstruction(INST_LDIND_R4,"将 float32 类型的值作为 F (float) 类型间接加载到计算堆栈上。","");
        addInstruction(INST_LDIND_R8,"将 float64 类型的值作为 F (float) 类型间接加载到计算堆栈上。","");
        addInstruction(INST_LDIND_REF,"将对象引用作为 O（对象引用）类型间接加载到计算堆栈上。","");
        addInstruction(INST_LDIND_U1,"将 unsigned int8 类型的值作为 int32 间接加载到计算堆栈上。","");
        addInstruction(INST_LDIND_U2,"将 unsigned int16 类型的值作为 int32 间接加载到计算堆栈上。","");
        addInstruction(INST_LDIND_U4,"将 unsigned int32 类型的值作为 int32 间接加载到计算堆栈上。","");
        addInstruction(INST_LDLEN,"将从零开始的、一维数组的元素的数目推送到计算堆栈上。","");
        addInstruction(INST_LDLOC,"将指定索引处的局部变量加载到计算堆栈上。","");
        addInstruction(INST_LDLOC_0,"将索引 0 处的局部变量加载到计算堆栈上。","");
        addInstruction(INST_LDLOC_1,"将索引 1 处的局部变量加载到计算堆栈上。","");
        addInstruction(INST_LDLOC_2,"将索引 2 处的局部变量加载到计算堆栈上。","");
        addInstruction(INST_LDLOC_3,"将索引 3 处的局部变量加载到计算堆栈上。","");
        addInstruction(INST_LDLOC_S,"将特定索引处的局部变量加载到计算堆栈上（短格式）。","");
        addInstruction(INST_LDLOCA,"将位于特定索引处的局部变量的地址加载到计算堆栈上。","");
        addInstruction(INST_LDLOCA_S,"将位于特定索引处的局部变量的地址加载到计算堆栈上（短格式）。","");
        addInstruction(INST_LDNULL,"将空引用（O 类型）推送到计算堆栈上。","");
        addInstruction(INST_LDOBJ,"将地址指向的值类型对象复制到计算堆栈的顶部。","");
        addInstruction(INST_LDSFLD,"将静态字段的值推送到计算堆栈上。","");
        addInstruction(INST_LDSFLDA,"将静态字段的地址推送到计算堆栈上。","");
        addInstruction(INST_LDSTR,"推送对元数据中存储的字符串的新对象引用。","");
        addInstruction(INST_LDTOKEN,"将元数据标记转换为其运行时表示形式，并将其推送到计算堆栈上。","");
        addInstruction(INST_LDVIRTFTN,"将指向实现与指定对象关联的特定虚方法的本机代码的非托管指针（native int 类型）推送到计算堆栈上。","");
        addInstruction(INST_LEAVE,"退出受保护的代码区域，无条件将控制转移到特定目标指令。","");
        addInstruction(INST_LEAVE_S,"退出受保护的代码区域，无条件将控制转移到目标指令（缩写形式）。","");
        addInstruction(INST_LOCALLOC,"从本地动态内存池分配特定数目的字节并将第一个分配的字节的地址（瞬态指针，* 类型）推送到计算堆栈上。","");
        addInstruction(INST_MKREFANY,"将对特定类型实例的类型化引用推送到计算堆栈上。","");
        addInstruction(INST_MUL,"将两个值相乘并将结果推送到计算堆栈上。","");
        addInstruction(INST_MUL_OVF,"将两个整数值相乘，执行溢出检查，并将结果推送到计算堆栈上。","");
        addInstruction(INST_MUL_OVF_UN,"将两个无符号整数值相乘，执行溢出检查，并将结果推送到计算堆栈上。","");
        addInstruction(INST_NEG,"对一个值执行求反并将结果推送到计算堆栈上。","");
        addInstruction(INST_NEWARR,"将对新的从零开始的一维数组（其元素属于特定类型）的对象引用推送到计算堆栈上。","");
        addInstruction(INST_NEWOBJ,"创建一个值类型的新对象或新实例，并将对象引用（O 类型）推送到计算堆栈上。","");
        addInstruction(INST_NOP,"如果修补操作码，则填充空间。尽管可能消耗处理周期，但未执行任何有意义的操作。","");
        addInstruction(INST_NOT,"计算堆栈顶部整数值的按位求补并将结果作为相同的类型推送到计算堆栈上。","");
        addInstruction(INST_OR,"计算位于堆栈顶部的两个整数值的按位求补并将结果推送到计算堆栈上。","");
        addInstruction(INST_POP,"移除当前位于计算堆栈顶部的值。","");
        addInstruction(INST_PREFIX1,"基础结构。此指令为保留指令。","");
        addInstruction(INST_PREFIX2,"基础结构。此指令为保留指令。","");
        addInstruction(INST_PREFIX3,"基础结构。此指令为保留指令。","");
        addInstruction(INST_PREFIX4,"基础结构。此指令为保留指令。","");
        addInstruction(INST_PREFIX5,"基础结构。此指令为保留指令。","");
        addInstruction(INST_PREFIX6,"基础结构。此指令为保留指令。","");
        addInstruction(INST_PREFIX7,"基础结构。此指令为保留指令。","");
        addInstruction(INST_PREFIXREF,"基础结构。此指令为保留指令。","");
        addInstruction(INST_READONLY,"指定后面的数组地址操作在运行时不执行类型检查，并且返回可变性受限的托管指针。","");
        addInstruction(INST_REFANYTYPE,"检索嵌入在类型化引用内的类型标记。","");
        addInstruction(INST_REFANYVAL,"检索嵌入在类型化引用内的地址（& 类型）。","");
        addInstruction(INST_REM,"将两个值相除并将余数推送到计算堆栈上。","");
        addInstruction(INST_REM_UN,"将两个无符号值相除并将余数推送到计算堆栈上。","");
        addInstruction(INST_RET,"从当前方法返回，并将返回值（如果存在）从调用方的计算堆栈推送到被调用方的计算堆栈上。","");
        addInstruction(INST_RETHROW,"再次引发当前异常。","");
        addInstruction(INST_SHL,"将整数值左移（用零填充）指定的位数，并将结果推送到计算堆栈上。","");
        addInstruction(INST_SHR,"将整数值右移（保留符号）指定的位数，并将结果推送到计算堆栈上。","");
        addInstruction(INST_SHR_UN,"将无符号整数值右移（用零填充）指定的位数，并将结果推送到计算堆栈上。","");
        addInstruction(INST_SIZEOF,"将提供的值类型的大小（以字节为单位）推送到计算堆栈上。","");
        addInstruction(INST_STARG,"将位于计算堆栈顶部的值存储到位于指定索引的参数槽中。","");
        addInstruction(INST_STARG_S,"将位于计算堆栈顶部的值存储在参数槽中的指定索引处（短格式）。","");
        addInstruction(INST_STELEM,"用计算堆栈中的值替换给定索引处的数组元素，其类型在指令中指定。","");
        addInstruction(INST_STELEM_I,"用计算堆栈上的 native int 值替换给定索引处的数组元素。","");
        addInstruction(INST_STELEM_I1,"用计算堆栈上的 int8 值替换给定索引处的数组元素。","");
        addInstruction(INST_STELEM_I2,"用计算堆栈上的 int16 值替换给定索引处的数组元素。","");
        addInstruction(INST_STELEM_I4,"用计算堆栈上的 int32 值替换给定索引处的数组元素。","");
        addInstruction(INST_STELEM_I8,"用计算堆栈上的 int64 值替换给定索引处的数组元素。","");
        addInstruction(INST_STELEM_R4,"用计算堆栈上的 float32 值替换给定索引处的数组元素。","");
        addInstruction(INST_STELEM_R8,"用计算堆栈上的 float64 值替换给定索引处的数组元素。","");
        addInstruction(INST_STELEM_REF,"用计算堆栈上的对象 ref 值（O 类型）替换给定索引处的数组元素。","");
        addInstruction(INST_STFLD,"用新值替换在对象引用或指针的字段中存储的值。","");
        addInstruction(INST_STIND_I,"在所提供的地址存储 native int 类型的值。","");
        addInstruction(INST_STIND_I1,"在所提供的地址存储 int8 类型的值。","");
        addInstruction(INST_STIND_I2,"在所提供的地址存储 int16 类型的值。","");
        addInstruction(INST_STIND_I4,"在所提供的地址存储 int32 类型的值。","");
        addInstruction(INST_STIND_I8,"在所提供的地址存储 int64 类型的值。","");
        addInstruction(INST_STIND_R4,"在所提供的地址存储 float32 类型的值。","");
        addInstruction(INST_STIND_R8,"在所提供的地址存储 float64 类型的值。","");
        addInstruction(INST_STIND_REF,"存储所提供地址处的对象引用值。","");
        addInstruction(INST_STLOC,"从计算堆栈的顶部弹出当前值并将其存储到指定索引处的局部变量列表中。","");
        addInstruction(INST_STLOC_0,"从计算堆栈的顶部弹出当前值并将其存储到索引 0 处的局部变量列表中。","");
        addInstruction(INST_STLOC_1,"从计算堆栈的顶部弹出当前值并将其存储到索引 1 处的局部变量列表中。","");
        addInstruction(INST_STLOC_2,"从计算堆栈的顶部弹出当前值并将其存储到索引 2 处的局部变量列表中。","");
        addInstruction(INST_STLOC_3,"从计算堆栈的顶部弹出当前值并将其存储到索引 3 处的局部变量列表中。","");
        addInstruction(INST_STLOC_S,"从计算堆栈的顶部弹出当前值并将其存储在局部变量列表中的 index 处（短格式）。","");
        addInstruction(INST_STOBJ,"将指定类型的值从计算堆栈复制到所提供的内存地址中。","");
        addInstruction(INST_STSFLD,"用来自计算堆栈的值替换静态字段的值。","");
        addInstruction(INST_SUB,"从其他值中减去一个值并将结果推送到计算堆栈上。","");
        addInstruction(INST_SUB_OVF,"从另一值中减去一个整数值，执行溢出检查，并且将结果推送到计算堆栈上。","");
        addInstruction(INST_SUB_OVF_UN,"从另一值中减去一个无符号整数值，执行溢出检查，并且将结果推送到计算堆栈上。","");
        addInstruction(INST_SWITCH,"实现跳转表。","");
        addInstruction(INST_TAILCALL,"执行后缀的方法调用指令，以便在执行实际调用指令前移除当前方法的堆栈帧。","");
        addInstruction(INST_THROW,"引发当前位于计算堆栈上的异常对象。","");
        addInstruction(INST_UNALIGNED,"指示当前位于计算堆栈上的地址可能没有与紧接的 ldind、stind、ldfld、stfld、ldobj、stobj、initblk 或 cpblk 指令的自然大小对齐。","");
        addInstruction(INST_UNBOX,"将值类型的已装箱的表示形式转换为其未装箱的形式。","");
        addInstruction(INST_UNBOX_ANY,"将指令中指定类型的已装箱的表示形式转换成未装箱形式。","");
        addInstruction(INST_VOLATILE,"指定当前位于计算堆栈顶部的地址可以是易失的，并且读取该位置的结果不能被缓存，或者对该地址的多个存储区不能被取消。","");
        addInstruction(INST_XOR,"计算位于计算堆栈顶部的两个值的按位异或，并且将结果推送到计算堆栈上。","");
        // 生成IL语句帮助信息
        addStatement(STAT_CLASS,"","");
        addStatement(STAT_INSTANCE,"","");
        convertToXml(ilPath,il);
    }

    private static void addInstruction(String name, String desc, String example){
        il.getInstructions().add(generateHelper(name,desc,example));
    }

    private static void addStatement(String name, String desc, String example){
        il.getStatements().add(generateHelper(name,desc,example));
    }

    private static Helper generateHelper(String name, String desc, String example){
        Helper helper = new Helper();
        helper.setName(name);
        helper.setDesc(desc);
        helper.setExample(example);
        return helper;
    }

    private static class HelperComparator implements Comparator<Helper>{
        @Override
        public int compare(Helper helper1, Helper helper2){
            return helper1.getName().compareTo(helper2.getName());
        }
    }
}
