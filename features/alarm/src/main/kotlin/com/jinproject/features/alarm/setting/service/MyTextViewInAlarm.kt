package com.jinproject.features.alarm.setting.service

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet

class MyTextViewInAlarm : androidx.appcompat.widget.AppCompatTextView {
    constructor(ctx: Context):super(ctx) //코드상에서 객체생성시 사용
    constructor(ctx: Context, attr: AttributeSet): super(ctx, attr)
    // xml로부터 inflater를통해 만들어질때
    constructor(ctx: Context, attr: AttributeSet, def: Int) : super(ctx, attr, def)
    // inflater를 통해 만들어지고 theme attribute로 기본스타일을 지정할때

    override fun onDraw(canvas: Canvas) {
        this.paint.style = Paint.Style.STROKE //테두리지정
        this.paint.strokeWidth = 6.0F //테두리두께
        this.setTextColor(Color.BLACK) //색깔지정
        super.onDraw(canvas) //그리기

        this.paint.style = Paint.Style.FILL //글자색지정
        setTextColor(Color.WHITE) //글자색지정
        super.onDraw(canvas) //그리기
    }
}