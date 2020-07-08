package com.robertkonrad.recipemanager.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.RecipeIngredient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class PdfUtil {

    public static ByteArrayInputStream generatePdf(Recipe recipe){
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD);
            Font infoFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC);
            document.open();
            Paragraph titleParagraph = new Paragraph(recipe.getTitle(), titleFont);
            titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            Paragraph infoParagraph = new Paragraph();
            infoParagraph.setFont(infoFont);
            infoParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            infoParagraph.add(new Chunk(recipe.getAuthor().getUsername() + " "));
            infoParagraph.add(new Chunk(recipe.getCreatedDate().toString()));
            Image image;
            if (recipe.getImage().isEmpty()){
                image = Image.getInstance("src/main/resources/static/img/default.jpg");
            } else {
                image = Image.getInstance(recipe.getImage());
            }
            image.scaleAbsolute(150f,150f);
            List ingredientsList = new List();
            ingredientsList.setListSymbol("");
            java.util.Set<RecipeIngredient> ingredients = recipe.getIngredient();
            for (RecipeIngredient ingredient : ingredients) {
                ListItem listItem = new ListItem(ingredient.getAmount() + " - " + ingredient.getUnit() + " - " + ingredient.getIngredientName());
                listItem.setAlignment(Element.ALIGN_CENTER);
                ingredientsList.add(listItem);
            }
            ingredientsList.setListSymbol("\u2022");;
            PdfPTable table = new PdfPTable(new float[]{1,2});
            table.getDefaultCell().setBorder(0);
            PdfPCell pdfPCell = new PdfPCell();
            pdfPCell.addElement(image);
            pdfPCell.addElement(infoParagraph);
            pdfPCell.addElement(titleParagraph);
            pdfPCell.addElement(ingredientsList);
            pdfPCell.setBorder(0);
            table.addCell(pdfPCell);
            PdfPCell pdfPCell2 = new PdfPCell();
            Paragraph directionsParagraph = new Paragraph(recipe.getDirections());
            pdfPCell2.addElement(directionsParagraph);
            pdfPCell2.setPaddingLeft(25f);
            pdfPCell2.setBorder(0);
            table.addCell(pdfPCell2);
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

}
