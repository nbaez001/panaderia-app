package com.besoft.panaderia.dto.request.array;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.besoft.panaderia.dto.request.DetalleVentaRequest;
import com.besoft.panaderia.util.ConexionUtil;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Component
public class DetalleVentaArray {

	@Autowired
	ConexionUtil conexionUtil;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public ARRAY toArray(List<DetalleVentaRequest> lista) throws Exception {
		Connection con = conexionUtil.getConexion();

		STRUCT[] structura = new STRUCT[lista.size()];
		StructDescriptor structDesc = StructDescriptor.createDescriptor("T_DETALLE_VENTA", con);
		ArrayDescriptor arrayDesc = ArrayDescriptor.createDescriptor("TB_DETALLE_VENTA", con);

		int cont = 0;
		for (DetalleVentaRequest p : lista) {
			Map<String, Object> map = new HashMap<>();
			map.put("ID_PRODUCTO", p.getIdProducto());
			map.put("CANTIDAD", p.getCantidad());
			map.put("PRECIO", p.getPrecio());
			map.put("SUBTOTAL", p.getSubtotal());
			map.put("FLG_ACTIVO", p.getFlagActivo());

			STRUCT s = new STRUCT(structDesc, con, map);
			structura[cont] = s;
			cont++;
		}
		ARRAY arrayOfType = new ARRAY(arrayDesc, con, structura);
		return arrayOfType;
	}

}