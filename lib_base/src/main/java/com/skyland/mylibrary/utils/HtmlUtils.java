package com.skyland.mylibrary.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by skyland on 2017/8/11
 */

public class HtmlUtils {

    public static String getWebTitleByUrl(String url){
        InputStream response = null;
        try {
            response = new URL(url).openStream();
            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();
            String title = responseBody.substring(responseBody.indexOf("<title>") + 7, responseBody.indexOf("</title>"));
            System.out.println(responseBody.substring(responseBody.indexOf("<title>") + 7, responseBody.indexOf("</title>")));

            return title;

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }

}
