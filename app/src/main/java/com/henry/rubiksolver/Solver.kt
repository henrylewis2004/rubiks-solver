package com.henry.rubiksolver

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.text.buildAnnotatedString
import kotlin.concurrent.fixedRateTimer
import kotlin.math.abs

public open class Solver {
    private val solverUtil: SolverUtil = SolverUtil()
    private var frontFace: Int = faces.WHITE.ordinal
    private var bottomFace: Int = faces.GREEN.ordinal
    private val utilFul: Util = Util()


    public fun getAlgorithm(cubeFaces: Array<CharArray>): Array<String>{
        var algorithm: Array<String> = arrayOf()
        val solveCube: Cube = Cube()
        solveCube.setCubeFaces(cubeFaces)

        Log.d("cubesolve","herex")
        algorithm += getWhiteFace(solveCube)
        Log.d("cubesolve","here1")
        algorithm += getSecondLayer(solveCube)
        Log.d("cubesolve","here2")
        algorithm += getYellowFace(solveCube)
        Log.d("cubesolve","here3")
        algorithm += finalSolve(solveCube)
        Log.d("cubesolve","here4")


        return algorithm
    }

    private fun getWhiteFace(cube: Cube): Array<String> {
        var algorithm: Array<String> = arrayOf()
        val crossSquares: IntArray = intArrayOf(3, 1, 5, 7)
        var cubeFace: Array<CharArray> = cube.getCube()
        frontFace = faces.WHITE.ordinal
        bottomFace = faces.GREEN.ordinal

        while (!solverUtil.isCrossSolved(cube, faces.WHITE.ordinal, faces.GREEN.ordinal)) { //white cross
            bottomFace = faces.WHITE.ordinal
            for (face in 1..4) {
                for (squareId in crossSquares) {
                    if (cube.getSquare(face, bottomFace, squareId) == 'w') {
                        algorithm += "rot_To_${solverUtil.getColour(face)}"
                        //move onto white face
                        when (squareId) {
                            3 -> {
                                while (cube.getSquare(bottomFace, cube.getOppositeFace(face), squareId) == 'w') {
                                    algorithm += solverUtil.move(cube, false, "d", face, bottomFace)
                                }
                                algorithm += solverUtil.move(cube, false, "l", face, bottomFace)
                            }

                            5 -> {
                                while (cube.getSquare(bottomFace, cube.getOppositeFace(face), squareId) == 'w') {
                                    algorithm += solverUtil.move(cube, false, "d", face, bottomFace)
                                }
                                algorithm += solverUtil.move(cube, true, "r", face, bottomFace)
                            }

                            1 -> {
                                if (cube.getSquare(bottomFace, cube.getOppositeFace(face), 1) == 'w') {
                                    if (cube.getSquare(bottomFace, cube.getOppositeFace(face), 3) != 'w') {
                                        algorithm += solverUtil.move(cube, true, "d", face, bottomFace) }

                                    else {
                                        while (cube.getSquare(bottomFace, cube.getOppositeFace(face), 1) == 'w') {
                                            algorithm += solverUtil.move(cube, false, "d", face, bottomFace)
                                        }
                                    }

                                }
                                algorithm += solverUtil.move(cube, true, "f", face, bottomFace)
                                while (cube.getSquare(bottomFace, cube.getOppositeFace(face), 5) == 'w') {
                                    algorithm += solverUtil.move(cube, false, "d", face, bottomFace)
                                }
                                algorithm += solverUtil.move(cube, true, "r", face, bottomFace)
                            }

                            7 -> {
                                while (cube.getSquare(bottomFace, cube.getOppositeFace(face), 1) == 'w') {

                                    algorithm += solverUtil.move(cube, false, "d", face, bottomFace)
                                }
                                algorithm += solverUtil.move(cube, true, "f", face, bottomFace)

                                while (cube.getSquare(bottomFace, cube.getOppositeFace(face), 5) == 'w') {
                                    algorithm += solverUtil.move(cube, false, "d", face, bottomFace)
                                }
                                algorithm += solverUtil.move(cube, true, "r", face, bottomFace)
                            }
                        }
                    }

                }
            }


            for (squareId in crossSquares) {
                val face = faces.YELLOW.ordinal
                val bFace = faces.GREEN.ordinal
                if (cube.getSquare(face, bFace, squareId) == 'w') {
                    when (squareId) {
                        1 -> frontFace = cube.getOppositeFace(bFace)
                        7 -> frontFace = bFace
                        else -> frontFace = cube.getSideFace(squareId == 5, face, bFace)
                    }
                    algorithm += "rot_To_${solverUtil.getColour(frontFace)}"

                    while (cube.getSquare(bottomFace,cube.getOppositeFace(frontFace),1) == 'w') {
                        algorithm += solverUtil.move(cube, false, "d", frontFace, faces.WHITE.ordinal)
                    }

                    algorithm += solverUtil.move(cube, false, "f", frontFace, faces.WHITE.ordinal)
                    algorithm += solverUtil.move(cube, false, "f", frontFace, faces.WHITE.ordinal)

                }
            }
            //position white correctly
            var squareCnt: Int = 0
            for (square in crossSquares){
                if (cube.getCubeFaceSquare(faces.WHITE.ordinal,square) == 'w'){
                    squareCnt++
                }
            }

            if(squareCnt == 4){
                frontFace = faces.WHITE.ordinal
                bottomFace = faces.GREEN.ordinal
                algorithm += "rot_To_${solverUtil.getColour(frontFace)}"

                for (squareId in crossSquares.indices) {
                val square = crossSquares[squareId]

                if (cube.getSquare(frontFace, bottomFace, square) == 'w') {
                    val sideFaceColour: Char =
                        cube.getSideFaceColour(square, faces.WHITE.ordinal, faces.GREEN.ordinal)
                    val sideColour: Char =
                        cube.getSideColour(square, faces.WHITE.ordinal, faces.GREEN.ordinal)

                    if (sideColour != sideFaceColour) {
                        val whiteSqCnt =
                            solverUtil.correctCrossCount(faces.WHITE.ordinal, bottomFace, cube)

                        if (whiteSqCnt == 0) { //no correct squares yet
                            var nextSquare: Int = squareId - 1
                            if (nextSquare < 0) {
                                nextSquare = 3
                            }

                            if (cube.getSideFaceColour(crossSquares[nextSquare], faces.WHITE.ordinal, bottomFace) == sideColour) {
                                algorithm += solverUtil.move(cube, true, "f", faces.WHITE.ordinal, bottomFace)
                            }
                            else { algorithm += solverUtil.move(cube, false, "f", faces.WHITE.ordinal, bottomFace)

                                if (sideColour != cube.getSideFaceColour(crossSquares[squareId + 1 - (4 * utilFul.booleanToInt(squareId == 3))], faces.WHITE.ordinal, bottomFace)) {
                                    algorithm += solverUtil.move(cube, false, "f", faces.WHITE.ordinal, bottomFace)
                                }
                            }
                        } else {
                            val targetSquare: Int = solverUtil.getWhiteTargetCrossSquare(sideColour)
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
                                algorithm += solverUtil.move(
                                    cube,
                                    move.length > 1,
                                    move[0].toString(),
                                    faces.WHITE.ordinal,
                                    bottomFace
                                )
                            }
                        }
                    }
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

        while (!solverUtil.isSecondLayerSolved(cube)) {
            for (face in 1..4){
                bottomFace = faces.WHITE.ordinal
                var newSquareLoc: Int
                for (squareId in cornerPos){
                    if (cube.getSquare(face,bottomFace, squareId) == 'w'){
                        frontFace = cube.getSideFace(squareId == 8 || squareId == 2, face, bottomFace)
                        var al: Array<String> = arrayOf()
                        when (squareId){
                            8 -> {
                                algorithm += "rot_To_${solverUtil.getColour(face)}"
                                al = arrayOf("f'","u'","f","u")
                                newSquareLoc = 2
                            }

                            6 ->{
                                algorithm += "rot_To_${solverUtil.getColour(face)}"
                                al = arrayOf("f","u","f'","u'")
                                newSquareLoc = 0
                            }
                            else -> newSquareLoc = squareId
                        }

                        for (move in al){
                            algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),face,bottomFace)
                        }
                        al = arrayOf()
                        algorithm += "rot_To_${solverUtil.getColour(frontFace)}"
                        //all corners are in top layer
                        when(cube.getSquare(frontFace,bottomFace,2 - 2 * utilFul.booleanToInt(newSquareLoc == 2))){
                            solverUtil.getColour(frontFace) -> {}//do nothing
                            solverUtil.getColour(cube.getSideFace(true, frontFace,bottomFace)) -> {
                                algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)
                                frontFace = cube.getSideFace(true, frontFace,bottomFace)
                                algorithm += "rot_To_${solverUtil.getColour(frontFace)}"

                            }
                            else ->{
                                algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                val sideFace: Int = cube.getSideFace(false, frontFace,bottomFace)

                                if (cube.getSquare(sideFace,bottomFace,2 - 2 * utilFul.booleanToInt(newSquareLoc == 2)) != solverUtil.getColour(sideFace)){
                                    algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                    frontFace = cube.getOppositeFace(frontFace)
                                }
                                else{frontFace = sideFace}
                                algorithm += "rot_To_${solverUtil.getColour(frontFace)}"
                            }
                        }

                        val topSquare:Int
                        when (newSquareLoc){
                            0 -> topSquare = 8
                            else -> topSquare = 6
                        }

                        if (cube.getSquare(frontFace,bottomFace,2 - 2* utilFul.booleanToInt(newSquareLoc == 2)) == cube.getSquare(frontFace,bottomFace,1) && cube.getSquare(cube.getOppositeFace(bottomFace),frontFace,topSquare) == cube.getSquare(cube.getOppositeFace(bottomFace),frontFace,7)){
                            when(newSquareLoc){
                                0 -> al = arrayOf("u'", "f'","u","f")
                                2 -> al = arrayOf("u","f","u'","f'")
                            }
                        }
                        else {
                            when(newSquareLoc){
                                0 -> al = arrayOf("r", "u", "r'", "u'")
                                2 -> al = arrayOf("l'", "u'", "l", "u")
                            }
                        }

                        for (move in al){
                            algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
                        }

                    }
                }
            }
            for (squareId in cornerPos) { //yellowFace
                val square = cube.getSquare(faces.YELLOW.ordinal, faces.GREEN.ordinal,squareId)

                if ( square == 'w'){
                    if (frontFace != cube.getSideFace(squareId == 2 || squareId == 8, faces.YELLOW.ordinal,faces.GREEN.ordinal)) {algorithm+= "rot_To_${solverUtil.getColour(cube.getSideFace(squareId == 2 || squareId == 8, faces.YELLOW.ordinal,faces.GREEN.ordinal))}"}
                    frontFace = cube.getSideFace(squareId == 2 || squareId == 8, faces.YELLOW.ordinal,faces.GREEN.ordinal)
                    bottomFace = faces.WHITE.ordinal
                    var newSquareLoc: Int
                    when (squareId){
                        6 -> newSquareLoc = 2
                        8 -> newSquareLoc = 0
                        else -> newSquareLoc = squareId
                    }

                    when(cube.getSquare(frontFace,bottomFace,newSquareLoc)){
                        solverUtil.getColour(frontFace) -> {
                            when(newSquareLoc) {
                                0 -> algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)
                                2 -> {
                                    algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                    frontFace = cube.getSideFace(false,frontFace,bottomFace)
                                    algorithm += "rot_To_${solverUtil.getColour(frontFace)}"
                                }
                            }

                        }
                        solverUtil.getColour(cube.getSideFace(true, frontFace,bottomFace)) -> {
                            if (newSquareLoc == 0){
                                algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)

                                if (frontFace != cube.getSideFace(true, frontFace,bottomFace)) {
                                    frontFace = cube.getSideFace(true, frontFace,bottomFace)
                                    algorithm += "rot_To_${solverUtil.getColour(frontFace)}"
                                }
                            }
                        }
                        solverUtil.getColour(cube.getSideFace(false, frontFace,bottomFace)) -> {
                            if (newSquareLoc == 2){
                                algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)

                                if (frontFace != cube.getSideFace(false, frontFace,bottomFace)){
                                    frontFace = cube.getSideFace(false, frontFace,bottomFace)
                                    algorithm += "rot_To_${solverUtil.getColour(frontFace)}"
                                }
                            }

                        }
                        solverUtil.getColour(cube.getOppositeFace(frontFace)) -> {
                            when (newSquareLoc) {
                                0 -> {
                                    algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                    frontFace = cube.getOppositeFace(frontFace)
                                    algorithm += "rot_To_${solverUtil.getColour(frontFace)}"
                                }
                                2 -> {
                                    algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)
                                    frontFace = cube.getSideFace(true,frontFace,bottomFace)
                                    algorithm += "rot_To_${solverUtil.getColour(frontFace)}"
                                }

                            }


                        }

                    }

