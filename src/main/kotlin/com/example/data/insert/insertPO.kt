package com.example.data.insert

import com.example.models.*
import com.example.models.Constants.c
import com.example.models.Constants.db_name
import com.example.models.Constants.rs
import com.example.models.Constants.s
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

/**
* Insert into table : po, podet, audit
* and will update table nextnumberinvoice
 */
suspend fun insertPO(intanpo: intanPO):String{
    var messege = ""

    try {
        c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
        c?.setAutoCommit (false)
        s = c?.createStatement()

        var termidresultset = "SELECT r.TERMSID\n" +
                "FROM PERSONDATA r\n" +
                "where r.ID = ${intanpo.pomodel.VENDORID}"
        val resultSettermid = s!!.executeQuery(termidresultset)
        var termid =""
        if (resultSettermid.next()){
            termid = resultSettermid.getString("TERMSID")
        }

        val poitem = intanpo.podetmodel.size
        var insertpo = "INSERT INTO PO (POID, PONO, VENDORID, PODATE, GLPERIOD, GLYEAR, SHIPTO1, EXPECTED, PROCEED, CLOSED, POAMOUNT, DP, DPACCOUNT, CASHDISCOUNT, CASHDISCPC, GLHISTID, TEMPLATEID, RATE, DPUSED, TERMID, FREIGHT, TAX1AMOUNT, TAX2AMOUNT, FREIGHTTOCOST, INCLUSIVETAX, VENDORISTAXABLE, CHEQUEDATE, FREIGHTTOVENDOR, ROUNDEDTAX1AMOUNT, ROUNDEDTAX2AMOUNT, RATETYPE, TRANSACTIONID, BRANCHCODEID, DESCRIPTION) " +
                "VALUES ('${intanpo.pomodel.POID}', '${intanpo.pomodel.PONO}', '${intanpo.pomodel.VENDORID}', '${intanpo.pomodel.PODATE}', '${intanpo.pomodel.GLPERIOD}', '${intanpo.pomodel.GLYEAR}', Null, '${intanpo.pomodel.EXPECTED}', '0', '0', '${intanpo.pomodel.POAMOUNT}', '0.0000', '110402', '0.0000', '0', '${intanpo.pomodel.GLHISTID}', '26', '1.0000', '0.0000', '$termid', '0.0000', '0.0000', '0.0000', '0', '0', '0', '${intanpo.pomodel.CHEQUEDATE}', '0', '0.0000', '0.0000', '0', '${intanpo.pomodel.TRANSACTIONID}', '1', '${intanpo.pomodel.DESCRIPTION}');"
        var updatenextinv = "UPDATE NEXTINVOICENUMBER SET NEXTNUMBER = '${intanpo.nextNumberInvoice.NEXTNUMBER}'\n" +
                "WHERE MODULEID = '1';"
        var insertaudit = "INSERT INTO AUDIT (AUDITID, USERID, \"SOURCE\", TRANSTYPE, GLHISTID, TRANSDESCRIPTION, MODIDATE, COUPLEID, STATUS, INVOICENO, COMP_NAME, IPADDRESS, APPVERSION) " +
                "VALUES ('${intanpo.audit.AUDITID}', '0', 'AC', 'POR', '-1', '${intanpo.audit.TRANSDESCRIPTION}', '${intanpo.audit.MODIDATE}', '${intanpo.audit.COUPLEID}', '0', '${intanpo.audit.INVOICENO}', NULL, NULL, '5.0.22.1903');"

       // println("size poitem $poitem")
       // println("size poitem $insertpo")
        c!!.prepareStatement(insertpo).executeUpdate()
        c!!.prepareStatement(insertaudit).executeUpdate()
        c!!.prepareStatement(updatenextinv).executeUpdate()
        var itempodata = intanpo.podetmodel
        if (c != null){
            itempodata.forEach{
                var insertpodet ="INSERT INTO PODET (POID, SEQ, ITEMNO, ITEMOVDESC, QUANTITY, QTYRECV, UNITPRICE, ITEMUNIT, UNITRATIO, CLOSED, TAXCODES, ITEMDISCPC, BRUTOUNITPRICE, TAXABLEAMOUNT1, TAXABLEAMOUNT2, DEPTID, PROJECTID, ITEMRESERVED1, ITEMRESERVED2, ITEMRESERVED3, ITEMRESERVED4, ITEMRESERVED5, ITEMRESERVED6, ITEMRESERVED7, ITEMRESERVED8, ITEMRESERVED9, ITEMRESERVED10, REQID, REQSEQ, GROUPSEQ, JOBID) " +
                            "VALUES ('${it.POID}', '${it.SEQ}', '${it.ITEMNO}', '${it.ITEMOVDESC}', '${it.QUANTITY}', '0.0000', '${it.UNITPRICE}', NULL, '1.0000', '0', NULL, NULL, '${it.BRUTOUNITPRICE}', '0.0000', '0.0000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '${it.ITEMRESERVED10}', NULL, NULL, NULL, NULL);"
                c!!.prepareStatement(insertpodet).executeUpdate()
            }
        }
//        println("resume")

    }catch (e: SQLException){
        messege =  "Error SQL $e"
      //  println(messege)
        return messege
    }finally {
        try {
            c?.commit()
            rs?.close()
            s?.close()
            c?.close()
            messege = "Data berhasil di Simpan"
        }catch (e: SQLException){
            messege = "Data gagal di Simpan $e"
        }
    }
    //println(messege)
    return messege
}

