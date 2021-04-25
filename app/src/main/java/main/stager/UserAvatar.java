package main.stager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import lombok.Setter;
import main.stager.utils.ThemeController;
import main.stager.utils.Utilits;


public class UserAvatar extends View {
    @Setter
    private String mUserName;
    private Rect mTextBoundRect = new Rect();

    private String[] backgroundColor = new String[] {
            "#505160",
            "#68829E",
            "#90AFC5",
            "#80BD9E",
            "#66A5AD",
            "#2E4600",
            "#3F681C",
            "#EC96A4",
            "#F0810F",
            "#E2DFA2"
    };


    public UserAvatar(Context context) {
        super(context);
    }

    public UserAvatar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UserAvatar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UserAvatar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String uid = FirebaseAuth.getInstance().getUid();

        String name = Utilits.getDefaultOnNullOrBlank(
                mUserName, Utilits.getDefaultOnNullOrBlank(
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(), "A"
                )
        );

        if (!name.equals("A"))
            name = name.substring(0,2).toUpperCase();

        float textWidth, textHeight;

        float width, height, centerX, centerY, radius;
        height = getHeight();
        radius = height / 2;
        centerX = height / 2;
        centerY = centerX;

        Paint paint = new Paint();

        // Стиль заливка
        paint.setStyle(Paint.Style.FILL);
        // Закрашиваем холст
        paint.setColor(Color.parseColor(
                backgroundColor[Integer.parseInt(uid.substring(uid.length() - 1))]
        ));
        paint.setAlpha(Color.TRANSPARENT);
        canvas.drawPaint(paint);

        // Рисуем круг
        paint.setAntiAlias(true);

        int color = Color.TRANSPARENT;
        Drawable background = getRootView().getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();

        paint.setColor(colorChangeBrightness(
                color,
                32 * (ThemeController.getTheme(getContext()) ? 1 : -1)
                )
        );
//
//        paint.setColor(getContext().getResources()
//                .getIdentifier("colorButtonNormal", "attr", getContext().getPackageName()));

        canvas.drawCircle(centerX, centerY, radius, paint);

        // Рисуем текст
        if (ThemeController.getTheme(getContext()))
            paint.setColor(Color.WHITE);
        else paint.setColor(Color.BLACK);

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextSize(80);

        // Подсчитаем размер текста
        paint.getTextBounds(name, 0, name.length(), mTextBoundRect);

        // Используем measureText для измерения ширины
        textWidth = paint.measureText(name);
        textHeight = mTextBoundRect.height();

        canvas.drawText(name,
                centerX - (textWidth / 2f),
                centerY + (textHeight /2f),
                paint
        );

    }

    private int colorChangeBrightness(int color, int brightnessOffset) {
        return Color.rgb(((color >> 16)&0xFF) + brightnessOffset,
                ((color >> 8)&0xFF) + brightnessOffset,
                (color&0xFF) + brightnessOffset);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }
}