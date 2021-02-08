(window.webpackJsonp=window.webpackJsonp||[]).push([[39],{INPU:function(e,t,n){"use strict";n.r(t),n.d(t,"_frontmatter",(function(){return b})),n.d(t,"default",(function(){return i}));var r=n("wx14"),c=n("zLVn"),o=(n("q1tI"),n("7ljp")),p=n("013z"),b=(n("qKvR"),{}),s={_frontmatter:b},a=p.a;function i(e){var t=e.components,n=Object(c.a)(e,["components"]);return Object(o.b)(a,Object(r.a)({},s,n,{components:t,mdxType:"MDXLayout"}),Object(o.b)("p",null,"These are the tips and tricks working with the IBM FHIR Server. "),Object(o.b)("h1",null,"DB2 Error 42501"),Object(o.b)("p",null,"If you see the 42501 error in the fhir-server logs, this section walks you through how to confirm and verify the permissions: "),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre"},"42501   The authorization ID does not have the privilege to perform the specified operation on the identified object.\n")),Object(o.b)("p",null,"Check the Stored Procedure and object mapping"),Object(o.b)("p",null,Object(o.b)("inlineCode",{parentName:"p"},"SELECT * FROM syscat.procedures WHERE procschema = 'FHIRDATA'")),Object(o.b)("p",null,"The response shows the SQL objects (third column): "),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre"}," SCHEMA     TABLE              OBJECTNAME\n FHIRDATA   ADD_CODE_SYSTEM    SQL191105170018025        84725 BLUADMIN          2 0x38001800                             Q      2019-11-05\n FHIRDATA   ADD_PARAMETER_NAME SQL191105170018126        84726 BLUADMIN          2 0x38001800                             Q      2019-11-05\n FHIRDATA   ADD_ANY_RESOURCE   SQL191105170018328        84728 BLUADMIN          9 0x380038007c006c003c003800380018001400 Q      2019-11-05\n")),Object(o.b)("p",null,"Check if the FHIRSERVER user has execute privelege on the objectname. "),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre"},"select substr(authid,1,20) as authid\n    , authidtype\n    , privilege\n    , grantable\n    , substr(objectschema,1,12) as objectschema\n    , substr(objectname,1,30) as objectname\n    , objecttype \nfrom sysibmadm.privileges\nwhere objectschema not like 'SYS%' AND AUTHID='FHIRSERVER' AND PRIVILEGE = 'EXECUTE' \n")),Object(o.b)("p",null,"If missing, then you’ll want to run a grant\n",Object(o.b)("inlineCode",{parentName:"p"},"GRANT EXECUTE ON PROCEDURE FHIRDATA.ADD_ANY_RESOURCE TO FHIRSERVER")),Object(o.b)("p",null,"Confirm the procedure and re-execute.  "),Object(o.b)("p",null,Object(o.b)("a",{parentName:"p",href:"https://www.ibm.com/support/knowledgecenter/en/SSEPEK_10.0.0/seca/src/tpc/db2z_grantprivilege4executesp.html"},"https://www.ibm.com/support/knowledgecenter/en/SSEPEK_10.0.0/seca/src/tpc/db2z_grantprivilege4executesp.html")))}i.isMDXComponent=!0}}]);
//# sourceMappingURL=component---src-pages-tips-and-tricks-mdx-0260d872a30efe69966a.js.map