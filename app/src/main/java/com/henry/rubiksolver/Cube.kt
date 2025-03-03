package com.henry.rubiksolver

public class Cube {

    private var cubeFace: Array<CharArray?> = newCubeFace() //array containing cube data in the order (w,b,r,g,o,y)

    private val utilFun = Util()
    private val rotationOrder: Array<IntArray> = arrayOf(intArrayOf(0,1,2,3,4,5,6,7,8), intArrayOf(6,3,0,7,4,1,8,5,2), intArrayOf(8,7,6,5,4,3,2,1,0), intArrayOf(2,5,8,1,4,7,0,3,6))

    public fun newCubeFace(): Array<CharArray?>{ //sets the cube to a 'solved' state

     return arrayOf(CharArray(9){'w'}, CharArray(9) {'b'}, CharArray(9) {'r'}, CharArray(9) {'g'}, CharArray(9){'o'}, CharArray(9) {'y'})
    }
	
	public fun getCube(): Array<CharArray?>{
		return cubeFace
	}
	
	
	public fun getCubeFace(face: Int): CharArray{
		return cubeFace[face]
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
		
		val order: IntArray
      //  if (!prime) {
			if (prime){order = [face,oppositeBottom, oppositeFace, bottomFace]}
			else {order = [face, bottomFace, oppositeFace, oppositeBottom]}
	  
            //front face
			val rotationSwap: Int = getRotation(order[1], order[2 - (2 * booleanToInt(prime))])
			
            cubeFace[order[0]]!![rotationOrder[rotation][2]] = cubeFace[order[1]]!![rotationOrder[rotationSwap][2]]
            cubeFace[order[0]]!![rotationOrder[rotation][5]] = cubeFace[order[1]]!![rotationOrder[rotationSwap][5]]
            cubeFace[order[0]]!![rotationOrder[rotation][8]] = cubeFace[order[1]]!![rotationOrder[rotationSwap][8]]

            //bottom face
			rotation = getRotation(order[2], order[3 - (2 * booleanToInt(prime))]) 
			
            cubeFace[order[1]]!![rotationOrder[rotationSwap][2]] = cubeFace[order[2]]!![rotationOrder[rotation][0]] 
            cubeFace[order[1]]!![rotationOrder[rotationSwap][5]] = cubeFace[order[2]]!![rotationOrder[rotation][3]]
            cubeFace[order[1]]!![rotationOrder[rotationSwap][8]] = cubeFace[order[2]]!![rotationOrder[rotation][6]]

            //back face
			rotationSwap = getRotation(order[3], order[0 + (2 * booleanToInt(prime))])
			
            cubeFace[order[2]]!![rotationOrder[rotation][0]] = cubeFace[order[3]]!![rotationOrder[rotationSwap][2]]
            cubeFace[order[2]]!![rotationOrder[rotation][3]] = cubeFace[order[3]]!![rotationOrder[rotationSwap][5]]
            cubeFace[order[2]]!![rotationOrder[rotation][6]] = cubeFace[order[3]]!![rotationOrder[rotationSwap][8]]

            //top face	
            cubeFace[order[3]]!![rotationOrder[rotationSwap][2]] = tempFace[0]
            cubeFace[order[3]]!![rotationOrder[rotationSwap][5]] = tempFace[1]
            cubeFace[order[3]]!![rotationOrder[rotationSwap][8]] = tempFace[2]
        /*}
        else{
            //front face
			val rotationSwap = getRotation(oppositeBottom, face)
			
            cubeFace[face]!![rotationOrder[rotation][2]] = cubeFace[oppositeBottom]!![rotationOrder[rotationSwap][2]]
            cubeFace[face]!![rotationOrder[rotation][5]] = cubeFace[oppositeBottom]!![rotationOrder[rotationSwap][5]]
            cubeFace[face]!![rotationOrder[rotation][8]] = cubeFace[oppositeBottom]!![rotationOrder[rotationSwap][8]]

            //top face
			rotation = getRotation(oppositeFace, oppositeBottom)
			
            cubeFace[oppositeBottom]!![rotationOrder[rotationSwap][2]] = cubeFace[oppositeFace]!![rotationOrder[rotation][0]]
            cubeFace[oppositeBottom]!![rotationOrder[rotationSwap][5]] = cubeFace[oppositeFace]!![rotationOrder[rotation][3]]
            cubeFace[oppositeBottom]!![rotationOrder[rotationSwap][8]] = cubeFace[oppositeFace]!![rotationOrder[rotation][6]]

            //back face
			rotationSwap = getRotation(bottomFace, oppositeFace)
			
            cubeFace[oppositeFace]!![rotationOrder[rotation][0]] = cubeFace[bottomFace]!![rotationOrder[rotationSwap][2]]
            cubeFace[oppositeFace]!![rotationOrder[rotation][3]] = cubeFace[bottomFace]!![rotationOrder[rotationSwap][5]]
            cubeFace[oppositeFace]!![rotationOrder[rotation][6]] = cubeFace[bottomFace]!![rotationOrder[rotationSwap][8]]

            //bottom face
            cubeFace[bottomFace]!![rotationOrder[rotationSwap][2]] = tempFace[0]
            cubeFace[bottomFace]!![rotationOrder[rotationSwap][5]] = tempFace[1]
            cubeFace[bottomFace]!![rotationOrder[rotationSwap][8]] = tempFace[2]
        }*/
        //rotate side piece
        rotateFace(prime, getSideFace(!prime, face, bottomFace))

    }

