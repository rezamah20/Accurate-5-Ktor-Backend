package com.example.utils.pdf

import com.example.models.Constants
import com.example.models.Constants.c
import com.example.models.Constants.db_name
import com.example.models.Constants.s
import com.example.models.showitemPO
import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfPageEventHelper
import com.lowagie.text.pdf.PdfTable
import com.lowagie.text.pdf.PdfWriter
import java.awt.Color
import java.io.FileOutputStream
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.*


fun createpdf(showitempo: showitemPO):String{
    try {
        c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
        s = Constants.c?.createStatement()
    }catch (e: Exception){

    }finally {

    }

    val dataquery = "SELECT r.BRANCHNAME FROM BRANCHCODES r where r.BRANCHCODEID = 1"
    val tempMap = mutableMapOf<String, MutableList<String>>()
    val resultSetDataitem = s!!.executeQuery(dataquery)
    var BRANCHNAME = ""
    //var ADDRESSLINE1 = ""
    //var ADDRESSLINE2 = ""
    if (resultSetDataitem.next()){
        BRANCHNAME = resultSetDataitem.getString("BRANCHNAME")
       // ADDRESSLINE1 = resultSetDataitem.getString("ADDRESSLINE1")
      //  ADDRESSLINE2 = resultSetDataitem.getString("ADDRESSLINE2")
    }


    val document = Document(PageSize.A4, 36F, 36F, 65F, 36F)

    val originalString = showitempo.pomodel.PONO
    val modifiedString = originalString.replace("/", "-")

    val writer = PdfWriter.getInstance(document, FileOutputStream("/ktor/src/main/resources/static/$modifiedString.pdf"))
    writer.pageEvent = HeaderAndFooterPageEventHelper()
    document.open()

    val lineTable = PdfPTable(1)
    lineTable.widthPercentage = 95f // Mengatur panjang garis bawah menjadi 90% dari lebar halaman
    val lineCell = PdfPCell()
    lineCell.paddingTop = 65f
    lineCell.borderWidthBottom = 3f
    lineCell.border = PdfPCell.BOTTOM
    lineTable.addCell(lineCell)
    document.add(lineTable)

    //body PO
    bodypo(showitempo, document, BRANCHNAME)


//    document.newPage()
//    val page2Body = Paragraph("Page two content.")
//    page2Body.alignment = Element.ALIGN_CENTER
//    document.add(page2Body)

    document.close()
    writer.close()
    return "/ktor/src/main/resources/static/$modifiedString.pdf"
}


