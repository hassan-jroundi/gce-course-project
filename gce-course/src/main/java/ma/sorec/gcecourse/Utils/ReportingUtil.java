package ma.sorec.gcecourse.Utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

public class ReportingUtil {

    public static final String FONT = "times.ttf";
    public static final BaseColor BASE_COLOR_GRIS_BARBE_DENSE;
    public static final BaseColor BASE_COLOR_GRIS_BARBE_TRES_CLAIR;
    public static final BaseColor BASE_COLOR_GRIS_BARBE_CLAIR;
    public static final BaseColor BASE_COLOR_BRONZE;
    public static final BaseColor BASE_COLOR_BRONZE_DENSE;

    static {
        BASE_COLOR_GRIS_BARBE_DENSE = new BaseColor(56, 59, 61);
        BASE_COLOR_GRIS_BARBE_TRES_CLAIR = new BaseColor(185, 195, 204);
        BASE_COLOR_GRIS_BARBE_CLAIR = new BaseColor(121, 128, 132);
        BASE_COLOR_BRONZE = new BaseColor(161, 145, 81);
        BASE_COLOR_BRONZE_DENSE = new BaseColor(125, 110, 52);
    }

    public static Font FONT_TIMES_ROMAN(int size) {
        return FontFactory.getFont(FontFactory.TIMES, size);
    }

    public static Font FONT_TIMES_ROMAN_BOLD(int size) {
        return FontFactory.getFont(FontFactory.TIMES_BOLD, size);
    }

    public static Font FONT_TIMES_ROMAN_BOLD_BASE_COLOR_BRONZE(int size) {
        return FontFactory.getFont(FontFactory.TIMES_BOLD, size, 0, BASE_COLOR_BRONZE);
    }

    public static Font FONT_TIMES_ROMAN_BOLD_WHITE(int size) {
        return FontFactory.getFont(FontFactory.TIMES_BOLD, size, 0, BaseColor.WHITE);
    }

    public static Font FONT_TIMES_ROMAN_BOLD_GRIS_BARBE_TRES_CLAIR(int size) {
        return FontFactory.getFont(FontFactory.TIMES_BOLD, size, 0, BASE_COLOR_GRIS_BARBE_TRES_CLAIR);
    }

    public static Font FONT_TIMES_ROMAN_BOLD_GRIS_BARBE_CLAIR(int size) {
        return FontFactory.getFont(FontFactory.TIMES_BOLD, size, 0, BASE_COLOR_GRIS_BARBE_CLAIR);
    }

    public static Font FONT_TIMES_ROMAN_BOLD_GRIS_BARBE_DENSE(int size) {
        return FontFactory.getFont(FontFactory.TIMES_BOLD, size, 0, BASE_COLOR_GRIS_BARBE_DENSE);
    }

    public static Font FONT_TIMES_ROMAN_GRIS_BARBE_DENSE(int size) {
        return FontFactory.getFont(FontFactory.TIMES, size, 0, BASE_COLOR_GRIS_BARBE_DENSE);
    }

    public static Font FONT_COURIER(float size) {
        return FontFactory.getFont(FontFactory.HELVETICA, size);
    }

    public static Font FONT_COURIER_BOLD(float size) {
        return FontFactory.getFont(FontFactory.TIMES_BOLD, size);
    }

