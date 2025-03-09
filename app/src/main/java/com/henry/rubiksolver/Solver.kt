package com.henry.rubiksolver

public open class Solver constructor(level: Int, frontFace: Int) {
    private val solverUtil: SolverUtil = SolverUtil()
    private val difficulty: Int = level
    private var frontFace: Int = frontFace

    public fun getDifficulty(): Int{
        return difficulty
    }

    public fun getAlgorithm(cube: Cube): Array<String>{
        var algorithm: Array<String> = arrayOf("")

        algorithm += getWhiteFace(cube)
        if (difficulty.equals(0)){ algorithm += getSecondLayer(cube)}
        algorithm += getYellowFace(cube)
        algorithm += finalSolve(cube)


        return algorithm
    }

    private fun getWhiteFace(cube: Cube): Array<String>{
        var algorithm: Array<String> = arrayOf("")

        when(difficulty){
            0 ->{



            }
        }


        return algorithm
    }

    private fun getSecondLayer(cube: Cube): Array<String>{
        var algorithm: Array<String> = arrayOf("")


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