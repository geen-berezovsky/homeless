package ru.homeless.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.*;
import ru.homeless.mappings.ICustomMappingWordDocument;

public class WordDocumentReplaceProcessor {
    public static final Logger log = Logger.getLogger(WordDocumentReplaceProcessor.class);
    private ObjectFactory factory;
    private WordprocessingMLPackage document;

    public WordDocumentReplaceProcessor(WordprocessingMLPackage document) {
    	this.document = document;
    }
    
    public WordprocessingMLPackage searchInParagraphs(Map<String, String> replacedMap, byte[] photo, int avatarLocation) throws Exception {

    	final String XPATH_TO_SELECT_TEXT_NODES = "//w:t";
    	
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

        //now check tables for picture placeholders
        factory = Context.getWmlObjectFactory();
        List elemetns = getAllElementFromObject(document.getMainDocumentPart(), Tbl.class);
        for(Object obj : elemetns){
            if(obj instanceof Tbl){
                Tbl table = (Tbl) obj;
                List rows = getAllElementFromObject(table, Tr.class);
                for(Object trObj : rows){
                    Tr tr = (Tr) trObj;
                    List cols = getAllElementFromObject(tr, Tc.class);
                    for(Object tcObj : cols){
                        Tc tc = (Tc) tcObj;
                        List texts2 = getAllElementFromObject(tc, Text.class);
                        for(Object textObj : texts2){
                            Text text = (Text) textObj;
                            if(text.getValue().equalsIgnoreCase("[t:photo]")){
                                P paragraphWithImage = addInlineImageToParagraph(createInlineImage(photo));
                                tc.getContent().remove(0);

                                PPr paragraphProperties = factory.createPPr();
                                Jc justification = factory.createJc();
                                if (avatarLocation == ICustomMappingWordDocument.AVATAR_LOCATION_TOP_RIGHT) {
                                    //SET ALIGNMENT TO THE RIGHT
                                    justification.setVal(JcEnumeration.RIGHT);
                                }
                                if (avatarLocation == ICustomMappingWordDocument.AVATAR_LOCATION_BOTTOM_CENTER) {
                                    //SET ALIGNMENT TO THE CENTER
                                    justification.setVal(JcEnumeration.CENTER);
                                }
                                paragraphProperties.setJc(justification);
                                paragraphWithImage.setPPr(paragraphProperties);


                                tc.getContent().add(paragraphWithImage);
                            }
                        }
                    }
                }
            }
        }



        return document;
    }

    private List getAllElementFromObject(Object obj, Class toSearch) {
        List result = new ArrayList();
        if (obj instanceof JAXBElement)
            obj = ((JAXBElement) obj).getValue();

        if (obj.getClass().equals(toSearch)){
            result.add(obj);
        }
        else if (obj instanceof ContentAccessor) {
            List children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
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
