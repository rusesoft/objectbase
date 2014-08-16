package org.objectbase;

import java.sql.Types;
import java.util.Date;

public enum OBType
{

	CHAR(Character.class, Types.CHAR),
    BOOLEAN(Boolean.class, Types.BOOLEAN),
    INTEGER(Integer.class, Types.INTEGER),
    LONG(Long.class, Types.BIGINT),
    FLOAT(Float.class, Types.FLOAT),
    DOUBLE(Double.class, Types.DOUBLE),
    DATE(Date.class, Types.TIMESTAMP),
    STRING(String.class, Types.VARCHAR),
    BINARY(byte[].class, Types.BINARY),
    OBJECT(Object.class, Types.OTHER),
    NULL(Object.class, Types.NULL),
    UNKNOWN( Object.class, Types.OTHER); 
    

    
    private OBType(Class<? extends Object> cls, int sqlType) {
        this.valueClass = cls;
        this.sqlType = sqlType;
    }
    
    private static final long serialVersionUID = 3834305120656962609L;
    private final transient Class<? extends Object> valueClass;
    private final transient int sqlType;


    public Class<? extends Object> getValueClass() {
        return valueClass;
    }
    
    public int getSQLType() {
        return sqlType;
    }

    
    public static OBType lookupClass(Class<?> cls) {
        if (cls.equals(String.class))
            return STRING;
        else if (cls.equals(Integer.class) || cls.equals(Integer.TYPE))
            return INTEGER;
        else if (cls.equals(Double.class) || cls.equals(Double.TYPE))
            return DOUBLE;
        else if (cls.equals(Boolean.class) || cls.equals(Boolean.TYPE))
            return BOOLEAN;
        else if (Date.class.isAssignableFrom(cls))
            return DATE;
        else if (Number.class.isAssignableFrom(cls))
            return DOUBLE;
        else if (cls.isArray() && cls.getComponentType().equals(Byte.TYPE))
            return BINARY;
        else if (!cls.isPrimitive() && !cls.isArray())
            return OBJECT;
        else
            return UNKNOWN;
    }
    
    public static OBType forValue(Object value) {
        if (value == null) {
            return OBType.STRING;
        }
        else {
            return lookupClass(value.getClass());
        }
    }
    

}
