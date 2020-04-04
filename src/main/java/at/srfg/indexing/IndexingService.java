package at.srfg.indexing;

import java.util.List;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.srfg.indexing.model.common.ClassType;
import at.srfg.indexing.model.common.CodedType;
import at.srfg.indexing.model.common.PropertyType;
import at.srfg.indexing.model.solr.FacetResult;
import at.srfg.indexing.model.solr.IndexField;
import at.srfg.indexing.model.solr.Search;
import at.srfg.indexing.model.solr.SearchResult;
import io.swagger.annotations.ApiOperation;

/**
 * Annotated interface for the main cores, e.g. {@link ClassType},
 * {@link PropertyType} and {@link CodedType} instances.
 * @author dglachs
 *
 */
public interface IndexingService {
	/**
	 * Read a single concept class, e.g.  
	 * @param uri The id/uri of the class
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(
				method = RequestMethod.GET, 
				value="/class", 
				consumes = MediaType.APPLICATION_JSON_VALUE, 
				produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/class")
	ResponseEntity<?> getClass(
//			@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestParam(name = "uri") 				
			String uri) throws Exception;
	/**
	 * Obtain the field description (see {@link IndexField}) 
	 * @param fieldNames A set of field names to include in the result.
	 * 		  When empty or not provided, all fields are returned
	 * @return A collection of {@link IndexField}s.
	 * @throws Exception
	 */	
	@RequestMapping(
			method = RequestMethod.GET, 
			value="/class/fields", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/class/fields")
	ResponseEntity<?> classFields(
//			@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestParam(name="fieldName", required = false)	
			Set<String> fieldNames) throws Exception;

	@RequestMapping(
			method = RequestMethod.GET, 
			value="/class/select", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/class/select")
	ResponseEntity<?> selectClass(
//			@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestParam(name = "q", required = false, defaultValue = "*:*") 
			String query,
			@RequestParam(name = "fq", required = false) 
			List<String> filterQuery,
			@RequestParam(name = "facet.field", required = false) 
			List<String> facetFields,
			@RequestParam(name = "facet.limit", required = false, defaultValue = "15") 
			int facetLimit,
			@RequestParam(name = "facet.mincount", required = false, defaultValue = "1") 
			int facetMinCount,
			@RequestParam(name = "start", required = false, defaultValue = "0") 
			int start,
			@RequestParam(name = "rows", required = false, defaultValue = "10") int rows) throws Exception;

	@RequestMapping(
			method = RequestMethod.GET, 
			value="/class/suggest", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/class/suggest")
	ResponseEntity<?> classSuggest(
//			@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestParam(name = "q") 
			String query,
			@RequestParam(name = "field") 
			String fieldName,
			@RequestParam(name = "limit", required = false, defaultValue = "10") 
			int limit,
			@RequestParam(name = "minCount", required = false, defaultValue = "1") 
			int minCount
			) throws Exception;

	@RequestMapping(
			method = RequestMethod.POST, 
			value="/class/search", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/class/search")
	public ResponseEntity<?> searchClass(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestBody Search search
			) throws Exception;
	
	@RequestMapping(
			method = RequestMethod.GET, 
			value="/classes", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Retrieve specific classes (category) search results ", response = SearchResult.class)
	@GetMapping("/classes")
	public ResponseEntity<?> getClasses(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam(name="uri", required = false) Set<String> uriList,
			@RequestParam(name="nameSpace", required = false) String nameSpace,
			@RequestParam(name="localName", required = false) Set<String> localNames, 
			@RequestParam(required = false) String property
			) throws Exception;
	
	@RequestMapping(
			method = RequestMethod.DELETE, 
			value="/class", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Delete a class (category)", response = Boolean.class)
	@DeleteMapping("/class")
	public ResponseEntity<?> removeClass(
//			@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestParam String uri) throws Exception ;

	@RequestMapping(
			method = RequestMethod.POST, 
			value="/class", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Index a class (category)", response = Boolean.class)
	@PostMapping("/class")
	public ResponseEntity<?> setClass(
//			@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestBody ClassType prop) throws Exception;
	/**
	 * Obtain a {@link CodedType} element based on it's id/uri
	 * @param uri The id / uri of the coded type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(
			method = RequestMethod.GET, 
			value="/code", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Retrieve a specific value-code with a given uri", response = CodedType.class)
	@GetMapping("/code")
	public ResponseEntity<?> getCode(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam String uri) throws Exception;
	
	/**
	 * Obtain the description of the used fields in the {@link CodedType} index 
	 * @param fieldNames Optional - a predefined list of field names to include
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(
			method = RequestMethod.GET, 
			value="/code/fields", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Retrieve specific fields in a value-code", response = IndexField.class,
			responseContainer = "List")
	@GetMapping("/code/fields")
	public ResponseEntity<?> codeFields(
//    		@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestParam(name="fieldName", required=false) Set<String> fieldNames
			) throws Exception;

	@RequestMapping(
			method = RequestMethod.GET, 
			value="/code/select", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Select a specific value-code", response = SearchResult.class)
	@GetMapping("/code/select")
	public ResponseEntity<?> selectCode(
//			@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestParam(name = "q", required = false, defaultValue = "*:*") String query,
			@RequestParam(name = "fq", required = false) List<String> filterQuery,
			@RequestParam(name = "facet.field", required = false) List<String> facetFields,
			@RequestParam(name = "facet.limit", required = false, defaultValue = "15") int facetLimit,
			@RequestParam(name = "facet.mincount", required = false, defaultValue = "1") int facetMinCount,
			@RequestParam(name = "start", required = false, defaultValue = "0") int start,
			@RequestParam(name = "rows", required = false, defaultValue = "10") int rows) throws Exception ;
	
	@RequestMapping(
			method = RequestMethod.GET, 
			value="/class/suggest", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Retrieve suggestions for value-codes", response = FacetResult.class)
	@GetMapping("/code/suggest")
	public ResponseEntity<?> codeSuggest(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam(name = "q") String query,
			@RequestParam(name = "field") String fieldName,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "minCount", required = false, defaultValue = "1") int minCount
			
			) throws Exception;
	
	
	@RequestMapping(
			method = RequestMethod.POST, 
			value="/code/search", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Search for a specific value-code", response = SearchResult.class)
	@PostMapping("/code/search")
	public ResponseEntity<?> searchCode(
//			@RequestHeader(value = "Authorization") 
//			String bearerToken,
			@RequestBody Search search) throws Exception;

	@RequestMapping(
			method = RequestMethod.GET, 
			value="/codes", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Retrieve a search result for value-codes for given parameters", response =
			SearchResult.class)
	@GetMapping("/codes")
	public ResponseEntity<?> getCodes(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam(name="uri", required = false) Set<String> uriList, 
			@RequestParam(name="listId", required = false) String listId,
			@RequestParam(name="nameSpace", required = false) String nameSpace,
			@RequestParam(name="localName", required = false) Set<String> localNames) throws Exception;
	

	@RequestMapping(
			method = RequestMethod.DELETE, 
			value="/code", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Delete a specific value-code", response = Boolean.class)
	@DeleteMapping("/code")
	public ResponseEntity<?> removeCode(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam String uri) throws Exception ;

	@RequestMapping(
			method = RequestMethod.POST, 
			value="/code", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Index a value-code", response = Boolean.class)
	@PostMapping("/code")
	public ResponseEntity<?> setCode(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestBody CodedType prop) throws Exception;

	@RequestMapping(
			method = RequestMethod.GET, 
			value="/property/fields", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Retrieve specific fields of property index", response = IndexField.class,
			responseContainer = "List")
	@GetMapping("/property/fields")
	public ResponseEntity<?> propFields(
//    		@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam(name="fieldName", required=false) Set<String> fieldNames
			)throws Exception;

	@RequestMapping(
			method = RequestMethod.GET, 
			value="/property", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Retrieve a property identified by a uri", response = PropertyType.class)
	@GetMapping("/property")
	public ResponseEntity<?> getProperty(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam String uri)throws Exception ;

	@RequestMapping(
			method = RequestMethod.GET, 
			value="/properties", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Retrieve a search result of properties with given parameters",
			response = SearchResult.class)
	@GetMapping("/properties")
	public ResponseEntity<?> getProperties(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam(name = "uri", required = false) Set<String> uri,
			@RequestParam(name = "class", required = false) Set<String> classType,
			@RequestParam(name = "nameSpace", required = false) String nameSpace,
			@RequestParam(name = "localName", required = false) Set<String> localNames,
			@RequestParam(name = "idxName", required = false) Set<String> idxNames) throws Exception;
	
	@RequestMapping(
			method = RequestMethod.GET, 
			value="/property/suggest", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Retrieve suggestions for properties", response = FacetResult.class)
	@GetMapping("/property/suggest")
	public ResponseEntity<?> propertySuggest(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam(name = "q") String query,
			@RequestParam(name = "field") String fieldName,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "minCount", required = false, defaultValue = "1") int minCount
			) throws Exception ;
	
	
	@RequestMapping(
			method = RequestMethod.GET, 
			value="/property/select", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Select specific properties", response = SearchResult.class)
	@GetMapping("/property/select")
	public ResponseEntity<?> selectProperties(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam(name = "q", required = false, defaultValue = "*:*") String query,
			@RequestParam(name = "fq", required = false) List<String> filterQuery,
			@RequestParam(name = "facet.field", required = false) List<String> facetFields,
			@RequestParam(name = "facet.limit", required = false, defaultValue = "15") int facetLimit,
			@RequestParam(name = "facet.mincount", required = false, defaultValue = "1") int facetMinCount,
			@RequestParam(name = "start", required = false, defaultValue = "0") int start,
			@RequestParam(name = "rows", required = false, defaultValue = "10") int rows)throws Exception ;
	
	
	@RequestMapping(
			method = RequestMethod.POST, 
			value="/property/search", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Search for a property", response = SearchResult.class)
	@PostMapping("/property/search")
	public ResponseEntity<?> searchProperties(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestBody Search search)throws Exception ;
	
	
	@RequestMapping(
			method = RequestMethod.DELETE, 
			value="/property", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)

	@ApiOperation(value = "", notes = "Delete a property", response = Boolean.class)
	@DeleteMapping("/property")
	public ResponseEntity<?> removeProperty(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestParam String uri)throws Exception ;
	
	@RequestMapping(
			method = RequestMethod.POST, 
			value="/property", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "", notes = "Index a property", response = Boolean.class)
	@PostMapping("/property")
	public ResponseEntity<?> setProperty(
//			@RequestHeader(value = "Authorization") String bearerToken,
			@RequestBody PropertyType prop)throws Exception ;
}