create or replace PACKAGE PCK_PPOS_REPORTE AS

    PROCEDURE SP_L_REP_INSUMO (
		I_ID_TIPO_REPORTE 	IN    NUMBER,
        I_ID_TIPO_INSUMO   	IN    NUMBER,
        I_ID_PERSONAL  		IN    NUMBER,
        I_FEC_INICIO   		IN    VARCHAR2,
        I_FEC_FIN     		IN    VARCHAR2,
        R_LISTA        		OUT   SYS_REFCURSOR,
        R_CODIGO       		OUT   NUMBER,
        R_MENSAJE      		OUT   VARCHAR2
    );
	
	PROCEDURE SP_L_REP_VENTA (
		I_ID_TIPO_REPORTE 	IN    NUMBER,
        I_ID_PRODUCTO  		IN    NUMBER,
        I_FEC_INICIO   		IN    VARCHAR2,
        I_FEC_FIN     		IN    VARCHAR2,
        R_LISTA        		OUT   SYS_REFCURSOR,
        R_CODIGO       		OUT   NUMBER,
        R_MENSAJE      		OUT   VARCHAR2
    );

END PCK_PPOS_REPORTE;
/
create or replace PACKAGE BODY PCK_PPOS_REPORTE AS

    PROCEDURE SP_L_REP_INSUMO (
		I_ID_TIPO_REPORTE 	IN    NUMBER,
        I_ID_TIPO_INSUMO   	IN    NUMBER,
        I_ID_PERSONAL  		IN    NUMBER,
        I_FEC_INICIO   		IN    VARCHAR2,
        I_FEC_FIN     		IN    VARCHAR2,
        R_LISTA        		OUT   SYS_REFCURSOR,
        R_CODIGO       		OUT   NUMBER,
        R_MENSAJE      		OUT   VARCHAR2
    ) AS
        V_SQL VARCHAR2(32000);
		V_SQL_ADI VARCHAR2(10000);
		D_FEC_FIN DATE;
    BEGIN
        V_SQL := '';
		V_SQL_ADI := '';

		IF ( (I_ID_TIPO_INSUMO IS NOT NULL) AND (I_ID_TIPO_INSUMO <> 0) ) THEN
            V_SQL_ADI := V_SQL_ADI || ' AND M.ID_TIPO_INSUMO=' || I_ID_TIPO_INSUMO;
        END IF;
		
		IF ( (I_ID_PERSONAL IS NOT NULL) AND (I_ID_PERSONAL <> 0) ) THEN
            V_SQL_ADI := V_SQL_ADI || ' AND M.ID_PERSONAL=' || I_ID_PERSONAL;
        END IF;
		
		IF ( I_ID_TIPO_REPORTE = 1 ) THEN
            V_SQL := 'SELECT ' ||
			I_ID_TIPO_REPORTE||' AS ID_TIPO_REPORTE,
			M.ID_PERSONAL AS ID_PERSONAL,
			(PE.NOMBRE ||'' '' || PE.APE_PATERNO || '' '' || PE.APE_MATERNO) AS NOM_PERSONAL,
			M.FECHA AS FECHA,
			M.ID_TIPO_INSUMO AS ID_TIPO_INSUMO,
			TI.NOMBRE AS NOM_TIPO_INSUMO,
			(SELECT O.NOMBRE FROM MAESTRA O WHERE O.ID = TI.IDT_UNIDAD_MEDIDA) AS NOM_UNIDAD_MEDIDA,
			SUM(M.CANTIDAD) AS SUMA
			FROM INSUMO M
			LEFT JOIN PERSONAL P ON P.ID = M.ID_PERSONAL
			LEFT JOIN PERSONA PE ON PE.ID = P.ID_PERSONA
			LEFT JOIN TIPO_INSUMO TI ON TI.ID = M.ID_TIPO_INSUMO
			WHERE M.FLG_ACTIVO=1'|| V_SQL_ADI ||' AND M.FECHA>='''|| TO_DATE(I_FEC_INICIO,'DD/MM/YY') ||''' AND M.FECHA<='''|| TO_DATE(I_FEC_FIN,'DD/MM/YY') ||'''
			GROUP BY
			M.ID_PERSONAL,
			(PE.NOMBRE ||'' '' || PE.APE_PATERNO || '' '' || PE.APE_MATERNO),
			M.FECHA, 
			M.ID_TIPO_INSUMO, 
			TI.NOMBRE,
			TI.IDT_UNIDAD_MEDIDA
			ORDER BY M.FECHA DESC';
		ELSE
			D_FEC_FIN := ADD_MONTHS(TO_DATE(I_FEC_FIN,'DD/MM/YY'), 1);
			V_SQL := 'SELECT ' ||
			I_ID_TIPO_REPORTE||' AS ID_TIPO_REPORTE,
			TO_CHAR(M.FECHA,''YYYY'') AS ANIO,
			TO_CHAR(M.FECHA,''MM'') AS MES,
			UPPER(TO_CHAR(M.FECHA, ''MONTH'')) AS FECHA,
			M.ID_PERSONAL AS ID_PERSONAL,
			(PE.NOMBRE ||'' '' || PE.APE_PATERNO || '' '' || PE.APE_MATERNO) AS NOM_PERSONAL,
			M.ID_TIPO_INSUMO AS ID_TIPO_INSUMO,
			TI.NOMBRE AS NOM_TIPO_INSUMO,
			(SELECT O.NOMBRE FROM MAESTRA O WHERE O.ID = TI.IDT_UNIDAD_MEDIDA) AS NOM_UNIDAD_MEDIDA,
			SUM(M.CANTIDAD) AS SUMA
			FROM INSUMO M
			LEFT JOIN PERSONAL P ON P.ID = M.ID_PERSONAL
			LEFT JOIN PERSONA PE ON PE.ID = P.ID_PERSONA
			LEFT JOIN TIPO_INSUMO TI ON TI.ID = M.ID_TIPO_INSUMO
			WHERE M.FLG_ACTIVO=1'|| V_SQL_ADI ||' AND M.FECHA>='''|| TO_DATE(I_FEC_INICIO,'DD/MM/YY') ||''' AND M.FECHA<'''|| D_FEC_FIN ||'''
			GROUP BY
			TO_CHAR(M.FECHA,''YYYY''),
			TO_CHAR(M.FECHA,''MM''),
			UPPER(TO_CHAR(M.FECHA, ''MONTH'')),
			M.ID_PERSONAL,
			(PE.NOMBRE ||'' '' || PE.APE_PATERNO || '' '' || PE.APE_MATERNO), 
			M.ID_TIPO_INSUMO,
			TI.NOMBRE,
			TI.IDT_UNIDAD_MEDIDA
			ORDER BY ANIO DESC, MES DESC';
        END IF;
        OPEN R_LISTA FOR V_SQL;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_L_REP_INSUMO;
	
	PROCEDURE SP_L_REP_VENTA (
		I_ID_TIPO_REPORTE 	IN    NUMBER,
        I_ID_PRODUCTO  		IN    NUMBER,
        I_FEC_INICIO   		IN    VARCHAR2,
        I_FEC_FIN     		IN    VARCHAR2,
        R_LISTA        		OUT   SYS_REFCURSOR,
        R_CODIGO       		OUT   NUMBER,
        R_MENSAJE      		OUT   VARCHAR2
    ) AS
        V_SQL VARCHAR2(32000);
		V_SQL_ADI VARCHAR2(10000);
		D_FEC_FIN DATE;
    BEGIN
        V_SQL := '';
		V_SQL_ADI := '';
		
		IF ( (I_ID_PRODUCTO IS NOT NULL) AND (I_ID_PRODUCTO <> 0) ) THEN
            V_SQL_ADI := V_SQL_ADI || ' AND DV.ID_PRODUCTO=' || I_ID_PRODUCTO;
        END IF;
		
		IF ( I_ID_TIPO_REPORTE = 1 ) THEN
            V_SQL := 'SELECT ' ||
			I_ID_TIPO_REPORTE||' AS ID_TIPO_REPORTE,
			V.FEC_USUARIO_CREA AS FECHA,
			DV.ID_PRODUCTO AS ID_PRODUCTO,
			P.NOMBRE AS NOM_PRODUCTO,
			(SELECT O.NOMBRE FROM MAESTRA O WHERE O.ID = P.IDT_UNIDAD_MEDIDA) AS NOM_UNIDAD_MEDIDA,
			SUM(DV.CANTIDAD) AS CANTIDAD,
            SUM(DV.SUBTOTAL) AS SUMA
			FROM VENTA V
			LEFT JOIN DETALLE_VENTA DV ON DV.ID_VENTA = V.ID
			LEFT JOIN PRODUCTO P ON P.ID = DV.ID_PRODUCTO
			WHERE V.FLG_ACTIVO=1'|| V_SQL_ADI ||' AND V.FEC_USUARIO_CREA>='''|| TO_DATE(I_FEC_INICIO,'DD/MM/YY') ||''' AND V.FEC_USUARIO_CREA<='''|| TO_DATE(I_FEC_FIN,'DD/MM/YY') ||'''
			GROUP BY
			DV.ID_PRODUCTO,
            P.NOMBRE,
			V.FEC_USUARIO_CREA,
			P.IDT_UNIDAD_MEDIDA
			ORDER BY DV.ID_PRODUCTO DESC, V.FEC_USUARIO_CREA DESC';
		ELSE
			D_FEC_FIN := ADD_MONTHS(TO_DATE(I_FEC_FIN,'DD/MM/YY'), 1);
			V_SQL := 'SELECT ' ||
			I_ID_TIPO_REPORTE||' AS ID_TIPO_REPORTE,
            TO_CHAR(V.FEC_USUARIO_CREA,''YYYY'') AS ANIO,
			TO_CHAR(V.FEC_USUARIO_CREA,''MM'') AS MES,
			UPPER(TO_CHAR(V.FEC_USUARIO_CREA, ''MONTH'')) AS FECHA,
			DV.ID_PRODUCTO AS ID_PRODUCTO,
			P.NOMBRE AS NOM_PRODUCTO,
			(SELECT O.NOMBRE FROM MAESTRA O WHERE O.ID = P.IDT_UNIDAD_MEDIDA) AS NOM_UNIDAD_MEDIDA,
			SUM(DV.CANTIDAD) AS CANTIDAD,
            SUM(DV.SUBTOTAL) AS SUMA
			FROM VENTA V
			LEFT JOIN DETALLE_VENTA DV ON DV.ID_VENTA = V.ID
			LEFT JOIN PRODUCTO P ON P.ID = DV.ID_PRODUCTO
			WHERE V.FLG_ACTIVO=1'|| V_SQL_ADI ||' AND V.FEC_USUARIO_CREA>='''|| TO_DATE(I_FEC_INICIO,'DD/MM/YY') ||''' AND V.FEC_USUARIO_CREA<'''|| TO_DATE(I_FEC_FIN,'DD/MM/YY') ||'''
			GROUP BY
            TO_CHAR(V.FEC_USUARIO_CREA,''YYYY''),
			TO_CHAR(V.FEC_USUARIO_CREA,''MM''),
			UPPER(TO_CHAR(V.FEC_USUARIO_CREA, ''MONTH'')),
			DV.ID_PRODUCTO,
            P.NOMBRE,
			P.IDT_UNIDAD_MEDIDA
			ORDER BY DV.ID_PRODUCTO DESC, MES DESC';
        END IF;
        OPEN R_LISTA FOR V_SQL;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_L_REP_VENTA;

END PCK_PPOS_REPORTE;
/