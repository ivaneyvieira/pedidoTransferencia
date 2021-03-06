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
       N.remarks                                            AS obs,
       ''                                                   AS nat,
       ''                                                   AS doc,
       ''                                                   AS ent,
       ''                                                   AS rec,
       IFNULL(U.name, '')                                   AS username,
       'S'                                                  AS marca,
       CASE
	 WHEN :storeno = 0
	   THEN 'TODOS'
	 WHEN N.storeno = :storeno
	   THEN 'ORIGEM'
	 WHEN S.no = :storeno
	   THEN 'DESTINO'
	 ELSE ''
       END                                                  AS tipo
FROM sqldados.nf            AS N
  INNER JOIN sqldados.custp AS C
	       ON C.no = N.custno
  INNER JOIN sqldados.store AS S
	       ON C.cpf_cgc = S.cgc
  LEFT JOIN  sqldados.nf2   AS N2
	       ON N.storeno = N2.storeno AND N.pdvno = N2.pdvno AND N.xano = N2.xano
  LEFT JOIN  sqldados.users AS U
	       ON U.no = N.padbits
WHERE N.issuedate >= 20200608
  AND N.nfse = '5'
  AND N.status <> 1
  AND (N.storeno = :storeno OR S.no = :storeno OR :storeno = 0)