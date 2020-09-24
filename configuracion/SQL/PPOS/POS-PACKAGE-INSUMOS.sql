create or replace PACKAGE PCK_PPOS_INSUMO AS

    PROCEDURE SP_L_INSUMO (
        I_ID_TIPO_INSUMO   	IN    NUMBER,
        I_ID_PERSONAL  		IN    NUMBER,
        I_FEC_INICIO   		IN    VARCHAR2,
        I_FEC_FIN     		IN    VARCHAR2,
        R_LISTA        		OUT   SYS_REFCURSOR,
        R_CODIGO       		OUT   NUMBER,
        R_MENSAJE      		OUT   VARCHAR2
    );

    PROCEDURE SP_I_INSUMO (
        I_ID_PERSONAL         IN      NUMBER,
        I_ID_TIPO_INSUMO      IN      NUMBER,
        I_CANTIDAD            IN      DECIMAL,
        I_FECHA               IN      VARCHAR2,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_CREA     IN      NUMBER,
        I_FEC_USUARIO_CREA    IN      VARCHAR2,
        R_ID                  OUT     NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );

    PROCEDURE SP_U_INSUMO (
        I_ID                  IN      NUMBER,       
        I_ID_PERSONAL         IN      NUMBER,
        I_ID_TIPO_INSUMO      IN      NUMBER,
        I_CANTIDAD            IN      DECIMAL,
        I_FECHA               IN      VARCHAR2,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_MOD      IN      NUMBER,
        I_FEC_USUARIO_MOD     IN      VARCHAR2,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );

    PROCEDURE SP_D_INSUMO (
        I_ID                  IN      NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );
	
	PROCEDURE SP_L_TIPO_INSUMO (
        I_NOMBRE   			IN    VARCHAR2,
        R_LISTA        		OUT   SYS_REFCURSOR,
        R_CODIGO       		OUT   NUMBER,
        R_MENSAJE      		OUT   VARCHAR2
    );

    PROCEDURE SP_I_TIPO_INSUMO (
        I_IDT_UNIDAD_MEDIDA   IN      NUMBER,
        I_NOMBRE      		  IN      VARCHAR2,
        I_CODIGO              IN OUT  VARCHAR2,
        I_OBSERVACION         IN      VARCHAR2,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_CREA     IN      NUMBER,
        I_FEC_USUARIO_CREA    IN      VARCHAR2,
        R_ID                  OUT     NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );

    PROCEDURE SP_U_TIPO_INSUMO (
        I_ID                  IN      NUMBER,       
        I_IDT_UNIDAD_MEDIDA   IN      NUMBER,
        I_NOMBRE      		  IN      VARCHAR2,
        I_CODIGO              IN      VARCHAR2,
        I_OBSERVACION         IN      VARCHAR2,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_MOD      IN      NUMBER,
        I_FEC_USUARIO_MOD     IN      VARCHAR2,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );

    PROCEDURE SP_D_TIPO_INSUMO (
        I_ID                  IN      NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );

