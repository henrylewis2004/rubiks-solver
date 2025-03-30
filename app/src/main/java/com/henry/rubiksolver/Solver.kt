package com.henry.rubiksolver

import android.util.Log
import kotlin.math.abs

public open class Solver constructor( ){
    private val solverUtil: SolverUtil = SolverUtil()
    private var frontFace: Int = faces.WHITE.ordinal
    private var bottomFace: Int = faces.GREEN.ordinal
    private val utilFul: Util = Util()


    public fun getAlgorithm(cubeFaces: Array<CharArray>): Array<String>{
        var algorithm: Array<String> = arrayOf()
        val solveCube: Cube = Cube()
        solveCube.setCubeFaces(cubeFaces)

        algorithm += getWhiteFace(solveCube)

        algorithm += getSecondLayer(solveCube)
        /*
        algorithm += getYellowFace(solveCube)

         */
      //  algorithm += finalSolve(solveCube)



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
        val cornerPos: IntArray = intArrayOf(0,2,6,8)
        val sidePos: IntArray = intArrayOf(3,1,5,7)
        frontFace = faces.WHITE.ordinal
        bottomFace = faces.GREEN.ordinal

        while (!solverUtil.isSecondLayerSolved(cube)) {
            for (face in 0..5) {
                when(face){
                    0 -> bottomFace = faces.GREEN.ordinal
                    5 -> bottomFace = faces.GREEN.ordinal
                    else -> bottomFace = faces.WHITE.ordinal
                }
                val cubeFace: CharArray = cube.getCubeFace(face)
                for (squareId in cornerPos) {
                    if (cubeFace[squareId] == 'w') {
                        val sideCol: CharArray = cube.getCornerSideColours(face,bottomFace,squareId)
                        val targetSquare: IntArray = cube.getCornerSide(sideCol)

                        if (face == faces.WHITE.ordinal && !solverUtil.sideCorrect(cube,squareId,face,bottomFace)){
                            if (targetSquare[0] == faces.YELLOW.ordinal) {
                                if (targetSquare[1] == 3 || targetSquare[1] == 5) {
                                    var al: Array<String> = arrayOf()
                                    val sideFace: Int = cube.getSideFace(squareId == 0 || squareId == 6,face,bottomFace)

                                    algorithm += "rot_To_${solverUtil.getColour(sideFace)}"
                                    bottomFace = frontFace
                                    frontFace = sideFace

                                    if (squareId == 2 || squareId == 6){
                                        al += arrayOf("l'","u'","l")
                                        var squareId = 0
                                        if (cube.getSquare(frontFace,bottomFace,squareId) != solverUtil.getColour(frontFace)){
                                            val distance: Int = solverUtil.distance(frontFace, solverUtil.getFaceFromColour(cube.getSquare(frontFace,bottomFace,squareId)))
                                            for (cnt in 1..distance){
                                                if (distance == 3 || distance == 1){
                                                    algorithm += solverUtil.move(cube,distance == 1, "u",frontFace,bottomFace)
                                                    break
                                                }
                                                algorithm += solverUtil.move(cube,false, "u",frontFace,bottomFace)

                                                //work here
                                            }

                                        }
                                    }
                                    else{
                                        al += arrayOf("r","u","r'")
                                    }

                                }
                            }
                        }

                    }

                }

            }
        }



        return algorithm
    }

    private fun getYellowFace(cube: Cube): Array<String>{
        var algorithm: Array<String> = arrayOf("")


        return algorithm

    }

