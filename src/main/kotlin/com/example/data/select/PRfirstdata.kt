package com.example.data.select

import com.example.models.*
import com.example.models.Constants.c
import com.example.models.Constants.db_name
import com.example.models.Constants.password
import com.example.models.Constants.s
import com.example.models.Constants.user
import java.sql.DriverManager
import java.sql.ResultSet
import java.time.LocalDate

fun selectpreparePR(): firstdataPR {
    try {
        c = DriverManager.getConnection(db_name, user, password)
        s = c?.createStatement()
    }catch (e: Exception){

    }
    val dataprquery = "select RequisitionID from Get_RequisitionID"
    val rsprid = s!!.executeQuery(dataprquery)
    var prid = ""
    if (rsprid.next()){
        prid = rsprid.getString("REQUISITIONID")
    }

    val currentDate = LocalDate.now()
    val currentMonth = currentDate.monthValue
    val currentYear = currentDate.year

    val resulSetPRNO ="SELECT\n" +
            "COUNT(*) AS TOTAL_DATA\n" +
            "FROM\n" +
            "REQUISITION\n" +
            "WHERE\n" +
            "EXTRACT(MONTH FROM reqdate) = $currentMonth\n" +
            "AND EXTRACT(YEAR FROM reqdate) = $currentYear;"
    //   val dataprnoquery = "select RequisitionID from Get_RequisitionID"
    val rsprno = s!!.executeQuery(resulSetPRNO)
    var prno = ""
    if (rsprno.next()){
        prno = rsprno.getString("TOTAL_DATA")
    }
    var prNocodebulan =""
    when (currentMonth) {
        1 -> prNocodebulan = "I"
        2 -> prNocodebulan = "II"
        3 -> prNocodebulan = "III"
        4 -> prNocodebulan = "IV"
        5 -> prNocodebulan = "V"
        6 -> prNocodebulan = "VI"
        7 -> prNocodebulan = "VII"
        8 -> prNocodebulan = "VIII"
        9 -> prNocodebulan = "IX"
        10 -> prNocodebulan = "X"
        11 -> prNocodebulan = "XI"
        12 -> prNocodebulan = "XII"
        else -> prNocodebulan = "NULL"
    }

    val prNOint = prno.toInt()+1
    val prNOintfix = when {
        prNOint < 100 -> String.format("%03d", prNOint)
        else -> prNOint.toString()
    }

    val resultSetPRnocode: ResultSet? = s?.executeQuery("SELECT r.KLU FROM COMPANY r")
    var prNOcode = ""
    if (resultSetPRnocode != null && resultSetPRnocode.next()){
        prNOcode = resultSetPRnocode.getString("KLU")
    }

    var prNO = "$prNOintfix/PR/$prNOcode/$prNocodebulan/$currentYear"

    return firstdataPR(PRID = prid, PRNO = prNO)
}

suspend fun getPRno(bulan:String, tahun:String):firstdataPR{
    try {
        c = DriverManager.getConnection(db_name, user, password)
        s = c?.createStatement()
    }catch (e: Exception){

    }
    val resulSetPRNO ="SELECT\n" +
            "COUNT(*) AS TOTAL_DATA\n" +
            "FROM\n" +
            "REQUISITION\n" +
            "WHERE\n" +
            "EXTRACT(MONTH FROM reqdate) = $bulan\n" +
            "AND EXTRACT(YEAR FROM reqdate) = $tahun;"
    //   val dataprnoquery = "select RequisitionID from Get_RequisitionID"
    val rsprno = s!!.executeQuery(resulSetPRNO)
    var prno = ""
    if (rsprno.next()){
        prno = rsprno.getString("TOTAL_DATA")
    }
    var prNocodebulan =""
    when (bulan.toInt()) {
        1 -> prNocodebulan = "I"
        2 -> prNocodebulan = "II"
        3 -> prNocodebulan = "III"
        4 -> prNocodebulan = "IV"
        5 -> prNocodebulan = "V"
        6 -> prNocodebulan = "VI"
        7 -> prNocodebulan = "VII"
        8 -> prNocodebulan = "VIII"
        9 -> prNocodebulan = "IX"
        10 -> prNocodebulan = "X"
        11 -> prNocodebulan = "XI"
        12 -> prNocodebulan = "XII"
        else -> prNocodebulan = "NULL"
    }

    val prNOint = prno.toInt()+1
    val prNOintfix = when {
        prNOint < 100 -> String.format("%03d", prNOint)
        else -> prNOint.toString()
    }

    val resultSetPRnocode: ResultSet? = s?.executeQuery("SELECT r.KLU FROM COMPANY r")
    var prNOcode = ""
    if (resultSetPRnocode != null && resultSetPRnocode.next()){
        prNOcode = resultSetPRnocode.getString("KLU")
    }

    val prNO = "$prNOintfix/PR/$prNOcode/$prNocodebulan/$tahun"

    return firstdataPR(PRNO = prNO)
}