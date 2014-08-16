package org.objectbase;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.objectbase.util.DateUtils;


public class OBValue extends AbstractSyntax implements Serializable{

    private static DecimalFormat doubleFormatter = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ROOT));
    static { doubleFormatter.setMaximumFractionDigits(15); }
    

    private static final long serialVersionUID = -732742411090773154L;
    private final OBType type;
    private final Object value;
    
    public OBValue(OBType type, Object value) {
        this.type = type;
        this.value = value;
    }
    
    public OBType getType() {
        return type;
    }
    
    public Object getValue() {
        return value;
    }
    
    public boolean asBoolean() {
       
        // If the value is null, it becomes false.
        if (isNull()) return false;
        
        if (type == OBType.BOOLEAN) {
            return ((Boolean)value).booleanValue();
        }
        
        // If the value is an integer, zero == false, all else == true
        if (type == OBType.INTEGER) {
            return ((Integer)value).intValue() != 0;
        }
        
        // If the value is an double, zero == false, all else == true
        if (type == OBType.DOUBLE) {
            return ((Number)value).doubleValue() != 0.0;
        }
        
        // Otherwise, non-null equals true;
        return true;
    }
    
    public String asString() {
        if (type == OBType.STRING) {
            return (String)value;
        }
        
        if (isNull()) {
            return null;
        }
        else {
            if (type == OBType.DATE) {
                return DateUtils.formatDate((Date)value);
            }
            else if (type == OBType.BOOLEAN) {
                return ((Boolean)value).booleanValue() ? "1" : "0"; 
            }
            else if (type == OBType.DOUBLE) {
                return ((DecimalFormat)doubleFormatter.clone()).format(value);
            }
            else {
                return String.valueOf(value);
            }
        }
    }
    
    public int asInt() {
        if (isNull()) return 0;
        
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        
        if (value instanceof String) {
            return Double.valueOf((String)value).intValue();
        }
        
        if (value instanceof Boolean) {
            return (Boolean)value ? 1 : 0;
        }
        
        return 0;
    }
    
    public double asDouble() {
        if (isNull()) return 0.0;
        
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        
        if (value instanceof String) {
            return Double.parseDouble((String)value);
        }
        
        if (value instanceof Boolean) {
            return (Boolean)value ? 1.0 : 0.0;
        }
        
        return 0.0;
    }
    
    public Date asDate() {
        if (isNull()) return null;
        
        if (value instanceof Date) {
            return (Date)value;
        }
        
        if (value instanceof String) {
            return DateUtils.parseDate((String)value);
        }
        
        return null; // TODO throw type cast exception
    }
    
    public boolean isNull() {
        if (value == null || 
                (value instanceof String && ((String)value).isEmpty())) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public Object asType(OBType type) {
        if (type == type || type == OBType.OBJECT) {
            return value;
        }
        
        else {
            switch (type) {
            case STRING:
                return asString();
            case BOOLEAN:
                return asBoolean();
            case DOUBLE:
                return asDouble();
            case INTEGER:
                return asInt();
            case DATE:
                return asDate();
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        OBValue other = (OBValue) obj;
        
        if (this.isNull() && other.isNull()) {
            return true;
        }
        
        if (this.type == OBType.STRING || other.type == OBType.STRING) {
            return this.asString().equals(other.asString());
        }
        else if (this.type == OBType.DOUBLE || other.type == OBType.DOUBLE) {
            return this.asDouble() == other.asDouble();
        }        
        else if (this.type == OBType.INTEGER || other.type == OBType.INTEGER) {
            return this.asInt() == other.asInt();
        }
        else if (this.type == OBType.BINARY && other.type == OBType.BINARY) {
            return Arrays.equals((byte[])this.value, (byte[])other.value);
        }
        else {
            return this.asString().equals(other.asString());
        }
    }

    @Override
    public String toString() {
        return value + "(" +type + ")";
    }
    
}
