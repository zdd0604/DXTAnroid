package com.dxtnerp.util.img;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by zhangdongdong on 2018/3/13.
 * 加载图片
 */

public class BitmapUtils {
    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() !=
                PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 获取资源图片
     *
     * @param imgID
     * @return
     */
    public static Drawable getResourcesDw(Context context, int imgID) {
        Resources resources = context.getResources();
        return resources.getDrawable(imgID);
    }


    /**
     * 将Bitmap转换成照片
     */

    public static void getBitmapImageFile(Bitmap bitmap, String imageName) {
//        server_user_hand = DadaUrlPath.SDCARD_PHOTO + "/" + imageName + ".jpg";
//        File bitmapFile = new File(server_user_hand);
//        FileOutputStream bitmapWtriter = null;
//        try {
//            if (bitmapFile.exists()) {
//                bitmapWtriter = new FileOutputStream(bitmapFile);
//            } else {
//                bitmapFile.mkdirs();
//                bitmapWtriter = new FileOutputStream(bitmapFile);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (bitmapFile.exists()) {
//            bitmap.compress(Bitmap.CompressFormat.PNG, 90, bitmapWtriter);
//        }
    }

}
