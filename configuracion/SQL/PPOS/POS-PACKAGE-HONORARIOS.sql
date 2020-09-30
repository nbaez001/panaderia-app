CREATE OR REPLACE PACKAGE PCK_PPOS_HONORARIO AS

    PROCEDURE SP_I_HONORARIO (
		I_ID_PERSONAL			IN 		NUMBER,
		I_MONTO		 			IN 		DECIMAL,
		I_FECHA_INICIO      	IN      VARCHAR2,
		I_FECHA_FIN      		IN      VARCHAR2,
		I_FECHA 	     		IN      VARCHAR2,
		I_FLG_ACTIVO 			IN 		NUMBER,
        I_ID_USUARIO_CREA       IN      NUMBER,
        I_FEC_USUARIO_CREA      IN      VARCHAR2,
		I_HONORARIO_INSUMO     	IN      VARCHAR2,
		I_HONORARIO_DETALLE    	IN      VARCHAR2,
        R_ID                    OUT     NUMBER,
        R_CODIGO                OUT     NUMBER,
        R_MENSAJE               OUT     VARCHAR2
    );

    PROCEDURE SP_L_HONORARIO (
		I_ID_PERSONAL  		  IN      NUMBER,
        I_FEC_INICIO	      IN      VARCHAR2,
        I_FEC_FIN  			  IN      VARCHAR2,
        R_LISTA               OUT     SYS_REFCURSOR,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );
	
	PROCEDURE SP_S_PERIODO_HONORARIO (
		I_ID_PERSONAL  		  IN      NUMBER,
        R_FEC_INICIO	      OUT     DATE,
        R_FEC_FIN  			  OUT     DATE,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );

