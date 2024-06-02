package com.example.data.update

import com.example.models.Constants
import com.example.models.Constants.c
import com.example.models.Constants.db_name
import com.example.models.Constants.s
import com.example.models.dataItem
import com.example.models.poDatatoko
import com.example.models.showitemPO
import java.sql.DriverManager
import java.sql.SQLException

fun updateItemPO(podataitem: showitemPO):String{
    var message = ""
    try {
        c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
        s = c?.createStatement()
        message = "Success Connect DB"
    }catch (e: Exception){
        message = "Error connect DB $e"
    }
    var dataitem = podataitem.showItem
    try {
        dataitem.forEach {
            val checkdata = "SELECT COUNT(*) AS TOTALITEM \n" +
                    "FROM PO p inner join PODET d on p.POID = d.POID\n" +
                    "WHERE d.POID = '${it.POID}' AND d.SEQ = '${it.SEQ}'"
            val resultSetCekItem = s!!.executeQuery(checkdata)
            var totalitem= ""
            if (resultSetCekItem != null && resultSetCekItem.next()){
                totalitem = resultSetCekItem.getString("TOTALITEM")
                if (totalitem.toInt() == 0){
                    try {
                        //     dataitem.forEach{
                        var insertUpdateitem = "INSERT INTO PODET (POID, SEQ, ITEMNO, ITEMOVDESC, QUANTITY, QTYRECV, UNITPRICE, ITEMUNIT, UNITRATIO, CLOSED, TAXCODES, ITEMDISCPC, BRUTOUNITPRICE, TAXABLEAMOUNT1, TAXABLEAMOUNT2, DEPTID, PROJECTID, ITEMRESERVED1, ITEMRESERVED2, ITEMRESERVED3, ITEMRESERVED4, ITEMRESERVED5, ITEMRESERVED6, ITEMRESERVED7, ITEMRESERVED8, ITEMRESERVED9, ITEMRESERVED10, REQID, REQSEQ, GROUPSEQ, JOBID) " +
                                "VALUES ('${it.POID}', '${it.SEQ}', '${it.ITEMNO}', '${it.ITEMOVDESC}', '${it.QUANTITY}', '0.0000', '${it.UNITPRICE}', NULL, '1.0000', '0', NULL, NULL, '${it.BRUTOUNITPRICE}', '0.0000', '0.0000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '${it.ITEMRESERVED10}', NULL, NULL, NULL, NULL);"
                        s!!.executeUpdate(insertUpdateitem)
                        //      }
                        message = "Succes Inpute New Data Item"
                    }catch (e: SQLException){
                        message = "Gagal Insert PO $e"
                    }
                }else if (totalitem.toInt() > 0){
                    println("total po ${it.ITEMNO}")
                    val updateitem = "UPDATE PODET SET \n" +
                            "POID = '${it.POID}', \n" +
                            "SEQ = '${it.SEQ}', \n" +
                            "ITEMNO = '${it.ITEMNO}', \n" +
                            "ITEMOVDESC = '${it.ITEMOVDESC}', \n" +
                            "QUANTITY = '${it.QUANTITY}', \n" +
                            "UNITPRICE = '${it.UNITPRICE}', \n" +
                            "BRUTOUNITPRICE = '${it.BRUTOUNITPRICE}', \n" +
                            "ITEMRESERVED10 = '${it.ITEMRESERVED10}'\n" +
                            "WHERE POID = '${it.POID}' AND SEQ = '${it.SEQ}';\n"
                    try {
                        s!!.executeUpdate(updateitem)
                        message = "Success Update Data"
                    }catch (e: SQLException){
                        message = "Gagal Update Data $e"
                        println("Gagal Update data $e")
                    }
                }
            }
        }
    }catch (e: Exception){

    }finally {

    }

    return message
}

fun updateAllDataPo(showitempo: showitemPO):String{
    var message = ""

    try {
        c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
        s = c?.createStatement()
        message = "Success Connect DB"
    }catch (e: Exception){
        message = "Error connect DB $e"
    }
    println("ini isi item: ${showitempo.pomodel}")

    val checkdatapo = "SELECT COUNT(*) AS TOTALPO FROM PO p WHERE p.POID = '${showitempo.pomodel.POID}' AND p.PONO = '${showitempo.pomodel.PONO}'"

    val resultSetCekPo = s!!.executeQuery(checkdatapo)
    var totalpo = ""
    if (resultSetCekPo != null && resultSetCekPo.next()){
        totalpo = resultSetCekPo.getString("TOTALPO")
        if (totalpo.toInt() > 0 ){
            try {
                var updatepo = "UPDATE PO SET VENDORID = '${showitempo.pomodel.VENDORID}', DESCRIPTION = '${showitempo.pomodel.DESCRIPTION}' WHERE POID = '${showitempo.pomodel.POID}'"
                s!!.executeUpdate(updatepo)
                updateItemPO(showitempo)
                message = "Success Update PO ${showitempo.pomodel.PONO}"
                println(message)
            }catch (e:SQLException){
                println("error update $e")
                message = "Gagal Update po $e"
            }
        }else{
            message = "Gagal update PO - Tidak ditemukan"
            println(message)
        }
    }
    return  message
}