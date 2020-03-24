package at.srfg.indexing.model.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.ReadOnlyProperty;

import com.google.common.base.Strings;

public interface IPropertyAware {
	@ReadOnlyProperty
	public Map<String, PropertyType> getPropertyMap();
	public void setPropertyMap(Map<String, PropertyType> customProperties);
	/**
	 * Convenience method to add a {@link PropertyType} object
	 * holding complete metadata to an {@link IPropertyAware} 
	 * object. The provided metadata must provide a {@link PropertyType#getUri()}.
	 * @param property The property metadata
	 */
	default void addProperty(PropertyType property) {
		
		if ( getPropertyMap() == null) {
			setPropertyMap(new HashMap<String, PropertyType>());
		}
		// 
		if (!Strings.isNullOrEmpty(property.getUri())) {
			getPropertyMap().put(property.getUri(), property);
		}
	}

}