internal class HeaderAndFooterPageEventHelper : PdfPageEventHelper() {
    override fun onStartPage(writer: PdfWriter, document: Document?) {
        try {
            c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
            s = Constants.c?.createStatement()
        }catch (e: Exception){

        }finally {

        }

        val dataquery = "SELECT r.COMPANYNAME, r.ADDRESSLINE1, r.ADDRESSLINE2 FROM COMPANY r"
        val tempMap = mutableMapOf<String, MutableList<String>>()
        val resultSetDataitem = s!!.executeQuery(dataquery)
        var COMPANYNAME = ""
        var ADDRESSLINE1 = ""
        var ADDRESSLINE2 = ""
        if (resultSetDataitem.next()){
            COMPANYNAME = resultSetDataitem.getString("COMPANYNAME")
            ADDRESSLINE1 = resultSetDataitem.getString("ADDRESSLINE1")
            ADDRESSLINE2 = resultSetDataitem.getString("ADDRESSLINE2")
        }

        /* Header */
        val table = PdfPTable(2)
        table.setTotalWidth(510f)
        table.setWidths(intArrayOf(35, 75))
        table.defaultCell.horizontalAlignment = Element.ALIGN_LEFT
        table.defaultCell.paddingBottom = 5f
        table.defaultCell.border = Rectangle.NO_BORDER
        val emptyCell = PdfPCell(Paragraph(""))
        emptyCell.border = Rectangle.NO_BORDER

        var table2 = PdfPTable(1)
        table2.widthPercentage = 100f

        // Row#1 having 1 empty cell, 1 title cell and empty cell.
        //table.addCell(emptyCell)


        val title = Paragraph(COMPANYNAME.uppercase(Locale.getDefault()), Font(Font.COURIER, 25F, Font.BOLD, Color(59, 147, 213)))
        val titleCell = PdfPCell(title)
        val jpg: Image = Image.getInstance("a.jpg")
        titleCell.paddingBottom = 0f
        titleCell.paddingTop = 20f
        titleCell.horizontalAlignment = Element.ALIGN_CENTER
        titleCell.border = Rectangle.NO_BORDER

        val title2 = Paragraph(
            "$ADDRESSLINE1 "+
                ADDRESSLINE2, Font(Font.COURIER, 10f, Font.NORMAL))
        val titleCell2 = PdfPCell(title2)
        titleCell2.paddingBottom = 10f
        titleCell2.paddingTop = 10f
        titleCell2.horizontalAlignment = Element.ALIGN_CENTER
        titleCell2.border = Rectangle.NO_BORDER

        table2.addCell(titleCell)
        table2.addCell(titleCell2)

        table.addCell(jpg)
        table.addCell(table2)
//        table.addCell(titleCell2)

//        //Row#2 having 3 cells
//        val cellFont = Font(Font.HELVETICA, 8F)
//        table.addCell(Paragraph("Phone Number: 888-999-0000", cellFont))
//        table.addCell(Paragraph("Address : 333, Manhattan, New York", cellFont))
//       // table.addCell(Paragraph("Website : http://grogu-yoda.com", cellFont))
//        table.addCell(Paragraph("Website : http://intangroup.co.id", cellFont))


        // write the table on PDF
        table.writeSelectedRows(0, -1, 34f, 828f, writer.getDirectContent())
    }

    override fun onEndPage(writer: PdfWriter, document: Document) {

//        /* Footer */
//        val table = PdfPTable(2)
//        table.setTotalWidth(510f)
//        table.setWidths(intArrayOf(50, 50))
//        // Magic about default cell - if you add styling to default cell it will apply to all cells except cells added using addCell(PdfPCell) method.
//        table.defaultCell.paddingBottom = 5f
//        table.defaultCell.border = Rectangle.TOP
//        val title = Paragraph("Grogu Inc.", Font(Font.HELVETICA, 10F))
//        val titleCell = PdfPCell(title)
//        titleCell.paddingTop = 4f
//        titleCell.horizontalAlignment = Element.ALIGN_LEFT
//        titleCell.border = Rectangle.TOP
//        table.addCell(titleCell)
//        val pageNumberText = Paragraph("Page " + document.pageNumber, Font(Font.HELVETICA, 10F))
//        val pageNumberCell = PdfPCell(pageNumberText)
//        pageNumberCell.paddingTop = 4f
//        pageNumberCell.horizontalAlignment = Element.ALIGN_RIGHT
//        pageNumberCell.border = Rectangle.TOP
//        table.addCell(pageNumberCell)
//
//        // write the table on PDF
//        table.writeSelectedRows(0, -1, 34f, 36f, writer.getDirectContent())
    }
}

fun bodypo(showitempo: showitemPO, document: Document, namep: String){

    headerbody(showitempo, document, namep)
    bodytable(showitempo, document)
    bodyfooter(document, namep)

}

