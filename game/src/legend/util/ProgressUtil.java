package legend.util;

import static legend.intf.ICommon.gsph;
import static legend.util.ConsoleUtil.CS;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import legend.util.intf.IProgress;
import legend.util.intf.IProgressUtil;

public class ProgressUtil implements IProgressUtil{
    public static IProgress ConsoleProgress(){
        return new ConsoleProgress();
    }

    private ProgressUtil(){}

    private static class ConsoleProgress implements IProgress{
        private AtomicInteger position;
        private AtomicInteger begin;
        private AtomicInteger end;
        private AtomicInteger progress;
        private AtomicReference<Float> amount;
        private AtomicReference<Float> size;
        private AtomicReference<State> state;
        private CyclicBarrier run;
        private CyclicBarrier finish;
        private CyclicBarrier reset;
        private CyclicBarrier stop;
        private CyclicBarrier resume;
        private CyclicBarrier update;
        private ExecutorService service;

        private ConsoleProgress(){
            position = new AtomicInteger(MIN);
            begin = new AtomicInteger(MIN);
            end = new AtomicInteger(MAX);
            progress = new AtomicInteger(MIN);
            amount = new AtomicReference<>(100f);
            size = new AtomicReference<>(0f);
            state = new AtomicReference<>(State.FINISH);
            finish = new CyclicBarrier(1,()->finish0());
            run = new CyclicBarrier(1,()->run0());
            reset = new CyclicBarrier(1,()->reset0());
            stop = new CyclicBarrier(1,()->stop0());
            resume = new CyclicBarrier(1,()->resume0());
            update = new CyclicBarrier(1,()->update0());
            service = Executors.newWorkStealingPool();
        }

        @Override
        public State state(){
            return state.get();
        }

        @Override
        public void runUntillFinish(Consumer<IProgress> consumer){
            run();
            consumer.accept(this);
            finish();
        }

        @Override
        public void run(){
            service.execute(()->await(run,ERR_RUN));
        }

        @Override
        public void finish(){
            service.execute(()->await(finish,ERR_RUN));
            sleep(SLEEP * 2,ERR_RUN);
        }

        @Override
        public void reset(float amount){
            reset(amount,MIN,MAX);
        }

        @Override
        public void reset(float amount, int position){
            reset(amount,position,MIN,MAX);
        }

        @Override
        public void reset(float amount, int begin, int end){
            reset(amount,MIN,begin,end);
        }

        @Override
        public void reset(float amount, int position, int begin, int end){
            service.execute(()->{
                this.amount.set(1 > amount ? 1 : amount);
                this.position.set(MIN > position ? MIN : MAX < position ? MAX : position);
                this.begin.set(MIN > begin ? MIN : MAX < begin ? MAX : begin);
                this.end.set(MIN > end ? MIN : MAX < end ? MAX : end);
                if(this.begin.get() >= this.end.get()) this.begin.set(MAX);
                await(reset,ERR_RESET);
            });
        }

        @Override
        public void stop(){
            service.execute(()->await(stop,ERR_STOP));
            sleep(SLEEP + 2,ERR_RUN);
        }

        @Override
        public void resume(){
            service.execute(()->await(resume,ERR_RESUME));
        }

        @Override
        public void update(float size){
            update(size,1f);
        }

        @Override
        public void update(float size, float scale){
            service.execute(()->{
                if(State.FINISH == state.get()) return;
                this.size.set(this.size.get() + scale * (MIN > size ? MIN : size));
                await(update,ERR_UPDATE);
                resume0();
            });
        }

        @Override
        public float countUpdate(float amount, float size){
            return size / amount;
        }

        @Override
        public float countUpdate(float amount, float size, float scale){
            return countUpdate(amount,size) * scale;
        }

        private void run0(){
            if(State.FINISH != state.get()) return;
            reset0();
            state.set(State.RUN);
            show();
        }

        private void finish0(){
            if(State.FINISH == state.get()) return;
            state.set(State.FINISH);
        }

        private void reset0(){
            size.set(position.get() * amount.get() / 100f);
            progress.set(position.get());
            if(State.RUN == state.get()){
                state.set(State.RESET);
                sleep(SLEEP * 2,ERR_RUN);
                state.set(State.RUN);
            }
        }

        private void stop0(){
            if(State.RUN != state.get()) return;
            state.set(State.STOP);
        }

        private void resume0(){
            if(State.STOP != state.get()) return;
            state.set(State.RUN);
        }

        private void update0(){
            int n = (int)(MAX * size.get() / amount.get());
            progress.set(MAX > n ? n : MAX - 1);
        }

        private void show(){
            while(State.FINISH != state.get()){
                if(State.RUN == state.get()){
                    int n = progress.get();
                    int b = begin.get() > n ? begin.get() : n;
                    int e = end.get();
                    if(b > n) for(int i = MIN;i < b && State.RUN == state.get();i++){
                        CS.s(COMPLETE,i < MID ? i : MID).s(REMAINER,MID - i).s(10 > i ? 2 : 1).s(i + STYLE).s(COMPLETE,i - MID).s(REMAINER,i > MID ? MAX - i : MID).s(RETURN);
                        sleep(SLEEP,ERR_RUN);
                    }
                    else if(e > n) for(int i = b;i < e && State.RUN == state.get();i++,n = progress.get()){
                        CS.s(COMPLETE,i < MID ? i : MID).s(REMAINER,MID - i).s(10 > n ? 2 : 1).s(n + STYLE).s(COMPLETE,i - MID).s(REMAINER,i > MID ? MAX - i : MID).s(RETURN);
                        sleep(SLEEP,ERR_RUN);
                    }
                    else for(int i = e;i < MAX && State.RUN == state.get();i++){
                        CS.s(COMPLETE,i < MID ? i : MID).s(REMAINER,MID - i).s(1).s(i + STYLE).s(COMPLETE,i - MID).s(REMAINER,i > MID ? MAX - i : MID).s(RETURN);
                        sleep(SLEEP,ERR_RUN);
                    }
                }else sleep(1,ERR_RUN);
            }
            CS.s(COMPLETE,MID).s(MAX + STYLE).s(COMPLETE,MID).l(2);
        }

        private void await(CyclicBarrier barrier, String error){
            try{
                barrier.await();
            }catch(InterruptedException | BrokenBarrierException e){
                CS.sl(gsph(error,e.toString()));
            }
        }

        private void sleep(long sleep, String error){
            try{
                Thread.sleep(sleep);
            }catch(InterruptedException e){
                CS.sl(gsph(error,e.toString()));
            }
        }
    }
}
