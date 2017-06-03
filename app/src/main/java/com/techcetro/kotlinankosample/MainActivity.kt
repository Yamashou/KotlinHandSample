package com.techcetro.kotlinankosample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.graphics.*
import android.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val handwriting = CustomView(this)
        setContentView(handwriting)

    }

}

internal class CustomView(context: Context) : View(context) {

    private var pos_x = 0f //イベントが起きたX座標
    private var pos_y = 0f //イベントが起きたY座標

    private var path: Path? = null //パス新規作成
    private var bitmap: Bitmap? = null //Viewの状態を保存するためのBitmap

    init {
        isFocusable = true
    }

    //イベントのタイプごとに処理を設定
    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN //最初のポイント
            -> {
                path = Path()//pathを新規作成
                pos_x = e.x//x座標取得
                pos_y = e.y//y座標取得
                //押されたPointに移動
                path!!.moveTo(e.x, e.y)
            }
            MotionEvent.ACTION_MOVE //途中のポイント
            -> {
                pos_x = e.x
                pos_y = e.y
                path!!.lineTo(pos_x, pos_y)
                invalidate()
            }
            MotionEvent.ACTION_UP //最後のポイント
            -> {
                path!!.lineTo(e.x, e.y)
                //キャッシュの中からキャプチャを作成するので、その一瞬の為にキャッシュをON
                isDrawingCacheEnabled = true
                bitmap = Bitmap.createBitmap(drawingCache)
                isDrawingCacheEnabled = false
                invalidate()
            }
            else//その他
            -> {
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bitmap != null) {
            canvas.drawBitmap(bitmap!!, 0f, 0f, null)
        }
        //この間にグラフィック描画のコードを記述する
        val paint = Paint()
        //色を設定
        paint.color = Color.BLACK
        //線の太さを指定
        paint.strokeWidth = 10f
        //線を滑らかに書く
        paint.isAntiAlias = true
        //線で描く
        paint.style = Paint.Style.STROKE
        //端点を丸く
        paint.strokeCap = Paint.Cap.ROUND
        //つなぎ目を丸く
        paint.strokeJoin = Paint.Join.ROUND
        //pathを描写
        if (path != null) {
            canvas.drawPath(path!!, paint)
            //終了     }

        }
    }
}