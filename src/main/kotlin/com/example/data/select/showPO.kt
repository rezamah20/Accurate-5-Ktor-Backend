package com.example.data.select

import com.example.data.DatabaseseConnector
import com.example.data.testconnnectDB
import com.example.models.*
import com.example.models.Constants.db_name
import java.sql.DriverManager
import java.time.LocalDate

fun showalldataPO(code_db: String):showallPO{

    val connector = DatabaseseConnector()
        connector.disconnect()
        connector.connnecttoDB(code_db)
    //testconnnectDB(code_db)
//    try {
//        Constants.c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
//        Constants.s = Constants.c?.createStatement()
//    }catch (e: Exception){
//        println("errror connect $e")
//    }

    val showallpoList = mutableListOf<showPO>()

    val dataquery = "select t1.POID, t1.PONO, t1.PODATE, t2.\"NAME\", t1.DESCRIPTION , t1.PROCEED FROM PO t1 INNER JOIN PERSONDATA t2 on t1.VENDORID = t2.ID ORDER BY t1.POID DESC;"
    val tempMap = mutableMapOf<String, MutableList<String>>()
    val resultSetDatatoko = Constants.s!!.executeQuery(dataquery)
    while (resultSetDatatoko.next()) {
        val poid = resultSetDatatoko.getString("POID")
        println("po id : $poid")
        val pono = resultSetDatatoko.getString("PONO")
        val podate = resultSetDatatoko.getString("PODATE")
        val name = resultSetDatatoko.getString("NAME")
        val desc = resultSetDatatoko.getString("DESCRIPTION")
        val procced = resultSetDatatoko.getString("PROCEED")


        // Menambahkan data ke dalam map dengan ID sebagai kunci
        if (!tempMap.containsKey(poid)) {
            tempMap[poid] = mutableListOf(pono ?: "", podate ?: "", name ?: "", desc ?: "", procced ?: "")
        } else {
            tempMap[poid]?.addAll(listOf(pono ?: "", podate ?: "", name ?: "", desc ?: "", procced ?: ""))
        }
    }
   // println("tmp data : $tempMap    ")


    for ((poid, values) in tempMap) {
        if (values.size % 5 == 0) {
            val poDatatokoObj = showPO(poid, values[0], values[1], values[2], values[3], values[4])
        //    println("po data : $poDatatokoObj")
            showallpoList.add(poDatatokoObj)
        }
    }

    return showallPO(showallpoList)
}


fun showitemPO(code_db: String, idpo: Int): showitemPO {

    val connector = DatabaseseConnector()
    connector.disconnect()
    connector.connnecttoDB(code_db)

    val showallpoList = mutableListOf<showItem>()
    val dataquery = "SELECT t1.POID, t2.SEQ, t4.\"NAME\", t1.PONO, t1.PODATE, t3.ITEMNO, t3.ITEMDESCRIPTION, t2.ITEMOVDESC, t2.QUANTITY, t2.UNITPRICE, t2.ITEMRESERVED10, t1.DESCRIPTION\n" +
            "FROM PO t1\n" +
            "INNER JOIN PODET t2 ON t1.POID = t2.POID\n" +
            "INNER JOIN ITEM t3 ON t2.ITEMNO = t3.ITEMNO\n" +
            "INNER JOIN PERSONDATA t4 ON t4.ID = t1.VENDORID\n" +
            "WHERE t2.POID = $idpo ORDER BY t2.SEQ ASC"
    val tempMap = mutableMapOf<String, MutableList<String>>()
    val resultSetDatatoko = Constants.s!!.executeQuery(dataquery)
    var id = 0
    while (resultSetDatatoko.next()) {
        id++
        val poid = resultSetDatatoko.getString("POID")
        val seq = resultSetDatatoko.getString("SEQ")
        val pono = resultSetDatatoko.getString("PONO")
        val namevendor = resultSetDatatoko.getString("NAME")
        val podate = resultSetDatatoko.getString("PODATE")
        val itemno = resultSetDatatoko.getString("ITEMNO")
        val itemdesc = resultSetDatatoko.getString("ITEMDESCRIPTION")
        val name = resultSetDatatoko.getString("ITEMOVDESC")
        val quantity = resultSetDatatoko.getString("QUANTITY")
        val unitprice = resultSetDatatoko.getString("UNITPRICE")
        val itemres10 = resultSetDatatoko.getString("ITEMRESERVED10")
        val desc = resultSetDatatoko.getString("DESCRIPTION")

        // Menambahkan data ke dalam map dengan ID sebagai kunci
        if (!tempMap.containsKey(id.toString())) {
            tempMap[id.toString()] = mutableListOf(poid ?: "", seq ?: "", pono ?: "", namevendor ?: "", podate ?: "", itemno ?: "", itemdesc ?: "", name ?: "", quantity ?: "", unitprice ?: "", itemres10 ?: "", desc ?: "")
        } else {
            tempMap[id.toString()]?.addAll(listOf(poid ?: "", seq ?: "", pono ?: "", namevendor ?: "", podate ?: "", itemno ?: "", itemdesc ?: "", name ?: "", quantity ?: "", unitprice ?: "", itemres10 ?: "", desc ?: ""))
        }
    }

    for ((id, values) in tempMap) {
        if (values.size % 12 == 0) {
            val poDatatokoObj = showItem(id, values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11])
            showallpoList.add(poDatatokoObj)
        }
    }

    val dataqueryitem = "SELECT r.POID, r.VENDORID, r.PONO, r.DESCRIPTION  FROM PO r where r.POID = $idpo"
    println("id po = $idpo")
    val resultSetitem = Constants.s!!.executeQuery(dataqueryitem)
    var pomodel = POmodel()
    if (resultSetitem.next()){
        val poid = resultSetitem.getString("POID")
        val pono = resultSetitem.getString("PONO")
        val vendorid = resultSetitem.getString("VENDORID")
        val desc = resultSetitem.getString("DESCRIPTION") ?: ""
        pomodel = POmodel(POID = poid, PONO = pono, VENDORID = vendorid, DESCRIPTION = desc)
    }

    return showitemPO(pomodel, showallpoList)
}