                    var al: Array<String> = arrayOf()
                    when(newSquareLoc){
                        0 -> al = arrayOf("l'","u'","u'","l","u","l'","u'","l")
                        2 -> al = arrayOf("r","u","u","r'","u'","r","u","r'")
                    }

                    for (move in al){
                        algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
                    }


                }

            }
            for (corner in cornerPos){ //white face
                val square: Char = cube.getSquare(faces.WHITE.ordinal,faces.GREEN.ordinal,corner)

                if (square == 'w'){
                    val face: Int = cube.getSideFace(corner == 2 || corner == 8, faces.WHITE.ordinal,faces.GREEN.ordinal)
                    bottomFace = faces.WHITE.ordinal
                    val sideCorner: Int
                    when(corner){
                        0 -> sideCorner = 8
                        2 -> sideCorner = 6
                        else -> sideCorner = corner
                    }

                    if(!solverUtil.cornerSquareCorrect(cube,face,bottomFace,sideCorner)) {
                        if (frontFace !=face){
                            algorithm += "rot_To_${solverUtil.getColour(face)}"
                            frontFace = face
                        }
                        var newCornerLoc: Int
                        when(corner){
                            0 -> newCornerLoc = 8
                            2 -> newCornerLoc = 6
                            else -> newCornerLoc = corner
                        }

                        var al: Array<String> = arrayOf()
                        if (newCornerLoc == 8){
                            al = arrayOf("r","u","r'")
                            newCornerLoc = 0
                        }
                        else{
                            al = arrayOf("l'","u'","l")
                            newCornerLoc = 2
                        }

                        for (move in al){
                            algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
                        }
                        al = arrayOf()

                        if(cube.getSquare(frontFace,bottomFace,newCornerLoc) != solverUtil.getColour(frontFace)){
                            when(cube.getSquare(frontFace,bottomFace,newCornerLoc)){
                                solverUtil.getColour(frontFace) -> {}//do nothing
                                solverUtil.getColour(cube.getSideFace(true, frontFace,bottomFace)) -> {
                                    algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)
                                    frontFace = cube.getSideFace(true, frontFace,bottomFace)
                                    algorithm += "rot_To_${solverUtil.getColour(frontFace)}"

                                }
                                else ->{
                                    algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                    val sideFace: Int = cube.getSideFace(false, frontFace,bottomFace)

                                    if (cube.getSquare(sideFace,bottomFace,newCornerLoc) != solverUtil.getColour(sideFace)){
                                        algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                        frontFace = cube.getOppositeFace(frontFace)
                                    }
                                    else{frontFace = sideFace}
                                    algorithm += "rot_To_${solverUtil.getColour(frontFace)}"
                                }
                            }



                        }
                        val topSquare: Int
                        when(newCornerLoc){
                            0 -> topSquare = 6
                            else -> topSquare = 8
                        }


                        if (cube.getSquare(frontFace,bottomFace,newCornerLoc) == cube.getSquare(frontFace,bottomFace,1) && cube.getSquare(cube.getOppositeFace(bottomFace),frontFace,topSquare) == cube.getSquare(cube.getOppositeFace(bottomFace),frontFace,7)){
                            when(newCornerLoc){
                                2 -> al = arrayOf("u'", "f'","u","f")
                                0 -> al = arrayOf("u","f","u'","f'")
                            }
                        }
                        else {
                            when(newCornerLoc){
                                0 -> al = arrayOf("l'", "u'", "l", "u")
                                2 -> al = arrayOf("r", "u", "r'", "u'")
                            }
                        }

                        for (move in al){
                            algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
                        }

                    }

                }



            }

            if (solverUtil.isFaceSolved(cube.getCubeFace(faces.WHITE.ordinal))) {

                for(squareId in cornerPos){
                    bottomFace = faces.WHITE.ordinal
                    val face: Int = cube.getSideFace(squareId == 2 || squareId == 8, faces.WHITE.ordinal,faces.GREEN.ordinal)
                    val cornerLoc: Int
                    when(squareId){
                        0 -> cornerLoc = 8
                        2 -> cornerLoc = 6
                        else -> cornerLoc = squareId
                    }

                    if(!solverUtil.cornerCorrect(cube,face,bottomFace,cornerLoc)) {
                        var targetSquare: IntArray = cube.getCornerSide(cube.getCornerSideColours(faces.WHITE.ordinal,faces.GREEN.ordinal,squareId))

                        if (targetSquare[1]  != 1){
                            frontFace = targetSquare[0]
                            algorithm += "rot_To_${solverUtil.getColour(frontFace)}"

                            var al: Array<String> = arrayOf()

                            when (targetSquare[1]){
                                3 -> {
                                    al = arrayOf("l'", "u'", "l","u","f","u","f'")
                                }
                                5 ->{
                                    al = arrayOf("r","u","r'","u'","f'","u'","f")
                                }
                            }
                            targetSquare = intArrayOf(cube.getOppositeFace(frontFace),1)

                            for (move in al){
                                algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
                            }

                        }

                        val topColour: Char = cube.getSquare(faces.YELLOW.ordinal,targetSquare[0],7)
                        if (targetSquare[0] != cube.getOppositeFace(solverUtil.getFaceFromColour(topColour))){
                            if (topColour == cube.getSideFaceColour(3,targetSquare[0],bottomFace)){
                                algorithm += solverUtil.move(cube,true,"u",targetSquare[0],bottomFace)
                                targetSquare[0] = cube.getSideFace(true,targetSquare[0],bottomFace)
                            }
                            else{
                                while(targetSquare[0] != cube.getOppositeFace(solverUtil.getFaceFromColour(topColour))){
                                    algorithm += solverUtil.move(cube,false,"u",targetSquare[0],bottomFace)
                                    targetSquare[0] = cube.getSideFace(false,targetSquare[0],bottomFace)
                                }
                            }
                        }

                        frontFace = solverUtil.getFaceFromColour(topColour)
                        algorithm += "rot_To_${solverUtil.getColour(frontFace)}"

                        var al: Array<String> = arrayOf()

                        if(topColour == cube.getSquare(frontFace,bottomFace,6) && cube.getSquare(cube.getOppositeFace(frontFace),bottomFace,1) == cube.getSquare(cube.getSideFace(false,frontFace,bottomFace),bottomFace,8)){
                            al = arrayOf("f","u","f'","u'","l'","u'","l")
                        }
                        else{
                            al = arrayOf("f'","u'","f","u","r","u","r'")
                        }


                        for (move in al){
                            algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
                        }
                    }
                }

            }
        }



        return algorithm
    }


    private fun getYellowCross(cube:Cube): Array<String>{
        val sidePos: IntArray = intArrayOf(3,1,5,7)
        var algorithm: Array<String> = arrayOf()

        for (square in sidePos){ //yellow cross already there
            if (cube.getSquare(faces.YELLOW.ordinal,faces.GREEN.ordinal,square) != 'y'){
                break
            }
            if (square ==7) {return algorithm}
        }

        //horizontal yellow line
        if (bottomFace != faces.WHITE.ordinal){
            frontFace = faces.BLUE.ordinal
            algorithm += "rot_To_${solverUtil.getColour(frontFace)}"
        }
        bottomFace = faces.WHITE.ordinal


        if ((cube.getSquare(faces.YELLOW.ordinal,frontFace,5) == cube.getSquare(faces.YELLOW.ordinal,frontFace,3) && cube.getSquare(faces.YELLOW.ordinal,frontFace,3) == 'y') || (cube.getSquare(faces.YELLOW.ordinal,frontFace,1) == cube.getSquare(faces.YELLOW.ordinal,frontFace,7) && cube.getSquare(faces.YELLOW.ordinal,frontFace,7) == 'y')){
            if (cube.getSquare(faces.YELLOW.ordinal,frontFace,1)  == cube.getSquare(faces.YELLOW.ordinal,frontFace,7) && cube.getSquare(faces.YELLOW.ordinal,frontFace,1) == 'y'){
                algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
            }
            val al: Array<String> = arrayOf("f","r","u","r'","u'","f'")

            for (move in al){
                algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
            }

            return getYellowCross(cube)
        }

        //L pattern

        for (square in sidePos){
            var nextSquare: Int = square + 1
            if (nextSquare > 3){nextSquare = 0}

            if (cube.getSquare(faces.YELLOW.ordinal,frontFace,square) == cube.getSquare(faces.YELLOW.ordinal,frontFace,nextSquare) && cube.getSquare(faces.YELLOW.ordinal,frontFace,square) == 'y'){
                if (cube.getSquare(faces.YELLOW.ordinal,frontFace,7) == 'y'){
                    if(cube.getSquare(faces.YELLOW.ordinal,frontFace,3) == 'y'){
                        algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                    }
                    else if(cube.getSquare(faces.YELLOW.ordinal,frontFace,5) == 'y'){
                        algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                        algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                    }
                }

                val al: Array<String> = arrayOf("f","r","u","r'","u'","f'")

                for (move in al){
                    algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
                }

                return getYellowCross(cube)
            }
        }

    //dot
    val al: Array<String> = arrayOf("f","r","u","r'","u'","f'")

    for (move in al){
        algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
    }
    return getYellowCross(cube)

    }

    private fun getYellowFace(cube: Cube): Array<String>{
        var algorithm: Array<String> = getYellowCross(cube)
        val basicAl: Array<String> = arrayOf("r","u","r'","u","r","u","u","r'")
        val cornerPiece: IntArray = intArrayOf(0,2,6,8)
        if (solverUtil.isFaceSolved(cube.getCubeFace(faces.YELLOW.ordinal))){return algorithm}

        var cornerUp:Int = 0
        var cornerCnt: Int = 0
        for (corner in cornerPiece){
            if (cube.getSquare(faces.YELLOW.ordinal,frontFace,corner) == 'y'){
                cornerCnt ++
                cornerUp = corner
            }
        }
        if (cornerCnt != 1){
            for(face in 1..4){
                if (cube.getSquare(face,bottomFace,2) == 'y'){
                    when(face){
                        frontFace ->{
                            algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                        }
                        cube.getOppositeFace(frontFace) -> {
                            algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)

                        }
                        cube.getSideFace(true,frontFace,bottomFace) ->{
                            algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                            algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)

                        }
                    }
                    break
                }
            }


            for (move in basicAl){
                algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
            }
            return getYellowFace(cube)
        }

        val cornerFacing: Boolean
        when(cornerUp){
            0 -> {
                if (cube.getSquare(cube.getSideFace(false,frontFace,bottomFace),bottomFace,2) == 'y') {
                    algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)
                }
                else{
                    algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                }
            }
            2 -> {
               if (cube.getSquare(cube.getOppositeFace(frontFace),bottomFace,2) == 'y'){
                   algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                   algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
               }
            }
            6 -> {
                if (cube.getSquare(frontFace,bottomFace,2) != 'y'){
                    algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                    algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                }
            }
            else -> {
                if (cube.getSquare(cube.getSideFace(true,frontFace,bottomFace),bottomFace,2) == 'y'){
                    algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                }
                else{
                    algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)

                }
            }
        }

        val al: Array<String>
        if (cube.getSquare(frontFace,bottomFace,2) == 'y'){
            al = basicAl
        }
        else{
            al = arrayOf("r","u","u","r'","u'","r","u'","r'")
        }

        for (move in al){
            algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
        }

        return algorithm
    }

    private fun finalSolve(cube: Cube): Array<String>{
        val leftSolve: Array<String> = arrayOf("l'","u","r","u'","l","u","u", "r'", "u", "r","u","u","r'")
        var algorithm: Array<String> = arrayOf()
        if (solverUtil.solvedCube(cube.getCube())){ return algorithm }

        if (bottomFace != faces.WHITE.ordinal){
            frontFace = faces.BLUE.ordinal
            algorithm += "rot_To_${solverUtil.getColour(frontFace)}"
        }
        bottomFace = faces.WHITE.ordinal

        var solvedFaces: IntArray = intArrayOf()
        for (face in 1..4){
            if (cube.getSquare(face,bottomFace,0) == cube.getSquare(face,bottomFace,2)){
                solvedFaces += face
            }

        }

        when(solvedFaces.size){
            0 -> {
                for (move in leftSolve){
                    algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
                }

                return finalSolve(cube)
            }

            1 -> {
                when(solvedFaces[0]){
                    cube.getSideFace(false,frontFace,bottomFace)->{} //do nothing

                    frontFace ->{
                        algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                    }

                    cube.getOppositeFace(frontFace)->{
                        algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)

                    }

                    else -> {
                        algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)
                        algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)
                    }

                }

                for (move in leftSolve){
                    algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
                }
                return finalSolve(cube)
            }
            else -> {
                val al: Array<String>

                for (face in 1..4){
                    if (cube.getSquare(face,bottomFace,1) == cube.getSquare(face,bottomFace,0)){
                        if (face == 1){
                            val nextFace: Int = 2
                            if(cube.getSquare(nextFace,bottomFace,1) == cube.getSquare(nextFace,bottomFace,0)){
                                when(solverUtil.getFaceFromColour(cube.getSquare(face,bottomFace,1))){
                                    cube.getOppositeFace(frontFace) ->{
                                        algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                        algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                    }
                                    cube.getSideFace(false,frontFace,bottomFace) -> {
                                        algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                    }
                                    else ->{
                                        algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)

                                    }
                                }
                                return algorithm

                            }
                        }


                        when(face){
                            frontFace -> {
                                algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                                algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                            }
                            cube.getSideFace(false,frontFace,bottomFace) -> {
                                algorithm += solverUtil.move(cube,false,"u",frontFace,bottomFace)
                            }
                            cube.getSideFace(true,frontFace,bottomFace) -> {
                                algorithm += solverUtil.move(cube,true,"u",frontFace,bottomFace)
                            }
                        }
                        break
                    }
                }
                al = arrayOf("f","f","u'","r'","l","f","f","l'","r","u'","f","f")

                for (move in al){
                    algorithm += solverUtil.move(cube,move.length>1,move[0].toString(),frontFace,bottomFace)
                }
                return finalSolve(cube)
            }
        }
    }








}