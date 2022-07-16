package ricochetrobotsfx;

import java.util.Objects;

public record Pair<K, V>(K key, V value) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Pair<?,?>))
            return false;

        Pair pair = (Pair)obj;
        if (!Objects.equals(key, pair.key()))
            return false;
        return Objects.equals(value, pair.value());
    }
}