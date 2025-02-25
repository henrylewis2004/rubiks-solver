package com.henry.rubiksolver

public class Cube {

    private var cubeFace: Array<CharArray?> = newCubeFace() //array containing cube data in the order (w,b,r,g,o,y)

    private val utilFun = Util()
    private val rotationOrder: Array<IntArray> = arrayOf(intArrayOf(0,1,2,3,4,5,6,7,8), intArrayOf(6,3,0,7,4,1,8,5,2), intArrayOf(8,7,6,5,4,3,2,1,0), intArrayOf(2,5,8,1,4,7,0,3,6))

    public fun newCubeFace(): Array<CharArray?>{ //sets the cube to a 'solved' state

     return arrayOf(CharArray(9){'w'}, CharArray(9) {'b'}, CharArray(9) {'r'}, CharArray(9) {'g'}, CharArray(9){'o'}, CharArray(9) {'y'})
    }

    private fun getOppositeFace(face: Int): Int{ //returns the opposite face number

        when (face){
            0 -> return 5 //yellow face
            5 -> return 0 //white face

            else -> {
                return (utilFun.booleanToInt(face + 2 > 4) * -4 + face + 2) //faces 1..4 (b,r,g,o)
            }
        }


    }

    private fun getSideFace(right: Boolean, face: Int, bottomFace: Int): Int{ //right refers to which side is wanted, if true the side to the right is returned
        when(face) {
            0 -> when (bottomFace){
                1 -> return 4 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(4)
                else -> { return (bottomFace - 1 + (2 * utilFun.booleanToInt(!right)))}
            }
            1 -> when (bottomFace){
                0 -> return 2 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(2)
                2 -> return 5 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(5)
                4 -> return 0 + utilFun.booleanToInt(!right) * getOppositeFace(0)
                5 -> return 4 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(4)
            }
            2 -> when (bottomFace){
                0 -> return 3 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(3)
                1 -> return 0 + utilFun.booleanToInt(!right) * getOppositeFace(2)
                3 -> return 5 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(5)
                5 -> return 1 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(1)
            }
            3 -> when (bottomFace){
                0 -> return 4 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(4)
                2 -> return 0 + utilFun.booleanToInt(!right) * getOppositeFace(0)
                4 -> return 5 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(5)
                5 -> return 2 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(2)
            }
            4 -> when (bottomFace){
                0 -> return 1 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(1)
                1 -> return 5 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(5)
                3 -> return 0 + utilFun.booleanToInt(!right) * getOppositeFace(0)
                5 -> return 3 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(3)

            }
            5 -> when (bottomFace){
                4 -> return 1 * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(1)
                else -> {return (bottomFace + 1 + (2 * utilFun.booleanToInt(!right)))}
            }
        }
        return -1 //should be an error if does not return earlier so will break code and will be noticeable
    }

    private fun getRotation(face: Int, bottomFace: Int): Int{
        when(face) {
            0 -> when (bottomFace){
                1 -> return 2
                2 -> return 1
                3 -> return 0
                4 -> return 3
            }
            1 -> when (bottomFace){
                0 -> return 0
                2 -> return 1
                4 -> return 3
                5 -> return 2
            }
            2 -> when (bottomFace){
                0 -> return 3
                1 -> return 2
                3 -> return 0
                5 -> return 1
            }
            3 -> when (bottomFace){
                0 -> return 2
                2 -> return 1
                4 -> return 3
                5 -> return 0
            }
            4 -> when (bottomFace){
                0 -> return 1
                1 -> return 2
                3 -> return 0
                5 -> return 3

            }
            5 -> when (bottomFace){
                1 -> return 2
                2 -> return 3
                3 -> return 0
                4 -> return 1
            }
        }
        return -1
    }

    private fun rotateFace(prime: Boolean, face: Int): Unit{ //rotates face, prime rotates counterclockwise if true (if face facing front)
        var temp: Char = cubeFace[face]!![0]
        if (prime) { //rotate counterclockwise
            cubeFace[face]!![0] = cubeFace[face]!![2]
            cubeFace[face]!![2] = cubeFace[face]!![8]
            cubeFace[face]!![8] = cubeFace[face]!![6]
            cubeFace[face]!![6] = temp

            temp = cubeFace[face]!![1]
            cubeFace[face]!![1] = cubeFace[face]!![5]
            cubeFace[face]!![5] = cubeFace[face]!![7]
            cubeFace[face]!![7] = cubeFace[face]!![3]
            cubeFace[face]!![3] = temp
        }
        else{ //rotate clockwise
            cubeFace[face]!![0] = cubeFace[face]!![6]
            cubeFace[face]!![6] = cubeFace[face]!![8]
            cubeFace[face]!![8] = cubeFace[face]!![2]
            cubeFace[face]!![2] = temp

            temp = cubeFace[face]!![1]
            cubeFace[face]!![1] = cubeFace[face]!![3]
            cubeFace[face]!![3] = cubeFace[face]!![7]
            cubeFace[face]!![7] = cubeFace[face]!![3]
            cubeFace[face]!![5] = temp
        }

    }

