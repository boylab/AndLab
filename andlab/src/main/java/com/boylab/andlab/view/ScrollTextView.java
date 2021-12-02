package com.boylab.andlab.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ScrollTextView extends SurfaceView implements SurfaceHolder.Callback {

    private RefreshThread myThread;
    private SurfaceHolder holder;

    private TextPaint mTextPaint;
    private int ShadowColor=Color.BLACK;
    private float textSize = 48;       //时间数字的字体大小
    private int textColor = Color.RED; //时间数字的颜色
    private String text;
    private int textWidth=0, textHeight=0;
    private float padTextSize=0;

    public ScrollTextView(Context context) {
        this(context, null, 0);
    }
    public ScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView(){
        holder = this.getHolder();
        holder.addCallback(this);
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        myThread = new RefreshThread(holder);//创建一个绘图线程
    }

    public void setText(String text){
        if(!TextUtils.isEmpty(text)){
            this.text = text;
            measurementsText();
        }
    }

    public void setShadowColor(int shadowColor) {
        ShadowColor = shadowColor;
        measurementsText();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        measurementsText();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        measurementsText();
    }

    protected void measurementsText() {
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        mTextPaint.setFakeBoldText(true);
        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
        mTextPaint.setShadowLayer(5, 3, 3, ShadowColor);
        textWidth = (int)mTextPaint.measureText(this.text);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        textHeight = (int) fontMetrics.bottom;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        myThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        myThread.shutdown();
    }

    private class RefreshThread extends AbsLoopThread{

        private SurfaceHolder holder;
        public int currentX=0;
        public int sepX=5;

        public RefreshThread(SurfaceHolder holder) {
            super();
            this.holder = holder;
        }

        @Override
        protected void beforeLoop() throws Exception {
            super.beforeLoop();
        }

        @Override
        protected void runInLoopThread() throws Exception {
            onDraw();
        }

        @Override
        protected void loopFinish(Exception var1) {

        }

        public void onDraw() {
            try {
                synchronized (holder) {
                    if(TextUtils.isEmpty(text)){
                        Thread.sleep(1000);//睡眠时间为1秒
                        return;
                    }

                    Canvas canvas = holder.lockCanvas();
                    // TODO: consider storing these as member variables to reduce

                    int paddingLeft = getPaddingLeft();
                    int paddingTop = getPaddingTop();
                    int paddingRight = getPaddingRight();
                    int paddingBottom = getPaddingBottom();

                    int contentWidth = getWidth() - paddingLeft - paddingRight;
                    int contentHeight = getHeight() - paddingTop - paddingBottom;

                    int centeYLine = paddingTop + contentHeight / 2;//中心线

                    if(currentX >=contentWidth){
                        currentX =-textWidth;
                    }else{
                        currentX += sepX;
                    }

                    canvas.drawColor(Color.BLACK);
                    canvas.drawText(text,currentX, centeYLine-textHeight/2, mTextPaint);
                    holder.unlockCanvasAndPost(canvas);//结束锁定画图，并提交改变。
                    Thread.sleep(1000);//睡眠时间为1秒
                }
            }
            catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public abstract class AbsLoopThread implements Runnable {
        public volatile Thread thread = null;
        private volatile boolean isStop = false;
        private volatile boolean isShutdown = true;
        private volatile Exception ioException = null;
        private volatile long loopTimes = 0L;

        public AbsLoopThread() {
            this.isStop = true;
        }

        public AbsLoopThread(String name) {
            this.isStop = true;
        }

        public synchronized void start() {
            if (this.isStop) {
                this.thread = new Thread(this);
                this.isStop = false;
                this.loopTimes = 0L;
                this.thread.start();
            }
        }

        public final void run() {
            try {
                this.isShutdown = false;
                this.beforeLoop();

                while(!this.isStop) {
                    this.runInLoopThread();
                    ++this.loopTimes;
                }
            } catch (Exception var5) {
                if (this.ioException == null) {
                    this.ioException = var5;
                }
            } finally {
                this.isShutdown = true;
                this.loopFinish(this.ioException);
                this.ioException = null;
            }
        }

        protected void beforeLoop() throws Exception {
        }

        protected abstract void runInLoopThread() throws Exception;

        protected abstract void loopFinish(Exception var1);

        public synchronized void shutdown() {
            if (this.thread != null && !this.isStop) {
                this.isStop = true;
                this.thread.interrupt();
                this.thread = null;
            }

        }

        public boolean isShutdown() {
            return this.isShutdown;
        }
    }

}
