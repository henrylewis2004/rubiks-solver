package com.henry.rubiksolver

public class Cube2x2: Cube(){

    override public fun newCubeFace(): Array<CharArray?>{
        return arrayOf(CharArray(4){'w'}, CharArray(4){'b'}, CharArray(4){'r'}, CharArray(4){'g'}, CharArray(4){'o'}, CharArray(4){'y'})
    }




}