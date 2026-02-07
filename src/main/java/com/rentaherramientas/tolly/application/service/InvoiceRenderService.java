package com.rentaherramientas.tolly.application.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.rentaherramientas.tolly.domain.model.Invoice;
import com.rentaherramientas.tolly.domain.model.InvoiceDetail;
import com.rentaherramientas.tolly.domain.model.Reservation;

@Service
public class InvoiceRenderService {

  public String renderHtml(Invoice invoice) {
    StringBuilder sb = new StringBuilder();
    Reservation reservation = invoice.getReservation();

    sb.append("<!doctype html>");
    sb.append("<html><head><meta charset=\"utf-8\">");
    sb.append("<title>Invoice ").append(invoice.getId()).append("</title>");
    sb.append("<style>");
    sb.append("body{font-family:Arial,sans-serif;color:#111;margin:24px}");
    sb.append("h1{margin:0 0 8px 0}");
    sb.append("table{border-collapse:collapse;width:100%;margin-top:12px}");
    sb.append("th,td{border:1px solid #ddd;padding:8px;text-align:left}");
    sb.append("th{background:#f5f5f5}");
    sb.append("</style></head><body>");

    sb.append("<h1>Factura</h1>");
    sb.append("<div><strong>ID:</strong> ").append(invoice.getId()).append("</div>");
    sb.append("<div><strong>Fecha:</strong> ").append(invoice.getIssueDate()).append("</div>");
    if (reservation != null) {
      sb.append("<div><strong>Reserva:</strong> ").append(reservation.getId()).append("</div>");
      sb.append("<div><strong>Cliente:</strong> ").append(reservation.getClientId()).append("</div>");
      sb.append("<div><strong>Desde:</strong> ").append(reservation.getStartDate()).append("</div>");
      sb.append("<div><strong>Hasta:</strong> ").append(reservation.getEndDate()).append("</div>");
    }

    sb.append("<table>");
    sb.append("<thead><tr>");
    sb.append("<th>Herramienta</th>");
    sb.append("<th>Precio dia</th>");
    sb.append("<th>Dias</th>");
    sb.append("<th>Subtotal</th>");
    sb.append("</tr></thead><tbody>");

    for (InvoiceDetail d : invoice.getDetails()) {
      sb.append("<tr>");
      sb.append("<td>").append(escapeHtml(nameOf(d))).append("</td>");
      sb.append("<td>").append(formatMoney(d.getDailyPrice())).append("</td>");
      sb.append("<td>").append(d.getRentalDay()).append("</td>");
      sb.append("<td>").append(formatMoney(d.getSubTotal())).append("</td>");
      sb.append("</tr>");
    }

    sb.append("</tbody></table>");
    sb.append("<h3>Total: ").append(formatMoney(invoice.getTotal())).append("</h3>");
    sb.append("</body></html>");
    return sb.toString();
  }

  public byte[] renderPdf(Invoice invoice) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document doc = new Document();
    try {
      PdfWriter.getInstance(doc, out);
      doc.open();

      Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
      doc.add(new Paragraph("Factura", titleFont));
      doc.add(new Paragraph("ID: " + invoice.getId()));
      doc.add(new Paragraph("Fecha: " + invoice.getIssueDate()));

      Reservation reservation = invoice.getReservation();
      if (reservation != null) {
        doc.add(new Paragraph("Reserva: " + reservation.getId()));
        doc.add(new Paragraph("Cliente: " + reservation.getClientId()));
        doc.add(new Paragraph("Desde: " + reservation.getStartDate()));
        doc.add(new Paragraph("Hasta: " + reservation.getEndDate()));
      }

      doc.add(new Paragraph(" "));
      PdfPTable table = new PdfPTable(4);
      table.setWidthPercentage(100);
      table.setHeaderRows(1);
      addHeader(table, "Herramienta");
      addHeader(table, "Precio dia");
      addHeader(table, "Dias");
      addHeader(table, "Subtotal");

      for (InvoiceDetail d : invoice.getDetails()) {
        table.addCell(safeCell(nameOf(d)));
        table.addCell(safeCell(formatMoney(d.getDailyPrice())));
        table.addCell(safeCell(String.valueOf(d.getRentalDay())));
        table.addCell(safeCell(formatMoney(d.getSubTotal())));
      }

      doc.add(table);
      doc.add(new Paragraph(" "));
      Paragraph total = new Paragraph("Total: " + formatMoney(invoice.getTotal()));
      total.setAlignment(Element.ALIGN_RIGHT);
      doc.add(total);
    } finally {
      doc.close();
    }
    return out.toByteArray();
  }

  private void addHeader(PdfPTable table, String text) {
    PdfPCell cell = new PdfPCell(new Phrase(text));
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    cell.setPadding(6);
    table.addCell(cell);
  }

  private PdfPCell safeCell(String text) {
    PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : ""));
    cell.setPadding(6);
    return cell;
  }

  private String nameOf(InvoiceDetail detail) {
    return detail.getTool() != null ? detail.getTool().getName() : "N/A";
  }

  private String formatMoney(BigDecimal value) {
    return value != null ? value.toPlainString() : "0.00";
  }

  private String escapeHtml(String value) {
    if (value == null) return "";
    return value.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;");
  }
}
