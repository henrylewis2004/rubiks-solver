<p align="center">
  <img src=".github/images/52525-9-rubik's-cube-image-png-file-hd.png" alt="Code-Geass-SRPG" width="111" height="128" />
</p>


<h1 align="center">rubiks-solver</h1>
<h3 align="center">An Android Rubiks-Cube Solver in Kotlin</h3>

<p align="center">
  <a href="#about">About</a> •
  <a href="#images">Images</a> •
  <a href="#details">Details</a> •
  <a href="#further-work">Further Work</a>
</p>


## About

rubiks-solver is an application written for Android in Kotlin using Android Studio that gives voiced commands to solve a Rubiks Cube after analysing 6 images showing each side of the cube.

This project was for my final year Computer Science dissertation at the University of Liverpool (2025), recieving a 85% mark. I had never used Kotlin or Android Studio prior to this project and it helped me to learn a lot.

### Algorithm Used

No specific algorithm was used (simply peak into [Solver.kt](app/src/main/java/com/henry/rubiksolver/Solver.kt) to see the consequences of that decision). I had decided against using third party libraries and as such I had to develop the algorithm myself. To this end I would randomise the cube and then solve the Cube describing the rational behind each decision. It is fair to say that the algorithm therefore uses a mixed group of agorithms (largely using [CFOP](https://ruwix.com/the-rubiks-cube/how-to-solve-the-rubiks-cube-beginners-method/) with some more complex additional moves). All that being said, whilst the algorithm is capable of solving the cube (I went through many bugfixes!!), it does not do so efficiently taking far more moves than alternate algorithms would, although potentially less than the beginners method.

### Dissertation Paper
[Click to see the dissertation paper]()

> Note
> I did forget to write the acknowledgements page due to last minute submission :(

## Images

## Details

## Further Work

## License
