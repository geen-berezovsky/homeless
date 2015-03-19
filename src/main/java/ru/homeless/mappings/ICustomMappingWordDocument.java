package ru.homeless.mappings;

import java.util.Map;

import javax.servlet.ServletContext;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import ru.homeless.entities.Client;

/**
 * Created by maxim on 30.11.14.
 */
public interface ICustomMappingWordDocument {
	
	public static final int AVATAR_LOCATION_TOP_RIGHT = 1;
    public static final int AVATAR_LOCATION_BOTTOM_CENTER = 2;

    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client);
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context);

}
