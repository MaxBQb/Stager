package main.stager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserAvatar extends View {
    private String text;
    private Rect mTextBoundRect = new Rect();

    private String[] backgroundColor = new String[] {
            "#505160",
            "#68829E",
            "#90AFC5",
            "#80BD9E",
            "#66A5AD",
            "#2E4600",
            "#486B00",
            "#2C7873",
            "#FB6542",
            "#FFBB00"
//            "#3F681C",
//            "#EC96A4",
//            "#F0810F",
//            "#E2DFA2"
//            "",
//            "",
//            "",
//            "",
//            "",
//            ""
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

        text = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Pattern pattern = Pattern.compile("^\\w*@{0}");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find())
            if (text.length() > 20)
                text = text.substring(matcher.start(), matcher.end());

        Paint paint = new Paint();

        float textWidth, textHeight;

        // стиль заливка
        paint.setStyle(Paint.Style.FILL);
        // закрашиваем холст
        paint.setColor(Color.parseColor(
                backgroundColor[Integer.parseInt(uid.substring(uid.length() - 1))]
        ));
        canvas.drawPaint(paint);

        // Рисуем текст
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextSize(80);


        // Подсчитаем размер текста
        paint.getTextBounds(text, 0, text.length(), mTextBoundRect);

        // Используем measureText для измерения ширины
        textWidth = paint.measureText(text);
        textHeight = mTextBoundRect.height();

        canvas.drawText(text, getWidth() / 2 - (textWidth / 2f),getHeight() / 2 + (textHeight /2f), paint);
    }

    // Можно выбрать 20 цветов. Берем хэш от user_id и используем как R G B
    // R = хэш(id), G = хэш(R), B = хэш(G)

    // setColor() {}
    // setChar() {}
}