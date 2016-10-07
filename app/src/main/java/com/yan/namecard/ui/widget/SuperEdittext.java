package com.yan.namecard.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yan.namecard.R;

/**
 * Created by Administrator on 2016/10/6.
 */
public class SuperEdittext extends RelativeLayout {

    private EditText tvShow;
    private EditText etEdit;

    private ImageView rotation;
    private ImageView scale;
    private ImageView transform;
    private View centerLocationView;

    @IdRes
    static final int id_rotation = 1011111;
    @IdRes
    static final int id_scale = 1101111;
    @IdRes
    static final int id_transform = 1110111;
    @IdRes
    static final int id_tvShow = 1111011;

    private OutLine outLine;
    private int outLineColor = Color.argb(150, 255, 50, 50);
    private int outLineWidth = 8;


    private float space = 40;
    private float textLayoutSpace = space * 5;

    private float textSize = 18;
    private float minWidth = 100;
    private int textColor = Color.argb(200, 0, 0, 0);

    private int controlWidth = (int) (2 * space * 4 / 5);


    public void setRotation(int resRotation) {
        this.rotation.setImageDrawable(ContextCompat.getDrawable(getContext(), resRotation));
    }

    public void setScale(int resScoll) {
        this.scale.setImageDrawable(ContextCompat.getDrawable(getContext(), resScoll));
    }

    public void setTransform(int resTransform) {
        this.transform.setImageDrawable(ContextCompat.getDrawable(getContext(), resTransform));
    }

    private void init() {
        tvShow = new EditText(getContext());
        etEdit = new EditText(getContext());
        outLine = new OutLine(getContext());

        rotation = new ImageView(getContext());
        scale = new ImageView(getContext());
        transform = new ImageView(getContext());
        centerLocationView = new View(getContext());

        rotation.setLayoutParams(new ViewGroup.LayoutParams(controlWidth, controlWidth));
        rotation.setId(id_rotation);
        rotation.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        rotation.setOnTouchListener(onTouchListener);
        setRotation(R.drawable.bg);

        scale.setLayoutParams(new ViewGroup.LayoutParams(controlWidth, controlWidth));
        scale.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        scale.setId(id_scale);
        scale.setOnTouchListener(onTouchListener);
        setScale(R.drawable.bg);

        transform.setLayoutParams(new ViewGroup.LayoutParams(controlWidth, controlWidth));
        transform.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        transform.setId(id_transform);
        transform.setOnTouchListener(onTouchListener);
        setTransform(R.drawable.bg);

        etEdit.setMinWidth((int) minWidth);
        etEdit.setTextSize(textSize);
        etEdit.setHint("请输入");
        etEdit.setTextColor(textColor);

        tvShow.setTextSize(textSize);
        tvShow.setId(id_tvShow);
        tvShow.setGravity(Gravity.CENTER_VERTICAL);
        tvShow.setTextColor(textColor);
        tvShow.setBackground(null);
        tvShow.setFocusable(false);
        tvShow.setOnTouchListener(onTouchListener);

        this.addView(centerLocationView);
        this.addView(tvShow);
        this.addView(etEdit);
        this.addView(rotation);
        this.addView(scale);
        this.addView(transform);
        this.addView(outLine);

        this.setClickable(true);
    }

    private float mDegree;
    private PointF firstPointF;
    private PointF centerInScreen;
    private float currentDistance;
    private float tempDistanceFirst;
    private PointF currentPointF;
    private Matrix matrix;
    OnTouchListener onTouchListener = (v, event) -> {
        switch (v.getId()) {
            case id_tvShow:
                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    setVisibilitySingle(1);
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(etEdit, InputMethodManager.SHOW_FORCED);
                }
                break;

            case id_transform:
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        if (firstPointF == null) firstPointF = new PointF();
                        firstPointF.set(event.getRawX(), event.getRawY());

                        if (currentPointF == null) currentPointF = new PointF();
                        currentPointF.set(this.getX(), this.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float pointX = event.getRawX();
                        float pointY = event.getRawY();
                        currentPointF.offset(pointX - firstPointF.x, pointY - firstPointF.y);
                        this.setX(currentPointF.x);
                        this.setY(currentPointF.y);
                        firstPointF.set(pointX, pointY);
                        break;
                }
                break;

            case id_scale:
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        centerInit();
                        if (firstPointF == null) firstPointF = new PointF();
                        firstPointF.set(event.getRawX(), event.getRawY());

                        if (currentPointF == null) currentPointF = new PointF();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        currentPointF.set(event.getRawX(), event.getRawY());
                        tempDistanceFirst = distance4PointF(firstPointF, centerInScreen);
                        currentDistance = distance4PointF(currentPointF, centerInScreen);

                        etEdit.setTextSize(textSize * (currentDistance / tempDistanceFirst));
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        textSize *= currentDistance / tempDistanceFirst;

                        break;
                }
                break;

            case id_rotation:
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        centerInit();
                        if (firstPointF == null) firstPointF = new PointF();
                        firstPointF.set(event.getRawX(), event.getRawY());

