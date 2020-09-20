CREATE OR REPLACE PACKAGE PCK_PSEG_AUTENTICACION AS

    PROCEDURE SP_BUSCAR_USUARIO (
        I_USERNAME  IN   VARCHAR2,
        R_RESULT     OUT   SYS_REFCURSOR,
        R_RESULT_DET OUT   SYS_REFCURSOR,
        R_CODIGO    OUT   NUMBER,
        R_MENSAJE   OUT   VARCHAR2
    );
	
	PROCEDURE SP_L_PERMISO (
        I_ID_USUARIO   IN   NUMBER,
        R_RESULT       OUT   SYS_REFCURSOR,
        R_CODIGO       OUT   NUMBER,
        R_MENSAJE      OUT   VARCHAR2
    );
	
END PCK_PSEG_AUTENTICACION;
/     
CREATE OR REPLACE PACKAGE BODY PCK_PSEG_AUTENTICACION AS

    PROCEDURE SP_BUSCAR_USUARIO (
        I_USERNAME  IN   VARCHAR2,
        R_RESULT     OUT   SYS_REFCURSOR,
        R_RESULT_DET OUT   SYS_REFCURSOR,
        R_CODIGO    OUT   NUMBER,
        R_MENSAJE   OUT   VARCHAR2
    ) AS
    BEGIN
        OPEN R_RESULT FOR
            SELECT
                    U.ID,
                    U.NOMBRE,
                    U.APE_PATERNO,
                    U.APE_MATERNO,
                    U.USERNAME,
                    U.PASSWORD,
                    U.EMAIL,
                    U.TELEFONO
            FROM PSEG.USUARIO U
            WHERE U.USERNAME=I_USERNAME;
       
        OPEN R_RESULT_DET FOR
            SELECT R.ID,R.NOMBRE FROM PSEG.USUARIO U
            LEFT JOIN PSEG.USUARIO_ROL UR ON UR.ID_USUARIO = U.ID
            LEFT JOIN PSEG.ROL R ON R.ID = UR.ID_ROL
            WHERE U.USERNAME=I_USERNAME;

        R_CODIGO := SQLCODE;
        R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_BUSCAR_USUARIO;
	
	PROCEDURE SP_L_PERMISO (
        I_ID_USUARIO   IN    NUMBER,
        R_RESULT       OUT   SYS_REFCURSOR,
        R_CODIGO       OUT   NUMBER,
        R_MENSAJE      OUT   VARCHAR2
    ) AS
    BEGIN
        OPEN R_RESULT FOR
            SELECT
			DISTINCT(P.ID) AS ID,
			P.ID_PADRE,
			P.NOMBRE,
			P.RUTA,
			P.ORDEN
            FROM PSEG.USUARIO U
            INNER JOIN PSEG.USUARIO_ROL UR ON UR.ID_USUARIO = U.ID
            INNER JOIN PSEG.ROL R ON R.ID = UR.ID_ROL
			INNER JOIN PSEG.ROL_PERMISO RP ON RP.ID_ROL=R.ID
			INNER JOIN PSEG.PERMISO P ON P.ID=RP.ID_PERMISO
            WHERE U.ID=I_ID_USUARIO 
			ORDER BY P.ORDEN ASC;

        R_CODIGO := SQLCODE;
        R_MENSAJE := SQLERRM;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            R_CODIGO := SQLCODE;
            R_MENSAJE := SQLERRM;
    END SP_L_PERMISO;

END PCK_PSEG_AUTENTICACION;