package ru.homeless.util;

import org.primefaces.component.fileupload.FileUploadRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Created by maxim on 12.11.2015.
 */
public class AdvancedFileUploadRenderer extends FileUploadRenderer {
    @Override
    public void decode(FacesContext context, UIComponent component) {
        if (context.getExternalContext().getRequestContentType().toLowerCase().startsWith("multipart/")) {
            super.decode(context, component);
        }
    }

}
