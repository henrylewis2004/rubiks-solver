package com.henry.rubiksolver

public class Algorithm {

    private lateinit var algorithm: Array<String>
    private var moveCnt: Int = 0
    private var alFinished: Boolean = false


    public fun setAlgorithm(newAl: Array<String>):Unit{
        algorithm = newAl
    }


    public fun getAbsoluteMove(move:Int): String{
        return algorithm[move]
    }

    public fun getMove(move:Int): String{
        if (algorithm[move].startsWith("rot_To_")){
            when(algorithm[move].last()){
                'b' -> return ("Rotate to BLUE face")
                'g' -> return ("Rotate to GREEN face")
                'w' -> return ("Rotate to WHITE face")
                'y' -> return ("Rotate to YELLOW face")
                'r' -> return ("Rotate to RED face")
                'o' -> return ("Rotate to ORANGE face")
            }
        }
        if (algorithm[move].length>1){
            return (algorithm[move][0].uppercase() + "'")
        }
        return (algorithm[move][0].uppercase())
    }


    public fun getNextMove(): String{
        if (moveCnt == algorithm.size - 1){
            alFinished = true
            return getMove(moveCnt)
        }

        moveCnt++

        return getMove(moveCnt)
    }

    public fun getPreviousMove(): String{
        if (moveCnt == 0){ moveCnt = 1}
        moveCnt -=1

        return getMove(moveCnt)
    }

    public fun algorithmFinished(): Boolean{
        return alFinished
    }

    public fun getMoveCount(): Int{
        return moveCnt
    }





}