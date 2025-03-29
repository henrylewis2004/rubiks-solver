package com.henry.rubiksolver

import android.util.Log

enum class faces {WHITE, BLUE, RED, GREEN, ORANGE, YELLOW}


public open class Cube {

    private var cubeFace: Array<CharArray> = newCubeFace(9) //array containing cube data in the order (w,b,r,g,o,y)

    private val utilFun = Util()
    private val rotationOrder: Array<IntArray> = arrayOf(intArrayOf(0,1,2,3,4,5,6,7,8), intArrayOf(6,3,0,7,4,1,8,5,2), intArrayOf(8,7,6,5,4,3,2,1,0), intArrayOf(2,5,8,1,4,7,0,3,6))


    public fun newCubeFace(size: Int): Array<CharArray>{ //sets the cube to a 'solved' state
     return arrayOf(CharArray(size){'w'}, CharArray(size) {'b'}, CharArray(size) {'r'}, CharArray(size) {'g'}, CharArray(size){'o'}, CharArray(size) {'y'})
    }

    public open fun setCubeFaces(faces: Array<CharArray>){
        cubeFace = faces
    }

    public fun mapCubeFace(face: Int, faceInput: CharArray): Unit{
        if (faceInput.size.equals(cubeFace[face].size)) {
            cubeFace[face] = faceInput
        }
        else{ println("error, different CharArray sizes when mapping cube")}

    }

	
	public fun getCube(): Array<CharArray>{
		return cubeFace
	}
	
	
	public fun getCubeFace(face: Int): CharArray{
		return cubeFace[face]
	}

    public fun getCubeFaceSquare(face: Int, square: Int): Char{
        if (square > cubeFace[face].size) { return 'e'} //e for error
        return cubeFace[face][square]
    }

    public fun getFaceColour(face: Int): Char{
        return cubeFace[face][4]
    }
	

    public fun getOppositeFace(face: Int): Int{ //returns the opposite face number

        when (face){
            faces.WHITE.ordinal -> return faces.YELLOW.ordinal
            faces.YELLOW.ordinal -> return faces.WHITE.ordinal

            else -> {
                return (utilFun.booleanToInt(face + 2 > 4) * -4 + face + 2) //faces 1..4 (b,r,g,o)
            }
        }


    }

    public fun getSquare(face: Int, bottomFace: Int, square: Int): Char{
        return cubeFace[face][rotationOrder[getRotation(face, bottomFace)][square]]
    }

    public fun getSquarePos(face: Int, bottomFace: Int, square: Int): Int{
        return rotationOrder[getRotation(face, bottomFace)][square]
    }