END PCK_PPOS_HONORARIO;
/
CREATE OR REPLACE PACKAGE BODY PCK_PPOS_HONORARIO AS

    PROCEDURE SP_I_HONORARIO (
		I_ID_PERSONAL			IN 		NUMBER,
		I_MONTO		 			IN 		DECIMAL,
		I_FECHA_INICIO      	IN      VARCHAR2,
		I_FECHA_FIN      		IN      VARCHAR2,
		I_FECHA 	     		IN      VARCHAR2,
		I_FLG_ACTIVO 			IN 		NUMBER,
        I_ID_USUARIO_CREA       IN      NUMBER,
        I_FEC_USUARIO_CREA      IN      VARCHAR2,
		I_HONORARIO_INSUMO     	IN      VARCHAR2,
		I_HONORARIO_DETALLE    	IN      VARCHAR2,
        R_ID                    OUT     NUMBER,
        R_CODIGO                OUT     NUMBER,
        R_MENSAJE               OUT     VARCHAR2
    ) AS
	CURSOR C_HONORARIO_INSUMO IS 
		SELECT
        REGEXP_SUBSTR(HONORARIO_INSUMO,'([^,]+)',1,1,'',1) AS ID_INSUMO,
        REGEXP_SUBSTR(HONORARIO_INSUMO,'([^,]+)',1,2,'',1) AS FLG_ACTIVO
        FROM (
            SELECT 
            REGEXP_SUBSTR(I_HONORARIO_INSUMO,'[^|]+',1,LEVEL) AS HONORARIO_INSUMO 
            FROM DUAL 
            CONNECT BY REGEXP_SUBSTR(I_HONORARIO_INSUMO,'[^|]+',1,LEVEL) IS NOT NULL
        );
	CURSOR C_HONORARIO_DETALLE IS 
		SELECT
        REGEXP_SUBSTR(HONORARIO_DETALLE,'([^,]+)',1,1,'',1) AS ID_TIPO_INSUMO,
        REGEXP_SUBSTR(HONORARIO_DETALLE,'([^,]+)',1,2,'',1) AS CANTIDAD,
        REGEXP_SUBSTR(HONORARIO_DETALLE,'([^,]+)',1,3,'',1) AS TARIFA,
        REGEXP_SUBSTR(HONORARIO_DETALLE,'([^,]+)',1,4,'',1) AS SUBTOTAL,
        REGEXP_SUBSTR(HONORARIO_DETALLE,'([^,]+)',1,5,'',1) AS FLG_ACTIVO
        FROM (
            SELECT 
            REGEXP_SUBSTR(I_HONORARIO_DETALLE,'[^|]+',1,LEVEL) AS HONORARIO_DETALLE 
            FROM DUAL 
            CONNECT BY REGEXP_SUBSTR(I_HONORARIO_DETALLE,'[^|]+',1,LEVEL) IS NOT NULL
        );
    BEGIN
        SELECT SEQ_HONORARIO.NEXTVAL INTO R_ID FROM DUAL;

        INSERT INTO HONORARIO(
        ID,
		ID_PERSONAL,
        MONTO,
		FECHA_INICIO,
		FECHA_FIN,
		FECHA,
        FLG_ACTIVO,
        ID_USUARIO_CREA,
        FEC_USUARIO_CREA)
        VALUES(
		R_ID,
		I_ID_PERSONAL,
        I_MONTO,
		TO_DATE(I_FECHA_INICIO,'DD/MM/YYYY'),
		TO_DATE(I_FECHA_FIN,'DD/MM/YYYY'),
		TO_DATE(I_FECHA,'DD/MM/YYYY'),
        I_FLG_ACTIVO,
        I_ID_USUARIO_CREA,
        TO_DATE(I_FEC_USUARIO_CREA,'DD/MM/YYYY'));

		FOR V_ITEM IN C_HONORARIO_INSUMO LOOP
			INSERT INTO HONORARIO_INSUMO(
			ID,
			ID_HONORARIO,
			ID_INSUMO,
			FLG_ACTIVO)
            VALUES(
			SEQ_HONORARIO_INSUMO.NEXTVAL,
			R_ID,
			V_ITEM.ID_INSUMO,
			V_ITEM.FLG_ACTIVO);
			
			UPDATE INSUMO SET 
			FLG_CAL_HONORARIO = 1
			WHERE ID = V_ITEM.ID_INSUMO;
		END LOOP;
		
		FOR V_ITEM IN C_HONORARIO_DETALLE LOOP
			INSERT INTO HONORARIO_DETALLE(
			ID,
			ID_HONORARIO,
			ID_TIPO_INSUMO,
			CANTIDAD,
			TARIFA,
			SUBTOTAL,
			FLG_ACTIVO)
            VALUES(
			SEQ_HONORARIO_DETALLE.NEXTVAL,
			R_ID,
			V_ITEM.ID_TIPO_INSUMO,
			TO_NUMBER(V_ITEM.CANTIDAD, '9999999.9'),
			TO_NUMBER(V_ITEM.TARIFA, '9999999.99'),
			TO_NUMBER(V_ITEM.SUBTOTAL, '9999999.99'),
			V_ITEM.FLG_ACTIVO);
		END LOOP;

        COMMIT;

        R_CODIGO := SQLCODE;
        R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_I_HONORARIO;

    PROCEDURE SP_L_HONORARIO (
		I_ID_PERSONAL  		  IN      NUMBER,
        I_FEC_INICIO	      IN      VARCHAR2,
        I_FEC_FIN  			  IN      VARCHAR2,
        R_LISTA               OUT     SYS_REFCURSOR,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
        V_SQL VARCHAR2(30000);
    BEGIN
        V_SQL := 'SELECT
		C.ID,
		C.ID_PERSONAL,
		PE.NOMBRE,
		PE.APE_PATERNO,
		PE.APE_MATERNO,
		C.MONTO,
		C.FECHA_INICIO,
		C.FECHA_FIN,
		C.FECHA,
		C.FLG_ACTIVO,
		C.ID_USUARIO_CREA,
		C.FEC_USUARIO_CREA,
		C.ID_USUARIO_MOD,
		C.FEC_USUARIO_MOD
        FROM HONORARIO C
		LEFT JOIN PERSONAL P ON P.ID = C.ID_PERSONAL
		LEFT JOIN PERSONA PE ON PE.ID = P.ID_PERSONA
        WHERE C.FLG_ACTIVO=1';

        IF ( (I_ID_PERSONAL IS NOT NULL) AND (I_ID_PERSONAL <> 0) ) THEN
            V_SQL := V_SQL || ' AND C.ID_PERSONAL=' || I_ID_PERSONAL;
        END IF;

        IF ( I_FEC_INICIO IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND C.FECHA>=TO_DATE(''' || I_FEC_INICIO || ''',''DD/MM/YY'')';
        END IF;

        IF ( I_FEC_FIN IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND C.FECHA<=TO_DATE(''' || I_FEC_FIN || ''',''DD/MM/YY'')';
        END IF;

        V_SQL := V_SQL||' ORDER BY C.FECHA DESC';
        OPEN R_LISTA FOR V_SQL;

        R_CODIGO := SQLCODE;
        R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_L_HONORARIO;
	
	PROCEDURE SP_S_PERIODO_HONORARIO (
		I_ID_PERSONAL  		  IN      NUMBER,
        R_FEC_INICIO	      OUT     DATE,
        R_FEC_FIN  			  OUT     DATE,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
        SELECT MIN(I.FECHA) INTO R_FEC_INICIO 
		FROM INSUMO I 
		INNER JOIN PERSONAL P ON P.ID = I.ID_PERSONAL
		WHERE I.FLG_ACTIVO=1 AND I.FLG_CAL_HONORARIO!=1 AND P.ID = I_ID_PERSONAL;
		
		SELECT MAX(I.FECHA) INTO R_FEC_FIN 
		FROM INSUMO I 
		INNER JOIN PERSONAL P ON P.ID = I.ID_PERSONAL
		WHERE I.FLG_ACTIVO=1 AND I.FLG_CAL_HONORARIO!=1 AND P.ID = I_ID_PERSONAL;

        R_CODIGO := SQLCODE;
        R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_S_PERIODO_HONORARIO;

END PCK_PPOS_HONORARIO;
/