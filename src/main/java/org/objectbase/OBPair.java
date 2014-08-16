package org.objectbase;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.objectbase.util.DateUtils;


public class OBPair extends AbstractSyntax implements Serializable{
    

    private static final long serialVersionUID = -732742411090773154L;
    private final String key;
    private final OBValue value;
    
    public OBPair(String key, OBValue value) {
        this.key = key;
        this.value = value;
    }

	public String getKey() {
		return key;
	}

	public OBValue getValue() {
		return value;
	}
    
   
    
}
