package de.unistuttgart.ipvs.pmp.resource.privacysetting.library;

import de.unistuttgart.ipvs.pmp.resource.privacysetting.IStringConverter;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IStringConverterWithExceptions;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

/**
 * Implements various state-less {@link IStringConverter} singletons.
 * 
 * @author Jakob Jarosch, Tobias Kuhn
 * 
 */
public final class StringConverter {
    
    /*
     * Singleton instances
     */
    
    public static final IStringConverter<String> forString = new StringConverter2();
    public static final IStringConverter<Long> forLong = new LongConverter();
    public static final IStringConverter<Integer> forInteger = new IntegerConverter();
    public static final IStringConverter<Double> forDouble = new DoubleConverter();
    public static final IStringConverter<Float> forFloat = new FloatConverter();
    
    /*
     * Actual converters
     */
    
    static class StringConverter2 implements IStringConverter<String> {
        
        @Override
        public String valueOf(String string) {
            return string;
        }
        
        
        @Override
        public String toString(String value) {
            return value;
        }
    }
    
    static class LongConverter implements IStringConverter<Long> {
        
        @Override
        public Long valueOf(String string) {
            return Long.parseLong(string);
        }
        
        
        @Override
        public String toString(Long value) {
            return value.toString();
        }
    }
    
    static class IntegerConverter implements IStringConverter<Integer> {
        
        @Override
        public Integer valueOf(String string) {
            return Integer.parseInt(string);
        }
        
        
        @Override
        public String toString(Integer value) {
            return value.toString();
        }
    }
    
    static class DoubleConverter implements IStringConverter<Double> {
        
        @Override
        public Double valueOf(String string) {
            return Double.parseDouble(string);
        }
        
        
        @Override
        public String toString(Double value) {
            return value.toString();
        }
    }
    
    static class FloatConverter implements IStringConverter<Float> {
        
        @Override
        public Float valueOf(String string) {
            return Float.parseFloat(string);
        }
        
        
        @Override
        public String toString(Float value) {
            return value.toString();
        }
    }
    
    /*
     * PS library wrapped converters
     */
    
    protected static final IStringConverterWithExceptions<String> forStringWrap = wrapExceptions(forString);
    protected static final IStringConverterWithExceptions<Long> forLongWrap = wrapExceptions(forLong);
    protected static final IStringConverterWithExceptions<Integer> forIntegerWrap = wrapExceptions(forInteger);
    protected static final IStringConverterWithExceptions<Double> forDoubleWrap = wrapExceptions(forDouble);
    protected static final IStringConverterWithExceptions<Float> forFloatWrap = wrapExceptions(forFloat);
    
    
    /**
     * Wraps a converter into another one and catches all exceptions, so {@link PrivacySettingValueException}s are
     * thrown.
     * 
     * @param converter
     * @return <code>converter</code> wrapped so that only {@link PrivacySettingValueException} will be thrown
     */
    public static <T> IStringConverterWithExceptions<T> wrapExceptions(final IStringConverter<T> converter) {
        return new IStringConverterWithExceptions<T>() {
            
            @Override
            public T valueOf(String string) throws PrivacySettingValueException {
                try {
                    return converter.valueOf(string);
                } catch (Throwable t) {
                    throw new PrivacySettingValueException(t.getMessage(), t);
                }
            }
            
            
            @Override
            public String toString(T value) throws PrivacySettingValueException {
                try {
                    return converter.toString(value);
                } catch (Throwable t) {
                    throw new PrivacySettingValueException(t.getMessage(), t);
                }
            }
        };
    }
}
