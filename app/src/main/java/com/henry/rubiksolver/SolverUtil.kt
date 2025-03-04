package com.henry.rubiksolver

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

    public fun solvedCube(cubeFace: Array<CharArray?>): Boolean{
        for (face in cubeFace){
            if (!isFaceSolved(face!!)){return false}
        }
        return true
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
}