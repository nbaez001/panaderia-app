package com.besoft.panaderia.dto.request.array;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.besoft.panaderia.dto.request.HonorarioDetalleRequest;
import com.besoft.panaderia.util.ConexionUtil;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Component
public class HonorarioDetalleArray {

	@Autowired
	ConexionUtil conexionUtil;

	public ARRAY toArray(List<HonorarioDetalleRequest> lista) throws Exception {
		Connection con = conexionUtil.getConexion();

		STRUCT[] structura = new STRUCT[lista.size()];
		StructDescriptor structDesc = StructDescriptor.createDescriptor("T_HONORARIO_DETALLE", con);
		ArrayDescriptor arrayDesc = ArrayDescriptor.createDescriptor("TB_HONORARIO_DETALLE", con);

		int cont = 0;
		for (HonorarioDetalleRequest p : lista) {
			Map<String, Object> map = new HashMap<>();
			map.put("ID_TIPO_INSUMO", p.getTipoInsumo().getId());
			map.put("CANTIDAD", p.getCantidad());
			map.put("TARIFA", p.getTarifa());
			map.put("SUBTOTAL", p.getSubtotal());
			map.put("FLG_ACTIVO", p.getFlgActivo());

			STRUCT s = new STRUCT(structDesc, con, map);
			structura[cont] = s;
			cont++;
		}
		ARRAY arrayOfType = new ARRAY(arrayDesc, con, structura);
		return arrayOfType;
	}
}