    private fun finalSolve(cube: Cube): Array<String>{
        if (solverUtil.solvedCube(cube.getCube())){ return arrayOf() }

        var algorithm: Array<String> = arrayOf()
        if (frontFace == 0 || frontFace == 5) {
            frontFace = 1
        }
        var solvedFace: Int = 0

        for (face in 1..4) {
            if (cube.getSquare(face, faces.WHITE.ordinal, 0) == cube.getSquare(face, faces.WHITE.ordinal, 2)) {
                if (cube.getSquare(cube.getOppositeFace(face), faces.WHITE.ordinal, 0) != cube.getSquare(cube.getOppositeFace(face), faces.WHITE.ordinal, 2)) {
                    when (face) {
                        frontFace -> algorithm += solverUtil.move(cube, false, "u", frontFace, faces.WHITE.ordinal)
                        cube.getOppositeFace(frontFace) -> algorithm += solverUtil.move(cube, true, "u", frontFace, faces.WHITE.ordinal)
                        cube.getSideFace(true, frontFace, faces.WHITE.ordinal) -> algorithm += solverUtil.move(cube, false, "u", frontFace, faces.WHITE.ordinal) + solverUtil.move(cube, false, "u", frontFace, faces.WHITE.ordinal)
                    }
                }
                else {
                    for (targetFace in 1..4) {
                        if (cube.getSquare(targetFace, faces.WHITE.ordinal, 1) == cube.getSquare(targetFace, faces.WHITE.ordinal, 0)) {
                            var al: Array<String> = arrayOf()
                            when (targetFace) {

                                frontFace -> algorithm += solverUtil.move(cube, false, "u", frontFace, faces.WHITE.ordinal) + solverUtil.move(cube, false, "u", frontFace, faces.WHITE.ordinal)
                                cube.getSideFace(true, frontFace, faces.WHITE.ordinal) -> algorithm += solverUtil.move(cube, true, "u", frontFace, faces.WHITE.ordinal)
                                cube.getSideFace(false, frontFace, faces.WHITE.ordinal) -> algorithm += solverUtil.move(cube, false, "u", frontFace, faces.WHITE.ordinal)
                                else -> { //on back
                                    when (cube.getSquare(frontFace, faces.WHITE.ordinal, 1)) {
                                        cube.getSquare(cube.getSideFace(true, frontFace, faces.WHITE.ordinal), faces.WHITE.ordinal, 0) -> {
                                            al = arrayOf("f", "f", "u'", "r'", "l", "f", "f", "l'", "r", "u'", "f", "f")


                                        }

                                        cube.getSquare(cube.getSideFace(false, frontFace, faces.WHITE.ordinal), faces.WHITE.ordinal, 0) -> {
                                            al = arrayOf("f", "f", "u", "r'", "l", "f", "f", "l'", "r", "u", "f", "f")
                                        }
                                    }
                                }
                            }
                            for (move in al) {
                                algorithm += solverUtil.move(cube, move.length > 2, move[0].toString(), frontFace, faces.WHITE.ordinal)
                            }
                            if (solverUtil.solvedCube(cube.getCube())){return algorithm}
                            break
                        }
                    }


                }
                val al: Array<String> = arrayOf("l'", "u", "r", "u'", "l", "u", "u", "r'", "u", "r", "u", "u", "r")

                for (move in al) {
                    algorithm += solverUtil.move(cube, move.length > 1, move[0].toString(), frontFace, faces.WHITE.ordinal)
                }
                break
            }
        }

        if (solverUtil.solvedCube(cube.getCube())){ return algorithm }

        Log.d("finalSolve", "h2ere")
        for (face in 1.. 4){
            if (cube.getCubeFaceSquare(face, 1) == cube.getCubeFaceSquare(cube.getOppositeFace(face), 1)){
                Log.d("finalSolve", "h3ere")
                val al: Array<String> = arrayOf("r","r","l","l","u","r","r","l","l","u","u","r","r","l","l","u","r","r","l","l")

                for (move in al){
                    if (move.length > 1){algorithm += solverUtil.move(cube,true,move,frontFace,faces.WHITE.ordinal) }
                    else{algorithm += solverUtil.move(cube,false,move,frontFace,faces.WHITE.ordinal) }
                }

                return algorithm

            }

            if (cube.getSquare(face,0,1) == cube.getSquare(face,0,0)){
                Log.d("finalSolve", "here4")
                when(face){
                    frontFace -> algorithm += solverUtil.move(cube,false,"u",face,faces.WHITE.ordinal) + solverUtil.move(cube,false,"u",face,faces.WHITE.ordinal)
                    cube.getSideFace(true,frontFace,faces.WHITE.ordinal) -> algorithm += solverUtil.move(cube,true,"u",face,faces.WHITE.ordinal)
                    cube.getOppositeFace(frontFace) -> break
                    else -> algorithm += solverUtil.move(cube,false,"u",face,faces.WHITE.ordinal)
                }
                break
            }
        }

        Log.d("finalSolve", "here6")
        var al: Array<String> = arrayOf("")
        when(cube.getSquare(frontFace,faces.WHITE.ordinal,0)){
            solverUtil.getColour(cube.getSideFace(true,frontFace,0)) -> al = arrayOf("f","f","u'", "r'","l","f","f","l'","r","u'","f","f")
            else -> al = arrayOf("f","f","u", "r'","l","f","f","r","l'","u","f","f")
        }

        for (move in al) {
            algorithm += solverUtil.move(cube, move.length > 1, move[0].toString(), frontFace, 0)
        }

        Log.d("finalSolve", "here7")
        return algorithm
    }








}