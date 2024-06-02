package com.example.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class intanPO(
    //@SerialName("POmodel")
    val pomodel: POmodel,
    //@SerialName("POdetmodel")
    val podetmodel: List<POdetmodel>,
    //@SerialName("Audit")
    val audit: Audit,
    //@SerialName("nextNumberInvoice")
    val nextNumberInvoice: nextNumberInvoice)

@Serializable
data class pointanData(val podata: poData, val audit: Audit, val nextNumberInvoice: nextNumberInvoice, val poDatatoko : List<poDatatoko>, val itemPO: List<itemPO>)

@Serializable
data class showallPO (val showPO: List<showPO>)

@Serializable
data class  showitemPO( val pomodel: POmodel, val showItem: List<showItem>)

@Serializable
data class dataToko(val poDatatoko : List<poDatatoko>)

@Serializable
data class dataItem(val poDataitem : List<itemPO>)

//@Serializable
//data class updatePoItem(val )

//PR
@Serializable
data class insertPR(
    val prmodel: PRmodel,
    val prmodeldet: List<PRmodeldet>
)

@Serializable
data class listPRmodel(
    val lsprmodel: List<PRmodel>
)
@Serializable
data class POmodel(
    val POID: String = "",
    val PONO: String = "",
    val VENDORID:String = "",
    val PODATE: String = "",
    val GLPERIOD: String = "",
    val GLYEAR:String = "",
    val SHIPTO1: String = "",
    val SHIPTO2: String = "",
    val SHIPTO3:String = "",
    val SHIPTO4: String = "",
    val SHIPTO5: String = "",
    val EXPECTED:String = "",
    val SHIPVIA:String = "",
    val PROCEED: String = "",
    val CLOSED: String = "",
    val POAMOUNT:String = "",
    val DP: String = "",
    val DPACCOUNT: String = "",
    val CASHDISCOUNT:String = "",
    val CASHDISCPC: String = "",
    val GLHISTID: String = "",
    val TEMPLATEID:String = "",
    val PAIDFROM: String = "",
    val RATE: String = "",
    val DPUSED:String = "",
    val LOCKED_BY: String = "",
    val LOCKED_TIME: String = "",
    val TERMID:String = "",
    val TAX1ID: String = "",
    val TAX2ID: String = "",
    val FREIGHT:String = "",
    val TAX1RATE: String = "",
    val TAX2RATE: String = "",
    val TAX1AMOUNT:String = "",
    val TAX2AMOUNT: String = "",
    val TAX1CODE: String = "",
    val TAX2CODE:String = "",
    val FREIGHTTOCOST: String = "",
    val FOB: String = "",
    val INCLUSIVETAX:String = "",
    val VENDORISTAXABLE: String = "",
    val CHEQUEDATE: String = "",
    val CHEQUENO:String = "",
    val FREIGHTACCNT: String = "",
    val SHIPVENDID: String = "",
    val FREIGHTTOVENDOR:String = "",
    val ROUNDEDTAX1AMOUNT: String = "",
    val ROUNDEDTAX2AMOUNT: String = "",
    val GETFROMREQUEST:String = "",
    val RATETYPE: String = "",
    val TRANSACTIONID:String = "",
    val IMPORTEDTRANSACTIONID: String = "",
    val BRANCHCODEID: String = "",
    val DESCRIPTION:String = "",
)

@Serializable
data class POdetmodel(
    val POID: String = "",
    val SEQ: String = "",
    val ITEMNO:String ="",
    val ITEMOVDESC: String = "",
    val QUANTITY: String = "",
    val QTYRECV:String ="",
    val UNITPRICE: String = "",
    val ITEMUNIT: String = "",
    val UNITRATIO:String ="",
    val CLOSED: String = "",
    val TAXCODES: String = "",
    val ITEMDISCPC:String ="",
    val BRUTOUNITPRICE: String = "",
    val TAXABLEAMOUNT1: String = "",
    val TAXABLEAMOUNT2:String ="",
    val DEPTID: String = "",
    val PROJECTID: String = "",
    val ITEMRESERVED1:String ="",
    val ITEMRESERVED2: String = "",
    val ITEMRESERVED3: String = "",
    val ITEMRESERVED4:String ="",
    val ITEMRESERVED5: String = "",
    val ITEMRESERVED6: String = "",
    val ITEMRESERVED7:String ="",
    val ITEMRESERVED8: String = "",
    val ITEMRESERVED9: String = "",
    val ITEMRESERVED10:String ="",
    val REQID: String = "",
    val REQSEQ: String = "",
    val GROUPSEQ:String ="",
    val JOBID: String = "",

    )

@Serializable
data class Audit(
    val AUDITID: String = "",
    val USERID: String = "",
    val SOURCE:String ="",
    val TRANSTYPE: String = "",
    val GLHISTID: String = "",
    val TRANSDESCRIPTION:String ="",
    val MODIDATE: String = "",
    val COUPLEID: String = "",
    val STATUS:String ="",
    val INVOICENO: String = "",
    val COMP_NAME: String = "",
    val IPADDRESS:String ="",
    val APPVERSION: String = "",
    )

@Serializable
data class nextNumberInvoice(
    val NEXTNUMBER: String = "",
)

