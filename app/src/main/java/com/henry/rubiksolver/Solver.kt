package com.henry.rubiksolver

public open class Solver constructor(level: Int) {
    private val solverUtil: SolverUtil = SolverUtil()
    private val difficulty: Int = level

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

        return algorithm
    }








}