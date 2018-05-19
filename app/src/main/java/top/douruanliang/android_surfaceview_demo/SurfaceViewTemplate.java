package top.douruanliang.android_surfaceview_demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * 作者：dourl on 2018/5/19 12:25
 * 邮箱：douruanliang@sina.com
 * SurfaceViewTemplate 的实例
 */

public class SurfaceViewTemplate extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    public SurfaceViewTemplate(Context context) {
        super(context);
        initView();
    }
    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public SurfaceViewTemplate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    //持有类
    private SurfaceHolder mHolder;
    //用于绘图的Canvas
    private Canvas mCanvas;
    //子线程的标识位
    private boolean mIsDrowing;

    private Path mPath;

    private Paint mPaint;

    //初始化操作
    private void initView(){
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        mPath = new Path();
        mPaint = new Paint();

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrowing = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        long  start = System.currentTimeMillis();
        while(mIsDrowing){
             //定义自己的绘制方法
            draw();
        }
        long  end = System.currentTimeMillis();

        if((end -start)<100 ){
            try {
                Thread.sleep(100-(end -start));
            }catch (Exception e){

            }

        }
    }
    private void draw(){
        try {
            mCanvas = mHolder.lockCanvas();
            //背景色
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath,mPaint);
        }catch (Exception e){

        }finally {
            if(mCanvas !=null){
                //放在这里用来保证每次都能将内容提交
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x  = (int)event.getX();
        int y  = (int)event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:

        }

        return true;
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
      mIsDrowing = false;
    }
}
