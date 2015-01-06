package ru.homeless.renderers;

import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.component.selectoneradio.SelectOneRadioRenderer;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;

/**
 * Created by maxim on 18.12.14.
 */
public class PreambleOneRadioChecboxRenderer extends SelectOneRadioRenderer {

    @Override
    protected void encodeOptionLabel(FacesContext context, SelectOneRadio radio, String containerClientId,
                                     SelectItem option, boolean disabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("label", null);
        writer.writeAttribute("for", containerClientId, null);

        if (option.getDescription() != null) {
            writer.writeAttribute("title", option.getDescription(), null);
        }

        if (disabled) {
            writer.writeAttribute("class", "ui-state-disabled", null);
        }

        if (option.isEscape()) {
            writer.writeText(option.getLabel(), null);
        } else {
            writer.write(option.getLabel());
        }

        writer.endElement("label");

    }
}