fun headerbody(showitempo: showitemPO, document: Document, namep: String){

    val tableheader = PdfPTable(2)

    tableheader.widthPercentage = 95f
    tableheader.setWidths(intArrayOf(90, 60))
    tableheader.defaultCell.horizontalAlignment = PdfPCell.LEFT
    tableheader.defaultCell.paddingTop = 0f
    tableheader.defaultCell.paddingBottom = 20f
    tableheader.defaultCell.border = Rectangle.NO_BORDER

    var tablerow = PdfPTable(1)
    tablerow.widthPercentage = 100f

    //Title purchase order
    val potext = Paragraph("Purchase Order", Font(Font.COURIER, 20f, Font.BOLD))
    val potextCell2 = PdfPCell(potext)
    potextCell2.paddingBottom = 0f
    potextCell2.paddingTop = 10f
    potextCell2.horizontalAlignment = Element.ALIGN_LEFT
    potextCell2.border = Rectangle.NO_BORDER

    val emptybodyCell = PdfPCell(Paragraph("", Font(Font.COURIER, 16f, Font.BOLD)))
    emptybodyCell.paddingBottom = 0f
    emptybodyCell.paddingTop = 30f
    emptybodyCell.border = Rectangle.NO_BORDER

    //No Surat
    val nosurat = Paragraph("No Surat", Font(Font.COURIER, 13f, Font.BOLD))
    val nosuratCell2 = PdfPCell(nosurat)
    nosuratCell2.paddingBottom = 0f
    nosuratCell2.paddingTop = 20f
    nosuratCell2.horizontalAlignment = Element.ALIGN_LEFT
    nosuratCell2.border = Rectangle.NO_BORDER

    val nosuratvalue = Paragraph(showitempo.pomodel.PONO, Font(Font.COURIER, 10f, Font.NORMAL))
    val nosuratvalueCell2 = PdfPCell(nosuratvalue)
    nosuratvalueCell2.paddingBottom = 0f
    nosuratvalueCell2.paddingTop = 3f
    nosuratvalueCell2.horizontalAlignment = Element.ALIGN_LEFT
    nosuratvalueCell2.border = Rectangle.NO_BORDER

    //Nama Toko
    val namatoko = Paragraph("Nama Toko", Font(Font.COURIER, 13f, Font.BOLD))
    val namatokoCell2 = PdfPCell(namatoko)
    namatokoCell2.paddingBottom = 0f
    namatokoCell2.paddingTop = 20f
    namatokoCell2.horizontalAlignment = Element.ALIGN_LEFT
    namatokoCell2.border = Rectangle.NO_BORDER

    val namatokovalue = Paragraph(showitempo.showItem[0].NAME, Font(Font.COURIER, 10f, Font.NORMAL))
    val namatokovalueCell2 = PdfPCell(namatokovalue)
    namatokovalueCell2.paddingBottom = 10f
    namatokovalueCell2.paddingTop = 3f
    namatokovalueCell2.horizontalAlignment = Element.ALIGN_LEFT
    namatokovalueCell2.border = Rectangle.NO_BORDER

    //No Surat
    val tanggal = Paragraph("Tanggal", Font(Font.COURIER, 13f, Font.BOLD))
    val tanggalCell2 = PdfPCell(tanggal)
    tanggalCell2.paddingBottom = 0f
    tanggalCell2.paddingTop = 10f
    tanggalCell2.horizontalAlignment = Element.ALIGN_LEFT
    tanggalCell2.border = Rectangle.NO_BORDER

    val inputDate = showitempo.showItem[0].PODATE
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    val date = parser.parse(inputDate)
    val formattedDate = formatter.format(date)


    val tanggalvalue = Paragraph(formattedDate, Font(Font.COURIER, 10f, Font.NORMAL))
    val tanggalvalueCell2 = PdfPCell(tanggalvalue)
    tanggalvalueCell2.paddingBottom = 0f
    tanggalvalueCell2.paddingTop = 3f
    tanggalvalueCell2.horizontalAlignment = Element.ALIGN_LEFT
    tanggalvalueCell2.border = Rectangle.NO_BORDER

    //tanggal
    val perusahaan = Paragraph("Perusahaan", Font(Font.COURIER, 13f, Font.BOLD))
    val perusahaanCell2 = PdfPCell(perusahaan)
    perusahaanCell2.paddingBottom = 0f
    perusahaanCell2.paddingTop = 20f
    perusahaanCell2.horizontalAlignment = Element.ALIGN_LEFT
    perusahaanCell2.border = Rectangle.NO_BORDER

    val perusahaanvalue = Paragraph(namep, Font(Font.COURIER, 10f, Font.NORMAL))
    val perusahaanvalueCell2 = PdfPCell(perusahaanvalue)
    perusahaanvalueCell2.paddingBottom = 10f
    perusahaanvalueCell2.paddingTop = 3f
    perusahaanvalueCell2.horizontalAlignment = Element.ALIGN_LEFT
    perusahaanvalueCell2.border = Rectangle.NO_BORDER


    var tableleft = PdfPTable(1)
    tableleft.defaultCell.border = Rectangle.NO_BORDER
    tablerow.widthPercentage = 100f

    var tableright = PdfPTable(1)
    tablerow.widthPercentage = 100f

    tableleft.addCell(potext)
    tableright.addCell(emptybodyCell)

    tableleft.addCell(nosuratCell2)
    tableleft.addCell(nosuratvalueCell2)
    tableleft.addCell(namatokoCell2)
    tableleft.addCell(namatokovalueCell2)

    tableright.addCell(tanggalCell2)
    tableright.addCell(tanggalvalueCell2)
    tableright.addCell(perusahaanCell2)
    tableright.addCell(perusahaanvalueCell2)

    tableheader.addCell(tableleft)
    tableheader.addCell(tableright)

    document.add(tableheader)
}

