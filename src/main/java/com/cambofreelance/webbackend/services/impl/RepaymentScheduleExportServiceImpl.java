package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.dto.response.InstallmentResponse;
import com.cambofreelance.webbackend.dto.response.PersistedScheduleResponse;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.services.RepaymentScheduleExportService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

@Service
public class RepaymentScheduleExportServiceImpl implements RepaymentScheduleExportService {

    private static final String[] HEADERS =
        {"#", "Due Date", "Principal", "Interest", "Total Due", "Paid", "Penalty", "Balance", "Status"};

    private static final ThreadLocal<SimpleDateFormat> DATE_FMT =
        ThreadLocal.withInitial(() -> new SimpleDateFormat("MMM dd, yyyy"));

    @Override
    public byte[] toXlsx(PersistedScheduleResponse s) {
        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Repayment Schedule");

            CellStyle headerStyle = wb.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle moneyStyle = wb.createCellStyle();
            moneyStyle.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));

            int row = 0;
            Row titleRow = sheet.createRow(row++);
            titleRow.createCell(0).setCellValue("Loan " + nullToEmpty(s.getLoanNumber())
                + " — Customer " + nullToEmpty(s.getCustomerCode()));

            Row summary = sheet.createRow(row++);
            summary.createCell(0).setCellValue("Total Repayment");
            summary.createCell(1).setCellValue(bd(s.getTotalRepayment()).doubleValue());
            summary.createCell(2).setCellValue("Net Disbursement");
            summary.createCell(3).setCellValue(bd(s.getNetDisbursement()).doubleValue());
            summary.createCell(4).setCellValue("Installments");
            summary.createCell(5).setCellValue(s.getNumberOfPayments());
            row++;

            Row header = sheet.createRow(row++);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(HEADERS[i]);
                c.setCellStyle(headerStyle);
            }

            List<InstallmentResponse> items = s.getInstallments() != null ? s.getInstallments() : List.of();
            for (InstallmentResponse it : items) {
                Row r = sheet.createRow(row++);
                r.createCell(0).setCellValue(it.getInstallmentNo());
                r.createCell(1).setCellValue(formatDate(it.getDueDate()));
                writeMoney(r, 2, it.getPrincipalAmount(), moneyStyle);
                writeMoney(r, 3, it.getInterestAmount(), moneyStyle);
                writeMoney(r, 4, it.getTotalPayment(), moneyStyle);
                writeMoney(r, 5, it.getPaidAmount(), moneyStyle);
                writeMoney(r, 6, it.getPenaltyAmount(), moneyStyle);
                writeMoney(r, 7, it.getRemainingBalance(), moneyStyle);
                r.createCell(8).setCellValue(nullToEmpty(it.getInstallmentStatus()));
            }

            for (int i = 0; i < HEADERS.length; i++) sheet.autoSizeColumn(i);

            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new AppException("EXPORT_FAILED", "Failed to generate XLSX: " + e.getMessage());
        }
    }

    @Override
    public byte[] toDocx(PersistedScheduleResponse s) {
        try (XWPFDocument doc = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XWPFParagraph title = doc.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.setText("Repayment Schedule");

            XWPFParagraph meta = doc.createParagraph();
            XWPFRun metaRun = meta.createRun();
            metaRun.setText("Loan: " + nullToEmpty(s.getLoanNumber())
                + "   Customer: " + nullToEmpty(s.getCustomerCode())
                + "   Currency: " + nullToEmpty(s.getCurrency()));
            metaRun.addBreak();
            metaRun.setText("Total Repayment: " + formatMoney(s.getTotalRepayment())
                + "   Net Disbursement: " + formatMoney(s.getNetDisbursement())
                + "   Installments: " + s.getNumberOfPayments());

            List<InstallmentResponse> items = s.getInstallments() != null ? s.getInstallments() : List.of();
            XWPFTable table = doc.createTable(items.size() + 1, HEADERS.length);
            XWPFTableRow header = table.getRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                XWPFTableCell c = header.getCell(i);
                c.setText("");
                XWPFParagraph p = c.getParagraphs().get(0);
                XWPFRun r = p.createRun();
                r.setBold(true);
                r.setText(HEADERS[i]);
            }
            for (int i = 0; i < items.size(); i++) {
                InstallmentResponse it = items.get(i);
                XWPFTableRow r = table.getRow(i + 1);
                r.getCell(0).setText(String.valueOf(it.getInstallmentNo()));
                r.getCell(1).setText(formatDate(it.getDueDate()));
                r.getCell(2).setText(formatMoney(it.getPrincipalAmount()));
                r.getCell(3).setText(formatMoney(it.getInterestAmount()));
                r.getCell(4).setText(formatMoney(it.getTotalPayment()));
                r.getCell(5).setText(formatMoney(it.getPaidAmount()));
                r.getCell(6).setText(formatMoney(it.getPenaltyAmount()));
                r.getCell(7).setText(formatMoney(it.getRemainingBalance()));
                r.getCell(8).setText(nullToEmpty(it.getInstallmentStatus()));
            }

            doc.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new AppException("EXPORT_FAILED", "Failed to generate DOCX: " + e.getMessage());
        }
    }

    @Override
    public byte[] toPdf(PersistedScheduleResponse s) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate(), 24, 24, 24, 24);
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Repayment Schedule", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);

            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            doc.add(new Paragraph("Loan: " + nullToEmpty(s.getLoanNumber())
                + "   Customer: " + nullToEmpty(s.getCustomerCode())
                + "   Currency: " + nullToEmpty(s.getCurrency()), metaFont));
            doc.add(new Paragraph("Total Repayment: " + formatMoney(s.getTotalRepayment())
                + "   Net Disbursement: " + formatMoney(s.getNetDisbursement())
                + "   Installments: " + s.getNumberOfPayments(), metaFont));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(HEADERS.length);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 3f, 2f, 2f, 2f, 2f, 2f, 2f, 2f});

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
            for (String h : HEADERS) {
                PdfPCell c = new PdfPCell(new Phrase(h, headerFont));
                c.setBackgroundColor(Color.DARK_GRAY);
                c.setHorizontalAlignment(Element.ALIGN_CENTER);
                c.setPadding(4f);
                table.addCell(c);
            }

            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            List<InstallmentResponse> items = s.getInstallments() != null ? s.getInstallments() : List.of();
            for (InstallmentResponse it : items) {
                table.addCell(pdfCell(String.valueOf(it.getInstallmentNo()), cellFont, Element.ALIGN_CENTER));
                table.addCell(pdfCell(formatDate(it.getDueDate()), cellFont, Element.ALIGN_LEFT));
                table.addCell(pdfCell(formatMoney(it.getPrincipalAmount()), cellFont, Element.ALIGN_RIGHT));
                table.addCell(pdfCell(formatMoney(it.getInterestAmount()), cellFont, Element.ALIGN_RIGHT));
                table.addCell(pdfCell(formatMoney(it.getTotalPayment()), cellFont, Element.ALIGN_RIGHT));
                table.addCell(pdfCell(formatMoney(it.getPaidAmount()), cellFont, Element.ALIGN_RIGHT));
                table.addCell(pdfCell(formatMoney(it.getPenaltyAmount()), cellFont, Element.ALIGN_RIGHT));
                table.addCell(pdfCell(formatMoney(it.getRemainingBalance()), cellFont, Element.ALIGN_RIGHT));
                table.addCell(pdfCell(nullToEmpty(it.getInstallmentStatus()), cellFont, Element.ALIGN_CENTER));
            }

            doc.add(table);
            doc.close();
            return out.toByteArray();
        } catch (IOException | com.lowagie.text.DocumentException e) {
            throw new AppException("EXPORT_FAILED", "Failed to generate PDF: " + e.getMessage());
        }
    }

    private PdfPCell pdfCell(String text, Font font, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(align);
        cell.setPadding(3f);
        return cell;
    }

    private void writeMoney(Row r, int col, BigDecimal value, CellStyle style) {
        Cell c = r.createCell(col);
        c.setCellValue(bd(value).doubleValue());
        c.setCellStyle(style);
    }

    private BigDecimal bd(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    private String formatMoney(BigDecimal v) {
        return String.format("%,.2f", bd(v));
    }

    private String formatDate(Date d) {
        return d == null ? "" : DATE_FMT.get().format(d);
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