    public static Phrase SET_TIMES_BOLD(String str, int size) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD(size));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_BOLD_WITHE(String str, int size) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD_WHITE(size));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_BOLD_WITHE(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD_WHITE(12));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_BOLD_BRONZE(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD_BASE_COLOR_BRONZE(12));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_BOLD_BRONZE(String str, int size) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD_BASE_COLOR_BRONZE(size));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_BOLD(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD(12));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_BOLD_GRIS_BARBE_TRES_CLAIR(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD_GRIS_BARBE_TRES_CLAIR(12));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_BOLD_GRIS_BARBE_CLAIR(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD_GRIS_BARBE_CLAIR(12));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_BOLD_GRIS_BARBE_DENSE(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD_GRIS_BARBE_DENSE(12));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_BOLD_GRIS_BARBE_DENSE_SMALLER(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD_GRIS_BARBE_DENSE(11));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_BOLD_GRIS_BARBE_DENSE(String str, int size) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD_GRIS_BARBE_DENSE(size));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_ROMANS(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN(12));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_ROMANS_GRIS_BARBE_DENSE(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_GRIS_BARBE_DENSE(12));
        return new Phrase(chunk);
    }

    public static Phrase SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_GRIS_BARBE_DENSE(11));
        return new Phrase(chunk);
    }

    public static Chunk STYLE_TICKET(String str) {
        return new Chunk(str, FONT_COURIER(9.5f));
    }

    public static Chunk STYLE_TICKET_BL(String str) {
        return new Chunk(str, FONT_COURIER(8.5f));
    }

    public static Chunk STYLE_TICKET_CUISINE(String str) {
        return new Chunk(str, FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
    }

    public static Chunk STYLE_TICKET_A4(String str) {
        return new Chunk(str, FONT_COURIER(11f));
    }

    public static Chunk STYLE_TICKET_A4_10(String str) {
        return new Chunk(str, FONT_COURIER(10f));
    }

    public static Chunk STYLE_TICKET_8(String str) {
        return new Chunk(str, FONT_COURIER(9.8f));
    }

    public static Chunk STYLE_TICKET(String str, float size) {
        return new Chunk(str, FONT_COURIER(size));
    }

    public static Phrase PH_STYLE_TICKET(BaseFont bf, String str) {
        return new Phrase(new Chunk(str, new Font(bf, 10)));
    }

    public static Phrase PH_STYLE_TICKET_A4(BaseFont bf, String str) {
        return new Phrase(new Chunk(str, new Font(bf, 12)));
    }

    public static Chunk STYLE_TIMES_BOLD(String str, int size) {
        return new Chunk(str, FONT_TIMES_ROMAN_BOLD(size));
    }

    public static Chunk STYLE_TICKET_BOLD(String str) {
        return new Chunk(str, FONT_COURIER_BOLD(13));
    }

    public static Chunk STYLE_TICKET_BOLD_A4(String str) {
        return new Chunk(str, FONT_COURIER_BOLD(12));
    }

    public static Chunk STYLE_TICKET_BOLD_A4_11(String str) {
        return new Chunk(str, FONT_COURIER_BOLD(10));
    }

    public static Chunk STYLE_TICKET_SITUATION(String str) {
        return new Chunk(str, FONT_COURIER_BOLD(9.5f));
    }

    public static Chunk STYLE_TICKET_SITUATION_A4(String str) {
        return new Chunk(str, FONT_COURIER_BOLD(11.5f));
    }

    public static Phrase PH_STYLE_TICKET_SITUATION(BaseFont bf, String str) {
        return new Phrase(new Chunk(str, new Font(bf, 10.5f)));
    }

    public static Phrase PH_STYLE_TICKET_SITUATION_A4(BaseFont bf, String str) {
        return new Phrase(new Chunk(str, new Font(bf, 11.5f)));
    }

    public static Chunk STYLE_TICKET_BOLD(String str, float size) {
        return new Chunk(str, FONT_COURIER_BOLD(size));
    }

    public static Phrase PH_STYLE_TICKET_BOLD(BaseFont bf, String str, float size) {
        return new Phrase(new Chunk(str, new Font(bf, size)));
    }

    public static Chunk TIMES_ROMAN_TITLE(String str) {
        return new Chunk(str, FONT_TIMES_ROMAN(15));
    }

    public static Chunk TIMES_ROMAN_FACTURE(String str) {
        return new Chunk(str, FONT_TIMES_ROMAN(13));
    }

    public static Chunk TIMES_BOLD_UNDER(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD(15));
        chunk.setUnderline(-2f, -2f);
        return chunk;
    }

    public static Chunk TIMES_BOLD(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD(12));
        return chunk;
    }

    public static Chunk TIMES_BOLD(String str, int size) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN_BOLD(size));
        return chunk;
    }

    public static Chunk TIMES_ROMANS(String str) {
        Chunk chunk = new Chunk(str, FONT_TIMES_ROMAN(12));
        return chunk;
    }

    public static Chunk TIMES_ROMAN_BL(String str) {
        return new Chunk(str, FONT_TIMES_ROMAN(8));
    }

    public static Chunk STYLE_FACTURE(String str) {
        return new Chunk(str, FONT_TIMES_ROMAN(12));
    }

    public static Chunk STYLE_BL(String str) {
        return new Chunk(str, FONT_TIMES_ROMAN(9));
    }

    public static Chunk STYLE_FACTURE_SIZE(String str, int size) {
        return new Chunk(str, FONT_TIMES_ROMAN(size));
    }

    public static Chunk STYLE_FACTURE_BOLD(String str) {
        return new Chunk(str, FONT_TIMES_ROMAN_BOLD(10));
    }

    public static Chunk STYLE_BL_BOLD(String str) {
        return new Chunk(str, FONT_TIMES_ROMAN_BOLD(8));
    }

    public static Chunk TIMES_ROMAN_9(String str) {
        return new Chunk(str, FONT_TIMES_ROMAN(9));
    }

    public static Document prepareDocumentPdf(ByteArrayOutputStream baos, Rectangle pageSize, float left, float right,
                                              float top, float bottom) throws DocumentException {
        Document document = new Document(pageSize, left, right, top, bottom);
        PdfWriter.getInstance(document, baos);
        document.open();
        return document;
    }

}
