package com.example.data.select

import com.example.models.Constants
import com.example.models.Constants.s
import com.example.models.PRmodel
import com.example.models.listPRmodel
import com.example.models.showPO
import java.sql.DriverManager

fun showallPR():listPRmodel {

    try {
        Constants.c = DriverManager.getConnection(Constants.db_name, Constants.user, Constants.password)
        Constants.s = Constants.c?.createStatement()
    }catch (e: Exception){
        println("errror connect $e")
    }

    val showallprList = mutableListOf<PRmodel>()

    val dataquerypr = "SELECT r.REQID, r.REQNO, r.REQDATE, r.TEMPLATEID, r.ISCLOSED\n" +
            "FROM REQUISITION r"
    val tempMap = mutableMapOf<String, MutableList<String>>()
    val resultSetDatatoko = s!!.executeQuery(dataquerypr)
    while (resultSetDatatoko.next()) {
        val prid = resultSetDatatoko.getString("REQID")
        val prno = resultSetDatatoko.getString("REQNO")
        val prdate = resultSetDatatoko.getString("REQDATE")
        val tmpltid = resultSetDatatoko.getString("TEMPLATEID")
        val isclosed = resultSetDatatoko.getString("ISCLOSED")

        // Menambahkan data ke dalam map dengan ID sebagai kunci
        if (!tempMap.containsKey(prid)) {
            tempMap[prid] = mutableListOf(prno ?: "", prdate ?: "", tmpltid ?: "", isclosed ?: "")
        } else {
            tempMap[prid]?.addAll(listOf(prno ?: "", prdate ?: "", tmpltid ?: "", isclosed ?: ""))
        }
    }
    // println("tmp data : $tempMap    ")


    for ((poid, values) in tempMap) {
        if (values.size % 4 == 0) {
            val poDatatokoObj = PRmodel(poid, values[0], values[1], values[2], values[3])
          //  println("po data : $poDatatokoObj")
            showallprList.add(poDatatokoObj)
        }
    }

    return  listPRmodel(showallprList)
}