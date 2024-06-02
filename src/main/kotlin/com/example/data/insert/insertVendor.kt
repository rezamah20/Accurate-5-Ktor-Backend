package com.example.data.insert

import com.example.models.Constants
import com.example.models.Constants.c
import com.example.models.Constants.password
import com.example.models.Constants.rs
import com.example.models.Constants.s
import com.example.models.Constants.user
import com.example.models.persondata
import java.sql.DriverManager
import java.sql.SQLException

fun insertVendor(insertPR: persondata):String{
    var messege = ""
    try {
        c = DriverManager.getConnection(Constants.db_name, user, password)
        s = c?.createStatement()
    }catch (e: Exception){
        println("errror connect $e")
    }

    val personidQuery ="Select PersonID from GetPersonID"
    val resultSettrpid = s!!.executeQuery(personidQuery)
    var personidinsrt = ""
    if (resultSettrpid.next()){
        personidinsrt = resultSettrpid.getString("PERSONID")
    }

    val nextnumberprno = "SELECT r.NEXTNUMBER\n" +
            "FROM NEXTINVOICENUMBER r\n" +
            "where r.MODULEID = 27\n"
    val resultsetpersonno = s!!.executeQuery(nextnumberprno)
    var personno = ""
    if (resultsetpersonno.next()){
        personno = resultsetpersonno.getString("NEXTNUMBER")
    }

    try {
        val persondataQuery = "INSERT INTO PERSONDATA (ID, PERSONNO, PERSONTYPE, BALANCE, \"NAME\", ADDRESSLINE1, ADDRESSLINE2, CITY, STATEPROV, ZIPCODE, COUNTRY, CONTACT, FAX, EMAIL, WEBPAGE, SUSPENDED, BILLTONO, TAX1ID, TAX2ID, BILLTOONLY, TAX1EXEMPTIONNO, TAX2EXEMPTIONNO, CURRENCYID, CREDITLIMIT, TERMSID, PRINTSTATEMENT, ALLOWBACKORDERS, SALESMANID, CUSTOMERTYPEID, PERSONMESSAGE, DEFAULTINVDESCRIPTION, NOTES, PRICELEVEL, DEFAULTDISC, TAXTYPE, TAXADDRESS1, TAXADDRESS2, TRANSACTIONID, IMPORTEDTRANSACTIONID, BRANCHCODEID, CHRRESERVED1, CHRRESERVED2, CHRRESERVED3, CHRRESERVED4, CHRRESERVED5, CHRRESERVED6, CHRRESERVED7, CHRRESERVED8, CHRRESERVED9, CHRRESERVED10, CURRRESERVED1, CURRRESERVED2, CURRRESERVED3, DATERESERVED1, DATERESERVED2, CREDITLIMITDAYS, DEFAULTINCLUSIVETAX, PHONE, NIK)" +
                "VALUES ('$personidinsrt', '$personno', '1', '0.0000', '${insertPR.NAME}', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, '151', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL);\n"
        c!!.prepareStatement(persondataQuery).executeUpdate()

        val updatepersonnoQuery = "UPDATE NEXTINVOICENUMBER r\n" +
                "SET r.NEXTNUMBER = '${personno.toInt()+1}'" +
                "WHERE r.MODULEID = 27;"
        c!!.prepareStatement(updatepersonnoQuery).executeUpdate()

    }catch (e:SQLException){
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