fun bodytable(showitempo: showitemPO, document: Document){

    val tableheaderbody = PdfPTable(4)

    tableheaderbody.widthPercentage = 95f
    tableheaderbody.setWidths(intArrayOf(15, 100, 30, 40))
    tableheaderbody.defaultCell.horizontalAlignment = PdfPCell.LEFT
    tableheaderbody.defaultCell.paddingTop = 20f
    tableheaderbody.defaultCell.paddingBottom = 0f
    tableheaderbody.defaultCell.border = Rectangle.NO_BORDER

    val pono = Paragraph("No", Font(Font.COURIER, 13f, Font.BOLD, Color.WHITE))
    val ponoCell2 = PdfPCell(pono)
    ponoCell2.fixedHeight = 24f
    ponoCell2.paddingBottom = 0f
    ponoCell2.paddingTop = 2f
    ponoCell2.horizontalAlignment = Element.ALIGN_CENTER
    ponoCell2.verticalAlignment = Element.ALIGN_CENTER
    ponoCell2.border = Rectangle.NO_BORDER
    ponoCell2.backgroundColor = Color(17, 107, 174)

    val namabarang = Paragraph("Nama Barang", Font(Font.COURIER, 13f, Font.BOLD, Color.WHITE))
    val namabarangCell2 = PdfPCell(namabarang)
    namabarangCell2.fixedHeight = 24f
    namabarangCell2.paddingBottom = 0f
    namabarangCell2.paddingTop = 2f
    namabarangCell2.horizontalAlignment = Element.ALIGN_LEFT
    namabarangCell2.verticalAlignment = Element.ALIGN_CENTER
    namabarangCell2.border = Rectangle.NO_BORDER
    namabarangCell2.backgroundColor = Color(17, 107, 174)

    val jumlah = Paragraph("Jumlah", Font(Font.COURIER, 13f, Font.BOLD, Color.WHITE))
    val jumlahCell2 = PdfPCell(jumlah)
    jumlahCell2.fixedHeight = 24f
    jumlahCell2.paddingBottom = 0f
    jumlahCell2.paddingTop = 2f
    jumlahCell2.paddingRight = 10f
    jumlahCell2.horizontalAlignment = Element.ALIGN_RIGHT
    jumlahCell2.verticalAlignment = Element.ALIGN_CENTER
    jumlahCell2.border = Rectangle.NO_BORDER
    jumlahCell2.backgroundColor = Color(17, 107, 174)

    val harga = Paragraph("Harga", Font(Font.COURIER, 13f, Font.BOLD, Color.WHITE))
    val hargaCell2 = PdfPCell(harga)
    hargaCell2.fixedHeight = 24f
    hargaCell2.paddingBottom = 0f
    hargaCell2.paddingTop = 2f
    hargaCell2.horizontalAlignment = Element.ALIGN_LEFT
    hargaCell2.verticalAlignment = Element.ALIGN_CENTER
    hargaCell2.border = Rectangle.NO_BORDER
    hargaCell2.backgroundColor = Color(17, 107, 174)


    tableheaderbody.addCell(ponoCell2)
    tableheaderbody.addCell(namabarangCell2)
    tableheaderbody.addCell(jumlahCell2)
    tableheaderbody.addCell(hargaCell2)

    var i = 0
    showitempo.showItem.forEach{
        i++
        val ponovalue = Paragraph(i.toString(), Font(Font.COURIER, 10f, Font.NORMAL))
        val ponovalueCell2 = PdfPCell(ponovalue)
        ponovalueCell2.fixedHeight = 24f
        ponovalueCell2.paddingBottom = 0f
        ponovalueCell2.paddingTop = 2f
        ponovalueCell2.horizontalAlignment = Element.ALIGN_CENTER
        ponovalueCell2.verticalAlignment = Element.ALIGN_CENTER
        ponovalueCell2.border = Rectangle.BOTTOM
       // ponovalueCell2.backgroundColor = Color(17, 107, 174)

        val namabarangvalue = Paragraph(it.ITEMOVDESC, Font(Font.COURIER, 10f, Font.NORMAL))
        val namabarangvalueCell2 = PdfPCell(namabarangvalue)
        namabarangvalueCell2.fixedHeight = 24f
        namabarangvalueCell2.paddingBottom = 0f
        namabarangvalueCell2.paddingTop = 2f
        namabarangvalueCell2.horizontalAlignment = Element.ALIGN_LEFT
        namabarangvalueCell2.verticalAlignment = Element.ALIGN_CENTER
        namabarangvalueCell2.border = Rectangle.BOTTOM
      //  namabarangvalueCell2.backgroundColor = Color(17, 107, 174)

        var total = if (it.QUANTITY.isEmpty()) "" else formatToThousands(it.QUANTITY.toFloat()).toString()
        val jumlahvalue = Paragraph("${total} ${it.ITEMRESERVED10}", Font(Font.COURIER, 10f, Font.NORMAL))
        val jumlahvalueCell2 = PdfPCell(jumlahvalue)
        jumlahvalueCell2.fixedHeight = 24f
        jumlahvalueCell2.paddingBottom = 0f
        jumlahvalueCell2.paddingTop = 2f
        jumlahvalueCell2.paddingRight = 10f
        jumlahvalueCell2.horizontalAlignment = Element.ALIGN_RIGHT
        jumlahvalueCell2.verticalAlignment = Element.ALIGN_CENTER
        jumlahvalueCell2.border = Rectangle.BOTTOM
       // jumlahvalueCell2.backgroundColor = Color(17, 107, 174)

        var harga = if (it.UNITPRICE.isEmpty()) "" else formatToThousands(it.UNITPRICE.toFloat())
        val hargaisi = if (harga.toInt() == 0){""}else{"Rp ${harga}"}
        val hargavalue = Paragraph(hargaisi, Font(Font.COURIER, 10f, Font.NORMAL))
        val hargavalueCell2 = PdfPCell(hargavalue)
        hargavalueCell2.fixedHeight = 24f
        hargavalueCell2.paddingBottom = 0f
        hargavalueCell2.paddingTop = 2f
        hargavalueCell2.horizontalAlignment = Element.ALIGN_LEFT
        hargavalueCell2.verticalAlignment = Element.ALIGN_CENTER
        hargavalueCell2.border = Rectangle.BOTTOM
      //  hargaCell2.backgroundColor = Color(17, 107, 174)
        tableheaderbody.addCell(ponovalueCell2)
        tableheaderbody.addCell(namabarangvalueCell2)
        tableheaderbody.addCell(jumlahvalueCell2)
        tableheaderbody.addCell(hargavalueCell2)

    }

    val keterangan = Paragraph("Keterangan", Font(Font.COURIER, 13f, Font.BOLD))
    val keteranganCell2 = PdfPCell(keterangan)
    keteranganCell2.paddingBottom = 0f
    keteranganCell2.paddingTop = 20f
    keteranganCell2.paddingLeft = 20f
    keteranganCell2.horizontalAlignment = Element.ALIGN_LEFT
    keteranganCell2.border = Rectangle.NO_BORDER

    val keteranganvalue = Paragraph(showitempo.pomodel.DESCRIPTION, Font(Font.COURIER, 10f, Font.NORMAL))
    val keteranganvalueCell2 = PdfPCell(keteranganvalue)
    keteranganvalueCell2.paddingBottom = 0f
    keteranganvalueCell2.paddingTop = 5f
    keteranganvalueCell2.paddingLeft = 20f
    keteranganvalueCell2.horizontalAlignment = Element.ALIGN_LEFT
    keteranganvalueCell2.border = Rectangle.NO_BORDER

    document.add(tableheaderbody)

    //keterangan
    var tableleftket = PdfPTable(1)
    tableleftket.widthPercentage = 60f
    tableleftket.defaultCell.backgroundColor = Color.BLUE
    tableleftket.horizontalAlignment = Element.ALIGN_LEFT


    tableleftket.addCell(keteranganCell2)
    tableleftket.addCell(keteranganvalueCell2)

    document.add(tableleftket)

}

