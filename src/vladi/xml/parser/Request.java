package vladi.xml.parser;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Request {

    public final String file;
    public final String tag;
    public final List<AttributeValuePair> attrValuePairs;

    public Request(String file,
                   String tag,
                   String[] attributes,
                   String[] values) {
        Objects.requireNonNull(file);
        Objects.requireNonNull(tag);
        Objects.requireNonNull(attributes);
        Objects.requireNonNull(values);
        if (attributes.length != values.length) {
            throw new IllegalStateException("Attributes and values mismatch");
        }
        this.file = file;
        this.tag = tag;
        List<AttributeValuePair> attrValuePairs = new ArrayList<>(attributes.length);
        for (int i = 0; i < attributes.length; i++) {
            attrValuePairs.add(new AttributeValuePair(attributes[i], values[i]));
        }
        this.attrValuePairs = Collections.unmodifiableList(attrValuePairs);
    }

    public static class AttributeValuePair {
        public AttributeValuePair(String attribute, String value) {
            this.attribute = attribute;
            this.value = value;
        }

        public final String attribute;
        public final String value;
    }
}
