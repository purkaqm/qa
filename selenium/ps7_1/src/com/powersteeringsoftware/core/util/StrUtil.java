package com.powersteeringsoftware.core.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import org.apache.commons.lang.CharUtils;

public class StrUtil {

	private StrUtil() {
	}

	public static String quoteUnicode(CharSequence charSeq) {
		StringBuffer strBuffer = new StringBuffer("");
		for(int i=0;i<charSeq.length();i++){
			strBuffer.append(CharUtils.unicodeEscaped(charSeq.charAt(i)));
		}
		return strBuffer.toString();
	}


	public static String quoteReqExp(CharSequence aRegexFragment){
		final StringBuilder result = new StringBuilder();

	    StringCharacterIterator iterator = new StringCharacterIterator(String.valueOf(aRegexFragment));
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	      /*
	      * All literals need to have backslashes doubled.
	      */
	      if (character == '.') {
	        result.append("\\.");
	      }
	      else if (character == '\\') {
	        result.append("\\\\");
	      }
	      else if (character == '?') {
	        result.append("\\?");
	      }
	      else if (character == '*') {
	        result.append("\\*");
	      }
	      else if (character == '+') {
	        result.append("\\+");
	      }
	      else if (character == '&') {
	        result.append("\\&");
	      }
	      else if (character == ':') {
	        result.append("\\:");
	      }
	      else if (character == '{') {
	        result.append("\\{");
	      }
	      else if (character == '}') {
	        result.append("\\}");
	      }
	      else if (character == '[') {
	        result.append("\\[");
	      }
	      else if (character == ']') {
	        result.append("\\]");
	      }
	      else if (character == '(') {
	        result.append("\\(");
	      }
	      else if (character == ')') {
	        result.append("\\)");
	      }
	      else if (character == '^') {
	        result.append("\\^");
	      }
	      else if (character == '$') {
	        result.append("\\$");
	      }
	      else {
	        //the char is not a special one
	        //add it to the result as is
	        result.append(character);
	      }
	      character = iterator.next();
	    }
	    return result.toString();
	}
}
