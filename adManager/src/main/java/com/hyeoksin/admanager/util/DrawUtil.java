package com.hyeoksin.admanager.util;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import androidx.annotation.ColorRes;
import androidx.annotation.Px;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * <p>
 * DrawUtil.java<br/>
 * 그리기와 관련된 유틸 함수들이 모여있는 클래스<br/>
 * </p><br/>
 * Created by OneKey on 2017-01-13.<br/>
 * Updated by OneKey at 2018.<br/>
 */
@SuppressWarnings("unused")
public class DrawUtil {
    public static final String TAG = DrawUtil.class.getSimpleName();

    /**
     * resource에 기술한 color를 버전에 관련없이 가져오는 함수
     *
     * @param view  뷰
     * @param color 색상
     */
    public static int getColor(View view,
                               @ColorRes int color) {
        return getColor(view.getContext(), color);
    }

    /**
     * resource에 기술한 color를 버전에 관련없이 가져오는 함수
     *
     * @param context 문맥
     * @param color   색상
     */
    public static int getColor(Context context,
                               @ColorRes int color) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ?
                context.getResources().getColor(color) :
                context.getResources().getColor(color, null);
    }

    /**
     * 원하는 색상으로 텍스트 뷰의 텍스트 색상을 버전에 관련없이 설정하는 함수
     *
     * @param textView 텍스트뷰
     * @param color    색상
     */
    public static void setTextColor(TextView textView,
                                    @ColorRes int color) {
        textView.setTextColor(getColor(textView, color));
    }

    /**
     * 원하는 색상으로 텍스트 뷰에 밑줄을 1dp의 너비로 그리는 함수
     *
     * @param textView  텍스트뷰
     * @param fillColor 색상
     */
    public static void drawTextUnderLine(TextView textView,
                                         @ColorRes int fillColor) {
        GradientDrawable shape = new GradientDrawable();
        GradientDrawable[] shapes = {shape};
        LayerDrawable layerDrawable = new LayerDrawable(shapes);

        shape.setColor(getColor(textView, fillColor));
        shape.setShape(GradientDrawable.RECTANGLE);

        textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        layerDrawable.setLayerInset(0, 0,
                textView.getMeasuredHeight() - SizeUtil.convertDpToPx(textView.getContext(), 1),
                0, 0
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(layerDrawable);
        } else {
            textView.setBackgroundDrawable(layerDrawable);
        }
    }

    /**
     * 뷰의 배경을 원하는 색상으로 채우는 함수
     *
     * @param view      뷰
     * @param fillColor 배경 색상
     */
    public static void drawRectangleBackground(View view,
                                               @ColorRes int fillColor) {
        view.setBackgroundColor(getColor(view, fillColor));
    }

    /**
     * 뷰의 배경을 원하는 색상으로 채우고
     * 테두리를 원하는 너비만큼 원하는 색상으로 채우는 함수
     *
     * @param view        뷰
     * @param fillColor   배경 색상
     * @param borderSize  테두리 너비(px)
     * @param borderColor 테두리 색상
     */
    public static void drawRectangleBackground(View view,
                                               @ColorRes int fillColor,
                                               @Px int borderSize,
                                               @ColorRes int borderColor) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        drawBackground(view, fillColor, new float[]{
                0, 0, 0, 0, 0, 0, 0, 0}, borderSize, borderColor);
    }

    /**
     * 뷰의 배경을 원하는 색상으로 채우고
     * 뷰 높이의 절반만큼 꼭지점을 둥글게 깎는 함수
     *
     * @param view      뷰
     * @param fillColor 배경 색상
     */
    public static void drawRoundBackground(View view,
                                           @ColorRes int fillColor) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int radius = view.getMeasuredHeight() / 2;

        drawBackground(view, fillColor,
                new float[]{radius, radius, radius, radius, radius, radius, radius, radius},
                0, fillColor);
    }

    /**
     * 뷰의 배경을 원하는 색상으로 채우고
     * 원하는 값만큼 각 꼭지점을 둥글게 깎는 함수
     *
     * @param view      뷰
     * @param fillColor 배경 색상
     * @param radius    둥들게 깎을 반지름
     */
    public static void drawRoundBackground(View view,
                                           @ColorRes int fillColor,
                                           float[] radius) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        drawBackground(view, fillColor, radius, 0, fillColor);
    }

    /**
     * 뷰의 배경을 원하는 색상으로 채우고
     * 테두리를 원하는 너비만큼 원하는 색상으로 채우면서
     * 뷰 높이의 절반만큼 꼭지점을 둥글게 깎는 함수
     *
     * @param view        뷰
     * @param fillColor   배경 색상
     * @param borderSize  테두리 너비(px)
     * @param borderColor 테두리 색상
     */
    public static void drawRoundBackground(View view,
                                           @ColorRes int fillColor,
                                           @Px int borderSize,
                                           @ColorRes int borderColor) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int radius = view.getMeasuredHeight() / 2;

        drawBackground(view, fillColor,
                new float[]{radius, radius, radius, radius, radius, radius, radius, radius},
                borderSize, borderColor);
    }

    /**
     * 뷰의 배경을 원하는 색상으로 채우고
     * 테두리를 원하는 너비만큼 원하는 색상으로 채우면서
     * 원하는 값만큼 각 꼭지점을 둥글게 깎는 함수
     *
     * @param view        뷰
     * @param fillColor   배경 색상
     * @param radius      둥들게 깎을 반지름
     * @param borderSize  테두리 너비(px)
     * @param borderColor 테두리 색상
     */
    public static void drawRoundBackground(View view,
                                           @ColorRes int fillColor,
                                           float[] radius,
                                           @Px int borderSize,
                                           @ColorRes int borderColor) {
        drawBackground(view, fillColor, radius, borderSize, borderColor);
    }

    /**
     * 뷰의 배경을 원하는 색상으로 채우고
     * 테두리를 원하는 너비만큼 원하는 색상으로 채우면서
     * 원하는 값만큼 각 꼭지점을 둥글게 깎는 함수
     *
     * @param view        뷰
     * @param fillColor   배경 색상
     * @param radius      둥들게 깎을 반지름
     * @param borderSize  테두리 너비(px)
     * @param borderColor 테두리 색상
     */
    private static void drawBackground(View view,
                                       @ColorRes int fillColor,
                                       float[] radius,
                                       @Px int borderSize,
                                       @ColorRes int borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(getColor(view, fillColor));
        shape.setStroke(borderSize, getColor(view, borderColor));
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(radius);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(shape);
        } else {
            view.setBackgroundDrawable(shape);
        }
    }

    /**
     * 뷰 위에 얹어질 그림자를 원하는 색상으로 채우는 함수
     *
     * @param view        뷰
     * @param startColor  시작 색상
     * @param endColor    끝 색상
     * @param orientation 채워질 색상의 방향
     */
    public static void drawRectangleShadow(View view,
                                           @ColorRes int startColor,
                                           @ColorRes int endColor,
                                           GradientDrawable.Orientation orientation) {
        GradientDrawable shape = new GradientDrawable(orientation,
                new int[]{getColor(view, startColor), getColor(view, endColor)});
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(shape);
        } else {
            view.setBackgroundDrawable(shape);
        }
    }

    /**
     * 뷰 위에 얹어질 그림자를 원하는 색상으로 채우는 함수
     *
     * @param view        뷰
     * @param startColor  시작 색상
     * @param centerColor 중간 색상
     * @param endColor    끝 색상
     * @param orientation 채워질 색상의 방향
     */
    public static void drawRectangleShadow(View view,
                                           @ColorRes int startColor,
                                           @ColorRes int centerColor,
                                           @ColorRes int endColor,
                                           GradientDrawable.Orientation orientation) {
        GradientDrawable shape = new GradientDrawable(orientation,
                new int[]{getColor(view, startColor), getColor(view, centerColor), getColor(view, endColor)});
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(shape);
        } else {
            view.setBackgroundDrawable(shape);
        }
    }

    /**
     * 뷰 위에 얹어질 그림자를 원하는 색상으로 채우고
     * 테두리를 원하는 너비만큼 원하는 색상으로 채우면서
     * 원하는 값만큼 각 꼭지점을 둥글게 깎는 함수
     *
     * @param view        뷰
     * @param startColor  시작 색상
     * @param endColor    끝 색상
     * @param orientation 채워질 색상의 방향
     * @param radius      둥들게 깎을 반지름
     * @param borderSize  테두리 너비(px)
     * @param borderColor 테두리 색상
     */
    public static void drawRoundShadow(View view,
                                       @ColorRes int startColor,
                                       @ColorRes int endColor,
                                       GradientDrawable.Orientation orientation,
                                       float[] radius,
                                       @Px int borderSize,
                                       @ColorRes int borderColor) {
        GradientDrawable shape = new GradientDrawable(orientation,
                new int[]{getColor(view, startColor), getColor(view, endColor)});
        shape.setStroke(borderSize, getColor(view, borderColor));
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        shape.setCornerRadii(radius);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(shape);
        } else {
            view.setBackgroundDrawable(shape);
        }
    }

    /**
     * 롤리팝 이상의 버전에서 상태바를 원하는 색상으로 채우는 함수
     *
     * @param window 뷰
     * @param color  상태바 색상
     */
    public static void setStatusBarColor(Window window,
                                         int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(window.getContext(), color));
        }
    }
}
