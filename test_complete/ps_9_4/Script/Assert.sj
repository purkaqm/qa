
//do an assertion for boolean and numeric values; it log the result if positive or negative
function assertValue(expected, value, message){
  if(expected == value){
    Assert.logPassAssertion(expected, value, message);
  }else{
    Assert.logFailAssertion(expected, value, message);
  }
}

//do an assertion for boolean and numeric values; it log the result if positive or negative
function assertStringValue(expected, value, message, caseSensitive){
  if(!aqString.Compare(expected,value,caseSensitive)){
    Assert.logPassAssertion(expected, value, message);
  }else{
    Assert.logFailAssertion(expected, value, message);
  }
}
//log assert pass
function logPassAssertion(expected, value, message){
  Log.Checkpoint("Assertion passed. Expected value = '"+expected+"' equals to '"+value+"'. "+message, clOlive)
}
//log assert fail
function logFailAssertion(expected, value, message){
  Log.Warning("Assertion failed. Expected value = '"+expected+"' NOT equals to '"+value+"'. "+message, clRed)
}