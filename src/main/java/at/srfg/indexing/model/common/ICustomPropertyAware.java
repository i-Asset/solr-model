package at.srfg.indexing.model.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.data.annotation.ReadOnlyProperty;
/**
 * Interface providing the functionality for indexing custom properties.
 * <p>
 * Implementors inherit default functionality for handling dynamic properties
 * 
 * </p>
 * @author dglachs
 *
 */
public interface ICustomPropertyAware {
	/**
	 * Custom Property Key's including their label used
	 */
	String CUSTOM_KEY_FIELD = "*_key";
	/**
	 * The custom string properties
	 */
	String CUSTOM_STRING_PROPERTY = "*_ss";
	String CUSTOM_DOUBLE_PROPERTY = "*_ds";
	String CUSTOM_INTEGER_PROPERTY = "*_is";
	String CUSTOM_BOOLEAN_PROPERTY = "*_b";
	/**
	 * Manage the Dynamic Key parts and manage the original labeling 
	 * in {@link ICustomPropertyAware#CUSTOM_KEY_FIELD}
	 * @param parts
	 * @return
	 */
	default String mapDynamicKeyParts(String ... parts) {
		String key = DynamicName.getDynamicFieldPart(parts);
		// use @ as delimiter
		getCustomPropertyKeys().put(key, String.join("@", parts));
		return key;
	}

	/**
	 * Setter for a customized boolean value. 
	 * @param value
	 * @param qualifier
	 */
	default void setProperty(Boolean value, String ...qualifier) {
		setProperty(value, null, qualifier);
	}
	
	/**
	 * Setter for a customized boolean value. 
	 * @param value
	 * @param qualifier
	 */
	default void setProperty(Boolean value, PropertyType customMeta, String ...qualifier) {
		String key = mapDynamicKeyParts(qualifier);
		getCustomBooleanValue().put(key, value);
		// handle the customMeta
		getCustomProperties().put(key, customMeta);
	}
	
	
	default void addProperty(Double value, String ... qualifier) {
		addProperty(value, null, qualifier);

	}
	default void addProperty(Double value, PropertyType customMeta, String ... qualifier) {
		String key = mapDynamicKeyParts(qualifier);
		
		Collection<Double> values = getCustomDoubleValues().get(key);
		if ( values == null ) {
			// use a set to avoid duplicates
			getCustomDoubleValues().put(key, new HashSet<Double>());
		}
		// now add the value to the double map
		getCustomDoubleValues().get(key).add(value);
		if ( customMeta !=null ) {
			// handle custom property metadata
			getCustomProperties().put(key, customMeta);
		}
	}
	
	default void addProperty(Integer value, String ... qualifier) {
		addProperty(value, null, qualifier);
	}
	
	default void addProperty(Integer value, PropertyType customMeta, String ... qualifier) {
		String key = mapDynamicKeyParts(qualifier);
		
		Collection<Integer> values = getCustomIntValues().get(key);
		if ( values == null ) {
			// use a set to avoid duplicates
			getCustomIntValues().put(key, new HashSet<Integer>());
		}
		// now add the value
		getCustomIntValues().get(key).add(value);
		if ( customMeta !=null ) {
			// handle custom property metadata
			getCustomProperties().put(key, customMeta);
		}
	}
	/**
	 * add a custom multilingual property to the index
	 * @param text
	 * @param locale
	 * @param qualifier
	 */
	default void addMultiLingualProperty(String text, Locale locale, String ...qualifier ) {
		addMultiLingualProperty(text, locale.getLanguage(), qualifier);
	}
	default void addMultiLingualProperty(String text, String lang, String ... qualifier) {
		String key = mapDynamicKeyParts(qualifier);
		Collection<String> values = getCustomStringValues().get(key);
		if ( values == null ) {
			getCustomStringValues().put(key,  new HashSet<String>());
		}
		// The value is finally stored with <text>@<lang>
		getCustomStringValues().get(key).add(String.format("%s@%s", text, lang));
	}
	/**
	 * Obtain a list of multilingual property values
	 * @param qualifier The qualifier used when storing  the multi lingual property value
	 * @param locale The language code, such as <i>en</i>, <i>es</i>
	 * @return A list of multi lingual property values for the desired languag, empty list when no value present
	 */
	default List<String> getMultiLingualProperties(Locale locale, String ...qualifier ) {
		return getMultiLingualProperties(locale.getLanguage(), qualifier);
	}
	default Optional<String> getMultiLingualProperty(Locale locale, String ...qualifier ) {
		return getMultiLingualProperty(locale.getLanguage(), qualifier);
	}
	/**
	 * Obtain a single multilingual property value
	 * @param language
	 * @param qualifier
	 * @return
	 */
	default Optional<String> getMultiLingualProperty(String language, String ...qualifier ) {
		Collection<String> v = getMultiLingualProperties(language, qualifier);
		if ( v!=null ) {
			return v.stream().findFirst();
		}
		return Optional.empty();
		
	}
	default List<String> getMultiLingualProperties(String language, String ... qualifier) {
		String key = mapDynamicKeyParts(qualifier);
		Collection<String> values = getCustomStringValues().get(key);
		if ( values != null && ! values.isEmpty() ) {
			return values.stream().filter(new Predicate<String>() {
				private String extractLanguage(String t) {
					int delim = t.lastIndexOf("@");
					if ( delim > 0 ) {
						return t.substring(delim+1);
					}
					return t;
				}
				@Override
				public boolean test(String t) {
					if ( language.equals(extractLanguage(t))) {
						return true;
					}
					return false;
				}})
			.map(new Function<String, String>() {
				private String extractValue(String t) {
					int delim = t.lastIndexOf("@");
					if ( delim > 0 && t.length()>delim+1) {
						return t.substring(0,delim);
					}
					return null;
				}
				@Override
				public String apply(String t) {
					return extractValue(t);
				}})
			.collect(Collectors.toList());
		}

		return new ArrayList<>();
	}

