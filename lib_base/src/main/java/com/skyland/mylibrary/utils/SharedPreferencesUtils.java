package com.skyland.mylibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;


public abstract class SharedPreferencesUtils {
    public abstract String getSharedPreferencesName();
    public abstract String getSharedPreferencesKey();

    /**
     * 将数据加密后保存到SharedPreferences中。
     * @param context           Context
     * @param key				保存的关键字。
     * @param value				要保存的值。
     */
    public void setSharedPreferencesWithAESEncrypt(Context context, String key, String value) {
        value = HashUtils.AESEncrypt(value, getSharedPreferencesKey());
        setSharedPreferences(context, key, value);
    }

    /**
     * 从SharedPreferences中取出数据并解密。
     * @param context				上下文。
     * @param key					关键字(根据关键字取出数据)。
     * @param defValue				当没有数据时返回的默认值。
     * @return						返回要取出的数据。
     */
    public String getSharedPreferencesWithAESEncrypt(Context context, String key, String defValue){
        String value = (String) getSharedPreferences(context, key, defValue);
        if(defValue.equals(value)){
            return value;
        }else {
            value = HashUtils.AESDecrypt(value, getSharedPreferencesKey());
            return value;
        }
    }

    public void removeKey(Context context, String key){
        SharedPreferences share = context.getSharedPreferences(getSharedPreferencesName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.remove(key);
        editor.apply();	//提交更新
    }

    /**
     * 将数据保存到SharedPreferences中。
     * @param context				上下文。
     * @param key					保存的关键字。
     * @param value					要保存的值。
     */
    public void setSharedPreferences(Context context, String key, Object value){
        SharedPreferences share = context.getSharedPreferences(getSharedPreferencesName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        if(value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }else if(value instanceof String){
            editor.putString(key, (String) value);
        }else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.apply();	//提交更新
    }

    /**
     * 从SharedPreferences中取出数据。
     * @param context				上下文。
     * @param key					关键字(根据关键字取出数据)。
     * @param defValue				当没有数据时返回的默认值。
     * @return						返回要取出的数据。
     */
    public Object getSharedPreferences(Context context, String key, Object defValue){
        Object value = null;
        SharedPreferences share = context.getSharedPreferences(getSharedPreferencesName(), Context.MODE_PRIVATE);
        if(defValue instanceof Integer){
            value = share.getInt(key, (Integer)defValue);
        }else if(defValue instanceof String){
            value = share.getString(key, (String)defValue);
        }else if (defValue instanceof Boolean) {
            value = share.getBoolean(key, (Boolean)defValue);
        }
        return value;
    }
}
