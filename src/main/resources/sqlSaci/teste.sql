DROP TABLE IF EXISTS sqldados.TMP1_18635;
CREATE TEMPORARY TABLE sqldados.TMP1_18635 /*T1*/ (
  PRIMARY KEY (storeno, ordno)
)
SELECT E.storeno,
       ordno,
       dataEntrega,
       dataMontagem,
       custno_addno,
       IF(E.bits6 & 1 = 0, 'M', 'T') AS TURNO_E
FROM sqldados.eord AS E
  INNER JOIN sqldados.custp
	       ON E.custno = custp.no
WHERE date BETWEEN 20200601 AND 20200605
  AND E.storeno IN (1, 3, 5, 6, 8, 9, 10, 11, 12)
  AND ((E.date BETWEEN 20200601 AND 20200605) OR 20200601 = 0 OR 20200605 = 0)
  AND (custp.no = 0 OR custp.cpf_cgc = '' OR ('' = "" AND 0 = 0))
  AND IF(1 = 1, paymno = 957, paymno != 0);
SELECT CONCAT("Codigo-Nome :", R.custno, "-", C.name, '\n', "Endereco: ", addr, " - ", nei) AS "",
       "         "                                                                          AS linha,
       IF(R.date = 0, "", DATE_FORMAT(R.date, "%d/%m/%y"))                                  AS Data_Venda,
       R.xano                                                                               AS Transacao,
       IF(E.dataEntrega = 0, "",
	  DATE_FORMAT(E.dataEntrega, "%d/%m/%y"))                                           AS Dt_Entrega,
       E.TURNO_E,
       IF(E.dataMontagem != 0, DATE_FORMAT(E.dataMontagem, "%d/%m/%y"),
	  "Sem Montagem")                                                                   AS Dt_Montagem,
       IF(M.bits & 1 = 0, 'M', 'T')                                                         AS TURNO_M,
       TRIM(RP.prdno)                                                                       AS Produto,
       MID(PD.name, 1, 30)                                                                  AS Descricao,
       CASE S.statusCarga
	 WHEN 0
	   THEN "Carga"
	 WHEN 1
	   THEN "Transito"
	 WHEN 2
	   THEN "Concluido"
	 WHEN 3
	   THEN "Devolvido"
       END                                                                                  AS Sit_Entrega,
       IF(M.dataMontagem = 0, "",
	  DATE_FORMAT(M.dataMontagem, "%d/%m/%y"))                                          AS Dt_montagem2,
       CASE T3.status
	 WHEN 0
	   THEN "Retorno de Entrega"
	 WHEN 1
	   THEN "Sa.da para Entrega"
	 WHEN 2
	   THEN "Troca de Carga"
	 WHEN 3
	   THEN "Exclus.o carga"
	 WHEN 4
	   THEN "NF Bloqueada"
	 WHEN 5
	   THEN "NF DEsbloqueada"
	 WHEN 6
	   THEN "Entrega Reagendada"
	 WHEN 7
	   THEN "NF Relativa CF"
	 WHEN 8
	   THEN "NF TO Entrega"
	 WHEN 9
	   THEN "NF TO Retira"
	 WHEN 10
	   THEN "Retira"
	 WHEN 11
	   THEN "Faturado"
	 WHEN 12
	   THEN "Estorno"
	 WHEN 13
	   THEN "Motorista alterado"
	 WHEN 14
	   THEN "Quantidades parciais"
	 WHEN 15
	   THEN "Nota emitida"
	 WHEN 16
	   THEN "Convers.o futura"
	 WHEN 17
	   THEN "Mudan.a na observa..o"
	 WHEN 18
	   THEN "Mudan.a na data"
	 WHEN 19
	   THEN "Mudan.a de Endere.o"
	 WHEN 20
	   THEN "Emissao de nota parcial de Simples Remessa"
	 WHEN 21
	   THEN "Emissao de Nota Fiscal de E.F. para Devolucao"
	 WHEN 22
	   THEN "Emissao de Nota Fiscal de Devolucao"
	 WHEN 23
	   THEN "Saida de Carga com romaneio que apresenta devolucao"
	 WHEN 24
	   THEN "Cancelamento de NF"
	 WHEN 25
	   THEN "Estorno de NF"
	 WHEN 26
	   THEN "Merge de Romaneios"
	 WHEN 27
	   THEN "Insercao Manual"
       END                                                                                  AS Observacao,
       CONCAT('\n')                                                                         AS _
FROM sqldados.TMP1_18635     AS E
  INNER JOIN sqldados.nfr    AS R
	       ON E.storeno = R.storeno AND E.ordno = R.auxLong1
  LEFT JOIN  sqldados.nfm    AS M
	       ON R.storeno = M.storeno AND R.pdvno = M.pdvno AND R.xano = M.xano
  INNER JOIN sqldados.nfrprd AS RP
	       ON R.storeno = RP.storeno AND R.pdvno = RP.pdvno AND R.xano = RP.xano
  INNER JOIN sqldados.prd    AS PD
	       ON PD.no = RP.prdno
  INNER JOIN sqldados.custp  AS C
	       ON C.no = R.custno
  INNER JOIN sqldados.ctadd  AS EN
	       ON EN.custno = R.custno AND EN.seqno = R.auxShort
  INNER JOIN sqldados.awnfr  AS S
	       ON R.storeno = S.storenoNfr AND R.pdvno = S.pdvnoNfr AND R.xano = S.xanoNfr
ORDER BY E.dataEntrega, R.custno DESC;
