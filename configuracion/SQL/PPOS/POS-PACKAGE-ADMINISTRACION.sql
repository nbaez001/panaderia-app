create or replace PACKAGE PCK_PPOS_ADMINISTRACION AS

    PROCEDURE SP_L_MAESTRA (
        I_FEC_INICIO   IN    VARCHAR2,
        I_FEC_FIN      IN    VARCHAR2,
        I_ID_MAESTRA   IN    NUMBER,
        I_ID_TABLA     IN    NUMBER,
        R_LISTA        OUT   SYS_REFCURSOR,
        R_CODIGO       OUT   NUMBER,
        R_MENSAJE      OUT   VARCHAR2
    );

    PROCEDURE SP_I_MAESTRA (
        I_ID_MAESTRA          IN      NUMBER,
        I_ID_TABLA            IN      NUMBER,
        I_ID_ITEM             IN      NUMBER,
        I_ORDEN               IN      NUMBER,
        I_CODIGO              IN      VARCHAR2,
        I_NOMBRE              IN      VARCHAR2,
        I_VALOR               IN      VARCHAR2,
        I_DESCRIPCION         IN      VARCHAR2,
        I_FLAG_ACTIVO         IN      NUMBER,
        I_ID_USUARIO_CREA     IN      NUMBER,
        I_FEC_USUARIO_CREA    IN      VARCHAR2,
        R_ID                  OUT     NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );

    PROCEDURE SP_U_MAESTRA (
        I_ID                  IN      NUMBER,       
        I_ID_MAESTRA          IN      NUMBER,
        I_ID_TABLA            IN      NUMBER,
        I_ID_ITEM             IN      NUMBER,
        I_ORDEN               IN      NUMBER,
        I_CODIGO              IN      VARCHAR2,
        I_NOMBRE              IN      VARCHAR2,
        I_VALOR               IN      VARCHAR2,
        I_DESCRIPCION         IN      VARCHAR2,
        I_FLAG_ACTIVO         IN      NUMBER,
        I_ID_USUARIO_MOD      IN      NUMBER,
        I_FEC_USUARIO_MOD     IN      VARCHAR2,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );

    PROCEDURE SP_D_MAESTRA (
        I_ID                  IN      NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );

    PROCEDURE SP_L_PRODUCTO (
        I_FEC_INICIO   IN    VARCHAR2,
        I_FEC_FIN      IN    VARCHAR2,
        I_NOMBRE       IN    VARCHAR2,
        R_LISTA        OUT   SYS_REFCURSOR,
        R_CODIGO       OUT   NUMBER,
        R_MENSAJE      OUT   VARCHAR2
    );

    PROCEDURE SP_I_PRODUCTO (
        I_IDT_UNIDAD_MEDIDA   IN      NUMBER,
        I_NOMBRE              IN      VARCHAR2,
        I_CODIGO              IN OUT  VARCHAR2,
        I_PRECIO              IN      NUMBER,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_CREA     IN      NUMBER,
        I_FEC_USUARIO_CREA    IN      VARCHAR2,
        R_ID                  OUT     NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );
	
	PROCEDURE SP_U_PRODUCTO (
        I_ID                  IN      NUMBER,       
        I_IDT_UNIDAD_MEDIDA   IN      NUMBER,
        I_NOMBRE              IN      VARCHAR2,
        I_CODIGO              IN      VARCHAR2,
        I_PRECIO              IN      NUMBER,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_MOD      IN      NUMBER,
        I_FEC_USUARIO_MOD     IN      VARCHAR2,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );
	
	PROCEDURE SP_D_PRODUCTO (
        I_ID                  IN      NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );
    
    PROCEDURE SP_L_COMPROBANTE (
		I_ID   					IN    NUMBER,
        I_FEC_INICIO   			IN    VARCHAR2,
        I_FEC_FIN      			IN    VARCHAR2,
        I_IDT_TIPO_COMPROBANTE  IN    NUMBER,
		I_FLG_ACTUAL			IN 	  NUMBER,
        R_LISTA        			OUT   SYS_REFCURSOR,
        R_CODIGO       			OUT   NUMBER,
        R_MENSAJE      			OUT   VARCHAR2
    );
	
	PROCEDURE SP_I_COMPROBANTE (
        I_IDT_TIPO_COMPROBANTE	IN      NUMBER,
        I_NOMBRE            	IN      VARCHAR2,
        I_SERIE             	IN      NUMBER,
        I_NUMERO               	IN      NUMBER,
        I_LONG_SERIE            IN      NUMBER,
        I_LONG_NUMERO           IN      NUMBER,
		I_FLG_ACTUAL            IN OUT  NUMBER,
        I_FLG_ACTIVO            IN      NUMBER,
        I_FLG_USADO         	IN      NUMBER,
        I_ID_USUARIO_CREA     	IN      NUMBER,
        I_FEC_USUARIO_CREA    	IN      VARCHAR2,
        R_ID                  	OUT     NUMBER,
        R_CODIGO              	OUT     NUMBER,
        R_MENSAJE             	OUT     VARCHAR2
    );
	
	PROCEDURE SP_U_COMPROBANTE (
        I_ID                  	IN      NUMBER,       
        I_IDT_TIPO_COMPROBANTE	IN      NUMBER,
        I_NOMBRE            	IN      VARCHAR2,
        I_SERIE             	IN      NUMBER,
        I_NUMERO               	IN      NUMBER,
        I_LONG_SERIE            IN      NUMBER,
        I_LONG_NUMERO           IN      NUMBER,
		I_FLG_ACTUAL            IN      NUMBER,
        I_FLG_ACTIVO            IN      NUMBER,
        I_FLG_USADO         	IN      NUMBER,
        I_ID_USUARIO_MOD      	IN      NUMBER,
        I_FEC_USUARIO_MOD     	IN      VARCHAR2,
        R_CODIGO              	OUT     NUMBER,
        R_MENSAJE             	OUT     VARCHAR2
    );
	
	PROCEDURE SP_D_COMPROBANTE (
        I_ID                  IN      NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    );
	
	PROCEDURE SP_EST_COMPROBANTE_ACTUAL (
        I_ID                  	IN      NUMBER,
		I_IDT_TIPO_COMPROBANTE  IN    	NUMBER,
        R_CODIGO              	OUT     NUMBER,
        R_MENSAJE             	OUT     VARCHAR2
    );

