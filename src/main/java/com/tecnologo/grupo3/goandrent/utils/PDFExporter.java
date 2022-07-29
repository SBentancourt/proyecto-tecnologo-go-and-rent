package com.tecnologo.grupo3.goandrent.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.tecnologo.grupo3.goandrent.dtos.HostBookingsPaymentsDTO;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class PDFExporter {
    private List<HostBookingsPaymentsDTO> hostBookingsPaymentsList;

    public PDFExporter(List<HostBookingsPaymentsDTO> hostBookingsPaymentsList) {
        this.hostBookingsPaymentsList = hostBookingsPaymentsList;
    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(RGBColor.DARK_GRAY);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.white);

        cell.setPhrase(new Phrase("Anfitrión - Banco - Cuenta", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("N° Reserva", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Fecha Inicio", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Imp Reserva", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Imp con comisión Go&Rent", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        PdfPCell cell = new PdfPCell();  cell.setBackgroundColor(RGBColor.GRAY);
        Font font = new Font(); font.setColor(RGBColor.WHITE);
        int lastReg = hostBookingsPaymentsList.size() - 1;
        int iter = 0;
        Float total = Float.valueOf(0);
        Boolean first = true;
        Float aPagar = Float.valueOf(0);
        Float diezPorc = Float.valueOf(0);
        for (HostBookingsPaymentsDTO h: hostBookingsPaymentsList){
            if(first) {
                table.addCell(h.getHostAlias() + " - " + h.getBank() + " - " + h.getAccount());
                first = false;
            } else {
                table.addCell("");
            }
            table.addCell(String.valueOf(h.getBookingId()));
            total = total + h.getFinalPrice();
            table.addCell(h.getStartDate());
            table.addCell(String.valueOf(h.getFinalPrice()));
            diezPorc = (h.getFinalPrice() * 10) / 100;
            aPagar = h.getFinalPrice() - diezPorc;
            table.addCell(String.valueOf(aPagar));

            if ((iter < lastReg) && !h.getHostAlias().trim().equals(hostBookingsPaymentsList.get(iter+1).getHostAlias().trim()) || (iter == lastReg)){
                diezPorc = (total * 10) / 100;
                aPagar = total - diezPorc;
                cell.setPhrase(new Phrase("", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Total: " + String.valueOf(aPagar), font));
                table.addCell(cell);

                total = Float.valueOf(0);   diezPorc = Float.valueOf(0);   aPagar = Float.valueOf(0);
                first = true;
            }
            iter = iter + 1;
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Total a pagar a Anfitriones", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);

        font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(10);
        font.setColor(Color.BLACK);
        p = new Paragraph("* En la última columna se indican los importes de cada reserva descontando la comisión que cobra Go&Rent por el uso de la aplicación. Go&Rent cobra el 10% del total de la reserva", font);
        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {4.0f, 1.0f, 1.1f, 1.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();

    }
}
