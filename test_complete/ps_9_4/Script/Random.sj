var MIN_RANDOM_VALUE=0;
var MAX_RANDOM_VALUE=65535;//unsigned short integer limit
//returns a random number between the min and max values
function randomInt(){ 
  return Math.round(Math.random()*(Random.MAX_RANDOM_VALUE-Random.MIN_RANDOM_VALUE)+Random.MIN_RANDOM_VALUE); //method Math.random() returns a random number between 0 and 1
} 