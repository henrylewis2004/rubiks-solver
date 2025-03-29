package com.henry.rubiksolver

import android.util.Log
import kotlin.math.abs

public open class Solver constructor(level: Int) {
    private val solverUtil: SolverUtil = SolverUtil()
    private val difficulty: Int = level
    private var frontFace: Int = faces.WHITE.ordinal
    private var bottomFace: Int = faces.GREEN.ordinal
    private val utilFul: Util = Util()

    public fun getDifficulty(): Int{
        return difficulty
    }

    public fun getAlgorithm(cubeFaces: Array<CharArray>): Array<String>{
        var algorithm: Array<String> = arrayOf()
        val solveCube: Cube = Cube()
        solveCube.setCubeFaces(cubeFaces)

        algorithm += getWhiteFace(solveCube)

        algorithm += getSecondLayer(solveCube)
        /*
        algorithm += getYellowFace(solveCube)
        algorithm += finalSolve(solveCube)

         */


        return algorithm
    }

    private fun getWhiteFace(cube: Cube): Array<String> {
        var algorithm: Array<String> = arrayOf()
        val crossSquares: IntArray = intArrayOf(3, 1, 5, 7)
        var cubeFace: CharArray = cube.getCubeFace(faces.WHITE.ordinal)
        bottomFace = faces.GREEN.ordinal

        while (!solverUtil.isCrossSolved(cube, faces.WHITE.ordinal, faces.GREEN.ordinal)) { //white cross
            cubeFace = cube.getCubeFace(faces.WHITE.ordinal)

            for (squareId in crossSquares.indices) {
                val square = crossSquares[squareId]

                if (cubeFace[square] == 'w') {
                    val sideFaceColour: Char = cube.getSideFaceColour(square, faces.WHITE.ordinal, faces.GREEN.ordinal)
                    val sideColour: Char = cube.getSideColour(square, faces.WHITE.ordinal, faces.GREEN.ordinal)

                    if (sideColour != sideFaceColour) {
                        bottomFace = faces.GREEN.ordinal
                        val whiteSqCnt = solverUtil.correctCrossCount(faces.WHITE.ordinal, bottomFace, cube)

                        if (whiteSqCnt == 0) { //no correct squares yet
                            var nextSquare: Int = squareId - 1
                            if (nextSquare < 0) {
                                nextSquare = 3
                            }
                            algorithm += "rot_To_${solverUtil.getColour(faces.WHITE.ordinal)}"

                            if (cube.getSideFaceColour(crossSquares[nextSquare], faces.WHITE.ordinal, bottomFace) == sideColour) {
                                algorithm += solverUtil.move(cube, true, "f", faces.WHITE.ordinal, bottomFace)
                            }

                            else {
                                algorithm += solverUtil.move(cube, false, "f", faces.WHITE.ordinal, bottomFace)

                                if (sideColour != cube.getSideFaceColour(crossSquares[squareId + 1 - (4 * utilFul.booleanToInt(squareId == 3))], faces.WHITE.ordinal, bottomFace)) {
                                    algorithm += solverUtil.move(cube,false, "f", faces.WHITE.ordinal, bottomFace)
                                }
                            }
                        }
                        else {
                            val targetSquare = solverUtil.getWhiteTargetCrossSquare(sideColour)
                            var squarePlacement: Int = 0
                            var a: Array<String> = arrayOf()

                            for (i in 0..3) {
                                if (targetSquare == crossSquares[i]) {
                                    squarePlacement = i
                                }
                            }

                            when (square) {
                                1 -> a += "u"
                                3 -> a += "l"
                                5 -> a += "r"
                                7 -> a += "d"
                            }

                            val distance = squarePlacement - squareId
                            when {
                                abs(distance) == 2 -> a += arrayOf("f", "f", a[0] + "'", "f", "f")
                                distance == 1 || distance == -3 -> arrayOf("f'", a[0] + "'", "f")
                                else -> a += arrayOf("f", a[0] + "'", "f'")
                            }

                            a += (a[0])

                            for (move in a) {
                                algorithm += solverUtil.move(cube, move.length > 1, move[0].toString(), faces.WHITE.ordinal, bottomFace)
                            }
                        }
                        break
                    }
                }

            }

            for (face in 1..5){
                when {
                    face < 5 -> bottomFace = faces.WHITE.ordinal
                    face == 5 -> bottomFace = faces.GREEN.ordinal

                }
                cubeFace = cube.getCubeFace(face)
                for (squareId in crossSquares){
                    var a: Array<String> = arrayOf()

                    val adjSquareId = cube.getSquarePos(face,bottomFace,squareId)
                    val square = cubeFace[adjSquareId]
                    if (square == 'w'){

                        if (face!=5) {
                            algorithm += "rot_to_${solverUtil.getColour(face)}"
                            if(squareId == 7){
                                algorithm += solverUtil.move(cube,true,"f",face,bottomFace)
                                if (cube.getSideColour(5, bottomFace, cube.getOppositeFace(face)) != 'w') {
                                    algorithm += solverUtil.move(cube,true,"r",face,bottomFace)
                                    break
                                }
                            }


                            while (cube.getSideColour(7,face,bottomFace) == 'w'){
                                algorithm += solverUtil.move(cube,false,"d",face,bottomFace)
                            }
                            if(squareId == 1) {
                                algorithm += solverUtil.move(cube, false, "f", face, bottomFace)

                                if (cube.getSideColour(5, bottomFace, cube.getOppositeFace(face)) != 'w') {
                                    algorithm += solverUtil.move(cube,true,"r",face,bottomFace)
                                    break
                                }
                            }
                            while (cube.getSideColour(7,face,bottomFace) == 'w'){
                                algorithm += solverUtil.move(cube,false,"d",face,bottomFace)
                            }
                            when (squareId) {
                                3 -> a += arrayOf("d'", "l")
                                else -> a += arrayOf("d", "r'")
                            }
                        }
                        else{
                            var targetFace: Int = 0
                            val bottomFace: Int = faces.WHITE.ordinal

                            for (whiteSquareId in crossSquares){
                                if (cube.getCubeFace(0)[whiteSquareId] != 'w'){
                                    when (whiteSquareId){
                                        1-> targetFace = faces.BLUE.ordinal
                                        3-> targetFace = faces.ORANGE.ordinal
                                        5 -> targetFace = faces.RED.ordinal
                                        7 -> targetFace = faces.GREEN.ordinal
                                    }
                                    break
                                }
                            }
                            algorithm += "rot_To_${solverUtil.getColour(targetFace)}"



                            while(cube.getSideColour(1,targetFace,bottomFace) != 'w'){
                                algorithm += solverUtil.move(cube,false,"u",targetFace,bottomFace)
                            }

                            a+= arrayOf("f","f")

                            for (move in a){
                                algorithm += solverUtil.move(cube,move.length > 1, move[0].toString(),targetFace,bottomFace)
                            }
                            break

                        }

                        for (move in a){
                            algorithm += solverUtil.move(cube,move.length > 1, move[0].toString(),face,bottomFace)
                        }

                        break
                    }
                }
            }
        }

        return algorithm
    }

