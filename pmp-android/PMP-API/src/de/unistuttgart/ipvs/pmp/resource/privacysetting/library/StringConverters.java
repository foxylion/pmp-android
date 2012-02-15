package de.unistuttgart.ipvs.pmp.resource.privacysetting.library;

public class StringConverters {
    
    public static class StringConverter implements IStringConverter<String> {
        
        @Override
        public String valueOf(String string) {
            return string;
        }
        
        
        @Override
        public String toString(String value) {
            return value;
        }
    }
    
    public static class LongConverter implements IStringConverter<Long> {
        
        @Override
        public Long valueOf(String string) {
            return Long.parseLong(string);
        }
        
        
        @Override
        public String toString(Long value) {
            return value.toString();
        }
    }
    
    public static class IntegerConverter implements IStringConverter<Integer> {
        
        @Override
        public Integer valueOf(String string) {
            return Integer.parseInt(string);
        }
        
        
        @Override
        public String toString(Integer value) {
            return value.toString();
        }
    }
    
    public class DoubleConverter implements IStringConverter<Double> {
        
        @Override
        public Double valueOf(String string) {
            return Double.parseDouble(string);
        }
        
        
        @Override
        public String toString(Double value) {
            return value.toString();
        }
    }
    
    public static class FloatConverter implements IStringConverter<Float> {
        
        @Override
        public Float valueOf(String string) {
            return Float.parseFloat(string);
        }
        
        
        @Override
        public String toString(Float value) {
            return value.toString();
        }
    }
}
