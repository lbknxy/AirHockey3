package com.arcsoft.firstopenglproject.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/9/27.
 */
public class TextResourceReader {

    public static String readTextFileFromResource(Context context, int resourceId) {

        StringBuilder body = new StringBuilder();

        try {

            InputStream inputStream = context.getResources().openRawResource(resourceId);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                body.append(line);
                body.append("\n");
            }

        } catch (IOException e) {

            throw new RuntimeException("cant open resource : " + resourceId);
        } catch (Resources.NotFoundException ee) {

            throw new RuntimeException("cant find resource : " + resourceId);
        }

        return body.toString();
    }
}
