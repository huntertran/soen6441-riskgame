package soen6441riskgame.models.serializers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class CountryJsonExcludeStrategy implements ExclusionStrategy {
    private final Class<?> typeToSkip;

    public CountryJsonExcludeStrategy(Class<?> typeToSkip) {
      this.typeToSkip = typeToSkip;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return (clazz == typeToSkip);
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(JsonIgnore.class) != null;
    }
}