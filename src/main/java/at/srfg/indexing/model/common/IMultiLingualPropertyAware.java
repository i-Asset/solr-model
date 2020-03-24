package at.srfg.indexing.model.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface IMultiLingualPropertyAware {
	/**
	 * Collection of languages
	 */
	String LANGUAGES_FIELD = "languages";
	
	public Collection<String> getLanguages();
	
	public Map<String, Collection<String>> getMultiLingualValues();
	default void setProperty(String property, Locale locale, String value) {
		String key = DynamicName.getDynamicFieldPart(locale.getLanguage(), property);
		getMultiLingualValues().put(key, Collections.singleton(value));
	}
	default void addProperty(String property, Locale locale, String value) {
		String key = DynamicName.getDynamicFieldPart(locale.getLanguage(), property);
		Collection<String> values = getMultiLingualValues().get(key);
		if ( values == null) {
			getMultiLingualValues().put(key, new HashSet<String>());
		}
		getMultiLingualValues().get(key).add(value);
	}
	default void setProperties(String property, Locale locale, Collection<String> value) {
		String key = DynamicName.getDynamicFieldPart(locale.getLanguage(), property);
		getMultiLingualValues().put(key, value);
	}
	default Collection<String> getProperties(String property, Locale locale) {
		String key = DynamicName.getDynamicFieldPart(locale.getLanguage(), property);
		return getMultiLingualValues().get(key);
	}
	default Optional<String> getProperty(String property, Locale locale) {
		String key = DynamicName.getDynamicFieldPart(locale.getLanguage(), property);
		Collection<String> v = getMultiLingualValues().get(key);
		if ( v!=null ) {
			return v.stream().findFirst();
		}
		return Optional.empty();
	}
}
