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
import legend.helper.il.IL;
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
        addInstruction(INST_ADD,"","");
        addInstruction(INST_ADD_OVF,"","");
        addInstruction(INST_ADD_OVF_UN,"","");
        addInstruction(INST_AND,"","");
        addInstruction(INST_ARGLIST,"","");
        addInstruction(INST_BEQ,"","");
        addInstruction(INST_BEQ_S,"","");
        addInstruction(INST_BGE,"","");
        addInstruction(INST_BGE_S,"","");
        addInstruction(INST_BGE_UN,"","");
        addInstruction(INST_BGE_UN_S,"","");
        addInstruction(INST_BGT,"","");
        addInstruction(INST_BGT_S,"","");
        addInstruction(INST_BGT_UN,"","");
        addInstruction(INST_BGT_UN_S,"","");
        addInstruction(INST_BLE,"","");
        addInstruction(INST_BLE_S,"","");
        addInstruction(INST_BLE_UN,"","");
        addInstruction(INST_BLE_UN_S,"","");
        addInstruction(INST_BLT,"","");
        addInstruction(INST_BLT_S,"","");
        addInstruction(INST_BLT_UN,"","");
        addInstruction(INST_BLT_UN_S,"","");
        addInstruction(INST_BNE_UN,"","");
        addInstruction(INST_BNE_UN_S,"","");
        addInstruction(INST_BOX,"","");
        addInstruction(INST_BR,"","");
        addInstruction(INST_BR_S,"","");
        addInstruction(INST_BREAK,"","");
        addInstruction(INST_BRFALSE,"","");
        addInstruction(INST_BRFALSE_S,"","");
        addInstruction(INST_BRTRUE,"","");
        addInstruction(INST_BRTRUE_S,"","");
        addInstruction(INST_CALL,"","");
        addInstruction(INST_CALLI,"","");
        addInstruction(INST_CALLVIRT,"","");
        addInstruction(INST_CASTCLASS,"","");
        addInstruction(INST_CEQ,"","");
        addInstruction(INST_CGT,"","");
        addInstruction(INST_CGT_UN,"","");
        addInstruction(INST_CKFINITE,"","");
        addInstruction(INST_CLT,"","");
        addInstruction(INST_CLT_UN,"","");
        addInstruction(INST_CONSTRAINED,"","");
        addInstruction(INST_CONV_I,"","");
        addInstruction(INST_CONV_I1,"","");
        addInstruction(INST_CONV_I2,"","");
        addInstruction(INST_CONV_I4,"","");
        addInstruction(INST_CONV_I8,"","");
        addInstruction(INST_CONV_OVF_I,"","");
        addInstruction(INST_CONV_OVF_I_UN,"","");
        addInstruction(INST_CONV_OVF_I1,"","");
        addInstruction(INST_CONV_OVF_I1_UN,"","");
        addInstruction(INST_CONV_OVF_I2,"","");
        addInstruction(INST_CONV_OVF_I2_UN,"","");
        addInstruction(INST_CONV_OVF_I4,"","");
        addInstruction(INST_CONV_OVF_I4_UN,"","");
        addInstruction(INST_CONV_OVF_I8,"","");
        addInstruction(INST_CONV_OVF_I8_UN,"","");
        addInstruction(INST_CONV_OVF_U,"","");
        addInstruction(INST_CONV_OVF_U_UN,"","");
        addInstruction(INST_CONV_OVF_U1,"","");
        addInstruction(INST_CONV_OVF_U1_UN,"","");
        addInstruction(INST_CONV_OVF_U2,"","");
        addInstruction(INST_CONV_OVF_U2_UN,"","");
        addInstruction(INST_CONV_OVF_U4,"","");
        addInstruction(INST_CONV_OVF_U4_UN,"","");
        addInstruction(INST_CONV_OVF_U8,"","");
        addInstruction(INST_CONV_OVF_U8_UN,"","");
        addInstruction(INST_CONV_R_UN,"","");
        addInstruction(INST_CONV_R4,"","");
        addInstruction(INST_CONV_R8,"","");
        addInstruction(INST_CONV_U,"","");
        addInstruction(INST_CONV_U1,"","");
        addInstruction(INST_CONV_U2,"","");
        addInstruction(INST_CONV_U4,"","");
        addInstruction(INST_CONV_U8,"","");
        addInstruction(INST_CPBLK,"","");
        addInstruction(INST_CPOBJ,"","");
        addInstruction(INST_DIV,"","");
        addInstruction(INST_DIV_UN,"","");
        addInstruction(INST_DUP,"","");
        addInstruction(INST_ENDFILTER,"","");
        addInstruction(INST_ENDFINALLY,"","");
        addInstruction(INST_INITBLK,"","");
        addInstruction(INST_INITOBJ,"","");
        addInstruction(INST_ISINST,"","");
        addInstruction(INST_JMP,"","");
        addInstruction(INST_LDARG,"","");
        addInstruction(INST_LDARG_0,"","");
        addInstruction(INST_LDARG_1,"","");
        addInstruction(INST_LDARG_2,"","");
        addInstruction(INST_LDARG_3,"","");
        addInstruction(INST_LDARG_S,"","");
        addInstruction(INST_LDARGA,"","");
        addInstruction(INST_LDARGA_S,"","");
        addInstruction(INST_LDC_I4,"","");
        addInstruction(INST_LDC_I4_0,"","");
        addInstruction(INST_LDC_I4_1,"","");
        addInstruction(INST_LDC_I4_2,"","");
        addInstruction(INST_LDC_I4_3,"","");
        addInstruction(INST_LDC_I4_4,"","");
        addInstruction(INST_LDC_I4_5,"","");
        addInstruction(INST_LDC_I4_6,"","");
        addInstruction(INST_LDC_I4_7,"","");
        addInstruction(INST_LDC_I4_8,"","");
        addInstruction(INST_LDC_I4_M1,"","");
        addInstruction(INST_LDC_I4_S,"","");
        addInstruction(INST_LDC_I8,"","");
        addInstruction(INST_LDC_R4,"","");
        addInstruction(INST_LDC_R8,"","");
        addInstruction(INST_LDELEM,"","");
        addInstruction(INST_LDELEM_I,"","");
        addInstruction(INST_LDELEM_I1,"","");
        addInstruction(INST_LDELEM_I2,"","");
        addInstruction(INST_LDELEM_I4,"","");
        addInstruction(INST_LDELEM_I8,"","");
        addInstruction(INST_LDELEM_R4,"","");
        addInstruction(INST_LDELEM_R8,"","");
        addInstruction(INST_LDELEM_REF,"","");
        addInstruction(INST_LDELEM_U1,"","");
        addInstruction(INST_LDELEM_U2,"","");
        addInstruction(INST_LDELEM_U4,"","");
        addInstruction(INST_LDELEMA,"","");
        addInstruction(INST_LDFLD,"","");
        addInstruction(INST_LDFLDA,"","");
        addInstruction(INST_LDFTN,"","");
        addInstruction(INST_LDIND_I,"","");
        addInstruction(INST_LDIND_I1,"","");
        addInstruction(INST_LDIND_I2,"","");
        addInstruction(INST_LDIND_I4,"","");
        addInstruction(INST_LDIND_I8,"","");
        addInstruction(INST_LDIND_R4,"","");
        addInstruction(INST_LDIND_R8,"","");
        addInstruction(INST_LDIND_REF,"","");
        addInstruction(INST_LDIND_U1,"","");
        addInstruction(INST_LDIND_U2,"","");
        addInstruction(INST_LDIND_U4,"","");
        addInstruction(INST_LDLEN,"","");
        addInstruction(INST_LDLOC,"","");
        addInstruction(INST_LDLOC_0,"","");
        addInstruction(INST_LDLOC_1,"","");
        addInstruction(INST_LDLOC_2,"","");
        addInstruction(INST_LDLOC_3,"","");
        addInstruction(INST_LDLOC_S,"","");
        addInstruction(INST_LDLOCA,"","");
        addInstruction(INST_LDLOCA_S,"","");
        addInstruction(INST_LDNULL,"","");
        addInstruction(INST_LDOBJ,"","");
        addInstruction(INST_LDSFLD,"","");
        addInstruction(INST_LDSFLDA,"","");
        addInstruction(INST_LDSTR,"","");
        addInstruction(INST_LDTOKEN,"","");
        addInstruction(INST_LDVIRTFTN,"","");
        addInstruction(INST_LEAVE,"","");
        addInstruction(INST_LEAVE_S,"","");
        addInstruction(INST_LOCALLOC,"","");
        addInstruction(INST_MKREFANY,"","");
        addInstruction(INST_MUL,"","");
        addInstruction(INST_MUL_OVF,"","");
        addInstruction(INST_MUL_OVF_UN,"","");
        addInstruction(INST_NEG,"","");
        addInstruction(INST_NEWARR,"","");
        addInstruction(INST_NEWOBJ,"","");
        addInstruction(INST_NOP,"","");
        addInstruction(INST_NOT,"","");
        addInstruction(INST_OR,"","");
        addInstruction(INST_POP,"","");
        addInstruction(INST_PREFIX1,"","");
        addInstruction(INST_PREFIX2,"","");
        addInstruction(INST_PREFIX3,"","");
        addInstruction(INST_PREFIX4,"","");
        addInstruction(INST_PREFIX5,"","");
        addInstruction(INST_PREFIX6,"","");
        addInstruction(INST_PREFIX7,"","");
        addInstruction(INST_PREFIXREF,"","");
        addInstruction(INST_READONLY,"","");
        addInstruction(INST_REFANYTYPE,"","");
        addInstruction(INST_REFANYVAL,"","");
        addInstruction(INST_REM,"","");
        addInstruction(INST_REM_UN,"","");
        addInstruction(INST_RET,"","");
        addInstruction(INST_RETTHROW,"","");
        addInstruction(INST_SHL,"","");
        addInstruction(INST_SHR,"","");
        addInstruction(INST_SHR_UN,"","");
        addInstruction(INST_SIZEOF,"","");
        addInstruction(INST_STARG,"","");
        addInstruction(INST_STARG_S,"","");
        addInstruction(INST_STELEM,"","");
        addInstruction(INST_STELEM_I,"","");
        addInstruction(INST_STELEM_I1,"","");
        addInstruction(INST_STELEM_I2,"","");
        addInstruction(INST_STELEM_I4,"","");
        addInstruction(INST_STELEM_I8,"","");
        addInstruction(INST_STELEM_R4,"","");
        addInstruction(INST_STELEM_R8,"","");
        addInstruction(INST_STELEM_REF,"","");
        addInstruction(INST_STFLD,"","");
        addInstruction(INST_STIND_I,"","");
        addInstruction(INST_STIND_I1,"","");
        addInstruction(INST_STIND_I2,"","");
        addInstruction(INST_STIND_I4,"","");
        addInstruction(INST_STIND_I8,"","");
        addInstruction(INST_STIND_R4,"","");
        addInstruction(INST_STIND_R8,"","");
        addInstruction(INST_STIND_REF,"","");
        addInstruction(INST_STLOC,"","");
        addInstruction(INST_STLOC_0,"","");
        addInstruction(INST_STLOC_1,"","");
        addInstruction(INST_STLOC_2,"","");
        addInstruction(INST_STLOC_3,"","");
        addInstruction(INST_STLOC_S,"","");
        addInstruction(INST_STOBJ,"","");
        addInstruction(INST_STSFLD,"","");
        addInstruction(INST_SUB,"","");
        addInstruction(INST_SUB_OVF,"","");
        addInstruction(INST_SUB_OVF_UN,"","");
        addInstruction(INST_SWITCH,"","");
        addInstruction(INST_TAILCALL,"","");
        addInstruction(INST_THROW,"","");
        addInstruction(INST_UNALIGNED,"","");
        addInstruction(INST_UNBOX,"","");
        addInstruction(INST_UNBOX_ANY,"","");
        addInstruction(INST_VOLATILE,"","");
        addInstruction(INST_XOR,"","");
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