    private fun getSecondLayer(cube: Cube): Array<String>{
        var algorithm: Array<String> = arrayOf()


        return algorithm
    }

    private fun getYellowFace(cube: Cube): Array<String>{
        var algorithm: Array<String> = arrayOf("")


        return algorithm

    }

    private fun finalSolve(cube: Cube): Array<String>{
        var algorithm: Array<String> = arrayOf("")
        var solvedSide: Boolean = false
        var solvedFace: Int = 0

        for (face in 1..4){
            if (cube.getSquare(face, 0, 0).equals(cube.getSquare(face,0,2))){
                solvedSide = true
                if (solvedFace != cube.getSideFace(false,frontFace,0)){
                    solvedFace = face
                }
            }
            else if(solvedSide){
                when(solvedFace){
                    frontFace -> algorithm += solverUtil.move(cube,false,"u",frontFace,faces.WHITE.ordinal)
                    cube.getOppositeFace(frontFace) -> algorithm += solverUtil.move(cube,true,"u",solvedFace,faces.WHITE.ordinal)
                    cube.getSideFace(true,frontFace,0) -> algorithm += solverUtil.move(cube,true,"u",solvedFace,faces.WHITE.ordinal) + solverUtil.move(cube,true,"u",solvedFace,faces.WHITE.ordinal)
                }

                //solved face is on the left
                val al: Array<String> = arrayOf("l'", "u", "r", "u'", "l","u","u", "r'", "u", "r", "u", "u", "r")

                for (move in al){
                    if (move.length > 1){algorithm += solverUtil.move(cube,true,move,frontFace,0) }
                    else{algorithm += solverUtil.move(cube,false,move,frontFace,0) }
                }

                break
            }
        }

        if (solverUtil.solvedCube(cube.getCube())){ return algorithm }

        for (face in 1.. 4){
            if (cube.getSquare(face,0,1).equals(cube.getSquare(face,0,0))){
                if (face.equals(cube.getOppositeFace(frontFace))){
                    break
                }
                when(face){
                    frontFace -> algorithm += solverUtil.move(cube,false,"u",face,0) + solverUtil.move(cube,false,"u",face,0)
                    cube.getSideFace(true,frontFace,0) -> algorithm += solverUtil.move(cube,true,"u",face,0)
                    else -> algorithm += solverUtil.move(cube,false,"u",face,0)
                }
                break
            }
            //add in code for opposite middles
            if (difficulty.equals(1) and face.equals(4)){
                if (cube.getCubeFaceSquare(face, 1).equals(cube.getCubeFaceSquare(cube.getOppositeFace(face), 1))){
                    val al: Array<String> = arrayOf("r","r","l","l","u","r","r","l","l","u","u","r","r","l","l","u","r","r","l","l")

                    for (move in al){
                        if (move.length > 1){algorithm += solverUtil.move(cube,true,move,frontFace,0) }
                        else{algorithm += solverUtil.move(cube,false,move,frontFace,0) }
                    }

                    return algorithm

                }
            }

        }

        var al: Array<String> = arrayOf("")
        when(cube.getSquare(frontFace,0,0)){
            solverUtil.getColour(cube.getSideFace(true,frontFace,0)) -> al = arrayOf("f","f","u'", "r'","l","f","f","l'","r","u'","f","f")
            else -> al = arrayOf("f","f","u", "r'","l","f","f","r","l'","u","f","f")
        }

        for (move in al){
            if (move.length > 1){algorithm += solverUtil.move(cube,true,move,frontFace,0) }
            else{algorithm += solverUtil.move(cube,false,move,frontFace,0) }
        }

        return algorithm
    }








}