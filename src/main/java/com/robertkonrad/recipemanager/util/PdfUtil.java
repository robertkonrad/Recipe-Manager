package com.robertkonrad.recipemanager.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.robertkonrad.recipemanager.entity.Recipe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class PdfUtil {

    public static ByteArrayInputStream generatePdf(Recipe recipe){
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            // TODO: 06.07.2020 prepare pdf layout 
            document.open();
            document.add(new Paragraph(recipe.getTitle()));
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

}
