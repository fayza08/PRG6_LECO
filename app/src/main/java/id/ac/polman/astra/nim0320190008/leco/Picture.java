package id.ac.polman.astra.nim0320190008.leco;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class Picture {
    public static Bitmap getScaleBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        return getScaleBitmap(path, size.x, size.y);
    }
    public static Bitmap getScaleBitmap(String path, int destWidth, int destHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if(srcHeight > destHeight || srcWidth > destHeight){
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }

    public static String convertToString(Bitmap bitmap){
        //encode image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

    public static Bitmap Compress(Bitmap bitmap, int maxsize){
        //encode image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        double mid = imageBytes.length / 1024;
        double i = mid / maxsize;
        if (i > 1) {
            bitmap = scale(bitmap, bitmap.getWidth() / Math.sqrt(i),
                    bitmap.getHeight() / Math.sqrt(i));
        }
        return bitmap;
    }

    public static Bitmap scale(Bitmap src, double newWidth, double newHeight) {
        // src
        float width = src.getWidth();
        float height = src.getHeight();
        // matrix
        Matrix matrix = new Matrix();
        //
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        //
        matrix.postScale(scaleWidth, scaleHeight);
        //
        return Bitmap.createBitmap(src, 0, 0, (int) width, (int) height,
                matrix, true);
    }

    public static Bitmap convertToImage(String bitmap){
        //decode base64 string to image
        byte[] imageBytes = Base64.decode(bitmap, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }
}
