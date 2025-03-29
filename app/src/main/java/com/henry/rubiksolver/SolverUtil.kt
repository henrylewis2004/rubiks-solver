package com.henry.rubiksolver

import kotlin.math.abs


public class SolverUtil {

    public fun isFaceSolved(face: CharArray): Boolean{
        val middleSquare: Char
        when(face.size) {
            9 -> {middleSquare = face[4] //3x3
                for (square in face) {
                    if (!square.equals(middleSquare)){return false}
                }
            }
        }

        return true
    }

    public fun isCrossSolved(cube: Cube, face: Int, bottomFace: Int): Boolean{
        val cubeFace = cube.getCube()
        val squares: IntArray = intArrayOf(1,3,5,7)

        for (square in squares){

            if (cubeFace[face][square] != cubeFace[face][4]){ return false}
            if (cube.getSideColour(square, face,bottomFace) != cube.getSideFaceColour(square, face,bottomFace)){return false}
        }

        return true
    }

    public fun correctCrossCount(face: Int,bottomFace: Int, cubeFace: Cube): Int{
        val crossSquares: IntArray = intArrayOf(1,3,5,7)
        val targetChar: Char = cubeFace.getCubeFace(face)[4]
        var cnt: Int = 0

        for (square in crossSquares){
            if (cubeFace.getCubeFace(face)[square] == targetChar && sideCorrect(cubeFace, square,face,bottomFace)){cnt ++}
        }
        return cnt
    }


    public fun getWhiteTargetCrossSquare(toPlaceChar: Char): Int{
            when (toPlaceChar) {
                'r' -> return 5
                'b' -> return 1
                'o' -> return 3
                'g' -> return 7
            }


        return -1
    }

    public fun getTargetPos(col: Char): CharArray{
        when(col){
            'b' -> charArrayOf('o','r','g')
            'o' -> charArrayOf('g','b','r')
            'r' -> charArrayOf('b','g','o')
            'g' -> charArrayOf('r','o','b')

        }
        return charArrayOf()
    }


    public fun sideCorrect(cube: Cube, square: Int, face: Int, bottomFace: Int): Boolean{
        return (cube.getSideColour(square,face,bottomFace) == cube.getSideFaceColour(square,face,bottomFace))
    }


    public fun solvedCube(cubeFace: Array<CharArray>): Boolean{
        for (face in cubeFace){
            if (!isFaceSolved(face!!)){return false}
        }
        return true
    }


    public fun getColour(face: Int): Char{
        when(face){
            0 -> return 'w'
            1 -> return 'b'
            2 -> return 'r'
            3 -> return 'g'
            4 -> return 'o'
            5 -> return 'y'
        }
        return 'e' //e for error
    }

    public fun getFaceFromColour(colour: Char): Int{
        when(colour){
            'w' -> return faces.WHITE.ordinal
            'b' -> return faces.BLUE.ordinal
            'r' -> return faces.RED.ordinal
            'g' -> return faces.GREEN.ordinal
            'o' -> return faces.ORANGE.ordinal
            'y' -> return faces.YELLOW.ordinal
        }
        return -1
    }


    public fun move(cube: Cube, prime: Boolean, action: String, face: Int, bottomFace: Int): String{
        when(action){
            "r" -> cube.rTurn(prime, faces.values()[face].ordinal, faces.values()[bottomFace].ordinal)
            "l" -> cube.lTurn(prime, faces.values()[face].ordinal, faces.values()[bottomFace].ordinal)
            "f" -> cube.fTurn(prime, faces.values()[face].ordinal, faces.values()[bottomFace].ordinal)
            "b" -> cube.bTurn(prime, faces.values()[face].ordinal, faces.values()[bottomFace].ordinal)
            "u" -> cube.uTurn(prime, faces.values()[face].ordinal, faces.values()[bottomFace].ordinal)
            "d" -> cube.dTurn(prime, faces.values()[face].ordinal, faces.values()[bottomFace].ordinal)
        }
        if (prime){return (action + "'")}
        return (action)
    }

    public fun rotateYLayer(frontFace:Int, col:Int): Array<String>{
        val value:Int = abs(frontFace - col)
        when{
            value == 2-> return arrayOf("u","u")
            frontFace == faces.ORANGE.ordinal ->{
                var a = arrayOf("u")
                if(frontFace - col == 3){ a += "'"}
                return a
            }
            frontFace == faces.BLUE.ordinal ->{
                var a = arrayOf("u")
                if(frontFace - col == -1){ a += "'"}
                return a
            }
            else -> {
                var a = arrayOf("u")
                if (frontFace - col < 0) { a += "'"}
                return a
            }

        }
        return arrayOf()
    }
}