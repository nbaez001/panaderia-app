package com.besoft.panaderia.dto.request.array;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.besoft.panaderia.dto.request.HonorarioInsumoRequest;
import com.besoft.panaderia.util.ConexionUtil;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Component
public class HonorarioInsumoArray {
	@Autowired
	ConexionUtil conexionUtil;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public ARRAY toArray(List<HonorarioInsumoRequest> lista) throws Exception {
		Connection con = conexionUtil.getConexion();

		STRUCT[] structura = new STRUCT[lista.size()];
		StructDescriptor structDesc = StructDescriptor.createDescriptor("T_HONORARIO_INSUMO", con);
		ArrayDescriptor arrayDesc = ArrayDescriptor.createDescriptor("TB_HONORARIO_INSUMO", con);

		int cont = 0;
		for (HonorarioInsumoRequest p : lista) {
			Map<String, Object> map = new HashMap<>();
			map.put("ID_INSUMO", p.getIdInsumo());
			map.put("FLG_ACTIVO", p.getFlgActivo());

			STRUCT s = new STRUCT(structDesc, con, map);
			structura[cont] = s;
			cont++;
		}
		ARRAY arrayOfType = new ARRAY(arrayDesc, con, structura);
		return arrayOfType;
	}
}