END PCK_PPOS_ADMINISTRACION;

/
create or replace PACKAGE BODY PCK_PPOS_ADMINISTRACION AS

    PROCEDURE SP_L_MAESTRA (
        I_FEC_INICIO   IN    VARCHAR2,
        I_FEC_FIN      IN    VARCHAR2,
        I_ID_MAESTRA   IN    NUMBER,
        I_ID_TABLA     IN    NUMBER,
        R_LISTA        OUT   SYS_REFCURSOR,
        R_CODIGO       OUT   NUMBER,
        R_MENSAJE      OUT   VARCHAR2
    ) AS
        V_SQL VARCHAR2(30000);
    BEGIN
        V_SQL := 'SELECT M.ID,M.ID_MAESTRA,M.ID_TABLA,M.ID_ITEM,M.ORDEN,M.CODIGO,M.NOMBRE,M.VALOR,M.DESCRIPCION,M.FLAG_ACTIVO,M.ID_USUARIO_CREA,M.FEC_USUARIO_CREA,M.ID_USUARIO_MOD,M.FEC_USUARIO_MOD FROM MAESTRA M WHERE M.FLAG_ACTIVO=1';

        IF ( I_FEC_INICIO IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.FEC_USUARIO_CREA>=TO_DATE(''' || I_FEC_INICIO || ''',''DD/MM/YY'')';
        END IF;

        IF ( I_FEC_FIN IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.FEC_USUARIO_CREA<=TO_DATE(''' || I_FEC_FIN || ''',''DD/MM/YY'')';
        END IF;

        IF ( I_ID_MAESTRA IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.ID_MAESTRA=' || I_ID_MAESTRA;
        END IF;

        IF ( I_ID_TABLA IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.ID_TABLA=' || I_ID_TABLA || ' AND M.ID_ITEM!=0';
        END IF;

        V_SQL := V_SQL||' ORDER BY ORDEN ASC, M.ID_TABLA DESC';
        OPEN R_LISTA FOR V_SQL;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_L_MAESTRA;

    PROCEDURE SP_I_MAESTRA (
        I_ID_MAESTRA          IN      NUMBER,
        I_ID_TABLA            IN      NUMBER,
        I_ID_ITEM             IN      NUMBER,
        I_ORDEN               IN      NUMBER,
        I_CODIGO              IN      VARCHAR2,
        I_NOMBRE              IN      VARCHAR2,
        I_VALOR               IN      VARCHAR2,
        I_DESCRIPCION         IN      VARCHAR2,
        I_FLAG_ACTIVO         IN      NUMBER,
        I_ID_USUARIO_CREA     IN      NUMBER,
        I_FEC_USUARIO_CREA    IN      VARCHAR2,
        R_ID                  OUT     NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
	V_CONT NUMBER;
    BEGIN
		SELECT COUNT(M.ID) INTO V_CONT FROM MAESTRA M WHERE M.ID_TABLA = I_ID_TABLA;

		IF I_ID_MAESTRA = 0 THEN
			IF V_CONT > 0 THEN
				R_CODIGO := -1;
				R_MENSAJE := 'ID DE TABLA YA EXISTE';
			ELSE
				SELECT SEQ_MAESTRA.NEXTVAL INTO R_ID FROM DUAL;
				INSERT INTO MAESTRA(ID,ID_MAESTRA, ID_TABLA, ID_ITEM, ORDEN, CODIGO, NOMBRE, VALOR, DESCRIPCION, FLAG_ACTIVO, ID_USUARIO_CREA, FEC_USUARIO_CREA)
				VALUES(R_ID, I_ID_MAESTRA, I_ID_TABLA, I_ID_ITEM, I_ORDEN, I_CODIGO, I_NOMBRE, I_VALOR, I_DESCRIPCION, I_FLAG_ACTIVO, I_ID_USUARIO_CREA, TO_DATE(I_FEC_USUARIO_CREA,'DD/MM/YYYY'));				

				R_CODIGO := SQLCODE;
				R_MENSAJE := SQLERRM;
			END IF;
		ELSE
			SELECT SEQ_MAESTRA.NEXTVAL INTO R_ID FROM DUAL;
			INSERT INTO MAESTRA(ID,ID_MAESTRA, ID_TABLA, ID_ITEM, ORDEN, CODIGO, NOMBRE, VALOR, DESCRIPCION, FLAG_ACTIVO, ID_USUARIO_CREA, FEC_USUARIO_CREA)
			VALUES(R_ID, I_ID_MAESTRA, I_ID_TABLA, I_ID_ITEM, I_ORDEN, I_CODIGO, I_NOMBRE, I_VALOR, I_DESCRIPCION, I_FLAG_ACTIVO, I_ID_USUARIO_CREA, TO_DATE(I_FEC_USUARIO_CREA,'DD/MM/YYYY'));
			COMMIT;

			R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_I_MAESTRA;

    PROCEDURE SP_U_MAESTRA (
        I_ID                  IN      NUMBER,       
        I_ID_MAESTRA          IN      NUMBER,
        I_ID_TABLA            IN      NUMBER,
        I_ID_ITEM             IN      NUMBER,
        I_ORDEN               IN      NUMBER,
        I_CODIGO              IN      VARCHAR2,
        I_NOMBRE              IN      VARCHAR2,
        I_VALOR               IN      VARCHAR2,
        I_DESCRIPCION         IN      VARCHAR2,
        I_FLAG_ACTIVO         IN      NUMBER,
        I_ID_USUARIO_MOD      IN      NUMBER,
        I_FEC_USUARIO_MOD     IN      VARCHAR2,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
        UPDATE MAESTRA SET ID_MAESTRA=I_ID_MAESTRA, ID_TABLA=I_ID_TABLA, ID_ITEM=I_ID_ITEM, ORDEN=I_ORDEN, CODIGO=I_CODIGO, NOMBRE=I_NOMBRE, VALOR=I_VALOR, DESCRIPCION=I_DESCRIPCION, FLAG_ACTIVO=I_FLAG_ACTIVO, ID_USUARIO_MOD=I_ID_USUARIO_MOD, FEC_USUARIO_MOD=TO_DATE(I_FEC_USUARIO_MOD,'DD/MM/YYYY')
        WHERE ID=I_ID;
        COMMIT;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_U_MAESTRA;

    PROCEDURE SP_D_MAESTRA (
        I_ID                  IN      NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
        UPDATE MAESTRA M SET M.FLAG_ACTIVO=0 WHERE M.ID=I_ID;
        COMMIT;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_D_MAESTRA;

    PROCEDURE SP_L_PRODUCTO (
        I_FEC_INICIO   IN    VARCHAR2,
        I_FEC_FIN      IN    VARCHAR2,
        I_NOMBRE       IN    VARCHAR2,
        R_LISTA        OUT   SYS_REFCURSOR,
        R_CODIGO       OUT   NUMBER,
        R_MENSAJE      OUT   VARCHAR2
    ) AS
        V_SQL VARCHAR2(30000);
    BEGIN
        V_SQL := 'SELECT 
        M.ID,
        M.IDT_UNIDAD_MEDIDA,
        (SELECT O.NOMBRE FROM MAESTRA O WHERE O.ID=M.IDT_UNIDAD_MEDIDA) AS NOM_UNIDAD_MEDIDA,
        M.NOMBRE,
        M.CODIGO,
        M.PRECIO,
        M.FLG_ACTIVO,
        M.ID_USUARIO_CREA,
        M.FEC_USUARIO_CREA,
        M.ID_USUARIO_MOD,
        M.FEC_USUARIO_MOD 
        FROM PRODUCTO M 
        WHERE M.FLG_ACTIVO=1';

        IF ( I_FEC_INICIO IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.FEC_USUARIO_CREA>=TO_DATE(''' || I_FEC_INICIO || ''',''DD/MM/YY'')';
        END IF;

        IF ( I_FEC_FIN IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.FEC_USUARIO_CREA<=TO_DATE(''' || I_FEC_FIN || ''',''DD/MM/YY'')';
        END IF;

        IF ( I_NOMBRE IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.NOMBRE LIKE ''%' || I_NOMBRE || '%''';
        END IF;

        V_SQL := V_SQL||' ORDER BY M.ID DESC';
        OPEN R_LISTA FOR V_SQL;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_L_PRODUCTO;

    PROCEDURE SP_I_PRODUCTO (
        I_IDT_UNIDAD_MEDIDA   IN      NUMBER,
        I_NOMBRE              IN      VARCHAR2,
        I_CODIGO              IN OUT  VARCHAR2,
        I_PRECIO              IN      NUMBER,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_CREA     IN      NUMBER,
        I_FEC_USUARIO_CREA    IN      VARCHAR2,
        R_ID                  OUT     NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
		SELECT SEQ_PRODUCTO.NEXTVAL INTO R_ID FROM DUAL;
		SELECT LPAD(R_ID, 10, '0') INTO I_CODIGO FROM DUAL;
        INSERT INTO PRODUCTO(ID, IDT_UNIDAD_MEDIDA, NOMBRE, CODIGO, PRECIO, FLG_ACTIVO, ID_USUARIO_CREA, FEC_USUARIO_CREA)
        VALUES(R_ID, I_IDT_UNIDAD_MEDIDA, I_NOMBRE, I_CODIGO, I_PRECIO, I_FLG_ACTIVO, I_ID_USUARIO_CREA, TO_DATE(I_FEC_USUARIO_CREA,'DD/MM/YYYY'));
        COMMIT;

        R_CODIGO := SQLCODE;
        R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_I_PRODUCTO;
	
	PROCEDURE SP_U_PRODUCTO (
        I_ID                  IN      NUMBER,       
        I_IDT_UNIDAD_MEDIDA   IN      NUMBER,
        I_NOMBRE              IN      VARCHAR2,
        I_CODIGO              IN	  VARCHAR2,
        I_PRECIO              IN      NUMBER,
        I_FLG_ACTIVO          IN      NUMBER,
        I_ID_USUARIO_MOD      IN      NUMBER,
        I_FEC_USUARIO_MOD     IN      VARCHAR2,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
        UPDATE PRODUCTO SET 
		IDT_UNIDAD_MEDIDA=I_IDT_UNIDAD_MEDIDA, 
		NOMBRE=I_NOMBRE, 
		CODIGO=I_CODIGO, 
		PRECIO=I_PRECIO, 
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
	END SP_U_PRODUCTO;
	
	PROCEDURE SP_D_PRODUCTO (
        I_ID                  IN      NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
        UPDATE PRODUCTO M SET M.FLG_ACTIVO=0 WHERE M.ID=I_ID;
        COMMIT;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_D_PRODUCTO;

	PROCEDURE SP_L_COMPROBANTE (
		I_ID   					IN    NUMBER,
        I_FEC_INICIO   			IN    VARCHAR2,
        I_FEC_FIN      			IN    VARCHAR2,
        I_IDT_TIPO_COMPROBANTE  IN    NUMBER,
		I_FLG_ACTUAL			IN 	  NUMBER,
        R_LISTA        			OUT   SYS_REFCURSOR,
        R_CODIGO       			OUT   NUMBER,
        R_MENSAJE      			OUT   VARCHAR2
    ) AS
        V_SQL VARCHAR2(30000);
    BEGIN
        V_SQL := 'SELECT 
		M.ID,
		M.IDT_TIPO_COMPROBANTE,
		(SELECT O.NOMBRE FROM MAESTRA O WHERE O.ID=M.IDT_TIPO_COMPROBANTE) AS NOM_TIPO_COMPROBANTE,
		M.NOMBRE,
		M.SERIE,
		M.NUMERO,
		M.LONG_SERIE,
		M.LONG_NUMERO,
		M.FLG_ACTUAL,
		M.FLG_ACTIVO,
		M.FLG_USADO,
		M.ID_USUARIO_CREA,
		M.FEC_USUARIO_CREA,
		M.ID_USUARIO_MOD,
		M.FEC_USUARIO_MOD 
		FROM COMPROBANTE M 
		WHERE M.FLG_ACTIVO=1';

		IF ( I_ID IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.ID=' || I_ID;
        END IF;
		
        IF ( I_FEC_INICIO IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.FEC_USUARIO_CREA>=TO_DATE(''' || I_FEC_INICIO || ''',''DD/MM/YY'')';
        END IF;

        IF ( I_FEC_FIN IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.FEC_USUARIO_CREA<=TO_DATE(''' || I_FEC_FIN || ''',''DD/MM/YY'')';
        END IF;

        IF ( (I_IDT_TIPO_COMPROBANTE IS NOT NULL) AND  (I_IDT_TIPO_COMPROBANTE <> 0)) THEN
            V_SQL := V_SQL || ' AND M.IDT_TIPO_COMPROBANTE=' || I_IDT_TIPO_COMPROBANTE;
        END IF;
		
        IF ( I_FLG_ACTUAL IS NOT NULL ) THEN
            V_SQL := V_SQL || ' AND M.FLG_ACTUAL=' || I_FLG_ACTUAL;
        END IF;

        V_SQL := V_SQL||' ORDER BY M.FLG_ACTUAL DESC, M.FEC_USUARIO_CREA DESC';
        OPEN R_LISTA FOR V_SQL;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_L_COMPROBANTE;

    PROCEDURE SP_I_COMPROBANTE (
        I_IDT_TIPO_COMPROBANTE	IN      NUMBER,
        I_NOMBRE            	IN      VARCHAR2,
        I_SERIE             	IN      NUMBER,
        I_NUMERO               	IN      NUMBER,
        I_LONG_SERIE            IN      NUMBER,
        I_LONG_NUMERO           IN      NUMBER,
		I_FLG_ACTUAL            IN OUT  NUMBER,
        I_FLG_ACTIVO            IN      NUMBER,
        I_FLG_USADO         	IN      NUMBER,
        I_ID_USUARIO_CREA     	IN      NUMBER,
        I_FEC_USUARIO_CREA    	IN      VARCHAR2,
        R_ID                  	OUT     NUMBER,
        R_CODIGO              	OUT     NUMBER,
        R_MENSAJE             	OUT     VARCHAR2
    ) AS
	V_CONT NUMBER;
	V_CONT_ACTUAL NUMBER;
    BEGIN
		SELECT COUNT(M.ID) INTO V_CONT FROM COMPROBANTE M WHERE M.SERIE = I_SERIE AND M.IDT_TIPO_COMPROBANTE=I_IDT_TIPO_COMPROBANTE;
		SELECT COUNT(M.ID) INTO V_CONT_ACTUAL FROM COMPROBANTE M WHERE M.FLG_ACTUAL = 1;
		
		IF V_CONT_ACTUAL = 0 THEN
			I_FLG_ACTUAL := 1;
		ELSE
			I_FLG_ACTUAL := 0;
		END IF;

		IF V_CONT > 0 THEN
			R_CODIGO := -1;
			R_MENSAJE := 'YA EXISTE EL REGISTRO DE SERIE INGRESADO';
		ELSE
			SELECT SEQ_COMPROBANTE.NEXTVAL INTO R_ID FROM DUAL;
			INSERT INTO COMPROBANTE(ID,IDT_TIPO_COMPROBANTE, NOMBRE, SERIE, NUMERO, LONG_SERIE, LONG_NUMERO, FLG_ACTUAL, FLG_ACTIVO, FLG_USADO, ID_USUARIO_CREA, FEC_USUARIO_CREA)
			VALUES(R_ID, I_IDT_TIPO_COMPROBANTE, I_NOMBRE, I_SERIE, I_NUMERO, I_LONG_SERIE, I_LONG_NUMERO, I_FLG_ACTUAL,I_FLG_ACTIVO, I_FLG_USADO, I_ID_USUARIO_CREA, TO_DATE(I_FEC_USUARIO_CREA,'DD/MM/YYYY'));				

			R_CODIGO := SQLCODE;
			R_MENSAJE := SQLERRM;
		END IF;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_I_COMPROBANTE;

    PROCEDURE SP_U_COMPROBANTE (
        I_ID                  	IN      NUMBER,       
        I_IDT_TIPO_COMPROBANTE	IN      NUMBER,
        I_NOMBRE            	IN      VARCHAR2,
        I_SERIE             	IN      NUMBER,
        I_NUMERO               	IN      NUMBER,
        I_LONG_SERIE            IN      NUMBER,
        I_LONG_NUMERO           IN      NUMBER,
		I_FLG_ACTUAL            IN      NUMBER,
        I_FLG_ACTIVO            IN      NUMBER,
        I_FLG_USADO         	IN      NUMBER,
        I_ID_USUARIO_MOD      	IN      NUMBER,
        I_FEC_USUARIO_MOD     	IN      VARCHAR2,
        R_CODIGO              	OUT     NUMBER,
        R_MENSAJE             	OUT     VARCHAR2
    ) AS
	V_FLG_USADO NUMBER;
	V_CONT NUMBER;
    BEGIN
		SELECT M.FLG_USADO INTO V_FLG_USADO FROM COMPROBANTE M WHERE M.ID = I_ID;

		IF V_FLG_USADO <> 0 THEN
			R_CODIGO := -1;
			R_MENSAJE := 'NO SE PUEDE EDITAR, PORQUE YASE GENERARON TICKETS DE ESTE REGISTRO';
		ELSE
			SELECT COUNT(M.ID) INTO V_CONT FROM COMPROBANTE M WHERE M.ID != I_ID AND M.SERIE = I_SERIE AND M.IDT_TIPO_COMPROBANTE=I_IDT_TIPO_COMPROBANTE;
			IF V_CONT > 0 THEN
				R_CODIGO := -1;
				R_MENSAJE := 'YA EXISTE EL REGISTRO DE SERIE INGRESADO';
			ELSE
				UPDATE COMPROBANTE SET 
				IDT_TIPO_COMPROBANTE=I_IDT_TIPO_COMPROBANTE, 
				NOMBRE=I_NOMBRE, 
				SERIE=I_SERIE, 
				NUMERO=I_NUMERO, 
				LONG_SERIE=I_LONG_SERIE, 
				LONG_NUMERO=I_LONG_NUMERO, 
				FLG_ACTUAL=I_FLG_ACTUAL, 
				FLG_ACTIVO=I_FLG_ACTIVO, 
				FLG_USADO=I_FLG_USADO,
				ID_USUARIO_MOD=I_ID_USUARIO_MOD, 
				FEC_USUARIO_MOD=TO_DATE(I_FEC_USUARIO_MOD,'DD/MM/YYYY')
				WHERE ID=I_ID;
				COMMIT;

				R_CODIGO := SQLCODE;
				R_MENSAJE := SQLERRM;
			END IF;
		END IF;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_U_COMPROBANTE;

    PROCEDURE SP_D_COMPROBANTE (
        I_ID                  IN      NUMBER,
        R_CODIGO              OUT     NUMBER,
        R_MENSAJE             OUT     VARCHAR2
    ) AS
    BEGIN
        UPDATE COMPROBANTE M SET M.FLG_ACTIVO=0 WHERE M.ID=I_ID;
        COMMIT;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_D_COMPROBANTE;
	
	PROCEDURE SP_EST_COMPROBANTE_ACTUAL (
        I_ID                  	IN      NUMBER,
		I_IDT_TIPO_COMPROBANTE  IN    	NUMBER,
        R_CODIGO              	OUT     NUMBER,
        R_MENSAJE             	OUT     VARCHAR2
    ) AS
    BEGIN
        UPDATE COMPROBANTE M SET M.FLG_ACTUAL=0 WHERE M.IDT_TIPO_COMPROBANTE=I_IDT_TIPO_COMPROBANTE;
		UPDATE COMPROBANTE M SET M.FLG_ACTUAL=1 WHERE M.ID=I_ID;
        COMMIT;

        R_CODIGO := SQLCODE;
		R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
	END SP_EST_COMPROBANTE_ACTUAL;

END PCK_PPOS_ADMINISTRACION;
/