fun searchPO(code_db: String, keyword: String):showallPO{
//    try {
//        Constants.c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
//        Constants.s = Constants.c?.createStatement()
//    }catch (e: Exception){
//
//    }


    val connector = DatabaseseConnector()
    connector.disconnect()
    connector.connnecttoDB(code_db)

    val showallpoList = mutableListOf<showPO>()

    val dataquery = "SELECT t1.POID, t2.SEQ, t4.\"NAME\", t1.PONO, t1.PODATE, t3.ITEMNO, t3.ITEMDESCRIPTION, t2.ITEMOVDESC, t2.QUANTITY, t2.UNITPRICE, t2.ITEMRESERVED10, t1.DESCRIPTION, t1.PROCEED\n" +
            "            FROM PO t1\n" +
            "            INNER JOIN PODET t2 ON t1.POID = t2.POID\n" +
            "            INNER JOIN ITEM t3 ON t2.ITEMNO = t3.ITEMNO\n" +
            "            INNER JOIN PERSONDATA t4 ON t4.ID = t1.VENDORID\n" +
            "            WHERE LOWER(t4.\"NAME\") like LOWER('%$keyword%') or LOWER(t2.ITEMOVDESC) like LOWER('%$keyword%') or LOWER(t1.PONO) like LOWER('%$keyword%') or LOWER(t1.DESCRIPTION) like LOWER('%$keyword%')\n" +
            "            order by t1.POID desc"
    val tempMap = mutableMapOf<String, MutableList<String>>()
    val resultSetDatatoko = Constants.s!!.executeQuery(dataquery)
    while (resultSetDatatoko.next()) {
        val poid = resultSetDatatoko.getString("POID")
        val pono = resultSetDatatoko.getString("PONO")
        val podate = resultSetDatatoko.getString("PODATE")
        val name = resultSetDatatoko.getString("NAME")
        val desc = resultSetDatatoko.getString("DESCRIPTION")
        val procced = resultSetDatatoko.getString("PROCEED")


        // Menambahkan data ke dalam map dengan ID sebagai kunci
        if (!tempMap.containsKey(poid)) {
            tempMap[poid] = mutableListOf(pono ?: "", podate ?: "", name ?: "", desc ?: "", procced ?: "")
        } else {
            tempMap[poid]?.addAll(listOf(pono ?: "", podate ?: "", name ?: "", desc ?: "", procced ?: ""))
        }
    }
    println("tmp data : $tempMap    ")


    for ((poid, values) in tempMap) {
        if (values.size % 5 == 0) {
            val poDatatokoObj = showPO(poid, values[0], values[1], values[2], values[3], values[4])
            showallpoList.add(poDatatokoObj)
        }
    }

    return showallPO(showallpoList)
}