END PCK_PPOS_INSUMO;
/
create or replace PACKAGE BODY PCK_PPOS_INSUMO AS

    PROCEDURE SP_L_INSUMO (
        I_ID_TIPO_INSUMO   	IN    NUMBER,
        I_ID_PERSONAL  		IN    NUMBER,
        I_FEC_INICIO   		IN    VARCHAR2,
        I_FEC_FIN     		IN    VARCHAR2,
        R_LISTA        		OUT   SYS_REFCURSOR,
        R_CODIGO       		OUT   NUMBER,
        R_MENSAJE      		OUT   VARCHAR2
    ) AS
        V_SQL VARCHAR2(30000);
    BEGIN
        V_SQL := 'SELECT 
		M.ID,
		M.ID_PERSONAL,
		M.ID_TIPO_INSUMO,
		M.CANTIDAD,
		M.FECHA,
		PE.NOMBRE AS NOMBRE_PER,
		PE.APE_PATERNO AS APE_PATERNO_PER,
		PE.APE_MATERNO AS APE_MATERNO_PER,
		TI.NOMBRE AS NOMBRE_TI,
		TI.IDT_UNIDAD_MEDIDA AS IDT_UNIDAD_MEDIDA_TI,
		(SELECT O.NOMBRE FROM MAESTRA O WHERE O.ID = TI.IDT_UNIDAD_MEDIDA) AS NOM_UNIDAD_MEDIDA,
		M.FLG_ACTIVO,
		M.ID_USUARIO_CREA,
		M.FEC_USUARIO_CREA,
		M.ID_USUARIO_MOD,
		M.FEC_USUARIO_MOD 
		FROM INSUMO M
		LEFT JOIN PERSONAL P ON P.ID = M.ID_PERSONAL
		LEFT JOIN PERSONA PE ON PE.ID = P.ID_PERSONA
		LEFT JOIN TIPO_INSUMO TI ON TI.ID = M.ID_TIPO_INSUMO
		WHERE M.FLG_ACTIVO=1';

		IF ( (I_ID_TIPO_INSUMO IS NOT NULL) AND (I_ID_TIPO_INSUMO <> 0) ) THEN
            V_SQL := V_SQL || ' AND M.ID_TIPO_INSUMO=' || I_ID_TIPO_INSUMO;
        END IF;
		
		IF ( (I_ID_PERSONAL IS NOT NULL) AND (I_ID_PERSONAL <> 0) ) THEN
            V_SQL := V_SQL || ' AND M.ID_PERSONAL=' || I_ID_PERSONAL;
        END IF;

        IF ( I_FEC_INICIO IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.FEC_USUARIO_CREA>=TO_DATE(''' || I_FEC_INICIO || ''',''DD/MM/YY'')';
        END IF;

        IF ( I_FEC_FIN IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.FEC_USUARIO_CREA<=TO_DATE(''' || I_FEC_FIN || ''',''DD/MM/YY'')';
        END IF;

        V_SQL := V_SQL||' ORDER BY M.FECHA DESC, P.ID DESC';
        OPEN R_LISTA FOR V_SQL;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_L_INSUMO;

    PROCEDURE SP_I_INSUMO (
        I_ID_PERSONAL         IN      NUMBER,
        I_ID_TIPO_INSUMO      IN      NUMBER,
        I_CANTIDAD            IN      DECIMAL,
        I_FECHA               IN      VARCHAR2,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_CREA     IN      NUMBER,
        I_FEC_USUARIO_CREA    IN      VARCHAR2,
        R_ID                  OUT     NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
		SELECT SEQ_INSUMO.NEXTVAL INTO R_ID FROM DUAL;
		INSERT INTO INSUMO(ID,ID_PERSONAL, ID_TIPO_INSUMO, CANTIDAD, FECHA, FLG_ACTIVO, ID_USUARIO_CREA, FEC_USUARIO_CREA)
		VALUES(R_ID, I_ID_PERSONAL, I_ID_TIPO_INSUMO, I_CANTIDAD, TO_DATE(I_FECHA,'DD/MM/YYYY'), I_FLG_ACTIVO, I_ID_USUARIO_CREA, TO_DATE(I_FEC_USUARIO_CREA,'DD/MM/YYYY'));

		R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_I_INSUMO;

    PROCEDURE SP_U_INSUMO (
        I_ID                  IN      NUMBER,       
        I_ID_PERSONAL         IN      NUMBER,
        I_ID_TIPO_INSUMO      IN      NUMBER,
        I_CANTIDAD            IN      DECIMAL,
        I_FECHA               IN      VARCHAR2,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_MOD      IN      NUMBER,
        I_FEC_USUARIO_MOD     IN      VARCHAR2,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
        UPDATE INSUMO SET
		ID_PERSONAL=I_ID_PERSONAL, 
		ID_TIPO_INSUMO=I_ID_TIPO_INSUMO, 
		CANTIDAD=I_CANTIDAD, 
		FECHA=TO_DATE(I_FECHA,'DD/MM/YYYY'), 
		FLG_ACTIVO=I_FLG_ACTIVO,
		ID_USUARIO_MOD=I_ID_USUARIO_MOD, 
		FEC_USUARIO_MOD=TO_DATE(I_FEC_USUARIO_MOD,'DD/MM/YYYY')
        WHERE ID=I_ID;
        COMMIT;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_U_INSUMO;

    PROCEDURE SP_D_INSUMO (
        I_ID                  IN      NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
        UPDATE INSUMO M SET M.FLG_ACTIVO=0 WHERE M.ID=I_ID;
        COMMIT;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_D_INSUMO;
	
	PROCEDURE SP_L_TIPO_INSUMO (
        I_NOMBRE   			IN    VARCHAR2,
        R_LISTA        		OUT   SYS_REFCURSOR,
        R_CODIGO       		OUT   NUMBER,
        R_MENSAJE      		OUT   VARCHAR2
    ) AS
        V_SQL VARCHAR2(30000);
    BEGIN
        V_SQL := 'SELECT 
		M.ID,
		M.IDT_UNIDAD_MEDIDA,
		(SELECT O.NOMBRE FROM MAESTRA O WHERE O.ID = M.IDT_UNIDAD_MEDIDA) AS NOM_UNIDAD_MEDIDA,
		M.NOMBRE,
		M.CODIGO,
		M.OBSERVACION,
		M.FLG_ACTIVO,
		M.ID_USUARIO_CREA,
		M.FEC_USUARIO_CREA,
		M.ID_USUARIO_MOD,
		M.FEC_USUARIO_MOD 
		FROM TIPO_INSUMO M
		WHERE M.FLG_ACTIVO=1';

		IF ( I_NOMBRE IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.NOMBRE LIKE ''%' || I_NOMBRE || '%''';
        END IF;

        V_SQL := V_SQL||' ORDER BY M.NOMBRE DESC';
        OPEN R_LISTA FOR V_SQL;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_L_TIPO_INSUMO;

    PROCEDURE SP_I_TIPO_INSUMO (
        I_IDT_UNIDAD_MEDIDA   IN      NUMBER,
        I_NOMBRE      		  IN      VARCHAR2,
        I_CODIGO              IN OUT  VARCHAR2,
        I_OBSERVACION         IN      VARCHAR2,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_CREA     IN      NUMBER,
        I_FEC_USUARIO_CREA    IN      VARCHAR2,
        R_ID                  OUT     NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
		SELECT SEQ_TIPO_INSUMO.NEXTVAL INTO R_ID FROM DUAL;
		SELECT LPAD(R_ID, 8, '0') INTO I_CODIGO FROM DUAL;
		
		INSERT INTO TIPO_INSUMO(ID,IDT_UNIDAD_MEDIDA, NOMBRE, CODIGO, OBSERVACION, FLG_ACTIVO, ID_USUARIO_CREA, FEC_USUARIO_CREA)
		VALUES(R_ID, I_IDT_UNIDAD_MEDIDA, I_NOMBRE, I_CODIGO, I_OBSERVACION, I_FLG_ACTIVO, I_ID_USUARIO_CREA, TO_DATE(I_FEC_USUARIO_CREA,'DD/MM/YYYY'));

		R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_I_TIPO_INSUMO;

    PROCEDURE SP_U_TIPO_INSUMO (
        I_ID                  IN      NUMBER,       
        I_IDT_UNIDAD_MEDIDA   IN      NUMBER,
        I_NOMBRE      		  IN      VARCHAR2,
        I_CODIGO              IN      VARCHAR2,
        I_OBSERVACION         IN      VARCHAR2,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_MOD      IN      NUMBER,
        I_FEC_USUARIO_MOD     IN      VARCHAR2,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
        UPDATE TIPO_INSUMO SET
		IDT_UNIDAD_MEDIDA=I_IDT_UNIDAD_MEDIDA, 
		NOMBRE=I_NOMBRE, 
		CODIGO=I_CODIGO, 
		OBSERVACION=I_OBSERVACION, 
		FLG_ACTIVO=I_FLG_ACTIVO,
		ID_USUARIO_MOD=I_ID_USUARIO_MOD, 
		FEC_USUARIO_MOD=TO_DATE(I_FEC_USUARIO_MOD,'DD/MM/YYYY')
        WHERE ID=I_ID;
        COMMIT;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_U_TIPO_INSUMO;

    PROCEDURE SP_D_TIPO_INSUMO (
        I_ID                  IN      NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
        UPDATE TIPO_INSUMO M SET M.FLG_ACTIVO=0 WHERE M.ID=I_ID;
        COMMIT;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_D_TIPO_INSUMO;

END PCK_PPOS_INSUMO;