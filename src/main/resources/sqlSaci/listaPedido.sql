SELECT P.storeno                                                                 AS lojaOrigem,
       S.no                                                                      AS lojaDestino,
       P.ordno                                                                   AS numPedido,
       IF(P.date = 0, NULL, cast(P.date AS DATE))                                AS dataPedido,
       SEC_TO_TIME(P.l4)                                                         AS horaPedido,

       P.paymno                                                                  AS metodo,

       IFNULL(cast(N.nfno AS CHAR), '')                                          AS nfnoNota,
       IFNULL(N.nfse, '')                                                        AS nfseNota,
       if(N.issuedate = 0, NULL, cast(N.issuedate AS DATE))                      AS dataNota,
       sec_to_time(N2.auxLong4)                                                  AS horaNota,
       TRIM(CONCAT(TRIM(MID(IFNULL(remarks__480, ' '), 1, 15)), ' ',
		   TRIM(RPAD(MID(IFNULL(remarks__480, ' '), 41, 56), 15, ' ')), ' ',
		   TRIM(RPAD(MID(IFNULL(remarks__480, ' '), 81, 99), 17, ' ')), ' ',
		   TRIM(RPAD(MID(IFNULL(remarks__480,' '),121,160),40,' ')))) AS obs,
       IFNULL(U.name, '')                                                        AS username
FROM sqldados.eord           AS P
  LEFT JOIN  sqlpdv.pxa      AS PX
	       ON (P.storeno = PX.storeno AND P.ordno = PX.eordno)
  INNER JOIN sqldados.custp  AS C
	       ON C.no = P.custno
  INNER JOIN sqldados.store  AS S
	       ON C.cpf_cgc = S.cgc
  LEFT JOIN  sqldados.nf     AS N
	       ON N.storeno = P.storeno AND N.nfno = P.nfno AND N.nfse = P.nfse
  LEFT JOIN  sqldados.nf2    AS N2
	       ON N.storeno = N2.storeno AND N.pdvno = N2.pdvno AND N.xano = N2.xano
  LEFT JOIN  sqldados.eordrk AS R
	       ON (R.storeno = P.storeno AND R.ordno = P.ordno)
  LEFT JOIN  sqldados.users  AS U
	       ON U.no = P.userno
WHERE P.paymno = 69
  AND P.date >= 20200601
  AND P.status <> 5
  AND (P.storeno = :storeno OR :storeno = 0)
GROUP BY P.ordno, P.storeno