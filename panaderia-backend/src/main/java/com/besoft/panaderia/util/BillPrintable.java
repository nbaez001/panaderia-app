package com.besoft.panaderia.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

import com.besoft.panaderia.dto.response.DetalleVentaResponse;

public class BillPrintable implements Printable {
	List<DetalleVentaResponse> ldv;

	public BillPrintable(List<DetalleVentaResponse> ldv) {
		this.ldv = ldv;
	}

	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

		int result = NO_SUCH_PAGE;
		if (pageIndex == 0) {

			Graphics2D g2d = (Graphics2D) graphics;

			double width = pageFormat.getImageableWidth();

			g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

			////////// code by alqama//////////////
			FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.BOLD, 7));
			// int idLength=metrics.stringWidth("000000");
			// int idLength=metrics.stringWidth("00");
			int idLength = metrics.stringWidth("000");
			int amtLength = metrics.stringWidth("000000");
			int qtyLength = metrics.stringWidth("00000");
			int priceLength = metrics.stringWidth("000000");
			int prodLength = (int) width - idLength - amtLength - qtyLength - priceLength - 17;

			// int idPosition=0;
			// int productPosition=idPosition + idLength + 2;
			// int pricePosition=productPosition + prodLength +10;
			// int qtyPosition=pricePosition + priceLength + 2;
			// int amtPosition=qtyPosition + qtyLength + 2;
			int productPosition = 0;
			int discountPosition = prodLength + 5;
			int pricePosition = discountPosition + idLength + 10;
			int qtyPosition = pricePosition + priceLength + 4;
			int amtPosition = qtyPosition + qtyLength;

			try {
				/* Draw Header */
				int y = 10;// POSICION INICIAL EN Y
				int yShift = 15;// AUMENTO EN Y
				int headerRectHeight = 20;// AUMENTO EN Y (MAS GRANDE)

				g2d.setFont(new Font("Monospaced", Font.PLAIN, 15));
				g2d.drawString("-------------------------------------", 8, y);
				y += yShift;
				g2d.drawString("      RECIBO        ", 12, y);
				y += yShift;
				g2d.drawString("-------------------------------------", 6, y);
				y += headerRectHeight;

				for (DetalleVentaResponse dv : ldv) {
					g2d.drawString("S/" + dv.getSubtotal() + "   " + dv.getNomProducto(), 6, y);
					y += yShift;
				}

				g2d.drawString("-------------------------------------", 6, y);
				y += yShift;

//            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift; 
			} catch (Exception r) {
				r.printStackTrace();
			}

			result = PAGE_EXISTS;
		}
		return result;
	}

}
