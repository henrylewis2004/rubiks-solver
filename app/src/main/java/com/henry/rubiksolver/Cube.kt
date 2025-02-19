package com.henry.rubiksolver

public class Cube {

   private var cubeFace: Array<CharArray?> = newCubeFace() //array containing cube data in the order (w,b,r,g,o,y)

    private val utilFun = Util()

    public fun newCubeFace(): Array<CharArray?>{ //sets the cube to a 'solved' state

     return arrayOf(CharArray(9){'w'}, CharArray(9) {'b'}, CharArray(9) {'r'}, CharArray(9) {'g'}, CharArray(9){'o'}, CharArray(9) {'y'})
    }


    public fun rTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{ //prime refers to ' to turn left or right. | face refers to face facing user

        val tempFace: CharArray = charArrayOf(cubeFace[face]!![2], cubeFace[face]!![5], cubeFace[face]!![8])
        if (face.equals(0) || face.equals(5)){
        }
        else{
            cubeFace[face]!![2] = cubeFace[bottomFace]!![2]
            cubeFace[face]!![5] = cubeFace[bottomFace]!![5]
            cubeFace[face]!![8] = cubeFace[bottomFace]!![8]

           val oppositeFace: Int = utilFun.booleanToInt(face + 2 > 5)

            cubeFace[bottomFace]!![2] = cubeFace[face + 2 * (-1 * oppositeFace)]!![2]
            cubeFace[bottomFace]!![5] = cubeFace[face + 2 * (-1 * oppositeFace)]!![5]
            cubeFace[bottomFace]!![8] = cubeFace[face + 2 * (-1 * oppositeFace)]!![8]

           val oppositeBottom: Int = utilFun.booleanToInt(cubeFace[bottomFace]!![4].equals('w'))

            cubeFace[face + 2 * (-1 * oppositeFace)]!![2] = cubeFace[oppositeBottom * 5]!![2]
            cubeFace[face + 2 * (-1 * oppositeFace)]!![5] = cubeFace[oppositeBottom * 5]!![5]
            cubeFace[face + 2 * (-1 * oppositeFace)]!![8] = cubeFace[oppositeBottom * 5]!![8]

            cubeFace[oppositeBottom * 5]!![2] = tempFace[0]
            cubeFace[oppositeBottom * 5]!![5] = tempFace[1]
            cubeFace[oppositeBottom * 5]!![8] = tempFace[2]


        }
    }

    public fun lTurn(): Unit{

    }

    public fun uTurn(): Unit{

    }

    public fun fTurn(): Unit{

    }

    public fun bTurn(): Unit{

    }

    public fun dTurn(): Unit{

    }



}