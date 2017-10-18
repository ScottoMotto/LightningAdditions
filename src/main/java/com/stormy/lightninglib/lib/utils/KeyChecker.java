/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightninglib.lib.utils;


import org.lwjgl.input.Keyboard;

public class KeyChecker {

    public static boolean isHoldingShift(){
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isHoldingCtrl(){
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isHoldingAlt(){
        if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)){
            return true;
        }else{
            return false;
        }
    }

}
