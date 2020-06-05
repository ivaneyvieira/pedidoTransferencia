SELECT N.storeno                                            AS lojaOrigem,
       S.no                                                 AS lojaDestino,
       N.eordno                                             AS numPedido,
       NULL                                                 AS dataPedido,
       NULL                                                 AS horaPedido,

       N.paymno                                             AS metodo,

       IFNULL(cast(N.nfno AS CHAR), '')                     AS nfnoNota,
       IFNULL(N.nfse, '')                                   AS nfseNota,
       if(N.issuedate = 0, NULL, cast(N.issuedate AS DATE)) AS dataNota,
       sec_to_time(N2.auxLong4)                             AS horaNota,
       N.remarks                                            AS obs
FROM sqldados.nf            AS N
  INNER JOIN sqldados.custp AS C
	       ON C.no = N.custno
  INNER JOIN sqldados.store AS S
	       ON C.cpf_cgc = S.cgc
  LEFT JOIN  sqldados.nf2   AS N2
	       ON N.storeno = N2.storeno AND N.pdvno = N2.pdvno AND N.xano = N2.xano
WHERE N.issuedate >= 20200601
  AND N.paymno = 69
ORDER BY N.issuedate DESC