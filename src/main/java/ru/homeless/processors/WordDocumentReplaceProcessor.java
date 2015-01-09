package ru.homeless.processors;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;

public class WordDocumentReplaceProcessor {
    public static final Logger log = Logger.getLogger(WordDocumentReplaceProcessor.class);
    private ObjectFactory factory;
    private WordprocessingMLPackage document;

    public WordDocumentReplaceProcessor(WordprocessingMLPackage document) {
    	this.document = document;
    }
    
    public WordprocessingMLPackage searchInParagraphs(Map<String, String> replacedMap, byte[] photo, int avatarLocation) throws Exception {

    	final String XPATH_TO_SELECT_TEXT_NODES = "//w:t";
    	
		if (photo != null) {
        	    //IF PHOTO NOT NULL AND WE KNOW WHERE TO INSERT IT, DO IT!
		    //get current factory
    		    factory = Context.getWmlObjectFactory();
        	    //init new paragraph and insert into it the avatar
		    P paragraphWithImage = addInlineImageToParagraph(createInlineImage(photo));
    	
		    //SET ALIGNMENT TO THE RIGHT
		    PPr paragraphProperties = factory.createPPr();
		    Jc justification = factory.createJc();
		    justification.setVal(JcEnumeration.RIGHT);
		    paragraphProperties.setJc(justification);
		    paragraphWithImage.setPPr(paragraphProperties);
		
		    //add new paragraph to the start of document (0th position)
		    document.getMainDocumentPart().getContent().add(0, paragraphWithImage);
		}
    	
		List<?> texts = document.getMainDocumentPart().getJAXBNodesViaXPath(XPATH_TO_SELECT_TEXT_NODES, true);
		for (Object obj : texts) {
			Text text = (Text) ((JAXBElement<?>) obj).getValue();
			String textValue = text.getValue();
			for (Entry<String, String> entry : replacedMap.entrySet()) {
				if (textValue != null && textValue.contains(entry.getKey())) {
                    log.info("textValue="+textValue+" and entry.getKey()="+entry.getKey());
					textValue = textValue.replace(entry.getKey(), entry.getValue());
				}
			}
			text.setValue(textValue);
		}

    	
        return document;
    }
    
	private Inline createInlineImage(byte[] bytes) throws Exception {
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(document, bytes);
		int docPrId = 1;
		int cNvPrId = 2;
		return imagePart.createImageInline("Filename hint", "Alternative text", docPrId, cNvPrId, false);
	}
	
	private static P addInlineImageToParagraph(Inline inline) {
		// Now add the in-line image to a paragraph
		ObjectFactory factory = new ObjectFactory();
		P paragraph = factory.createP();
		R run = factory.createR();
		paragraph.getContent().add(run);
		Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		return paragraph;
	}



}