@Serializable
data class poData(
    val POID: String ="",
    val PONO: String = "",
    val DATE: String="",
    val GLPRIOD: String="",
    val GLYEAR: String="",
    val CHEQUEDATE: String= "",
    val GLHISTID: String = "",
    val TRANSACTIONID: String = ""
)

@Serializable
data class poDatatoko(
    val ID:String = "",
    val PERSONNO:String = "",
    val PERSONTYPE:String ="",
    val NAME:String = ""
)

@Serializable
data class itemPO(
    val ITEMNO:String = "",
    val ITEMDESCRIPTION:String = "",
    val ITEMTYPE:String ="",
    val SUBITEM:String = ""
)

@Serializable
data class showPO(
    val POID: String = "",
    val PONO: String = "",
    val PODATE: String = "",
    val NAME: String = "",
    val DESCRIPTION: String ="",
    val PROCEED: String = ""
)

@Serializable
data class showItem(
    val ID: String = "",
    val POID: String = "",
    val SEQ: String = "",
    val PONO: String = "",
    val NAME: String = "",
    val PODATE: String = "",
    val ITEMNO: String = "",
    val ITEMDESCRIPTION: String = "",
    val ITEMOVDESC: String = "",
    val QUANTITY: String = "",
    val UNITPRICE: String = "",
    val ITEMRESERVED10: String = "",
    val DESCRIPTION: String = "",
    val BRUTOUNITPRICE: String = "",
)

@Serializable
data class firstdataPR(
    val PRID: String = "",
    val PRNO: String = ""
)
@Serializable
data class PRmodel(
    val REQID: String = "",
    val REQNO: String = "",
    val REQDATE: String = "",
    val TEMPLATEID: String = "",
    val ISCLOSE:String = "",
    val BRANCHCODEID: String ="",
    val TRANSACTIONID: String = "",
    val IMPORTEDTRANSACTIONID: String = "",
    val DESCRIPTION: String = ""
)
@Serializable
data class PRmodeldet(
    val REQID: String = "",
    val SEQ: String = "",
    val ITEMNO: String = "",
    val ITEMOVDESC: String = "",
    val QUANTITY: String = "",
    val ITEMUNIT: String = "",
    val UNITRATIO: String = "",
    val QYTORDERED: String = "",
    val QYTRECEIVED: String = "",
    val REQDATE:String = "",
    val ITEMRESERVED1:String = "",
    val ITEMRESERVED2:String = "",
    val ITEMRESERVED3:String = "",
    val ITEMRESERVED4:String = "",
    val ITEMRESERVED5:String = "",
    val ITEMRESERVED6:String = "",
    val ITEMRESERVED7:String = "",
    val ITEMRESERVED8:String = "",
    val ITEMRESERVED9:String = "",
    val ITEMRESERVED10:String = "",
    val NOTES:String = "",
    val PROJECTID:String = "",
    val JOBID: String = "",
    val DEPTID: String =""
)

@Serializable
data class persondata(
    val ID: String ="",
    val PERSONNO: String = "",
    val PERSONTYPE: String = "",
    val BALANCE: String = "",
    val NAME: String = "",
    val ADDRESSLINE: String = "",
    val ADDRESSLINE2: String = "",
    val CITY:String = "",
    val STATEPROV:String = "",
    val ZIPCODE:String = "",
    val COUNTRY:String = "",
    val CONTACT:String = "",
    val FAX:String = "",
    val EMAIL:String = "",
    val WEBPAGE:String = "",
    val SUSPENDED:String = "",
    val BILLTONO:String = "",
    val TAX1ID:String = "",
    val TAX2ID:String = "",
    val TAX1EXEMPTIONNO:String = "",
    val TAX2EXEMPTIONNO:String = "",
    val CURRENCYID:String = "",
    val CREDITLIMIT:String = "",
    val TERMSID:String = "",
    val PRINTSTATEMENT:String = "",
    val ALLOWBACKORDERS:String = "",
    val SALESMANID:String = "",
    val CUSTOMERTYPEID:String = "",
    val PERSONMESSAGE:String = "",
    val DEFAULTINVDESCRIPTION:String = "",
    val NOTES:String = "",
    val TAXTYPE:String = "",
    val TAXADDRESS1:String = "",
    val TAXADDRESS2:String = "",
    val TRANSACTIONID:String = "",
    val IMPORTEDTRANSACTIONID:String = "",
    val BRANCHCODEID:String = "",
    val CHRRESERVED1:String = "",
    val CHRRESERVED2:String = "",
    val CHRRESERVED3:String = "",
    val CHRRESERVED4:String = "",
    val CHRRESERVED5:String = "",
    val CHRRESERVED6:String = "",
    val CHRRESERVED7:String = "",
    val CHRRESERVED8:String = "",
    val CHRRESERVED9:String = "",
    val CHRRESERVED10:String = "",
    val CURRRESERVED1:String = "",
    val CURRRESERVED2:String = "",
    val DATERESERVED1:String = "",
    val DATERESERVED2:String = "",
    val CREDITLIMITDAYS:String = "",
    val DEFAULTINCLUSIVETAX:String = "",
    val PHONE:String = "",
    val NIK:String = ""

    )