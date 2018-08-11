package legend.game.dos2;

import static java.nio.file.Paths.get;
import static java.util.regex.Pattern.compile;
import static legend.util.ConsoleUtil.CS;
import static legend.util.FileUtil.makeDirs;
import static legend.util.JaxbUtil.convertToJavaBean;
import static legend.util.JaxbUtil.convertToXml;
import static legend.util.TimeUtil.runWithConsole;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import legend.game.dos2.entity.Content;
import legend.game.dos2.entity.ContentList;
import legend.intf.ICommonVar;
import legend.intf.IProgress;
import legend.util.ProgressUtil;
import legend.util.param.FileParam;

public final class Main implements ICommonVar{
    private static FileParam param;
    private static final IProgress progress;
    static{
        param = new FileParam();
        param.setOpt(OPT_INSIDE);
        progress = ProgressUtil.ConsoleProgress();
    }

    public static void main(String[] args){
        runWithConsole(t->runCmd(args),args,HELP_EOC);
    }

    public static void runCmd(String[] args){
        dealParam(args);
        switch(args[0]){
            case DOS2_DEBUG:
            progress.runUntillFinish(Main::toDebugVersion);
            break;
            case DOS2_RELEASE:
            progress.runUntillFinish(Main::toReleaseVersion);
            break;
            case DOS2_UPDATE:
            progress.runUntillFinish(Main::toUpdateVersion);
        }
    }

    public static void toDebugVersion(IProgress progress){
        ContentList contentList = convertToJavaBean(param.getSrcPath(),ContentList.class);
        Optional.of(contentList.getContents()).ifPresent(contents->{
            progress.reset(contents.size(),0,90);
            AtomicInteger i = new AtomicInteger(1);
            contents.parallelStream().forEach((content)->{
                String value = FLAG_DEBUG + String.valueOf(i.getAndIncrement()) + FLAG_DEBUG + content.getValue();
                content.setValue(value);
                progress.update(1);
            });
        });
        convertToXml(makeDirs(param.getDestPath()),contentList);
    }

    public static void toReleaseVersion(IProgress progress){
        ContentList contentList = convertToJavaBean(param.getSrcPath(),ContentList.class);
        Optional.of(contentList.getContents()).ifPresent(contents->{
            progress.reset(contents.size(),0,90);
            Pattern pattern = Pattern.compile(REG_RELEASE);
            contents.parallelStream().forEach((content)->{
                String value = content.getValue();
                content.setValue(pattern.matcher(value).replaceFirst(""));
                progress.update(1);
            });
        });
        convertToXml(makeDirs(param.getDestPath()),contentList);
    }

    public static void toUpdateVersion(IProgress progress){
        ContentList updateList = convertToJavaBean(param.getSrcPath(),ContentList.class);
        ContentList resultList = convertToJavaBean(param.getDestPath(),ContentList.class);
        Optional.of(resultList.getContents()).ifPresent(resultContents->{
            progress.reset(updateList.getContentMap().size(),0,90);
            AtomicInteger i = new AtomicInteger(resultContents.size());
            Pattern pattern = compile(REG_DEBUG);
            Pattern modPattern = compile(REG_MOD);
            Pattern addPattern = compile(REG_ADD);
            ConcurrentMap<String,Content> resultMap = resultList.getContentMap();
            updateList.getContentMap().entrySet().parallelStream().forEach((entry)->{
                String key = entry.getKey();
                Content updateContent = entry.getValue();
                String modValue = updateContent.getValue();
                if(resultMap.containsKey(key)){
                    Content resultContent = resultMap.get(key);
                    String resultValue = resultContent.getValue();
                    if(!modPattern.matcher(resultValue).find()){
                        Matcher matcher = pattern.matcher(resultValue);
                        Matcher addMatcher = addPattern.matcher(resultValue);
                        if(matcher.find()) resultContent.setValue(matcher.group() + modValue);
                        else if(addMatcher.find()) resultContent.setValue(addMatcher.replaceFirst(REP_ADD) + modValue);
                        else resultContent.setValue(modValue);
                    }
                }else{
                    String value = FLAG_DEBUG + FLAG_ADD + String.valueOf(i.incrementAndGet()) + FLAG_DEBUG + modValue;
                    updateContent.setValue(value);
                    resultContents.add(updateContent);
                }
                progress.update(1);
            });
        });
        convertToXml(param.getDestPath(),resultList);
    }

    private static void dealParam(String[] args){
        CS.showError(ERR_ARG_ANLS,new String[]{ST_ARG_ERR},()->args.length != 3);
        try{
            switch(args[0]){
                case DOS2_DEBUG:
                case DOS2_RELEASE:
                case DOS2_UPDATE:
                param.setSrcPath(get(args[1]));
                param.setDestPath(get(args[2]));
                break;
                default:
                CS.showError(ERR_ARG_ANLS,new String[]{ST_ARG_ERR});
            }
        }catch(Exception e){
            CS.showError(ERR_CMD_EXEC,new String[]{e.toString()});
        }
    }
}