                        if (currentPointF == null) currentPointF = new PointF();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        currentPointF.set(event.getRawX(), event.getRawY());
                        // 角度
                        double a = distance4PointF(centerInScreen, firstPointF);
                        double b = distance4PointF(firstPointF, currentPointF);
                        double c = distance4PointF(centerInScreen, currentPointF);

                        double cosb = (a * a + c * c - b * b) / (2 * a * c);

                        if (cosb >= 1) {
                            cosb = 1f;
                        }

                        double radian = Math.acos(cosb);
                        float newDegree = (float) radianToDegree(radian);

                        //center -> proMove的向量， 我们使用PointF来实现
                        PointF centerToProMove = new PointF((firstPointF.x - currentPointF.x), (firstPointF.y - currentPointF.y));

                        //center -> curMove 的向量
                        PointF centerToCurMove = new PointF((currentPointF.x - centerInScreen.x), (currentPointF.y - centerInScreen.y));

                        //向量叉乘结果, 如果结果为负数， 表示为逆时针， 结果为正数表示顺时针
                        float result = centerToProMove.x * centerToCurMove.y - centerToProMove.y * centerToCurMove.x;

                        if (result < 0) {
                            newDegree = -newDegree;
                        }

                        mDegree = mDegree + newDegree;

                        this.setRotation(mDegree % 360);

                        break;
                }
                break;
        }

        return true;
    };

    private void centerInit() {
        if (centerInScreen == null) {
            centerInScreen = new PointF();
        }
        int[] xy = new int[2];
        centerLocationView.getLocationInWindow(xy);
        centerInScreen.set(xy[0], xy[1]);
    }

    private float distance4PointF(PointF pf1, PointF pf2) {
        float disX = pf2.x - pf1.x;
        float disY = pf2.y - pf1.y;
        return (float) Math.sqrt(disX * disX + disY * disY);
    }

    public double radianToDegree(double radian) {
        return radian * 90 / Math.PI;
    }

    /**
     * 设置Matrix, 强制刷新
     */
    private void transformDraw() {
        matrix = this.getMatrix();
        //绕着图片中心进行旋转
        matrix.postRotate(mDegree % 360, centerInScreen.x / 2, centerInScreen.y / 2);
        //设置画该图片的起始点
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = etEdit.getMeasuredWidth();
        int height = etEdit.getMeasuredHeight();

        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec((int) (width + 2 * textLayoutSpace * 3 / 4), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) (height + textLayoutSpace), MeasureSpec.EXACTLY));

        tvShow.setWidth(width);
        tvShow.setHeight(height);
        tvShow.setY(this.getMeasuredHeight() / 2 - height / 2);
        tvShow.setX(this.getMeasuredWidth() / 2 - width / 2);

        etEdit.setY(this.getMeasuredHeight() / 2 - height / 2);
        etEdit.setX(this.getMeasuredWidth() / 2 - width / 2);

        centerLocationView.setY(this.getMeasuredHeight() / 2);
        centerLocationView.setX(this.getMeasuredWidth() / 2);

        float left = etEdit.getX() - space;
        float top = etEdit.getY();
        float right = etEdit.getX() + width + space;
        float bottom = etEdit.getY() + height;

        scale.setX(right / 2 + scale.getMeasuredWidth() / 2);
        scale.setY(top - space * 6 / 5 - scale.getMeasuredHeight() / 2);

        rotation.setX(right + space * 6 / 5 - rotation.getMeasuredWidth() / 2);
        rotation.setY(top - space * 6 / 5 - rotation.getMeasuredHeight() / 2);

        transform.setX(right + space * 6 / 5 - transform.getMeasuredWidth() / 2);
        transform.setY(bottom + space * 6 / 5 - transform.getMeasuredHeight() / 2);

        outLine.setRectF(left, top, right, bottom);
    }


    private void setVisibilitySingle(int mode) {
        if (mode == 1) {
            tvShow.setVisibility(GONE);
            etEdit.setVisibility(VISIBLE);
            outLine.setColor(outLineColor);
            scale.setVisibility(VISIBLE);
            rotation.setVisibility(VISIBLE);
            transform.setVisibility(VISIBLE);
        } else {
            tvShow.setText(etEdit.getText());
            tvShow.setVisibility(VISIBLE);
            etEdit.setVisibility(GONE);
            outLine.setColor(Color.argb(0, 255, 50, 50));
            scale.setVisibility(GONE);
            rotation.setVisibility(GONE);
            transform.setVisibility(GONE);

        }
    }


    public SuperEdittext(Context context) {
        super(context);
        init();
    }

    public SuperEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SuperEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public class OutLine extends View {
        private Paint paint;
        private RectF mainRectF;
        private int color = outLineColor;

        public void setRectF(float left, float top, float right, float bottom) {
            mainRectF.set(left, top, right, bottom);
            postInvalidate();
        }

        public void setColor(int color) {
            this.color = color;
        }

        private void init() {
            paint = new Paint();
            paint.setAntiAlias(true);
            mainRectF = new RectF(0, 0, 0, 0);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setStrokeWidth(outLineWidth);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(mainRectF, paint);
        }

        public OutLine(Context context) {
            super(context);
            init();
        }

        public OutLine(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public OutLine(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }
    }

}
