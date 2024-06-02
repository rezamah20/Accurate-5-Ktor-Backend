package com.example.data.select

import com.example.data.DatabaseseConnector
import com.example.data.connectDB
import com.example.models.*
import com.example.models.Constants.c
import com.example.models.Constants.db_name
import com.example.models.Constants.rs
import com.example.models.Constants.s
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.log

fun selectPOprepare():pointanData{
    var pointandata: pointanData

    val currentTime = LocalDateTime.now()
    val formatTanggal = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val tanggalsimple = currentTime.format(formatTanggal)
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    val datenow = currentTime.format(formatter)

    try {
        c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
        s = c?.createStatement()
    }catch (e: Exception){

    }
    val poDatatokoList = mutableListOf<poDatatoko>()

    //select from tb PERSONDATA
    val dataquery = "SELECT r.ID, r.PERSONNO, r.PERSONTYPE, r.BALANCE, r.\"NAME\" FROM PERSONDATA r where PERSONTYPE = 1"
    val tempMap = mutableMapOf<String, MutableList<String>>()
    val resultSetDatatoko = s!!.executeQuery(dataquery)
    while (resultSetDatatoko.next()) {
        val id = resultSetDatatoko.getString("ID")
        val personNo = resultSetDatatoko.getString("PERSONNO")
        val personType = resultSetDatatoko.getString("PERSONTYPE")
        val name = resultSetDatatoko.getString("NAME")

        // Menambahkan data ke dalam map dengan ID sebagai kunci
        if (!tempMap.containsKey(id)) {
            tempMap[id] = mutableListOf(personNo, personType, name)
        } else {
            tempMap[id]?.addAll(listOf(personNo, personType, name))
        }
    }

    for ((id, values) in tempMap) {
        if (values.size % 3 == 0) {
            val poDatatokoObj = poDatatoko(id, values[0], values[1], values[2])
            poDatatokoList.add(poDatatokoObj)
        }
    }

    //select from tb item
    val dataqueryitem = "SELECT r.ITEMNO, r.ITEMDESCRIPTION, r.ITEMTYPE, r.SUBITEM FROM ITEM r"
    val tempMapitem = mutableMapOf<String, MutableList<String>>()
    val resultSetitem = s!!.executeQuery(dataqueryitem)
    val poDataitemList = mutableListOf<itemPO>()

    //ambil data bulan tahun
    val bulanAngka = LocalDate.now().monthValue
    val tahunAngka = LocalDate.now().year
    while (resultSetitem.next()) {
        val itemno = resultSetitem.getString("ITEMNO")
        val itemdescription = resultSetitem.getString("ITEMDESCRIPTION")
        val itemtype = resultSetitem.getString("ITEMTYPE")
        val subitem = resultSetitem.getString("SUBITEM")
        if (itemdescription != null){
            if (!tempMapitem.containsKey(itemno)) {
                tempMapitem[itemno] = mutableListOf(itemdescription, itemtype,subitem)
            } else {
                tempMapitem[itemno]?.addAll(listOf(itemdescription, itemtype,subitem))
            }
        }
        // Menambahkan data ke dalam map dengan ID sebagai kunci
    }

    for ((id, values) in tempMapitem) {
        if (values.size % 3 == 0) {
            val poDatatokoObj = itemPO(id, values[0], values[1])
            poDataitemList.add(poDatatokoObj)
        }
    }

    val resultSetNo: ResultSet? = s?.executeQuery("SELECT r.NEXTNUMBER FROM NEXTINVOICENUMBER r where r.MODULEID = 1")
    var nextNumber = "" // Inisialisasi String kosong

    if (resultSetNo != null && resultSetNo.next()) {
        nextNumber = resultSetNo.getString("NEXTNUMBER")
    }

    //mengambil data poid
    val resultSetPOid: ResultSet? = s?.executeQuery("SELECT FIRST 1 POID, GLHISTID,TRANSACTIONID FROM PO ORDER BY POID DESC;")
    var poID = ""
    var poNO = ""
    var nextPOno = ""
    var finalglhistid = 0
    var finaltransactionid = 0
    var poNOcode = ""
    var poNocodebulan =""
    when (bulanAngka) {
        1 -> poNocodebulan = "I"
        2 -> poNocodebulan = "II"
        3 -> poNocodebulan = "III"
        4 -> poNocodebulan = "IV"
        5 -> poNocodebulan = "V"
        6 -> poNocodebulan = "VI"
        7 -> poNocodebulan = "VII"
        8 -> poNocodebulan = "VIII"
        9 -> poNocodebulan = "IX"
        10 -> poNocodebulan = "X"
        11 -> poNocodebulan = "XI"
        12 -> poNocodebulan = "XII"
        else -> poNocodebulan = "NULL"
    }

    if (resultSetPOid != null && resultSetPOid.next()){

        //mengambil data GLHISTID
        var resultglhistid = resultSetPOid.getString("GLHISTID")
        finalglhistid = resultglhistid.toInt()+1
        var transactionid = resultSetPOid.getString("TRANSACTIONID")
        finaltransactionid = transactionid.toInt()+1
       // var resultpoNO = resultSetPOid.getString("PONO")

        val resultIDPO: ResultSet? = s?.executeQuery("select * from GETPOID")
        var resultpoID = ""
        if (resultIDPO!!.next()){
            println("result poid ${resultIDPO!!.getString("POID")}")
            resultpoID = resultIDPO!!.getString("POID")
        }


        //get 3 string first
        val resulSetpoNO: ResultSet? = s?.executeQuery("SELECT COUNT(*) AS TOTALPO FROM PO WHERE GLPERIOD = '$bulanAngka' AND GLYEAR = '$tahunAngka'")
        var resultpoNO3string = ""
        if (resulSetpoNO != null && resulSetpoNO.next()){
            resultpoNO3string = resulSetpoNO.getString("TOTALPO")
        }
        //hasil po ID, po NO
        var poIDint = resultpoID
        val poNOint = resultpoNO3string.toInt()+1
//        println("result po no : $resultpoNO3string")
//        println("poNOint : $poNOint")

        val poNOintfix = when {
            poNOint < 100 -> String.format("%03d", poNOint)
            else -> poNOint.toString()
        }

        //nextnumber po
        val nextPOint = resultpoNO3string.toInt()+2
        val nextPOnointfix = when{
            nextPOint < 100 -> String.format("%03d", nextPOint)
            else -> nextPOint.toString()
        }

        val resultSetPOnocode: ResultSet? = s?.executeQuery("SELECT r.KLU FROM COMPANY r")
        if (resultSetPOnocode != null && resultSetPOnocode.next()){
            poNOcode = resultSetPOnocode.getString("KLU")
        }

        //hasil akhir poid, pono
        poID = poIDint.toString()
        poNO = "$poNOintfix/PO/$poNOcode/$poNocodebulan/$tahunAngka"
        nextPOno ="$nextPOnointfix/PO/$poNOcode/$poNocodebulan/$tahunAngka"

    }else{
        val resultSetPOnocode: ResultSet? = s?.executeQuery("SELECT r.KLU FROM COMPANY r")
        if (resultSetPOnocode != null && resultSetPOnocode.next()){
            poNOcode = resultSetPOnocode.getString("KLU")
        }
        poID = "1"
        poNO = "001/PO/$poNOcode/$poNocodebulan/$tahunAngka"
    }

    //mengambil data GLHISTID


    val podata = poData(POID = poID, PONO = poNO, DATE = datenow, GLPRIOD = bulanAngka.toString(), GLYEAR = tahunAngka.toString(), CHEQUEDATE = tanggalsimple, GLHISTID = finalglhistid.toString(), TRANSACTIONID = finaltransactionid.toString())

    //ambil data audit
    val resultSetAuditid: ResultSet? = s?.executeQuery("SELECT FIRST 1 AUDITID FROM AUDIT ORDER BY AUDITID DESC;")
    var auditidmentah = ""
    if (resultSetAuditid != null && resultSetAuditid.next()){
        auditidmentah = resultSetAuditid.getString("AUDITID")
    }
    var auditid = auditidmentah.toInt()+1
    val audit = Audit(AUDITID = auditid.toString())

    // nextnumber PO
    val nextnumberpo = nextNumberInvoice(NEXTNUMBER = nextPOno)
    pointandata = pointanData(podata, audit,nextnumberpo, poDatatokoList, poDataitemList)
   // println("datalis : $pointandata")

    return  pointandata
}

