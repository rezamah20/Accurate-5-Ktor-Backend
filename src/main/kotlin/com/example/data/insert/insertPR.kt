package com.example.data.insert

import com.example.models.Constants
import com.example.models.Constants.c
import com.example.models.Constants.rs
import com.example.models.Constants.s
import com.example.models.insertPR
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun insertPR(insertPR: insertPR):String{
    var messege = ""

    try {
        c = DriverManager.getConnection(Constants.db_name, Constants.user, Constants.password)
        s = c?.createStatement()
    }catch (e: Exception){

    }

    try {

        val gettransid = "select TransactionID from ADDTRANSACTIONSHITORY(79, 1, '', 1, Null, 0)"

        val resultSettrid = s!!.executeQuery(gettransid)
        var transidinsrt = ""
        if (resultSettrid.next()) {
            transidinsrt = resultSettrid.getString("TRANSACTIONID")
        }

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        val inserttrans = "INSERT INTO TRANSHISTORY (TRANSACTIONID, TRANSTYPE, CREATEDTIME, LASTMODIFYTIME, HISTORYSTATE, HISTORYMESSAGE, BRANCHCODEID, REFERENCE, STATUS, DEL_TRANSACTIONID, USERID) VALUES ('$transidinsrt', '79', '$formattedDateTime.000', '$formattedDateTime.000', '-1', NULL, '1', '', '1', NULL, '0');"
        c!!.prepareStatement(inserttrans).executeUpdate()

        val insertPRquery = "INSERT INTO REQUISITION (REQID, REQNO, REQDATE, TEMPLATEID, ISCLOSED, BRANCHCODEID, TRANSACTIONID, IMPORTEDTRANSACTIONID, DESCRIPTION) VALUES ('${insertPR.prmodel.REQID}', '${insertPR.prmodel.REQNO}', '${insertPR.prmodel.REQDATE}', '45', '0', '1', '$transidinsrt', NULL, NULL);"
        c!!.prepareStatement(insertPRquery).executeUpdate()

        var itemprdata = insertPR.prmodeldet
        itemprdata.forEach{
            val insertPRdata = "INSERT INTO REQUISITIONDET (REQID, SEQ, ITEMNO, ITEMOVDESC, QUANTITY, ITEMUNIT, UNITRATIO, QTYORDERED, QTYRECEIVED, REQDATE, ITEMRESERVED1, ITEMRESERVED2, ITEMRESERVED3, ITEMRESERVED4, ITEMRESERVED5, ITEMRESERVED6, ITEMRESERVED7, ITEMRESERVED8, ITEMRESERVED9, ITEMRESERVED10, NOTES, PROJECTID, JOBID, DEPTID) \n" +
                    "VALUES ('${insertPR.prmodel.REQID}', '${it.SEQ}', '${it.ITEMNO}', '${it.ITEMOVDESC}', '${it.QUANTITY}', NULL, '1.0000', '0.0000', '0.0000', '${insertPR.prmodel.REQDATE}', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '${it.ITEMRESERVED10}', '${it.NOTES}', NULL, NULL, NULL);\n"
            println(insertPRdata)
            c!!.prepareStatement(insertPRdata).executeUpdate()
        }
    }catch (e: SQLException){
        messege =  "Error SQL $e"
        println(messege)
        return messege
    }finally {
        try {
            //c?.commit()
            rs?.close()
            s?.close()
            c?.close()
            messege = "Data PR berhasil di Simpan"
        }catch (e: SQLException){
            messege = "Data PR gagal di Simpan $e"
        }
    }
    return messege
}