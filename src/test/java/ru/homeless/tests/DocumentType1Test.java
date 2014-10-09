package ru.homeless.tests;

import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Assert;
import org.junit.Test;

import ru.homeless.parsers.HttpRequestParser;
import ru.homeless.shared.IDocumentMapping;

public class DocumentType1Test {

	@Test
	public void notNullTest() throws IOException {
		HttpRequestParser hrp = new HttpRequestParser();
		XWPFDocument result = null;
		result = hrp.documentType1(IDocumentMapping.DOCUMENT_SOCIAL_HELP, 0, "", null);
		Assert.assertNotEquals(result, null);
	}

	
	@Test
	public void doesDataExistTest() throws IOException {
		HttpRequestParser hrp = new HttpRequestParser();
		XWPFDocument result = null;
		String city = "somevalue";
		result = hrp.documentType1(IDocumentMapping.DOCUMENT_SOCIAL_HELP, 0, city, null);
		Assert.assertTrue(result.toString().contains(city));
	}

	
}