fun selectnumberpo(bulanAngka: Int, tahunAngka: Int, code_db: String):poData{

    val connector = DatabaseseConnector()
    connector.disconnect()
    connector.connnecttoDB(code_db)

    val resulSetpoNO: ResultSet? = s?.executeQuery("SELECT COUNT(*) AS TOTALPO FROM PO WHERE GLPERIOD = '$bulanAngka' AND GLYEAR = '$tahunAngka'")
    var resultpoNO3string = ""
    var poNOcode = ""
    var poNocodebulan =""
    var poNO = ""
    when (bulanAngka) {
        1 -> poNocodebulan = "I"
        2 -> poNocodebulan = "II"
        3 -> poNocodebulan = "III"
        4 -> poNocodebulan = "IV"
        5 -> poNocodebulan = "V"
        6 -> poNocodebulan = "VI"
        7 -> poNocodebulan = "VII"
        8 -> poNocodebulan = "VIII"
        9 -> poNocodebulan = "IX"
        10 -> poNocodebulan = "X"
        11 -> poNocodebulan = "XI"
        12 -> poNocodebulan = "XII"
        else -> poNocodebulan = "NULL"
    }

    if (resulSetpoNO != null && resulSetpoNO.next()){
        resultpoNO3string = resulSetpoNO.getString("TOTALPO")
    }
    //hasil po ID, po NO
    val poNOint = resultpoNO3string.toInt()+1

    val poNOintfix = when {
        poNOint < 100 -> String.format("%03d", poNOint)
        else -> poNOint.toString()
    }

    //nextnumber po
    val nextPOint = resultpoNO3string.toInt()+2
    val nextPOnointfix = when{
        nextPOint < 100 -> String.format("%03d", nextPOint)
        else -> nextPOint.toString()
    }

    val resultSetPOnocode: ResultSet? = s?.executeQuery("SELECT r.KLU FROM COMPANY r")
    if (resultSetPOnocode != null && resultSetPOnocode.next()){
        poNOcode = resultSetPOnocode.getString("KLU")
    }

    //hasil akhir poid, pono
    poNO = "$poNOintfix/PO/$poNOcode/$poNocodebulan/$tahunAngka"

    return poData(PONO = poNO)
}