    public fun getSideFace(right: Boolean, face: Int, bottomFace: Int): Int{ //right refers to which side is wanted, if true the side to the right is returned
        when(face) {
            faces.WHITE.ordinal -> when (bottomFace){
                faces.BLUE.ordinal -> return faces.ORANGE.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.ORANGE.ordinal)
                else -> { return ( (bottomFace - 1) * utilFun.booleanToInt(right) + (getOppositeFace(bottomFace - 1) * utilFun.booleanToInt(!right)) )}
            }
            faces.BLUE.ordinal -> when (bottomFace){
                faces.WHITE.ordinal -> return faces.RED.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.RED.ordinal)
                faces.RED.ordinal -> return faces.YELLOW.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.YELLOW.ordinal)
                faces.ORANGE.ordinal -> return faces.WHITE.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.WHITE.ordinal)
                faces.YELLOW.ordinal -> return faces.ORANGE.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.ORANGE.ordinal)
            }
            faces.RED.ordinal -> when (bottomFace){
                faces.WHITE.ordinal -> return faces.GREEN.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.GREEN.ordinal)
                faces.BLUE.ordinal -> return faces.WHITE.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.WHITE.ordinal)
                faces.GREEN.ordinal -> return faces.YELLOW.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.YELLOW.ordinal)
                faces.YELLOW.ordinal -> return faces.BLUE.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.BLUE.ordinal)
            }
            faces.GREEN.ordinal -> when (bottomFace){
                faces.WHITE.ordinal -> return faces.ORANGE.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.ORANGE.ordinal)
                faces.RED.ordinal -> return faces.WHITE.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.WHITE.ordinal)
                faces.ORANGE.ordinal -> return faces.YELLOW.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.YELLOW.ordinal)
                faces.YELLOW.ordinal -> return faces.RED.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.RED.ordinal)
            }
            faces.ORANGE.ordinal -> when (bottomFace){
                faces.WHITE.ordinal -> return faces.BLUE.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.BLUE.ordinal)
                faces.BLUE.ordinal -> return faces.YELLOW.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.YELLOW.ordinal)
                faces.GREEN.ordinal -> return faces.WHITE.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.WHITE.ordinal)
                faces.YELLOW.ordinal -> return faces.GREEN.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.GREEN.ordinal)

            }
            faces.YELLOW.ordinal -> when (bottomFace){
                faces.ORANGE.ordinal -> return faces.BLUE.ordinal * utilFun.booleanToInt(right) + utilFun.booleanToInt(!right) * getOppositeFace(faces.BLUE.ordinal)
                else -> {return ( (bottomFace + 1) * utilFun.booleanToInt(right) + (getOppositeFace(bottomFace + 1) * utilFun.booleanToInt(!right)))}
            }
        }
        return -1 //should be an error if does not return earlier so will break code and will be noticeable
    }

    public fun getSideFaceColour(side: Int,frontFace: Int,bottomFace: Int): Char{
        val rotSquare: Int = rotationOrder[getRotation(frontFace,bottomFace)][side]

        when(rotSquare){
            1 -> return cubeFace[getSideFace(false,frontFace,getSideFace(false,frontFace,bottomFace))][4]
            3 -> return cubeFace[getSideFace(false,frontFace,bottomFace)][4]
            5 -> return cubeFace[getSideFace(true,frontFace,bottomFace)][4]
            7 -> return cubeFace[getSideFace(true,frontFace,getSideFace(false,frontFace,bottomFace))][4]
        }


        return 'x'
    }

    public fun getSideColour(square: Int, frontFace: Int, bottomFace: Int): Char{
        val rotSquare: Int = rotationOrder[getRotation(frontFace,bottomFace)][square]

        when(rotSquare)
        {
            1 -> return cubeFace[getSideFace(false,frontFace,getSideFace(false,frontFace,bottomFace))][7]
            3 -> return cubeFace[getSideFace(false,frontFace,bottomFace)][5]
            5 -> return cubeFace[getSideFace(true,frontFace,bottomFace)][3]
            7 -> return cubeFace[getSideFace(true,frontFace,getSideFace(false,frontFace,bottomFace))][1]
        }
        return 'x'
    }

    public fun getSideSquare(square: Int, frontFace: Int, bottomFace: Int): Int{
        val rotSquare: Int = rotationOrder[getRotation(frontFace,bottomFace)][square]

        when(rotSquare){
            1 -> return 7
            3 -> return 5
            5 -> return 3
            7 -> return 1
        }

        return -1

    }

    private fun getRotation(face: Int, bottomFace: Int): Int{
        when(face) {
            faces.WHITE.ordinal -> when (bottomFace){
                faces.BLUE.ordinal -> return 2
                faces.RED.ordinal -> return 1
                faces.GREEN.ordinal -> return 0
                faces.ORANGE.ordinal -> return 3
            }
            faces.BLUE.ordinal -> when (bottomFace){
                faces.WHITE.ordinal -> return 0
                faces.RED.ordinal -> return 1
                faces.ORANGE.ordinal -> return 3
                faces.YELLOW.ordinal -> return 2
            }
            faces.RED.ordinal -> when (bottomFace){
                faces.WHITE.ordinal -> return 3
                faces.BLUE.ordinal -> return 2
                faces.GREEN.ordinal -> return 0
                faces.YELLOW.ordinal -> return 1
            }
            faces.GREEN.ordinal -> when (bottomFace){
                faces.WHITE.ordinal -> return 2
                faces.RED.ordinal -> return 1
                faces.ORANGE.ordinal -> return 3
                faces.YELLOW.ordinal -> return 0
            }
            faces.ORANGE.ordinal  -> when (bottomFace){
                faces.WHITE.ordinal -> return 1
                faces.BLUE.ordinal -> return 2
                faces.GREEN.ordinal -> return 0
                faces.YELLOW.ordinal -> return 3

            }
            faces.YELLOW.ordinal -> when (bottomFace){
                faces.BLUE.ordinal -> return 2
                faces.RED.ordinal -> return 3
                faces.GREEN.ordinal -> return 0
                faces.ORANGE.ordinal -> return 1
            }
        }
        return -1
    }

    private fun rotateFace(prime: Boolean, face: Int): Unit{ //rotates face, prime rotates counterclockwise if true (if face facing front)
        var temp: Char = cubeFace[face][0]
        var order: IntArray = intArrayOf(0,6,8,2,1,3,7,5)
        if (prime){ order = intArrayOf(0,2,8,6,1,5,7,3)}

       for (i in 0..7) {
           if (i.equals(3) || i.equals(7)){
               cubeFace[face][order[i]] = temp
               temp = cubeFace[face][1]
           }
           else{cubeFace[face][order[i]] = cubeFace[face][order[i+1]]}

       }
    }

    public fun rTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{ //prime refers to ' to turn the opposite direction. | face refers to face facing user

        val oppositeFace: Int = getOppositeFace(face)
        val oppositeBottom: Int = getOppositeFace(bottomFace)
        var rotation: Int = getRotation(face, bottomFace)

        val tempFace: CharArray = charArrayOf(cubeFace[face][rotationOrder[rotation][2]], cubeFace[face][rotationOrder[rotation][5]], cubeFace[face][rotationOrder[rotation][8]])
		
        var order = intArrayOf(face,oppositeBottom, oppositeFace, bottomFace)
        if (!prime){order = intArrayOf(face, bottomFace, oppositeFace, oppositeBottom)}

        //front face
        var i: Int = 0
        var rotationSwap: Int = getRotation(order[i+1], order[i+2 - (2 * utilFun.booleanToInt(prime))]) //rotation for either top or bottom, depending on prime

        cubeFace[order[i]][rotationOrder[rotation][2]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][2]]
        cubeFace[order[i]][rotationOrder[rotation][5]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][5]]
        cubeFace[order[i]][rotationOrder[rotation][8]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][8]]
        i++

        //bottom or top face
        rotation = getRotation(order[i+1],bottomFace) //rotation for back piece

        cubeFace[order[i]][rotationOrder[rotationSwap][2]] = cubeFace[order[i + 1]][rotationOrder[rotation][6]]
        cubeFace[order[i]][rotationOrder[rotationSwap][5]] = cubeFace[order[i + 1]][rotationOrder[rotation][3]]
        cubeFace[order[i]][rotationOrder[rotationSwap][8]] = cubeFace[order[i + 1]][rotationOrder[rotation][0]]
        i++

        //back face
        rotationSwap = getRotation(order[i+1], order[0 + (2 * utilFun.booleanToInt(prime))]) //rotation for either top or bottom, depending on prime

        cubeFace[order[i]][rotationOrder[rotation][0]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][2]]
        cubeFace[order[i]][rotationOrder[rotation][3]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][5]]
        cubeFace[order[i]][rotationOrder[rotation][6]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][8]]
        i++

        //top or bottom face
        cubeFace[order[i]][rotationOrder[rotationSwap][2]] = tempFace[0]
        cubeFace[order[i]][rotationOrder[rotationSwap][5]] = tempFace[1]
        cubeFace[order[i]][rotationOrder[rotationSwap][8]] = tempFace[2]

        //rotate side piece
        rotateFace(prime, getSideFace(!prime, face, bottomFace))
    }

    public fun uTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{

        val oppositeFace: Int = getOppositeFace(face)
        val sideFace = getSideFace(true, face, bottomFace)
        val oppositeSideFace = getOppositeFace(sideFace)
		
		var rotation = getRotation(face, bottomFace)
        val tempFace: CharArray = charArrayOf(cubeFace[face][rotationOrder[rotation][0]], cubeFace[face][rotationOrder[rotation][1]], cubeFace[face][rotationOrder[rotation][2]])

        var order: IntArray = intArrayOf(face, oppositeSideFace, oppositeFace, sideFace)
        if (!prime){ order = intArrayOf(face, sideFace, oppositeFace,oppositeSideFace)}

        //front face
        var i: Int = 0
        var rotationSwap = getRotation(order[i+1],bottomFace)

        cubeFace[order[i]][rotationOrder[rotation][0]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][0]]
        cubeFace[order[i]][rotationOrder[rotation][1]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][1]]
        cubeFace[order[i]][rotationOrder[rotation][2]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][2]]
        i++

        //side face
        rotation = getRotation(order[i+1], bottomFace)

        cubeFace[order[i]][rotationOrder[rotationSwap][0]] = cubeFace[order[i+1]][rotationOrder[rotation][0]]
        cubeFace[order[i]][rotationOrder[rotationSwap][1]] = cubeFace[order[i+1]][rotationOrder[rotation][1]]
        cubeFace[order[i]][rotationOrder[rotationSwap][2]] = cubeFace[order[i+1]][rotationOrder[rotation][2]]
        i++

        //back face
        rotationSwap = getRotation(order[i+1], bottomFace)

        cubeFace[order[i]][rotationOrder[rotation][0]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][0]]
        cubeFace[order[i]][rotationOrder[rotation][1]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][1]]
        cubeFace[order[i]][rotationOrder[rotation][2]] = cubeFace[order[i+1]][rotationOrder[rotationSwap][2]]
        i++

        //other side face
        cubeFace[order[i]][rotationOrder[rotationSwap][0]] = tempFace[0]
        cubeFace[order[i]][rotationOrder[rotationSwap][1]] = tempFace[1]
        cubeFace[order[i]][rotationOrder[rotationSwap][2]] = tempFace[2]

        //rotate top face
		rotateFace(prime, getOppositeFace(bottomFace))

    }
	
	public fun lTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{
        rTurn(prime, face, getOppositeFace(bottomFace))
    }
	
    public fun fTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{
        rTurn(prime, getSideFace(false, face, bottomFace), bottomFace)
    }

    public fun bTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{
		fTurn(prime, getOppositeFace(face), bottomFace)
    }

    public fun dTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{
		uTurn(prime, face, getOppositeFace(bottomFace))
    }



}