fun bodyfooter(document: Document, namep: String){
    val tablefooterbody = PdfPTable(2)

    tablefooterbody.widthPercentage = 95f
    tablefooterbody.setWidths(intArrayOf(90, 50))
    tablefooterbody.defaultCell.horizontalAlignment = PdfPCell.LEFT
    tablefooterbody.defaultCell.paddingTop = 20f
    tablefooterbody.defaultCell.paddingBottom = 0f
    tablefooterbody.defaultCell.border = Rectangle.NO_BORDER

    var tableleffoot = PdfPTable(1)
    tableleffoot.widthPercentage = 100f
    tableleffoot.defaultCell.backgroundColor = Color.BLUE
    tableleffoot.horizontalAlignment = Element.ALIGN_LEFT


    var tablerightfoot = PdfPTable(1)
    tablerightfoot.widthPercentage = 100f
    tablerightfoot.defaultCell.backgroundColor = Color.BLUE
    tablerightfoot.horizontalAlignment = Element.ALIGN_CENTER


    val hormatkami = Paragraph("Hormat Kami", Font(Font.COURIER, 12f, Font.BOLD))
    val hormatkamiCell2 = PdfPCell(hormatkami)
    hormatkamiCell2.paddingBottom = 0f
    hormatkamiCell2.paddingTop = 5f
    hormatkamiCell2.paddingLeft = 20f
    hormatkamiCell2.horizontalAlignment = Element.ALIGN_CENTER
    hormatkamiCell2.border = Rectangle.NO_BORDER

    val hormatpt = Paragraph(namep, Font(Font.COURIER, 12f, Font.NORMAL))
    val hormatptCell2 = PdfPCell(hormatpt)
    hormatptCell2.paddingBottom = 0f
    hormatptCell2.paddingTop = 60f
    hormatptCell2.paddingLeft = 20f
    hormatptCell2.horizontalAlignment = Element.ALIGN_CENTER
    hormatptCell2.border = Rectangle.NO_BORDER

    val emptyCell = PdfPCell(Paragraph(""))
    emptyCell.border = Rectangle.NO_BORDER

    tableleffoot.addCell(emptyCell)
    tablerightfoot.addCell(hormatkamiCell2)
    tablerightfoot.addCell(hormatptCell2)

    //tableleffoot.addCell(tableleffoot)
    tablefooterbody.addCell(tableleffoot)
    tablefooterbody.addCell(tablerightfoot)


    document.add(tablefooterbody)

}

fun formatToThousands(input: Float): String {
    var resulta = ""
    val result = if (input % 1 == 0f) {
        var a = input.toInt()
        resulta = a.toString()
    } else {
        var b = input.toDouble()
        resulta = b.toString()
    }
    return resulta
}