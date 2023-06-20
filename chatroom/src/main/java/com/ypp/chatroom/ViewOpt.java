package com.ypp.chatroom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.view_annotation.ViewMapperCreatorHost;


@ViewMapperCreatorHost
public class ViewOpt {

    private static volatile IViewCreator sIViewCreator;

    static {
        try {
            String ifsName = ViewOpt.class.getName();
            String proxyClassName = String.format("%s__ViewCreator__Proxy", ifsName);
            Class proxyClass = Class.forName(proxyClassName);
            Object proxyInstance = proxyClass.newInstance();
            if (proxyInstance instanceof IViewCreator) {
                sIViewCreator = (IViewCreator) proxyInstance;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static IViewCreator getViewCreator() {
        IViewCreator sIViewCreator = null;
        try {
            String ifsName = ViewOpt.class.getName();
            String proxyClassName = String.format("%s__ViewCreator__Proxy", ifsName);
            Class proxyClass = Class.forName(proxyClassName);
            Object proxyInstance = proxyClass.newInstance();
            if (proxyInstance instanceof IViewCreator) {
                sIViewCreator = (IViewCreator) proxyInstance;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return sIViewCreator;
    }


}