    public fun rTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{ //prime refers to ' to turn the opposite direction. | face refers to face facing user

        val oppositeFace: Int = getOppositeFace(face)
        val oppositeBottom: Int = getOppositeFace(bottomFace)
        var rotation: Int = getRotation(face, bottomFace)

        val tempFace: CharArray = charArrayOf(cubeFace[face]!![rotationOrder[rotation][2]], cubeFace[face]!![rotationOrder[rotation][5]], cubeFace[face]!![rotationOrder[rotation][8]])
        //work here
        if (!prime) {
            //front face
            cubeFace[face]!![2] = cubeFace[bottomFace]!![2]
            cubeFace[face]!![5] = cubeFace[bottomFace]!![5]
            cubeFace[face]!![8] = cubeFace[bottomFace]!![8]

            //bottom face
            cubeFace[bottomFace]!![2] = cubeFace[oppositeFace]!![0]
            cubeFace[bottomFace]!![5] = cubeFace[oppositeFace]!![3]
            cubeFace[bottomFace]!![8] = cubeFace[oppositeFace]!![6]

            //back face
            cubeFace[oppositeFace]!![0] = cubeFace[oppositeBottom]!![2]
            cubeFace[oppositeFace]!![3] = cubeFace[oppositeBottom]!![5]
            cubeFace[oppositeFace]!![6] = cubeFace[oppositeBottom]!![8]

            //top face
            cubeFace[oppositeBottom]!![2] = tempFace[0]
            cubeFace[oppositeBottom]!![5] = tempFace[1]
            cubeFace[oppositeBottom]!![8] = tempFace[2]
        }
        else{
            //front face
            cubeFace[face]!![2] = cubeFace[oppositeBottom]!![2]
            cubeFace[face]!![5] = cubeFace[oppositeBottom]!![5]
            cubeFace[face]!![8] = cubeFace[oppositeBottom]!![8]

            //top face
            cubeFace[oppositeBottom]!![2] = cubeFace[oppositeFace]!![2]
            cubeFace[oppositeBottom]!![5] = cubeFace[oppositeFace]!![5]
            cubeFace[oppositeBottom]!![8] = cubeFace[oppositeFace]!![8]

            //back face
            cubeFace[oppositeFace]!![2] = cubeFace[bottomFace]!![2]
            cubeFace[oppositeFace]!![5] = cubeFace[bottomFace]!![5]
            cubeFace[oppositeFace]!![8] = cubeFace[bottomFace]!![8]

            //bottom face
            cubeFace[bottomFace]!![2] = tempFace[0]
            cubeFace[bottomFace]!![5] = tempFace[1]
            cubeFace[bottomFace]!![8] = tempFace[2]
        }
        //rotate side piece
        rotateFace(prime, getSideFace(!prime, face, bottomFace))

    }

    public fun lTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{
        rTurn(prime, face, getOppositeFace(bottomFace))
    }

    public fun uTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{

        val tempFace: CharArray = charArrayOf(cubeFace[face]!![0], cubeFace[face]!![1], cubeFace[face]!![2])
        val oppositeFace: Int = getOppositeFace(face)
        val sideFace = getSideFace(true, face, bottomFace)
        val oppositeSideFace = getOppositeFace(sideFace)

        if (!prime){
            //front face
            cubeFace[face]!![0] = cubeFace[sideFace]!![0]
            cubeFace[face]!![1] = cubeFace[sideFace]!![1]
            cubeFace[face]!![2] = cubeFace[sideFace]!![2]

            //right side face
            cubeFace[sideFace]!![0] = cubeFace[oppositeFace]!![0]
            cubeFace[sideFace]!![1] = cubeFace[oppositeFace]!![1]
            cubeFace[sideFace]!![2] = cubeFace[oppositeFace]!![2]

            //back face
            cubeFace[oppositeFace]!![0] = cubeFace[oppositeSideFace]!![0]
            cubeFace[oppositeFace]!![1] = cubeFace[oppositeSideFace]!![1]
            cubeFace[oppositeFace]!![2] = cubeFace[oppositeSideFace]!![2]

            //left side face
            cubeFace[oppositeSideFace]!![0] = tempFace[0]
            cubeFace[oppositeSideFace]!![1] = tempFace[1]
            cubeFace[oppositeSideFace]!![2] = tempFace[2]

        }
        else{
        }

    }

    public fun fTurn(prime: Boolean, face: Int): Unit{
        rotateFace(prime, face)

    }

    public fun bTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{

    }

    public fun dTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{

    }



}