    public fun uTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{

        val oppositeFace: Int = getOppositeFace(face)
        val sideFace = getSideFace(true, face, bottomFace)
        val oppositeSideFace = getOppositeFace(sideFace)
		
		val rotation = getRotation(face, bottomFace)
        val tempFace: CharArray = charArrayOf(cubeFace[face]!![rotationOrder[rotation][0]], cubeFace[face]!![rotationOrder[rotation][1]], cubeFace[face]!![rotationOrder[rotation][2]])

        if (!prime){
            //front face
			val rotationSwap = getRotation(sideFace, bottomFace)
			
            cubeFace[face]!![rotationOrder[rotation][0]] = cubeFace[sideFace]!![rotationOrder[rotationSwap][0]]
            cubeFace[face]!![rotationOrder[rotation][1]] = cubeFace[sideFace]!![rotationOrder[rotationSwap][1]]
            cubeFace[face]!![rotationOrder[rotation][2]] = cubeFace[sideFace]!![rotationOrder[rotationSwap][2]]

            //right side face
			rotation = getRotation(oppositeFace, bottomFace)
			
            cubeFace[sideFace]!![rotationOrder[rotationSwap][0]] = cubeFace[oppositeFace]!![rotationOrder[rotation][0]]
            cubeFace[sideFace]!![rotationOrder[rotationSwap][1]] = cubeFace[oppositeFace]!![rotationOrder[rotation][1]]
            cubeFace[sideFace]!![rotationOrder[rotationSwap][2]] = cubeFace[oppositeFace]!![rotationOrder[rotation][2]]

            //back face
			rotationSwap = getRotation(oppositeSideFace, bottomFace)
			
            cubeFace[oppositeFace]!![rotationOrder[rotation][0]] = cubeFace[oppositeSideFace]!![rotationOrder[rotationSwap][0]]
            cubeFace[oppositeFace]!![rotationOrder[rotation][1]] = cubeFace[oppositeSideFace]!![rotationOrder[rotationSwap][1]]
            cubeFace[oppositeFace]!![rotationOrder[rotation][2]] = cubeFace[oppositeSideFace]!![rotationOrder[rotationSwap][2]]

            //left side face
            cubeFace[oppositeSideFace]!![rotationOrder[rotationSwap][0]] = tempFace[0]
            cubeFace[oppositeSideFace]!![rotationOrder[rotationSwap][1]] = tempFace[1]
            cubeFace[oppositeSideFace]!![rotationOrder[rotationSwap][2]] = tempFace[2]

        }
        else{
        }
		rotateFace(prime, getOppositeFace(bottomFace))

    }
	
	public fun lTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{
        rTurn(prime, face, getOppositeFace(bottomFace))
    }
	
    public fun fTurn(prime: Boolean, face: Int): Unit{
        rTurn(prime, getSideFace(!prime, face, bottomFace))
    }

    public fun bTurn(prime: Boolean, face: Int): Unit{
		fTurn(prime, getOppositeFace(face))
    }

    public fun dTurn(prime: Boolean, face: Int, bottomFace: Int): Unit{
		uTurn(prime, face, getOppositeFace(bottomFace))
    }



}