	default void addProperty(String value, String ... qualifier) {
		String key = mapDynamicKeyParts(qualifier);
		
		Collection<String> values = getCustomStringValues().get(key);
		if ( values == null ) {
			// use a set to avoid duplicates
			getCustomStringValues().put(key, new HashSet<String>());
		}
		// now add the value
		getCustomStringValues().get(key).add(value);
	}
	/**
	 * Set a single custom string property
	 * @param value
	 * @param qualifier
	 */
	default void setProperty(String value, String ...qualifier ) {
		String key = mapDynamicKeyParts(qualifier);
		// single value, override any existing value
		getCustomStringValues().put(key, Collections.singleton(value));
	}
	/**
	 * Retrieve the collection of values - may be <code>null</code>
	 * @param qualifier
	 * @return
	 */
	default Collection<String> getStringPropertyValues(String ... qualifier) {
		String key = mapDynamicKeyParts(qualifier);
		return getCustomStringValues().get(key);
		
	}
	default void setStringPropertyValues(Collection<String> values, String ...qualifier ) {
		String key = mapDynamicKeyParts(qualifier);
		getCustomStringValues().put(key, values);
	}
	default Optional<String> getStringPropertyValue(String ...qualifier) {
		Collection<String> v = getStringPropertyValues(qualifier);
		if ( v!=null ) {
			return v.stream().findFirst();
		}
		return Optional.empty();
	}
	default Collection<Double> getDoublePropertyValues(String ... qualifier) {
		String key = mapDynamicKeyParts(qualifier);
		return getCustomDoubleValues().get(key);
	}
	default <T> Collection<T> getProperty(Class<T> clazz, String ... qualifier) {
		Collection<T> values = new ArrayList<T>();
		return values;
	}
	default Collection<Integer> getIntPropertyValues(String ... qualifier) {
		String key = mapDynamicKeyParts(qualifier);
		return getCustomIntValues().get(key);
	}
	/**
	 * Getter for the custom Integer values. Must not return <code>null</code>
	 * @return
	 */
	public Map<String, Collection<Integer>> getCustomIntValues();
	/**
	 * Getter for the custom double values. Must not return <code>null</code>
	 * @return
	 */
	public Map<String, Collection<Double>> getCustomDoubleValues();
	/**
	 * Getter for the custom string values. Must not return null;
	 * @return
	 */
	public Map<String, Collection<String>> getCustomStringValues();
	/**
	 * Getter for the custom boolean values, must not return <code>null</code>
	 * @return
	 */
	public Map<String, Boolean> getCustomBooleanValue();
	/**
	 * Getter for the custom property key map holding the original qualifier
	 * and the mapped index field name part.
	 * @return
	 */
	public Map<String,String> getCustomPropertyKeys();
	/**
	 * Getter for the map holding the meta data for the custom 
	 * property.
	 * @return The custom property map, must not return <code>null</code>.
	 */
	@ReadOnlyProperty
	public Map<String, PropertyType> getCustomProperties();
	
}
