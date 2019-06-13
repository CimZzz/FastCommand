package com.virtualightning;


import com.virtualightning.core.CoreMain;

public final class Main {
    public static void main(String[] args) {
//        CoreMain.INSTANCE.start(args);
        CoreMain.INSTANCE.start(new String[] {
            "-startFrame"
        });
    }
}