suspend fun insertPoDet(intanpo: intanPO){


//    if (c != null){
//        val rowinsertPO = s_po.executeUpdate()
//        var rowinsertPOdet = 0
//        var rowinsertAudit: Int
//        var rowupdateinv = 0
//        if (rowinsertPO >= 1){
//            println("inset to podet")
//            for (i in 0 until poitem){
//                val currentPO = intanpo.podetmodel[i]
//                var insertpodet ="INSERT INTO PODET (POID, SEQ, ITEMNO, ITEMOVDESC, QUANTITY, QTYRECV, UNITPRICE, ITEMUNIT, UNITRATIO, CLOSED, TAXCODES, ITEMDISCPC, BRUTOUNITPRICE, TAXABLEAMOUNT1, TAXABLEAMOUNT2, DEPTID, PROJECTID, ITEMRESERVED1, ITEMRESERVED2, ITEMRESERVED3, ITEMRESERVED4, ITEMRESERVED5, ITEMRESERVED6, ITEMRESERVED7, ITEMRESERVED8, ITEMRESERVED9, ITEMRESERVED10, REQID, REQSEQ, GROUPSEQ, JOBID) " +
//                        "VALUES ('${currentPO.POID}', '${currentPO.SEQ}', '${currentPO.ITEMNO}', '${currentPO.ITEMOVDESC}', '${currentPO.QUANTITY}', '0.0000', '${currentPO.UNITPRICE}', NULL, '1.0000', '0', NULL, NULL, '${currentPO.BRUTOUNITPRICE}', '0.0000', '0.0000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '${currentPO.ITEMRESERVED10}', NULL, NULL, NULL, NULL);"
//                s_podet = c!!.prepareStatement(insertpodet)
//                rowinsertPOdet = s_podet!!.executeUpdate()
//            } }
//    }
}

suspend fun insertTBPoDet(intanpo: intanPO){
    try {
        c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
        s = c?.createStatement()
    }catch (e: Exception){

    }
    var itempodata = intanpo.podetmodel

    try {
        itempodata.forEach{
            var insertpodet ="INSERT INTO PODET (POID, SEQ, ITEMNO, ITEMOVDESC, QUANTITY, QTYRECV, UNITPRICE, ITEMUNIT, UNITRATIO, CLOSED, TAXCODES, ITEMDISCPC, BRUTOUNITPRICE, TAXABLEAMOUNT1, TAXABLEAMOUNT2, DEPTID, PROJECTID, ITEMRESERVED1, ITEMRESERVED2, ITEMRESERVED3, ITEMRESERVED4, ITEMRESERVED5, ITEMRESERVED6, ITEMRESERVED7, ITEMRESERVED8, ITEMRESERVED9, ITEMRESERVED10, REQID, REQSEQ, GROUPSEQ, JOBID) " +
                    "VALUES ('${it.POID}', '${it.SEQ}', '${it.ITEMNO}', '${it.ITEMOVDESC}', '${it.QUANTITY}', '0.0000', '${it.UNITPRICE}', NULL, '1.0000', '0', NULL, NULL, '${it.BRUTOUNITPRICE}', '0.0000', '0.0000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '${it.ITEMRESERVED10}', NULL, NULL, NULL, NULL);"
            c!!.prepareStatement(insertpodet).executeUpdate()
        }
    }catch (e: Exception){
        println("gagal menyimpan $e")
    }finally {
        try {
            c?.commit()
            rs?.close()
            s?.close()
            c?.close()
            print("Data berhasil di Simpan")
        }catch (e: SQLException){
            println("Data gagal di Simpan $e")
        }
    }
}