fun selectPOdatatoko(code_db:String):dataToko{
    var podatatoko: poDatatoko

    val poDatatokoList = mutableListOf<poDatatoko>()
    val connector = DatabaseseConnector()
    connector.disconnect()
    connector.connnecttoDB(code_db)

    //select from tb PERSONDATA
    val dataquery = "SELECT r.ID, r.PERSONNO, r.PERSONTYPE, r.\"NAME\" FROM PERSONDATA r WHERE r.PERSONTYPE = 1"
    val tempMap = mutableMapOf<String, MutableList<String>>()
    val resultSetDatatoko = s!!.executeQuery(dataquery)
    while (resultSetDatatoko.next()) {
        val id = resultSetDatatoko.getString("ID")
        val personNo = resultSetDatatoko.getString("PERSONNO")
        val personType = resultSetDatatoko.getString("PERSONTYPE")
        val name = resultSetDatatoko.getString("NAME")

        // Menambahkan data ke dalam map dengan ID sebagai kunci
        if (!tempMap.containsKey(id)) {
            tempMap[id] = mutableListOf(personNo, personType, name)
        } else {
            tempMap[id]?.addAll(listOf(personNo, personType, name))
        }
    }

    for ((id, values) in tempMap) {
        if (values.size % 3 == 0) {
            val poDatatokoObj = poDatatoko(id, values[0], values[1], values[2])
            poDatatokoList.add(poDatatokoObj)
        }
    }

    return  dataToko(poDatatokoList)
}

fun selectPOdataitem(code_db: String):dataItem{

    val connector = DatabaseseConnector()
    connector.disconnect()
    connector.connnecttoDB(code_db)

    val poDataitemList = mutableListOf<itemPO>()
//    try {
//        c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
//        s = c?.createStatement()
//    }catch (e: Exception){
//
//    }

    //select from tb PERSONDATA
    val dataquery = "SELECT r.ITEMNO, r.ITEMDESCRIPTION, r.ITEMTYPE, r.SUBITEM FROM ITEM r WHERE r.ITEMTYPE = 1"
    val tempMap = mutableMapOf<String, MutableList<String>>()
    val resultSetDataitem = s!!.executeQuery(dataquery)
    while (resultSetDataitem.next()) {
        val ITEMNO = resultSetDataitem.getString("ITEMNO")
        val ITEMDESCRIPTION = resultSetDataitem.getString("ITEMDESCRIPTION")
        val ITEMTYPE = resultSetDataitem.getString("ITEMTYPE")
        val SUBITEM = resultSetDataitem.getString("SUBITEM")

        // Menambahkan data ke dalam map dengan ID sebagai kunci
        if (!tempMap.containsKey(ITEMNO)) {
            tempMap[ITEMNO] = mutableListOf(ITEMDESCRIPTION, ITEMTYPE, SUBITEM)
        } else {
            tempMap[ITEMNO]?.addAll(listOf(ITEMDESCRIPTION, ITEMTYPE, SUBITEM))
        }
    }

    for ((id, values) in tempMap) {
        if (values.size % 3 == 0) {
            val poDataitemObj = itemPO(id, values[0], values[1], values[2])
            poDataitemList.add(poDataitemObj)
        }
    }

    return  dataItem(poDataitemList)
}